package org.jeecg.modules.test.seata.product.controller;

import org.jeecg.modules.test.seata.product.service.SeataProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author zyf
 */
@RestController
@RequestMapping("/test/seata/product")
public class SeataProductController {

    @Autowired
    private SeataProductService seataProductService;

    @PostMapping("/reduceStock")
    public BigDecimal reduceStock(Long productId, Integer count) {
        return seataProductService.reduceStock(productId, count);
    }
}
