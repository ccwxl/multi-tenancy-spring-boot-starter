package cc.sofast.example.extension;

import cc.sofast.example.bean.Account;
import cc.sofast.infrastructure.extension.Extension;
import org.springframework.context.ApplicationContext;

@Extension(bizId = AccountExtPt.BIZ_ID, scenario = AccountExtPt.SCENARIO)
public class DefaultAccountExtPt implements AccountExtPt {

    @Override
    public Account accountCalculate(Account account) {

        return null;
    }

    @Override
    public void registerBefore(ApplicationContext applicationContext) {

    }
}
