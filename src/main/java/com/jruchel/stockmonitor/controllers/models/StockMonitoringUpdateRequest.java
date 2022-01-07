package com.jruchel.stockmonitor.controllers.models;

import com.jruchel.stockmonitor.validation.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockMonitoringUpdateRequest {

    @UUID
    protected String id;
    @Min(0)
    protected double notifyAbove;
    @Min(0)
    protected double notifyBelow;
    @Min(0)
    protected double notifyEveryPercent;

}
