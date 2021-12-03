package vn.ptit.controllers;

import java.security.SecureRandom;
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

import vn.ptit.models.BankAccountType;
import vn.ptit.models.CreatedBankAccount;
import vn.ptit.models.CreditAccount;
import vn.ptit.models.Customer;
import vn.ptit.models.Employee;
import vn.ptit.utils.HelperCreateBankAccount;
import vn.ptit.utils.RandomString;
import vn.ptit.utils.SendMailService;

@Controller
@RequestMapping("/admin/manage/credit-account")
public class CreditAccountController {
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

		return "credit_account/search_customer";
	}

	@GetMapping("/detail/{id}")
	public String viewCustomerDetail(@PathVariable("id") int id, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		Customer customer = rest.getForObject(domainServices + "/rest/api/customer/find-by-id/" + id, Customer.class);
		model.addAttribute("customer", customer);
		req.getSession().setAttribute("customer_", customer);

		List<CreditAccount> creditAccounts = Arrays.asList(rest.getForObject(
				domainServices + "/rest/api/credit-account/find-by-customer/" + id, CreditAccount[].class));
		model.addAttribute("creditAccounts", creditAccounts);

		return "credit_account/detail_customer";
	}

	@GetMapping("/add")
	public String viewAddCreditAccount(Model model, HttpServletRequest req, HttpServletResponse resp) {
		Customer customer = new Customer();
		HttpSession httpSession = req.getSession();
		if (httpSession.getAttribute("customer_") != null) {
			customer = (Customer) httpSession.getAttribute("customer_");
		}
		boolean flag = rest.getForObject(domainServices + "/rest/api/credit-account/count/" + customer.getId(),
				Boolean.class);
		if (!flag)
			return "redirect:/admin/manage/credit-account/detail/" + customer.getId();

		CreditAccount creditAccount = new CreditAccount();
		RandomString randomString = new RandomString(14, new SecureRandom(), RandomString.digits);
		creditAccount.setId(randomString.nextString());

		List<BankAccountType> bankAccountTypes = Arrays.asList(
				rest.getForObject(domainServices + "/rest/api/bank-account-type/find-all", BankAccountType[].class));

		model.addAttribute("bankAccountTypes", bankAccountTypes);
		model.addAttribute("creditAccount", creditAccount);

		return "credit_account/add_credit_account";
	}

	@PostMapping("/add")
	public String addCreditAccount(@ModelAttribute("creditAccount") CreditAccount creditAccount, Model model,
			HttpServletRequest req, HttpServletResponse resp) throws MessagingException {
		creditAccount.setStatus(true);
		Customer customer = new Customer();
		HttpSession httpSession = req.getSession();
		if (httpSession.getAttribute("customer_") != null) {
			customer = (Customer) httpSession.getAttribute("customer_");
		} else
			return "redirect:/admin/manage/credit-account";
		Employee employee = rest.getForObject(
				domainServices + "/rest/api/employee/get/" + httpSession.getAttribute("usernameEmployee").toString(),
				Employee.class);
		CreatedBankAccount createdBankAccount = new CreatedBankAccount();
		createdBankAccount.setBankAccount(creditAccount);
		createdBankAccount.setCustomer(customer);
		createdBankAccount.setEmployee(employee);
		createdBankAccount.setDateCreate(new Date());
		HelperCreateBankAccount helperCreateBankAccount = new HelperCreateBankAccount(creditAccount, null,
				createdBankAccount);
		rest.postForObject(domainServices + "/rest/api/credit-account/insert", helperCreateBankAccount,
				CreditAccount.class);

		sendMailService.sendMailCreateAccount(null, creditAccount, customer);
		return "redirect:/admin/manage/credit-account/detail/" + customer.getId();
	}

	@GetMapping("/edit/{id}")
	public String viewEditCreditAccount(@PathVariable("id") String id, Model model) {
		CreditAccount creditAccount = rest.getForObject(domainServices + "/rest/api/credit-account/find-by-id/" + id,
				CreditAccount.class);

		List<BankAccountType> bankAccountTypes = Arrays.asList(
				rest.getForObject(domainServices + "/rest/api/bank-account-type/find-all", BankAccountType[].class));

		model.addAttribute("bankAccountTypes", bankAccountTypes);
		model.addAttribute("creditAccount", creditAccount);

		return "credit_account/edit_credit_account";
	}

	@GetMapping("/delete/{id}")
	public String deleteCustomer(@PathVariable("id") String id, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		Customer customer = new Customer();
		HttpSession httpSession = req.getSession();
		if (httpSession.getAttribute("customer_") != null) {
			customer = (Customer) httpSession.getAttribute("customer_");
		} else
			return "redirect:/admin/manage/credit-account";
		rest.delete(domainServices + "/rest/api/credit-account/delete-by-id/" + id);
		return "redirect:/admin/manage/credit-account/detail/" + customer.getId();
	}

	@PostMapping("/edit")
	public String editCreditAccount(@ModelAttribute("creditAccount") CreditAccount creditAccount, Model model,
			HttpServletRequest req, HttpServletResponse resp) {
		Customer customer = new Customer();
		HttpSession httpSession = req.getSession();
		if (httpSession.getAttribute("customer_") != null) {
			customer = (Customer) httpSession.getAttribute("customer_");
		} else
			return "redirect:/admin/manage/credit-account";
		creditAccount.setStatus(true);
		rest.postForObject(domainServices + "/rest/api/credit-account/update", creditAccount, CreditAccount.class);

		return "redirect:/admin/manage/credit-account/detail/" + customer.getId();
	}

}
