package cc.sofast.infrastructure.customization.notify;

import cc.sofast.infrastructure.customization.CustomizationProperties;
import cc.sofast.infrastructure.customization.TKey;
import cc.sofast.infrastructure.customization.mem.MemCustomizationLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;

public class TenantCustomizationEventListener implements StreamListener<String, MapRecord<String, String, String>> {

    private static final Logger logger = LoggerFactory.getLogger(TenantCustomizationEventChanger.class);

    private final MemCustomizationLoader memCustomizationLoader;

    public final CustomizationProperties customizationProperties;

    public TenantCustomizationEventListener(MemCustomizationLoader memCustomizationLoader,
                                            CustomizationProperties customizationProperties) {
        this.memCustomizationLoader = memCustomizationLoader;
        this.customizationProperties = customizationProperties;
    }

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        //某个配置被更新了，直接清空本地缓存，重新加载
        String val = message.getValue().get(customizationProperties.getStream().getKey());
        String[] tk = val.split("\\$");
        memCustomizationLoader.remove(new TKey(tk[0], tk[1]));
        logger.info("received customization change event tenant: [{}] key: [{}]", tk[0], tk[1]);
    }
}
