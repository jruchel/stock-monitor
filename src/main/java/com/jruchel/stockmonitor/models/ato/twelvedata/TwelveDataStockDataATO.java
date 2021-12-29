package com.jruchel.stockmonitor.models.ato.twelvedata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TwelveDataStockDataATO {

    @JsonProperty("meta")
    private Meta meta;
    @JsonProperty("values")
    private List<Value> values = new ArrayList<>();
}
