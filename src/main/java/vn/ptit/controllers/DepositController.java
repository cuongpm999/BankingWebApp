package vn.ptit.controllers;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import vn.ptit.models.Customer;
import vn.ptit.models.DepositAccount;
import vn.ptit.models.Employee;
import vn.ptit.models.Transaction;
import vn.ptit.services.SendMailService;
import vn.ptit.utils.HelperTransaction;

@Controller
@RequestMapping("/admin/transaction/deposit")
public class DepositController {

	private RestTemplate rest = new RestTemplate();

	@Autowired
	SendMailService sendMailService;

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
		return "deposit/search_customer";
	}

	@GetMapping("/detail-customer/{id}")
	public String viewCustomerDetail(@PathVariable("id") int id, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		Customer customer = rest.getForObject(domainServices + "/rest/api/customer/find-by-id/" + id, Customer.class);
		model.addAttribute("customer", customer);
		req.getSession().setAttribute("customerDeposit_deposit", customer);
		List<DepositAccount> depositAccounts = Arrays.asList(rest.getForObject(
				domainServices + "/rest/api/deposit-account/find-by-customer/" + id, DepositAccount[].class));
		model.addAttribute("depositAccounts", depositAccounts);
		return "deposit/detail_customer";
	}

	@GetMapping("/detail-account/{id}")
	public String viewDetailAccount(@PathVariable("id") String id, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
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
		List<Transaction> transactions = Arrays.asList(rest.postForObject(
				domainServices + "/rest/api/deposit/find-by-deposit-account/" + id, map, Transaction[].class));
		model.addAttribute("transactions", transactions);
		HttpSession httpSession = req.getSession();
		httpSession.setAttribute("accountId_deposit", id);
		if (httpSession.getAttribute("customerDeposit_deposit") == null)
			return "redirect:/admin/transaction/deposit";

		return "deposit/detail_account";
	}

	@GetMapping("/save-money")
	public String saveMoney(Model model, HttpServletRequest req, HttpServletResponse resp) {
		Transaction transaction = new Transaction();
		HttpSession httpSession = req.getSession();
		if (httpSession.getAttribute("customerDeposit_deposit") == null)
			return "redirect:/admin/transaction/deposit";
		if (httpSession.getAttribute("accountId_deposit") == null)
			return "redirect:/admin/transaction/deposit";
		String id = (String) req.getSession().getAttribute("accountId_deposit");
		DepositAccount depositAccount = rest.getForObject(domainServices + "/rest/api/deposit-account/find-by-id/" + id,
				DepositAccount.class);

		req.getSession().setAttribute("depositAccount_deposit", depositAccount);
		model.addAttribute("transaction", transaction);
		return "deposit/save_money";
	}

	@PostMapping("/save-money")
	public String saveMoneyPost(@ModelAttribute("transaction") Transaction transaction, Model model,
			HttpServletRequest req, HttpServletResponse resp) throws MessagingException {
		HttpSession httpSession = req.getSession();
		DepositAccount depositAccount = new DepositAccount();
		if (httpSession.getAttribute("customerDeposit_deposit") != null) {
			Customer customer = (Customer) httpSession.getAttribute("customerDeposit_deposit");
			transaction.setCustomer(customer);
		} else {
			return "redirect:/admin/transaction/deposit";
		}
		if (httpSession.getAttribute("depositAccount_deposit") != null) {
			depositAccount = (DepositAccount) httpSession.getAttribute("depositAccount_deposit");
			transaction.setDepositAccount(depositAccount);
		} else {
			return "redirect:/admin/transaction/deposit";
		}
		String usernameEmployee = (String) httpSession.getAttribute("usernameEmployee");
		Employee employee = rest.getForObject(domainServices + "/rest/api/employee/get/" + usernameEmployee,
				Employee.class);
		transaction.setEmployee(employee);
		transaction.setDateCreate(new Date());
		HelperTransaction helperTransaction = rest.postForObject(domainServices + "/rest/api/deposit/insert", transaction, HelperTransaction.class);
		if (helperTransaction.getStatus()==0) {
			model.addAttribute("transaction", transaction);
			model.addAttribute("status", "failed");
			return "deposit/save_money";
		}
//		sendMailService.sendMailDeposit(helperTransaction.getTransaction());
		return "redirect:/admin/transaction/deposit/detail-account/" + depositAccount.getId();
	}

	@GetMapping("/detail-transaction/{id}")
	public String viewTransactionDetail(@PathVariable("id") int id, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		HttpSession httpSession = req.getSession();
		if (httpSession.getAttribute("customerDeposit_deposit") == null)
			return "redirect:/admin/transaction/deposit";
		if (httpSession.getAttribute("accountId_deposit") == null)
			return "redirect:/admin/transaction/deposit";

		Transaction transaction = rest.getForObject(domainServices + "/rest/api/deposit/find-transaction-by-id/" + id,
				Transaction.class);
		model.addAttribute("transaction", transaction);
		return "deposit/detail_transaction";
	}

}
