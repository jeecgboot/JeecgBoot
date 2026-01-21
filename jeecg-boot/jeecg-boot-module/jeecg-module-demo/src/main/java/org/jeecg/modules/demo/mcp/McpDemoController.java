package org.jeecg.modules.demo.mcp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.config.shiro.IgnoreAuth;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MCP Server 示例 (Model Context Protocol)
 * 
 * 这是一个符合 MCP 协议的服务端实现，支持 SSE 传输。
 * 
 * 连接地址: http://你的服务器:8080/jeecg-boot/demo/mcp/sse
 * 
 * 提供的工具:
 * - hello: 打招呼工具
 * - get_time: 获取当前时间
 * - calculate: 简单计算器
 */
@Slf4j
@RestController
@RequestMapping("/demo/mcp")
@Tag(name = "MCP Server 示例")
public class McpDemoController {

    // 存储 SSE 连接
    private final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    
    // 定义工具列表
    private final List<Map<String, Object>> TOOLS = List.of(
            Map.of(
                    "name", "hello",
                    "description", "打招呼工具，返回问候语",
                    "inputSchema", Map.of(
                            "type", "object",
                            "properties", Map.of(
                                    "name", Map.of("type", "string", "description", "你的名字")
                            ),
                            "required", List.of("name")
                    )
            ),
            Map.of(
                    "name", "get_time",
                    "description", "获取当前服务器时间",
                    "inputSchema", Map.of(
                            "type", "object",
                            "properties", Map.of()
                    )
            ),
            Map.of(
                    "name", "calculate",
                    "description", "简单计算器，支持加减乘除",
                    "inputSchema", Map.of(
                            "type", "object",
                            "properties", Map.of(
                                    "a", Map.of("type", "number", "description", "第一个数"),
                                    "b", Map.of("type", "number", "description", "第二个数"),
                                    "operator", Map.of("type", "string", "description", "运算符: +, -, *, /")
                            ),
                            "required", List.of("a", "b", "operator")
                    )
            )
    );

    /**
     * MCP SSE 端点 - 客户端通过此接口建立 SSE 连接
     */
    @IgnoreAuth
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "MCP SSE 连接端点")
    public SseEmitter sse(HttpServletRequest request) {
        String clientId = UUID.randomUUID().toString();
        log.info("[MCP Server] 新客户端 SSE 连接: {}", clientId);
        
        SseEmitter emitter = new SseEmitter(0L); // 不超时
        sseEmitters.put(clientId, emitter);
        
        emitter.onCompletion(() -> {
            log.info("[MCP Server] 客户端断开: {}", clientId);
            sseEmitters.remove(clientId);
        });
        emitter.onTimeout(() -> {
            log.info("[MCP Server] 客户端超时: {}", clientId);
            sseEmitters.remove(clientId);
        });
        emitter.onError(e -> {
            log.error("[MCP Server] SSE 错误: {}", e.getMessage());
            sseEmitters.remove(clientId);
        });
        
        // 发送 endpoint 事件，告诉客户端消息端点地址
        try {
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String messageEndpoint = baseUrl + request.getContextPath() + "/demo/mcp/message?sessionId=" + clientId;
            emitter.send(SseEmitter.event()
                    .name("endpoint")
                    .data(messageEndpoint));
            log.info("[MCP Server] 发送 endpoint 事件: {}", messageEndpoint);
        } catch (IOException e) {
            log.error("[MCP Server] 发送 endpoint 事件失败", e);
        }
        
        return emitter;
    }

    /**
     * Streamable HTTP 端点 - 同时支持 POST 到 /sse 的 JSON-RPC 请求
     * Cursor 客户端会先尝试这种方式
     */
    @IgnoreAuth
    @PostMapping(value = "/sse")
    @Operation(summary = "MCP Streamable HTTP 端点")
    public void ssePost(@RequestBody String body, HttpServletResponse response) throws IOException {
        log.info("[MCP Server] Streamable HTTP 请求: {}", body);
        handleJsonRpcRequest(body, response);
    }

    /**
     * MCP 消息处理端点 - 处理 JSON-RPC 请求
     * 直接写入原始 JSON-RPC 响应，避免框架包装
     */
    @IgnoreAuth
    @PostMapping(value = "/message")
    @Operation(summary = "MCP 消息处理")
    public void handleMessage(@RequestParam(required = false) String sessionId,
                              @RequestBody String body,
                              HttpServletResponse response) throws IOException {
        log.info("[MCP Server] 收到消息, sessionId: {}, body: {}", sessionId, body);
        handleJsonRpcRequest(body, response);
    }

    /**
     * 处理 JSON-RPC 请求的公共方法
     */
    private void handleJsonRpcRequest(String body, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        
        try {
            JSONObject request = JSON.parseObject(body);
            String method = request.getString("method");
            Object id = request.get("id");
            JSONObject params = request.getJSONObject("params");
            
            // 通知类消息（没有id）不需要响应
            if (id == null) {
                log.info("[MCP Server] 收到通知: {}", method);
                writer.write("{}");
                writer.flush();
                return;
            }
            
            // 构建 JSON-RPC 2.0 响应
            Map<String, Object> jsonRpcResponse = new LinkedHashMap<>();
            jsonRpcResponse.put("jsonrpc", "2.0");
            jsonRpcResponse.put("id", id);
            
            try {
                Object result = switch (method) {
                    case "initialize" -> handleInitialize(params);
                    case "initialized", "notifications/initialized" -> handleInitialized();
                    case "tools/list" -> handleToolsList();
                    case "tools/call" -> handleToolsCall(params);
                    case "ping" -> handlePing();
                    case "notifications/cancelled" -> handleCancelled(params);
                    default -> {
                        if (method != null && method.startsWith("notifications/")) {
                            log.info("[MCP Server] 忽略未知通知: {}", method);
                            yield Map.of();
                        }
                        throw new RuntimeException("未知方法: " + method);
                    }
                };
                jsonRpcResponse.put("result", result);
            } catch (Exception e) {
                log.error("[MCP Server] 处理请求失败", e);
                jsonRpcResponse.put("error", Map.of(
                        "code", -32603,
                        "message", e.getMessage()
                ));
            }
            
            String responseJson = JSON.toJSONString(jsonRpcResponse);
            log.info("[MCP Server] 返回: {}", responseJson);
            writer.write(responseJson);
            
        } catch (Exception e) {
            log.error("[MCP Server] 解析请求失败", e);
            writer.write("{\"jsonrpc\":\"2.0\",\"id\":null,\"error\":{\"code\":-32700,\"message\":\"Parse error\"}}");
        }
        
        writer.flush();
    }

    /**
     * 处理 initialize 请求
     */
    private Map<String, Object> handleInitialize(JSONObject params) {
        log.info("[MCP Server] 初始化请求: {}", params);
        return Map.of(
                "protocolVersion", "2024-11-05",
                "capabilities", Map.of(
                        "tools", Map.of()
                ),
                "serverInfo", Map.of(
                        "name", "jeecg-mcp-demo",
                        "version", "1.0.0"
                )
        );
    }
    
    /**
     * 处理 initialized 通知
     */
    private Map<String, Object> handleInitialized() {
        log.info("[MCP Server] 客户端已初始化完成");
        return Map.of();
    }
    
    /**
     * 处理 ping 请求
     */
    private Map<String, Object> handlePing() {
        log.info("[MCP Server] Ping");
        return Map.of();
    }
    
    /**
     * 处理 notifications/cancelled 通知
     */
    private Map<String, Object> handleCancelled(JSONObject params) {
        log.info("[MCP Server] 请求被取消: {}", params);
        return Map.of();
    }

    /**
     * 处理 tools/list 请求
     */
    private Map<String, Object> handleToolsList() {
        log.info("[MCP Server] 获取工具列表");
        return Map.of("tools", TOOLS);
    }

    /**
     * 处理 tools/call 请求
     */
    private Map<String, Object> handleToolsCall(JSONObject params) {
        String toolName = params.getString("name");
        JSONObject arguments = params.getJSONObject("arguments");
        if (arguments == null) {
            arguments = new JSONObject();
        }
        log.info("[MCP Server] 调用工具: {}, 参数: {}", toolName, arguments);
        
        String result = switch (toolName) {
            case "hello" -> {
                String name = arguments.getString("name");
                if (name == null || name.isEmpty()) {
                    name = "World";
                }
                yield "你好, " + name + "! 欢迎使用 JeecgBoot MCP 服务!";
            }
            case "get_time" -> {
                yield "当前时间: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            case "calculate" -> {
                double a = arguments.getDoubleValue("a");
                double b = arguments.getDoubleValue("b");
                String op = arguments.getString("operator");
                if (op == null) op = "+";
                double res = switch (op) {
                    case "+" -> a + b;
                    case "-" -> a - b;
                    case "*" -> a * b;
                    case "/" -> b != 0 ? a / b : Double.NaN;
                    default -> throw new RuntimeException("不支持的运算符: " + op);
                };
                yield String.format("%.2f %s %.2f = %.2f", a, op, b, res);
            }
            default -> throw new RuntimeException("未知工具: " + toolName);
        };
        
        return Map.of(
                "content", List.of(Map.of(
                        "type", "text",
                        "text", result
                ))
        );
    }

    /**
     * 使用说明页面
     */
    @IgnoreAuth
    @GetMapping("/info")
    @Operation(summary = "MCP Server 使用说明")
    public Map<String, Object> info(HttpServletRequest request) {
        log.info("[MCP Server] Hello 接口被访问");
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        return Map.of(
                "success", true,
                "message", "JeecgBoot MCP Server 示例",
                "sseUrl", baseUrl + "/demo/mcp/sse",
                "tools", List.of(
                        Map.of("name", "hello", "description", "打招呼工具", "params", "name: 你的名字"),
                        Map.of("name", "get_time", "description", "获取当前时间", "params", "无"),
                        Map.of("name", "calculate", "description", "简单计算器", "params", "a, b, operator(+,-,*,/)")
                ),
                "usage", "在 Cursor/Claude 等 MCP 客户端中配置 SSE URL: " + baseUrl + "/demo/mcp/sse",
                "example", "请调用 hello 工具，参数 name 填 \"测试用户\""
        );
    }
}
