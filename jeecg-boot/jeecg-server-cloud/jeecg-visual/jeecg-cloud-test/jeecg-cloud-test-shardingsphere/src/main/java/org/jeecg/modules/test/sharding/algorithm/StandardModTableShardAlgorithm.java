package org.jeecg.modules.test.sharding.algorithm;


import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.Collection;
import java.util.Properties;

/**
 * 用于处理使用单一键
 * 根据分片字段的值和sharding-count进行取模运算
 * SQL 语句中有>，>=, <=，<，=，IN 和 BETWEEN AND 操作符，都可以应用此分片策略。
 *
 * @author zyf
 */
public class StandardModTableShardAlgorithm implements StandardShardingAlgorithm<Integer> {
    private Properties props = new Properties();


    /**
     * 用于处理=和IN的分片
     *
     * @param collection           目标分片的集合(表名)
     * @param preciseShardingValue 逻辑表相关信息
     * @return
     */
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Integer> preciseShardingValue) {

        for (String name : collection) {
            Integer value = preciseShardingValue.getValue();
            //根据值进行取模，得到一个目标值
            if (name.indexOf(value % 2+"") > -1) {
                return name;
            }
        }
        throw new UnsupportedOperationException();
    }

    /**
     * 用于处理BETWEEN AND分片，如果不配置RangeShardingAlgorithm，SQL中的BETWEEN AND将按照全库路由处理
     *
     * @param collection
     * @param rangeShardingValue
     * @return
     */
    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Integer> rangeShardingValue) {

        return collection;
    }

    /**
     * 初始化对象的时候调用的方法
     */
    @Override
    public void init() {
    }

    /**
     * 对应分片算法（sharding-algorithms）的类型
     *
     * @return
     */
    @Override
    public String getType() {
        return "STANDARD_MOD";
    }

    @Override
    public Properties getProps() {
        return this.props;
    }

    /**
     * 获取分片相关属性
     *
     * @param properties
     */
    @Override
    public void setProps(Properties properties) {
        this.props = properties;
    }
}