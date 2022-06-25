package com.example.SaveMon.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.engine.internal.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Family {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User creator;
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<User> members = new ArrayList<>();

    public Family() {

    }

    public List<Payment> getAllPayments(User user) {
        List<Payment> payments = new ArrayList<>();
        for (int i = 0; i < members.size(); i++) {
            for (int j = 0; j < members.get(i).getPayments().size(); j++) {
                if (!user.getPayments().contains(members.get(i).getPayments().get(j))) {
                    payments.add(members.get(i).getPayments().get(j));
                }
            }
        }
        return payments;
    }

    public Family(User userCreator, String name) {
        this.name = name;
        this.creator = userCreator;
        this.members.add(userCreator);
    }

    public void addMember(User user) {
        if (getCount() < 6) {
            members.add(user);
        }
    }

    public int getCount() {
        return members.size();
    }

}

