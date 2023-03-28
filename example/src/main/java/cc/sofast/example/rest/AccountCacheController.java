package cc.sofast.example.rest;

import cc.sofast.example.bean.Account;
import cc.sofast.example.service.AccountService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author apple
 */
@RestController
@RequestMapping("cache")
@CacheConfig(cacheNames = "account")
public class AccountCacheController {

    private final AccountService accountService;

    public AccountCacheController(AccountService accountService) {
        this.accountService = accountService;
    }


    @Cacheable(value = "account", key = "#account", sync = true)
    @GetMapping("{account}")
    public Account getAccount(@PathVariable Long account) {
        Account accountServiceById = accountService.getById(account);
        return accountServiceById;
    }
}
