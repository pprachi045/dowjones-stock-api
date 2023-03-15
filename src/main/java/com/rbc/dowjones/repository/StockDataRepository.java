package com.rbc.dowjones.repository;

import com.rbc.dowjones.model.StockData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockDataRepository extends CrudRepository<StockData,Long> {
    public List<StockData> findByStock(String stock);
}
