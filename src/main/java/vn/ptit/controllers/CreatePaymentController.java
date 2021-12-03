package vn.ptit.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

import vn.ptit.models.CreditAccount;
import vn.ptit.models.Customer;
import vn.ptit.models.DepositAccount;
import vn.ptit.models.Employee;
import vn.ptit.models.Transaction;
import vn.ptit.utils.SendMailService;

@Controller
@RequestMapping("/admin/transaction/create-payment")
public class CreatePaymentController {
	private RestTemplate rest = new RestTemplate();

	@Autowired
	SendMailService sendMailService;

	@Value("${domain.services.name}")
	private String domainServices;

	@GetMapping()
	public String viewSearchCustomer(Model model) {
		List<Customer> customers = Arrays
				.asList(rest.getForObject(domainServices + "/rest/api/customer/find-all", Customer[].class));
		model.addAttribute("customers", customers);

		return "payment/search_customer";
	}

	@GetMapping("/detail-customer/{id}")
	public String viewCustomerDetail(@PathVariable("id") int id, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		Customer customer = rest.getForObject(domainServices + "/rest/api/customer/find-by-id/" + id, Customer.class);
		model.addAttribute("customer", customer);
		req.getSession().setAttribute("customerPay", customer);

		List<DepositAccount> depositAccounts = Arrays.asList(rest.getForObject(
				domainServices + "/rest/api/deposit-account/find-by-customer/" + id, DepositAccount[].class));
		model.addAttribute("depositAccounts", depositAccounts);

		return "payment/detail_customer";
	}

	@GetMapping("/detail-account/{id}")
	public String viewAccountDetail(@PathVariable("id") String id, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		HttpSession httpSession = req.getSession();
		if (httpSession.getAttribute("customerPay") == null)
			return "redirect:/admin/transaction/create-payment";
		List<Transaction> transactions = Arrays.asList(rest.getForObject(
				domainServices + "/rest/api/create-payment/find-payment-account/" + id, Transaction[].class));
		model.addAttribute("transactions", transactions);
		req.getSession().setAttribute("depositAccountId_Pay", id);
		return "payment/detail_account";
	}

	@GetMapping("/create")
	public String viewPay(Model model, HttpServletRequest req, HttpServletResponse resp) {
		HttpSession httpSession = req.getSession();
		if (httpSession.getAttribute("customerPay") == null)
			return "redirect:/admin/transaction/create-payment";
		if (httpSession.getAttribute("depositAccountId_Pay") == null)
			return "redirect:/admin/transaction/create-payment";
		String id = httpSession.getAttribute("depositAccountId_Pay").toString();
		DepositAccount depositAccount = rest.getForObject(domainServices + "/rest/api/deposit-account/find-by-id/" + id,
				DepositAccount.class);
		req.getSession().setAttribute("depositAccount_Pay", depositAccount);

		Transaction transaction = new Transaction();
		model.addAttribute("transaction", transaction);

		return "payment/create_payment";
	}

	@PostMapping("/create")
	public String pay(@ModelAttribute("transaction") Transaction transaction, Model model, HttpServletRequest req,
			HttpServletResponse resp) throws MessagingException, IOException {
		HttpSession httpSession = req.getSession();
		if (httpSession.getAttribute("customerPay") == null)
			return "redirect:/admin/transaction/create-payment";
		if (httpSession.getAttribute("depositAccount_Pay") == null)
			return "redirect:/admin/transaction/create-payment";

		if (httpSession.getAttribute("creditAccount_Pay") == null) {
			model.addAttribute("transaction", transaction);
			model.addAttribute("status", "selectAccount");
			httpSession.removeAttribute("creditAccount_Pay");
			return "payment/create_payment";
		}

		Customer customer = (Customer) httpSession.getAttribute("customerPay");
		DepositAccount depositAccount = (DepositAccount) httpSession.getAttribute("depositAccount_Pay");
		CreditAccount creditAccount = (CreditAccount) httpSession.getAttribute("creditAccount_Pay");
		Employee employee = rest.getForObject(
				domainServices + "/rest/api/employee/get/" + httpSession.getAttribute("usernameEmployee").toString(),
				Employee.class);
		;
		transaction.setEmployee(employee);
		transaction.setCustomer(customer);
		transaction.setDepositAccount(depositAccount);
		transaction.setCreditAccount(creditAccount);
		transaction.setDateCreate(new Date());

		boolean flag = rest.postForObject(domainServices + "/rest/api/create-payment/insert", transaction,
				Boolean.class);
		if (!flag) {
			model.addAttribute("transaction", transaction);
			model.addAttribute("status", "failed");
			httpSession.removeAttribute("creditAccount_Pay");
			return "payment/create_payment";
		}

		sendMailService.sendMailPayment(transaction, (Customer) httpSession.getAttribute("customerOtherPay"));

		httpSession.removeAttribute("creditAccount_Pay");
		return "redirect:/admin/transaction/create-payment/detail-account/" + depositAccount.getId();
	}

	@GetMapping("/detail-transaction/{id}")
	public String viewTransactionDetail(@PathVariable("id") int id, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		HttpSession httpSession = req.getSession();
		if (httpSession.getAttribute("customerPay") == null)
			return "redirect:/admin/transaction/create-payment";
		if (httpSession.getAttribute("depositAccountId_Pay") == null)
			return "redirect:/admin/transaction/create-payment";

		Transaction transaction = rest.getForObject(
				domainServices + "/rest/api/create-payment/find-transaction-by-id/" + id, Transaction.class);
		model.addAttribute("transaction", transaction);
		return "payment/detail_transaction";
	}

}
