package tech.buildrun.agregadorinvestimentos.service;

import org.springframework.stereotype.Service;
import tech.buildrun.agregadorinvestimentos.controller.dto.CreateStockDTO;
import tech.buildrun.agregadorinvestimentos.entity.Stock;
import tech.buildrun.agregadorinvestimentos.repository.StockRepository;

@Service
public class StockService {
    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void createStock(CreateStockDTO createStockDTO) {
        //DTO -> ENTITY
        var stock = new Stock(
                createStockDTO.stockId(),
                createStockDTO.description()
        );

        stockRepository.save(stock);
    }
}
