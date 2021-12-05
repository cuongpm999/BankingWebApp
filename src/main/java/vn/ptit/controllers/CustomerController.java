package vn.ptit.controllers;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import vn.ptit.models.Customer;

@Controller
@RequestMapping("/admin/manage/customer")
public class CustomerController {

	private RestTemplate rest = new RestTemplate();

	@Value("${domain.services.name}")
	private String domainServices;

	@GetMapping
	public String viewCustomer(Model model, HttpServletRequest req, HttpServletResponse resp) {
		Map<String, Object> map = new HashMap<String, Object>();

		int page = 1;
		if (req.getParameter("page") != null) {
			page = Integer.parseInt(req.getParameter("page"));
			map.put("page", page);
			model.addAttribute("page", page);
		}
		if (req.getParameter("keyCustomer") != null) {
			String keyCustomer = req.getParameter("keyCustomer");
			map.put("keyCustomer", keyCustomer);
			model.addAttribute("keyCustomer", keyCustomer);
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
		List<Customer> customers = Arrays
				.asList(rest.postForObject(domainServices + "/rest/api/customer/find-all", map, Customer[].class));
		model.addAttribute("customers", customers);
		return "customer/manage_customer";
	}

	@GetMapping(value = "/add")
	public String addCustomer(Model model, HttpServletRequest req, HttpServletResponse resp) {
		model.addAttribute("customer", new Customer());
		return "customer/add_customer";
	}

	@PostMapping(value = "/add")
	public String addCustomerPost(@ModelAttribute("customer") Customer customer,
			@RequestParam("dob") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dob, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		customer.setDateOfBirth(dob);
		customer.setStatus(true);
		rest.postForObject(domainServices + "/rest/api/customer/insert", customer, Customer.class);
		return "redirect:/admin/manage/customer";
	}

	@GetMapping(value = "/edit/{id}")
	public String editCustomer(@PathVariable("id") int id, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		Customer customer = rest.getForObject(domainServices + "/rest/api/customer/find-by-id/" + id, Customer.class);
		model.addAttribute("customer", customer);
		return "customer/edit_customer";
	}

	@PostMapping(value = "/edit")
	public String editCustomerPost(@ModelAttribute("customer") Customer customer,
			@RequestParam("dob") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dob, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		customer.setDateOfBirth(dob);
		customer.setStatus(true);
		rest.postForObject(domainServices + "/rest/api/customer/insert", customer, Customer.class);
		return "redirect:/admin/manage/customer";
	}

	@GetMapping("/delete/{id}")
	public String deleteCustomer(@PathVariable("id") int id, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		rest.delete(domainServices + "/rest/api/customer/delete-by-id/" + id);
		return "redirect:/admin/manage/customer";
	}
}
