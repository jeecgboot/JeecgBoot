<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
</head>

<body>
<div  class="box-content">
    <div  class="info-top">
        <img src="https://qiaoqiaoyun.oss-cn-beijing.aliyuncs.com/site/qqyunemaillogo.png" style="width: 35px;height:35px; background: #5e8ee5; border-radius: 5px;" />
        <div style="color:#fff;">
            <strong>【重要】新数据提醒</strong>
        </div>
    </div>
    <div class="info-wrap">
        <div class="tips" style="padding:15px;">
            <p style="margin: 10px 0;">
                尊敬的 ${userName} 用户,您好：
            </p>
            你的表单  <a style="color: #006eff;" href="${formLink}" target="_blank" rel="noopener">【${formName}】</a>
            在 ${createTime} 新增了1条数据。

            ${dataMarkdown}

            <p>
                如需查看更多请点击
                <a style="color: #006eff;" href="${moreLink}" target="_blank" rel="noopener">[查看所有数据]</a>
            </p>
        </div>
        <div class="footer">敲敲云平台</div>
        <div class="footer" id="currentTime"></div>
    </div>
    <div style="width: 600px; margin: 0 auto;  margin-top: 50px; font-size: 12px; -webkit-font-smoothing: subpixel-antialiased; text-size-adjust: 100%;">
        <p style="text-align: center; line-height: 20.4px; text-size-adjust: 100%; font-family: 'Microsoft YaHei'!important; padding: 0px !important; margin: 0px !important; color: #7e8890 !important;">
            <span class="appleLinks">Copyright © 2023-2024 北京敲敲云科技有限公司. 保留所有权利。</span>
        </p>
        <p style="text-align: center;line-height: 20.4px; text-size-adjust: 100%; font-family: 'Microsoft YaHei'!important; padding: 0px !important; margin: 0px; color: #7e8890 !important; margin-top: 10px;">
            <span class="appleLinks">邮件由系统自动发送，请勿直接回复本邮件！</span>
        </p>
    </div>
</div>
</body>

<style>
    .box-content{
        width: 80%;
        margin: 20px auto;
        max-width: 800px;
        min-width: 600px;
    }

    .info-top{
        display: flex;
        align-items: center;
        padding: 15px 25px;
        border-top-left-radius: 10px;
        border-top-right-radius: 10px;
        background: #4ea3f2;
        color: #fff;
        overflow: hidden;
        line-height: 32px;
    }

    .info-wrap{
        border-bottom-left-radius: 10px;
        border-bottom-right-radius: 10px;
        border:1px solid #ddd;
        overflow: hidden;
        padding: 15px 15px 20px;
    }

    .footer{
        text-align: right;
        color: #999;
        padding: 0 15px 15px;
    }
</style>

</html>