package vn.ptit.controllers;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import vn.ptit.models.CreditAccount;
import vn.ptit.models.Customer;
import vn.ptit.models.DepositAccount;
import vn.ptit.models.Employee;
import vn.ptit.models.Transaction;

@Controller
@RequestMapping("/admin/transaction/credit")
public class CreditController {

	private RestTemplate rest = new RestTemplate();

	@Value("${domain.services.name}")
	private String domainServices;
	
	@GetMapping()
	public String viewSearchCustomer(Model model) {
		List<Customer> customers = Arrays
				.asList(rest.getForObject(domainServices + "/rest/api/customer/find-all", Customer[].class));
		model.addAttribute("customers", customers);
		return "credit/search_customer";
	}
	
	@GetMapping("/detail-customer/{id}")
	public String viewCustomerDetail(@PathVariable("id") int id, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		Customer customer = rest.getForObject(domainServices + "/rest/api/customer/find-by-id/" + id, Customer.class);
		model.addAttribute("customer", customer);
		req.getSession().setAttribute("customerCredit_credit", customer);
		List<CreditAccount> creditAccounts = Arrays.asList(rest.getForObject(
				domainServices + "/rest/api/credit-account/find-by-customer/" + id, CreditAccount[].class));
		model.addAttribute("creditAccounts", creditAccounts);
		return "credit/detail_customer";
	}
	
	@GetMapping("/detail-account/{id}")
	public String viewDetailAccount(@PathVariable("id") String id, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		List<Transaction> transactions = Arrays.asList(rest
				.getForObject(domainServices + "/rest/api/credit/find-by-credit-account/" + id, Transaction[].class));
		model.addAttribute("transactions", transactions);
		HttpSession httpSession = req.getSession();
		httpSession.setAttribute("accountId_credit", id);
		if (httpSession.getAttribute("customerCredit_credit") == null) return "redirect:/admin/transaction/credit";
		return "credit/detail_account";
	}
	
	@GetMapping("/deal")
	public String saveMoney(Model model, HttpServletRequest req, HttpServletResponse resp) {
		Transaction transaction = new Transaction();
		HttpSession httpSession = req.getSession();
		if(httpSession.getAttribute("customerCredit_credit")==null) return "redirect:/admin/transaction/credit";
		if(httpSession.getAttribute("accountId_credit")==null) return "redirect:/admin/transaction/credit";
		String id = (String) req.getSession().getAttribute("accountId_credit");
		CreditAccount creditAccount = rest.getForObject(domainServices + "/rest/api/credit-account/find-by-id/" + id,
				CreditAccount.class);
		req.getSession().setAttribute("creditAccount_credit", creditAccount);
		model.addAttribute("transaction", transaction);	
		return "credit/deal";
	}
	
	@PostMapping("/deal")
	public String saveMoneyPost(@ModelAttribute("transaction") Transaction transaction, Model model,
			HttpServletRequest req, HttpServletResponse resp) {
		HttpSession httpSession = req.getSession();
		CreditAccount creditAccount = new CreditAccount();
		if (httpSession.getAttribute("customerCredit_credit") != null) {
			Customer customer = (Customer) httpSession.getAttribute("customerCredit_credit");
			transaction.setCustomer(customer);
		} else {
			return "redirect:/admin/transaction/credit";
		}
		if (httpSession.getAttribute("creditAccount_credit") != null) {
			creditAccount = (CreditAccount) httpSession.getAttribute("creditAccount_credit");
			transaction.setCreditAccount(creditAccount);
		} else {
			return "redirect:/admin/transaction/credit";
		}
		String usernameEmployee = (String) httpSession.getAttribute("usernameEmployee");
		Employee employee = rest.getForObject(domainServices + "/rest/api/employee/get/" + usernameEmployee,
				Employee.class);
		transaction.setEmployee(employee);
		transaction.setDateCreate(new Date());
		boolean flag = rest.postForObject(domainServices + "/rest/api/credit/insert", transaction, Boolean.class);
		if(!flag) {
			model.addAttribute("transaction", transaction);
			model.addAttribute("status", "failed");
			return "credit/deal";
		}
		return "redirect:/admin/transaction/credit/detail-account/" + creditAccount.getId();
	}
	
	@GetMapping("/detail-transaction/{id}")
	public String viewTransactionDetail(@PathVariable("id") int id, Model model, HttpServletRequest req, HttpServletResponse resp) {
		HttpSession httpSession = req.getSession();
		if(httpSession.getAttribute("customerCredit_credit")==null) return "redirect:/admin/transaction/credit";
		if(httpSession.getAttribute("accountId_credit")==null) return "redirect:/admin/transaction/credit";
		
		Transaction transaction = rest.getForObject(domainServices + "/rest/api/credit/find-transaction-by-id/"+id, Transaction.class);
		model.addAttribute("transaction", transaction);
		return "credit/detail_transaction";
	}
	
}
