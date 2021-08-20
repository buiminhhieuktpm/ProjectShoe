package com.project.repository;

import com.project.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon ,Integer> {
    @Query(value = "SELECT * FROM Coupon c where c.code = ?1", nativeQuery = true)
    Optional<Coupon> findByCode(String code);

    @Query("select c from Coupon c where c.code = :code and c.amount > 0 and c.endDate >= current_timestamp")
    Optional<Coupon> findCode(String code);

    @Transactional
    @Modifying
    @Query("update Coupon c set c.amount = :amount where c.id = :id")
    void updateAmount(@Param("amount") Integer amount, @Param("id") Integer id);
}
