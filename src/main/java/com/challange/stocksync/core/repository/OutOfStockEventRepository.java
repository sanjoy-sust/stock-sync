package com.challange.stocksync.core.repository;

import com.challange.stocksync.core.entity.OutOfStockEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutOfStockEventRepository extends JpaRepository<OutOfStockEvent, Long> {
}