package com.jruchel.stockmonitor.services.stocks;

import com.jruchel.stockmonitor.controllers.mappers.StockMonitoringRequestToStockMapper;
import com.jruchel.stockmonitor.controllers.models.StockMonitoringUpdateRequest;
import com.jruchel.stockmonitor.models.entities.MonitoredStock;
import com.jruchel.stockmonitor.models.entities.User;
import com.jruchel.stockmonitor.repositories.MonitoredStockRepository;
import com.jruchel.stockmonitor.services.notifications.NotificationService;
import com.jruchel.stockmonitor.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonitoredStockService {

    private final MonitoredStockRepository repository;
    private final StockMonitoringRequestToStockMapper mapper;
    private final UserService userService;

    public List<User> getUsersMonitoringStock(String ticker) {
        List<MonitoredStock> stocks = repository.getMonitoredStockByTicker(ticker);
        return stocks.stream().map(MonitoredStock::getUser).collect(Collectors.toSet()).stream().toList();
    }

    public MonitoredStock getStockByTicker(String ticker, User user) {
        return repository.getMonitoredStockByTickerAndUser(ticker, user);
    }

    public MonitoredStock save(MonitoredStock stock) {
        return repository.save(stock);
    }

    public MonitoredStock saveStockForUser(MonitoredStock stock, User user) {
        User[] finalUser = new User[1];
        finalUser[0] = user;
        List<MonitoredStock> existingStocks = repository
                .getMonitoredStockByTickerAndNotifyBelowAndNotifyAboveAndNotifyEveryPercent(
                        stock.getTicker(),
                        stock.getNotifyBelow(),
                        stock.getNotifyAbove(),
                        stock.getNotifyEveryPercent()
                );
        if (existingStocks.stream().anyMatch(s -> s.getUser().getId().equals(finalUser[0].getId())))
            throw new ValidationException("Identical stock monitoring already exists");
        finalUser[0].getMonitoredStocks().add(stock);
        stock.setUser(finalUser[0]);
        finalUser[0] = userService.save(finalUser[0]);

        return user.getMonitoredStocks().stream().filter(s -> sameAs(stock, s)).findFirst().orElse(null);
    }

    public List<MonitoredStock> getAll(User user) {
        return repository.getAllByUser(user);
    }

    public MonitoredStock update(StockMonitoringUpdateRequest stock, User user) {
        MonitoredStock existingStock = repository.getMonitoredStockById(stock.getId());
        if (existingStock == null)
            throw new NullPointerException("Cannot update stock with id: '%s' because it doesn't exist".formatted(stock.getId()));
        verifyUserHasMonitoring(existingStock, user);
        existingStock.setNotifyAbove(stock.getNotifyAbove());
        existingStock.setNotifyBelow(stock.getNotifyBelow());
        existingStock.setNotifyEveryPercent(stock.getNotifyEveryPercent());

        return repository.save(existingStock);
    }

    public MonitoredStock delete(String id, User user) {
        MonitoredStock stock = repository.findById(id).orElse(null);
        verifyUserHasMonitoring(stock, user);
        repository.deleteById(id);
        return stock;
    }

    public List<MonitoredStock> delete(List<String> ids, User user) {
        List<MonitoredStock> stocks = getMonitoredStocksByIds(ids);
        stocks.forEach(stock -> delete(stock.getId(), user));
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

    private boolean sameAs(MonitoredStock stock, MonitoredStock stock2) {
        return stock.getNotifyBelow() == stock2.getNotifyBelow() && stock.getTicker().equals(stock2.getTicker()) && stock.getNotifyAbove() == stock2.getNotifyAbove() && stock.getNotifyEveryPercent() == stock2.getNotifyEveryPercent();
    }

    private void verifyUserHasMonitoring(MonitoredStock stock, User user) {
        if (!stock.getUser().getId().equals(user.getId()))
            throw new ValidationException("This stock does not belong to the given user");
    }


}
