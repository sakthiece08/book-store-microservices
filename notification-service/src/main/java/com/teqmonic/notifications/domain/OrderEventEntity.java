package com.teqmonic.notifications.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "order_events")
public class OrderEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_event_id_generator")
    @SequenceGenerator(name = "order_event_id_generator", sequenceName = "order_event_id_seq")
    private Long id;

    @Column(nullable = false, unique = true)
    private String eventId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
