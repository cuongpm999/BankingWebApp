package vn.ptit.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import vn.ptit.models.CreatedBankAccount;
import vn.ptit.models.Customer;
import vn.ptit.models.Employee;
import vn.ptit.models.EmployeeSalary;
import vn.ptit.models.Salary;
import vn.ptit.models.Transaction;
import vn.ptit.utils.ExcelExporter;
import vn.ptit.utils.ExcelSalaryExporter;

@Controller
@RequestMapping("/admin/manage/salary")
public class ManageSalaryController {
	@Value("${domain.services.name}")
	private String domainServices;
	private RestTemplate rest = new RestTemplate();

	@GetMapping
	public String viewManageSalaryEmployee(Model model, HttpServletRequest req, HttpServletResponse resp) {
		Date date = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
		String month;
		String year;
		int page = 1;
		if (req.getParameter("page") != null) {
			page = Integer.parseInt(req.getParameter("page"));
			model.addAttribute("page", page);
		}
		if (req.getParameter("month") != null) {
			month = req.getParameter("month");
			model.addAttribute("month", month);
		} else {
			month = sdf1.format(date);
			model.addAttribute("month", month);
		}
		if (req.getParameter("year") != null) {
			year = req.getParameter("year");
			model.addAttribute("year", year);
		} else {
			year = sdf2.format(date);
			model.addAttribute("year", year);
		}
		LocalDate now = LocalDate.now();
		int currentMonth = now.getMonthValue();
		int currentYear = now.getYear();
		if (Integer.parseInt(year) > currentYear) {
			return "salary/manage_salary_employee";
		}
		if (Integer.parseInt(month) > currentMonth && Integer.parseInt(year) == currentYear) {
			return "salary/manage_salary_employee";
		}
		if (Integer.parseInt(year) == currentYear && Integer.parseInt(month) == currentMonth) {
			rest.getForObject(domainServices + "/rest/api/salary/insert/" + month + "-" + year, Boolean.class);
		}
		List<EmployeeSalary> employeeSalaries = Arrays.asList(rest.getForObject(
				domainServices + "/rest/api/salary/find-salary-in-month/" + month + "-" + year + "-" + page,
				EmployeeSalary[].class));
		model.addAttribute("employeeSalaries", employeeSalaries);
		req.getSession().setAttribute("dateSalaryToExportExcel", month + "/" + year);
		return "salary/manage_salary_employee";
	}

	@GetMapping("/detail")
	public String viewDetailSalary(Model model, HttpServletRequest req, HttpServletResponse resp) {
		String empId = (req.getParameter("empId"));
		String month = req.getParameter("month");
		model.addAttribute("month", month);
		String year = req.getParameter("year");
		model.addAttribute("year", year);

		List<String> textFind = new ArrayList<String>();
		textFind.add(empId);
		textFind.add(month);
		textFind.add(year);
		Salary salary = rest.postForObject(domainServices + "/rest/api/salary/find-salary-detail", textFind,
				Salary.class);
		Employee employee = rest.getForObject(domainServices + "/rest/api/employee/find-by-id/" + empId,
				Employee.class);
		List<String> text = new ArrayList<String>();
		text.add(String.valueOf(empId));
		text.add(salary.getDateSalary());
		List<Transaction> transactions = Arrays.asList(rest.postForObject(
				domainServices + "/rest/api/deposit/find-first-transactions-in-month", text, Transaction[].class));
		double totalMoney = salary.getBasicSalary() + salary.getBonusSalary();
		double moneyDeposit1 = 0;
		for (Transaction transaction : transactions) {
			moneyDeposit1 += transaction.getMoney() * 0.02;
		}
		if (employee.getPosition().equals("MANAGER")) {
			List<CreatedBankAccount> createdBankAccounts = Arrays.asList(
					rest.postForObject(domainServices + "/rest/api/credit-account/find-create-bank-account-by-employee",
							text, CreatedBankAccount[].class));
			double moneyCreatedCreditAccount = createdBankAccounts.size() * 500000;
			List<Transaction> transactions1 = Arrays.asList(
					rest.postForObject(domainServices + "/rest/api/deposit/find-first-transactions-deposit-account",
							text, Transaction[].class));
			double moneyDeposit2 = 0;
			for (Transaction transaction : transactions1) {
				moneyDeposit2 += transaction.getMoney() * 0.02;
			}
			model.addAttribute("moneyCreatedCreditAccount", moneyCreatedCreditAccount);
			model.addAttribute("createdBankAccounts", createdBankAccounts);
			model.addAttribute("moneyDeposit2", moneyDeposit2);
		}
		model.addAttribute("employee", employee);
		model.addAttribute("moneyDeposit1", moneyDeposit1);
		model.addAttribute("totalMoney", totalMoney);
		model.addAttribute("salary", salary);
		return "salary/detail_salary_employee";
	}

	@GetMapping("/export-salary")
	public void exportTransactionInDepositAccount(Model model, HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		HttpSession httpSession = req.getSession();
		String dateSalary = "";
		if (httpSession.getAttribute("dateSalaryToExportExcel") != null)
			dateSalary = httpSession.getAttribute("dateSalaryToExportExcel").toString();

		httpSession.removeAttribute("dateSalaryToExportExcel");
		List<EmployeeSalary> employeeSalaries = Arrays
				.asList(rest.getForObject(domainServices + "/rest/api/salary/export-all-salary-in-month/"
						+ dateSalary.split("\\/")[0] + "-" + dateSalary.split("\\/")[1], EmployeeSalary[].class));

		resp.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=salary_" + currentDateTime + ".xlsx";
		resp.setHeader(headerKey, headerValue);

		ExcelSalaryExporter excelSalaryExporter = new ExcelSalaryExporter(employeeSalaries, dateSalary);
		excelSalaryExporter.export(resp);

	}

}
