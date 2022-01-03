package com.jruchel.stockmonitor.models.ato.twelvedata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Value {

    @JsonProperty("open")
    private double openingPrice;
    @JsonProperty("high")
    private double highPrice;
    @JsonProperty("low")
    private double lowPrice;
    @JsonProperty("close")
    private double closingPrice;
    @JsonProperty("datetime")
    private String timestamp;

}
