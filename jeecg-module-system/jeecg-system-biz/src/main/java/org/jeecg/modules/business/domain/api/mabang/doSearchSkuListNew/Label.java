package org.jeecg.modules.business.domain.api.mabang.doSearchSkuListNew;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
 // 仓库信息,接口参数传showWarehouse才返回
public class Label {
    /**自定义分类ID*/
    @JSONField(name = "id")
    private long id;
    /**自定义分类名称**/
    @JSONField(name = "name")
    private String name;
}
