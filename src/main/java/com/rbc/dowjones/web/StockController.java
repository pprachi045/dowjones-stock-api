package com.rbc.dowjones.web;

import com.rbc.dowjones.data.Stock;
import com.rbc.dowjones.service.StockService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StockController {

    private static final Logger logger = LoggerFactory.getLogger(StockController.class);
    @Autowired
    private StockService stockService;

    @GetMapping(path = "/stock-data/{ticker}", produces = "application/json")
    public ResponseEntity<?> retrieveStockData(@PathVariable String ticker) {

        List<Stock> stockList = stockService.retrieveStockData(ticker);
        return ResponseEntity.status(HttpStatus.OK).body(stockList);
    }

    @PostMapping(path = "/stock-data/bulk-insert", produces = "application/json")
    public ResponseEntity<?> insertStockData(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No Input file");
        } else {
            try {
                stockService.addStockData(readFile(file));;
            } catch (IOException e) {
                logger.error("Error reading file", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing input file");
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("File processed successfully");
    }

    @PostMapping(path = "/stock-data", produces = "application/json")
    public ResponseEntity<?> updateStockData(@RequestBody Stock stock) {
        stockService.updateStockData(stock);
        return ResponseEntity.status(HttpStatus.OK).body("Stock updated");
    }

    private List<Stock> readFile(MultipartFile file) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
        CSVParser csvParser = CSVParser.parse(bufferedReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
        Iterable<CSVRecord> csvRecords = csvParser.getRecords();

        List<Stock> stockList = new ArrayList<>();
        for (CSVRecord csvRecord : csvRecords) {
            Stock stock = Stock.builder()
                    .low(csvRecord.get("low"))
                    .high(csvRecord.get("high"))
                    .open(csvRecord.get("open"))
                    .close(csvRecord.get("close"))
                    .stock(csvRecord.get("stock"))
                    .quarter(csvRecord.get("quarter"))
                    .date(csvRecord.get("date"))
                    .volume(csvRecord.get("volume"))
                    .next_weeks_close("next_weeks_close")
                    .next_weeks_open(csvRecord.get("next_weeks_open"))
                    .days_to_next_dividend(csvRecord.get("days_to_next_dividend"))
                    .percent_change_price(csvRecord.get("percent_change_price"))
                    .percent_change_next_weeks_price(csvRecord.get("percent_change_next_weeks_price"))
                    .percent_return_next_dividend(csvRecord.get("percent_return_next_dividend"))
                    .percent_change_volume_over_last_wk(csvRecord.get("percent_change_volume_over_last_wk"))
                    .previous_weeks_volume(csvRecord.get("previous_weeks_volume"))
                    .build();
            stockList.add(stock);
        }
        return stockList;
    }

}
