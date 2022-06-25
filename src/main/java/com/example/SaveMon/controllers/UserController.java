package com.example.SaveMon.controllers;

import com.example.SaveMon.domain.Countries;
import com.example.SaveMon.domain.User;
import com.example.SaveMon.enums.DirectionPayment;
import com.example.SaveMon.enums.Roles;
import com.example.SaveMon.repo.FamilyRepository;
import com.example.SaveMon.repo.UserRepository;
import com.example.SaveMon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Lob;
import javax.transaction.Transactional;
import java.io.IOException;

@Controller
public class UserController {
    @Autowired
    UserRepository userRepo;
    @Autowired
    UserService userServ;
    @Autowired
    FamilyRepository famRep;

    @PostMapping("/registration")
    public String register(@RequestParam("login") String login,
                           @RequestParam("password1") String password1,
                           @RequestParam("password2") String password2,
                           @RequestParam("email") String email,
                           @RequestParam("country") String country,
                           @RequestParam("sex") String sex,
                           @RequestParam("birthday") String birthday,
                           Model model) {
        String result = userServ.registryNewUser(login, password1, password2, email, country, sex, birthday);
        if (result.equals("Registration successful!")) {
            return "login";
        } else {
            model.addAttribute("error", result);
            model.addAttribute("countries", Countries.countries);
            return "registration";
        }

    }

    @GetMapping("/registration")
    public String registerPage(Model model) {
        model.addAttribute("error", "");
        model.addAttribute("countries", Countries.countries);
        return "registration";
    }


    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal User user) {
        user = userRepo.findById(user.getId()).get();
        model.addAttribute("user", user);
        return "profile";
    }


    @GetMapping("/profile/edit")
    public String profileEdit(Model model, @AuthenticationPrincipal User user) {
        user = userRepo.findById(user.getId()).get();
        model.addAttribute("user", user);
        model.addAttribute("errors", "");
        model.addAttribute("countries", Countries.countries);
        return "profile-edit";
    }

    @PostMapping("/profile/edit")
    public String saveProfileChanges(Model model, @RequestParam("newImage") MultipartFile file, @AuthenticationPrincipal User user,
                                     @RequestParam("newLogin") String newLogin,
                                     @RequestParam("newPassword1") String newPassword1, @RequestParam("newPassword2") String newPassword2,
                                     @RequestParam("newEmail") String newEmail,
                                     @RequestParam("newName") String newName,
                                     @RequestParam("newSurname") String newSurname,
                                     @RequestParam("newBirthday") String newBirthday,
                                     @RequestParam("newSex") String newSex,
                                     @RequestParam("newCountry") String newCountry,
                                     @RequestParam("newNumber") String newNumber) throws IOException {
        user = userRepo.findById(user.getId()).get();
        String result = userServ.editUser(file, newLogin, newPassword1, newPassword2, newEmail, newNumber, newName, newSurname, newBirthday, newSex, newCountry, user, Roles.USER);
        if (result.equals("Data successful updated")) {
            user = userRepo.findById(user.getId()).get();
            model.addAttribute("user", user);
            return "redirect:/profile";
        } else {
            model.addAttribute("countries", Countries.countries);
            model.addAttribute("user", user);
            model.addAttribute("errors", result);
            return "profile-edit";
        }
    }

}
