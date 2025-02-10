package com.example.foscore.repository;

import com.example.foscore.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.createdBy = :createdBy " +
           "AND o.createdAt BETWEEN :startDate AND :endDate"
    )
    List<Order> findAllByCreatedByAndDateRange(
        @Param("createdBy") Long createdBy,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

}
