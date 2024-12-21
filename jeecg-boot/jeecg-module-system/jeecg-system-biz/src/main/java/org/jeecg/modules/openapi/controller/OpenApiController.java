package org.jeecg.modules.openapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Lists;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.openapi.entity.OpenApi;
import org.jeecg.modules.openapi.entity.OpenApiHeader;
import org.jeecg.modules.openapi.entity.OpenApiParam;
import org.jeecg.modules.openapi.generator.PathGenerator;
import org.jeecg.modules.openapi.service.OpenApiHeaderService;
import org.jeecg.modules.openapi.service.OpenApiParamService;
import org.jeecg.modules.openapi.service.OpenApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.schema.*;
import springfox.documentation.service.*;
import springfox.documentation.spring.web.DocumentationCache;

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
    private DocumentationCache documentationCache;
    @Autowired
    private TypeResolver typeResolver;

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

        HttpEntity<String> httpEntity = new HttpEntity<>(json, httpHeaders);

        return restTemplate.exchange(url, HttpMethod.resolve(method), httpEntity, Result.class, request.getParameterMap()).getBody();
    }

    @GetMapping("/call/add")
    public void addSwagger() {
        Documentation documentation = documentationCache.documentationByGroup("default");

        List<ApiListing> apis = new ArrayList<>();

        Set<String> produces = new HashSet<>();
        produces.add("application/json");
        Set<String> consumes = new HashSet<>();
        consumes.add("application/json");

        Set<String> tags = new HashSet<>();
        Tag tag = new Tag("openapi", "openapi");
        tags.add("openapi");
        Set<Tag> apiTags = new HashSet<>();
        apiTags.add(tag);
        documentation.getTags().add(tag);

        Model resultModel = documentation.getApiListings().get("login-controller").get(0).getModels().get("接口返回对象«JSONObject»");

        ModelRef stringModelRef = new ModelRef("string");

        ResolvedType stringResolvedType = typeResolver.resolve(String.class);


        HashMap<String, ModelProperty> propertyMap = new HashMap<>();
        ModelProperty modelProperty = new ModelProperty("abc", stringResolvedType, "java.lang.String", 0, true, false, false, false, "姓名", null, null, null, null, null, new ArrayList<>());
        propertyMap.put("abc", modelProperty);
        ModelProperty modelProperty1 = new ModelProperty("bcd", stringResolvedType, "java.lang.String", 0, true, false, false, false, "姓名", null, null, null, null, null, new ArrayList<>());
        propertyMap.put("bcd", modelProperty1);

        ResolvedType mapResolvedType = typeResolver.resolve(HashMap.class);

        List<ModelReference> subTypes = new ArrayList<>();
        subTypes.add(stringModelRef);

        Model bodyModel = new Model("path_body", "body", mapResolvedType, "java.util.HashMap", propertyMap, "请求体结构", "", "", subTypes, null, null);
        ModelRef bodyRef = new ModelRef("bodyModel", "bodyModel", null, null, "path_body");

        Set<ResponseMessage> responseMessages = documentation.getApiListings().get("login-controller").get(0).getApis().get(2).getOperations().get(0).getResponseMessages();

        List<Parameter> parameters = new ArrayList<>();
        //    header-->请求参数的获取：@RequestHeader()
        //    query-->请求参数的获取：@RequestParam()
        //    path-->请求参数的获取：@PathVariable()
        //    body-->请求参数的获取：@RequestBody()
        //    form（不常用）
        Parameter body = new Parameter("body",
                "请求体",
                "",
                true,
                false,
                true,
                bodyRef,
                Optional.of(mapResolvedType),
                null,
                "body",
                null,
                false,
                null,
                null,
                2147483647,
                null,
                new HashMap<>(),
                new ArrayList<>());

        parameters.add(body);

        Parameter parameter = new Parameter("name",
                "姓名",
                "张三",
                false,
                false,
                true,
                stringModelRef,
                Optional.empty(),
                null,
                "query",
                null,
                false,
                null,
                null,
                2147483647,
                null,
                new HashMap<>(),
                new ArrayList<>());

        parameters.add(parameter);

        ModelRef modelRef = new ModelRef(resultModel.getType().getTypeName(),
                resultModel.getQualifiedType(),
                null,
                null,
                false,
                resultModel.getId());

        Operation operation = new Operation(HttpMethod.resolve("GET"),
                "模拟第一个openapi接口",
                "模拟第一个openapi接口",
                modelRef,
                "abcUsingGET",
                0, tags, produces, consumes,
                new LinkedHashSet<>(),
                new ArrayList<>(),
                parameters,
                responseMessages,
                "false",
                false,
                new ArrayList<>());

        List<Operation> operations = new ArrayList<>();
        operations.add(operation);

        ApiDescription apiDescription = new ApiDescription("openapi",
                "/jeecg-boot/openapi/call/abc", "openapi", operations, false);

        List<ApiDescription> apiList = new ArrayList<>();
        apiList.add(apiDescription);

        Map<String, Model> responseModel = new HashMap<>();
        responseModel.put("接口返回对象«JSONObject»", resultModel);
        responseModel.put("bodyModel", bodyModel);

        ApiListing api = new ApiListing("1.0",
                "/",
                "/openapi/call/abc",
                produces, consumes,
                "", new HashSet<>(), new ArrayList<>(), apiList, responseModel, "abc", 0, apiTags);

        apis.add(api);

        documentation.getApiListings().put("openapi", apis);
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

    public void resetOpenapiSwagger() {
        List<OpenApi> openapis = service.list();
        Documentation documentation = documentationCache.documentationByGroup("default");
        List<ApiListing> apis = new ArrayList<>();

        // --------------------- swagger common --------------

        // http参数
        Set<String> produces = new HashSet<>();
        produces.add("application/json");
        Set<String> consumes = new HashSet<>();
        consumes.add("application/json");

        // 标题栏
        Set<String> tags = new HashSet<>();
        Tag tag = new Tag("openapi", "openapi");
        tags.add("openapi");
        Set<Tag> apiTags = new HashSet<>();
        apiTags.add(tag);
        documentation.getTags().add(tag);
        ModelRef stringModelRef = new ModelRef("string", null, false);

        // 响应文档
        Model resultModel = documentation.getApiListings().get("login-controller").get(0).getModels().get("接口返回对象«JSONObject»");
        Set<ResponseMessage> responseMessages = documentation.getApiListings().get("login-controller").get(0).getApis().get(2).getOperations().get(0).getResponseMessages();
        ModelRef modelRef = new ModelRef(resultModel.getType().getTypeName(),
                resultModel.getQualifiedType(),
                null,
                null,
                false,
                resultModel.getId());
        // --------------------- swagger common --------------

        for (OpenApi openapi : openapis) {
            openapi.setParams(openApiParamService.findByApiId(openapi.getId()));
            openapi.setHeaders(openApiHeaderService.findByApiId(openapi.getId()));

            // 参数，包含header\query\path\body\form 五类数据
            List<Parameter> parameters = new ArrayList<>();
            //    header-->请求参数的获取：@RequestHeader()
            //    query-->请求参数的获取：@RequestParam()
            //    path-->请求参数的获取：@PathVariable()
            //    body-->请求参数的获取：@RequestBody()
            //    form（不常用）
            for (OpenApiHeader openApiHeader : openapi.getHeaders()) {
                Parameter parameter = new Parameter(openApiHeader.getHeaderKey(),
                        openApiHeader.getNote(),
                        openApiHeader.getDefaultValue(),
                        openApiHeader.getRequired() == 1,
                        false,
                        true,
                        stringModelRef,
                        Optional.empty(),
                        null,
                        "header",
                        null,
                        false,
                        null,
                        null,
                        2147483647,
                        null,
                        new HashMap<>(),
                        new ArrayList<>());
                parameters.add(parameter);
            }

            for (OpenApiParam openApiParam : openapi.getParams()) {
                Parameter parameter = new Parameter(openApiParam.getParamKey(),
                        openApiParam.getNote(),
                        openApiParam.getDefaultValue(),
                        openApiParam.getRequired() == 1,
                        false,
                        true,
                        stringModelRef,
                        Optional.empty(),
                        null,
                        "query",
                        null,
                        false,
                        null,
                        null,
                        2147483647,
                        null,
                        new HashMap<>(),
                        new ArrayList<>());

                parameters.add(parameter);
            }

            Operation operation = new Operation(HttpMethod.resolve("GET"),
                    "模拟第一个openapi接口",
                    "模拟第一个openapi接口",
                    modelRef,
                    "abcUsingGET",
                    0, tags, produces, consumes,
                    new LinkedHashSet<>(),
                    new ArrayList<>(),
                    parameters,
                    responseMessages,
                    "false",
                    false,
                    new ArrayList<>());

            List<Operation> operations = new ArrayList<>();
            operations.add(operation);

            ApiDescription apiDescription = new ApiDescription("openapi",
                    "/jeecg-boot/openapi/call/abc", "openapi", operations, false);

            List<ApiDescription> apiList = new ArrayList<>();
            apiList.add(apiDescription);

            Map<String, Model> responseModel = new HashMap<>();
            responseModel.put("接口返回对象«JSONObject»", resultModel);

            ApiListing api = new ApiListing("1.0",
                    "/",
                    "/openapi/call/abc",
                    produces, consumes,
                    "", new HashSet<>(), new ArrayList<>(), apiList, responseModel, "abc", 0, apiTags);

            apis.add(api);
        }

        documentation.getApiListings().put("openapi", apis);
    }
}
