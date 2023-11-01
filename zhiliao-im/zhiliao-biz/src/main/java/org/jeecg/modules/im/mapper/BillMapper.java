package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Bill;
import org.jeecg.modules.im.entity.Bill;
import org.jeecg.modules.im.entity.query_helper.QBill;

/**
 * <p>
 * 账单 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-03-25
 */
@Mapper
public interface BillMapper extends BaseMapper<Bill> {
    MyPage<Bill> pagination(@Param("pg") MyPage<Bill> pg, @Param("q") QBill q);

}
