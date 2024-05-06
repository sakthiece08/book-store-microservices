package com.teqmonic.orders.domain;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderEventRepository extends JpaRepository<OrderEventEntity, Long> {

    List<OrderEventEntity> findAllByIsEventPublished(Sort sort, boolean isEventPublished);

    List<OrderEventEntity> findALlByOrderNumber(String no);
}
