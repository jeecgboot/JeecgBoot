package com.shop.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.dto.CardsDto;
import com.shop.entity.Cards;
import com.shop.common.excel.Excel;
import com.shop.common.excel.ExcelSxssFUtil;
import com.shop.common.excel.ExcelType;
import com.shop.mapper.CardsMapper;
import com.shop.service.CardsService;
import com.shop.vo.CardsDts;
import com.shop.common.core.utils.CoreUtil;
import com.shop.common.core.web.JsonResult;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.Products;
import com.shop.mapper.ProductsMapper;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 卡密服务实现类
 * 2021-03-28 00:33:15
 */
@Service
@Transactional
public class CardsServiceImpl extends ServiceImpl<CardsMapper, Cards> implements CardsService {

    @Autowired
    private ProductsMapper productsMapper;

    @Override
    public PageResult<Cards> listPage(PageParam<Cards> page) {
        List<Cards> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    @Override
    public List<Cards> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

    @Override
    public JsonResult addCards(CardsDts cardsDts) {
        if (cardsDts.getSellType() == 0) { // 单卡销售
            String[] cardsInfo = cardsDts.getCardInfo().split("\\n");

            List<String> newlist = new ArrayList();
            for (int i = 0; i < cardsInfo.length; ++i) {
                if (cardsDts.getRepeat() == 0) {
                    newlist.add(CoreUtil.getStringNoBlank(cardsInfo[i]));
                } else if (cardsDts.getRepeat() == 1 && !newlist.contains(cardsInfo[i])) {
                    newlist.add(CoreUtil.getStringNoBlank(cardsInfo[i]));
                }
            }

            while (newlist.remove(null)) ;
            while (newlist.remove("")) ;

            List<Cards> cardsArrayList = new ArrayList<>();
            for (String cardInfo : newlist) {
                Cards cards = new Cards();
                cards.setProductId(cardsDts.getProductId());
                cards.setCardInfo(CoreUtil.getStringNoBlank(cardInfo));
                cards.setStatus(0); // 设置未出售
                cards.setSellType(0);
                cards.setNumber(1);
                cards.setSellNumber(0);
                cards.setCreatedAt(new Date());
                cards.setUpdatedAt(new Date());
                cards.setUsername(cardsDts.getUsername());
                cardsArrayList.add(cards);
            }

            boolean batch = this.saveBatch(cardsArrayList);
            if (batch) {
                return JsonResult.ok("批量添加卡密成功！");
            }
            return JsonResult.error("添加卡密失败");
        } else { // 重复销售

            Cards cards1 = this.getOne(new QueryWrapper<Cards>().eq("product_id", cardsDts.getProductId()).eq("status", 0).eq("sell_type", 1));
            if (!ObjectUtils.isEmpty(cards1)) {
                return JsonResult.error("当前商品为重复销售类型、已存在一个重复销售的卡密、请勿重复添加，如需修改当前卡密数量请前往卡密管理进行操作。");
            }

            Cards cards = new Cards();
            cards.setProductId(cardsDts.getProductId());
            cards.setCardInfo(CoreUtil.getStringNoBlank(cardsDts.getCardInfo()));
            cards.setStatus(0); // 设置未出售
            cards.setSellType(1);
            cards.setNumber(cardsDts.getSellNumber());
            cards.setSellNumber(0);
            cards.setCreatedAt(new Date());
            cards.setUpdatedAt(new Date());
            cards.setUsername(cardsDts.getUsername());
            boolean save = this.save(cards);
            if (save) {
                return JsonResult.ok("重复销售卡密添加成功！");
            }
            return JsonResult.error("重复销售的卡密添加失败。");
        }
    }

    @Override
    public List<Cards> getCard(Integer status, Integer productId, Integer number) {
        return baseMapper.getCard(status, productId, number);
    }

    @Override
    public void export(HttpServletRequest request) {
        PageParam<Cards> pageParam = new PageParam<>(request);
        List<Cards> cardsList = this.list(pageParam.getOrderWrapper());
        List<CardsDto> cardsDtoList = cardsList.stream().map((cards -> {
            CardsDto cardsDto = new CardsDto();
            BeanUtils.copyProperties(cards, cardsDto);
            // TODO 导出时间这里需要抓换String，否则导出数据异常，可优化
            cardsDto.setCreatedAt(DateUtil.formatDateTime(cards.getCreatedAt()));
            if (cards.getStatus() == 1) {
                cardsDto.setStatus("已出售");
            } else {
                cardsDto.setStatus("未出售");
            }
            Products products = productsMapper.selectById(cards.getProductId());
            if (!ObjectUtils.isEmpty(products)) {
                cardsDto.setProductName(products.getName());
            } else {
                cardsDto.setProductName("商品已删除");
            }
            return cardsDto;
        })).collect(Collectors.toList());

        Excel excel = CardsDto.class.getAnnotation(Excel.class);
        List<Field> fields = ExcelSxssFUtil.getExcelList(CardsDto.class, ExcelType.EXPORT);
        String sheetTitle = excel.value();
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        ExcelSxssFUtil.exportExcel(fields, workbook, cardsDtoList, sheetTitle);
        ExcelSxssFUtil.download(workbook, sheetTitle);
    }

}
