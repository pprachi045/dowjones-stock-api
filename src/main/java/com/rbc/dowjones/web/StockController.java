package com.rbc.dowjones.web;

import com.rbc.dowjones.model.StockData;
import com.rbc.dowjones.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StockController {

    private static final Logger logger = LoggerFactory.getLogger(StockController.class);
    @Autowired
    private StockService stockService;

    @GetMapping(path = "/stock-data/{ticker}", produces = "application/json")
    public ResponseEntity<?> retrieveStockData(@PathVariable String ticker) {

        List<StockData> stockList = stockService.retrieveStockData(ticker);
        return ResponseEntity.status(HttpStatus.OK).body(stockList);
    }

    @PostMapping(path = "/stock-data/bulk-insert", produces = "application/json")
    public ResponseEntity<?> insertStockData(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No Input file");
        } else {
            try {
                stockService.addStockData(file);
            } catch (IOException e) {
                logger.error("Error reading file", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing input file");
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("File processed successfully");
    }

    @PostMapping(path = "/stock-data", produces = "application/json")
    public ResponseEntity<?> updateStockData(@RequestBody StockData stockData) {
        stockService.addMissingStock(stockData);
        return ResponseEntity.status(HttpStatus.OK).body("Stock updated");
    }

}
