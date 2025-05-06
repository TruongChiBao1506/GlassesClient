package iuh.fit.se.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	@GetMapping("/home")
	public String goHome() {
		return "home";
	}
	@GetMapping("/about")
	public String about() {
		return "about";
	}
}
