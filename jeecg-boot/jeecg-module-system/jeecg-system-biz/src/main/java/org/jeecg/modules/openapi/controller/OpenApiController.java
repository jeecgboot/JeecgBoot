package org.jeecg.modules.openapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.modules.openapi.entity.OpenApi;
import org.jeecg.modules.openapi.entity.OpenApiAuth;
import org.jeecg.modules.openapi.entity.OpenApiHeader;
import org.jeecg.modules.openapi.entity.OpenApiParam;
import org.jeecg.modules.openapi.generator.PathGenerator;
import org.jeecg.modules.openapi.service.OpenApiAuthService;
import org.jeecg.modules.openapi.service.OpenApiHeaderService;
import org.jeecg.modules.openapi.service.OpenApiParamService;
import org.jeecg.modules.openapi.service.OpenApiService;
import org.jeecg.modules.openapi.swagger.*;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @date 2024/12/10 9:11
 */
@RestController
@RequestMapping("/openapi")
public class OpenApiController extends JeecgController<OpenApi, OpenApiService> {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private OpenApiParamService openApiParamService;
    @Autowired
    private OpenApiHeaderService openApiHeaderService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private OpenApiAuthService openApiAuthService;

    /**
     * 分页列表查询
     *
     * @param openApi
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(OpenApi openApi, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<OpenApi> queryWrapper = QueryGenerator.initQueryWrapper(openApi, req.getParameterMap());
        Page<OpenApi> page = new Page<>(pageNo, pageSize);
        IPage<OpenApi> pageList = service.page(page, queryWrapper);
        for (OpenApi api : pageList.getRecords()) {
            api.setParams(openApiParamService.findByApiId(api.getId()));
            api.setHeaders(openApiHeaderService.findByApiId(api.getId()));
        }
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param openApi
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OpenApi openApi) {
        if (service.save(openApi)) {
            if (!CollectionUtils.isEmpty(openApi.getHeaders())) {
                openApiHeaderService.saveBatch(openApi.getHeaders());
            }

            if (!CollectionUtils.isEmpty(openApi.getParams())) {
                openApiParamService.saveBatch(openApi.getParams());
            }
        }

        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param openApi
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody OpenApi openApi) {
        if (service.updateById(openApi)) {
            openApiHeaderService.deleteByApiId(openApi.getId());
            openApiParamService.deleteByApiId(openApi.getId());

            if (!CollectionUtils.isEmpty(openApi.getHeaders())) {
                openApiHeaderService.saveBatch(openApi.getHeaders());
            }

            if (!CollectionUtils.isEmpty(openApi.getParams())) {
                openApiParamService.saveBatch(openApi.getParams());
            }
        }
        return Result.ok("修改成功!");

    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        service.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {

        this.service.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        OpenApi OpenApi = service.getById(id);
        return Result.ok(OpenApi);
    }

    /**
     * 接口调用
     * @param path
     * @return
     */
    @RequestMapping(value = "/call/{path}", method = {RequestMethod.GET,RequestMethod.POST})
    public Result<?> call(@PathVariable String path, @RequestBody(required = false) String json, HttpServletRequest request) {
        OpenApi openApi = service.findByPath(path);
        if (Objects.isNull(openApi)) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 404);
            result.put("data", null);
            return Result.error("失败", result);
        }
        List<OpenApiHeader> headers = openApiHeaderService.findByApiId(openApi.getId());

        String url = openApi.getOriginUrl();
        String method = openApi.getRequestMethod();

        HttpHeaders httpHeaders = new HttpHeaders();
        for (OpenApiHeader header : headers) {
            httpHeaders.put(header.getHeaderKey(), Lists.newArrayList(request.getHeader(header.getHeaderKey())));
        }

        String appkey = request.getHeader("appkey");
        OpenApiAuth openApiAuth = openApiAuthService.getByAppkey(appkey);
        //TODO 使用创建用户生成token签名，这样无需关联用户
        SysUser systemUser = sysUserService.getById(openApiAuth.getCreateBy());
        String token = JwtUtil.sign(systemUser.getUsername(), systemUser.getPassword());
        httpHeaders.put("X-Access-Token", Lists.newArrayList(token));

        HttpEntity<String> httpEntity = new HttpEntity<>(json, httpHeaders);

        return restTemplate.exchange(url, HttpMethod.resolve(method), httpEntity, Result.class, request.getParameterMap()).getBody();
    }

    @GetMapping("/json")
    public SwaggerModel swaggerModel() {

        SwaggerModel swaggerModel = new SwaggerModel();
        swaggerModel.setSwagger("2.0");
        swaggerModel.setInfo(swaggerInfo());
        swaggerModel.setHost("jeecg.com");
        swaggerModel.setBasePath("/jeecg-boot");
        swaggerModel.setSchemes(Lists.newArrayList("http", "https"));

        SwaggerTag swaggerTag = new SwaggerTag();
        swaggerTag.setName("openapi");
        swaggerModel.setTags(Lists.newArrayList(swaggerTag));

        pathsAndDefinitions(swaggerModel);

        return swaggerModel;
    }

    private void pathsAndDefinitions(SwaggerModel swaggerModel) {
        Map<String, Map<String, SwaggerOperation>> paths = new HashMap<>();
        Map<String, SwaggerDefinition> definitions = new HashMap<>();
        List<OpenApi> openapis = service.list();
        for (OpenApi openApi : openapis) {
            Map<String, SwaggerOperation> operations = new HashMap<>();
            SwaggerOperation operation = new SwaggerOperation();
            operation.setTags(Lists.newArrayList("openapi"));
            operation.setSummary(openApi.getName());
            operation.setDescription(openApi.getName());
            operation.setOperationId(openApi.getRequestUrl()+"Using"+openApi.getRequestMethod());
            operation.setProduces(Lists.newArrayList("application/json"));
            parameters(operation, openApi);

            // body入参
            if (StringUtils.hasText(openApi.getBody())) {
                SwaggerDefinition definition = new SwaggerDefinition();
                definition.setType("object");
                Map<String, SwaggerDefinitionProperties> definitionProperties = new HashMap<>();
                definition.setProperties(definitionProperties);

                JSONObject jsonObject = JSONObject.parseObject(openApi.getBody());
                for (Map.Entry<String, Object> properties : jsonObject.entrySet()) {
                    SwaggerDefinitionProperties swaggerDefinitionProperties = new SwaggerDefinitionProperties();
                    swaggerDefinitionProperties.setType("string");
                    swaggerDefinitionProperties.setDescription(properties.getValue()+"");
                    definitionProperties.put(properties.getKey(), swaggerDefinitionProperties);
                }
                // body的definition构建完成
                definitions.put(openApi.getRequestUrl()+"Using"+openApi.getRequestMethod()+"body", definition);

                SwaggerOperationParameter bodyParameter = new SwaggerOperationParameter();
                bodyParameter.setDescription(openApi.getName() + " body");
                bodyParameter.setIn("body");
                bodyParameter.setName(openApi.getName() + " body");
                bodyParameter.setRequired(true);

                Map<String, String> bodySchema = new HashMap<>();
                bodySchema.put("$ref", "#/definitions/" + openApi.getRequestUrl()+"Using"+openApi.getRequestMethod()+"body");
                bodyParameter.setSchema(bodySchema);

                // 构建参数构建完成
                operation.getParameters().add(bodyParameter);

            }

            // 响应
            Map<String, SwaggerOperationResponse> responses = new HashMap<>();
            SwaggerOperationResponse resp200 = new SwaggerOperationResponse();
            resp200.setDescription("OK");
            Map<String, String> respSchema = new HashMap<>();
            respSchema.put("$ref", "#/definitions/OpenApiResult");
            resp200.setSchema(respSchema);

            responses.put("200", resp200);

            Map<String, String> emptySchema = new HashMap<>();
            SwaggerOperationResponse resp201 = new SwaggerOperationResponse();
            resp201.setDescription("Created");
            resp201.setSchema(emptySchema);
            responses.put("201", resp201);
            SwaggerOperationResponse resp401 = new SwaggerOperationResponse();
            resp401.setDescription("Unauthorized");
            resp401.setSchema(emptySchema);
            responses.put("401", resp401);
            SwaggerOperationResponse resp403 = new SwaggerOperationResponse();
            resp403.setDescription("Forbidden");
            resp403.setSchema(emptySchema);
            responses.put("403", resp403);
            SwaggerOperationResponse resp404 = new SwaggerOperationResponse();
            resp404.setDescription("Not Found");
            resp404.setSchema(emptySchema);
            responses.put("404", resp404);

            // 构建响应definition
            SwaggerDefinition respDefinition = new SwaggerDefinition();
            respDefinition.setType("object");

            Map<String, SwaggerDefinitionProperties> definitionProperties = new HashMap<>();
            respDefinition.setProperties(definitionProperties);

            SwaggerDefinitionProperties codeProperties = new SwaggerDefinitionProperties();
            codeProperties.setType("integer");
            codeProperties.setDescription("返回代码");
            definitionProperties.put("code", codeProperties);
            SwaggerDefinitionProperties messageProperties = new SwaggerDefinitionProperties();
            messageProperties.setType("string");
            messageProperties.setDescription("返回处理消息");
            definitionProperties.put("message", messageProperties);
            SwaggerDefinitionProperties resultProperties = new SwaggerDefinitionProperties();
            resultProperties.setType("object");
            resultProperties.setDescription("返回数据对象");
            definitionProperties.put("result", resultProperties);
            SwaggerDefinitionProperties successProperties = new SwaggerDefinitionProperties();
            successProperties.setType("boolean");
            successProperties.setDescription("成功标志");
            definitionProperties.put("success", successProperties);
            SwaggerDefinitionProperties timestampProperties = new SwaggerDefinitionProperties();
            timestampProperties.setType("integer");
            timestampProperties.setDescription("时间戳");
            definitionProperties.put("timestamp", timestampProperties);

            definitions.put("OpenApiResult", respDefinition);


            operation.setResponses(responses);
            operations.put(openApi.getRequestMethod().toLowerCase(), operation);
            paths.put("/openapi/call/"+openApi.getRequestUrl(), operations);
        }

        swaggerModel.setDefinitions(definitions);
        swaggerModel.setPaths(paths);

    }

    private void parameters(SwaggerOperation operation, OpenApi openApi) {
        List<SwaggerOperationParameter> parameters = new ArrayList<>();

        for (OpenApiParam openApiParam : openApiParamService.findByApiId(openApi.getId())) {
            SwaggerOperationParameter parameter = new SwaggerOperationParameter();
            parameter.setIn("path");
            parameter.setName(openApiParam.getParamKey());
            parameter.setRequired(openApiParam.getRequired() == 1);
            parameter.setDescription(openApiParam.getNote());
            parameters.add(parameter);
        }

        for (OpenApiHeader openApiHeader : openApiHeaderService.findByApiId(openApi.getId())) {
            SwaggerOperationParameter parameter = new SwaggerOperationParameter();
            parameter.setIn("header");
            parameter.setName(openApiHeader.getHeaderKey());
            parameter.setRequired(openApiHeader.getRequired() == 1);
            parameter.setDescription(openApiHeader.getNote());
            parameters.add(parameter);
        }

        operation.setParameters(parameters);
    }

    private SwaggerInfo swaggerInfo() {
        SwaggerInfo info = new SwaggerInfo();

        info.setDescription("OpenAPI 接口列表");
        info.setVersion("3.8.0");
        info.setTitle("OpenAPI 接口列表");
        info.setTermsOfService("https://jeecg.com");

        SwaggerInfoContact contact = new SwaggerInfoContact();
        contact.setName("jeecg@qq.com");

        info.setContact(contact);

        SwaggerInfoLicense license = new SwaggerInfoLicense();
        license.setName("Apache 2.0");
        license.setUrl("http://www.apache.org/licenses/LICENSE-2.0.html");

        info.setLicense(license);

        return info;
    }

    /**
     * 生成接口路径
     * @return
     */
    @GetMapping("genPath")
    public Result<String> genPath() {
        Result<String> r = new Result<String>();
        r.setSuccess(true);
        r.setCode(CommonConstant.SC_OK_200);
        r.setResult(PathGenerator.genPath());
        return r;
    }
}
