package vn.ptit.controllers;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import vn.ptit.models.CreatedBankAccount;
import vn.ptit.models.Employee;
import vn.ptit.models.Salary;
import vn.ptit.models.Transaction;

@Controller
@RequestMapping("/admin/manage/salary")
public class ManageSalaryController {
	@Value("${domain.services.name}")
	private String domainServices;
	private RestTemplate rest = new RestTemplate();

	// hiển thị trang quản lý lương của nhân viên
	@GetMapping
	public String viewManageSalaryEmployee(Model model, HttpServletRequest req, HttpServletResponse resp) {
		List<Employee> employees = Arrays
				.asList(rest.getForObject(domainServices + "/rest/api/employee/find-all", Employee[].class));
		model.addAttribute("employees", employees);
		return "salary/manage_salary_employee";
	}
	// hiển thị trang danh sách lương các tháng của 1 nhân viên nào đó
	@GetMapping("/{id}")
	public String viewListSalaryEmployee(@PathVariable("id") int id, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		Employee employee = rest.getForObject(domainServices + "/rest/api/employee/find-by-id/" + id, Employee.class);
		req.getSession().setAttribute("employeeSalary", employee);
		List<Salary> salaries = Arrays.asList(
				rest.getForObject(domainServices + "/rest/api/salary/find-all-by-employee/" + id, Salary[].class));
		model.addAttribute("salaries", salaries);
		return "salary/list_salary_employee";
	}
	// hiển thị trang thêm lương cho 1 nhân viên
	@GetMapping("/add")
	public String viewAddSalaryEmployee(Model model, HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		if (session.getAttribute("employeeSalary") == null) {
			return "redirect:/admin/manage/salary";
		}
		int id = ((Employee) session.getAttribute("employeeSalary")).getId();
		Salary salary = new Salary();
		List<Transaction> transactions = Arrays.asList(rest.getForObject(
				domainServices + "/rest/api/deposit/find-first-transactions-in-month/" + id, Transaction[].class));
		double total = 0;
		for (Transaction transaction : transactions) {
			total += transaction.getMoney() * 0.02;
		}
		if(((Employee) session.getAttribute("employeeSalary")).getPosition().equals("MANAGER")){
			List<Transaction> transactions1 = Arrays.asList(rest.getForObject(
					domainServices + "/rest/api/deposit/find-first-transactions-deposit-account/" + id, Transaction[].class));
			for (Transaction transaction : transactions1) {
				total += transaction.getMoney() * 0.02;
			}
			List<CreatedBankAccount> createdBankAccounts = Arrays.asList(rest.getForObject(
					domainServices + "/rest/api/credit-account/find-by-employee/" + id, CreatedBankAccount[].class));
			total += createdBankAccounts.size() * 500000;
		}
		salary.setBonusSalary(total);
		model.addAttribute("salary", salary);
		return "/salary/add_salary_employee";
	}

	// thêm lương cho 1 nhân viên
	@PostMapping("/add")
	public String addSalaryEmloyee(@ModelAttribute("salary") Salary salary,
			@RequestParam("dateS") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateStart,
			@RequestParam("dateE") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateEnd, Model model,
			HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		if (session.getAttribute("employeeSalary") == null) {
			return "redirect:/admin/manage/salary";
		}
		salary.setDateStart(dateStart);
		salary.setDateEnd(dateEnd);
		int id = ((Employee) session.getAttribute("employeeSalary")).getId();
		rest.postForObject(domainServices + "/rest/api/salary/insert/" + id, salary, Salary.class);
		return "redirect:/admin/manage/salary/" + id;
	}
	
	// sửa lương
	@GetMapping("/edit/{id}")
	public String viewEditSalaryEmployee(@PathVariable("id") int salaryId, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		HttpSession session = req.getSession();
		if (session.getAttribute("employeeSalary") == null) {
			return "redirect:/admin/manage/salary";
		}
		int id = ((Employee) session.getAttribute("employeeSalary")).getId();
		Salary salary = rest.getForObject(domainServices + "/rest/api/salary/find-by-id/" + salaryId, Salary.class);
		List<Transaction> transactions = Arrays.asList(rest.getForObject(
				domainServices + "/rest/api/deposit/find-first-transactions-in-month/" + id, Transaction[].class));
		double total = 0;
		for (Transaction transaction : transactions) {
			total += transaction.getMoney() * 0.02;
		}
		if(((Employee) session.getAttribute("employeeSalary")).getPosition().equals("MANAGER")){
			List<Transaction> transactions1 = Arrays.asList(rest.getForObject(
					domainServices + "/rest/api/deposit/find-first-transactions-deposit-account/" + id, Transaction[].class));
			for (Transaction transaction : transactions1) {
				total += transaction.getMoney() * 0.02;
			}
			List<CreatedBankAccount> createdBankAccounts = Arrays.asList(rest.getForObject(
					domainServices + "/rest/api/credit-account/find-by-employee/" + id, CreatedBankAccount[].class));
			total += createdBankAccounts.size() * 500000;
		}
		salary.setBonusSalary(total);
		model.addAttribute("salary", salary);
		return "salary/edit_salary_employee";
	}
	@PostMapping("/edit")
	public String editSalaryEmloyee(@ModelAttribute("salary") Salary salary,
			@RequestParam("dateS") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateStart,
			@RequestParam("dateE") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateEnd, Model model,
			HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		if (session.getAttribute("employeeSalary") == null) {
			return "redirect:/admin/manage/salary";
		}
		salary.setDateStart(dateStart);
		salary.setDateEnd(dateEnd);
		int id = ((Employee) session.getAttribute("employeeSalary")).getId();
		rest.postForObject(domainServices + "/rest/api/salary/insert/" + id, salary, Salary.class);
		return "redirect:/admin/manage/salary/" + id;
	}
	
	// xem chi tiết lương của nhân viên
	@GetMapping("/detail/{id}")
	public String viewDetailSalary(@PathVariable("id") int salaryId, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		HttpSession session = req.getSession();
		if (session.getAttribute("employeeSalary") == null) {
			return "redirect:/manage/salary";
		}
		Salary salary = rest.getForObject(domainServices + "/rest/api/salary/find-by-id/" + salaryId, Salary.class);
		int id = ((Employee) session.getAttribute("employeeSalary")).getId();
		List<Transaction> transactions = Arrays.asList(rest.getForObject(
				domainServices + "/rest/api/deposit/find-first-transactions-in-month/" + id, Transaction[].class));
		double moneyDeposit = 0;
		for (Transaction transaction : transactions) {
			moneyDeposit += transaction.getMoney()*0.02;
		}
		if(((Employee) session.getAttribute("employeeSalary")).getPosition().equals("MANAGER")){
			List<CreatedBankAccount> createdBankAccounts = Arrays.asList(rest.getForObject(
					domainServices + "/rest/api/credit-account/find-by-employee/" + id, CreatedBankAccount[].class));
			double moneyCreatedCreditAccount = createdBankAccounts.size()*500000;
			List<Transaction> transactions1 = Arrays.asList(rest.getForObject(
					domainServices + "/rest/api/deposit/find-first-transactions-deposit-account/" + id, Transaction[].class));
			for (Transaction transaction : transactions1) {
				moneyDeposit += transaction.getMoney() * 0.02;
			}
			model.addAttribute("moneyCreatedCreditAccount", moneyCreatedCreditAccount);
			model.addAttribute("createdBankAccounts", createdBankAccounts);
		}
		model.addAttribute("moneyDeposit", moneyDeposit);
		model.addAttribute("salary", salary);
		return "/salary/detail_salary_employee";
	}
}
