package transfi.web.app.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import transfi.web.app.dto.AccountDto;
import transfi.web.app.dto.ConvertDto;
import transfi.web.app.dto.TransferDto;
import transfi.web.app.entity.Account;
import transfi.web.app.entity.Transaction;
import transfi.web.app.entity.User;
import transfi.web.app.service.AccountService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody AccountDto accountDto, Authentication authentication) throws Exception {
  var user = (User) authentication.getPrincipal();
          return ResponseEntity.ok(accountService.createAccount(accountDto, user));
    }

    @GetMapping
    public ResponseEntity<List<Account>> getUserAccounts(Authentication authentication)  {
        var user= (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.getUserAccounts(user.getUid()));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transferFunds(Authentication authentication, @RequestBody TransferDto transferDto) throws Exception {
        var user= (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.transferFunds(user, transferDto));
    }
    @GetMapping("/rates")
    public ResponseEntity<Map<String, Double>> getExchangeRate(){
        return ResponseEntity.ok(accountService.getExchangeRate());
    }

    @PostMapping("/convert")
    public ResponseEntity<Transaction> convertCurrency(@RequestBody ConvertDto convertDto, Authentication authentication) throws Exception {
        var user= (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.convertCurrency(convertDto, user));
    }
}
