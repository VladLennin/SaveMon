package com.example.SaveMon.controllers;

import com.example.SaveMon.domain.Payment;
import com.example.SaveMon.domain.User;
import com.example.SaveMon.domain.UserStatistic;
import com.example.SaveMon.enums.AgeZones;
import com.example.SaveMon.enums.DirectionPayment;
import com.example.SaveMon.enums.TypePayment;
import com.example.SaveMon.repo.PaymentRepository;
import com.example.SaveMon.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Transactional
@Controller
public class StatisticController {


    @Autowired
    PaymentRepository paymentRepo;

    @Autowired
    UserRepository userRepo;

    private static String currencyAge;
    private static String currencyDirection;

    public List<Payment> getPayments() {
        Iterable<Payment> iterable = paymentRepo.findAll();
        List<Payment> payments = new ArrayList<Payment>();
        iterable.forEach(payments::add);
        return payments;
    }

    @GetMapping("/statistic")
    public String statisticPage(Model model, @AuthenticationPrincipal User user) {
        Map<DirectionPayment, Long> paymentGroupedByDirections = getPayments().stream().collect(Collectors.groupingBy(Payment::getDirection, Collectors.counting()));

        model.addAttribute("user", user);
        model.addAttribute("ageOption", AgeZones.TEENAGERS.name());
        model.addAttribute("countAllPayments", getPayments().size());
        model.addAttribute("valuesByDirection", paymentGroupedByDirections.values());
        model.addAttribute("paymentDirections", paymentGroupedByDirections.keySet());
        model.addAttribute("ageZones", AgeZones.values());
        List<String> typesPayment = new ArrayList<>();
        for (int i = 0; i < TypePayment.values().length; i++) {
            typesPayment.add(TypePayment.values()[i].name());
        }
        model.addAttribute("paymentTypes", typesPayment);

        currencyAge = AgeZones.TEENAGERS.toString();
        currencyDirection = DirectionPayment.values()[0].toString();
        List<Payment> filteredByAge = getPayments().stream().filter(s -> s.getAgeZoneUser() == AgeZones.TEENAGERS).collect(Collectors.toList());
        Map<DirectionPayment, Long> groupedByAge = filteredByAge.stream().collect(Collectors.groupingBy(Payment::getDirection, Collectors.counting()));

        Map<TypePayment, Long> groupedByType = getPayments().stream().collect(Collectors.groupingBy(Payment::getType, Collectors.counting()));

        model.addAttribute("valuesByType", groupedByType.values());
        model.addAttribute("countByAge", filteredByAge.size());
        model.addAttribute("valuesByAge", groupedByAge.values());
        model.addAttribute("keysByAge", groupedByAge.keySet());

        List<Payment> paymentsByDirection = getPayments().stream().filter(s -> s.getDirection().equals(DirectionPayment.values()[0])).collect(Collectors.toList());
        Map<String, Long> groupedByCountry = paymentsByDirection.stream().collect(Collectors.groupingBy(Payment::getUserCountry, Collectors.counting()));
        model.addAttribute("valuesByCountry", groupedByCountry.values());
        model.addAttribute("countries", groupedByCountry.keySet());
        model.addAttribute("countPaymentsByDirection", paymentsByDirection.size());
        return "all-statistic-page";
    }

    @PostMapping("/change-age")
    public String changeAge(Model model, @RequestParam("age") String age, @AuthenticationPrincipal User user) {
        currencyAge = age;
        Map<DirectionPayment, Long> paymentGroupedByDirections = getPayments().stream().collect(Collectors.groupingBy(Payment::getDirection, Collectors.counting()));

        model.addAttribute("user", user);
        model.addAttribute("ageOption", AgeZones.valueOf(age));
        model.addAttribute("countAllPayments", getPayments().size());
        model.addAttribute("valuesByDirection", paymentGroupedByDirections.values());
        model.addAttribute("paymentDirections", paymentGroupedByDirections.keySet());
        model.addAttribute("ageZones", AgeZones.values());

        List<Payment> filteredByAge = getPayments().stream().filter(s -> s.getAgeZoneUser() == AgeZones.valueOf(age)).collect(Collectors.toList());
        Map<DirectionPayment, Long> groupedByAge = filteredByAge.stream().collect(Collectors.groupingBy(Payment::getDirection, Collectors.counting()));

        model.addAttribute("countByAge", filteredByAge.size());
        model.addAttribute("valuesByAge", groupedByAge.values());
        model.addAttribute("keysByAge", groupedByAge.keySet());

        List<Payment> paymentsByDirection = getPayments().stream().filter(s -> s.getDirection().equals(DirectionPayment.valueOf(currencyDirection))).collect(Collectors.toList());
        Map<String, Long> groupedByCountry = paymentsByDirection.stream().collect(Collectors.groupingBy(Payment::getUserCountry, Collectors.counting()));
        model.addAttribute("valuesByCountry", groupedByCountry.values());
        model.addAttribute("countries", groupedByCountry.keySet());
        model.addAttribute("countPaymentsByDirection", paymentsByDirection.size());
        model.addAttribute("directionT", DirectionPayment.valueOf(currencyDirection));

        List<String> typesPayment = new ArrayList<>();
        for (int i = 0; i < TypePayment.values().length; i++) {
            typesPayment.add(TypePayment.values()[i].name());
        }
        Map<TypePayment, Long> groupedByType = getPayments().stream().collect(Collectors.groupingBy(Payment::getType, Collectors.counting()));
        model.addAttribute("paymentTypes", typesPayment);
        model.addAttribute("valuesByType", groupedByType.values());
        return "all-statistic-page";
    }

    @PostMapping("/change-direction")
    public String changeDirection(Model model, @AuthenticationPrincipal User user, @RequestParam("direction") String direction) {
        currencyDirection = direction;
        Map<DirectionPayment, Long> paymentGroupedByDirections = getPayments().stream().collect(Collectors.groupingBy(Payment::getDirection, Collectors.counting()));

        model.addAttribute("user", user);
        model.addAttribute("ageOption", AgeZones.valueOf(currencyAge));
        model.addAttribute("countAllPayments", getPayments().size());
        model.addAttribute("valuesByDirection", paymentGroupedByDirections.values());
        model.addAttribute("paymentDirections", paymentGroupedByDirections.keySet());
        model.addAttribute("ageZones", AgeZones.values());

        List<Payment> filteredByAge = getPayments().stream().filter(s -> s.getAgeZoneUser() == AgeZones.valueOf(currencyAge)).collect(Collectors.toList());
        Map<DirectionPayment, Long> groupedByAge = filteredByAge.stream().collect(Collectors.groupingBy(Payment::getDirection, Collectors.counting()));

        model.addAttribute("countByAge", filteredByAge.size());
        model.addAttribute("valuesByAge", groupedByAge.values());
        model.addAttribute("keysByAge", groupedByAge.keySet());

        List<Payment> paymentsByDirection = getPayments().stream().filter(s -> s.getDirection().equals(DirectionPayment.valueOf(direction))).collect(Collectors.toList());
        Map<String, Long> groupedByCountry = paymentsByDirection.stream().collect(Collectors.groupingBy(Payment::getUserCountry, Collectors.counting()));
        model.addAttribute("valuesByCountry", groupedByCountry.values());
        model.addAttribute("countries", groupedByCountry.keySet());
        model.addAttribute("countPaymentsByDirection", paymentsByDirection.size());
        model.addAttribute("directionT", DirectionPayment.valueOf(direction));

        List<String> typesPayment = new ArrayList<>();
        for (int i = 0; i < TypePayment.values().length; i++) {
            typesPayment.add(TypePayment.values()[i].name());
        }
        Map<TypePayment, Long> groupedByType = getPayments().stream().collect(Collectors.groupingBy(Payment::getType, Collectors.counting()));
        model.addAttribute("paymentTypes", typesPayment);
        model.addAttribute("valuesByType", groupedByType.values());
        return "all-statistic-page";
    }

    @GetMapping("/my-statistic")
    public String myStat(Model model, @AuthenticationPrincipal User user) {
        user = userRepo.findById(user.getId()).get();
        List<Payment> payments = paymentRepo.findAllByUserId(user.getId());

        Map<DirectionPayment, Double> directionsPayments = UserStatistic.getPercentageDirections(payments, "month");


        if (payments.stream().filter(s -> s.getPaymentAgeInMonth() <= 1).collect(Collectors.toList()).size() != 0) {
            DirectionPayment tempKey = null;
            double tempValue = 0;
            for (Map.Entry<DirectionPayment, Double> entry : UserStatistic.mostSpentDirection(payments).entrySet()) {
                tempKey = entry.getKey();
                tempValue = entry.getValue();
            }
            model.addAttribute("mostSpentDirectionValue", tempValue);
            model.addAttribute("mostSpentDirectionKey", tempKey);
        }

        model.addAttribute("periods", Payment.periods);
        model.addAttribute("user", user);

        model.addAttribute("keysDirectionPercentage", directionsPayments.keySet());
        model.addAttribute("valuesDirectionPercentage", directionsPayments.values());
        model.addAttribute("month", Month.values());
        model.addAttribute("spentByMonth", UserStatistic.spentByCurrentMonth(payments));
        if (user.getCurrentFamily() != null) {

            List<Payment> paymentsByFamily = new ArrayList<>();
            for (int i = 0; i < user.getCurrentFamily().getMembers().size(); i++) {
                List<Payment> tempList = paymentRepo.findAllByUserId(user.getCurrentFamily().getMembers().get(i).getId());
                for (int j = 0; j < tempList.size(); j++) {
                    if (tempList.get(j).getPaymentAgeInMonth() <= 1) {
                        paymentsByFamily.add(tempList.get(j));
                    }
                }
            }
            model.addAttribute("spentByMonthByFamily", UserStatistic.spentByCurrentMonthByFamily(paymentsByFamily) + " uah");

            double planOfFamily = 0;
            for (int i = 0; i < user.getCurrentFamily().getMembers().size(); i++) {
                planOfFamily += user.getCurrentFamily().getMembers().get(i).getPurse();
            }
            model.addAttribute("planOfFamily", planOfFamily);

            double doPlanedOfFamily = UserStatistic.spentByCurrentMonthByFamily(paymentsByFamily) / planOfFamily;
            model.addAttribute("doPlanedOfFamily", doPlanedOfFamily);

        } else {
            model.addAttribute("spentByMonthByFamily", "You don`t have family");
        }
        model.addAttribute("by", "month");
        model.addAttribute("planedSpent", user.getPurse());


        double doPlaned = UserStatistic.spentByCurrentMonth(user.getPayments()) / user.getPurse();
        model.addAttribute("doPlaned", doPlaned);


        return "my-statistic-page";
    }

    @PostMapping("/my-statistic/change-plan")
    public String changePlan(Model model, @AuthenticationPrincipal User user, @RequestParam("newPlan") double newPlan) {
        user = userRepo.findById(user.getId()).get();
        user.setPurse(newPlan);
        userRepo.save(user);
        return "redirect:/my-statistic";
    }

    @PostMapping("/my-statistic/change-period")
    public String changePeriod(Model model,@AuthenticationPrincipal User user,@RequestParam String age){

        user = userRepo.findById(user.getId()).get();
        List<Payment> payments = paymentRepo.findAllByUserId(user.getId());

        Map<DirectionPayment, Double> directionsPayments = UserStatistic.getPercentageDirections(payments, age);


        if (payments.stream().filter(s -> s.getPaymentAgeInMonth() <= 1).collect(Collectors.toList()).size() != 0) {
            DirectionPayment tempKey = null;
            double tempValue = 0;
            for (Map.Entry<DirectionPayment, Double> entry : UserStatistic.mostSpentDirection(payments).entrySet()) {
                tempKey = entry.getKey();
                tempValue = entry.getValue();
            }
            model.addAttribute("mostSpentDirectionValue", tempValue);
            model.addAttribute("mostSpentDirectionKey", tempKey);
        }

        model.addAttribute("periods", Payment.periods);
        model.addAttribute("user", user);

        model.addAttribute("keysDirectionPercentage", directionsPayments.keySet());
        model.addAttribute("valuesDirectionPercentage", directionsPayments.values());
        model.addAttribute("month", Month.values());
        model.addAttribute("spentByMonth", UserStatistic.spentByCurrentMonth(payments));
        if (user.getCurrentFamily() != null) {

            List<Payment> paymentsByFamily = new ArrayList<>();
            for (int i = 0; i < user.getCurrentFamily().getMembers().size(); i++) {
                List<Payment> tempList = paymentRepo.findAllByUserId(user.getCurrentFamily().getMembers().get(i).getId());
                for (int j = 0; j < tempList.size(); j++) {
                    if (tempList.get(j).getPaymentAgeInMonth() <= 1) {
                        paymentsByFamily.add(tempList.get(j));
                    }
                }
            }
            model.addAttribute("spentByMonthByFamily", UserStatistic.spentByCurrentMonthByFamily(paymentsByFamily) + " uah");

            double planOfFamily = 0;
            for (int i = 0; i < user.getCurrentFamily().getMembers().size(); i++) {
                planOfFamily += user.getCurrentFamily().getMembers().get(i).getPurse();
            }
            model.addAttribute("planOfFamily", planOfFamily);

            double doPlanedOfFamily = UserStatistic.spentByCurrentMonthByFamily(paymentsByFamily) / planOfFamily;
            model.addAttribute("doPlanedOfFamily", doPlanedOfFamily);

        } else {
            model.addAttribute("spentByMonthByFamily", "You don`t have family");
        }
        model.addAttribute("by", age);
        model.addAttribute("planedSpent", user.getPurse());


        double doPlaned = UserStatistic.spentByCurrentMonth(user.getPayments()) / user.getPurse();
        model.addAttribute("doPlaned", doPlaned);


        return "my-statistic-page";
    }

}
