package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import org.jeecg.modules.business.entity.ShippingInvoiceEntity;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.data.relational.core.mapping.event.AbstractRelationalEventListener;
import org.springframework.data.relational.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

/**
 * Strategy to generate id when inserting shipping invoice entity to DB.
 */
@Component
public class BeforeSaveListener extends AbstractRelationalEventListener<ShippingInvoiceEntity> {

    @Override
    protected void onBeforeSave(BeforeSaveEvent<ShippingInvoiceEntity> event) {
        ShippingInvoiceEntity entity = event.getEntity();
        /* this method is guaranteed to not be null, please omit warning of IDE in case of exist. */
        entity.setId(
                "" + new DefaultIdentifierGenerator().nextId(entity)
        );
    }
}
