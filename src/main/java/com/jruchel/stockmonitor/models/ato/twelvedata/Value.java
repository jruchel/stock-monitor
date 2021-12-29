package com.jruchel.stockmonitor.models.ato.twelvedata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Value {

    @JsonProperty("open")
    private String openingPrice;
    @JsonProperty("hight")
    private String highPrice;
    @JsonProperty("low")
    private String lowPrice;
    @JsonProperty("close")
    private String closingPrice;
    @JsonProperty("datetime")
    private String timestamp;

}
