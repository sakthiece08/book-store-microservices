package com.teqmonic.orders.jobs;

import com.teqmonic.orders.domain.OrderService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderProcessingJob {

    private final OrderService orderService;

    @Scheduled(initialDelayString = "20000", fixedDelayString = "20000")
    @SchedulerLock(name = "processNewOrders", lockAtLeastFor = "1m", lockAtMostFor = "5m")
    public void processNewOrders() {
        // To assert that the lock is held (prevents misconfiguration errors)
        LockAssert.assertLocked();
        log.info("Processing New Orders at {}", Instant.now());
        orderService.processNewOrders();
    }
}
