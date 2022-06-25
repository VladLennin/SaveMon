package com.example.SaveMon.controllers;

import com.example.SaveMon.domain.Payment;
import com.example.SaveMon.domain.Statistic;
import com.example.SaveMon.domain.User;
import com.example.SaveMon.repo.PaymentRepository;
import com.example.SaveMon.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MainRestController {

    @Autowired
    UserRepository userRepo;
    @Autowired
    PaymentRepository payRepo;

    @GetMapping("/get-statistic/{by}")
    public String transactions(@PathVariable(value = "by") String by) {
        return Statistic.getStat((List<Payment>) payRepo.findAll(), by);
    }

    @GetMapping("/get-small-statistic/{by}")
    public String transactionsSmall(@PathVariable(value = "by") String by) {
        return Statistic.getSmallStat((List<Payment>) payRepo.findAll(), by);
    }

    @RequestMapping("/get-statistic/telegram/{telegramId}")
    public String transactionsByUser(@PathVariable(value = "telegramId") long telegramId) {
        return Statistic.getStatByTelegramId(userRepo.findByTelegramId(telegramId));
    }

    @GetMapping("/check-user/by-telegram-id/{telegramId}")
    public String checkUser(@PathVariable(value = "telegramId") long telegramId) {
        if (userRepo.findByTelegramId(telegramId) != null) {
            return "true";
        } else return "false";
    }


    @PostMapping("/create/file")
    public String createFile(Model model, @RequestParam String bytes, @AuthenticationPrincipal User user) {
        System.out.println(bytes);
        model.addAttribute("bytes", bytes);
        model.addAttribute("user", user);
        return "main";
    }

    @PostMapping(value = "/add-payment/telegram", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public String addNewPaymentFromTelegram(@RequestBody Payment payment) {
       User user =  userRepo.findById(payment.getUser().getId()).get();
       user.addPayment(payment);
       userRepo.save(user);
        return "Successful";
    }

}



















