package com.jruchel.stockmonitor.controllers;

import com.jruchel.stockmonitor.controllers.mappers.StockMonitoringRequestToStockMapper;
import com.jruchel.stockmonitor.controllers.models.MonitoredStockResponse;
import com.jruchel.stockmonitor.controllers.models.StockMonitoringRequest;
import com.jruchel.stockmonitor.controllers.models.StockMonitoringUpdateRequest;
import com.jruchel.stockmonitor.models.entities.MonitoredStock;
import com.jruchel.stockmonitor.security.SecurityService;
import com.jruchel.stockmonitor.security.autoconfig.Controller;
import com.jruchel.stockmonitor.security.autoconfig.SecuredMapping;
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
public class MonitoredStocksController extends Controller {

    private final MonitoredStockService monitoredStockService;
    private final StockMonitoringRequestToStockMapper mapper;
    private final SecurityService securityService;

    @SecuredMapping(method = RequestMethod.POST, role = "user")
    public ResponseEntity<Void> post(@RequestBody @Valid StockMonitoringRequest request) {
        monitoredStockService.saveStockForUser(mapper.monitoredStock(request), securityService.getCurrentUser());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @SecuredMapping(method = RequestMethod.GET, role = "user")
    public ResponseEntity<List<MonitoredStockResponse>> getAll() {
        return ResponseEntity.ok(mapper.monitoredStockResponseList(monitoredStockService.getAll(securityService.getCurrentUser())));
    }

    @SecuredMapping(method = RequestMethod.DELETE, role = "user")
    public ResponseEntity<List<MonitoredStockResponse>> delete(@RequestBody List<String> ids) {
        return ResponseEntity.ok(mapper.monitoredStockResponseList(monitoredStockService.delete(ids, securityService.getCurrentUser())));
    }

    @SecuredMapping(method = RequestMethod.PUT, role = "user")
    public ResponseEntity<MonitoredStockResponse> update(@RequestBody @Valid StockMonitoringUpdateRequest request) {
        return ResponseEntity.ok(mapper.monitoredStockResponse(monitoredStockService.update(request, securityService.getCurrentUser())));
    }

}
