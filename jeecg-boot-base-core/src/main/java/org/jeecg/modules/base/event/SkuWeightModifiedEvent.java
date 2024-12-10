package org.jeecg.modules.base.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SkuWeightModifiedEvent extends ApplicationEvent {
    private final String id;
    private final String operation; // "INSERT", "UPDATE", "DELETE"

    public SkuWeightModifiedEvent(Object source, String id, String operation) {
        super(source);
        this.id = id;
        this.operation = operation;
    }

}