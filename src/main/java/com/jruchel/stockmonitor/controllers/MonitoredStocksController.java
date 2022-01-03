package com.jruchel.stockmonitor.controllers;

import com.jruchel.stockmonitor.controllers.mappers.StockMonitoringRequestToStockMapper;
import com.jruchel.stockmonitor.controllers.models.StockMonitoringRequest;
import com.jruchel.stockmonitor.controllers.models.StockMonitoringUpdateRequest;
import com.jruchel.stockmonitor.models.entities.MonitoredStock;
import com.jruchel.stockmonitor.services.stocks.MonitoredStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stocks/monitoring")
public class MonitoredStocksController {

    private final MonitoredStockService monitoredStockService;
    private final StockMonitoringRequestToStockMapper mapper;

    @PostMapping
    public ResponseEntity<Void> post(@RequestBody @Valid StockMonitoringRequest request) {
        monitoredStockService.save(mapper.monitoredStock(request));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<MonitoredStock>> getAll() {
        return ResponseEntity.ok(monitoredStockService.getAll());
    }

    @DeleteMapping
    public ResponseEntity<List<MonitoredStock>> delete(@RequestBody List<String> ids) {
        return ResponseEntity.ok(monitoredStockService.delete(ids));
    }

    @PutMapping
    public ResponseEntity<MonitoredStock> update(@RequestBody @Valid StockMonitoringUpdateRequest request) {
        return ResponseEntity.ok(monitoredStockService.update(mapper.monitoredStock(request)));
    }

}
