package com.shop.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.common.core.web.BaseController;
import com.shop.dto.HotProductDTO;
import com.shop.dto.ProductDTO;
import com.shop.dto.ProductListDTO;
import com.shop.dto.SearchDTO;
import com.shop.entity.Cards;
import com.shop.service.CardsService;
import com.shop.common.core.enmu.*;
import com.shop.common.core.utils.DateUtil;
import com.shop.common.core.utils.DeviceUtils;
import com.shop.common.core.web.JsonResult;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.User;
import com.shop.service.UserService;
import com.shop.entity.Article;
import com.shop.entity.Carousel;
import com.shop.service.ArticleService;
import com.shop.service.CarouselService;
import com.shop.entity.Orders;
import com.shop.service.OrdersService;
import com.shop.common.util.ProductUtil;
import com.shop.vo.ArticleVo;
import com.shop.vo.OrdersVo;
import com.shop.entity.Classifys;
import com.shop.entity.Products;
import com.shop.service.ClassifysService;
import com.shop.service.ProductsService;
import com.shop.vo.ClassifysVo;
import com.shop.vo.ProductsVos;
import com.shop.entity.Coupon;
import com.shop.entity.Pays;
import com.shop.entity.ShopSettings;
import com.shop.service.CouponService;
import com.shop.service.PaysService;
import com.shop.service.ShopSettingsService;
import com.shop.vo.PaysVo;
import com.shop.entity.Theme;
import com.shop.service.ThemeService;
import com.shop.entity.Website;
import com.shop.service.WebsiteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Controller
public class IndexController {

    @Autowired
    public HttpServletRequest request;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private ClassifysService classifysService;

    @Autowired
    private CardsService cardsService;

    @Autowired
    private PaysService paysService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private WebsiteService websiteService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private ThemeService themeService;

    @Autowired
    private ShopSettingsService shopSettingsService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private CarouselService carouselService;

    /**
     * 首页
     *
     * @param model
     * @return
     */
    @RequestMapping({"/", "/index"})
    public String IndexView(Model model) {
        Website website = websiteService.getById(1);
        model.addAttribute("website", website);
        ShopSettings shopSettings = shopSettingsService.getById(1);
        shopSettings.setWxpushUid(null);
        model.addAttribute("isBackground", shopSettings.getIsBackground());
        model.addAttribute("shopSettings", JSON.toJSONString(shopSettings));
        model.addAttribute("shop", shopSettings);
        Theme theme = themeService.getOne(Wrappers.<Theme>lambdaQuery().eq(Theme::getEnable, 1));

        /**
         * 轮播图
         */
        List<Carousel> carouselList = carouselService.list(Wrappers.<Carousel>lambdaQuery().eq(Carousel::getEnabled, 1));
        model.addAttribute("carouselList", carouselList);

        /**
         * 分类列表
         * 查询出所有上架的分类
         */
        List<Classifys> classifysList = classifysService.list(Wrappers.<Classifys>lambdaQuery().eq(Classifys::getStatus, 1).orderByAsc(Classifys::getSort));
        AtomicInteger index = new AtomicInteger(0); // 索引
        List<ClassifysVo> classifysVoList = classifysList.stream().map((classifys) -> {
            ClassifysVo classifysVo = new ClassifysVo();
            BeanUtils.copyProperties(classifys, classifysVo);
            long count = productsService.count(Wrappers.<Products>lambdaQuery().eq(Products::getClassifyId, classifys.getId()).eq(Products::getStatus, 1));
            classifysVo.setProductsMember(count);
            int andIncrement = index.getAndIncrement();
            classifysVo.setAndIncrement(andIncrement); // 索引
            return classifysVo;
        }).collect(Collectors.toList());

        if (shopSettings.getIsModel() == 1) {
            model.addAttribute("classifysListJson", JSON.toJSONString(classifysVoList));
            return "theme/" + theme.getDriver() + "/index.html";
        } else if (shopSettings.getIsModel() == 0) {
            /**
             * 分类与分类下的商品
             */
            List<ProductListDTO> productListDTOList = classifysList.stream().map((classifys -> {
                ProductListDTO productListDTO = new ProductListDTO();
                productListDTO.setId(classifys.getId());
                productListDTO.setTitle(classifys.getName());
                productListDTO.setCreateTime(DateUtil.getSubDateMiao(classifys.getCreatedAt()));
                productListDTO.setUpdateTime(DateUtil.getSubDateMiao(classifys.getUpdatedAt()));
                long productCount = productsService.count(Wrappers.<Products>lambdaQuery().eq(Products::getClassifyId, classifys.getId()).eq(Products::getStatus, 1));
                productListDTO.setProductNum(productCount);
                List<Products> productsList = productsService.list(Wrappers.<Products>lambdaQuery()
                        .eq(Products::getClassifyId, classifys.getId())
                        .eq(Products::getStatus, 1)
                        .orderByAsc(Products::getSort));
                List<ProductDTO> productDTOS = getProductDTOList(productsList);
                productListDTO.setProductDTOList(productDTOS);
                return productListDTO;
            })).collect(Collectors.toList());
            model.addAttribute("productListDTOList", productListDTOList);
            return "theme/" + theme.getDriver() + "/index-list.html";
        } else {
            /**
             * 查出四个热门商品
             */
            List<Products> randomProductList = productsService.getRandomProductList(4);
            List<ProductDTO> productDTOList = getProductDTOList(randomProductList);
            List<HotProductDTO> hotProductList = ProductUtil.getHotProductList(productDTOList);
            model.addAttribute("hotProductList", hotProductList);
            model.addAttribute("classifysListJson", JSON.toJSONString(classifysVoList));
            return "theme/" + theme.getDriver() + "/index-easy.html";
        }
    }

    @RequestMapping("/article")
    public String articleView(Model model) {
        /**
         * 查出四个热门商品
         */
        List<Products> randomProductList = productsService.getRandomProductList(4);
        List<ProductDTO> productDTOList = getProductDTOList(randomProductList);
        List<HotProductDTO> hotProductList = ProductUtil.getHotProductList(productDTOList);
        model.addAttribute("hotProductList", hotProductList);

        Website website = websiteService.getById(1);
        model.addAttribute("website", website);

        ShopSettings shopSettings = shopSettingsService.getById(1);
        model.addAttribute("isBackground", shopSettings.getIsBackground());
        model.addAttribute("shop", shopSettings);
        Theme theme = themeService.getOne(Wrappers.<Theme>lambdaQuery().eq(Theme::getEnable, 1));
        return "theme/" + theme.getDriver() + "/article.html";
    }

    /**
     * 文章内页
     *
     * @param model
     * @return
     */
    @RequestMapping("/article/{id}")
    public String articleContentView(Model model, @PathVariable("id") Integer id) {
        Article article = articleService.getById(id);
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setCreateTime(DateUtil.getSubDate(article.getCreateTime()));
        articleVo.setUpdateTime(DateUtil.getSubDate(article.getUpdateTime()));
        model.addAttribute("article", articleVo);

        /**
         * 每次点击自动统计加一
         */
        Article article1 = new Article();
        article1.setId(article.getId());
        article1.setSeeNumber(article.getSeeNumber() + 1);

        articleService.updateById(article1);

        /**
         * 查出四个热门商品
         */
        List<Products> randomProductList = productsService.getRandomProductList(4);
        List<ProductDTO> productDTOList = getProductDTOList(randomProductList);
        List<HotProductDTO> hotProductList = ProductUtil.getHotProductList(productDTOList);
        model.addAttribute("hotProductList", hotProductList);

        Website website = websiteService.getById(1);
        model.addAttribute("website", website);

        ShopSettings shopSettings = shopSettingsService.getById(1);
        model.addAttribute("isBackground", shopSettings.getIsBackground());
        model.addAttribute("shop", shopSettings);
        Theme theme = themeService.getOne(Wrappers.<Theme>lambdaQuery().eq(Theme::getEnable, 1));
        return "theme/" + theme.getDriver() + "/article-content.html";
    }

    /**
     * 获取文章列表
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getArticleList")
    public PageResult<ArticleVo> getArticleList() {
        PageParam<Article> pageParam = new PageParam<>(request);
        pageParam.remove("username");
        pageParam.put("enabled", 1);
        List<Article> articleList = articleService.page(pageParam, pageParam.getWrapper()).getRecords();
        List<ArticleVo> articleVoList = articleList.stream().map((article) -> {
            ArticleVo articleVo = new ArticleVo();
            BeanUtils.copyProperties(article, articleVo);
            User user = userService.getOne(new QueryWrapper<User>().eq("username", article.getUsername()));
            articleVo.setUserName(user.getNickName());
            articleVo.setUserHead(user.getAvatar());
            articleVo.setCreateTime(DateUtil.getSubDate(article.getCreateTime()));
            articleVo.setUpdateTime(DateUtil.getSubDate(article.getUpdateTime()));
            return articleVo;
        }).collect(Collectors.toList());
        return new PageResult<>(articleVoList, pageParam.getTotal());
    }

    @ResponseBody
    @RequestMapping("/getProductList")
    public PageResult<ProductDTO> getProductList(Integer page, Integer limit, Integer classifyId, String name) {
        if (ObjectUtils.isEmpty(classifyId)) return new PageResult<>(null, 0).setCode(1001).setMsg("商品内容为空");
        IPage<Products> productIPage = new Page<>(page, limit);
        IPage<Products> productIPageList = productsService.page(productIPage, Wrappers.<Products>lambdaQuery()
                .eq(Products::getStatus, 1)
                .eq(Products::getClassifyId, classifyId)
                .like(!StringUtils.isEmpty(name), Products::getName, name)
                .orderByAsc(Products::getSort));
        List<ProductDTO> productDTOList = getProductDTOList(productIPageList.getRecords());
        if (ObjectUtils.isEmpty(productDTOList) && productIPage.getTotal() == 0)
            return new PageResult<>(productDTOList, productIPageList.getTotal()).setCode(1000);
        return new PageResult<>(productDTOList, productIPageList.getTotal());
    }

    /**
     * 商品购买页面
     *
     * @param model
     * @param link
     * @return
     */
    @RequestMapping("/product/{link}")
    public String product(Model model, @PathVariable("link") String link) {
        Website website = websiteService.getById(1);
        model.addAttribute("website", website);
        ShopSettings shopSettings = shopSettingsService.getById(1);
        model.addAttribute("isBackground", shopSettings.getIsBackground());
        model.addAttribute("shop", shopSettings);
        Theme theme = themeService.getOne(Wrappers.<Theme>lambdaQuery().eq(Theme::getEnable, 1));

        // 查出商品
        Products products = productsService.getOne(new QueryWrapper<Products>().eq("link", link));

        /**
         * 商品为空
         * 直接返回商品找不到页面
         */
        if (ObjectUtils.isEmpty(products) || products.getStatus() == 0) {
            return "theme/" + theme.getDriver() + "/product404.html";
        }

        // 查出分类
        Classifys classifys = classifysService.getById(products.getClassifyId());

        boolean isMobile = DeviceUtils.isMobileDevice(request);
        AtomicInteger index = new AtomicInteger(0);
        if (isMobile) {
            List<PaysVo> paysVoList = getPaysVoList(paysService.list(new QueryWrapper<Pays>().eq("is_mobile", 1)), index);
            model.addAttribute("paysList", paysVoList);
        } else {
            List<PaysVo> paysVoList = getPaysVoList(paysService.list(new QueryWrapper<Pays>().eq("is_pc", 1)), index);
            model.addAttribute("paysList", paysVoList);
        }

        model.addAttribute("products", products);
        model.addAttribute("productsJson", JSON.toJSONString(getProductById(products)));
        model.addAttribute("classifyName", classifys.getName());

        /**
         * 批发参数遍历
         */
        if (products.getIsWholesale() == 1) {
            String wholesale = products.getWholesale();
            String[] wholesales = wholesale.split("\\n");
            List<Map<String, String>> list = new ArrayList<>();
            AtomicInteger atomicInteger = new AtomicInteger(0);
            for (String s : wholesales) {
                String[] split = s.split("=");
                Map<String, String> map = new HashMap<>();
                Integer andIncrement = atomicInteger.getAndIncrement();
                map.put("id", andIncrement.toString());
                map.put("number", split[0]);
                map.put("money", split[1]);
                list.add(map);
            }
            model.addAttribute("wholesaleList", list);
        }

        /**
         * 该商品是否有优惠券
         */
        long isCoupon = couponService.count(new QueryWrapper<Coupon>().eq("product_id", products.getId()));
        model.addAttribute("isCoupon", isCoupon);

        /**
         * 是否启用自定义输入框
         */
        Integer isCustomize = products.getIsCustomize();
        model.addAttribute("isCustomize", isCustomize);
        if (isCustomize == 1) {
            if (!StringUtils.isEmpty(products.getCustomizeInput())) {
                String customizeInput = products.getCustomizeInput();
                String[] customize = customizeInput.split("\\n");
                List<Map<String, String>> list = new ArrayList<>();
                for (String s : customize) {
                    String[] split = s.split("=");
                    Map<String, String> map = new HashMap<>();
                    map.put("field", split[0]);
                    map.put("name", split[1]);
                    map.put("switch", split[2]);
                    list.add(map);
                }
                model.addAttribute("customizeList", list);
            }
        }

        if (products.getShipType() == 0) { // 自动发货模式
            Integer count = getCardListCount(cardsService, products); // 计算卡密使用情况
            model.addAttribute("cardCount", count);
            if (products.getSellType() == 1) {
                Cards cards = cardsService.getOne(new QueryWrapper<Cards>().eq("product_id", products.getId()).eq("status", 0).eq("sell_type", 1));
                if (!ObjectUtils.isEmpty(cards)) {
                    model.addAttribute("cardCount", cards.getNumber());
                } else {
                    model.addAttribute("cardCount", 0);
                }
            }
        } else { // 手动发货模式
            model.addAttribute("cardCount", products.getInventory());
        }

        return "theme/" + theme.getDriver() + "/product.html";
    }

    public List<PaysVo> getPaysVoList(List<Pays> paysList, AtomicInteger index) {
        List<PaysVo> paysVoList = paysList.stream().map((pays) -> {
            PaysVo paysVo = new PaysVo();
            BeanUtils.copyProperties(pays, paysVo);
            int andIncrement = index.getAndIncrement();
            paysVo.setAndIncrement(andIncrement); // 索引
            return paysVo;
        }).collect(Collectors.toList());
        return paysVoList;
    }

    public ProductsVos getProductById(Products products) {
        ProductsVos productsVos = new ProductsVos();
        BeanUtils.copyProperties(products, productsVos);
        productsVos.setId(products.getId());
        productsVos.setPrice(products.getPrice().toString());

        if (products.getShipType() == 0) { // 自动发货模式
            Integer count = getCardListCount(cardsService, products); // 计算卡密使用情况
            productsVos.setCardsCount(count.toString());
        } else { // 手动发货模式
            productsVos.setCardsCount(products.getInventory().toString());
        }

        if (products.getSellType() == 1) {
            Cards cards = cardsService.getOne(new QueryWrapper<Cards>().eq("product_id", products.getId()).eq("status", 0).eq("sell_type", 1));
            if (!ObjectUtils.isEmpty(cards)) {
                productsVos.setCardsCount(cards.getNumber().toString());
            } else {
                productsVos.setCardsCount("0");
            }
        }
        return productsVos;
    }

    /**
     * 计算卡密使用情况
     *
     * @param cardsService
     * @param products
     * @return
     */
    public static Integer getCardListCount(CardsService cardsService, Products products) {
        List<Cards> cardsList = cardsService.list(new QueryWrapper<Cards>().eq("product_id", products.getId()).eq("sell_type", 0));
        Integer count = 0;
        for (Cards cards : cardsList) {
            if (cards.getStatus() == 0) {
                count++;
            }
        }
        return count;
    }

    @RequestMapping("/search")
    public String search(Model model, @CookieValue(name = "BROWSER_ORDERS_CACHE", required = false) String orderCache) {
        if (!ObjectUtils.isEmpty(orderCache)) {
            String[] split = orderCache.split("=");
            List<SearchDTO> ordersList = new ArrayList<>();
            AtomicInteger index = new AtomicInteger(0);
            for (String s : split) {
                Orders member = ordersService.getOne(new QueryWrapper<Orders>().eq("member", s));
                if (ObjectUtils.isEmpty(member)) continue;
                SearchDTO searchDTO = new SearchDTO();
                searchDTO.setId(member.getId());
                Integer andIncrement = index.getAndIncrement();
                searchDTO.setAndIncrement(andIncrement);
                searchDTO.setMember(member.getMember());
                searchDTO.setCreateTime(DateUtil.getSubDateMiao(member.getCreateTime()));
                searchDTO.setMoney(member.getMoney().toString());
                if (Alipay.getByValue(member.getPayType())) {
                    searchDTO.setPayType("支付宝");
                } else if (Wxpay.getByValue(member.getPayType())) {
                    searchDTO.setPayType("微信");
                } else if (Paypal.getByValue(member.getPayType())) {
                    searchDTO.setPayType("Paypal");
                } else if (QQPay.getByValue(member.getPayType())) {
                    searchDTO.setPayType("QQ钱包");
                } else if (USDT.getByValue(member.getPayType())) {
                    searchDTO.setPayType("USDT");
                }
                switch (member.getStatus()) {
                    case 1:
                        searchDTO.setStatus("已支付");
                        break;
                    case 2:
                        searchDTO.setStatus("待发货");
                        break;
                    case 3:
                        searchDTO.setStatus("已发货");
                        break;
                    default:
                        searchDTO.setStatus("未付款");
                        break;
                }
                ordersList.add(searchDTO);
            }
            model.addAttribute("ordersList", JSON.toJSONString(ordersList));
        } else {
            List<SearchDTO> ordersList = new ArrayList<>();
            model.addAttribute("ordersList", JSON.toJSONString(ordersList));
        }

        Website website = websiteService.getById(1);
        model.addAttribute("website", website);
        ShopSettings shopSettings = shopSettingsService.getById(1);
        model.addAttribute("isBackground", shopSettings.getIsBackground());
        model.addAttribute("shop", shopSettings);
        Theme theme = themeService.getOne(Wrappers.<Theme>lambdaQuery().eq(Theme::getEnable, 1));
        return "theme/" + theme.getDriver() + "/search.html";
    }

    @RequestMapping("/search/order/{order}")
    public String searchOrder(Model model, @PathVariable("order") String order) {
        Orders member = ordersService.getOne(Wrappers.<Orders>lambdaQuery().eq(Orders::getMember, order));
        Products products = productsService.getById(member.getProductId());

        Website website = websiteService.getById(1);
        model.addAttribute("website", website);
        ShopSettings shopSettings = shopSettingsService.getById(1);
        model.addAttribute("isBackground", shopSettings.getIsBackground());
        model.addAttribute("shop", shopSettings);
        model.addAttribute("orderId", member.getId());
        model.addAttribute("member", member.getMember());
        Theme theme = themeService.getOne(Wrappers.<Theme>lambdaQuery().eq(Theme::getEnable, 1));

        if (!StringUtils.isEmpty(products.getIsPassword())) {
            if (products.getIsPassword() == 1) {
                return "theme/" + theme.getDriver() + "/orderPass.html";
            }
        }
        Classifys classifys = classifysService.getById(products.getClassifyId());

        List<String> cardsList = new ArrayList<>();
        if (!StringUtils.isEmpty(member.getCardsInfo())) {
            String[] cardsInfo = member.getCardsInfo().split("\n");
            for (String cardInfo : cardsInfo) {
                StringBuilder cardInfoText = new StringBuilder();
                if (products.getShipType() == 0) {
                    cardInfoText.append(cardInfo).append("\n");
                    cardsList.add(cardInfoText.toString());
                } else {
                    cardInfoText.append(cardInfo);
                    cardsList.add(cardInfoText.toString());
                }
            }
        }

        OrdersVo ordersVo = new OrdersVo();
        BeanUtils.copyProperties(member, ordersVo);
        if (member.getPayTime() != null) {
            ordersVo.setPayTime(DateUtil.getSubDateMiao(member.getPayTime()));
        } else {
            ordersVo.setPayTime(null);
        }
        ordersVo.setMoney(member.getMoney().toString());
        /**
         * 发货模式
         */
        ordersVo.setShipType(products.getShipType());
        model.addAttribute("cardsList", cardsList); // 订单
        model.addAttribute("orders", ordersVo); // 订单
        model.addAttribute("goods", products);  // 商品
        model.addAttribute("classify", classifys);  // 分类
        return "theme/" + theme.getDriver() + "/order.html";
    }

    /**
     * 支付状态
     *
     * @param model
     * @return
     */
    @RequestMapping("/pay/state/{payId}")
    public String payState(Model model, @PathVariable("payId") String payId) {
        Orders orders = ordersService.getOne(new QueryWrapper<Orders>().eq("member", payId));
        model.addAttribute("orderId", orders.getId());
        model.addAttribute("ordersMember", orders.getMember());

        Website website = websiteService.getById(1);
        model.addAttribute("website", website);

        ShopSettings shopSettings = shopSettingsService.getById(1);
        model.addAttribute("isBackground", shopSettings.getIsBackground());
        model.addAttribute("shop", shopSettings);
        Theme theme = themeService.getOne(Wrappers.<Theme>lambdaQuery().eq(Theme::getEnable, 1));
        return "theme/" + theme.getDriver() + "/payState.html";
    }

    @ResponseBody
    @GetMapping("/getProductSearchList")
    public JsonResult getProductSearchList(String content) {
        List<Products> productsList = productsService.list(Wrappers.<Products>lambdaQuery().eq(Products::getStatus, 1).like(Products::getName, content));
        List<ProductDTO> productDTOList = getProductDTOList(productsList);
        return JsonResult.ok("查询成功！").setData(productDTOList);
    }

    /**
     * 获取购物须知
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/getShoppingNotes")
    public JsonResult getShoppingNotes() {
        ShopSettings shopSettings = shopSettingsService.getById(1);
        return JsonResult.ok().setData(shopSettings.getWindowText());
    }

    /* 拼接字符串
     * @author
     * @param
     * @return
     */
    @RequestMapping("/exportCards")
    public void exportCardsList(HttpServletResponse response, Integer orderId) {
        Orders orders = ordersService.getById(orderId);
        Products products = productsService.getById(orders.getProductId());
        StringBuffer text = new StringBuffer();
        if (!ObjectUtils.isEmpty(orders)) {
            String[] split = orders.getCardsInfo().split("\n");
            for (String s : split) {
                text.append(s).append("\n");
            }
        }
        exportTxt(response, products.getName() + "-" + orders.getMember(), text.toString());
    }

    /*
     * 导出txt文件
     * @author  Panyoujie
     * @param	response
     * @param	text 导出的字符串
     * @return
     */
    public void exportTxt(HttpServletResponse response, String fileName, String text) {
        response.setCharacterEncoding("utf-8");
        // 设置响应的内容类型
        response.setContentType("text/plain");
        // 设置文件的名称和格式
        response.addHeader("Content-Disposition", "attachment;filename="
                + genAttachmentFileName(fileName, "JSON_FOR_UCC_") //设置名称格式，没有这个中文名称无法显示
                + ".txt");
        BufferedOutputStream buff = null;
        ServletOutputStream outStr = null;
        try {
            outStr = response.getOutputStream();
            buff = new BufferedOutputStream(outStr);
            buff.write(text.getBytes(StandardCharsets.UTF_8));
            buff.flush();
            buff.close();
        } catch (Exception e) {
            //LOGGER.error("导出文件文件出错:{}",e);
        } finally {
            try {
                buff.close();
                outStr.close();
            } catch (Exception e) {
                //LOGGER.error("关闭流对象出错 e:{}",e);
            }
        }
    }

    /**
     * 原文说这个方法能解决文件中文名乱码问题，但是我实际试了以后中文的文件名依然乱码（文件内容中的中文能正常显示），不知道为什么
     * 最后是由前端生成的中文名
     *
     * @param cnName
     * @param defaultName
     * @return
     */
    public String genAttachmentFileName(String cnName, String defaultName) {
        try {
            cnName = new String(cnName.getBytes("gb2312"), "ISO8859-1");
        } catch (Exception e) {
            cnName = defaultName;
        }
        return cnName;
    }

    /**
     * 通用获取商品列表
     * 统计商品的卡密使用信息
     *
     * @param productsList
     * @return
     */
    public List<ProductDTO> getProductDTOList(List<Products> productsList) {
        List<ProductDTO> productDTOList = productsList.stream().map((products) -> {
            ProductDTO productDTO = new ProductDTO();
            BeanUtils.copyProperties(products, productDTO);
            long count = cardsService.count(new QueryWrapper<Cards>().eq("product_id", products.getId()).eq("status", 0).eq("sell_type", 0));
            productDTO.setCardMember(count);
            long count2 = cardsService.count(new QueryWrapper<Cards>().eq("product_id", products.getId()).eq("status", 1).eq("sell_type", 0));
            productDTO.setSellCardMember(count2);
            productDTO.setPrice(products.getPrice().toString());
            long count1 = couponService.count(new QueryWrapper<Coupon>().eq("product_id", products.getId()));
            productDTO.setIsCoupon(count1);
            if (products.getShipType() == 1) {
                productDTO.setCardMember(products.getInventory().longValue());
                productDTO.setSellCardMember(products.getSales().longValue());
            }
            if (products.getSellType() == 1) {
                Cards cards = cardsService.getOne(new QueryWrapper<Cards>().eq("product_id", products.getId()).eq("sell_type", 1));
                if (ObjectUtils.isEmpty(cards)) { // kon
                    productDTO.setCardMember(0L);
                    productDTO.setSellCardMember(0L);
                } else {
                    productDTO.setCardMember(cards.getNumber().longValue());
                    productDTO.setSellCardMember(cards.getSellNumber().longValue());
                }
            }
            return productDTO;
        }).collect(Collectors.toList());
        return productDTOList;
    }
}
