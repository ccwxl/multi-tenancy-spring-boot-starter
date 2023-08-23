package cc.sofast.infrastructure.tenant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class TenantBizService {

    public final Map<String, Function<Object, Object>> FUN = new ConcurrentHashMap<>();

    public Object exec(String tenant, Object param) {
        Function<Object, Object> prFunction = FUN.get(tenant);
        if (prFunction != null) {
            return prFunction.apply(param);
        }
        return null;
    }
}
