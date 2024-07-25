package com.shop.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.shop.common.core.utils.DateUtil;
import com.shop.common.core.web.BaseApiController;
import com.shop.entity.Orders;
import com.shop.service.OrdersService;
import com.shop.vo.OrdersVo;
import com.shop.entity.Classifys;
import com.shop.entity.Products;
import com.shop.service.ClassifysService;
import com.shop.service.ProductsService;
import com.shop.entity.ShopSettings;
import com.shop.service.ShopSettingsService;
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
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api")
public class SearchOrderController extends BaseApiController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ThemeService themeService;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private ClassifysService classifysService;

    @Autowired
    private ShopSettingsService shopSettingsService;

    @Autowired
    private WebsiteService websiteService;

    @RequestMapping("/order")
    public String Chat(Model model) {
        Long orderId = getLoginUserId(request);

        Orders member = ordersService.getById(orderId);

        /**
         * 商品没有找到
         */
        Theme theme = themeService.getOne(Wrappers.<Theme>lambdaQuery().eq(Theme::getEnable, 1));
        if (ObjectUtils.isEmpty(member)) return "theme/" + theme.getDriver() + "/error.html";

        Products products = productsService.getById(member.getProductId());
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

        Website website = websiteService.getById(1);
        model.addAttribute("website", website);

        model.addAttribute("cardsList", cardsList); // 订单
        model.addAttribute("orders", ordersVo); // 订单
        model.addAttribute("goods", products);  // 商品
        model.addAttribute("classify", classifys);  // 分类

        ShopSettings shopSettings = shopSettingsService.getById(1);
        model.addAttribute("isBackground", shopSettings.getIsBackground());
        model.addAttribute("shop", shopSettings);
        return "theme/" + theme.getDriver() + "/order.html";
    }

}
