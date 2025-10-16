package transfi.web.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import transfi.web.app.dto.AccountDto;
import transfi.web.app.dto.TransferDto;
import transfi.web.app.entity.Account;
import transfi.web.app.entity.Transaction;
import transfi.web.app.entity.User;
import transfi.web.app.repository.AccountRepository;
import transfi.web.app.service.helper.AccountHelper;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountService {
    private  final AccountRepository accountRepository;
    private final AccountHelper accountHelper;
    private final ExchangeRateService exchangeRateService;

    public Account createAccount(AccountDto accountDto, User user) throws Exception {
        return accountHelper.createAccount(accountDto, user);
    }

    public List<Account> getUserAccounts(String uid) {
        return accountRepository.findAllByOwnerUid(uid);
    }

    public Transaction transferFunds(User user, TransferDto transferDto) throws Exception {
            var  sendersAccount = accountRepository.findByCodeAndOwner_Uid(transferDto.getCode(),user.getUid())
                    .orElseThrow(() -> new UnsupportedOperationException("Sender account not found"));
            var  receiversAccount = accountRepository.findByAccountNumber(transferDto.getReceiverAccount()).orElseThrow(() -> new UnsupportedOperationException("Receiver account not found"));
            return accountHelper.performTransfer(sendersAccount, receiversAccount, transferDto.getAmount(), transferDto.getDescription(), user);
    }

    public Map<String, Double> getExchangeRate(){
        return exchangeRateService.getRates();
    }
}
