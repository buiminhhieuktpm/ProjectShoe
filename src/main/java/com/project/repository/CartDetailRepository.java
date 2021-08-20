package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entity.CartDetail;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartDetailRepository extends JpaRepository<CartDetail, Integer>{
    List<CartDetail> findAllByCartId(Integer id);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartDetail c where c.id = :id")
    void deleteCart(@Param("id") Integer id);
}
