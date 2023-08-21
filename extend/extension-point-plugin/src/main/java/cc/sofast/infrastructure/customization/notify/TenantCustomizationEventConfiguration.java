package cc.sofast.infrastructure.customization.notify;

import cc.sofast.infrastructure.customization.ComposeCustomizationLoader;
import cc.sofast.infrastructure.customization.CustomizationProperties;
import cc.sofast.infrastructure.customization.mem.MemCustomizationLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.util.ErrorHandler;

import java.time.Duration;

@Configuration
@EnableConfigurationProperties(CustomizationProperties.class)
public class TenantCustomizationEventConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TenantCustomizationEventChanger tenantCustomizationEventChanger(StringRedisTemplate stringRedisTemplate,
                                                                           CustomizationProperties customizationProperties) {

        return new TenantCustomizationEventChanger(stringRedisTemplate, customizationProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public TenantCustomizationEventListener customizationEventListener(MemCustomizationLoader memCustomizationLoader,
                                                                       CustomizationProperties customizationProperties) {

        return new TenantCustomizationEventListener(memCustomizationLoader, customizationProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public ErrorHandler customizationErrorHandler() {

        return new ErrorHandler() {

            private static final Logger log = LoggerFactory.getLogger("customizationErrorHandler");

            @Override
            public void handleError(Throwable t) {
                log.error("customizationError ", t);
            }
        };
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnMissingBean
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> tenantCustomizationEventListenerContainer(
            RedisConnectionFactory redisConnectionFactory, CustomizationProperties customizationProperties,
            TenantCustomizationEventListener customizationEventListener, ErrorHandler customizationErrorHandler) {
        //监听容器
        StreamMessageListenerContainer<String, MapRecord<String, String, String>> tenantEventListenerContainer =
                StreamMessageListenerContainer.create(redisConnectionFactory,
                        StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                                .builder()
                                .batchSize(customizationProperties.getStream().getPoolSize())
                                .pollTimeout(Duration.ofMillis(customizationProperties.getStream().getPollTimeout()))
                                .build());

        //读取stream的请求. 这里是需要广播的模式，而不是指定消费者组
        StreamMessageListenerContainer.StreamReadRequest<String> customizationStream =
                StreamMessageListenerContainer.StreamReadRequest
                        .builder(StreamOffset.create(customizationProperties.getStream().getKey(), ReadOffset.lastConsumed()))
                        //出现链接异常等.不要取消订阅. 一旦取消订阅如果网络恢复就会丢失数据
                        .cancelOnError(throwable -> false)
                        //异常处理器
                        .errorHandler(customizationErrorHandler)
                        .build();

        //不加group使用的 XREAD 命令.实现的是pub/sub模型.
        tenantEventListenerContainer.register(customizationStream, customizationEventListener);
        return tenantEventListenerContainer;
    }
}
