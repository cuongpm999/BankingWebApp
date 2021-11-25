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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import vn.ptit.models.CreditAccount;
import vn.ptit.models.CustomerCreditStat;
import vn.ptit.models.CustomerDepositStat;
import vn.ptit.models.CustomerTransactionStat;
import vn.ptit.models.DepositAccount;

@Controller
@RequestMapping("/admin/statistic")
public class StatisticController {
	private RestTemplate rest = new RestTemplate();

	@Value("${domain.services.name}")
	private String domainServices;
	
	@GetMapping("/customer-deposit-max")
	public String customerWithDepositMax(Model model) {
		List<CustomerDepositStat> customerDepositStats = Arrays.asList(rest.getForObject(domainServices+"/rest/api/statistics/get-customer-deposit-stat", CustomerDepositStat[].class));
		model.addAttribute("customerDepositStats", customerDepositStats);
		return "statistic/customer_deposit_max";
	}
	
	@GetMapping("/credit-account-balance-max")
	public String creditAccountBalanceMax(Model model) {
		List<CreditAccount> creditAccounts = Arrays.asList(rest.getForObject(domainServices+"/rest/api/credit-account/find-all", CreditAccount[].class));
		model.addAttribute("creditAccounts", creditAccounts);
		return "statistic/credit_account_balance_max";
	}
	
	@GetMapping("/deposit-account-balance-max")
	public String depositAccountBalanceMax(Model model) {
		List<DepositAccount> depositAccounts = Arrays.asList(rest.getForObject(domainServices+"/rest/api/deposit-account/find-all", DepositAccount[].class));
		model.addAttribute("depositAccounts", depositAccounts);
		return "statistic/deposit_account_balance_max";
	}
	
	@GetMapping("/customer-credit-max")
	public String customerWithCreditMax(Model model) {
		List<CustomerCreditStat> customerCreditStats = Arrays.asList(rest.getForObject(domainServices+"/rest/api/statistics/get-customer-credit-stat", CustomerCreditStat[].class));
		model.addAttribute("customerCreditStats", customerCreditStats);
		return "statistic/customer_credit_max";
	}
	
	@GetMapping("/customer-with-transaction")
	public String viewCustomerWithTransaction(Model model) {
		return "statistic/customer_transaction";
	}
	
	@PostMapping("/customer-with-transaction")
	public String customerWithTransaction(Model model, HttpServletRequest req, HttpServletResponse resp) {
		String from = req.getParameter("from");
		String to = req.getParameter("to");
		String type = req.getParameter("selectType");
		List<String> truyVan = new ArrayList<String>();
		truyVan.add(type);
		truyVan.add(from);
		truyVan.add(to);
		
		List<CustomerTransactionStat> customerTransactionStats = Arrays.asList(rest.postForObject(domainServices+"/rest/api/statistics/get-customer-transaction-stat", truyVan, CustomerTransactionStat[].class));
		model.addAttribute("customerTransactionStats", customerTransactionStats);
		model.addAttribute("type", type);
		model.addAttribute("fromD", from);
		model.addAttribute("toD", to);
		
		return "statistic/customer_transaction";
	}

}
