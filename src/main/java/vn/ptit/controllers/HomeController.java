package vn.ptit.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import vn.ptit.models.Employee;

@Controller
@RequestMapping("/")
public class HomeController {
	private RestTemplate rest = new RestTemplate();
	@Autowired PasswordEncoder passwordEncoder;

	@Value("${domain.services.name}")
	private String domainServices;
	
	@GetMapping()
	public String home() {
		return "home";
	}
	
	@GetMapping("/login")
	public String viewLogin() {
		return "login";
	}
	
	@PostMapping("/perform-login")
	public String loginEmployee(Model model, HttpServletRequest req, HttpServletResponse resp) {
		String username = req.getParameter("username");
		String password = req.getParameter("password");

		Employee employee = rest.getForObject(domainServices+"/rest/api/employee/get/"+ username,
				Employee.class);
		if(employee==null) {
			return "login";
		}
		boolean flag = passwordEncoder.matches(password, employee.getAccount().getPassword());
		if (flag) {
			UserDetails userDetail = buildUser(employee);
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail,
					null, userDetail.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			req.getSession().setAttribute("usernameEmployee", employee.getAccount().getUsername());
			return "redirect:/admin";
		}
		
		return "login";
	}

	private UserDetails buildUser(Employee employee) {
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(employee.getPosition()));

		UserDetails userDetail = new User(employee.getAccount().getUsername(), employee.getAccount().getPassword(), enabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked, authorities);

		return userDetail;
	}
	
}
