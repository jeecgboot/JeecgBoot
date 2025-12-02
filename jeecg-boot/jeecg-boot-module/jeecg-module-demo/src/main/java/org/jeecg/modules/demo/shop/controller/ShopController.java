package org.jeecg.modules.demo.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.demo.shop.entity.Order;
import org.jeecg.modules.demo.shop.entity.Product;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 商品管理模拟接口
 * 用于AI Agent通过工具帮助用户查询商品并采购的业务演示
 * @Author: chenrui
 * @Date: 2025-11-06
 */
@Tag(name = "商品管理Demo")
@RestController
@RequestMapping("/demo/shop")
@Slf4j
public class ShopController {
    
    /**
     * 商品数据存储(内存)
     */
    private final Map<String, Product> productStore = new ConcurrentHashMap<>();
    
    /**
     * 订单数据存储(内存)
     */
    private final Map<String, Order> orderStore = new ConcurrentHashMap<>();
    
    /**
     * 订单ID生成器
     */
    private final AtomicInteger orderIdGenerator = new AtomicInteger(1000);
    
    /**
     * 初始化商品数据
     *
     * @author chenrui
     * @date 2025/11/6 14:30
     */
    @PostConstruct
    public void initProducts() {
        // 电子产品
        productStore.put("P001", new Product("P001", "iPhone 15 Pro", new BigDecimal("7999.00"), "电子产品", "Apple最新旗舰手机,6.1英寸屏幕,钛金属边框", 50));
        productStore.put("P002", new Product("P002", "MacBook Pro 14", new BigDecimal("14999.00"), "电子产品", "M3 Pro芯片,16GB内存,512GB存储", 30));
        productStore.put("P003", new Product("P003", "AirPods Pro 2", new BigDecimal("1899.00"), "电子产品", "主动降噪无线耳机,支持空间音频", 100));
        productStore.put("P004", new Product("P004", "iPad Air", new BigDecimal("4799.00"), "电子产品", "10.9英寸液晶显示屏,M1芯片", 60));
        
        // 图书
        productStore.put("B001", new Product("B001", "Java核心技术卷I", new BigDecimal("119.00"), "图书", "Java编程经典教材,适合初学者和进阶开发者", 200));
        productStore.put("B002", new Product("B002", "深入理解计算机系统", new BigDecimal("139.00"), "图书", "CSAPP经典教材,计算机系统必读书籍", 150));
        productStore.put("B003", new Product("B003", "设计模式", new BigDecimal("89.00"), "图书", "软件设计经典著作,GoF四人组著作", 180));
        
        // 生活用品
        productStore.put("L001", new Product("L001", "小米电动牙刷", new BigDecimal("199.00"), "生活用品", "声波震动,IPX7防水,续航18天", 300));
        productStore.put("L002", new Product("L002", "戴森吹风机", new BigDecimal("2990.00"), "生活用品", "快速干发,智能温控,保护头发", 80));
        productStore.put("L003", new Product("L003", "膳魔师保温杯", new BigDecimal("259.00"), "生活用品", "真空保温,304不锈钢,保温12小时", 500));
        
        // 食品
        productStore.put("F001", new Product("F001", "三只松鼠坚果礼盒", new BigDecimal("159.00"), "食品", "混合坚果大礼包,1500g装", 400));
        productStore.put("F002", new Product("F002", "茅台飞天53度", new BigDecimal("2899.00"), "食品", "贵州茅台酒,500ml", 20));
        productStore.put("F003", new Product("F003", "星巴克咖啡豆", new BigDecimal("128.00"), "食品", "中度烘焙,派克市场,250g", 250));
        
        log.info("商品数据初始化完成,共{}个商品", productStore.size());
    }
    
    /**
     * 查询商品列表
     *
     * @param category 商品分类(可选)
     * @param keyword 搜索关键词(可选)
     * @return 商品列表
     * @author chenrui
     * @date 2025/11/6 14:30
     */
    @Operation(summary = "查询商品列表", description = "支持按分类和关键词搜索")
    @GetMapping("/products")
    public Result<List<Product>> getProducts(
            @Parameter(description = "商品分类") @RequestParam(required = false) String category,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword) {
        
        log.info("查询商品列表 - 分类: {}, 关键词: {}", category, keyword);
        
        List<Product> products = new ArrayList<>(productStore.values());
        
        // 按分类过滤
        if (category != null && !category.trim().isEmpty()) {
            products = products.stream()
                    .filter(p -> category.equals(p.getCategory()))
                    .collect(Collectors.toList());
        }
        
        // 按关键词过滤(搜索商品名称和描述)
        if (keyword != null && !keyword.trim().isEmpty()) {
            String searchKey = keyword.toLowerCase();
            products = products.stream()
                    .filter(p -> p.getName().toLowerCase().contains(searchKey) 
                            || p.getDescription().toLowerCase().contains(searchKey))
                    .collect(Collectors.toList());
        }
        
        // 按价格排序
        products.sort(Comparator.comparing(Product::getPrice));
        
        log.info("查询到{}个商品", products.size());
        return Result.OK(products);
    }
    
    /**
     * 查询商品库存
     *
     * @param productId 商品ID
     * @return 库存信息
     * @author chenrui
     * @date 2025/11/6 14:30
     */
    @Operation(summary = "查询商品库存", description = "根据商品ID查询库存数量")
    @GetMapping("/stock")
    public Result<Map<String, Object>> getStock(
            @Parameter(description = "商品ID", required = true) @RequestParam String productId) {
        
        log.info("查询商品库存 - 商品ID: {}", productId);
        
        Product product = productStore.get(productId);
        if (product == null) {
            return Result.error("商品不存在: " + productId);
        }
        
        Map<String, Object> stockInfo = new HashMap<>();
        stockInfo.put("productId", product.getId());
        stockInfo.put("productName", product.getName());
        stockInfo.put("stock", product.getStock());
        stockInfo.put("available", product.getStock() > 0);
        
        return Result.OK(stockInfo);
    }
    
    /**
     * 购买商品(下单)
     *
     * @param productId 商品ID
     * @param quantity 购买数量
     * @param userId 用户ID(可选)
     * @return 订单信息
     * @author chenrui
     * @date 2025/11/6 14:30
     */
    @Operation(summary = "购买商品", description = "创建订单,但不立即扣减库存")
    @PostMapping("/purchase")
    public Result<Order> purchase(
            @Parameter(description = "商品ID", required = true) @RequestParam String productId,
            @Parameter(description = "购买数量", required = true) @RequestParam Integer quantity,
            @Parameter(description = "用户ID") @RequestParam(required = false) String userId) {
        
        log.info("购买商品 - 商品ID: {}, 数量: {}, 用户: {}", productId, quantity, userId);
        
        // 参数校验
        if (quantity == null || quantity <= 0) {
            return Result.error("购买数量必须大于0");
        }
        
        // 查询商品
        Product product = productStore.get(productId);
        if (product == null) {
            return Result.error("商品不存在: " + productId);
        }
        
        // 检查库存
        if (product.getStock() < quantity) {
            return Result.error("库存不足,当前库存: " + product.getStock());
        }
        
        // 创建订单
        String orderId = "O" + orderIdGenerator.incrementAndGet();
        BigDecimal totalAmount = product.getPrice().multiply(new BigDecimal(quantity));
        
        Order order = new Order();
        order.setId(orderId);
        order.setProductId(productId);
        order.setProductName(product.getName());
        order.setQuantity(quantity);
        order.setUnitPrice(product.getPrice());
        order.setTotalAmount(totalAmount);
        order.setStatus("pending");
        order.setCreateTime(new Date());
        order.setUserId(userId);
        
        orderStore.put(orderId, order);
        
        log.info("订单创建成功 - 订单ID: {}, 总金额: {}", orderId, totalAmount);
        return Result.OK(order);
    }
    
    /**
     * 扣减商品库存
     *
     * @param orderId 订单ID
     * @return 扣减结果
     * @author chenrui
     * @date 2025/11/6 14:30
     */
    @Operation(summary = "扣减商品库存", description = "根据订单ID扣减对应商品库存")
    @PostMapping("/stock/deduct")
    public Result<Map<String, Object>> deductStock(
            @Parameter(description = "订单ID", required = true) @RequestParam String orderId) {
        
        log.info("扣减库存 - 订单ID: {}", orderId);
        
        // 查询订单
        Order order = orderStore.get(orderId);
        if (order == null) {
            return Result.error("订单不存在: " + orderId);
        }
        
        // 检查订单状态
        if ("paid".equals(order.getStatus())) {
            return Result.error("订单已支付,库存已扣减");
        }
        
        if ("cancelled".equals(order.getStatus())) {
            return Result.error("订单已取消");
        }
        
        // 查询商品
        Product product = productStore.get(order.getProductId());
        if (product == null) {
            return Result.error("商品不存在: " + order.getProductId());
        }
        
        // 检查库存
        synchronized (product) {
            if (product.getStock() < order.getQuantity()) {
                return Result.error("库存不足,当前库存: " + product.getStock() + ", 需要: " + order.getQuantity());
            }
            
            // 扣减库存
            int newStock = product.getStock() - order.getQuantity();
            product.setStock(newStock);
            
            // 更新订单状态
            order.setStatus("paid");
            
            log.info("库存扣减成功 - 商品: {}, 扣减数量: {}, 剩余库存: {}", 
                    product.getName(), order.getQuantity(), newStock);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("orderId", orderId);
        result.put("productId", product.getId());
        result.put("productName", product.getName());
        result.put("deductedQuantity", order.getQuantity());
        result.put("remainingStock", product.getStock());
        result.put("orderStatus", order.getStatus());
        
        return Result.OK(result);
    }
    
    /**
     * 查询订单详情
     *
     * @param orderId 订单ID
     * @return 订单详情
     * @author chenrui
     * @date 2025/11/6 14:30
     */
    @Operation(summary = "查询订单详情", description = "根据订单ID查询订单信息")
    @GetMapping("/order")
    public Result<Order> getOrder(
            @Parameter(description = "订单ID", required = true) @RequestParam String orderId) {
        
        log.info("查询订单 - 订单ID: {}", orderId);
        
        Order order = orderStore.get(orderId);
        if (order == null) {
            return Result.error("订单不存在: " + orderId);
        }
        
        return Result.OK(order);
    }
    
    /**
     * 获取所有商品分类
     *
     * @return 分类列表
     * @author chenrui
     * @date 2025/11/6 14:30
     */
    @Operation(summary = "获取商品分类", description = "获取所有商品的分类列表")
    @GetMapping("/categories")
    public Result<List<String>> getCategories() {
        
        Set<String> categories = productStore.values().stream()
                .map(Product::getCategory)
                .collect(Collectors.toSet());
        
        List<String> categoryList = new ArrayList<>(categories);
        categoryList.sort(String::compareTo);
        
        return Result.OK(categoryList);
    }
    
    /**
     * 重置所有数据(仅用于测试)
     *
     * @return 重置结果
     * @author chenrui
     * @date 2025/11/6 14:30
     */
    @Operation(summary = "重置数据", description = "清空所有订单并重置商品库存(仅用于测试)")
    @PostMapping("/reset")
    public Result<String> reset() {
        
        log.info("重置商品和订单数据");
        
        orderStore.clear();
        orderIdGenerator.set(1000);
        productStore.clear();
        initProducts();
        
        return Result.OK("数据重置成功");
    }
}
