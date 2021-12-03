package vn.ptit.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
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

import vn.ptit.models.CreditAccount;
import vn.ptit.models.CustomerCreditStat;
import vn.ptit.models.CustomerDepositStat;
import vn.ptit.models.DepositAccount;
import vn.ptit.models.Employee;

@Controller
@RequestMapping("/admin")
public class ManageController {
	private RestTemplate rest = new RestTemplate();

	@Value("${domain.services.name}")
	private String domainServices;

	@GetMapping
	public String viewHomeManage(Model model) {
		int countTransaction = rest.getForObject(domainServices + "/rest/api/statistics/count-trasaction",
				Integer.class);
		int countCustomer = rest.getForObject(domainServices + "/rest/api/statistics/count-customer", Integer.class);
		int countBankAccount = rest.getForObject(domainServices + "/rest/api/statistics/count-bank-account",
				Integer.class);
		int totalDeposit = rest.getForObject(domainServices + "/rest/api/statistics/total-deposit", Integer.class);
		model.addAttribute("countTransaction", countTransaction);
		model.addAttribute("countCustomer", countCustomer);
		model.addAttribute("countBankAccount", countBankAccount);
		model.addAttribute("totalDeposit", totalDeposit);

		List<CustomerDepositStat> customerDepositStats = Arrays.asList(rest.getForObject(
				domainServices + "/rest/api/statistics/get-customer-deposit-stat", CustomerDepositStat[].class));
		model.addAttribute("customerDepositStats", customerDepositStats);

		List<CustomerCreditStat> customerCreditStats = Arrays.asList(rest.getForObject(
				domainServices + "/rest/api/statistics/get-customer-credit-stat", CustomerCreditStat[].class));
		model.addAttribute("customerCreditStats", customerCreditStats);

		List<DepositAccount> depositAccounts = Arrays.asList(
				rest.getForObject(domainServices + "/rest/api/deposit-account/find-all", DepositAccount[].class));
		List<CreditAccount> creditAccounts = Arrays
				.asList(rest.getForObject(domainServices + "/rest/api/credit-account/find-all", CreditAccount[].class));

		// chart deposit
		List<Double> listDataDeposit = new ArrayList<>();
		int size = 0;
		for (DepositAccount depositAccount : depositAccounts) {
			listDataDeposit.add(depositAccount.getBalance());
			size++;
			if (size == 5)
				break;
		}
		List<String> listLabelDeposit = new ArrayList<>();
		size = 0;
		for (DepositAccount depositAccount : depositAccounts) {
			listLabelDeposit.add(depositAccount.getId());
			size++;
			if (size == 5)
				break;
		}
		model.addAttribute("listDataDeposit", listDataDeposit);
		model.addAttribute("listLabelDeposit", listLabelDeposit);

		// chart credit
		List<Double> listDataCredit = new ArrayList<>();
		size = 0;
		for (CreditAccount creditAccount : creditAccounts) {
			listDataCredit.add(creditAccount.getBalance());
			size++;
			if (size == 5)
				break;
		}
		List<String> listLabelCredit = new ArrayList<>();
		size = 0;
		for (CreditAccount creditAccount : creditAccounts) {
			listLabelCredit.add(creditAccount.getId());
			size++;
			if (size == 5)
				break;
		}
		model.addAttribute("listDataCredit", listDataCredit);
		model.addAttribute("listLabelCredit", listLabelCredit);

		return "manage";
	}

	@GetMapping("/403-error")
	public String view403Error() {
		return "error/403_error";
	}

	@GetMapping("/profile")
	public String viewProfile(Model model, HttpServletRequest req, HttpServletResponse resp) {
		String username = (String) req.getSession().getAttribute("usernameEmployee");
		Employee employee = rest.getForObject(domainServices + "/rest/api/employee/get/" + username, Employee.class);
		model.addAttribute("employee", employee);
		return "employee/detail_employee";
	}
}
