package com.iex.stocktrading.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "trace_log")
public class CustomHttpTrace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date timestamp;
    private String username;
    private Integer status;
    private String sourceIp;
    private String path;
    private String queryParams;
    private String method;
    private long timeTaken;
    private String payload;
    @Column(length=1000)
    private String rawBody;

    public CustomHttpTrace(CustomHttpTraceBuilder builder) {
        this.timestamp = builder.timestamp;
        this.username = builder.username;
        this.status = builder.status;
        this.sourceIp = builder.sourceIp;
        this.path = builder.path;
        this.queryParams = builder.queryParams;
        this.method = builder.method;
        this.timeTaken = builder.timeTaken;
        this.payload = builder.payload;
        this.rawBody = builder.rawBody;
    }

    public static class CustomHttpTraceBuilder {
        private Date timestamp;
        private String username;
        private Integer status;
        private String sourceIp;
        private String path;
        private String queryParams;
        private String method;
        private long timeTaken;
        private String payload;
        private String rawBody;

        public CustomHttpTraceBuilder(){ }

        public CustomHttpTraceBuilder timestamp(Date s) {
            this.timestamp = s;
            return this;
        }

        public CustomHttpTraceBuilder username(String s) {
            this.username = s;
            return this;
        }

        public CustomHttpTraceBuilder status(Integer s) {
            this.status = s;
            return this;
        }

        public CustomHttpTraceBuilder sourceIp(String s) {
            this.sourceIp = s;
            return this;
        }

        public CustomHttpTraceBuilder path(String s) {
            this.path = s;
            return this;
        }

        public CustomHttpTraceBuilder queryParams(String s) {
            this.queryParams = s;
            return this;
        }

        public CustomHttpTraceBuilder method(String s) {
            this.method = s;
            return this;
        }

        public CustomHttpTraceBuilder timeTaken(long s) {
            this.timeTaken = s;
            return this;
        }

        public CustomHttpTraceBuilder payload(String s) {
            this.payload = s;
            return this;
        }

        public CustomHttpTraceBuilder rawBody(String s) {
            this.rawBody = s;
            return this;
        }

        public CustomHttpTrace build() {
            return new CustomHttpTrace(this);
        }
    }

}