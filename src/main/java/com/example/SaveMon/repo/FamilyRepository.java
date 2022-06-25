package com.example.SaveMon.repo;

import com.example.SaveMon.domain.Family;
import com.example.SaveMon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.print.attribute.SetOfIntegerSyntax;
import javax.transaction.Transactional;

@Transactional
@Repository
public interface FamilyRepository extends JpaRepository<Family, Long> {
    @Query("select f from Family f where f.creator.id = :id")
    Family findByIdCreator(Long id);
    boolean existsByCreator(User creator);
}
