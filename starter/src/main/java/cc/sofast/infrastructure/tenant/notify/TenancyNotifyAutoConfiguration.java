package cc.sofast.infrastructure.tenant.notify;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;

/**
 * @author apple
 */
@Configuration
@ConditionalOnBean(RedisConnectionFactory.class)
@EnableConfigurationProperties(TenantEventNotifyProperties.class)
public class TenancyNotifyAutoConfiguration {

    @Bean
    public TenantEventProcess tenantEventProcess() {

        return new TenantEventProcess();
    }

    @Bean
    public TenantEventListenerErrorHandler streamListenerErrorHandler() {

        return new TenantEventListenerErrorHandler();
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
                                .pollTimeout(Duration.ofSeconds(tenantEventNotifyProperties.getStream().getPollTimeout()))
                                .build());

        //读取stream的请求.
        StreamMessageListenerContainer.StreamReadRequest<String> tenantEventStream =
                StreamMessageListenerContainer.StreamReadRequest
                        .builder(StreamOffset.create(tenantEventNotifyProperties.getStream().getKey(), ReadOffset.lastConsumed()))
                        //出现链接异常等.不要取消订阅. 一旦取消订阅如果网络恢复就会丢失数据
                        .cancelOnError(throwable -> false)
                        //异常处理器
                        .errorHandler(tenantEventListenerErrorHandler)
                        .build();

        //不加group使用的XREAD命令.实现的是pub/sub模型.
        tenantEventListenerContainer.register(tenantEventStream, tenantEventProcess);
        return tenantEventListenerContainer;
    }
}
