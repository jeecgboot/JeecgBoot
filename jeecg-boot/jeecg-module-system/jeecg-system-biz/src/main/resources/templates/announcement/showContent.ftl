<!DOCTYPE html>
<html lang="zh">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge，chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <title>通告详情</title>
    <style>

        body {
            margin: 0;
            padding: 20px 16px 12px;
            background-color: #fafafa;
        }

        .rich_media_title {
            font-size: 22px;
            line-height: 1.4;
            margin: 0 0 14px;
        }

        .meta_content {
            margin-bottom: 22px;
            line-height: 20px;
            font-size: 0;
            word-wrap: break-word;
            -webkit-hyphens: auto;
            -ms-hyphens: auto;
            hyphens: auto;
        }

        .rich_media_meta {
            display: inline-block;
            vertical-align: middle;
            margin: 0 10px 10px 0;
            font-size: 15px;
            -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
        }

        .rich_media_meta.priority {
            padding: 0 4px;
            font-size: 12px;
            line-height: 1.67;
            border: 1px solid #d9d9d9;
            border-radius: 4px;
            -moz-border-radius: 4px;
            -webkit-border-radius: 4px;
            width: auto;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            word-wrap: normal;
            max-width: 70%;
            font-style: normal;
            letter-spacing: normal;
            background: rgba(0, 0, 0, 0.05);
            color: rgba(0, 0, 0, 0.3);
            margin-right: 8px;
        }

        .rich_media_meta.H {
            color: #f5222d;
            background: #fff1f0;
            border-color: #ffcfbf;
        }


        .rich_media_meta.M {
            color: #fa8c16;
            background: #fff7e6;
            border-color: #ffe59a;
        }

        .rich_media_meta.text {
            color: rgba(0, 0, 0, 0.3);
        }

        img {
            max-width: 100%;
            height: auto;
        }

        /* 滚动条优化 start */
        ::-webkit-scrollbar{
            width:8px;
            height:8px;
        }
        ::-webkit-scrollbar-track{
            background: #f6f6f6;
            border-radius:2px;
        }
        ::-webkit-scrollbar-thumb{
            background: #cdcdcd;
            border-radius:2px;
        }
        ::-webkit-scrollbar-thumb:hover{
            background: #747474;
        }
        ::-webkit-scrollbar-corner {
            background: #f6f6f6;
        }
        /* 滚动条优化 end */

    </style>
</head>
<body>
<div>
    <h2 class="rich_media_title">${data.titile}</h2>
    <div class="meta_content">
        <#if data.priority??>
            <span class="rich_media_meta priority ${data.priority}">
                <#if data.priority == "H">
                    高
                <#elseif data.priority == "M">
                    中
                <#elseif data.priority == "L">
                    低
                <#else >
                    ${data.priority}
                </#if>
            </span>
        </#if>
        <#if data.sender??>
        <span class="rich_media_meta text">${data.sender}</span>
        </#if>
        <#if data.sendTime??>
        <span class="rich_media_meta text">${data.sendTime?string('yyyy年MM月dd日')}</span>
        </#if>
    </div>
</div>
<div id="content"></div>
<script>
    //update-begin-author:liusq---date:2023-10-30--for: 【QQYUN-6802】查看公告详情，此段端渲染有问题
    // 获取富文本内容的容器元素
    let contentContainer = document.getElementById('content');

    // 富文本内容
    let richText = `${data.msgContent!""}`;
    if(richText.includes("&lt;") || richText.includes("&gt;")){
        richText = richText.replace(/&lt;/g, '<').replace(/&gt;/g, '>');
    }
    // 将富文本内容插入到容器中
    contentContainer.innerHTML = richText;
    // 找到所有的iframe元素
    let iframes = contentContainer.getElementsByTagName('iframe');

    // 动态加载和渲染每个iframe
    Array.prototype.forEach.call(iframes, function(iframe) {
        iframe.onload = function() {
            // iframe加载完成后，调整它的高度以适应内容
            iframe.style.width = '100%';
            iframe.style.height = iframe.contentWindow.document.body.scrollHeight +'px';
        };
        iframe.src = iframe.getAttribute('src');
    });
    //update-end-author:liusq---date:2023-10-30--for: 【QQYUN-6802】查看公告详情，此段端渲染有问题
</script>
</body>
</html>