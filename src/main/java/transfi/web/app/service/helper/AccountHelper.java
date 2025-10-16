package transfi.web.app.service.helper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import transfi.web.app.dto.AccountDto;
import transfi.web.app.entity.*;
import transfi.web.app.repository.AccountRepository;
import transfi.web.app.repository.TransactionRepository;
import transfi.web.app.util.RandomUtil;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Getter
public class AccountHelper {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    private final Map<String, String> CURRENCIES = Map.of(
            "USD", "United States Dollar",
            "EUR", "Euro",
            "JPY", "Japanese Yen",
            "GBP", "British Pound Sterling",
            "NGN", "Nigerian Naira",
            "INR", "Indian Rupee"
    );

    public Account createAccount(AccountDto accountDto, User user) throws Exception {

        long accountNumber;
        validateAccountNonExistsForUser(accountDto.getCode(), user.getUid());
        do{
            accountNumber= new RandomUtil().generateRandom(10);
        }
        while (accountRepository.existsByAccountNumber(accountNumber));

        var account = Account.builder()
                .accountNumber(accountNumber)
                .accountName(user.getFirstname() + " " + user.getLastname())
                .balance(500.0)
                .owner(user)
                .code(accountDto.getCode())
                .symbol(accountDto.getSymbol())
                .label(CURRENCIES.get(accountDto.getCode()))
                .build();
        return accountRepository.save(account);
    }
    public Transaction performTransfer(Account sendersAccount, Account receiversAccount,
                                       double amount, String description, User user) throws Exception {
        validateUserAccountOwnership(sendersAccount, sendersAccount.getOwner());
        validateSufficientFunds(sendersAccount, amount*1.01);
        sendersAccount.setBalance(sendersAccount.getBalance()-amount*1.01);
        receiversAccount.setBalance(receiversAccount.getBalance()+amount);
        accountRepository.saveAll(List.of(sendersAccount, receiversAccount));
        var senderTransaction = Transaction.builder()
                .account(sendersAccount)
                .txFee(amount*0.01)
                .amount(amount)
                .sender(sendersAccount.getAccountName())
                .status(Status.COMPLETED)
                .description(description)
                .type(Type.WITHDRAWAL)
                .owner(sendersAccount.getOwner())
                .build();
        var receiverTransaction = Transaction.builder()
                .account(receiversAccount)
                .txFee(0.0)
                .amount(amount)
                .receiver(receiversAccount.getAccountName())
                .status(Status.COMPLETED)
                .type(Type.DEPOSIT)
                .description(description)
                .owner(sendersAccount.getOwner())
                .build();
        return transactionRepository.saveAll(List.of(senderTransaction, receiverTransaction)).getFirst();
    }
    public void validateAccountNonExistsForUser(String code, String uid ) throws Exception {
        if(accountRepository.existsByCodeAndOwner_Uid(code, uid)){
            throw new Exception("Account with code "+code+" already exists for user "+uid);
        }
    }
    public void validateUserAccountOwnership(Account account, User  user) throws OperationNotSupportedException {
        if(!account.getOwner().getUid().equals(user.getUid())){
            throw new OperationNotSupportedException("Account does not belong to user");
        }
    }
    public void validateSufficientFunds(Account account, double amount) throws Exception {
        if(account.getBalance()<amount){
            throw new OperationNotSupportedException("Insufficient funds in account ");
        }
    }


}
