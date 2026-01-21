package org.jeecg.modules.airag.llm.consts;

/**
* @Description: 流程插件常量
*
* @author: wangshuai
* @date: 2025/12/23 19:37
*/
public interface FlowPluginContent {
    /**
     * 名称
     */
    String NAME = "name";

    /**
     * 描述
     */
    String DESCRIPTION = "description";

    /**
     * 响应
     */
    String RESPONSES = "responses";

    /**
     * 类型
     */
    String TYPE = "type";

    /**
     * 参数
     */
    String PARAMETERS = "parameters";

    /**
     * 是否必须
     */
    String REQUIRED = "required";

    /**
     * 默认值
     */
    String DEFAULT_VALUE = "defaultValue";

    /**
     * 路径
     */
    String PATH = "path";

    /**
     * 方法
     */
    String METHOD = "method";

    /**
     * 位置
     */
    String LOCATION = "location";

    /**
     * 认证类型
     */
    String AUTH_TYPE = "authType";

    /**
     * token参数名称
     */
    String TOKEN_PARAM_NAME = "tokenParamName";

    /**
     * token参数值
     */
    String TOKEN_PARAM_VALUE = "tokenParamValue";

    /**
     * token
     */
    String TOKEN = "token";

    /**
     * Path位置
     */
    String LOCATION_PATH = "Path";

    /**
     * Header位置
     */
    String LOCATION_HEADER = "Header";

    /**
     * Query位置
     */
    String LOCATION_QUERY = "Query";

    /**
     * Body位置
     */
    String LOCATION_BODY = "Body";

    /**
     * Form-Data位置
     */
    String LOCATION_FORM_DATA = "Form-Data";

    /**
     * String类型
     */
    String TYPE_STRING = "String";

    /**
     * string类型
     */
    String TYPE_STRING_LOWER = "string";

    /**
     * Number类型
     */
    String TYPE_NUMBER = "Number";

    /**
     * number类型
     */
    String TYPE_NUMBER_LOWER = "number";

    /**
     * Integer类型
     */
    String TYPE_INTEGER = "Integer";

    /**
     * integer类型
     */
    String TYPE_INTEGER_LOWER = "integer";

    /**
     * Boolean类型
     */
    String TYPE_BOOLEAN = "Boolean";

    /**
     * boolean类型
     */
    String TYPE_BOOLEAN_LOWER = "boolean";

    /**
     * 工具数量
     */
    String TOOL_COUNT = "tool_count";

    /**
     * 是否启用
     */
    String ENABLED = "enabled";

    /**
     * 输入
     */
    String INPUTS = "inputs";

    /**
     * 输出
     */
    String OUTPUTS = "outputs";

    /**
     * POST请求
     */
    String POST = "POST";

    /**
     * token名称
     */
    String X_ACCESS_TOKEN = "X-Access-Token";

    /**
     * 插件名称
     */
    String PLUGIN_NAME = "流程调用";

    /**
     * 插件描述
     */
    String PLUGIN_DESC = "调用工作流";

    /**
     * 插件请求地址
     */
    String PLUGIN_REQUEST_URL = "/airag/flow/plugin/run/";

    /**
     * 记忆库插件名称
     */
    String PLUGIN_MEMORY_NAME = "记忆库";

    /**
     * 记忆库插件描述
     */
    String PLUGIN_MEMORY_DESC = "用于记录长期记忆";
    
    /**
     * 添加记忆路径
     */
    String PLUGIN_MEMORY_ADD_PATH = "/airag/knowledge/plugin/add";

    /**
     * 查询记忆路径
     */
    String PLUGIN_MEMORY_QUERY_PATH = "/airag/knowledge/plugin/query";
}
