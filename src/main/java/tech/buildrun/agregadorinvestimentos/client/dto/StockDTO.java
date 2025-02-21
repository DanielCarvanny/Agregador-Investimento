package tech.buildrun.agregadorinvestimentos.client.dto;

public record StockDTO(double regularMarketPrice, double regularMarketChange, String currency,
                       String longName, String shortName) {

}
