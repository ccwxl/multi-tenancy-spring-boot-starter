package cc.sofast.infrastructure.tenant.notify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisStreamCommands;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;

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

    public TenantNotify(TenantEventNotifyProperties tenantEventNotifyProperties,
                        StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper) {
        this.tenantEventNotifyProperties = tenantEventNotifyProperties;
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 广播到其他服务上去. 增加租户事件
     *
     * @param tenantEvent 租户事件
     */
    public void pubTenantEvent(TenantEvent tenantEvent) {
        Assert.notEmpty(tenantEvent.getTenants(), "tenant must not null.");
        Assert.hasLength(tenantEvent.getDsType(), "dsType must not empty.");
        Assert.hasLength(tenantEvent.getUrl(), "dsUrl must not empty.");
        Assert.hasLength(tenantEvent.getUsername(), "dsUsername must not empty.");
        Assert.hasLength(tenantEvent.getPassword(), "dsPassword must not empty.");
        RecordId recordId = stringRedisTemplate.execute(connection -> {
            try {
                byte[] eventBytes = objectMapper.writeValueAsBytes(tenantEvent);
                Map<byte[], byte[]> mapByte = new HashMap<>(16);
                mapByte.put(tenantEventNotifyProperties.getStream().getKey().getBytes(), eventBytes);
                MapRecord<byte[], byte[], byte[]> event =
                        MapRecord.create(tenantEventNotifyProperties.getStream().getKey().getBytes(), mapByte);
                return connection.streamCommands()
                        .xAdd(event, RedisStreamCommands.XAddOptions.maxlen(tenantEventNotifyProperties.getStream().getMaxLen()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }, true);

        logger.info("pubTenantEvent tenant: [{}] RecordId: [{}]", tenantEvent.getTenants(), recordId);
    }
}
