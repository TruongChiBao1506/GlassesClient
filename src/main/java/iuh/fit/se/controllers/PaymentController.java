package iuh.fit.se.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaymentController {
	
	@GetMapping("/payment-result")
	public String paymentResult(@RequestParam String status, @RequestParam String txnRef, Model model) {
		System.out.println("Payment status: " + status);
		System.out.println("Transaction reference: " + txnRef);
		if(status.equals("success")) {
			model.addAttribute("status", status);
			model.addAttribute("message", "Thanh toán thành công");
			model.addAttribute("donhang", txnRef);
			return "payment-result";
		}
		else {
			model.addAttribute("status", status);
			model.addAttribute("message", "Thanh toán thất bại");
			model.addAttribute("donhang", txnRef);
			return "payment-result";
		}
		
	}
}
