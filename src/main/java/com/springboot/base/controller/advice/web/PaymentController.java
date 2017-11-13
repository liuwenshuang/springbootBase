package com.springboot.base.controller.advice.web;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.springboot.base.data.entity.OrderInfo;
import com.springboot.base.service.PaypalService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/pay")
@Log4j
public class PaymentController {

    @Value("${web.url}")
    private String WEB_URL;

    private String PAYPAL_SUCCESS_URL = WEB_URL + "";

    private String PAYPAL_CANCEL_URL= WEB_URL + "";

    @Autowired
    private PaypalService paypalService;

    @RequestMapping(method = RequestMethod.POST)
    public String pay(@RequestBody @Validated OrderInfo order) throws Exception {
        try {
            Payment payment = paypalService.createPayment(order);
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return "redirect:" + links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.GET)
    public String cancelPay() {
        log.error("11111111111111111");
        return "cancel";
    }

    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) throws Exception {
        log.error("2222222222222222222");
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return "success";
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }

}