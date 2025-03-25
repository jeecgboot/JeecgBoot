package org.jeecg.modules.business.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ResponsesWithMsg<T> {
    protected Map<T, List<String>> successes = new HashMap<>();
    protected Map<T, List<String>> failures = new HashMap<>();

    public void addSuccess(T data) {
        successes.putIfAbsent(data, new ArrayList<>());
    }
    public void addSuccess(T data, String message) {
        successes.computeIfAbsent(data, k -> new ArrayList<>()).add(message);
    }
    public void addSuccess(T data, List<String> messages) {
        successes.computeIfAbsent(data, k -> new ArrayList<>()).addAll(messages);
    }
    public void addFailure(T data) {
        failures.putIfAbsent(data, new ArrayList<>());
    }
    public void addFailure(T data, String message) {
        failures.computeIfAbsent(data, k -> new ArrayList<>()).add(message);
    }
    public void addFailure(T data, List<String> messages) {
        failures.computeIfAbsent(data, k -> new ArrayList<>()).addAll(messages);
    }
}
