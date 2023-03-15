package com.rbc.dowjones.service;

import com.rbc.dowjones.data.Stock;
import com.rbc.dowjones.data.StockPK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class StockService {

    private Map<StockPK,Stock> stockRepo = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(StockService.class);
    public List<Stock> retrieveStockData(String ticker){
        logger.info("Stock data retrieved");
        return stockRepo.values().stream().filter(k -> k.getStock().equals(ticker)).collect(Collectors.toList());
    }

    public void addStockData(List<Stock> stockData){
        logger.info("Stock data received {}",stockData.size());
        stockData.stream().forEach(s -> {
            StockPK stockPK = new StockPK(s.getStock(),s.getDate());
            stockRepo.compute(stockPK,(k,v) -> {
                if(v == null){
                    v = s;
                }
                return v;
            });
        });
        logger.info("Stock data inserted {}",stockRepo.size());
    }

    public void updateStockData(Stock stock){
        StockPK stockPK = new StockPK(stock.getStock(),stock.getDate());
        stockRepo.compute(stockPK,(k,v) -> {
            if(v == null){
                v = stock;
            }
            return v;
        });
        logger.info("Stock data updated");
    }

}
