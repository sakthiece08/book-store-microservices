package com.teqmonic.orders.jobs;

import com.teqmonic.orders.domain.OrderEventService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderEventsPublishingJob {

    private final OrderEventService orderEventService;

    @Scheduled(cron = "${orders.publish-order-events-job-cron}")
    public void publishOrders() {
        log.info("OrderEventsPublishingJob, publishOrders started at: {}", Instant.now());
        orderEventService.publishOrderEvents();
    }
}
