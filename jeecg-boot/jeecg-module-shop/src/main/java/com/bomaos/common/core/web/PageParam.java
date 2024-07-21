package com.bomaos.common.core.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 列表分页、排序、搜索通用接收参数封装
 * Created by Panyoujie on 2019-04-26 10:34
 */
public class PageParam<T> extends Page<T> {
    private static final String FILED_PAGE = "page";  // 第几页参数名称
    private static final String FILED_LIMIT = "limit";  // 每页显示数量参数名称
    private static final String FILED_SORT = "sort";  // 排序字段参数名称
    private static final String FILED_ORDER = "order";  // 排序方式参数名称
    private static final String VALUE_ORDER_ASC = "asc";  // 表示升序的值
    private static final String VALUE_ORDER_DESC = "desc";  // 表示降序的值
    private static final Pattern HUMP_PATTERN = Pattern.compile("[A-Z]");  // 驼峰转下划线正则匹配
    /**
     * 除分页、排序外的其他参数
     */
    private Map<String, Object> pageData;
    /**
     * 是否把字段名称驼峰转下划线
     */
    private boolean needToLine = true;

    public PageParam() {
        super();
    }

    public PageParam(HttpServletRequest request) {
        init(request);
    }

    public PageParam(HttpServletRequest request, boolean needToLine) {
        setNeedToLine(needToLine);
        init(request);
    }

    public boolean isNeedToLine() {
        return needToLine;
    }

    public PageParam<T> setNeedToLine(boolean needToLine) {
        this.needToLine = needToLine;
        return this;
    }

    public Map<String, Object> getPageData() {
        return pageData;
    }

    public void setPageData(Map<String, Object> data) {
        this.pageData = data;
    }

    /**
     * 从request中获取参数并填充到PageParam中
     */
    public PageParam<T> init(HttpServletRequest request) {
        String sortValue = null, orderValue = null;
        Map<String, Object> map = new HashMap<>();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = request.getParameter(name);
            if (value == null || value.isEmpty() || value.replace(" ", "").isEmpty()) {
                continue;
            }
            if (FILED_PAGE.equals(name)) {
                setCurrent(Long.parseLong(value));
            } else if (FILED_LIMIT.equals(name)) {
                setSize(Long.parseLong(value));
            } else if (FILED_SORT.equals(name)) {
                sortValue = (needToLine ? humpToLine(value) : value);
            } else if (FILED_ORDER.equals(name)) {
                orderValue = value;
            } else {
                map.put(name, value);
            }
        }
        setPageData(map);
        // 同步排序方式到MyBatisPlus中
        if (sortValue != null) {
            /* if (VALUE_ORDER_ASC.equals(orderValue))*/
            setOrder(sortValue, !VALUE_ORDER_DESC.equals(orderValue));
        }
        return this;
    }

    /**
     * 获取升序排序的字段
     */
    public List<String> getOrderAscs() {
        List<String> ascs = new ArrayList<>();
        List<OrderItem> orders = getOrders();
        if (orders != null) {
            for (OrderItem order : orders) {
                if (order.isAsc()) ascs.add(order.getColumn());
            }
        }
        return ascs;
    }

    /**
     * 获取降序排序的字段
     */
    public List<String> getOrderDescs() {
        List<String> descs = new ArrayList<>();
        List<OrderItem> orders = getOrders();
        if (orders != null) {
            for (OrderItem order : orders) {
                if (!order.isAsc()) descs.add(order.getColumn());
            }
        }
        return descs;
    }

    /**
     * 增加asc排序方式
     */
    public PageParam<T> addOrderAsc(String... ascs) {
        if (ascs != null) {
            List<String> tAscs = getOrderAscs();
            List<OrderItem> orderItems = new ArrayList<>();
            for (String column : ascs) {
                if (!tAscs.contains(column)) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setAsc(true);
                    orderItem.setColumn(column);
                    orderItems.add(orderItem);
                }
            }
            if (getOrders() == null) {
                setOrders(orderItems);
            } else {
                getOrders().addAll(orderItems);
            }
        }
        return this;
    }

    /**
     * 增加desc排序方式
     */
    public PageParam<T> addOrderDesc(String... descs) {
        if (descs != null) {
            List<String> tDescs = getOrderDescs();
            List<OrderItem> orderItems = new ArrayList<>();
            for (String column : descs) {
                if (!tDescs.contains(column)) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setAsc(false);
                    orderItem.setColumn(column);
                    orderItems.add(orderItem);
                }
            }
            if (getOrders() == null) {
                setOrders(orderItems);
            } else {
                getOrders().addAll(orderItems);
            }
        }
        return this;
    }

    /**
     * 移除某个排序字段
     */
    public PageParam<T> removeOrder(String order, Boolean isAsc) {
        List<OrderItem> orderItems = getOrders();
        if (orderItems != null) {
            Iterator<OrderItem> iterator = orderItems.iterator();
            while (iterator.hasNext()) {
                OrderItem item = iterator.next();
                if (isAsc == null || isAsc == item.isAsc()) {
                    if (order.equals(item.getColumn())) iterator.remove();
                }
            }
        }
        return this;
    }

    /**
     * 设置排序方式
     */
    public PageParam<T> setOrder(String order, boolean isAsc) {
        List<OrderItem> orderItems = new ArrayList<>();
        if (order != null) {
            OrderItem orderItem = new OrderItem();
            orderItem.setAsc(isAsc);
            orderItem.setColumn(order);
            orderItems.add(orderItem);
        }
        setOrders(orderItems);
        return this;
    }

    /**
     * 设置默认排序方式
     */
    public PageParam<T> setDefaultOrder(String[] ascs, String[] descs) {
        List<OrderItem> orderItems = getOrders();
        if (orderItems == null || orderItems.size() == 0) {
            addOrderAsc(ascs);
            addOrderDesc(descs);
        }
        return this;
    }

    /**
     * 往pageData里面增加参数
     */
    public PageParam<T> put(String key, Object value) {
        if (pageData == null) pageData = new HashMap<>();
        pageData.put(key, value);
        return this;
    }

    /**
     * 获取pageData里面参数
     */
    public Object get(String key) {
        return pageData.get(key);
    }

    public String getString(String key) {
        Object o = pageData.get(key);
        if (o != null) return String.valueOf(o);
        return null;
    }

    public Integer getInt(String key) {
        String str = getString(key);
        if (str != null) return Integer.parseInt(str);
        return null;
    }

    public Long getLong(String key) {
        String str = getString(key);
        if (str != null) return Long.parseLong(str);
        return null;
    }

    public Float getFloat(String key) {
        String str = getString(key);
        if (str != null) return Float.parseFloat(str);
        return null;
    }

    public Double getDouble(String key) {
        String str = getString(key);
        if (str != null) return Double.parseDouble(str);
        return null;
    }

    public Boolean getBoolean(String key) {
        String str = getString(key);
        if (str != null) return Boolean.parseBoolean(str);
        return null;
    }

    /**
     * 构建查询条件
     *
     * @param excludes 不包含的参数
     * @return QueryWrapper
     */
    public QueryWrapper<T> getWrapper(String... excludes) {
        List<String> exList = Arrays.asList(excludes);
        QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
        for (String key : pageData.keySet()) {
            if (!exList.contains(key)) buildWrapper(queryWrapper, key, getString(key));
        }
        return queryWrapper;
    }

    /**
     * 构建查询条件
     *
     * @param columns 只包含的参数
     * @return QueryWrapper
     */
    public QueryWrapper<T> getWrapperWith(String... columns) {
        List<String> keyList = Arrays.asList(columns);
        QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
        for (String key : pageData.keySet()) {
            if (keyList.contains(key)) buildWrapper(queryWrapper, key, getString(key));
        }
        return queryWrapper;
    }

    /**
     * 逐个构建QueryWrapper
     */
    private void buildWrapper(QueryWrapper<T> queryWrapper, String key, String value) {
        if (value == null || "deleted".equals(key)) return;
        if (Arrays.asList("id", "sortNumber", "state").contains(key) || key.endsWith("Id")) {
            queryWrapper.eq(needToLine ? humpToLine(key) : key, value);
        } else if ("createTimeStart".equals(key)) {
            queryWrapper.ge("create_time", value);
        } else if ("createTimeEnd".equals(key)) {
            queryWrapper.le("create_time", value);
        } else {
            queryWrapper.like(needToLine ? humpToLine(key) : key, value);
        }
    }

    /**
     * 构建排序的QueryWrapper
     *
     * @param queryWrapper 搜索条件的wrapper
     * @return QueryWrapper
     */
    public QueryWrapper<T> getOrderWrapper(QueryWrapper<T> queryWrapper) {
        if (queryWrapper == null) queryWrapper = new QueryWrapper<T>();
        queryWrapper.orderByAsc(getOrderAscs());
        queryWrapper.orderByDesc(getOrderDescs());
        return queryWrapper;
    }

    /**
     * 构建包含排序的查询条件
     *
     * @return QueryWrapper
     */
    public QueryWrapper<T> getOrderWrapper() {
        return getOrderWrapper(getWrapper());
    }

    /**
     * 获取不分页的参数，包含排序和pageData
     *
     * @return Map
     */
    public Map<String, Object> getNoPageParam() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageData", this.pageData);
        List<OrderItem> orders = getOrders();
        if (orders != null && orders.size() > 0) map.put("orders", orders);
        return map;
    }

    /**
     * 获取查询结果中第一条数据
     */
    public T getOne(List<T> records) {
        return records == null || records.size() == 0 ? null : records.get(0);
    }

    /**
     * 代码排序查询结果，实现类似SQL语句的排序效果
     */
    public List<T> sortRecords(List<T> records) {
        if (records == null || records.size() <= 1) return records;
        List<OrderItem> orderItems = getOrders();
        if (orderItems != null) {
            Comparator<T> comparator = null;
            for (OrderItem order : orderItems) {
                if (order.getColumn() != null) {
                    Function keyExtractor = t -> getFieldValue(t, needToLine ? lineToHump(order.getColumn()) : order.getColumn());
                    if (comparator == null) {
                        if (order.isAsc()) {
                            comparator = Comparator.comparing(keyExtractor);
                        } else {
                            comparator = Comparator.comparing(keyExtractor, Comparator.reverseOrder());
                        }
                    } else {
                        if (order.isAsc()) {
                            comparator.thenComparing(keyExtractor);
                        } else {
                            comparator.thenComparing(keyExtractor, Comparator.reverseOrder());
                        }
                    }
                }
            }
            if (comparator != null) {
                return records.stream().sorted(comparator).collect(Collectors.toList());
            }
        }
        return records;
    }

    /**
     * 驼峰转下划线
     */
    public static String humpToLine(String str) {
        if (str == null) return null;
        Matcher matcher = HUMP_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 下划线转驼峰
     */
    public static String lineToHump(String str) {
        if (str == null) return null;
        StringBuilder sb = new StringBuilder();
        String[] ss = str.split("_");
        sb.append(ss[0]);
        for (int i = 1; i < ss.length; i++) {
            if (ss[i].length() > 0) sb.append(ss[i].substring(0, 1).toUpperCase());
            if (ss[i].length() > 1) sb.append(ss[i].substring(1));
        }
        return sb.toString();
    }

    /**
     * 获取对象某个字段的值
     *
     * @param t     对象
     * @param field 字段
     * @return Object
     */
    public static Object getFieldValue(Object t, String field) {
        if (t == null || field == null) return null;
        try {
            Field clazzField = t.getClass().getDeclaredField(field);
            clazzField.setAccessible(true);
            return clazzField.get(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
