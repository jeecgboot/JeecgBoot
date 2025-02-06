package org.jeecg.modules.business.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ResponsesWithMsg {
    private Map<String, List<String>> successes = new HashMap<>();
    private Map<String, List<String>> failures = new HashMap<>();

    public void addSuccess(String message) {
        successes.putIfAbsent(message, new ArrayList<>());
    }
    public void addSuccess(String key, String message) {
        if (!successes.containsKey(key)) {
            successes.put(key, new ArrayList<>());
        }
        successes.get(key).add(message);
    }
    public void addSuccess(String key, List<String> messages) {
        if (!successes.containsKey(key)) {
            successes.put(key, new ArrayList<>());
        }
        successes.get(key).addAll(messages);
    }
    public void addFailure(String message) {
        failures.putIfAbsent(message, new ArrayList<>());
    }
    public void addFailure(String key, String message) {
        if (!failures.containsKey(key)) {
            failures.put(key, new ArrayList<>());
        }
        failures.get(key).add(message);
    }
    public void addFailure(String key, List<String> messages) {
        if (!failures.containsKey(key)) {
            failures.put(key, new ArrayList<>());
        }
        failures.get(key).addAll(messages);
    }
}
