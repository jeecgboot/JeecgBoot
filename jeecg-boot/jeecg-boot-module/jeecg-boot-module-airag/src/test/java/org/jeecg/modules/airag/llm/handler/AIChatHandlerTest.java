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
}
