package cc.sofast.infrastructure.tenant.notify;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisStreamCommands;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author apple
 */
public class TenantNotify {

    private static final Logger logger = LoggerFactory.getLogger(TenantNotify.class);

    private final TenantEventNotifyProperties tenantEventNotifyProperties;

    private final StringRedisTemplate stringRedisTemplate;

    private final ObjectMapper objectMapper;

    public TenantNotify(TenantEventNotifyProperties tenantEventNotifyProperties, StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper) {
        this.tenantEventNotifyProperties = tenantEventNotifyProperties;
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 广播到其他服务上去. 增加租户事件
     *
     * @param tenantEvent 租户事件
     */
    private void pubTenantEvent(TenantEvent tenantEvent) {
        RecordId recordId = stringRedisTemplate.execute(connection -> {
            Map<String, String> mapVal = objectMapper.convertValue(tenantEvent, new TypeReference<>() {
            });
            Map<byte[], byte[]> mapByte = new HashMap<>(16);
            for (Map.Entry<String, String> maps : mapVal.entrySet()) {
                if (maps.getValue() != null) {
                    mapByte.put(maps.getKey().getBytes(), maps.getValue().getBytes());
                }
            }
            MapRecord<byte[], byte[], byte[]> event =
                    MapRecord.create(tenantEventNotifyProperties.getStream().getKey().getBytes(), mapByte);
            return connection.streamCommands().xAdd(event, RedisStreamCommands.XAddOptions.maxlen(tenantEventNotifyProperties.getStream().getMaxLen()));
        }, true);
        logger.info("pubTenantEvent tenant: [{}] RecordId: [{}]", tenantEvent.getTenant(), recordId);
    }
}
