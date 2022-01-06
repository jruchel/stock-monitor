package com.jruchel.stockmonitor.controllers.models;

import com.jruchel.stockmonitor.models.entities.MonitoredStock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserATO {

    private String username;
    private List<MonitoredStock> monitoredStocks;

}
