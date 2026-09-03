package org.jeecg.modules.airag.app.service.impl;

import org.jeecg.modules.airag.common.consts.AiragConsts;
import org.jeecg.modules.airag.common.vo.MessageHistory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AiragChatServiceImplTest {

    @Test
    void shouldKeepFilesFromConsecutiveAiMessagesWithoutImages() throws Exception {
        MessageHistory firstMessage = aiMessage("first", "first.xlsx");
        MessageHistory secondMessage = aiMessage("second", "second.xlsx");

        List<MessageHistory> mergedMessages = mergeToolMessages(List.of(firstMessage, secondMessage));

        assertEquals(1, mergedMessages.size());
        List<MessageHistory.FileHistory> files = mergedMessages.get(0).getFiles();
        assertEquals(List.of("first.xlsx", "second.xlsx"), files.stream()
                .map(MessageHistory.FileHistory::getFilePath)
                .toList());
    }

    private MessageHistory aiMessage(String content, String filePath) {
        return MessageHistory.builder()
                .role(AiragConsts.MESSAGE_ROLE_AI)
                .content(content)
                .files(List.of(new MessageHistory.FileHistory(filePath)))
                .build();
    }

    @SuppressWarnings("unchecked")
    private List<MessageHistory> mergeToolMessages(List<MessageHistory> histories) throws Exception {
        AiragChatServiceImpl service = new AiragChatServiceImpl();
        Method method = AiragChatServiceImpl.class.getDeclaredMethod("mergeToolMessages", List.class, boolean.class);
        method.setAccessible(true);
        return (List<MessageHistory>) method.invoke(service, histories, false);
    }
}
