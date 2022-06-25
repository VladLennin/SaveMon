package com.example.SaveMon.domain;

import com.example.SaveMon.enums.AgeZones;
import com.example.SaveMon.enums.DirectionPayment;
import com.example.SaveMon.enums.TypePayment;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.File;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

@Getter
@Setter
@Entity
public class Payment {
    private LocalDateTime time;
    private double suma;
    private TypePayment type;
    private DirectionPayment direction;
    private String comment;
    public static final ArrayList<String> periods = new ArrayList<>();


    @Lob
    private String voiceComment;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Override
    public String toString() {
        return "Payment{" +
                "time=" + time +
                ", suma=" + suma +
                ", type=" + type +
                ", direction=" + direction +
                ", comment='" + comment + '\'' +
                ", id=" + id +
                '}';
    }


    public Payment() {
    }


    public Payment(double suma, TypePayment type, DirectionPayment direction, String comment, User user) {

        this.suma = suma;
        this.type = type;
        this.direction = direction;
        if (!comment.equals("")) {
            this.comment = comment;
        } else {
            this.comment = "ABSENT";
        }
        this.user = user;
        this.time = LocalDateTime.now();


    }

    public int getUserAge() {
        return user.getAge();
    }

    public AgeZones getAgeZoneUser() {
        return user.getAgeZone();
    }

    public String getImage() {
        return direction.getImage();
    }

    public Month getMonth() {
        return time.getMonth();
    }

    public int getPaymentAgeInDays() {
        return Period.between(time.toLocalDate(), LocalDate.now()).getDays();
    }

    public int getPaymentAgeInMonth() {
        return Period.between(time.toLocalDate(), LocalDate.now()).getMonths();
    }

    public int getPaymentAgeInYears() {
        return Period.between(time.toLocalDate(), LocalDate.now()).getYears();
    }

    public String getUserCountry() {
        return user.getCountry();
    }

    public String getTimeFormated() {
        return this.time.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT));
    }

    public boolean isCommentEmpty() {
        return comment.equals("");
    }


}
