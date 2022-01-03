package com.jruchel.stockmonitor.services.stocks;

import com.jruchel.stockmonitor.models.entities.MonitoredStock;
import com.jruchel.stockmonitor.repositories.MonitoredStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonitoredStockService {

    private final MonitoredStockRepository repository;

    public MonitoredStock getStockByTicker(String ticker) {
        return repository.getMonitoredStockByTicker(ticker);
    }

    public MonitoredStock addStockMonitoring(MonitoredStock stock) {
        return save(stock);
    }

    public List<MonitoredStock> addStockMonitoring(List<MonitoredStock> stocks) {
        return stocks.stream().peek(this::save).collect(Collectors.toList());
    }

    public MonitoredStock save(MonitoredStock stock) {
        return repository.save(stock);
    }

    public List<MonitoredStock> getAll() {
        return repository.findAll();
    }

    public MonitoredStock update(MonitoredStock stock) {
        MonitoredStock existingStock = repository.getMonitoredStockById(stock.getId());
        if (existingStock == null)
            throw new NullPointerException("Cannot update stock with id: '%s' because it doesn't exist".formatted(stock.getId()));
        return save(stock);
    }

    public MonitoredStock delete(String id) {
        MonitoredStock stock = repository.findById(id).orElse(null);
        repository.deleteById(id);
        return stock;
    }

    public List<MonitoredStock> delete(List<String> ids) {
        List<MonitoredStock> stocks = getMonitoredStocksByIds(ids);
        stocks.forEach(stock -> delete(stock.getId()));
        return stocks;
    }

    private List<MonitoredStock> getMonitoredStocksByIds(List<String> ids) {
        List<MonitoredStock> stocks = new ArrayList<>();
        for (String id : ids) {
            MonitoredStock stock = repository.getMonitoredStockById(id);
            if (stock != null) stocks.add(stock);
        }
        return stocks;
    }

}
