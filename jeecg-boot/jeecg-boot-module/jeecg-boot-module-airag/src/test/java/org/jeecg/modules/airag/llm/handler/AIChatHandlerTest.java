package org.jeecg.modules.airag.llm.handler;

import com.sun.net.httpserver.HttpServer;
import dev.langchain4j.http.client.jdk.JdkHttpClient;
import dev.langchain4j.model.anthropic.AnthropicChatModel;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AIChatHandlerTest {

    @Test
    void mapsMiniMaxToOpenAiCompatibleProvider() {
        assertEquals("OPENAI", AIChatHandler.resolveProvider("MINIMAX"));
        assertEquals("OPENAI", AIChatHandler.resolveProvider("minimax"));
    }

    @Test
    void preservesOtherProviders() {
        assertEquals("ANTHROPIC", AIChatHandler.resolveProvider("ANTHROPIC"));
    }

    @Test
    void derivesMiniMaxAnthropicClientBaseUrls() {
        assertEquals("https://api.minimax.io/anthropic/v1",
                AIChatHandler.resolveRequestBaseUrl("ANTHROPIC", "https://api.minimax.io/anthropic"));
        assertEquals("https://api.minimaxi.com/anthropic/v1",
                AIChatHandler.resolveRequestBaseUrl("ANTHROPIC", "https://api.minimaxi.com/anthropic/"));
    }

    @Test
    void preservesOtherRequestBaseUrls() {
        assertEquals("https://api.anthropic.com/v1",
                AIChatHandler.resolveRequestBaseUrl("ANTHROPIC", "https://api.anthropic.com/v1"));
        assertEquals("https://api.minimax.io/anthropic",
                AIChatHandler.resolveRequestBaseUrl("OPENAI", "https://api.minimax.io/anthropic"));
    }

    @Test
    void capturesAnthropicMessagesRequestPath() throws Exception {
        AtomicReference<String> requestPath = new AtomicReference<>();
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", exchange -> {
            exchange.getRequestBody().readAllBytes();
            requestPath.set(exchange.getRequestURI().getPath());
            byte[] response = ("{\"id\":\"msg_test\",\"type\":\"message\",\"role\":\"assistant\"," +
                    "\"content\":[{\"type\":\"text\",\"text\":\"ok\"}],\"model\":\"MiniMax-M3\"," +
                    "\"stop_reason\":\"end_turn\",\"stop_sequence\":null," +
                    "\"usage\":{\"input_tokens\":1,\"output_tokens\":1}}")
                    .getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length);
            try (OutputStream body = exchange.getResponseBody()) {
                body.write(response);
            }
        });
        server.start();

        try {
            String baseUrl = "http://127.0.0.1:" + server.getAddress().getPort() + "/anthropic/v1";
            String response = AnthropicChatModel.builder()
                    .httpClientBuilder(JdkHttpClient.builder())
                    .baseUrl(baseUrl)
                    .apiKey("test-key")
                    .modelName("MiniMax-M3")
                    .maxRetries(0)
                    .build()
                    .chat("ping");

            assertEquals("ok", response);
            assertEquals("/anthropic/v1/messages", requestPath.get());
        } finally {
            server.stop(0);
        }
    }
}
