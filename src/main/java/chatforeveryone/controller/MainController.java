package chatforeveryone.controller;

import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import chatforeveryone.entity.User;
import chatforeveryone.service.EmailService;
import chatforeveryone.service.UserService;

@Controller
public class MainController {

	@Value("${email.enable.sending}")
	private boolean emailEnableSending;

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@RequestMapping("/")
	public String home(Model model) {
		return "main/chat";
	}

	@RequestMapping(path = "/kapcsolatok", method = RequestMethod.GET)
	public String kapcsolatok(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = auth.getName();

		Set<User> requested = userService.findWhoUserSentButNotYetElfogadva(user);
		model.addAttribute("requested", requested);

		Set<User> waitsForMe = userService.findWhoWaitsForMyElfogadva(user);
		model.addAttribute("waitsForMe", waitsForMe);

		model.addAttribute("hiba", "");

		return "main/kapcsolatok";
	}

	@RequestMapping(path = "/kapcsolatok", method = RequestMethod.POST)
	public String kapcsolatok(@ModelAttribute("email") String email, HttpServletResponse response, Model model) {
		if (email != null && !email.equals("")) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String user = auth.getName();

			String result = userService.makeRelationship(user, email);
			model.addAttribute("hiba", result);

			Set<User> requested = userService.findWhoUserSentButNotYetElfogadva(user);
			model.addAttribute("requested", requested);

			Set<User> waitsForMe = userService.findWhoWaitsForMyElfogadva(user);
			model.addAttribute("waitsForMe", waitsForMe);

			return "main/kapcsolatok";
		}

		return "main/kapcsolatok";
	}

	@RequestMapping(path = "/registration", method = RequestMethod.GET)
	public String registration(Model model) {

		model.addAttribute("user", new User());

		return "index/registration";
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public String reg(@ModelAttribute User user, Model model) {
		String[] eredmeny = userService.registerUser(user);

		if (emailEnableSending && eredmeny[0] == "successfulRegistration")
			emailService.sendMessage(user.getEmail(), user.getNickName(), eredmeny[1]);

		model.addAttribute("user", new User());

		if (eredmeny[0] == "successfulRegistration") {
			model.addAttribute("msg", "Sikeres regisztráció!");
			return "index/login";
		} else if (eredmeny[0] == "emailAlreadyRegistered") {
			model.addAttribute("msg", "Ez az e-mail már foglalt!");
			return "index/registration";
		} else
			return "index/registration";
	}

	@RequestMapping("/successfulActivation")
	public String activated(Model model) {
		model.addAttribute("msg", "Sikeres aktiválás!");
		return "index/login";
	}

	@RequestMapping(path = "/activation/{code}", method = RequestMethod.GET)
	public String activation(@PathVariable("code") String code, HttpServletResponse response) {
		if (code != null && !code.equals("")) {
			String result = userService.userActivationCode(code);
			if (result.equals("ok"))
				return "redirect:/successfulActivation";
			else
				return "redirect:/";
		}

		return "redirect:/";
	}

}
