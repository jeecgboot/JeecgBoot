AIGC应用平台介绍
===============

一个全栈式 AI 开发平台，旨在帮助开发者快速构建和部署个性化的 AI 应用。

> JDK说明：AI流程编排引擎暂时不支持jdk21，所以目前只能使用jdk8或者jdk17启动项目。


JeecgBoot平台的AIGC功能模块，是一套类似`Dify`的`AIGC应用开发平台`+`知识库问答`，是一款基于LLM大语言模型AI应用平台和 RAG 的知识库问答系统。
其直观的界面结合了 AI 流程编排、RAG 管道、知识库管理、模型管理、对接向量库、实时运行可观察等，让您可以快速从原型到生产，拥有AI服务能力。



### AI视频介绍

[![](https://jeecgos.oss-cn-beijing.aliyuncs.com/files/jeecg_aivideo.png)](https://www.bilibili.com/video/BV1zmd7YFE4w)



#### Dify `VS` JEECG AI

> JEECG AI与Dify相比，在多个方面展现出显著的优势，特别是在文档处理、格式和图片保持方面。以下是一些具体的优点：
> - Markdown文档库导入：
> JEECG AI允许用户直接导入整个Markdown文档库，这不仅保留markdown格式，还支持图片的导入，确保文档内容的完整性和可视化效果。
> - 对话回复格式美观：
> 在对话过程中，JEECG AI能够保持回复内容的原格式，也不丢失图片，使得输出的文章更加美观，不会出现格式错乱的情况，还支持图片的渲染。
> - PDF文档导入与格式转换：
> JEECG AI在处理PDF文档时，能够更好地保持原始格式和图片，确保转换后的内容与原始文档一致。这个功能在许多AI产品中表现不佳，而JEECG AI在这方面做出了显著的优化


| 功能         | Dify             | Jeecg AI                                |
|------------|------------------|-----------------------------------------|
| AI工作流      | 有                | 有                                       |
| RAG 管道向量搜索 | 有                | 有                                       |
| AI模型管理     | 有                | 有                                       |
| AI应用管理     | 有                | 有                                       |
| AI知识库      | 有                | 有                                       |
| 产品方向       | 一款独立的 LLM 应用开发平台 | 低代码与AIGC应用二者结合的平台                       |
| 业务集成       | 业务集成能力弱          | 更方便与业务系统集成，调用系统接口和逻辑更加方便                |
| AI业务流      | 侧重AI逻辑流程         | AI流程编排作为低代码的业务引擎，用户可以通过AI流程配置各种业务流和AI流程 |
| 实现语言       | python + react | JAVA + vue3                             |
| 上传markdown文档库(支持图片) | 不支持 | 支持 |
| AI对话支持发图和展示图片 | 支持 | 支持 |



### 安装向量库 pgvector

- https://help.jeecg.com/aigc/config




## 功能特点

- AI流程: 提供强大的AI流程设计器引擎，支持编排 AI 工作过程，满足复杂业务场景，支持画布上构建和实时运行查看 AI流程运行情况。
- AI流程即服务: 通过AI流程编排你需要的智能体，结合AI+自定义开发节点 实现功能性 API，让你瞬间拥有各种智能体API。
- AI助手对话功能: 集成 ChatGPT、Deepseek、智普、私有大模型 等 AI 模型，提供智能对话和生成式 AI 功能，深度与知识库结合提供更精准的知识。
- RAG 功能: 涵盖从文档摄入到检索的所有内容，支持从 PDF、PPT 和其他常见文档格式中提取文本，支持检索增强生成（RAG），将未训练数据与 AI 模型集成，提升智能交互能力。
- AI 知识库: 通过导入文档或已有问答对进行训练，让 AI 模型能根据文档以交互式对话方式回答问题。
- 模型管理：支持对接各种大模型，包括本地私有大模型（Deepseek/ Llama 3 / Qwen 2 等）、国内公共大模型（通义千问 / 腾讯混元 / 字节豆包 / 百度千帆 / 智谱 AI / Kimi 等）和国外公共大模型（OpenAI / Claude / Gemini 等）；
- 无缝嵌入：Iframe一键嵌入,支持将AI聊天助手快速嵌入到第三方系统，让系统快速拥有智能问答能力，提高用户满意度。




#### 在线体验

- JeecgBoot演示：  https://boot3.jeecg.com
- 敲敲云在线搭建AI知识库：https://app.qiaoqiaoyun.com


## 技术交流

- 开发文档：https://help.jeecg.com/aigc 
- QQ群：716488839


## 功能列表

- AI应用管理(普通应用、高级流程应用)
- AI模型管理
- AI知识库
- AI应用平台(普通、对接AI流程)
- AI流程编排
- AI聊天支持嵌入第三方
- AI向量库对接



## 支持AI模型

| AI大模型         |  支持   |
|---------------| --- |
| DeepSeek      |  √   |
| ChatGTP       |  √   |
| Qwq           |  √   |
| 智库            |  √   |
| Ollama本地搭建大模型 |  √   |
| 等等。。          |  √   |




##  AIGC能做什么

AIGC模块是一个基于AI的自动化流程编排工具和聊天应用搭建平台，它可以帮助用户快速生成AI流程接口和聊天应用，提高效率。
以下是一些具体的应用场景和示例：

- 你可能需要一个翻译接口，可以通过AI流程编排搭建出来。
- 你可能需要一个接口转换工具，可以通过AI流程编排搭建出来。（比如：jimureport所需要接口返回格式与你的系统不同，你通过AI接口实现自动转换）
- 你可能需要一个聊天机器人，可以通过AI流程编排搭建出来。
- 你可能需要一个自动化流程，可以通过AI流程编排搭建出来。
- 你可能需要一个自动化处理文件的流程，可以通过AI流程结合python脚本实现操作电脑，文件等。


## AI应用平台功能展示

AI模型列表

![](https://oscimg.oschina.net/oscnet//a5fb3e0d69ca1706b0de221535c7acaa.png)

选择AI模型，配置你的参数

![](https://oscimg.oschina.net/oscnet//1f941472758a5fc227f54f2683953b8e.png)


AI知识库支持手工录入文本，导入pdf\\word\\excel等文档，支持问答对训练

![](https://oscimg.oschina.net/oscnet//150bb33f48d6c8e2ae059e2a58f4200b.png)

![](https://oscimg.oschina.net/oscnet//032d16c915b0f79318935484c81df260.png)



AI流程，提供强大的AI流程设计器引擎，支持编排 AI 工作过程，满足复杂业务场景，支持画布上构建和实时运行查看 AI流程运行情况。

![](https://oscimg.oschina.net/oscnet//f40f9aa275cd4aea94e1c209513151e2.png)


目前支持的节点有：开始、结束、AI知识库节点、AI节点、分类节点、分支节点、JAVA节点、脚本节点、子流程节点、http请求节点、直接回复节点等节点

![](https://oscimg.oschina.net/oscnet//6d86480ab1bbfab5b2e6992b416b2152.png)

节点项配置

![](https://oscimg.oschina.net/oscnet//90a5f76b6b4fc406e2e2b87245b35459.png)

在线运行看结果

![](https://oscimg.oschina.net/oscnet//bc9817a7bbd94936a5a3e885abe3cb38.png)


AI应用配置，支持AI流程配置和简单的AI配置

![](https://oscimg.oschina.net/oscnet//a853d9be4d3756806799ad025e722df8.png)![](https://oscimg.oschina.net/oscnet//d3bcbf5977c6fb75a8f996e1e40590be.png)

可以关联多个知识库，右侧是AI智能回复，你可以搭建自己的智能体，比如搭建一个 “诗词达人” “翻译助手”

![](https://oscimg.oschina.net/oscnet//c26a848136be3e22ec1e0651e78976c2.png)

可以将创建的聊天应用，集成到第三方系统中

![](https://oscimg.oschina.net/oscnet//39c6f589ef46f0454b229915ffa263f4.png)