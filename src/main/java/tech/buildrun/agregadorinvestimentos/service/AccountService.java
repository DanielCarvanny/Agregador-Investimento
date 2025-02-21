package tech.buildrun.agregadorinvestimentos.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tech.buildrun.agregadorinvestimentos.client.BrapiClient;
import tech.buildrun.agregadorinvestimentos.controller.dto.AccountStockResponseDTO;
import tech.buildrun.agregadorinvestimentos.controller.dto.AssociateAccountStockDTO;
import tech.buildrun.agregadorinvestimentos.entity.AccountStock;
import tech.buildrun.agregadorinvestimentos.entity.AccountStockId;
import tech.buildrun.agregadorinvestimentos.repository.AccountRepository;
import tech.buildrun.agregadorinvestimentos.repository.AccountStockRepository;
import tech.buildrun.agregadorinvestimentos.repository.StockRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Value("#{environment.TOKEN}")
    private String TOKEN;

    private AccountRepository accountRepository;

    private StockRepository stockRepository;

    private AccountStockRepository accountStockRepository;

    private BrapiClient brapiClient;

    public AccountService(AccountRepository accountRepository,
                          StockRepository stockRepository,
                          AccountStockRepository accountStockRepository, BrapiClient brapiClient) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
        this.brapiClient = brapiClient;
    }

    public void associateStock(UUID accountId, AssociateAccountStockDTO dto) {

        var account = accountRepository.findById(accountId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var stock = stockRepository.findById(dto.stockId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        // DTO -> ENTITY
        var id = new AccountStockId(
            account.getAccountId(),
            stock.getStockId()
        );

        var entity = new AccountStock(
                id,
                account,
                stock,
                dto.quantity()
        );

        accountStockRepository.save(entity);

    }

    public List<AccountStockResponseDTO> listStock(UUID accountId) {
        var account = accountRepository.findById(accountId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

       return account.getAccountStocks().stream()
                .map(asr -> {
                    var response = brapiClient.getQuote(TOKEN, asr.getStock().getStockId());
                    var stockInfo = response.results().getFirst();

                    return new AccountStockResponseDTO(
                            asr.getStock().getStockId(),       // ID da ação
                            asr.getQuantity(),                 // Quantidade de ações do usuário
                            asr.getQuantity() * stockInfo.regularMarketPrice(), // Cálculo do total (preço * quantidade)
                            stockInfo.regularMarketChange(),   // Variação do mercado
                            stockInfo.currency(),              // Moeda
                            stockInfo.longName(),              // Nome longo da ação
                            stockInfo.shortName()              // Nome curto da ação
                    );
                })
                .collect(Collectors.toList());
    }
}
