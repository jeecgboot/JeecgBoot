/**
 * qiankun配置
 */
import {registerMicroApps, setDefaultMountApp, start, runAfterFirstMounted, addGlobalUncaughtErrorHandler} from 'qiankun';
import {apps} from './apps';
import {getProps, initGlState} from './state';
/**
 * 重构apps
 */
function filterApps() {
    apps.forEach((item) => {
        //主应用需要传递给微应用的数据。
        item.props = getProps();
        //微应用触发的路由规则
        item.activeRule = genActiveRule('/' + item.activeRule);
    });
    return apps;
}

/**
 * 路由监听
 * @param {*} routerPrefix 前缀
 */
function genActiveRule(routerPrefix) {
    return location => location.pathname.startsWith(routerPrefix);
}

/**
 * 微应用注册
 */
function registerApps() {
    const _apps = filterApps();
    registerMicroApps(_apps,
        {
            beforeLoad: [
                loadApp => {
                    console.log('before load', loadApp);
                }
            ],
            beforeMount: [
                mountApp => {
                    console.log('before mount', mountApp);
                }
            ],
            afterMount: [
                mountApp => {
                    console.log('before mount', mountApp);
                }
            ],
            afterUnmount: [
                unloadApp => {
                    console.log('after unload', unloadApp);
                }
            ]
        });
    // 设置默认子应用,与 genActiveRule中的参数保持一致
    // setDefaultMountApp();
    // 第一个微应用 mount 后需要调用的方法，比如开启一些监控或者埋点脚本。
    runAfterFirstMounted(() => console.log('开启监控'));
    // 添加全局的未捕获异常处理器。
    addGlobalUncaughtErrorHandler(event => console.log(event));
    // 定义全局状态
    initGlState();
    //启动qiankun
    start({});
}

export default registerApps;
