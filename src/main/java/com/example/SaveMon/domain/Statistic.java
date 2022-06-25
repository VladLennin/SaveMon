package com.example.SaveMon.domain;

import com.example.SaveMon.enums.AgeZones;
import com.example.SaveMon.enums.DirectionPayment;
import com.example.SaveMon.enums.TypePayment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Statistic {

    public static String getStat(List<Payment> paymentsRaw, String by) {
        List<Payment> payments = new ArrayList<>();
        switch (by) {
            case "day":
                payments = paymentsRaw.stream().filter(e -> e.getPaymentAgeInDays() < 2).collect(Collectors.toList());
                break;
            case "week":
                payments = paymentsRaw.stream().filter(e -> e.getPaymentAgeInDays() < 8).collect(Collectors.toList());
                break;
            case "month":
                payments = paymentsRaw.stream().filter(e -> e.getPaymentAgeInMonth() < 2).collect(Collectors.toList());
                break;
            case "year":
                payments = paymentsRaw.stream().filter(e -> e.getPaymentAgeInYears() < 2).collect(Collectors.toList());
                break;
            default:
                payments = paymentsRaw;
                break;
        }
        Map<DirectionPayment, Long> groupByDirection = payments.stream().collect(Collectors.groupingBy(Payment::getDirection, Collectors.counting()));
        Map<DirectionPayment, Double> groupByDirectionAndSum = payments.stream()
                .collect(Collectors.toMap(e -> e.getDirection(), e -> e.getSuma(), Double::sum, LinkedHashMap::new));

        String json = "\"Statistic\":\n" + new Gson().toJson(groupByDirectionAndSum) + ",";
        json += "\"Grouped by age\":";
        for (int i = 0; i < AgeZones.values().length; i++) {
            int finalI = i;
            List<Payment> filteredByAge =
                    payments.stream().filter(s -> s.getAgeZoneUser() == AgeZones.values()[finalI]).collect(Collectors.toList());
            Map<DirectionPayment, Double> groupedByAge = filteredByAge.stream()
                    .collect(Collectors.toMap(e -> e.getDirection(), e -> e.getSuma(), Double::sum, LinkedHashMap::new));
            json += "\"" + AgeZones.values()[i].getName() + "\":" + "" + new Gson().toJson(groupedByAge) + ",";
        }

        List<String> countries = new ArrayList<>();
        for (int i = 0; i < payments.size(); i++) {
            if (!countries.contains(payments.get(i).getUserCountry())) {
                countries.add(payments.get(i).getUserCountry());
            }
        }
        json += "\"Grouped by countries\":";
        for (int i = 0; i < countries.size(); i++) {
            int finalI = i;
            json += "\"" + countries.get(i) + "\":";
            List<Payment> filteredByCountries = payments.stream().filter(s -> s.getUserCountry().equals(countries.get(finalI))).collect(Collectors.toList());
            Map<DirectionPayment, Double> groupedByCountry = filteredByCountries.stream()
                    .collect(Collectors.toMap(e -> e.getDirection(), e -> e.getSuma(), Double::sum, LinkedHashMap::new));
            json += new Gson().toJson(groupedByCountry);
        }


        json += "\n\"Grouped by Direction\":" + new Gson().toJson(groupByDirection);

        Map<TypePayment, Long> groupedByType = payments.stream().collect(Collectors.groupingBy(Payment::getType, Collectors.counting()));
        double costByOne = 100 / (double) payments.size();
        double cash = 0;
        double terminal = 0;
        for (Map.Entry<TypePayment, Long> entry : groupedByType.entrySet()) {
            if (entry.getKey().name().equals(TypePayment.CASH.name())) {
                cash = costByOne * entry.getValue();
            } else {
                terminal = costByOne * entry.getValue();
            }
        }
        json += "\"Percents by type\":{";
        json += "\"Cash\":" + cash + "%,";
        json += "\"Terminal\":" + terminal + "%},";


        for (int i = 0; i < TypePayment.values().length; i++) {
            List<Payment> paymentsByType = new ArrayList<>();
            for (int j = 0; j < payments.size(); j++) {
                if (payments.get(j).getType().equals(TypePayment.values()[i])) {
                    paymentsByType.add(payments.get(j));
                }
            }
            Map<DirectionPayment, Double> sumaByTypeAndDirection = paymentsByType.stream().collect(Collectors.toMap(e -> e.getDirection(), e -> e.getSuma(), Double::sum, LinkedHashMap::new));
            json += "\"" + TypePayment.values()[i] + "\":" + new Gson().toJson(sumaByTypeAndDirection) + ",";
        }
        return json;
    }

    public static String getSmallStat(List<Payment> paymentsRaw, String by) {
        List<Payment> payments = new ArrayList<>();
        switch (by) {
            case "day":
                payments = paymentsRaw.stream().filter(e -> e.getPaymentAgeInDays() < 2).collect(Collectors.toList());
                break;
            case "week":
                payments = paymentsRaw.stream().filter(e -> e.getPaymentAgeInDays() < 8).collect(Collectors.toList());
                break;
            case "month":
                payments = paymentsRaw.stream().filter(e -> e.getPaymentAgeInMonth() < 2).collect(Collectors.toList());
                break;
            case "year":
                payments = paymentsRaw.stream().filter(e -> e.getPaymentAgeInYears() < 2).collect(Collectors.toList());
                break;
            default:
                payments = paymentsRaw;
                break;
        }
        Map<DirectionPayment, Long> groupByDirection = payments.stream().collect(Collectors.groupingBy(Payment::getDirection, Collectors.counting()));
        Map<DirectionPayment, Double> groupByDirectionAndSum = payments.stream()
                .collect(Collectors.toMap(e -> e.getDirection(), e -> e.getSuma(), Double::sum, LinkedHashMap::new));
        return new Gson().toJson(groupByDirectionAndSum);
    }

    public static String getStatByTelegramId(User user) {
        Map<DirectionPayment, Long> groupByDirection = user.getPayments().stream().collect(Collectors.groupingBy(Payment::getDirection, Collectors.counting()));
        Map<DirectionPayment, Double> groupByDirectionAndSum = user.getPayments().stream()
                .collect(Collectors.toMap(e -> e.getDirection(), e -> e.getSuma(), Double::sum, LinkedHashMap::new));
        return (new Gson().toJson(groupByDirection)+" "+new Gson().toJson(groupByDirectionAndSum)).toString();
    }





}
