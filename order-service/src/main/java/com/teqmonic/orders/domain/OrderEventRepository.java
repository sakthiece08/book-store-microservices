package com.teqmonic.orders.domain;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderEventRepository extends JpaRepository<OrderEventEntity, Long> {

    List<OrderEventEntity> findAllByIsEventPublished(Sort sort, boolean isEventPublished);

    List<OrderEventEntity> findALlByOrderNumber(String no);
}
