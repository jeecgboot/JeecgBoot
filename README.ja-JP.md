[中文](./README.md) | [English](./README.en-US.md) | 日本語

![JEECG](https://jeecgos.oss-cn-beijing.aliyuncs.com/files/logov3.png "JeecgBoot ローコード開発プラットフォーム")



JEECG BOOT AI ローコードプラットフォーム
===============

🚀 **ローコードは v2.0 時代へ — 自然言語の一文だけでシステム全体を生成**

現在のバージョン：3.9.2（2026-04-30）


[![AUR](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](https://github.com/zhangdaiscott/jeecg-boot/blob/master/LICENSE)
[![](https://img.shields.io/badge/Author-guojusoft-orange.svg)](http://www.jeecg.com)
[![](https://img.shields.io/badge/blog-技術ブログ-orange.svg)](https://www.toutiao.com/c/user/token/CiZtC9Gn_yl2qDcTF9Kqhh9LV7tW1cJsdRn2rfFUU5lZy4RJaD4hdBpJCjwAAAAAAAAAAAAAUFvsvy4j57fg5tqwVmxukKxGEhbMl7pE14lY9_YSDq-A26V7xrG3hYuq_Z9y6F3W01oQkIKQDhjDxYPqBCIBA2iiHh0=)
[![](https://img.shields.io/badge/version-3.9.2-brightgreen.svg)](https://github.com/zhangdaiscott/jeecg-boot)
[![GitHub stars](https://img.shields.io/github/stars/zhangdaiscott/jeecg-boot.svg?style=social&label=Stars)](https://github.com/zhangdaiscott/jeecg-boot)
[![GitHub forks](https://img.shields.io/github/forks/zhangdaiscott/jeecg-boot.svg?style=social&label=Fork)](https://github.com/zhangdaiscott/jeecg-boot)
[![Skills](https://img.shields.io/badge/AI%20Skills-自然言語プログラミング-ff4d4f.svg)](https://jeecg.com/skills)


> 🔥 **AI Skills 自然言語プログラミング**：Claude Code と組み合わせ、一文だけでフルスタックコード生成、BPMN フロー作成、フォーム設計、レポートと大画面ダッシュボード生成が可能。JeecgBoot ローコードの全シーンをカバー。
>
> - 📦 Skills リポジトリ：**https://github.com/jeecgboot/skills**
> - 🌐 Skills 公式サイト：**https://jeecg.com/skills**
> - 📺 デモ動画：[JeecgBoot + Skills 自然言語プログラミング](https://www.bilibili.com/video/BV1KKwTzJEbX/) ｜ 📖 ドキュメント：[Skill 比較表](https://help.jeecg.com/java/ai/skills/skill-comparison/)
>
> **Skills でできること：** 一文で CRUD 全コード（フロントエンド + バックエンド + SQL + メニュー権限）生成・Online フォーム / Online レポート / デザイナーフォーム作成・BPMN フロー描画・大画面ダッシュボード構築・JimuReport、ポータル、ミニフローなどを自然言語で生成。


プロジェクト紹介
-----------------------------------

<h3 align="center">Java AI ローコードプラットフォーム</h3>

JeecgBootは、コード`ジェネレーター`を基盤とした`AIローコードプラットフォーム`です！フロントエンドとバックエンドの分離アーキテクチャとして、SpringBoot2.x、SpringCloud、Ant Design & Vue、Mybatis Plus、Shiro、JWTを採用し、マイクロサービスに対応しています。強力なコードジェネレーターにより、フロントエンドとバックエンドのコードを自動生成し、ローコード開発を実現します！JeecgBootは新しいローコード開発パラダイム（OnlineCoding → コードジェネレーター → 手動マージ）を提唱し、Javaプロジェクトにおける70%の重複作業を解消することで、開発をよりビジネスに集中させます。開発効率を迅速に向上させ、研究開発コストを削減するだけでなく、柔軟性も損ないません！

JeecgBootは、オンライン開発を真のゼロコードにするための一連のローコードモジュールを提供します：オンラインフォーム開発、オンラインレポート、レポート設定機能、オンラインチャート設計、大画面設計、モバイル設定機能、フォームデザイナー、オンラインフロー設計、プロセス自動化設定、プラグイン機能（プラグ可能）など！


JEECGの目的は：シンプルな機能はOnlineCodingの設定で実装し、ゼロコード開発を実現すること。複雑な機能はコードジェネレーターで生成し、手動マージすることでローコード開発を実現し、インテリジェンスと柔軟性の両方を確保します。ローコード開発を実現しつつ、柔軟なコーディングもサポートし、現在のローコード製品に共通する柔軟性の欠如という欠点を解消しています！

JEECGのビジネスプロセス：ワークフローを使用してタスクインターフェースを実装・拡張し、ビジネスロジックを開発・記述します。フォームは複数のソリューションを提供：フォームデザイナー、オンライン設定フォーム、コーディングフォーム。同時に、プロセスとフォームの分離設計（疎結合）を実現し、タスクノードの柔軟な設定をサポートすることで、企業のプロセスの機密性を確保しつつ、開発者の作業負荷を軽減します。

AIによるローコードの強化：現在、JeecgBootはChatGPTやDeepSeekなどのAI大規模言語モデルに対応しています。最新バージョンではデフォルトでDeepSeekを使用しており、より高速かつ高品質です。AIチャットアシスタント、AIテーブル作成、AIレポート生成などの機能を提供しています。

技術サポート
-----------------------------------

使用中の問題やバグは[Issues](https://github.com/jeecgboot/JeecgBoot/issues/new?template=bug_report.md)で報告できます。


##### プロジェクト説明

| プロジェクト                | 説明                     | 
|--------------------|------------------------|
| `jeecg-boot`    | SpringBootバックエンドソースコード（マイクロサービス対応）      |
| `jeecgboot-vue3` | Vue3+TS 新フロントエンドソースコード|
| `jeecg-uniapp` | [APP開発フレームワーク、一つのコードで複数端末に対応、APP・ミニプログラム・H5をサポート](https://github.com/jeecgboot/jeecg-uniapp) |


### 動画紹介

[![](https://upload.jeecg.com/jeecg/qiaoqiaoyunsite/jeecgvideo02.png)](https://www.bilibili.com/video/BV1Nk4y1o7Qc)



その他のソースコードダウンロード
-----------------------------------
- APPソースコード：https://github.com/jeecgboot/jeecg-uniapp



プロジェクトの適用範囲
-----------------------------------
JeecgBoot AIローコードプラットフォームは、あらゆるJ2EEプロジェクトの開発に適用でき、特にSAASプロジェクト、企業情報管理システム（MIS）、社内オフィスシステム（OA）、企業資源計画システム（ERP）、顧客関係管理システム（CRM）などに適しています。その半インテリジェントな手動マージ開発方式により、開発効率を70%以上大幅に向上させ、開発コストを大幅に削減できます。


プロジェクトの起動
-----------------------------------

> デフォルトのアカウント/パスワード：admin/123456

- [開発環境のセットアップ](https://help.jeecg.com/java/setup/tools)
- [IDEAクイックスタート（シングルモデル）](https://help.jeecg.com/java/setup/idea/startup)
- [Dockerクイックスタート（シングルモデル）](https://help.jeecg.com/java/docker/quick)
- [IDEAクイックスタート（マイクロサービスモデル）](https://help.jeecg.com/java/springcloud/switchcloud/monomer)
- [Dockerクイックスタート（マイクロサービスモデル）](https://help.jeecg.com/java/docker/quickcloud)


技術ドキュメント
-----------------------------------

- 公式サイト：[http://www.jeecg.com](http://www.jeecg.com)
- デモ：[オンラインデモ](http://boot3.jeecg.com) | [APP](http://jeecg.com/appIndex)
- ドキュメント：[ドキュメントセンター](http://help.jeecg.com) | [AI設定](https://help.jeecg.com/java/ai/aichat)
- 初心者ガイド：[クイックスタート](http://www.jeecg.com/doc/quickstart) | [Q&A](http://www.jeecg.com/doc/qa) | [1分間体験](https://my.oschina.net/jeecg/blog/3083313)
- QQグループ：964611995、⑩716488839(満)、⑨808791225(満)






スター推移
-----------------------------------

[![Star History Chart](https://api.star-history.com/svg?repos=jeecgboot/jeecg-boot&type=Date)](https://star-history.com/#jeecgboot/jeecg-boot)




バックエンドディレクトリ構成
-----------------------------------
```
プロジェクト構成
├─jeecg-boot-parent
│  ├─jeecg-boot-base-core
│  ├─jeecg-module-demo    
│  ├─jeecg-module-system 
│  │  ├─jeecg-system-biz   
│  │  ├─jeecg-system-start システム（8080）
│  │  ├─jeecg-system-api   
│  │  │  ├─jeecg-system-cloud-api  
│  │  │  ├─jeecg-system-local-api   
│  ├─jeecg-server-cloud           
│     ├─jeecg-cloud-gateway       （9999）
│     ├─jeecg-cloud-nacos       --Nacos（8848）
│     ├─jeecg-system-cloud-start  --システム（7001）
│     ├─jeecg-demo-cloud-start    --デモ（7002）
│     ├─jeecg-visual
│        ├─jeecg-cloud-monitor       -- （9111）
│        ├─jeecg-cloud-xxljob        -- （9080）
│        ├─jeecg-cloud-sentinel     --sentinel（9000）
│        ├─jeecg-cloud-test           
│           ├─jeecg-cloud-test-more        
│           ├─jeecg-cloud-test-rabbitmq     
│           ├─jeecg-cloud-test-seata         
│           ├─jeecg-cloud-test-shardingsphere    

```




なぜJeecgBootなのか？
-----------------------------------
* 最新の主流フロントエンド・バックエンド分離フレームワーク（Springboot+Mybatis+antd）を採用し、使いやすい。コードジェネレーターは低依存で、柔軟な拡張性を持ち、迅速な二次開発が可能。
* マイクロサービス SpringCloud Alibaba（Nacos, Gateway, Sentinel, Skywalking）をサポートし、切替メカニズムを提供。シングルサービスとマイクロサービスの自由な切替に対応。
* 高い開発効率、コードジェネレーターを使用して、単一テーブル、ツリーリスト、一対多、一対一などのデータモデルの追加・削除・変更・検索機能をワンクリックで生成、メニュー設定でそのまま使用可能。
* コードジェネレーターは強力なテンプレートメカニズムを提供し、カスタムテンプレートをサポート。現在4セットのスタイルテンプレートを提供（単一テーブル2セット、ツリーモデル1セット、一対多3セット）。
* コードジェネレーターは非常にインテリジェントで、オンラインビジネスモデリング、オンライン設定、WYSIWYG（見たまま編集）で23種類のコントロールをサポート。ワンクリックでフロントエンドとバックエンドのコードを生成し、開発効率を大幅に向上。繰り返し作業の心配はもう不要。
* ローコード機能：オンラインフォーム（コーディング不要、オンライン設定でフォームの追加・削除・変更・検索を実現。単一テーブル、ツリー、一対多、一対一モデルをサポートし、誰でもコーディング可能に）
* ローコード機能：オンラインレポート（コーディング不要、オンライン設定でデータレポートを実現。迅速なデータ抽出で開発負担を軽減し、誰でもコーディング可能に）
* ローコード機能：オンラインチャート（コーディング不要、オンライン設定でグラフ、棒グラフ、データレポートなどを実現。カスタムレイアウトをサポートし、誰でもコーディング可能に）
* ユーザー、ロール、メニュー、組織、データ辞書、オンラインスケジュールタスクなどの基本機能を完全にカプセル化。アクセス権限、ボタン権限、データ権限などの機能をサポート。
* よく使われる共通パッケージ、各種ツール（スケジュールタスク、SMS連携、メール送信、Excelインポート・エクスポートなど）を搭載し、プロジェクト要件の80%をほぼカバー。
* 簡単なExcelインポート・エクスポート、単一テーブルエクスポートと一対多テーブルモードのエクスポートをサポート。生成されたコードにはインポート・エクスポート機能が付属。
* シンプルなレポートツールを統合、画像レポートやデータエクスポートが非常に便利。グラフィカルレポート、PDF、Excel、Wordなどのレポートを極めて簡単に生成可能。
* フロントエンド・バックエンド分離技術により、ページのUIスタイルは精巧。よく使われるコンポーネントをカプセル化：時間、行テーブルコントロール、切り取り表示コントロール、レポートコンポーネント、エディタなど。
* クエリフィルター：クエリ機能を自動生成、バックエンドで動的にSQLをスプライスして追加クエリ条件を生成。複数のマッチングモード（完全一致、あいまい検索、包含検索、不一致検索）をサポート。
* データ権限（きめ細かいデータ権限制御、行レベル・リストレベル・フォームフィールドレベルまで制御可能。異なる人が異なるデータを表示し、同じページでも異なる人が異なるフィールドを操作可能）
* ページバリデーションを自動生成（必須入力、数値検証、金額検証、日時検証など）。
* SAASサービスモデルをサポートし、SaaSマルチテナントアーキテクチャソリューションを提供。
* 分散ファイルサービス、Minio、Alibaba OSSなどの優れたサードパーティを統合し、便利なファイルアップロードと管理を提供。ローカルストレージもサポート。
* 主流データベース互換性、一つのコードでMySQL、PostgreSQL、Oracle、SQL Server、MariaDB、DM（達夢）などの主流データベースに完全対応。
* ワークフローFlowableを統合し、ページ上でのフロー方向の設定のみで実現。BPMワークフローの開発を大幅に簡素化。BPMのプロセスデザイナーでフロー方向を描画すれば、少量のJavaコードでワークフローがほぼ完成。
* ローコード機能：オンラインプロセス設計、オープンソースのActivitiプロセスエンジンを使用し、オンラインでプロセスを描画、カスタムフォーム、フォーム添付ファイル、ビジネスフローを実現。
* マルチデータソース：シンプルな使用方法、オンラインでデータソースを設定し、他のデータから簡単にデータを取得可能。
* シングルサインオンCAS統合ソリューションを提供し、プロジェクト内に完全な連携コードを提供済み。
* ローコード機能：フォームデザイナー、ユーザー定義のフォームレイアウトをサポート。単一テーブル、一対多フォームをサポートし、select、radio、checkbox、textarea、date、ポップアップ、リスト、マクロなどのコントロールに対応。
* プロフェッショナルなインターフェース連携メカニズム、統一的にRESTfulインターフェースを使用、swagger-uiオンラインインターフェースドキュメントを統合、JWTトークンによるセキュリティ検証で、クライアントの連携が容易。
* インターフェースセキュリティメカニズム、きめ細かいインターフェース認可制御が可能。異なるクライアントが自分のデータのみ参照可能な制御を非常に簡単に実現。
* 高度な組み合わせクエリ機能、オンライン設定でメインテーブルとサブテーブルの関連クエリをサポート、クエリ履歴の保存が可能。
* 各種システム監視を提供、システム稼働状況のリアルタイム追跡（Redis、Tomcat、JVM、サーバー情報、リクエストトラッキング、SQL監視の監視）
* メッセージセンター（SMS、メール、WeChat プッシュなどをサポート）
* WebSocketメッセージ通知メカニズムを統合
* 優れたモバイル対応エフェクト、APPリリーススキームを提供：
* 多言語対応、国際化ソリューションを提供
* データ変更記録ログ、各データの変更内容を記録可能。バージョン比較機能により過去の変更履歴を確認可能
* プラットフォームUIは強力で、モバイル対応を実装
* プラットフォームホーム画面スタイル、複数の組み合わせモードを提供、カスタムスタイルをサポート
* 使いやすい印刷プラグインを提供、Google Chrome、Firefox、IE11+などのブラウザをサポート
* 豊富なサンプルコード、多数の学習用事例を参照として提供
* Mavenモジュール開発方式を採用
* 動的メニュールーティングをサポート
* RBAC（ロールベースアクセス制御）による権限制御を採用
* 新しい行編集テーブル JVXETable を提供、様々な複雑なERPレイアウトに容易に対応。より高いパフォーマンス、より柔軟な拡張性、より強力な機能

 
 
 
技術アーキテクチャ：
-----------------------------------

#### 開発環境

- 言語：Java デフォルト JDK17（JDK8、JDK21もサポート）

- IDE（Java）：IDEA（lombokプラグインのインストールが必須）

- IDE（フロントエンド）：VSCode、WebStorm、IDEA

- 依存関係管理：Maven

- キャッシュ：Redis

- データベース：MySQL5.7+ [その他のデータベース](https://my.oschina.net/jeecg/blog/4905722)


#### バックエンド

- 基盤フレームワーク：Spring Boot 2.7.18

- マイクロサービスフレームワーク：Spring Cloud Alibaba 2021.0.6.2

- 永続化レイヤーフレームワーク：MybatisPlus 3.5.3.2

- レポートツール：JimuReport 1.9.5

- セキュリティフレームワーク：Apache Shiro 1.13.0、Jwt 4.5.0

- マイクロサービス技術スタック：Spring Cloud Alibaba、Nacos、Gateway、Sentinel、Skywalking

- データベース接続プール：Alibaba Druid 1.1.24

- ログ出力：logback

- その他：autopoi、fastjson、poi、Swagger-ui、quartz、lombok（コード簡素化）など


#### フロントエンド

- 技術スタック：`Vue3.0+TypeScript+Vite+AntDesignVue+pinia+echarts`

#### フロントエンド環境要件

*    `Node.js、npm、pnpm`
*   pnpm `v9+` が必要です。
*   Node.js 推奨バージョン：`v20.15.0`
 ` （Vite6以降、Node.js 18/20以上が必要です）`
 

#### 対応データベース

|  データベース   |  対応状況   |
| --- | --- |
|   MySQL   |  √   |
|  Oracle11g   |  √   |
|  SQL Server 2017   |  √   |
|   PostgreSQL   |  √   |
|   MariaDB   |  √   |
|   達夢（DM）   |  √   |
|   人大金倉（KingbaseES）   |  √   |
|   TiDB   |  √   |


#### AI対応

| AIモデル | 対応状況 |
| --- | --- |
| DeepSeek | √ |
| ChatGPT | √ |
| Qwq | √ |
| 智庫 | √ |
| Ollamaローカル大規模モデル | √ |
| その他 | √ |


AI設定：https://help.jeecg.com/java/ai/aichat

AIアプリ：https://help.jeecg.com/aigc


## マイクロサービスソリューション

- 1. サービス登録と検出 Nacos √
- 2. 設定センター Nacos √
- 3. ルートゲートウェイ Gateway（3つのロードモード） √
- 4. 分散HTTP Feign √
- 5. サーキットブレーカー・デグレード・レート制限 Sentinel √
- 6. 分散ファイル Minio および Alibaba OSS √
- 7. 統一権限制御
- 8. サービス監視 SpringBootAdmin √
- 9. リンクトレーシング Skywalking [参考ドキュメント](https://help.jeecg.com/java/springcloud/super/skywarking)
- 10. メッセージングミドルウェア RabbitMQ √
- 11. 分散タスク xxl-job √
- 12. 分散トランザクション Seata
- 13. 分散ログ Loki+Grafana
- 14. docker-compose、k8s、jenkinsに対応
- 15. CAS SSO √
- 16. ルートレート制限 √

   
#### マイクロサービスアーキテクチャ図
![マイクロサービスアーキテクチャ図](https://jeecgos.oss-cn-beijing.aliyuncs.com/files/jeecgboot_springcloud2022.png "マイクロサービスアーキテクチャ図")

### JeecgBoot 製品機能ブループリント
![機能ブループリント](https://jeecgos.oss-cn-beijing.aliyuncs.com/upload/test/Jeecg-Boot-lantu202005_1590912449914.jpg "機能ブループリント")

### クイックスタート
- マイクロサービス開発：[モノリスからマイクロサービスへのアップグレード](https://help.jeecg.com/java/springcloud/switchcloud/monomer)
- [Dockerでマイクロサービスバックエンドを起動](https://help.jeecg.com/java/docker/springcloud)


### システム画面

##### ChatGPT AIダイアログ
> JeecgBootのバックエンドホームページに移動し、ホームページ右側中央の「AIアシスタント」をクリックすると、AIアシスタントのダイアログ画面が表示されます。
![](https://oscimg.oschina.net/oscnet/up-7c6405641a40f56638999d52da0cb5b4343.png)


##### PC

##### AIモデルとアプリケーション管理

![](https://oscimg.oschina.net/oscnet/up-0b1779e923566ccebb2d5a9cc9220c78b4a.png)

![](https://oscimg.oschina.net/oscnet/up-c8956df1d37d66b2d40136afaeca677628b.png)

![](https://oscimg.oschina.net/oscnet/up-8c348eeafd89673ca8cd1a2705014e3ac04.png)

AIワークフローオーケストレーション

![](https://oscimg.oschina.net/oscnet/up-2343657de2c7ac8010bc471470d084075ae.png)

MCPとツール管理

![](https://oscimg.oschina.net/oscnet/up-8119d5dbc72e534236a3d042e11534c52ad.png)

AIナレッジベース（各種ドキュメント形式に対応、優れたMarkdown互換性）

![](https://oscimg.oschina.net/oscnet/up-e2e9c118982ea366ed7f2b9827d4bb46c5d.png)

AIツールボックス

![](https://oscimg.oschina.net/oscnet/up-bf2a808d22a11fd83e577ad74741d97884b.png)

AIチャットアシスタント

![](https://oscimg.oschina.net/oscnet/up-2a51accc2ff0b647e0ee058a58d291fe811.png)

![](https://oscimg.oschina.net/oscnet/up-ea1069c2a92a3ab2963d88763016cb037c2.png)

![](https://oscimg.oschina.net/oscnet//65298d5710b4e6039a5f802b5f8505c5.png)



![](https://oscimg.oschina.net/oscnet/up-000530d95df337b43089ac77e562494f454.png)

![](https://static.oschina.net/uploads/img/201904/14155402_AmlV.png)

![](https://oscimg.oschina.net/oscnet/up-9d6f36f251e71a0b515a01323474b03004c.png)

![](https://static.oschina.net/uploads/img/201904/14160813_KmXS.png)

![](https://static.oschina.net/uploads/img/201904/14160935_Nibs.png)

![](https://static.oschina.net/uploads/img/201904/14161004_bxQ4.png)

##### インタラクティブ
![](https://oscimg.oschina.net/oscnet/up-78b151fc888d4319377bf1cc311fe826871.png)

![](https://oscimg.oschina.net/oscnet/up-16c07e000278329b69b228ae3189814b8e9.png)


##### プロセスデザイナー
![](https://oscimg.oschina.net/oscnet/up-981ce174e4fbb48c8a2ce4ccfd7372e2994.png)

![](https://static.oschina.net/uploads/img/201907/05165142_yyQ7.png)

![](https://static.oschina.net/uploads/img/201904/14160917_9Ftz.png)

![](https://static.oschina.net/uploads/img/201904/14160633_u59G.png)

##### ミニプロセス

![](https://oscimg.oschina.net/oscnet/up-1dc0d052149ec675f3e4fad632b82b48add.png)

![](https://oscimg.oschina.net/oscnet/up-de31bc2f9d9b8332c554b0954cc73d79593.png)

![](https://oscimg.oschina.net/oscnet/up-7f83b25159663686d67ed080eb16068c3b4.png)

##### ダッシュボードデザイナー


![](https://jeecgos.oss-cn-beijing.aliyuncs.com/files/darg20240726105556.png)

![](https://jeecgos.oss-cn-beijing.aliyuncs.com/files/drag20240724135626.png)

![](https://jeecgos.oss-cn-beijing.aliyuncs.com/files/drag20240724135619.png)

![](https://jeecgos.oss-cn-beijing.aliyuncs.com/files/drag20240724135630.png)

![](https://jeecgos.oss-cn-beijing.aliyuncs.com/files/drag20240726105547.png)

![](https://oscimg.oschina.net/oscnet/up-fad98d42b2cf92f92a903c9cff7579f18ec.png)



##### レポートデザイナー
![](https://oscimg.oschina.net/oscnet/up-64648de000851f15f6c7b9573d107ebb5f8.png)

![](https://oscimg.oschina.net/oscnet/up-fa52b44445db281c51d3f267dce7450d21b.gif)

![](https://oscimg.oschina.net/oscnet/up-68a19149d640f1646c8ed89ed4375e3326c.png)

![](https://oscimg.oschina.net/oscnet/up-f7e9cb2e3740f2d19ff63b40ec2dd554f96.png)

##### フォームデザイナー
![](https://oscimg.oschina.net/oscnet/up-5f8cb657615714b02190b355e59f60c5937.png)

![](https://oscimg.oschina.net/oscnet/up-d9659b2f324e33218476ec98c9b400e6508.png)

![](https://oscimg.oschina.net/oscnet/up-4868615395272d3206dbb960ade02dbc291.png)

##### 大画面デザイナー
![](https://oscimg.oschina.net/oscnet/up-402a6034124474bfef8dfc5b4b2bac1ce5c.png)

![](https://oscimg.oschina.net/oscnet/up-6f7ba2e2ebbeea0d203db8d69fd87644c9f.png)

![](https://oscimg.oschina.net/oscnet/up-ee8d34f318da466b8a6070a6e3111d12ce7.png)

![](https://oscimg.oschina.net/oscnet/up-6b81781b43086819049c4421206810667c5.png)

##### UniApp
![](https://oscimg.oschina.net/oscnet/up-aac943fbd26561879c57a41f7a406edf274.png)

![](https://oscimg.oschina.net/oscnet/up-9a44ba2e82b09c750629d12fafd7f60f553.png)

##### ローコードアプリ
![](https://oscimg.oschina.net/oscnet/up-4be29ae761b2615c8c54b3f668cd8432d9b.png)

![](https://oscimg.oschina.net/oscnet/up-787e76bc24b38ecc7ed19f338808d128255.png)

![](https://oscimg.oschina.net/oscnet/up-99d24a236c483362868523ad0d90f611487.png)

![](https://oscimg.oschina.net/oscnet/up-339a0f29d10449abc7724e3bcda802761c1.png)

![](https://oscimg.oschina.net/oscnet/up-b356670cdc14c609958c7619a537397c4b9.png)

##### アプリ
![](https://oscimg.oschina.net/oscnet/da543c5d0d57baab0cecaa4670c8b68c521.jpg)
![](https://oscimg.oschina.net/oscnet/fda4bd82cab9d682de1c1fbf2060bf14fa6.jpg)

##### PAD
![](https://oscimg.oschina.net/oscnet/e90fef970a8c33790ab03ffd6c4c7cec225.jpg)
![](https://oscimg.oschina.net/oscnet/d78218803a9e856a0aa82b45efc49849a0c.jpg)
![](https://oscimg.oschina.net/oscnet/59c23b230f52384e588ee16309b44fa20de.jpg)


##### チャート
![](https://oscimg.oschina.net/oscnet/up-218bc6a1669496b241ebb23506440c0083e.png)

![](https://static.oschina.net/uploads/img/201904/14160834_Lo23.png)
![](https://static.oschina.net/uploads/img/201904/14160842_QK7B.png)
![](https://static.oschina.net/uploads/img/201904/14160849_GBm5.png)
![](https://static.oschina.net/uploads/img/201904/14160858_6RAM.png)

##### Swagger
![](https://static.oschina.net/uploads/img/201908/27095258_M2Xq.png)
![](https://static.oschina.net/uploads/img/201904/14160957_hN3X.png)


## 寄付

よろしければ、作者にコーヒーを一杯おごってください ☺

![](https://static.oschina.net/uploads/img/201903/08155608_0EFX.png)


## 免責事項

JeecgBoot は [Apache License 2.0](./LICENSE) の下で公開されています。本ソフトウェアをご利用になる前に、以下の事項を必ずご確認ください：

1. **「現状有姿」での提供**  
   本ソフトウェアおよび関連ドキュメントは「現状有姿（AS IS）」で提供され、商品性、特定目的への適合性、非侵害性を含め、明示・黙示を問わずいかなる保証も行いません。

2. **自己責任での利用**  
   ご利用者は、本ソフトウェアがご自身の業務要件を満たすか否かを独自にご評価いただき、ダウンロード・インストール・使用・改変または二次開発に起因するすべてのリスク（データ損失、業務中断、利益損失など一切の損害を含む）を自ら負担するものとします。

3. **責任の制限**  
   適用法令が認める最大限の範囲において、JeecgBoot の作者、貢献者および著作権者は、本ソフトウェアの使用に起因して利用者または第三者に生じたあらゆる直接的、間接的、偶発的、特別的、懲罰的または結果的損害について、一切の責任を負いません。

4. **コミュニティへのお願い**  
   Apache License 2.0 に従って本ソフトウェアを自由にご利用・改変・再配布いただけますが、派生作品において JeecgBoot 公式版を直接複製したり、公式版を装ったり、JeecgBoot と極めて類似した名称・ブランドで公開することは、何卒お控えいただきますようお願いいたします。

本ソフトウェアをダウンロード、複製、インストール、またはその他の方法で使用された場合、上記の免責事項を読み、理解し、同意されたものとみなします。

