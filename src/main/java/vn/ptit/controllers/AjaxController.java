package vn.ptit.controllers;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import vn.ptit.models.CreditAccount;
import vn.ptit.models.Customer;
import vn.ptit.utils.AjaxResponse;

@RestController
@RequestMapping("/rest/api")
public class AjaxController {
	private RestTemplate rest = new RestTemplate();

	@Value("${domain.services.name}")
	private String domainServices;

	@PostMapping(value = "/check/account")
	public ResponseEntity<AjaxResponse> checkAccount(@RequestBody final Map<String, Object> data, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		if(data.get("number").toString().isEmpty()) return ResponseEntity.ok(new AjaxResponse(222, null));
		String accountId = data.get("number").toString();
		CreditAccount creditAccount = rest.getForObject(domainServices+"/rest/api/credit-account/check-account/"+accountId, CreditAccount.class);
		if(creditAccount ==null) {
			return ResponseEntity.ok(new AjaxResponse(111, null));
		}
		Customer customer = rest.getForObject(domainServices+"/rest/api/customer/find-by-account/"+accountId, Customer.class);
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute("creditAccount_Pay", creditAccount);
		httpSession.setAttribute("customerOtherPay", customer);
		return ResponseEntity.ok(new AjaxResponse(333, customer.getFullName()));
	}

}
