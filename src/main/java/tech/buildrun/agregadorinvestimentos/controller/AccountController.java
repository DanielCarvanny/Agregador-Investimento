package tech.buildrun.agregadorinvestimentos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.buildrun.agregadorinvestimentos.controller.dto.AccountStockResponseDTO;
import tech.buildrun.agregadorinvestimentos.controller.dto.AssociateAccountStockDTO;
import tech.buildrun.agregadorinvestimentos.service.AccountService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/accounts")
public class AccountController {
    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{accountId}/stocks")
    public ResponseEntity<Void> associateStock(@PathVariable("accountId") UUID accountId,
                                              @RequestBody AssociateAccountStockDTO dto){
        accountService.associateStock(accountId, dto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountId}/stocks")
    public ResponseEntity<List<AccountStockResponseDTO>> listStock(@PathVariable("accountId") UUID accountId){

        var stocks = accountService.listStock(accountId);

        if (stocks.isEmpty()){
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.ok().body(stocks);
        }
    }
}
