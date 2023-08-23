package cc.sofast.example.extension;


import cc.sofast.example.bean.Account;
import cc.sofast.infrastructure.extension.ExtensionPointI;

public interface AccountExtPt extends ExtensionPointI {

     String BIZ_ID="account";

     String SCENARIO="converter";

    Account accountCalculate(Account account);
}
