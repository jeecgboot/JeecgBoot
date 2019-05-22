package org.jeecg.modules.demo.test.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.demo.test.entity.JeecgOrderCustomer;
import org.jeecg.modules.demo.test.entity.JeecgOrderMain;
import org.jeecg.modules.demo.test.entity.JeecgOrderTicket;
import org.jeecg.modules.demo.test.service.IJeecgOrderCustomerService;
import org.jeecg.modules.demo.test.service.IJeecgOrderMainService;
import org.jeecg.modules.demo.test.service.IJeecgOrderTicketService;
import org.jeecg.modules.demo.test.vo.JeecgOrderMainPage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * @Title: Controller
 * @Description: 订单模拟
 * @Author: ZhiLin
 * @Date: 2019-02-20
 * @Version: v1.0
 */
@Slf4j
@RestController
@RequestMapping("/test/order")
public class JeecgOrderDMainController {
    @Autowired
    private IJeecgOrderMainService jeecgOrderMainService;
    @Autowired
    private IJeecgOrderCustomerService jeecgOrderCustomerService;
    @Autowired
    private IJeecgOrderTicketService jeecgOrderTicketService;
    @Autowired
    private IJeecgOrderCustomerService customerService;
    @Autowired
    private IJeecgOrderTicketService ticketService;

    /**
     * 分页列表查询
     *
     * @param jeecgOrderMain
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/orderList")
    public Result<IPage<JeecgOrderMain>> respondePagedData(JeecgOrderMain jeecgOrderMain,
                                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                           HttpServletRequest req) {
        Result<IPage<JeecgOrderMain>> result = new Result<IPage<JeecgOrderMain>>();
        QueryWrapper<JeecgOrderMain> queryWrapper = QueryGenerator.initQueryWrapper(jeecgOrderMain, req.getParameterMap());
        Page<JeecgOrderMain> page = new Page<JeecgOrderMain>(pageNo, pageSize);
        IPage<JeecgOrderMain> pageList = jeecgOrderMainService.page(page, queryWrapper);
        //log.debug("查询当前页："+pageList.getCurrent());
        //log.debug("查询当前页数量："+pageList.getSize());
        //log.debug("查询结果数量："+pageList.getRecords().size());
        //log.debug("数据总数："+pageList.getTotal());
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param jeecgOrderMainPage
     * @return
     */
    @PostMapping(value = "/add")
    public Result<JeecgOrderMain> add(@RequestBody JeecgOrderMainPage jeecgOrderMainPage) {
        Result<JeecgOrderMain> result = new Result<JeecgOrderMain>();
        try {
            JeecgOrderMain jeecgOrderMain = new JeecgOrderMain();
            BeanUtils.copyProperties(jeecgOrderMainPage, jeecgOrderMain);
            jeecgOrderMainService.save(jeecgOrderMain);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑
     *
     * @param jeecgOrderMainPage
     * @return
     */
    @PutMapping("/edit")
    public Result<JeecgOrderMain> edit(@RequestBody JeecgOrderMainPage jeecgOrderMainPage) {
        Result<JeecgOrderMain> result = new Result<JeecgOrderMain>();
        JeecgOrderMain jeecgOrderMain = new JeecgOrderMain();
        BeanUtils.copyProperties(jeecgOrderMainPage, jeecgOrderMain);
        JeecgOrderMain jeecgOrderMainEntity = jeecgOrderMainService.getById(jeecgOrderMain.getId());
        if (jeecgOrderMainEntity == null) {
            result.error500("未找到对应实体");
        } else {
            jeecgOrderMainService.updateById(jeecgOrderMain);
            result.success("修改成功!");
        }

        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<JeecgOrderMain> delete(@RequestParam(name = "id", required = true) String id) {
        Result<JeecgOrderMain> result = new Result<JeecgOrderMain>();
        JeecgOrderMain jeecgOrderMain = jeecgOrderMainService.getById(id);
        if (jeecgOrderMain == null) {
            result.error500("未找到对应实体");
        } else {
            jeecgOrderMainService.delMain(id);
            result.success("删除成功!");
        }

        return result;
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<JeecgOrderMain> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<JeecgOrderMain> result = new Result<JeecgOrderMain>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.jeecgOrderMainService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<JeecgOrderMain> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<JeecgOrderMain> result = new Result<JeecgOrderMain>();
        JeecgOrderMain jeecgOrderMain = jeecgOrderMainService.getById(id);
        if (jeecgOrderMain == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(jeecgOrderMain);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 通过id查询
     *
     * @param mainId
     * @return
     */
    @GetMapping(value = "/listOrderCustomerByMainId")
    public Result<List<JeecgOrderCustomer>> queryOrderCustomerListByMainId(@RequestParam(name = "mainId", required = false) String mainId) {
        Result<List<JeecgOrderCustomer>> result = new Result<List<JeecgOrderCustomer>>();
        List<JeecgOrderCustomer> jeecgOrderCustomerList = null;
        if (mainId != null) {
            jeecgOrderCustomerList = jeecgOrderCustomerService.selectCustomersByMainId(mainId);
            result.setResult(jeecgOrderCustomerList);
            result.setSuccess(true);
            return result;
        } else {
			return null;
		}
    }

    /**
     * 通过id查询
     *
     * @param mainId
     * @return
     */
    @GetMapping(value = "/listOrderTicketByMainId")
    public Result<List<JeecgOrderTicket>> queryOrderTicketListByMainId(@RequestParam(name = "mainId", required = false) String mainId) {
        Result<List<JeecgOrderTicket>> result = new Result<List<JeecgOrderTicket>>();
        List<JeecgOrderTicket> jeecgOrderTicketList = null;
        if (mainId != null) {
            jeecgOrderTicketList = jeecgOrderTicketService.selectTicketsByMainId(mainId);
            result.setResult(jeecgOrderTicketList);
            result.setSuccess(true);
            return result;
        } else {
			return null;
		}
    }

// ================================以下是客户信息相关的API=================================

    /**
     * 添加
     *
     * @param jeecgOrderCustomer
     * @return
     */
    @PostMapping(value = "/addCustomer")
    public Result<JeecgOrderCustomer> addCustomer(@RequestBody JeecgOrderCustomer jeecgOrderCustomer) {
        Result<JeecgOrderCustomer> result = new Result<>();
        try {
            boolean ok = customerService.save(jeecgOrderCustomer);
            if (ok) {
                result.setSuccess(true);
                result.setMessage("添加数据成功");
            } else {
                result.setSuccess(false);
                result.setMessage("添加数据失败");
            }
            return result;
        } catch (Exception e) {
            e.fillInStackTrace();
            result.setSuccess(false);
            result.setMessage("遇到问题了!");
            return result;
        }

    }

    /**
     * 编辑
     *
     * @param jeecgOrderCustomer
     * @return
     */
    @PutMapping("/editCustomer")
    public Result<JeecgOrderCustomer> editCustomer(@RequestBody JeecgOrderCustomer jeecgOrderCustomer) {
        Result<JeecgOrderCustomer> result = new Result<>();
        try {
            boolean ok = customerService.updateById(jeecgOrderCustomer);
            if (ok) {
                result.setSuccess(true);
                result.setMessage("更新成功");
            } else {
                result.setSuccess(false);
                result.setMessage("更新失败");
            }
            return result;
        } catch (Exception e) {
            e.fillInStackTrace();
            result.setSuccess(true);
            result.setMessage("更新中碰到异常了");
            return result;
        }

    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/deleteCustomer")
    public Result<JeecgOrderCustomer> deleteCustomer(@RequestParam(name = "id", required = true) String id) {
        Result<JeecgOrderCustomer> result = new Result<>();
        try {
            boolean ok = customerService.removeById(id);
            if (ok) {
                result.setSuccess(true);
                result.setMessage("删除成功");
            } else {
                result.setSuccess(false);
                result.setMessage("删除失败");
            }
            return result;
        } catch (Exception e) {
            e.fillInStackTrace();
            result.setSuccess(false);
            result.setMessage("删除过程中碰到异常了");
            return result;
        }
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatchCustomer")
    public Result<JeecgOrderCustomer> deleteBatchCustomer(@RequestParam(name = "ids", required = true) String ids) {
        Result<JeecgOrderCustomer> result = new Result<JeecgOrderCustomer>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.customerService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }
//===========================以下是机票的相关API====================================	

    /**
     * 添加
     *
     * @param jeecgOrderTicket
     * @return
     */
    @PostMapping(value = "/addTicket")
    public Result<JeecgOrderTicket> addTicket(@RequestBody JeecgOrderTicket jeecgOrderTicket) {
        Result<JeecgOrderTicket> result = new Result<>();
        try {
            boolean ok = ticketService.save(jeecgOrderTicket);
            if (ok) {
                result.setSuccess(true);
                result.setMessage("添加机票信息成功.");
            } else {
                result.setSuccess(false);
                result.setMessage("添加机票信息失败!");
            }
            return result;
        } catch (Exception e) {
            e.fillInStackTrace();
            result.setSuccess(false);
            result.setMessage("添加机票信息过程中出现了异常: " + e.getMessage());
            return result;
        }

    }

    /**
     * 编辑
     *
     * @param jeecgOrderTicket
     * @return
     */
    @PutMapping("/editTicket")
    public Result<JeecgOrderTicket> editTicket(@RequestBody JeecgOrderTicket jeecgOrderTicket) {
        Result<JeecgOrderTicket> result = new Result<>();
        try {
            boolean ok = ticketService.updateById(jeecgOrderTicket);
            if (ok) {
                result.setSuccess(true);
                result.setMessage("更新数据成功.");
            } else {
                result.setSuccess(false);
                result.setMessage("更新机票 信息失败!");
            }
            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("更新数据过程中出现异常啦: " + e.getMessage());
            return result;
        }
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/deleteTicket")
    public Result<JeecgOrderTicket> deleteTicket(@RequestParam(name = "id", required = true) String id) {
        Result<JeecgOrderTicket> result = new Result<>();
        try {
            boolean ok = ticketService.removeById(id);
            if (ok) {
                result.setSuccess(true);
                result.setMessage("删除机票信息成功.");
            } else {
                result.setSuccess(false);
                result.setMessage("删除机票信息失败!");
            }
            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("删除机票信息过程中出现异常啦: " + e.getMessage());
            return result;
        }
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatchTicket")
    public Result<JeecgOrderTicket> deleteBatchTicket(@RequestParam(name = "ids", required = true) String ids) {
        Result<JeecgOrderTicket> result = new Result<JeecgOrderTicket>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.ticketService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

}