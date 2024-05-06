package com.teqmonic.orders.domain;

import com.teqmonic.orders.domain.models.OrderStatusEnum;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByStatus(OrderStatusEnum status);
}
