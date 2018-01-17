package chatforeveryone.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import chatforeveryone.entity.Relationship;
import chatforeveryone.entity.Role;
import chatforeveryone.entity.User;
import chatforeveryone.repository.RelationshipRepository;
import chatforeveryone.repository.RoleRepository;
import chatforeveryone.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Value("${email.enable.sending}")
	private boolean emailEnableSending;	

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private RelationshipRepository relationshipRepository;

	@Value("${role.default}")
	private String USER_ROLE;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}

		return new UserDetailsImpl(user);
	}

	public String registerUser(User userToRegister)
	{
		String email, name, password;
		email = userToRegister.getEmail();
		name = userToRegister.getNickName();
		password = userToRegister.getPassword();
		
		User userCheckEmail = userRepository.findByEmail(userToRegister.getEmail());
		User userCheckNickName = userRepository.findByNickName(userToRegister.getNickName());
		
		if(email == null || email.equals("") || email.length() < 5 || email.length() > 100 || !email.contains("@") || !email.contains("."))
		{
			return "Nem megfelelő e-mail!";
		}
		else if(userCheckEmail != null)
		{
			return "Az e-mail már foglalt!";
		}
		else if(name == null || name.equals("") || name.length() < 3 || name.length() > 50)
		{
			return "Nem megfelelő felhasználónév!";
		}
		else if(userCheckNickName != null)
		{
			return "Az felhasználónév már foglalt!";
		}
		else if(password == null || password.equals("") || password.length() < 3 || password.length() > 100)
		{
			return "Nem megfelelő jelszó!";
		}
		else
		{		
			Role userRole = roleRepository.findByRole(USER_ROLE);
			
			if (userRole == null)
			{
				roleRepository.save(new Role(USER_ROLE));
				userRole = roleRepository.findByRole(USER_ROLE);
			}		
			
			String generatedKey = generateKey();
			
			userToRegister.setPassword(hashPassword(password));
			userToRegister.setRole(userRole);			
			userToRegister.setActivationCode(generatedKey);
			userToRegister.setRegistrationDate(new Date());
			
			userRepository.save(userToRegister);
			
			sendRegistrationEmail(email, name, generatedKey);		
			
			return "";
		}
	}
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public void sendRegistrationEmail(String email, String name, String code)
	{
		if(emailEnableSending)
			emailService.sendMessage(email, name, code);		
	}

	public List<FriendResponse> sendFriendsByEmail(String email) {
		
		List<User> friends = userRepository.findFriendsByEmail(email);
		List<FriendResponse> friendResponse = new ArrayList<>();
		
		for(User friend : friends)
		{
			friendResponse.add(new FriendResponse(friend.getEmail(), friend.getNickName()));
		}
		
		return friendResponse;
	}

	public Set<User> findWhoUserSentButNotYetElfogadva(String email) {
		return userRepository.findWhoUserSentButNotYetElfogadva(email);
	}

	public Set<User> findWhoWaitsForMyElfogadva(String email) {
		return userRepository.findWhoWaitsForMyElfogadva(email);
	}

	public String makeRelationship(String _kuldo, String _fogado) {
		User kuldo_, fogado_;

		kuldo_ = userRepository.findByEmail(_kuldo);
		fogado_ = userRepository.findByEmail(_fogado);

		if (fogado_ == null)
			return "Ilyen user nem létezik!";

		String kuldo = kuldo_.getEmail();
		String fogado = fogado_.getEmail();

		if (kuldo.equals(fogado))
			return "Saját magadat nem veheted fel!";

		Relationship rship = relationshipRepository.checkForRelationshipBetweenTwo(kuldo, fogado);

		if (rship != null) {
			if (kuldo.equals(rship.getFogado().getEmail()))
				updateRelationshipToElfogadva(kuldo, fogado);
			else
				return "Már küldtél neki!";
		} else
			createNewRelationship(kuldo, fogado);

		return "";
	}

	public void updateRelationshipToElfogadva(String a, String b) {
		Relationship rship = relationshipRepository.findRelationshipByTwoUser(a, b);
		rship.setElfogadva(true);
		relationshipRepository.save(rship);
	}

	public void createNewRelationship(String ki, String kit) {
		relationshipRepository.createNewRelationship(ki, kit);
	}

	public Set<User> findRelationshipsByEmail(String email) {
		return userRepository.findRelationshipsByEmail(email);
	}

	public String hashPassword(String password) {
		return new BCryptPasswordEncoder().encode(password);
	}

	public String generateKey() {
		Random random = new Random();
		char[] word = new char[50];
		for (int j = 0; j < word.length; j++) {
			word[j] = (char) ('a' + random.nextInt(26));
		}

		return new String(word);
	}

	public String userActivationCode(String code) {
		User user = userRepository.findByActivationCode(code);
		if (user == null)
			return "noresult";

		user.setActivationCode("");
		userRepository.save(user);
		return "ok";
	}
}