package com.jruchel.stockmonitor.services.stockapi;

import com.jruchel.stockmonitor.config.stocksapi.StockApiConfiguration;
import com.jruchel.stockmonitor.models.StockData;
import com.jruchel.stockmonitor.models.ato.twelvedata.TwelveDataResponse;
import com.jruchel.stockmonitor.models.ato.twelvedata.TwelveDataStockDataATO;
import com.jruchel.stockmonitor.models.mappers.TwelveDataToStockDataMapper;
import com.jruchel.stockmonitor.services.stocks.StockDataRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class TwelveDataClient extends StockDataRepository {

    private final RestTemplate restTemplate;
    private final TwelveDataToStockDataMapper mapper;

    public TwelveDataClient(StockApiConfiguration configuration, RestTemplate restTemplate) {
        super(configuration);
        this.mapper = new TwelveDataToStockDataMapper();
        this.restTemplate = restTemplate;
    }

    @Override
    public StockData fetchStockData(String ticker) {
        String address = configuration.getAddress();
        String endpoint = configuration.getMainEndpoint();
        String parameters = String.format("symbol=%s&interval=1min&%s=%s", ticker, configuration.getKey().getName(), configuration.getKey().getValue());

        String url = String.format("%s/%s?%s", address, endpoint, parameters);

        RequestEntity<Void> requestEntity = RequestEntity.get(url).build();

        try {
            ResponseEntity<TwelveDataStockDataATO> response = restTemplate.exchange(requestEntity, TwelveDataStockDataATO.class);
            validateResponse(response);
            return mapper.map(response.getBody());

        } catch (HttpClientErrorException exception) {
            throw new NullPointerException(exception.getMessage());
        }
    }

    @Override
    public List<StockData> fetchStockData(List<String> tickers) {
        String address = configuration.getAddress();
        String endpoint = configuration.getMainEndpoint();
        StringBuilder symbolsBuilder = new StringBuilder();

        tickers.forEach(ticker -> symbolsBuilder.append(ticker).append(","));
        symbolsBuilder.setLength(symbolsBuilder.length() - 1);

        String parameters = String.format("symbol=%s&interval=1min&%s=%s", symbolsBuilder, configuration.getKey().getName(), configuration.getKey().getValue());

        String url = String.format("%s/%s?%s", address, endpoint, parameters);

        RequestEntity<Void> requestEntity = RequestEntity.get(url).build();

        try {
            ParameterizedTypeReference<Map<String, TwelveDataStockDataATO>> typeReference = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<Map<String, TwelveDataStockDataATO>> response = restTemplate.exchange(requestEntity, typeReference);
            if (response.getBody() == null || response.getBody() == null || response.getBody().isEmpty())
                throw new NullPointerException("TwelveData response was empty");
            return mapper.map(new TwelveDataResponse(response.getBody()));

        } catch (HttpClientErrorException exception) {
            throw new NullPointerException(exception.getMessage());
        }
    }

    private void validateResponse(ResponseEntity<TwelveDataStockDataATO> response) {
        if (Objects.isNull(response) || Objects.isNull(response.getBody()))
            throw new NullPointerException("TwelveData response was empty");
        if (response.getBody().getValues().size() == 0)
            throw new NullPointerException("Api calls exceeded for this minute");
    }
}
