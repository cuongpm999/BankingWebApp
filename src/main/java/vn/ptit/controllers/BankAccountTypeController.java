package vn.ptit.controllers;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import vn.ptit.models.BankAccountType;

@Controller
@RequestMapping("/admin/manage/bank-account-type")
public class BankAccountTypeController {
	RestTemplate rest = new RestTemplate();

	@Value("${domain.services.name}")
	private String domainService;

	@GetMapping
	public String viewBankAccountType(ModelMap model, HttpServletRequest req, HttpServletResponse resp) {
		List<BankAccountType> bankAccountTypes = Arrays.asList(
				rest.getForObject(domainService + "/rest/api/bank-account-type/find-all", BankAccountType[].class));
		model.addAttribute("bankAccountTypes", bankAccountTypes);
		return "bank_account_type/manage_bank_account_type";
	}

	@GetMapping("/add")
	public String viewInsertBankAccountType(ModelMap model, HttpServletRequest req, HttpServletResponse resp) {
		model.addAttribute("bankAccountType", new BankAccountType());
		return "bank_account_type/add_bank_account_type";
	}

	@PostMapping("/add")
	public String insertBankAccountType(@ModelAttribute("bankAccountType") BankAccountType bankAccountType,
			ModelMap model, HttpServletRequest req, HttpServletResponse resp) {
		bankAccountType.setStatus(true);
		rest.postForObject(domainService + "/rest/api/bank-account-type/insert", bankAccountType,
				BankAccountType.class);
		model.addAttribute("bankAccountType", new BankAccountType());
		return "bank_account_type/add_bank_account_type";
	}

	@GetMapping("/edit/{bankAccountTypeID}")
	public String viewEditBankAccoutType(@PathVariable("bankAccountTypeID") int bankAccountTypeID, ModelMap model,
			HttpServletRequest req, HttpServletResponse resp) {
		BankAccountType bankAccountType = rest.getForObject(
				domainService + "/rest/api/bank-account-type/find-by-id/" + bankAccountTypeID, BankAccountType.class);
		model.addAttribute("bankAccountType", bankAccountType);
		return "bank_account_type/edit_bank_account_type";
	}

	@PostMapping("/edit")
	public String editBankAccountType(@ModelAttribute("bankAccountType") BankAccountType bankAccountType,
			ModelMap model, HttpServletRequest req, HttpServletResponse resp) {
		bankAccountType.setStatus(true);
		rest.postForObject(domainService + "/rest/api/bank-account-type/insert", bankAccountType,
				BankAccountType.class);
		return "redirect:/admin/manage/bank-account-type";
	}
	
	@GetMapping("/delete/{bankAccountTypeID}")
	public String deleteEmployee(@PathVariable("bankAccountTypeID") int bankAccountTypeID, Model model) {
		rest.delete(domainService + "/rest/api/bank-account-type/delete-by-id/" + bankAccountTypeID);
		return "redirect:/admin/manage/bank-account-type";
	}
	
}
