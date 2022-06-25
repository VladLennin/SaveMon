package com.example.SaveMon.controllers;


import com.example.SaveMon.domain.Family;
import com.example.SaveMon.domain.Request;
import com.example.SaveMon.domain.User;
import com.example.SaveMon.repo.FamilyRepository;
import com.example.SaveMon.repo.RequestRepository;
import com.example.SaveMon.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class FamilyController {

    @Autowired
    UserRepository userRepo;
    @Autowired
    RequestRepository reqRepo;
    @Autowired
    FamilyRepository famRepo;

    @GetMapping("/family")
    public String familyPage(Model model, @AuthenticationPrincipal User user) {
        user = userRepo.findById(user.getId()).get();
        model.addAttribute("user", user);

        List<User> users = (List<User>) userRepo.findAll();
        users = users.stream().filter(s -> s.getCurrentFamily() == null).collect(Collectors.toList());

        List<Request> sendedRequests = reqRepo.findByUserFromIdAndIsActive(user.getId(), true);
        List<User> alreadySended = new ArrayList<>();
        for (int i = 0; i < sendedRequests.size(); i++) {
            alreadySended.add(userRepo.findById(sendedRequests.get(i).getUserToId()).get());
        }

        for (int i = 0; i < alreadySended.size(); i++) {
            users.remove(alreadySended.get(i));
        }
        users.remove(user);

        model.addAttribute("users", users);


        if (user.getCurrentFamily() != null) {
            model.addAttribute("members", user.getCurrentFamily().getMembers());
        }
        model.addAttribute("requests", user.getRequests());

        List<Request> requestFromMe = reqRepo.findByUserFromId(user.getId());

        model.addAttribute("requestFromMe", requestFromMe);
        model.addAttribute("FamilyController",this);
        return "family-main";
    }

    public User getUserTo(long id){
        return userRepo.findById(id).get();
    }

    @PostMapping("/family/send-request/{id}")
    public String sendRequest(Model model, @PathVariable(value = "id") long idUser, @AuthenticationPrincipal User user) {
        user = userRepo.findById(user.getId()).get();
        User userTo = userRepo.findById(idUser).get();
        Request req = new Request(user, userTo);
        userTo.getRequests().add(req);
        userRepo.save(userTo);
        userRepo.save(user);
        return "redirect:/family";
    }

    @PostMapping("/family/create")
    public String createFamily(Model model, @RequestParam("familyName") String name, @AuthenticationPrincipal User user) {
        user = userRepo.findById(user.getId()).get();
        Family family = new Family(user, name);
        user.setCurrentFamily(family);
        userRepo.save(user);


        return "redirect:/family";
    }

    @Transactional
    @PostMapping("family/remove-member/{id}")
    public String removeMEmber(Model model, @AuthenticationPrincipal User user, @PathVariable(value = "id") long id) {
        user = userRepo.findById(user.getId()).get();
        User member = userRepo.findById(id).get();
        user.getCurrentFamily().getMembers().remove(member);
        member.setCurrentFamily(null);
        userRepo.save(user);
        userRepo.save(member);
        return "redirect:/family";
    }

    @Transactional
    @PostMapping("/family/request-answer/{id}")
    public String answerRequest(Model model, @AuthenticationPrincipal User user,
                                @RequestParam("answer") String asnwer, @PathVariable(value = "id") long reqId) {

        Request request = reqRepo.findById(reqId).get();
        user = userRepo.findById(user.getId()).get();
        User userFrom = userRepo.findById(request.getUserFrom().getId()).get();

        if (asnwer.equals("accept")) {
            request.setActive(false);
            request.setStatus("Accepted");
            user.setCurrentFamily(userFrom.getCurrentFamily());
            user.getCurrentFamily().addMember(user);
            user.getRequests().remove(request);
            userRepo.save(user);
        } else if (asnwer.equals("deny")) {
            request.setActive(false);
            request.setStatus("Denied");
            user.getRequests().remove(request);
            userRepo.save(user);
        }

        return "redirect:/family";
    }


}
