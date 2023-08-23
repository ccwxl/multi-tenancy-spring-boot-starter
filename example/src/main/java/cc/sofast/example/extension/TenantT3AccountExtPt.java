package cc.sofast.example.extension;

import cc.sofast.example.bean.Account;
import cc.sofast.infrastructure.extension.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

@Extension(tenant = "t3", bizId = AccountExtPt.BIZ_ID, scenario = AccountExtPt.SCENARIO)
public class TenantT3AccountExtPt implements AccountExtPt {

    private static final Logger log = LoggerFactory.getLogger(TenantT3AccountExtPt.class);

    @Override
    public Account accountCalculate(Account account) {
        log.info("call t3 ext point.");
        account.setNickname("t3: " + account.getNickname());
        return account;
    }

    @Override
    public void registerBefore(ApplicationContext applicationContext) {

    }
}
