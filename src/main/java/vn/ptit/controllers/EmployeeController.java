package vn.ptit.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	@Autowired
	PasswordEncoder passwordEncoder;

	@Value("${domain.services.name}")
	private String domainServices;

	@GetMapping()
	public String viewManageEmployee(Model model, HttpServletRequest req, HttpServletResponse resp) {
		Map<String, Object> map = new HashMap<String, Object>();

		int page = 1;
		if (req.getParameter("page") != null) {
			page = Integer.parseInt(req.getParameter("page"));
			map.put("page", page);
			model.addAttribute("page", page);
		}
		if (req.getParameter("keyEmployee") != null) {
			String keyEmployee = req.getParameter("keyEmployee");
			map.put("keyEmployee", keyEmployee);
			model.addAttribute("keyEmployee", keyEmployee);
		}
		if (req.getParameter("fromDate") != null) {
			String fromDate = req.getParameter("fromDate");
			map.put("fromDate", fromDate);
			model.addAttribute("fromDate", fromDate);
		}
		if (req.getParameter("toDate") != null) {
			String toDate = req.getParameter("toDate");
			map.put("toDate", toDate);
			model.addAttribute("toDate", toDate);
		}
		if (req.getParameter("sort") != null) {
			String sort = req.getParameter("sort");
			map.put("sort", sort);
			model.addAttribute("sort", sort);
		}

		List<Employee> employees = Arrays
				.asList(rest.postForObject(domainServices + "/rest/api/employee/find-all", map, Employee[].class));
		model.addAttribute("employees", employees);
		return "employee/manage_employee";
	}

	@GetMapping("/add")
	public String viewAddEmployee(Model model) {
		model.addAttribute("employee", new Employee());
		return "employee/add_employee";
	}

	@PostMapping("/add")
	public String addEmployee(@ModelAttribute("employee") Employee employee,
			@RequestParam("dob") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dob, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		String password = req.getParameter("password_");
		employee.getAccount().setPassword(password);
		employee.setDateOfBirth(dob);
		
		Map<String, Object> map = new HashMap<String, Object>();
		Employee employeeCheck = rest.getForObject(
				domainServices + "/rest/api/employee/get/" + employee.getAccount().getUsername(), Employee.class);

		if (employeeCheck != null) {
			model.addAttribute("status", "faileTenBiTrung");
			return "employee/add_employee";
		}

		employee.getAccount().setPassword(passwordEncoder.encode(employee.getAccount().getPassword()));
		employee.setStatus(true);
		rest.postForObject(domainServices + "/rest/api/employee/insert", employee, Employee.class);
		return "redirect:/admin/manage/employee";
	}

	@GetMapping("/edit/{id}")
	public String viewEditEmployee(@PathVariable("id") int id, Model model) {
		Employee employee = rest.getForObject(domainServices + "/rest/api/employee/find-by-id/" + id, Employee.class);
		model.addAttribute("employee", employee);
		return "employee/edit_employee";
	}

	@PostMapping("/edit")
	public String editEmployee(@ModelAttribute("employee") Employee employee,
			@RequestParam("dob") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dob, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		String password = req.getParameter("password_");
		Employee emp = rest.getForObject(
				domainServices + "/rest/api/employee/get/" + employee.getAccount().getUsername(), Employee.class);

		if (emp.getAccount().getPassword().equalsIgnoreCase(password))
			employee.getAccount().setPassword(password);
		else
			employee.getAccount().setPassword(passwordEncoder.encode(password));

		employee.setDateOfBirth(dob);
		employee.setStatus(true);

		rest.postForObject(domainServices + "/rest/api/employee/update", employee, Employee.class);
		return "redirect:/admin/manage/employee";
	}

	@GetMapping("/delete/{id}")
	public String deleteEmployee(@PathVariable("id") int id, Model model) {
		rest.delete(domainServices + "/rest/api/employee/delete-by-id/" + id);
		return "redirect:/admin/manage/employee";
	}

}
