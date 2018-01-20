package chatforeveryone.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import chatforeveryone.entity.User;
import chatforeveryone.service.UserService;

@Controller
public class MainController {

	@Autowired
	private UserService userService;

	@RequestMapping("/")
	public String home()
	{
		return "main/chat";
	}

	@GetMapping("/kapcsolatok")
	public String kapcsolatok(Model model)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = auth.getName();

		Set<User> requested = userService.findWhoUserSentButNotYetElfogadva(user);
		model.addAttribute("requested", requested);

		Set<User> waitsForMe = userService.findWhoWaitsForMyElfogadva(user);
		model.addAttribute("waitsForMe", waitsForMe);

		model.addAttribute("hiba", "");

		return "main/kapcsolatok";
	}

	@PostMapping("/kapcsolatok")
	public String kapcsolatok(@ModelAttribute("email") String email, Model model)
	{
		if(UserService.isValidEmail(email))
		{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String user = auth.getName();

			String result = userService.makeRelationship(user, email);
			model.addAttribute("hiba", result);

			Set<User> requested = userService.findWhoUserSentButNotYetElfogadva(user);
			model.addAttribute("requested", requested);

			Set<User> waitsForMe = userService.findWhoWaitsForMyElfogadva(user);
			model.addAttribute("waitsForMe", waitsForMe);
		}

		return "main/kapcsolatok";
	}

	@GetMapping("/registration")
	public String registration(Model model)
	{
		model.addAttribute("user", new User());
		return "index/registration";
	}

	@PostMapping("/registration")
	public String registration(@ModelAttribute User user, Model model)
	{
		String registrationError = userService.registerUser(user);
		model.addAttribute("user", new User());

		if(registrationError.isEmpty())
		{
			model.addAttribute("msg", "Sikeres regisztráció! (Az aktiváló kódot elküldtük az e-mail címedre.)");
			return "index/login";
		}
		else
		{
			model.addAttribute("msg", registrationError);
			return "index/registration";
		}
	}

	@RequestMapping("/successfulActivation")
	public String activated(Model model)
	{
		model.addAttribute("msg", "Sikeres aktiválás!");
		return "index/login";
	}

	@GetMapping("/activation/{code}")
	public String activation(@PathVariable("code") String code)
	{
		if (code != null && !code.isEmpty())
		{
			String result = userService.userActivationCode(code);
			if (result.equals("ok"))
				return "redirect:/successfulActivation";
			else
				return "redirect:/";
		}

		return "redirect:/";
	}
}