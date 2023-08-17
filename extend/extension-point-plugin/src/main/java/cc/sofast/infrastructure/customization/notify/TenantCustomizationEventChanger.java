package cc.sofast.infrastructure.customization.notify;

import cc.sofast.infrastructure.customization.CustomizationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisStreamCommands;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public class TenantCustomizationEventChanger {

    private static final Logger logger = LoggerFactory.getLogger(TenantCustomizationEventChanger.class);

    private final StringRedisTemplate stringRedisTemplate;

    private final CustomizationProperties customizationProperties;

    public TenantCustomizationEventChanger(StringRedisTemplate stringRedisTemplate,
                                           CustomizationProperties customizationProperties) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.customizationProperties = customizationProperties;
    }

    /**
     * 租户配置项变更
     *
     * @param tenant 租户
     * @param key    key
     */
    public void updateOrDelete(String tenant, String key) {
        Assert.hasLength(tenant, "tenant must not null.");
        Assert.hasLength(key, "key must not empty.");
        RecordId recordId = stringRedisTemplate.execute(connection -> {
            Map<byte[], byte[]> mapByte = new HashMap<>(1);
            mapByte.put(customizationProperties.getStream().getKey().getBytes(), (tenant + "$" + key).getBytes());
            MapRecord<byte[], byte[], byte[]> event =
                    MapRecord.create(customizationProperties.getStream().getKey().getBytes(), mapByte);
            return connection.streamCommands()
                    .xAdd(event, RedisStreamCommands.XAddOptions.maxlen(customizationProperties.getStream().getMaxLen()));
        }, true);
        logger.info("pubTenantCustomization changer event: [{}] key: [{}] recordId: [{}]", tenant, key, recordId);
    }
}
