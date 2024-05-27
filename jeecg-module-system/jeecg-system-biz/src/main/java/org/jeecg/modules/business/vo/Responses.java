package org.jeecg.modules.business.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Responses {
    private List<String> successes = new ArrayList<>();
    private List<String> failures = new ArrayList<>();

    public void addSuccess(String message) {
        successes.add(message);
    }
    public void addFailure(String message) {
        failures.add(message);
    }
}
