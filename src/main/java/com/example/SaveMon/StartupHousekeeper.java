package com.example.SaveMon;

import com.example.SaveMon.domain.Countries;
import com.example.SaveMon.domain.Payment;
import com.example.SaveMon.domain.User;
import com.example.SaveMon.enums.Roles;
import com.example.SaveMon.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class StartupHousekeeper {

    @Autowired
    UserRepository userRepo;

    @EventListener(ContextRefreshedEvent.class)
    void contextRefreshedEvent() throws IOException {
        try {
            String inputCountries = new Scanner(new URL("https://restcountries.com/v3.1/all").openStream(), "UTF-8").useDelimiter("\\A").next();

            PrintWriter writer = new PrintWriter("countries.txt", "UTF-8");
            writer.println(inputCountries);
            writer.close();


            Pattern pattern = Pattern.compile("\"eng\":.\"f\":\"([a-zA-Z ]*)\"");
            Matcher mather = pattern.matcher(inputCountries);
            while (mather.find()) {
                Countries.countries.add(mather.group(1));
            }
            Countries.countries = (ArrayList<String>) Countries.countries.stream().sorted((s1, s2) -> s1.compareTo(s2)).collect(Collectors.toList());
        } catch (Exception e) {
            String fileName = "countries.txt";
            String inputCountries = Files.lines(Paths.get(fileName)).reduce("", String::concat);
            Pattern pattern = Pattern.compile("\"eng\":.\"f\":\"([a-zA-Z ]*)\"");
            Matcher mather = pattern.matcher(inputCountries);
            while (mather.find()) {
                Countries.countries.add(mather.group(1));
            }
            Countries.countries = (ArrayList<String>) Countries.countries.stream().sorted((s1, s2) -> s1.compareTo(s2)).collect(Collectors.toList());

            System.out.println(e.getMessage());
        }
        if (userRepo.findByLogin("Vladlen") != null) {
            User user = userRepo.findByLogin("Vladlen");
            user.setRole(Roles.ADMIN);
            userRepo.save(user);
        }

        Payment.periods.add("day");
        Payment.periods.add("week");
        Payment.periods.add("month");
        Payment.periods.add("year");
        Payment.periods.add("all");
    }
}
