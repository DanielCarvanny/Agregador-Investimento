package tech.buildrun.agregadorinvestimentos.controller.dto;

public record AccountStockResponseDTO(
        String stockId,
        Integer quantity,
        double total,
        double marketChange,
        String currency,
        String longName,
        String shortName
) {}


