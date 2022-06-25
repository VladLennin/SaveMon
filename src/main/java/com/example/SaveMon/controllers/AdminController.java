package com.example.SaveMon.controllers;

import com.example.SaveMon.domain.Countries;
import com.example.SaveMon.domain.User;
import com.example.SaveMon.enums.Roles;
import com.example.SaveMon.repo.UserRepository;
import com.example.SaveMon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Controller
public class AdminController {

    @Autowired
    public UserRepository userRepo;

    @Autowired
    public UserService userServ;

    private static String lastSort = "";

    @GetMapping("/admin-page")
    public String getAdminPage(Model model, @AuthenticationPrincipal User user) {
        List<User> users = (List<User>) userRepo.findAll();
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("user", user);

        return "admin-page";
    }

    @GetMapping("/admin-page/edit/{id}")
    public String editUserPage(Model model, @PathVariable(value = "id") long id, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        model.addAttribute("userT", userRepo.findById(id).get());
        model.addAttribute("errors", "");
        model.addAttribute("countries", Countries.countries);
        return "profile-edit-admin";
    }

    @PostMapping("/admin-page/profile/edit/{id}")
    public String saveProfileChanges(Model model, @RequestParam("newImage") MultipartFile file, @AuthenticationPrincipal User user,
                                     @RequestParam("newLogin") String newLogin,
                                     @RequestParam("newPassword1") String newPassword1, @RequestParam("newPassword2") String newPassword2,
                                     @RequestParam("newEmail") String newEmail,
                                     @RequestParam("newName") String newName,
                                     @RequestParam("newSurname") String newSurname,
                                     @RequestParam("newBirthday") String newBirthday,
                                     @RequestParam("newSex") String newSex,
                                     @RequestParam("newCountry") String newCountry,
                                     @RequestParam("newNumber") String newNumber,
                                     @RequestParam("newRole") String role,
                                     @PathVariable(value = "id") long id
    ) throws IOException {
        user = userRepo.findById(user.getId()).get();
        User userT = userRepo.findById(id).get();
        String result = userServ.editUser(file, newLogin, newPassword1, newPassword2, newEmail, newNumber, newName, newSurname, newBirthday, newSex, newCountry, userT, Roles.valueOf(role));
        if (result.equals("Data successful updated")) {
            userT = userRepo.findById(id).get();
            model.addAttribute("users", userRepo.findAll());
            model.addAttribute("user", user);
            return "admin-page";
        } else {
            model.addAttribute("countries", Countries.countries);
            model.addAttribute("user", user);
            model.addAttribute("userT", userT);
            model.addAttribute("errors", result);
            return "profile-edit-admin";
        }
    }

    @PostMapping("/admin-page/remove/{id}")
    public String removeUser(Model model, @PathVariable(value = "id") long id, @AuthenticationPrincipal User user) {
        User userT = userRepo.findById(id).get();
        userRepo.delete(userT);
        return "redirect:/admin-page";
    }

    @PostMapping("/admin-page/search")
    public String searchUsers(Model model, @RequestParam("data") String data, @AuthenticationPrincipal User user) {
        List<User> users = (List<User>) userRepo.findAll();
        List<User> resultSearching = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).toString().toLowerCase(Locale.ROOT).contains(data.toLowerCase(Locale.ROOT))) {
                resultSearching.add(users.get(i));
            }
        }
        model.addAttribute("users", resultSearching);
        model.addAttribute("user", user);
        return "admin-page";
    }

    @PostMapping("/admin-page/sort")
    public String sortBy(Model model, @RequestParam("sortBy") String sortBy, @AuthenticationPrincipal User user) {
        List<User> users = (List<User>) userRepo.findAll();
        List<User> sortedUsers = new ArrayList<>();


        switch (sortBy) {
            case "id":
                sortedUsers = users.stream().sorted(Comparator.comparing(User::getId)).collect(Collectors.toList());
                break;
            case "login":
                sortedUsers = users.stream().sorted(Comparator.comparing(User::getLogin)).collect(Collectors.toList());
                break;
            case "password":
                sortedUsers = users.stream().sorted(Comparator.comparing(User::getPassword)).collect(Collectors.toList());
                break;
            case "email":
                sortedUsers = users.stream().sorted(Comparator.comparing(User::getEmail)).collect(Collectors.toList());
                break;
            case "number":
                sortedUsers = users.stream().sorted(Comparator.comparing(User::getNumber)).collect(Collectors.toList());
                break;
            case "country":
                sortedUsers = users.stream().sorted(Comparator.comparing(User::getCountry)).collect(Collectors.toList());
                break;
            case "name":
                sortedUsers = users.stream().sorted(Comparator.comparing(User::getName)).collect(Collectors.toList());
                break;
            case "surname":
                sortedUsers = users.stream().sorted(Comparator.comparing(User::getSurname)).collect(Collectors.toList());
                break;
            case "birthday":
                sortedUsers = users.stream().sorted(Comparator.comparing(User::getBirthday)).collect(Collectors.toList());
                break;
            case "role":
                sortedUsers = users.stream().sorted(Comparator.comparing(User::getRole)).collect(Collectors.toList());
                break;
            case "familyId":
                sortedUsers = users.stream().sorted(Comparator.comparing(User::getFamilyId)).collect(Collectors.toList());
                break;
            case "sex":
                sortedUsers = users.stream().sorted(Comparator.comparing(User::getSex)).collect(Collectors.toList());
                break;
            case "countPayments":
                sortedUsers = users.stream().sorted(Comparator.comparing(User::getCountPayments)).collect(Collectors.toList());
                break;

        }
        if (sortBy.equals(lastSort)) {
            Collections.reverse(sortedUsers);
            lastSort = "";
        }else {
            lastSort = sortBy;
        }
        model.addAttribute("users", sortedUsers);
        model.addAttribute("user", user);
        return "admin-page";
    }
}
