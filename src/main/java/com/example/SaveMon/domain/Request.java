package com.example.SaveMon.domain;

import com.example.SaveMon.repo.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Request {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private User userFrom;



    private long userToId;
    public boolean isActive;
    public LocalDateTime time;

    private String status;

    public Request() {
    }

    public String getNameUserFrom() {
        return userFrom.getLogin();
    }


    public Request(User userFrom,User userTo) {
        this.userFrom = userFrom;
        this.isActive = true;
        this.status = "Waiting";
        this.time = LocalDateTime.now();
        this.userToId = userTo.getId();
    }

}
