package com.hsy.log.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 日志配置
 *
 * @author huoshengyu
 * @version 1.0
 * @date 2020/12/17 20:10
 */
@ConfigurationProperties(prefix = "hsy.log")
public class LogProperties {
    /**
     * 发送类型
     */
    private LogType type;
    /**
     * 是否打印日志
     */
    private boolean console;
    /**
     * redis配置
     */
    private Redis redis;
    /**
     * redis配置
     */
    private Rabbit rabbit;
    /**
     * kafka配置
     */
    private Kafka kafka;

    public LogType getType() {
        return type;
    }

    public void setType(LogType type) {
        this.type = type;
    }

    public boolean isConsole() {
        return console;
    }

    public void setConsole(boolean console) {
        this.console = console;
    }

    public Redis getRedis() {
        return redis;
    }

    public void setRedis(Redis redis) {
        this.redis = redis;
    }

    public Rabbit getRabbit() {
        return rabbit;
    }

    public void setRabbit(Rabbit rabbit) {
        this.rabbit = rabbit;
    }

    public Kafka getKafka() {
        return kafka;
    }

    public void setKafka(Kafka kafka) {
        this.kafka = kafka;
    }

    /**
     * redis配置信息
     */
    public static class Redis {
        /**
         * list的key
         */
        private String key;
        /**
         * 最大条数
         */
        private Long maxCount;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Long getMaxCount() {
            return maxCount;
        }

        public void setMaxCount(Long maxCount) {
            this.maxCount = maxCount;
        }
    }

    /**
     * rabbit配置信息
     */
    public static class Rabbit {
        /**
         * 交换机
         */
        private String exchange;
        /**
         * 队列
         */
        private String queue;
        /**
         * 路由键
         */
        private String routingKey;
        /**
         * 请求头
         */
        private Map<String, String> headers;

        public String getExchange() {
            return exchange;
        }

        public void setExchange(String exchange) {
            this.exchange = exchange;
        }

        public String getQueue() {
            return queue;
        }

        public void setQueue(String queue) {
            this.queue = queue;
        }

        public String getRoutingKey() {
            return routingKey;
        }

        public void setRoutingKey(String routingKey) {
            this.routingKey = routingKey;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public void setHeaders(Map<String, String> headers) {
            this.headers = headers;
        }
    }

    /**
     * kafka配置信息
     */
    public static class Kafka {
        /**
         * 主题
         */
        private String topic;
        /**
         * 键
         */
        private String key;
        /**
         * 请求头
         */
        private Map<String, String> headers;

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public void setHeaders(Map<String, String> headers) {
            this.headers = headers;
        }
    }

    /**
     * 日志发送类型
     */
    public enum LogType {
        /**
         * REDIS
         */
        REDIS,
        /**
         * RABBIT
         */
        RABBIT,
        /**
         * KAFKA
         */
        KAFKA;
    }
}
