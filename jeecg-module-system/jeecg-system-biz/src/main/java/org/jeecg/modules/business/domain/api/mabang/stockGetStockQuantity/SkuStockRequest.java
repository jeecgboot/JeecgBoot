package org.jeecg.modules.business.domain.api.mabang.stockGetStockQuantity;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.Request;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuListNew.SkuListRequestBody;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuListNew.SkuListResponse;
import org.springframework.http.ResponseEntity;

/**
 * This class contains some key information and necessary procedures
 * to send a request to mabang "get order list" API, for example: target URL,
 * correspondent HTTP method, procedure to generate authorization.
 * <p>
 * One can use static method {@code sendRequest} to send request with body,
 * and then get respective response. Or use instance of this class, see below.
 * <p>
 * Because data returned by target API is paginated. One can retrieve all data
 * by calling next and hasNext.
 */
@Slf4j
public class SkuStockRequest extends Request {

    public SkuStockRequest(SkuStockRequestBody body) {
        super(body);
    }


    @Override
    public SkuStockResponse send() {
        ResponseEntity<String> res = rawSend();
        return SkuStockResponse.parse(JSON.parseObject(res.getBody()));
    }
}
