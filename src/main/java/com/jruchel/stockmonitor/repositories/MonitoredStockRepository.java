package com.jruchel.stockmonitor.repositories;

import com.jruchel.stockmonitor.models.entities.MonitoredStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoredStockRepository extends JpaRepository<MonitoredStock, String> {

    MonitoredStock getMonitoredStockByTicker(String ticker);

    MonitoredStock getMonitoredStockById(String id);

    MonitoredStock getMonitoredStockByTickerAndNotifyBelowAndNotifyAboveAndNotifyEveryPercent(String ticker, double notifyBelow, double notifyAbove, double notifyEveryPercent);

}
