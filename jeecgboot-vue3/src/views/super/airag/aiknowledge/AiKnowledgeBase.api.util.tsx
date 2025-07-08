import {knowledgeDeleteAllDoc} from "./AiKnowledgeBase.api";
import {useMessage} from "@/hooks/web/useMessage";

const {createConfirmSync} = useMessage();

// 清空文档
export async function doDeleteAllDoc(knowledgeId: string, reload: () => void) {
  const flag = await createConfirmSync({
    title: '清空文档',
    content: () => (
      <p>
        <span>确定要清空所有文档吗？</span>
        <br/>
        <span style="color: #ee0000;">
          此操作会删除所有已录入的文档，并且不能恢复，请谨慎操作
        </span>
      </p>
    ),
  });
  if (!flag) {
    return;
  }
  knowledgeDeleteAllDoc(knowledgeId, reload)
}
