package com.rbc.dowjones.service;

import com.rbc.dowjones.model.StockData;
import com.rbc.dowjones.repository.StockDataRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@Service
public class StockService {

    private static final Logger logger = LoggerFactory.getLogger(StockService.class);
    @Autowired
    private StockDataRepository stockDataRepository;
    public List<StockData> retrieveStockData(String ticker){
        List<StockData> stockData = stockDataRepository.findByStock(ticker);
        return stockData;
    }

    public void addStockData(MultipartFile file) throws IOException {
        List<StockData> stockData = readFile(file);
        logger.info("Stock data size {}",stockData.size());
        stockDataRepository.saveAll(stockData);
    }

    public void addMissingStock(StockData stockData){
        stockDataRepository.save(stockData);
        logger.info("Stock data updated");
    }

    private List<StockData> readFile(MultipartFile file) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
        CSVParser csvParser = CSVParser.parse(bufferedReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
        Iterable<CSVRecord> csvRecords = csvParser.getRecords();

        List<StockData> stockList = new ArrayList<>();
        for (CSVRecord csvRecord : csvRecords) {
            StockData stockData = new StockData();
            stockData.setLow(csvRecord.get("low"));
            stockData.setHigh(csvRecord.get("high"));
            stockData.setOpen(csvRecord.get("open"));
            stockData.setClose(csvRecord.get("close"));
            stockData.setStock(csvRecord.get("stock"));
            stockData.setQuarter(csvRecord.get("quarter"));
            stockData.setDate(csvRecord.get("date"));
            stockData.setVolume(csvRecord.get("volume"));
            stockData.setNext_weeks_close("next_weeks_close");
            stockData.setNext_weeks_open(csvRecord.get("next_weeks_open"));
            stockData.setDays_to_next_dividend(csvRecord.get("days_to_next_dividend"));
            stockData.setPercent_change_price(csvRecord.get("percent_change_price"));
            stockData.setPercent_change_next_weeks_price(csvRecord.get("percent_change_next_weeks_price"));
            stockData.setPercent_return_next_dividend(csvRecord.get("percent_return_next_dividend"));
            stockData.setPercent_change_volume_over_last_wk(csvRecord.get("percent_change_volume_over_last_wk"));
            stockData.setPrevious_weeks_volume(csvRecord.get("previous_weeks_volume"));
            stockList.add(stockData);
        }
        return stockList;
    }

}
