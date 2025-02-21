package tech.buildrun.agregadorinvestimentos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.buildrun.agregadorinvestimentos.controller.dto.CreateStockDTO;
import tech.buildrun.agregadorinvestimentos.service.StockService;

@RestController
@RequestMapping("/v1/stocks")
public class StockController {
    private StockService stockService;

    public StockController(StockService stockServie) {
        this.stockService = stockServie;
    }

    @PostMapping
    public ResponseEntity<Void> createStock(@RequestBody CreateStockDTO createStockDTO){
        stockService.createStock(createStockDTO);
        return ResponseEntity.ok().build();
    }
}
