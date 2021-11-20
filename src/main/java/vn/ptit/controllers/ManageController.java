package vn.ptit.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/manage")
public class ManageController {

	@GetMapping
	public String viewHomeManage() {
		return "manage";
	}
}
