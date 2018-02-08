package chatforeveryone.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class UserService implements UserDetailsService
{

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
	
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
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
		name = userToRegister.getName();
		password = userToRegister.getPassword();
		
		if(!isValidEmail(email))
		{			
			return "Nem megfelelő e-mail!";
		}
		
		User userCheckEmail = userRepository.findByEmail(userToRegister.getEmail());
		User userCheckNickName = userRepository.findByName(userToRegister.getName());
		
		if(userCheckEmail != null)
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
		else if(password == null || password.equals("") || password.length() < 5 || password.length() > 100)
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
			
			userRepository.save(userToRegister);
			
			sendRegistrationEmail(email, name, generatedKey);		
			
			return "";
		}
	}	
	
	public static boolean isValidEmail(String email)
	{
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();			
	}
	
	public User findByEmail(String email)
	{
		return userRepository.findByEmail(email);
	}
	
	public void sendRegistrationEmail(String email, String name, String code)
	{
		if(emailEnableSending)
			emailService.sendMessage(email, name, code);		
	}
	
	public boolean isThereRelationshipBetweenTwo(String email1, String email2)
	{
		Relationship rship = relationshipRepository.findRelationshipBetweenTwoUser(email1, email2);
		
		return (rship != null && rship.isAccepted());
	}

	public List<FriendResponse> findFriendsByEmail(String email)
	{
		List<User> friends = userRepository.findFriendsByEmail(email);
		List<FriendResponse> friendResponse = new ArrayList<>();
		
		for(User friend : friends)
		{
			friendResponse.add(new FriendResponse(friend.getEmail(), friend.getName()));
		}
		
		return friendResponse;
	}

	public Set<User> findWhoAuthUserSentButNotYetAccepted(String email)
	{
		return userRepository.findWhoAuthUserSentButNotYetAccepted(email);
	}

	public Set<User> findWhoWaitsForAuthUserAccept(String email)
	{
		return userRepository.findWhoWaitsForAuthUserAccept(email);
	}

	public String makeRelationship(String email1, String email2)
	{
		User sender = userRepository.findByEmail(email1);
		User receiver = userRepository.findByEmail(email2);

		if (receiver == null)
			return "Ilyen user nem létezik!";

		if (sender.getEmail().equals(receiver.getEmail()))
			return "Saját magadat nem veheted fel!";

		Relationship rship = relationshipRepository.findRelationshipBetweenTwoUser(sender.getEmail(), receiver.getEmail());

		if (rship != null)
		{
			if(rship.isAccepted())
				return "Már kapcsolatban vagytok!";
			
			if(sender.getEmail().equals(rship.getReceiver().getEmail()))
				updateRelationshipToAccepted(sender.getEmail(), receiver.getEmail());
			else
				return "Már küldtél neki!";
		}
		else
			createNewRelationship(sender.getEmail(), receiver.getEmail());

		return "";
	}

	public void updateRelationshipToAccepted(String email1, String email2)
	{
		Relationship rship = relationshipRepository.findRelationshipBetweenTwoUser(email1, email2);
		rship.setAccepted(true);
		relationshipRepository.save(rship);
	}

	public void createNewRelationship(String email1, String email2)
	{
		relationshipRepository.createNewRelationship(email1, email2);
	}

	public Set<User> findRelationshipsByEmail(String email)
	{
		return userRepository.findRelationshipsByEmail(email);
	}

	public String hashPassword(String password)
	{
		return new BCryptPasswordEncoder().encode(password);
	}

	public String generateKey()
	{
		char[] key = new char[50];
        for (int i = 0; i < key.length; i++)
        {
            key[i] = (char) ('a' + new Random().nextInt(26));
        }

		return new String(key);
	}

	public String userActivationCode(String code)
	{
		User user = userRepository.findByActivationCode(code);
		if (user == null)
			return "";

		user.setActivationCode("");
		userRepository.save(user);
		return "ok";
	}
}