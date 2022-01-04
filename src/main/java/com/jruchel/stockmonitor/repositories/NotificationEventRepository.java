package com.jruchel.stockmonitor.repositories;

import com.jruchel.stockmonitor.models.entities.NotificationEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NotificationEventRepository extends JpaRepository<NotificationEvent, String> {

    NotificationEvent findByTicker(String ticker);

    @Transactional
    int deleteByTicker(String ticker);
}
