/**
 *微应用apps
 * @name: 微应用名称 - 具有唯一性
 * @entry: 微应用入口.必选 - 通过该地址加载微应用，
 * @container: 微应用挂载节点 - 微应用加载完成后将挂载在该节点上
 * @activeRule: 微应用触发的路由规则 - 触发路由规则后将加载该微应用
 */
//子应用列表
const _apps = [];
for (const key in process.env) {
    if (key.includes('VUE_APP_SUB_')) {
        const name = key.split('VUE_APP_SUB_')[1];
        const obj = {
            name,
            entry: process.env[key],
            container: '#content',
            activeRule: name,
        };
        _apps.push(obj)
    }
}
export const apps = _apps;
