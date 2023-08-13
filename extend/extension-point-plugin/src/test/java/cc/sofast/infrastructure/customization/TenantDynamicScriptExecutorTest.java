package cc.sofast.infrastructure.customization;

import cc.sofast.infrastructure.customization.file.FileCustomizationLoader;
import cc.sofast.infrastructure.customization.script.EngineExecutorResult;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.LongStream;

class TenantDynamicScriptExecutorTest {

    @Test
    void eval() throws InterruptedException {
        TenantDynamicScriptExecutor tds = new TenantDynamicScriptExecutor();
        tds.setTenantCustomization(new TenantCustomization(new FileCustomizationLoader()));
        tds.setScriptExecutor(new ScriptExecutor());

        TKey tKey = new TKey();
        tKey.setTenant("t");
        tKey.setKey("script");
        //先预热
        LongStream.range(0, 10).forEach(idx -> {
            Map<String, Object> param = new HashMap<>();
            param.put("idx", idx);
            tds.eval(tKey, param);
        });

        //压测
        CountDownLatch countDownLatch = new CountDownLatch(100);
        Long startTime = System.currentTimeMillis();
        LongStream.range(0, 100).forEach(idx -> new Thread(() -> {
            long innerStartTime = System.currentTimeMillis();
            Map<String, Object> param = new HashMap<>();
            param.put("idx", idx);
            EngineExecutorResult result = tds.eval(tKey, param);
            System.out.println(result.<Long>context() + "  innerCostTime: " + (System.currentTimeMillis() - innerStartTime));
            countDownLatch.countDown();
        }).start());
        countDownLatch.await();
        Long endTime = System.currentTimeMillis();
        System.out.println("cost time: " + (endTime - startTime));
    }
}