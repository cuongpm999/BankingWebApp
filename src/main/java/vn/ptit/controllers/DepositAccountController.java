package vn.ptit.controllers;

import java.security.SecureRandom;
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

import vn.ptit.models.BankAccountType;
import vn.ptit.models.CreatedBankAccount;
import vn.ptit.models.Customer;
import vn.ptit.models.DepositAccount;
import vn.ptit.models.Employee;
import vn.ptit.utils.HelperCreateBankAccount;
import vn.ptit.utils.RandomString;

@Controller
@RequestMapping("/admin/manage/deposit-account")
public class DepositAccountController {
	private RestTemplate rest = new RestTemplate();

	@Value("${domain.services.name}")
	private String domainServices;

	@GetMapping()
	public String viewSearchCustomer(Model model) {
		List<Customer> customers = Arrays
				.asList(rest.getForObject(domainServices + "/rest/api/customer/find-all", Customer[].class));
		model.addAttribute("customers", customers);

		return "deposit_account/search_customer";
	}

	@GetMapping("/detail/{id}")
	public String viewCustomerDetail(@PathVariable("id") int id, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		Customer customer = rest.getForObject(domainServices + "/rest/api/customer/find-by-id/" + id, Customer.class);
		model.addAttribute("customer", customer);
		req.getSession().setAttribute("customer_", customer);

		List<DepositAccount> depositAccounts = Arrays.asList(rest.getForObject(
				domainServices + "/rest/api/deposit-account/find-by-customer/" + id, DepositAccount[].class));
		model.addAttribute("depositAccounts", depositAccounts);

		return "deposit_account/detail_customer";
	}

	@GetMapping("/add")
	public String viewAddDepositAccount(Model model) {
		DepositAccount depositAccount = new DepositAccount();
		RandomString randomString = new RandomString(14, new SecureRandom(), RandomString.digits);
		depositAccount.setId(randomString.nextString());

		List<BankAccountType> bankAccountTypes = Arrays.asList(
				rest.getForObject(domainServices + "/rest/api/bank-account-type/find-all", BankAccountType[].class));

		model.addAttribute("bankAccountTypes", bankAccountTypes);
		model.addAttribute("depositAccount", depositAccount);

		return "deposit_account/add_deposit_account";
	}

	@PostMapping("/add")
	public String addDepositAccount(@ModelAttribute("depositAccount") DepositAccount depositAccount, Model model,
			HttpServletRequest req, HttpServletResponse resp) {
		depositAccount.setStatus(true);
		Customer customer = new Customer();
		HttpSession httpSession = req.getSession();
		if (httpSession.getAttribute("customer_") != null) {
			customer = (Customer) httpSession.getAttribute("customer_");
		} else
			return "redirect:/admin/manage/deposit-account";
		Employee employee = rest.getForObject(
				domainServices + "/rest/api/employee/get/" + httpSession.getAttribute("usernameEmployee").toString(),
				Employee.class);
		CreatedBankAccount createdBankAccount = new CreatedBankAccount();
		createdBankAccount.setBankAccount(depositAccount);
		createdBankAccount.setCustomer(customer);
		createdBankAccount.setEmployee(employee);
		createdBankAccount.setDateCreate(new Date());
		HelperCreateBankAccount helperCreateBankAccount = new HelperCreateBankAccount(null, depositAccount,
				createdBankAccount);
		rest.postForObject(domainServices + "/rest/api/deposit-account/insert", helperCreateBankAccount,
				DepositAccount.class);

		return "redirect:/admin/manage/deposit-account/detail/" + customer.getId();
	}

	@GetMapping("/edit/{id}")
	public String viewEditDepositAccount(@PathVariable("id") String id, Model model) {
		DepositAccount depositAccount = rest.getForObject(domainServices + "/rest/api/deposit-account/find-by-id/" + id,
				DepositAccount.class);

		List<BankAccountType> bankAccountTypes = Arrays.asList(
				rest.getForObject(domainServices + "/rest/api/bank-account-type/find-all", BankAccountType[].class));

		model.addAttribute("bankAccountTypes", bankAccountTypes);
		model.addAttribute("depositAccount", depositAccount);

		return "deposit_account/edit_deposit_account";
	}

	@GetMapping("/delete/{id}")
	public String deleteDepositAccount(@PathVariable("id") String id, Model model, HttpServletRequest req,
			HttpServletResponse resp) {
		Customer customer = new Customer();
		HttpSession httpSession = req.getSession();
		if (httpSession.getAttribute("customer_") != null) {
			customer = (Customer) httpSession.getAttribute("customer_");
		}
		rest.delete(domainServices + "/rest/api/deposit-account/delete-by-id/" + id);
		return "redirect:/admin/manage/deposit-account/detail/" + customer.getId();
	}

	@PostMapping("/edit")
	public String editDepositAccount(@ModelAttribute("depositAccount") DepositAccount depositAccount, Model model,
			HttpServletRequest req, HttpServletResponse resp) {
		Customer customer = new Customer();
		HttpSession httpSession = req.getSession();
		if (httpSession.getAttribute("customer_") != null) {
			customer = (Customer) httpSession.getAttribute("customer_");
		} else
			return "redirect:/admin/manage/deposit-account";
		depositAccount.setStatus(true);
		rest.postForObject(domainServices + "/rest/api/deposit-account/update", depositAccount, DepositAccount.class);

		return "redirect:/admin/manage/deposit-account/detail/" + customer.getId();
	}

}
