package com.mock.interview.lib.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Slf4j
@Getter
@Setter
@ToString
@Validated
@Component
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@EnableConfigurationProperties(AppProperties.class)
public class AppProperties {

    @Valid
    private Twilio twilio = new Twilio();

    @Valid
    private Kafka kafka = new Kafka();

    @Valid
    private Pagination pagination = new Pagination();

    @Valid
    private Report report = new Report();

    private Email email = new Email();

    @Getter
    @Setter
    @ToString
    public static class Email {
        private String sender;
    }

    @Getter
    @Setter
    @ToString
    public static class Pagination {
        @Min(1)
        @Max(100)
        private int defaultPageSize = 20;

        @Min(1)
        @Max(500)
        private int maxPageSize = 100;
    }

    @Getter
    @Setter
    @ToString
    public static class Twilio {
        @Valid
        private Count count = new Count();

        @Valid
        private Auth auth = new Auth();

        @Valid
        private Phone phone = new Phone();

        @Getter
        @Setter
        @ToString
        public static class Count {
            @NotBlank
            private String sid;
        }

        @Getter
        @Setter
        @ToString
        public static class Auth {
            @NotBlank
            private String token;
        }

        @Getter
        @Setter
        @ToString
        public static class Phone {
            @NotBlank
            private String number;
        }
    }

    @Getter
    @Setter
    @ToString
    public static class Kafka {
        @Valid
        private Topic topic = new Topic();

        @NotBlank
        private String transactionIdPrefix = "txn_";

        @Getter
        @Setter
        @ToString
        public static class Topic {
            @NotBlank
            private String name;
        }
    }

    @Getter
    @Setter
    @ToString
    public static class Report {
        @Valid
        private Generation generation = new Generation();

        @Getter
        @Setter
        @ToString
        public static class Generation {
            @Valid
            private Threads threads = new Threads();

            @Valid
            private Queue queue = new Queue();

            @Valid
            private Timeout timeout = new Timeout();

            @Getter
            @Setter
            @ToString
            public static class Threads {
                @Min(1)
                @Max(50)
                private int core = 5;

                @Min(1)
                @Max(100)
                private int max = 10;

                @Min(1)
                @Max(300)
                private int keepAlive = 60;
            }

            @Getter
            @Setter
            @ToString
            public static class Queue {
                @Min(1)
                @Max(10000)
                private int capacity = 100;
            }

            @Getter
            @Setter
            @ToString
            public static class Timeout {
                @Min(1)
                @Max(300)
                private int seconds = 30;
            }
        }
    }

    // Helper methods for easy access to nested properties
    public String getTwilioAccountSid() {
        return twilio.getCount().getSid();
    }

    public String getTwilioAuthToken() {
        return twilio.getAuth().getToken();
    }

    public String getTwilioPhoneNumber() {
        return twilio.getPhone().getNumber();
    }

    public String getKafkaTopicName() {
        return kafka.getTopic().getName();
    }

    public int getReportGenerationCoreThreads() {
        return report.getGeneration().getThreads().getCore();
    }

    public int getReportGenerationMaxThreads() {
        return report.getGeneration().getThreads().getMax();
    }

    public int getReportGenerationQueueCapacity() {
        return report.getGeneration().getQueue().getCapacity();
    }

    public int getReportGenerationTimeoutSeconds() {
        return report.getGeneration().getTimeout().getSeconds();
    }

    public int getReportGenerationKeepAliveSeconds() {
        return report.getGeneration().getThreads().getKeepAlive();
    }
}