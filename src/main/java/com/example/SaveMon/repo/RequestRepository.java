package com.example.SaveMon.repo;

import com.example.SaveMon.domain.Request;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface RequestRepository extends CrudRepository<Request,Long> {
    List<Request> findByUserFromId(long userFromId);
    List<Request> findByUserFromIdAndIsActive(long userFromId,boolean isActive);
}
