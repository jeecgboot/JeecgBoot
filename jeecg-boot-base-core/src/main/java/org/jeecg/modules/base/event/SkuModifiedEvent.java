package org.jeecg.modules.base.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SkuModifiedEvent extends ApplicationEvent {
    private final String skuId;
    private final String operation; // "INSERT", "UPDATE", "DELETE"

    public SkuModifiedEvent(Object source, String skuId, String operation) {
        super(source);
        this.skuId = skuId;
        this.operation = operation;
    }

}