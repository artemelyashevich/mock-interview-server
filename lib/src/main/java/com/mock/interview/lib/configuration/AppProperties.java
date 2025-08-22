package com.mock.interview.lib.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private Twilio twilio;

    private Kafka kafka;

    private Pagination pagination;

    @Getter
    @Setter
    public static class Pagination {
        private int defaultPageSize;
        private int maxPageSize;
    }

    @Getter
    @Setter
    public static class Twilio {
        private Count count;
        private Auth auth;
        private Phone phone;
    }

    @Getter
    @Setter
    public static class Kafka {
        private Topic topic;
        private String transactionIdPrefix;
    }

    @Getter
    @Setter
    public static class Count {
        private String sid;
    }

    @Getter
    @Setter
    public static class Auth {
        private String token;
    }

    @Getter
    @Setter
    public static class Phone {
        private String number;
    }

    @Getter
    @Setter
    public static class Topic {
        private String name;
    }
}
