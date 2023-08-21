package cc.sofast.example.rest

import cc.sofast.example.bean.Account
import cc.sofast.infrastructure.customization.Cons;
import cc.sofast.infrastructure.customization.script.groovy.ScriptTemplate;
import org.springframework.context.ApplicationContext;

class TenantAccountConverter extends ScriptTemplate {

    @Override
    Object toRun(ApplicationContext applicationContext, String tenant, String key, Binding binding) {
        Account account = binding.getVariable("account") as Account
        account.setAge(account.getAge() + 10)
        return Cons.SUCCESS
    }
}
