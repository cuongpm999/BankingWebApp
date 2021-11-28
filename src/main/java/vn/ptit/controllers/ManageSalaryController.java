package vn.ptit.controllers;

import java.util.ArrayList;
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

	@GetMapping
	public String viewManageSalaryEmployee(Model model, HttpServletRequest req, HttpServletResponse resp) {
		List<Employee> employees = Arrays
				.asList(rest.getForObject(domainServices + "/rest/api/employee/find-all", Employee[].class));
		model.addAttribute("employees", employees);
		return "salary/manage_salary_employee";
	}
	
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
	
	@GetMapping("/add")
	public String viewAddSalaryEmployee(Model model, HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		if (session.getAttribute("employeeSalary") == null) {
			return "redirect:/admin/manage/salary";
		}
		Salary salary = new Salary();
		model.addAttribute("salary", salary);
		return "salary/add_salary_employee";
	}

	@PostMapping("/add")
	public String addSalaryEmloyee(@ModelAttribute("salary") Salary salary, Model model,
			HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		if (session.getAttribute("employeeSalary") == null) {
			return "redirect:/admin/manage/salary";
		}

		int id = ((Employee) session.getAttribute("employeeSalary")).getId();
		Boolean flag = rest.postForObject(domainServices + "/rest/api/salary/insert/" + id, salary, Boolean.class);
		if(!flag) {
			model.addAttribute("salary", salary);
			model.addAttribute("status", "failed");
			return "salary/add_salary_employee";
		}
		return "redirect:/admin/manage/salary/" + id;
	}
	
	@GetMapping("/edit/{id}")
	public String viewEditSalaryEmployee(@PathVariable("id") int salaryId, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		HttpSession session = req.getSession();
		if (session.getAttribute("employeeSalary") == null) {
			return "redirect:/admin/manage/salary";
		}
		Salary salary = rest.getForObject(domainServices + "/rest/api/salary/find-by-id/" + salaryId, Salary.class);
		model.addAttribute("salary", salary);
		return "salary/edit_salary_employee";
	}
	
	@PostMapping("/edit")
	public String editSalaryEmloyee(@ModelAttribute("salary") Salary salary, Model model,
			HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		if (session.getAttribute("employeeSalary") == null) {
			return "redirect:/admin/manage/salary";
		}
		int id = ((Employee) session.getAttribute("employeeSalary")).getId();
		Boolean flag = rest.postForObject(domainServices + "/rest/api/salary/update/" + id, salary, Boolean.class);
		if(!flag) {
			model.addAttribute("salary", salary);
			model.addAttribute("status", "failed");
			return "salary/edit_salary_employee";
		}
		return "redirect:/admin/manage/salary/" + id;
	}
	
	
	@GetMapping("/detail/{id}")
	public String viewDetailSalary(@PathVariable("id") int salaryId, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		HttpSession session = req.getSession();
		if (session.getAttribute("employeeSalary") == null) {
			return "redirect:/admin/manage/salary";
		}
		Salary salary = rest.getForObject(domainServices + "/rest/api/salary/find-by-id/" + salaryId, Salary.class);
		int id = ((Employee) session.getAttribute("employeeSalary")).getId();
		List<String> text = new ArrayList<String>();
		text.add(String.valueOf(id));
		text.add(salary.getDateSalary());
		List<Transaction> transactions = Arrays.asList(rest.postForObject(
				domainServices + "/rest/api/deposit/find-first-transactions-in-month",text, Transaction[].class));
		double totalMoney = salary.getBasicSalary()+salary.getBonusSalary();
		double moneyDeposit1 = 0;
		for (Transaction transaction : transactions) {
			moneyDeposit1 += transaction.getMoney()*0.02;
		}
		if(((Employee) session.getAttribute("employeeSalary")).getPosition().equals("MANAGER")){
			List<CreatedBankAccount> createdBankAccounts = Arrays.asList(rest.postForObject(
					domainServices + "/rest/api/credit-account/find-create-bank-account-by-employee", text,CreatedBankAccount[].class));
			double moneyCreatedCreditAccount = createdBankAccounts.size()*500000;
			List<Transaction> transactions1 = Arrays.asList(rest.postForObject(
					domainServices + "/rest/api/deposit/find-first-transactions-deposit-account", text, Transaction[].class));
			double moneyDeposit2 = 0;
			for (Transaction transaction : transactions1) {
				moneyDeposit2 += transaction.getMoney() * 0.02;
			}
			model.addAttribute("moneyCreatedCreditAccount", moneyCreatedCreditAccount);
			model.addAttribute("createdBankAccounts", createdBankAccounts);
			model.addAttribute("moneyDeposit2", moneyDeposit2);
		}
		model.addAttribute("moneyDeposit1", moneyDeposit1);
		model.addAttribute("totalMoney", totalMoney);
		model.addAttribute("salary", salary);
		return "salary/detail_salary_employee";
	}
}
