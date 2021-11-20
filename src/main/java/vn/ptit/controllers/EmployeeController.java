package vn.ptit.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import vn.ptit.models.Employee;

@Controller
@RequestMapping("/admin/manage/employee")
public class EmployeeController {
	private RestTemplate rest = new RestTemplate();

	@Value("${domain.services.name}")
	private String domainServices;

	@GetMapping()
	public String viewManageEmployee(Model model) {
		List<Employee> employees = Arrays
				.asList(rest.getForObject("http://localhost:6969/rest/api/employee/find-all", Employee[].class));
		model.addAttribute("employees", employees);
		return "manage_employee";
	}

	@GetMapping("/add")
	public String viewAddEmployee(Model model) {
		model.addAttribute("employee", new Employee());
		return "add_employee";
	}

	@PostMapping("/add")
	public String addEmployee(@ModelAttribute("employee") Employee employee,
			@RequestParam("dob") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dob, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		employee.setDateOfBirth(dob);
		rest.postForObject(domainServices + "/rest/api/employee/insert", employee, Employee.class);
		return "redirect:/admin/manage/employee";
	}

	@GetMapping("/edit/{id}")
	public String viewEditEmployee(@PathVariable("id") int id, Model model) {
		Employee employee = rest.getForObject(domainServices + "/rest/api/employee/find-by-id/" + id, Employee.class);
		model.addAttribute("employee", employee);
		return "edit_employee";
	}

	@PostMapping("/edit")
	public String editEmployee(@ModelAttribute("employee") Employee employee,
			@RequestParam("dob") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dob, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		String password = req.getParameter("password_");
		employee.setDateOfBirth(dob);
		employee.getAccount().setPassword(password);
		rest.postForObject(domainServices + "/rest/api/employee/update", employee, Employee.class);
		return "redirect:/admin/manage/employee";
	}

	@GetMapping("/delete/{id}")
	public String deleteEmployee(@PathVariable("id") int id, Model model) {
		rest.delete(domainServices + "/rest/api/employee/delete-by-id/" + id);
		return "redirect:/admin/manage/employee";
	}

}
