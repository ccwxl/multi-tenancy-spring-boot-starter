package cc.sofast.example.rest;

import cc.sofast.example.bean.Account;
import cc.sofast.example.service.AccountService;
import org.springframework.web.bind.annotation.*;

/**
 * @author apple
 */
@RestController
@RequestMapping("account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public Account addAccount(@RequestBody Account account) {
        accountService.save(account);
        return account;
    }

    @GetMapping("{account}")
    public Account getAccount(@PathVariable Long account) {

        return accountService.getById(account);
    }

    @PutMapping("{accountId}")
    public Account updateAccount(@PathVariable Long accountId, @RequestBody Account account) {

        return accountService.getById(accountId);
    }
}
