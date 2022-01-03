package com.jruchel.stockmonitor.controllers.models;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class StockMonitoringRequest {

    @Pattern(regexp = "[A-Z]{3,4}")
    private String ticker;
    @Min(0)
    private double notifyAbove;
    @Min(0)
    private double notifyBelow;
    @Min(0)
    private double notifyEveryPercent;

}
