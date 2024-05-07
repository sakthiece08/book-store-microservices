package com.teqmonic.orders.domain;

import com.teqmonic.orders.domain.models.OrderStatusEnum;
import com.teqmonic.orders.domain.models.OrderSummary;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByStatusOrderByCreatedAtAsc(OrderStatusEnum status);

    @Query(
            """
              SELECT new com.teqmonic.orders.domain.models.OrderSummary(o.orderNumber, o.status)
              FROM OrderEntity o
              WHERE o.userName = :userName
            """)
    List<OrderSummary> findByUserName(String userName);

    @Query(
            """
                     SELECT o
                     FROM OrderEntity o left join fetch o.orderItem left join fetch o.customer
                     WHERE o.orderNumber = :orderNumber and o.userName = :userName
            """)
    Optional<OrderEntity> findByOrderNumberAndUserName(String orderNumber, String userName);
}
