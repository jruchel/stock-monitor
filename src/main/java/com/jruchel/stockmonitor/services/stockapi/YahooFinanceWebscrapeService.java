package com.jruchel.stockmonitor.services.stockapi;

import com.jruchel.stockmonitor.config.stocksapi.StockApiConfiguration;
import com.jruchel.stockmonitor.models.StockData;
import com.jruchel.stockmonitor.services.stocks.StockDataRepository;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class YahooFinanceWebscrapeService extends StockDataRepository {

    private final RestTemplate restTemplate;
    private final String yahooFinanceUrl = "https://finance.yahoo.com/quote/%s";
    private final String priceRegex = "[ ]+<fin-streamer.*data-field=\"regularMarketPrice\".*value=\"([0-9.]+)\".*>";

    public YahooFinanceWebscrapeService(StockApiConfiguration configuration, RestTemplate restTemplate) {
        super(configuration);
        this.restTemplate = restTemplate;
    }

    public double getStockPriceForTicker(String ticker) {
        String url = yahooFinanceUrl.formatted(ticker);
        String source = Jsoup.parse(restTemplate.getForObject(URI.create(url), String.class)).toString();
        if (source == null)
            throw new NullPointerException("Data for request %s was empty".formatted(yahooFinanceUrl.formatted(ticker.toUpperCase())));
        Pattern pattern = Pattern.compile(priceRegex);
        Matcher matcher;
        for (String line : source.split("\n")) {
            if (line.contains("regularMarketPrice") && line.contains("data-symbol=\"%s\"".formatted(ticker.toUpperCase()))) {
                matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    return Double.parseDouble(matcher.group(1));
                }
            }
        }
        throw new NullPointerException("Data for request %s was empty".formatted(yahooFinanceUrl.formatted(ticker)));
    }

    @Override
    public StockData fetchStockData(String ticker) {
        return new StockData(ticker, getStockPriceForTicker(ticker), new Date().toString());
    }
}
