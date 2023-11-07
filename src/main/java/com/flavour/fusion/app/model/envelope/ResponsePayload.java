package com.flavour.fusion.app.model.envelope;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

public class ResponsePayload extends LinkedHashMap<String, Object> {
    public ResponsePayload(String uri, HttpStatus httpStatus) {
        this.put("status", httpStatus.value());
        this.put("path", uri);
        this.put("timestamp", LocalDateTime.now().toString());
    }
}
