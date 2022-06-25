package com.example.SaveMon.controllers;

import com.example.SaveMon.domain.Payment;
import com.example.SaveMon.domain.User;
import com.example.SaveMon.enums.AgeZones;
import com.example.SaveMon.enums.DirectionPayment;
import com.example.SaveMon.enums.PeriodPayments;
import com.example.SaveMon.enums.TypePayment;
import com.example.SaveMon.repo.PaymentRepository;
import com.example.SaveMon.repo.UserRepository;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;
@Transactional
@Controller
public class PaymentController {

    @Autowired
    PaymentRepository paymentRepo;
    @Autowired
    UserRepository userRepo;

    @GetMapping("/add-payment")
    public String addPayment(Model model, @AuthenticationPrincipal User user) {
        user = userRepo.findById(user.getId()).get();
        model.addAttribute("user", user);
        List<Payment> last5Payments = user.getPayments().stream().sorted(Comparator.comparing(Payment::getTime)).collect(Collectors.toList());
        Collections.reverse(last5Payments);

        List<Payment> last3PaymentsOfFamily = null;
        if (user.getCurrentFamily() != null) {
            last3PaymentsOfFamily = user.getCurrentFamily().getAllPayments(user).stream().sorted(Comparator.comparing(Payment::getTime)).collect(Collectors.toList());
            Collections.reverse(last3PaymentsOfFamily);
            last3PaymentsOfFamily = last3PaymentsOfFamily.stream().limit(3).collect(Collectors.toList());
        }

        last5Payments = last5Payments.stream().limit(5).collect(Collectors.toList());
        model.addAttribute("last5Payments", last5Payments);
        model.addAttribute("directionPayments", DirectionPayment.values());
        model.addAttribute("last3PaymentsOfFamily", last3PaymentsOfFamily);
        return "add-payment";
    }

    @PostMapping("/add-payment")
    public String addPayment(Model model, @AuthenticationPrincipal User user, @RequestParam("selectDirection") String paymentDirection,
                             @RequestParam("suma") String sumaTemp,
                             @RequestParam("typePayment") String typePayment,
                             @RequestParam("comment") String comment) {
        user = userRepo.findByLogin(user.getLogin());

        double suma = 0;
        TypePayment type = TypePayment.CASH;
        Payment payment;
        if (sumaTemp.equals("")) {
            model.addAttribute("paymentError", "You must enter sum of  your payment!");
            model.addAttribute("comment", comment);
            model.addAttribute("user", user);
            model.addAttribute("directionPayments", DirectionPayment.values());
            return "add-payment";
        } else if (comment.length() > 250) {
            model.addAttribute("paymentError", "Too longer comment!");
            model.addAttribute("comment", comment);
            model.addAttribute("user", user);
            model.addAttribute("directionPayments", DirectionPayment.values());
            return "add-payment";
        } else {
            suma = Double.parseDouble(sumaTemp);
        }
        if (typePayment.equals("1")) {
            type = TypePayment.TERMINAL;
        }
        DirectionPayment direction = DirectionPayment.values()[Integer.parseInt(paymentDirection)];
        payment = new Payment(suma, type, direction, comment, user);
        user.addPayment(payment);
        userRepo.save(user);
        return "redirect:/add-payment";
    }

    public List<Payment> getPayments() {
        Iterable<Payment> iterable = paymentRepo.findAll();
        List<Payment> payments = new ArrayList<Payment>();
        iterable.forEach(payments::add);
        return payments;
    }


    @PostMapping("/my-payments/sort")
    public String sortBy(Model model, @RequestParam("sortBy") String sortBy, @AuthenticationPrincipal User user) {
        user = userRepo.findByLogin(user.getLogin());
        List<Payment> userPayments = paymentRepo.findAllByUserId(user.getId());


        List<Payment> totalPayments = new ArrayList<>();
        for (int i = 0; i < DirectionPayment.values().length; i++) {
            Payment tempPayment = new Payment();
            tempPayment.setDirection(DirectionPayment.values()[i]);
            for (int j = 0; j < userPayments.size(); j++) {
                if (userPayments.get(j).getDirection().equals(tempPayment.getDirection())) {
                    tempPayment.setSuma(tempPayment.getSuma() + userPayments.get(j).getSuma());
                }
            }
            totalPayments.add(tempPayment);
        }

        List<Double> values = new ArrayList<>();
        for (int i = 0; i < totalPayments.size(); i++) {
            values.add(totalPayments.get(i).getSuma());
        }
        List<String> directions = new ArrayList<>();
        for (int i = 0; i < totalPayments.size(); i++) {
            directions.add(totalPayments.get(i).getDirection().name());
        }
        model.addAttribute("values", values);
        model.addAttribute("labels", directions);
        model.addAttribute("periods", PeriodPayments.values());
        model.addAttribute("user", user);
        List<Payment> temp = new ArrayList<>();
        switch (sortBy) {
            case "sum":
                temp = userPayments.stream().sorted(Comparator.comparing(Payment::getSuma)).collect(Collectors.toList());
                break;
            case "time":
                temp = userPayments.stream().sorted(Comparator.comparing(Payment::getTime)).collect(Collectors.toList());
                break;
            case "direction":
                temp = userPayments.stream().sorted(Comparator.comparing(Payment::getDirection)).collect(Collectors.toList());
                break;
        }
        model.addAttribute("payments", temp);
        model.addAttribute("currencyPeriod", "allTime");


        int countDays = 7;
        List<Double> sumPerDays = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        for (int i = 0; i < countDays; i++) {
            sumPerDays.add(0d);
            dates.add(LocalDate.now().minusDays(i).toString());
            for (int j = 0; j < userPayments.size(); j++) {
                if (Period.between(userPayments.get(j).getTime().toLocalDate(), LocalDate.now()).getDays() == i) {
                    sumPerDays.set(i, sumPerDays.get(i) + userPayments.get(j).getSuma());
                }
            }
        }

        Collections.reverse(sumPerDays);
        Collections.reverse(dates);
        model.addAttribute("valuesWeek", sumPerDays);
        model.addAttribute("labelsWeek", dates);

        return "my-payments";
    }

    @GetMapping("/my-payments")
    public String viewMyPayments(Model model, @AuthenticationPrincipal User user) {
        user = userRepo.findByLogin(user.getLogin());
        List<Payment> userPayments = paymentRepo.findAllByUserId(user.getId());


        List<Payment> totalPayments = new ArrayList<>();
        for (int i = 0; i < DirectionPayment.values().length; i++) {
            Payment tempPayment = new Payment();
            tempPayment.setDirection(DirectionPayment.values()[i]);
            for (int j = 0; j < userPayments.size(); j++) {
                if (userPayments.get(j).getDirection().equals(tempPayment.getDirection())) {
                    tempPayment.setSuma(tempPayment.getSuma() + userPayments.get(j).getSuma());
                }
            }
            totalPayments.add(tempPayment);
        }

        List<Double> values = new ArrayList<>();
        for (int i = 0; i < totalPayments.size(); i++) {
            values.add(totalPayments.get(i).getSuma());
        }
        List<String> directions = new ArrayList<>();
        for (int i = 0; i < totalPayments.size(); i++) {
            directions.add(totalPayments.get(i).getDirection().name());
        }
        model.addAttribute("values", values);
        model.addAttribute("labels", directions);
        model.addAttribute("periods", PeriodPayments.values());
        model.addAttribute("user", user);
        List<Payment> temp = userPayments.stream().sorted(Comparator.comparing(Payment::getTime)).collect(Collectors.toList());
        Collections.reverse(temp);
        model.addAttribute("payments", temp);
        model.addAttribute("currencyPeriod", "allTime");


        int countDays = 7;
        List<Double> sumPerDays = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        for (int i = 0; i < countDays; i++) {
            sumPerDays.add(0d);
            dates.add(LocalDate.now().minusDays(i).toString());
            for (int j = 0; j < userPayments.size(); j++) {
                if (Period.between(userPayments.get(j).getTime().toLocalDate(), LocalDate.now()).getDays() == i) {
                    sumPerDays.set(i, sumPerDays.get(i) + userPayments.get(j).getSuma());
                }
            }
        }

        Collections.reverse(sumPerDays);
        Collections.reverse(dates);
        model.addAttribute("valuesWeek", sumPerDays);
        model.addAttribute("labelsWeek", dates);


        return "my-payments";

    }

    @PostMapping("/change-period")
    public String changePeriod(Model model, @RequestParam("period") String period, @AuthenticationPrincipal User user) {
        user = userRepo.findById(user.getId()).get();
        List<Payment> userPayments = paymentRepo.findAllByUserId(user.getId());

        List<Payment> tempPayments = new ArrayList<>();

        switch (period) {
            case "allTime":
                tempPayments = userPayments;
                break;
            case "day":
                for (int i = 0; i < userPayments.size(); i++) {
                    if (userPayments.get(i).getTime().getDayOfMonth() == LocalDate.now().getDayOfMonth()) {
                        tempPayments.add(userPayments.get(i));
                    }
                }
                break;
            case "week":
                for (int i = 0; i < userPayments.size(); i++) {
                    if (userPayments.get(i).getPaymentAgeInDays() <= 7) {
                        tempPayments.add(userPayments.get(i));
                    }
                }
                break;
            case "month":
                for (int i = 0; i < userPayments.size(); i++) {
                    if (userPayments.get(i).getPaymentAgeInMonth() <= 1) {
                        tempPayments.add(userPayments.get(i));
                    }
                }
                break;
            case "year":
                for (int i = 0; i < userPayments.size(); i++) {
                    if (userPayments.get(i).getPaymentAgeInYears() <= 1) {
                        tempPayments.add(userPayments.get(i));
                    }
                }
                break;
        }
        List<Payment> totalPayments = new ArrayList<>();
        for (int i = 0; i < DirectionPayment.values().length; i++) {
            Payment tempPayment = new Payment();
            tempPayment.setDirection(DirectionPayment.values()[i]);
            for (int j = 0; j < tempPayments.size(); j++) {
                if (tempPayments.get(j).getDirection().equals(tempPayment.getDirection())) {
                    tempPayment.setSuma(tempPayment.getSuma() + tempPayments.get(j).getSuma());
                }
            }
            totalPayments.add(tempPayment);
        }

        List<Double> values = new ArrayList<>();
        for (int i = 0; i < totalPayments.size(); i++) {
            values.add(totalPayments.get(i).getSuma());
        }
        List<String> directions = new ArrayList<>();
        for (int i = 0; i < totalPayments.size(); i++) {
            directions.add(totalPayments.get(i).getDirection().name());
        }
        model.addAttribute("values", values);
        model.addAttribute("labels", directions);
        model.addAttribute("payments", tempPayments);
        model.addAttribute("periods", PeriodPayments.values());
        model.addAttribute("currencyPeriod", period);
        model.addAttribute("user", user);

        int countDays = 7;
        List<Double> sumPerDays = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        for (int i = 0; i < countDays; i++) {
            sumPerDays.add(0d);
            dates.add(LocalDate.now().minusDays(i).toString());
            for (int j = 0; j < userPayments.size(); j++) {
                if (Period.between(userPayments.get(j).getTime().toLocalDate(), LocalDate.now()).getDays() == i) {
                    sumPerDays.set(i, sumPerDays.get(i) + userPayments.get(j).getSuma());
                }
            }
        }
        Collections.reverse(sumPerDays);
        Collections.reverse(dates);
        model.addAttribute("valuesWeek", sumPerDays);
        model.addAttribute("labelsWeek", dates);
        return "my-payments";
    }

    @PostMapping("/payment/{id}/remove")
    public String removePayment(Model model, @PathVariable(value = "id") long id, @AuthenticationPrincipal User user) {
        user = userRepo.findById(user.getId()).get();
        user.getPayments().remove(paymentRepo.findById(id).get());
        paymentRepo.deleteById(id);
        userRepo.save(user);
        return "redirect:/my-payments";
    }


}
