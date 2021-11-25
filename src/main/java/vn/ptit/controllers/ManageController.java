package vn.ptit.controllers;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import vn.ptit.models.Employee;

@Controller
@RequestMapping("/admin")
public class ManageController {
	private RestTemplate rest = new RestTemplate();
	@Autowired
	PasswordEncoder passwordEncoder;

	@Value("${domain.services.name}")
	private String domainServices;

	@GetMapping
	public String viewHomeManage() {
		return "manage";
	}

	@GetMapping("/profile")
	public String viewProfile(Model model, HttpServletRequest req, HttpServletResponse resp) {
		String username = (String) req.getSession().getAttribute("usernameEmployee");
		Employee employee = rest.getForObject(domainServices + "/rest/api/employee/get/" + username, Employee.class);
		model.addAttribute("employee", employee);
		return "employee/detail_employee";
	}

}
