package chatforeveryone.controller;

import java.util.Set;

import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.RequestMethod;

import chatforeveryone.entity.User;
import chatforeveryone.service.UserService;

@Controller
public class MainController {

	@Autowired
	private UserService userService;

	@RequestMapping("/")
	public String home() {
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

	@GetMapping("/registration")
	public String registration(Model model)
	{
		model.addAttribute("user", new User());
		return "index/registration";
	}

	@PostMapping("/registration")
	public String reg(@ModelAttribute User user, Model model)
	{
		String registrationError = userService.registerUser(user);
		model.addAttribute("user", new User());

		if (registrationError.isEmpty())
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
	public String activation(@PathVariable("code") String code, HttpServletResponse response)
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