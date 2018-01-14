package chatforeveryone.service;

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

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public String[] registerUser(User userToRegister) {
		User userCheck = userRepository.findByEmail(userToRegister.getEmail());
		String[] ret = new String[2];
		if (userCheck != null) {
			ret[0] = "emailAlreadyRegistered";
			ret[1] = "";
			return ret;
		} else {
			Role userRole = roleRepository.findByRole(USER_ROLE);
			if (userRole != null) {
				userToRegister.setRole(userRole);
			} else {
				Role newUserRole = new Role(USER_ROLE);
				roleRepository.save(newUserRole);
				userToRegister.setRole(newUserRole);
			}

			String hashPassword = hashPassword(userToRegister.getPassword());
			userToRegister.setPassword(hashPassword);

			String generatedKey = generateKey();
			userToRegister.setActivationCode(generatedKey);

			userToRegister.setRegistrationDate(new Date());

			userRepository.save(userToRegister);

			ret[0] = "successfulRegistration";
			ret[1] = generatedKey;

			return ret;
		}
	}

	public List<String> findFriendsByEmail(String email) {
		return userRepository.findFriendsByEmail(email);
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