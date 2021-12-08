package vn.ptit.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import vn.ptit.models.CreditAccount;
import vn.ptit.models.Customer;
import vn.ptit.models.CustomerCreditStat;
import vn.ptit.models.CustomerDepositStat;
import vn.ptit.models.CustomerTransactionStat;
import vn.ptit.models.DepositAccount;
import vn.ptit.models.Transaction;
import vn.ptit.utils.ExcelExporter;

@Controller
@RequestMapping("/admin/statistic")
public class StatisticController {
	private RestTemplate rest = new RestTemplate();

	@Value("${domain.services.name}")
	private String domainServices;

	@GetMapping("/customer-deposit-max")
	public String customerWithDepositMax(Model model) {
		List<CustomerDepositStat> customerDepositStats = Arrays.asList(rest.getForObject(
				domainServices + "/rest/api/statistics/get-customer-deposit-stat", CustomerDepositStat[].class));
		model.addAttribute("customerDepositStats", customerDepositStats);
		return "statistic/customer_deposit_max";
	}

	@GetMapping("/credit-account-balance-max")
	public String creditAccountBalanceMax(Model model) {
		List<CreditAccount> creditAccounts = Arrays
				.asList(rest.getForObject(domainServices + "/rest/api/credit-account/find-all", CreditAccount[].class));
		model.addAttribute("creditAccounts", creditAccounts);
		return "statistic/credit_account_balance_max";
	}

	@GetMapping("/deposit-account-balance-max")
	public String depositAccountBalanceMax(Model model) {
		List<DepositAccount> depositAccounts = Arrays.asList(
				rest.getForObject(domainServices + "/rest/api/deposit-account/find-all", DepositAccount[].class));
		model.addAttribute("depositAccounts", depositAccounts);
		return "statistic/deposit_account_balance_max";
	}

	@GetMapping("/customer-credit-max")
	public String customerWithCreditMax(Model model) {
		List<CustomerCreditStat> customerCreditStats = Arrays.asList(rest.getForObject(
				domainServices + "/rest/api/statistics/get-customer-credit-stat", CustomerCreditStat[].class));
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

		List<CustomerTransactionStat> customerTransactionStats = Arrays
				.asList(rest.postForObject(domainServices + "/rest/api/statistics/get-customer-transaction-stat",
						truyVan, CustomerTransactionStat[].class));
		model.addAttribute("customerTransactionStats", customerTransactionStats);
		model.addAttribute("type", type);
		model.addAttribute("fromD", from);
		model.addAttribute("toD", to);

		return "statistic/customer_transaction";
	}

	// Find transaction by customer
	@GetMapping("/transaction-by-customer")
	public String transactionByCustomer(Model model, HttpServletRequest req, HttpServletResponse resp) {
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
		return "statistic/search_customer";
	}

	@GetMapping("/transaction-by-customer/detail-customer/{id}")
	public String viewCustomerDetail(@PathVariable("id") int id, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		Customer customer = rest.getForObject(domainServices + "/rest/api/customer/find-by-id/" + id, Customer.class);
		model.addAttribute("customer", customer);
		req.getSession().setAttribute("customerStatistic", customer);
		List<CreditAccount> creditAccounts = Arrays.asList(rest.getForObject(
				domainServices + "/rest/api/credit-account/find-by-customer/" + id, CreditAccount[].class));
		List<DepositAccount> depositAccounts = Arrays.asList(rest.getForObject(
				domainServices + "/rest/api/deposit-account/find-by-customer/" + id, DepositAccount[].class));
		model.addAttribute("depositAccounts", depositAccounts);
		model.addAttribute("creditAccounts", creditAccounts);
		return "statistic/detail_customer";
	}

	@GetMapping("/transaction-by-customer/detail-credit-account/{id}")
	public String viewDetailCreditAccount(@PathVariable("id") String id, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		HttpSession httpSession = req.getSession();
		httpSession.setAttribute("accountId_credit_statistic", id);
		if (httpSession.getAttribute("customerStatistic") == null)
			return "redirect:/admin/statistic/transaction-by-customer";

		Map<String, Object> map = new HashMap<String, Object>();
		int page = 1;
		if (req.getParameter("page") != null) {
			page = Integer.parseInt(req.getParameter("page"));
			map.put("page", page);
			model.addAttribute("page", page);
		}
		if (req.getParameter("fromDate") != null) {
			String fromDate = req.getParameter("fromDate");
			map.put("fromDate", fromDate);
			model.addAttribute("fromDate", fromDate);
			httpSession.setAttribute("fromDateExportCredit", fromDate);
		}
		if (req.getParameter("toDate") != null) {
			String toDate = req.getParameter("toDate");
			map.put("toDate", toDate);
			model.addAttribute("toDate", toDate);
			httpSession.setAttribute("toDateExportCredit", toDate);
		}
		if (req.getParameter("sort") != null) {
			String sort = req.getParameter("sort");
			map.put("sort", sort);
			model.addAttribute("sort", sort);
		}
		List<Transaction> transactions = Arrays.asList(rest.postForObject(
				domainServices + "/rest/api/credit/statistic-by-credit-account/" + id, map, Transaction[].class));
		model.addAttribute("transactions", transactions);

		return "statistic/detail_credit_account";
	}

	@GetMapping("/transaction-by-customer/detail-deposit-account/{id}")
	public String viewDetailDepositAccount(@PathVariable("id") String id, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		HttpSession httpSession = req.getSession();
		httpSession.setAttribute("accountId_deposit_statistic", id);
		if (httpSession.getAttribute("customerStatistic") == null)
			return "redirect:/admin/statistic/transaction-by-customer";

		Map<String, Object> map = new HashMap<String, Object>();
		int page = 1;
		if (req.getParameter("page") != null) {
			page = Integer.parseInt(req.getParameter("page"));
			map.put("page", page);
			model.addAttribute("page", page);
		}
		if (req.getParameter("fromDate") != null) {
			String fromDate = req.getParameter("fromDate");
			map.put("fromDate", fromDate);
			model.addAttribute("fromDate", fromDate);
			httpSession.setAttribute("fromDateExportDeposit", fromDate);
		}
		if (req.getParameter("toDate") != null) {
			String toDate = req.getParameter("toDate");
			map.put("toDate", toDate);
			model.addAttribute("toDate", toDate);
			httpSession.setAttribute("toDateExportDeposit", toDate);
		}
		if (req.getParameter("sort") != null) {
			String sort = req.getParameter("sort");
			map.put("sort", sort);
			model.addAttribute("sort", sort);
		}
		List<Transaction> transactions = Arrays.asList(rest.postForObject(
				domainServices + "/rest/api/deposit/statistic-by-deposit-account/" + id, map, Transaction[].class));
		model.addAttribute("transactions", transactions);
		return "statistic/detail_deposit_account";
	}

	@GetMapping("/transaction-by-customer/export/deposit-account")
	public void exportTransactionInDepositAccount(Model model, HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		HttpSession httpSession = req.getSession();
		String idAccount = "";
		Customer customer = new Customer();
		if (httpSession.getAttribute("customerStatistic") != null)
			customer = (Customer) httpSession.getAttribute("customerStatistic");
		if (httpSession.getAttribute("accountId_deposit_statistic") != null)
			idAccount = httpSession.getAttribute("accountId_deposit_statistic").toString();

		Map<String, Object> map = new HashMap<String, Object>();
		if (httpSession.getAttribute("fromDateExportDeposit") != null) {
			String fromDate = httpSession.getAttribute("fromDateExportDeposit").toString();
			map.put("fromDate", fromDate);
		}
		if (httpSession.getAttribute("toDateExportDeposit") != null) {
			String toDate = httpSession.getAttribute("toDateExportDeposit").toString();
			map.put("toDate", toDate);
		}
		List<Transaction> transactions = Arrays.asList(
				rest.postForObject(domainServices + "/rest/api/deposit/export-with-deposit-account/" + idAccount, map,
						Transaction[].class));

		httpSession.removeAttribute("fromDateExportDeposit");
		httpSession.removeAttribute("toDateExportDeposit");

		resp.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=transactions_" + currentDateTime + ".xlsx";
		resp.setHeader(headerKey, headerValue);

		ExcelExporter excelExporter = new ExcelExporter(transactions, customer, idAccount, 2);
		excelExporter.export(resp);

	}

	@GetMapping("/transaction-by-customer/export/credit-account")
	public void exportTransactionInCreditAccount(Model model, HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		HttpSession httpSession = req.getSession();
		String idAccount = "";
		Customer customer = new Customer();
		if (httpSession.getAttribute("customerStatistic") != null)
			customer = (Customer) httpSession.getAttribute("customerStatistic");
		if (httpSession.getAttribute("accountId_credit_statistic") != null)
			idAccount = httpSession.getAttribute("accountId_credit_statistic").toString();

		Map<String, Object> map = new HashMap<String, Object>();
		if (httpSession.getAttribute("fromDateExportCredit") != null) {
			String fromDate = httpSession.getAttribute("fromDateExportCredit").toString();
			map.put("fromDate", fromDate);
		}
		if (httpSession.getAttribute("toDateExportCredit") != null) {
			String toDate = httpSession.getAttribute("toDateExportCredit").toString();
			map.put("toDate", toDate);
		}

		httpSession.removeAttribute("fromDateExportCredit");
		httpSession.removeAttribute("toDateExportCredit");

		List<Transaction> transactions = Arrays.asList(rest.postForObject(
				domainServices + "/rest/api/credit/export-with-credit-account/" + idAccount, map, Transaction[].class));

		resp.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=transactions_" + currentDateTime + ".xlsx";
		resp.setHeader(headerKey, headerValue);

		ExcelExporter excelExporter = new ExcelExporter(transactions, customer, idAccount, 1);
		excelExporter.export(resp);

	}

	// Find customer by account
	@GetMapping("/customer-by-account")
	public String viewCustomerByAccount(Model model) {
		return "statistic/search_customer_by_account";
	}

	@PostMapping("/customer-by-account")
	public String customerByAccount(Model model, HttpServletRequest req, HttpServletResponse resp) {
		String idAccount = req.getParameter("accountId");

		Customer customer = rest.getForObject(domainServices + "/rest/api/customer/find-by-account/" + idAccount,
				Customer.class);
		if (customer == null) {
			model.addAttribute("status", "noCustomer");
			return "statistic/search_customer_by_account";
		}
		
		HttpSession httpSession = req.getSession();
		httpSession.setAttribute("customerStatistic_", customer);
		
		List<CreditAccount> creditAccounts = Arrays.asList(
				rest.getForObject(domainServices + "/rest/api/credit-account/find-by-customer/" + customer.getId(),
						CreditAccount[].class));
		for (CreditAccount creditAccount : creditAccounts) {
			if (creditAccount.getId().equalsIgnoreCase(idAccount)) {
				model.addAttribute("customer", customer);
				model.addAttribute("idAccount", idAccount);
				model.addAttribute("type", "CREDIT");
				return "statistic/search_customer_by_account";
			}
		}
		List<DepositAccount> depositAccounts = Arrays.asList(
				rest.getForObject(domainServices + "/rest/api/deposit-account/find-by-customer/" + customer.getId(),
						DepositAccount[].class));
		for (DepositAccount depositAccount : depositAccounts) {
			if(depositAccount.getId().equalsIgnoreCase(idAccount)){
				model.addAttribute("customer", customer);
				model.addAttribute("idAccount", idAccount);
				model.addAttribute("type", "DEPOSIT");
				return "statistic/search_customer_by_account";
			}
		}
		return null;
	}
	
	@GetMapping("/customer-by-account/detail-credit-account/{id}")
	public String viewDetailCreditAccount_(@PathVariable("id") String id, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		HttpSession httpSession = req.getSession();
		httpSession.setAttribute("accountId_credit_statistic_", id);
		if (httpSession.getAttribute("customerStatistic_") == null)
			return "redirect:/admin/statistic/customer-by-account";

		Map<String, Object> map = new HashMap<String, Object>();
		int page = 1;
		if (req.getParameter("page") != null) {
			page = Integer.parseInt(req.getParameter("page"));
			map.put("page", page);
			model.addAttribute("page", page);
		}
		if (req.getParameter("fromDate") != null) {
			String fromDate = req.getParameter("fromDate");
			map.put("fromDate", fromDate);
			model.addAttribute("fromDate", fromDate);
			httpSession.setAttribute("fromDateExportCredit_", fromDate);
		}
		if (req.getParameter("toDate") != null) {
			String toDate = req.getParameter("toDate");
			map.put("toDate", toDate);
			model.addAttribute("toDate", toDate);
			httpSession.setAttribute("toDateExportCredit_", toDate);
		}
		if (req.getParameter("sort") != null) {
			String sort = req.getParameter("sort");
			map.put("sort", sort);
			model.addAttribute("sort", sort);
		}
		List<Transaction> transactions = Arrays.asList(rest.postForObject(
				domainServices + "/rest/api/credit/statistic-by-credit-account/" + id, map, Transaction[].class));
		model.addAttribute("transactions", transactions);
		model.addAttribute("credit", "credit");
		return "statistic/detail_credit_account";
	}

	@GetMapping("/customer-by-account/detail-deposit-account/{id}")
	public String viewDetailDepositAccount_(@PathVariable("id") String id, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		HttpSession httpSession = req.getSession();
		httpSession.setAttribute("accountId_deposit_statistic_", id);
		if (httpSession.getAttribute("customerStatistic_") == null)
			return "redirect:/admin/statistic/customer-by-account";

		Map<String, Object> map = new HashMap<String, Object>();
		int page = 1;
		if (req.getParameter("page") != null) {
			page = Integer.parseInt(req.getParameter("page"));
			map.put("page", page);
			model.addAttribute("page", page);
		}
		if (req.getParameter("fromDate") != null) {
			String fromDate = req.getParameter("fromDate");
			map.put("fromDate", fromDate);
			model.addAttribute("fromDate", fromDate);
			httpSession.setAttribute("fromDateExportDeposit_", fromDate);
		}
		if (req.getParameter("toDate") != null) {
			String toDate = req.getParameter("toDate");
			map.put("toDate", toDate);
			model.addAttribute("toDate", toDate);
			httpSession.setAttribute("toDateExportDeposit_", toDate);
		}
		if (req.getParameter("sort") != null) {
			String sort = req.getParameter("sort");
			map.put("sort", sort);
			model.addAttribute("sort", sort);
		}
		List<Transaction> transactions = Arrays.asList(rest.postForObject(
				domainServices + "/rest/api/deposit/statistic-by-deposit-account/" + id, map, Transaction[].class));
		model.addAttribute("transactions", transactions);
		model.addAttribute("deposit", "deposit");
		return "statistic/detail_deposit_account";
	}
	
	@GetMapping("/customer-by-account/export/deposit-account")
	public void exportTransactionInDepositAccount_(Model model, HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		HttpSession httpSession = req.getSession();
		String idAccount = "";
		Customer customer = new Customer();
		if (httpSession.getAttribute("customerStatistic_") != null)
			customer = (Customer) httpSession.getAttribute("customerStatistic_");
		if (httpSession.getAttribute("accountId_deposit_statistic_") != null)
			idAccount = httpSession.getAttribute("accountId_deposit_statistic_").toString();

		Map<String, Object> map = new HashMap<String, Object>();
		if (httpSession.getAttribute("fromDateExportDeposit_") != null) {
			String fromDate = httpSession.getAttribute("fromDateExportDeposit_").toString();
			map.put("fromDate", fromDate);
		}
		if (httpSession.getAttribute("toDateExportDeposit_") != null) {
			String toDate = httpSession.getAttribute("toDateExportDeposit_").toString();
			map.put("toDate", toDate);
		}
		List<Transaction> transactions = Arrays.asList(
				rest.postForObject(domainServices + "/rest/api/deposit/export-with-deposit-account/" + idAccount, map,
						Transaction[].class));

		httpSession.removeAttribute("fromDateExportDeposit_");
		httpSession.removeAttribute("toDateExportDeposit_");

		resp.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=transactions_" + currentDateTime + ".xlsx";
		resp.setHeader(headerKey, headerValue);

		ExcelExporter excelExporter = new ExcelExporter(transactions, customer, idAccount, 2);
		excelExporter.export(resp);

	}

	@GetMapping("/customer-by-account/export/credit-account")
	public void exportTransactionInCreditAccount_(Model model, HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		HttpSession httpSession = req.getSession();
		String idAccount = "";
		Customer customer = new Customer();
		if (httpSession.getAttribute("customerStatistic_") != null)
			customer = (Customer) httpSession.getAttribute("customerStatistic_");
		if (httpSession.getAttribute("accountId_credit_statistic_") != null)
			idAccount = httpSession.getAttribute("accountId_credit_statistic_").toString();

		Map<String, Object> map = new HashMap<String, Object>();
		if (httpSession.getAttribute("fromDateExportCredit_") != null) {
			String fromDate = httpSession.getAttribute("fromDateExportCredit_").toString();
			map.put("fromDate", fromDate);
		}
		if (httpSession.getAttribute("toDateExportCredit_") != null) {
			String toDate = httpSession.getAttribute("toDateExportCredit_").toString();
			map.put("toDate", toDate);
		}

		httpSession.removeAttribute("fromDateExportCredit_");
		httpSession.removeAttribute("toDateExportCredit_");

		List<Transaction> transactions = Arrays.asList(rest.postForObject(
				domainServices + "/rest/api/credit/export-with-credit-account/" + idAccount, map, Transaction[].class));

		resp.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=transactions_" + currentDateTime + ".xlsx";
		resp.setHeader(headerKey, headerValue);

		ExcelExporter excelExporter = new ExcelExporter(transactions, customer, idAccount, 1);
		excelExporter.export(resp);

	}
}
