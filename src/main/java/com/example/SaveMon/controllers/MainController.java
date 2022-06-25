package com.example.SaveMon.controllers;

import com.example.SaveMon.domain.Payment;
import com.example.SaveMon.domain.User;
import com.example.SaveMon.enums.DirectionPayment;
import com.example.SaveMon.enums.Roles;
import com.example.SaveMon.enums.TypePayment;
import com.example.SaveMon.repo.PaymentRepository;
import com.example.SaveMon.repo.UserRepository;
import com.example.SaveMon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class MainController {
    @Autowired
    UserRepository userRepo;

    @Autowired
    UserService userServ;

    @GetMapping("/")
    public String mainPage(Model model, @AuthenticationPrincipal User user) {
        int count = 0;
        List<User> users = (List<User>) userRepo.findAll();
        model.addAttribute("usersCount",users.size() );
        model.addAttribute("user", user);
        return "main";
    }


}
