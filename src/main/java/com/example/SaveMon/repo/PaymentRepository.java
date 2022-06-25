package com.example.SaveMon.repo;

import com.example.SaveMon.domain.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Transactional
@Repository
public interface PaymentRepository extends CrudRepository<Payment, Long> {
    ArrayList<Payment> findAllByUserId(Long id);
    void deleteById(Long id);
}
