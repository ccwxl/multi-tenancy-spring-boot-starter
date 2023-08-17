package cc.sofast.infrastructure.customization;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = CustomizationProperties.PREFIX)
public class CustomizationProperties {

    public static final String PREFIX = "spring.multitenancy.customization";

    private Stream stream = new Stream();

    public static class Stream {

        private int poolSize = 10;

        private int pollTimeout = 100;

        private String key = "tenant_customization_event_stream";

        private long maxLen = 1000;

        public int getPoolSize() {
            return poolSize;
        }

        public void setPoolSize(int poolSize) {
            this.poolSize = poolSize;
        }

        public int getPollTimeout() {
            return pollTimeout;
        }

        public void setPollTimeout(int pollTimeout) {
            this.pollTimeout = pollTimeout;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public long getMaxLen() {

            return maxLen;
        }

        public void setMaxLen(long maxLen) {
            this.maxLen = maxLen;
        }
    }

    public Stream getStream() {
        return stream;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }
}
