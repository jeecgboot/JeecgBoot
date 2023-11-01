package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.util.Kv;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.entity.query_helper.QUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-18
 */
public interface UserService extends IService<User> {
    IPage<User> pagination(MyPage<User> page, QUser q);

    /**
     * 查找群组里的所有用户id
     */
    List<Integer> getIdsByMucId(Integer mucId);
    //批量查询
    List<User> getByIds(String userIds);

    /**
     * 根据id查询
     */
    User findById(Integer id);
    //查询密码
    String getPassword(Integer id);

    /**
     * 
     * @param id
     * @return
     */
    User findBaseById(Integer id);
    //关联查询信息
    User findByIdWithInfo(Integer id);

    /**
     * 获取用户的基本资料
     */
    Kv getBasicInfoById(Integer id);

    /**
     * 获取用户的基本资料，不包含个人设置
     */
    Kv getBasicInfoWithoutSettingById(Integer id);

    /**
     * 注册
     */
    Result<Object> register(QUser user, String verifyCode,String inviteCode);

    /**
     * 手机号重置密码
     */
    Result<Object> resetPwdByMobile(String mobile, String password, String verifyCode);


    //密保问题找回密码
    Result<Object> resetPwdBySecretQuestion(String account, String password, String questions);
    /**
     * 账号查询
     */
    User findByAccount(String account);

    /**
     * 用户名查询
     */
    User findByUsername(String username);

    /**
     * 手机号查询
     */
    User findByMobile(String mobile);

    User findByQrCode(String qrCode);

    /**
     * 登录，支持手机号、用户名、邮箱、账号
     */
    Result<Object> login(String account, String password);

    /**
     * 二次校验谷歌验证码
     */
    Result<Object> checkGoogleCode(String account,String googleCode);

    /**
     * 手机短信验证码登录
     */
    Result<Object> loginBySmsCode(String mobile, String verifyCode, String smsCodeType);

    /**
     * token登录
     */
    Result<Object> tokenLogin(Boolean isScan);

    /**
     * 更新资料
     * 昵称
     * 性别
     * 地区
     * 个性签名
     * 状态
     * 头像
     */
    Result<Object> updateInfo(QUser param);

    /**
     * 修改密码
     */
    Result<Object> updatePwd(Integer userId,String oldPwd,String newPwd);

    /**
     * 关键字查询
     * 手机号精确
     * 账号精确
     * 用户名精确
     * 昵称模糊
     */
    Result<Object> search(Integer userId,String keyword,Integer type);

    void handleLogin(Integer id, String resource) ;

    void closeConnection(Integer id, String resource, boolean userOffline) ;
    //注销
    Result<Object> logout();
    //控制台创建或更新用户
    Result<Object> consoleCreateOrUpdate(User user);
    //控制台踢下线
    Result<Object> consoleKickOut(Integer userId);
    //默认好友
    Result<Object> setDefaultFriend(Integer userId);
    //控制台禁言
    Result<Object> consoleMute(Integer userId,Long tsMute);
    //控制台锁定
    Result<Object> consoleLock(Integer userId,Long tsLocked);
    //控制台禁止连接聊天服务
    Result<Object> consoleNoConnect(Integer userId,Long tsNoConnect);
    //当前在线数
    Integer getOnlineCount();
    //所有真实用户
    Integer getTotalUser();
    //查询该昵称的用户数量
    Integer getCountOfNickname(String nickname);
    //设置支付密码
    Result<Object> setPayPassword(User user, String oldPwd, String pwd);
    //设置谷歌密钥
    Result<Object> setGoogleCode(User user, String code,Boolean enable);
    //获取系统用户
    List<User> findSysUser();

    List<User> findByTypes(List<Integer> types);
    //批量添加好友
    int batchAddFriend(Integer userId, List<User> users);
    //批量关注系统号
    int batchFollowSys(Integer userId, List<User> users);
}
