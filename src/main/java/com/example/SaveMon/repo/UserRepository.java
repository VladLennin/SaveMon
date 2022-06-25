package com.example.SaveMon.repo;

import com.example.SaveMon.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByLogin(String login);

    User findByEmail(String email);

    User findByTelegramId(long telegramId);
}
