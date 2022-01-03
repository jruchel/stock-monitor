package com.jruchel.stockmonitor.repositories;

import com.jruchel.stockmonitor.models.entities.NotificationEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationEventRepository extends JpaRepository<NotificationEvent, String> {

    NotificationEvent findByTicker(String ticker);

    NotificationEvent deleteByTicker(String ticker);
}
