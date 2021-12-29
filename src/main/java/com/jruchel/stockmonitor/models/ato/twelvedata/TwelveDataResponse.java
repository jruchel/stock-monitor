package com.jruchel.stockmonitor.models.ato.twelvedata;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwelveDataResponse {

    private Map<String, TwelveDataStockDataATO> data = new HashMap<>();

}
