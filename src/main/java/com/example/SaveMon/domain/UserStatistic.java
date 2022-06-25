package com.example.SaveMon.domain;

import com.example.SaveMon.enums.DirectionPayment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Transactional
public class UserStatistic {

    private User user;
    private Map<DirectionPayment, Double> spentForALlTimeByDirection;
    private double spentByMonth;

    public UserStatistic(User user, List<Payment> payments) {
        spentByCurrentMonth(payments);
        spentForALlTimeByDirection = payments.stream()
                .collect(Collectors.toMap(e -> e.getDirection(), e -> e.getSuma(), Double::sum, LinkedHashMap::new));
    }

    public static Map<DirectionPayment, Double> getPercentageDirections(List<Payment> paymentsRaw, String by) {

        List<Payment> payments = new ArrayList<>();
        switch (by) {
            case "day":
                payments = paymentsRaw.stream().filter(s -> s.getPaymentAgeInDays() <= 1).collect(Collectors.toList());
                break;
            case "week":
                payments = paymentsRaw.stream().filter(s -> s.getPaymentAgeInDays() <= 7).collect(Collectors.toList());
                break;
            case "month":
                payments = paymentsRaw.stream().filter(s -> s.getPaymentAgeInMonth() <= 1).collect(Collectors.toList());
                break;
            case "year":
                payments = paymentsRaw.stream().filter(s -> s.getPaymentAgeInYears() <= 1).collect(Collectors.toList());
                break;
            case "all":
                payments = paymentsRaw;
                break;
        }
        if (payments.size() == 0) {
            Map<DirectionPayment, Double> map = new HashMap<>();
            for (int i = 0; i < DirectionPayment.values().length; i++) {
                map.put(DirectionPayment.values()[i], 0d);
            }
            return map;
        }
        spentByCurrentMonth(payments);
        Map<DirectionPayment, Double> resultSet = payments.stream()
                .collect(Collectors.groupingBy(t -> t.getDirection(),
                        Collectors.summingDouble(Payment::getSuma)));

        double costByOnePayment = 100d / spentByCurrentMonth(payments);
        Map<DirectionPayment, Double> result = new HashMap<>();
        for (Map.Entry<DirectionPayment, Double> entry : resultSet.entrySet()) {
            result.put(entry.getKey(), entry.getValue() * costByOnePayment);
        }
        return result;
    }

    public static double spentByCurrentMonth(List<Payment> payments) {

        List<Payment> paymentsByCurrentMonth = payments.stream().filter(s -> s.getPaymentAgeInMonth() <= 1).collect(Collectors.toList());
        double result = 0;
        for (int i = 0; i < paymentsByCurrentMonth.size(); i++) {
            result += paymentsByCurrentMonth.get(i).getSuma();
        }
        return result;
    }

    public static Map<DirectionPayment, Double> mostSpentDirection(List<Payment> paymentsRaw) {
        List<Payment> payments = paymentsRaw.stream().filter(s -> s.getPaymentAgeInMonth() <= 1).collect(Collectors.toList());
        Map<DirectionPayment, Double> resultSet = payments.stream()
                .collect(Collectors.groupingBy(t -> t.getDirection(),
                        Collectors.summingDouble(Payment::getSuma)));
        Map<DirectionPayment, Double> mostSpentDirection = new HashMap<>();
        mostSpentDirection.put(Collections.max(resultSet.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey(), Collections.max(resultSet.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getValue());

        return mostSpentDirection;
    }


    public static double spentByCurrentMonthByFamily(List<Payment> payments) {

        List<Payment> paymentsByCurrentMonth = payments.stream().filter(s -> s.getPaymentAgeInMonth() <= 1).collect(Collectors.toList());
        double result = 0;
        for (int i = 0; i < paymentsByCurrentMonth.size(); i++) {
            result += paymentsByCurrentMonth.get(i).getSuma();
        }
        return result;
    }
}
