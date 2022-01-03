package com.jruchel.stockmonitor.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Stock {

    private String ticker;
    private double notifyBelow;
    private double notifyAbove;
    private double notifyEveryPercent;
}
