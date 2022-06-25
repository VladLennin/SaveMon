package com.example.SaveMon.domain;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Currency {
    private String name;
    private double costInUah;

    public void getCourseOfCurrencys() throws IOException {
       String json = new Scanner(new URL( "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5")
               .openStream(), "UTF-8").useDelimiter("\\A").next();
       String pattern = "\"USD\",\"base_ccy\":\"UAH\",\"buy\":\"(.*?)\",\"sale\":\"(.*?)\"";
       Pattern rx = Pattern.compile(pattern);
       Matcher matcher = rx.matcher(json);



    }


}
