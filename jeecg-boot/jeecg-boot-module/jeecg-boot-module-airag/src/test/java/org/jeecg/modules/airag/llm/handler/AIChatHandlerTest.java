package org.jeecg.modules.airag.llm.handler;

import org.junit.jupiter.api.Test;

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
    void buildsMiniMaxAnthropicMessageUrls() {
        assertEquals("https://api.minimax.io/anthropic/v1/messages",
                AIChatHandler.resolveRequestBaseUrl("ANTHROPIC", "https://api.minimax.io/anthropic") + "/messages");
        assertEquals("https://api.minimaxi.com/anthropic/v1/messages",
                AIChatHandler.resolveRequestBaseUrl("ANTHROPIC", "https://api.minimaxi.com/anthropic/") + "/messages");
    }

    @Test
    void preservesOtherRequestBaseUrls() {
        assertEquals("https://api.anthropic.com/v1",
                AIChatHandler.resolveRequestBaseUrl("ANTHROPIC", "https://api.anthropic.com/v1"));
        assertEquals("https://api.minimax.io/anthropic",
                AIChatHandler.resolveRequestBaseUrl("OPENAI", "https://api.minimax.io/anthropic"));
    }
}
