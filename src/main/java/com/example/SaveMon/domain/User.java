package com.example.SaveMon.domain;

import com.example.SaveMon.enums.AgeZones;
import com.example.SaveMon.enums.DirectionPayment;
import com.example.SaveMon.enums.Roles;
import com.example.SaveMon.enums.TypePayment;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "usr")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String login;
    private String email;
    private String password;
    private String name;
    private String surname;
    private LocalDate birthday;
    private double purse;
    private String sex;
    private String number;
    private String country;
    private long telegramId;
    @Enumerated(EnumType.STRING)
    private Roles role;
    @Lob
    private String image;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Family currentFamily;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Payment> payments;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Request> requests;


    public User(String login, String email, String password, Roles role) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.role = role;
        this.payments = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.name = "none";
        this.surname = "none";
        this.number = "none";


    }

    public User() {
        this.payments = new ArrayList<>();
        this.requests = new ArrayList<>();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(getRole());
    }

    @Override
    public String getUsername() {
        return login;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isAdmin() {
        return role == Roles.ADMIN;
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
    }

    public void addPayment(String typePayment, String directionPayment, Long userID, double sum, String comment) {
        payments.add(new Payment(sum, TypePayment.valueOf(typePayment), DirectionPayment.valueOf(directionPayment), comment, this));
    }


    public boolean hasPayments() {
        return payments.stream().count() != 0;
    }

    @Autowired
    public boolean isImageReal() {
        if (image.equals("Default")) {
            return false;
        } else {
            return true;
        }
    }

    public static int getAge(@NotNull LocalDate birthday) {
        return Period.between(birthday, LocalDate.now()).getYears();
    }

    public int getAge() {
        return Period.between(birthday, LocalDate.now()).getYears();
    }

    public int getCountPayments() {
        if (payments != null) {
            return payments.size();
        } else {
            return 0;
        }
    }

    public int getCountPayments(String period) {
        switch (period) {

        }
        return payments.size();
    }

    public AgeZones getAgeZone() {
        int age = getAge();
        if (age >= 12 && age < 20) {
            return AgeZones.TEENAGERS;
        } else if (age >= 20 && age < 36) {
            return AgeZones.ADULT_1;
        } else if (age >= 36 && age <= 60) {
            return AgeZones.ADULT_2;
        } else {
            return AgeZones.OLD;
        }
    }

    public long getFamilyId() {
        if (currentFamily != null) {
            return currentFamily.getId();
        } else return 0;
    }

    public List<Payment> getPayments(String by) {

        List<Payment> sortedPayments = new ArrayList<>();
        switch (by) {
            case "day":
                sortedPayments = payments.stream().filter(s -> s.getPaymentAgeInDays() <= 1).collect(Collectors.toList());
                break;
            case "week":
                sortedPayments = payments.stream().filter(s -> s.getPaymentAgeInDays() <= 7).collect(Collectors.toList());
                break;
            case "month":
                sortedPayments = payments.stream().filter(s -> s.getPaymentAgeInMonth() <= 1).collect(Collectors.toList());
                break;
        }
        return sortedPayments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Double.compare(user.purse, purse) == 0 && Objects.equals(id, user.id) && Objects.equals(login, user.login) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(name, user.name) && Objects.equals(surname, user.surname) && Objects.equals(birthday, user.birthday) && sex == user.sex && Objects.equals(number, user.number) && Objects.equals(country, user.country) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, email, password, name, surname, birthday, purse, sex, number, country, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthday=" + birthday +
                ", planedSpent=" + purse +
                ", sex='" + sex + '\'' +
                ", number='" + number + '\'' +
                ", country='" + country + '\'' +
                ", role=" + role +
                '}';
    }


}

