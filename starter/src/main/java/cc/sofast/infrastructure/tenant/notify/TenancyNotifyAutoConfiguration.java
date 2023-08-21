package cc.sofast.infrastructure.tenant.notify;

import cc.sofast.infrastructure.tenant.datasource.TenantDataSourceRegister;
import cc.sofast.infrastructure.tenant.redis.TenantRedisConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.Duration;

/**
 * @author apple
 */
@Configuration
@AutoConfigureAfter(TenantRedisConfiguration.class)
@EnableConfigurationProperties(TenantEventNotifyProperties.class)
public class TenancyNotifyAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TenantEventProcess tenantEventProcess(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder,
                                                 TenantDataSourceRegister tenantDataSourceRegister,
                                                 TenantEventNotifyProperties tenantEventNotifyProperties) {
        ObjectMapper objectMapper = jackson2ObjectMapperBuilder.createXmlMapper(false).build();
        return new TenantEventProcess(objectMapper, tenantDataSourceRegister, tenantEventNotifyProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public TenantEventListenerErrorHandler tenantEventListenerErrorHandler() {

        return new TenantEventListenerErrorHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public TenantNotify tenantNotify(TenantEventNotifyProperties tenantEventNotifyProperties,
                                     StringRedisTemplate stringRedisTemplate,
                                     Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
        ObjectMapper objectMapper = jackson2ObjectMapperBuilder.createXmlMapper(false).build();
        return new TenantNotify(tenantEventNotifyProperties, stringRedisTemplate, objectMapper);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> tenantEventStreamMessageListenerContainer(
            RedisConnectionFactory redisConnectionFactory, TenantEventProcess tenantEventProcess,
            TenantEventNotifyProperties tenantEventNotifyProperties, TenantEventListenerErrorHandler tenantEventListenerErrorHandler) {
        //监听容器
        StreamMessageListenerContainer<String, MapRecord<String, String, String>> tenantEventListenerContainer =
                StreamMessageListenerContainer.create(redisConnectionFactory,
                        StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                                .builder()
                                .batchSize(tenantEventNotifyProperties.getStream().getPoolSize())
                                .pollTimeout(Duration.ofMillis(tenantEventNotifyProperties.getStream().getPollTimeout()))
                                .build());

        //读取stream的请求. 这里是需要广播的模式，而不是指定消费者组
        StreamMessageListenerContainer.StreamReadRequest<String> tenantEventStream =
                StreamMessageListenerContainer.StreamReadRequest
                        .builder(StreamOffset.create(tenantEventNotifyProperties.getStream().getKey(), ReadOffset.lastConsumed()))
                        //出现链接异常等.不要取消订阅. 一旦取消订阅如果网络恢复就会丢失数据
                        .cancelOnError(throwable -> false)
                        //异常处理器
                        .errorHandler(tenantEventListenerErrorHandler)
                        .build();

        //不加group使用的 XREAD 命令.实现的是pub/sub模型.
        tenantEventListenerContainer.register(tenantEventStream, tenantEventProcess);
        return tenantEventListenerContainer;
    }
}
