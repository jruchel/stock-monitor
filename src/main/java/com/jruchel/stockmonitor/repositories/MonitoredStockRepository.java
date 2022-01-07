package com.jruchel.stockmonitor.repositories;

import com.jruchel.stockmonitor.models.entities.MonitoredStock;
import com.jruchel.stockmonitor.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonitoredStockRepository extends JpaRepository<MonitoredStock, String> {

    MonitoredStock getMonitoredStockByTickerAndUser(String ticker, User user);

    List<MonitoredStock> getMonitoredStockByTicker(String ticker);

    MonitoredStock getMonitoredStockById(String id);

    List<MonitoredStock> getAllByUser(User user);

    List<MonitoredStock> getMonitoredStockByTickerAndNotifyBelowAndNotifyAboveAndNotifyEveryPercent(String ticker, double notifyBelow, double notifyAbove, double notifyEveryPercent);

}
