package com.jruchel.stockmonitor.controllers.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonitoredStockResponse {

    private String id;
    private String ticker;
    private double notifyBelow;
    private double notifyAbove;
    private double notifyEveryPercent;

}
