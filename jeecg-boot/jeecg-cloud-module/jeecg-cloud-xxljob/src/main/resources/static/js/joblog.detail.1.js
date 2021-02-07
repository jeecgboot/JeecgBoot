$(function() {

    // trigger fail, end
    if ( !(triggerCode == 200 || handleCode != 0) ) {
        $('#logConsoleRunning').hide();
        $('#logConsole').append('<span style="color: red;">'+ I18n.joblog_rolling_log_triggerfail +'</span>');
        return;
    }

    // pull log
    var fromLineNum = 1;    // [from, to], start as 1
    var pullFailCount = 0;
    function pullLog() {
        // pullFailCount, max=20
        if (pullFailCount++ > 20) {
            logRunStop('<span style="color: red;">'+ I18n.joblog_rolling_log_failoften +'</span>');
            return;
        }

        // load
        console.log("pullLog, fromLineNum:" + fromLineNum);

        $.ajax({
            type : 'POST',
            async: false,   // sync, make log ordered
            url : base_url + '/joblog/logDetailCat',
            data : {
                "executorAddress":executorAddress,
                "triggerTime":triggerTime,
                "logId":logId,
                "fromLineNum":fromLineNum
            },
            dataType : "json",
            success : function(data){

                if (data.code == 200) {
                    if (!data.content) {
                        console.log('pullLog fail');
                        return;
                    }
                    if (fromLineNum != data.content.fromLineNum) {
                        console.log('pullLog fromLineNum not match');
                        return;
                    }
                    if (fromLineNum > data.content.toLineNum ) {
                        console.log('pullLog already line-end');

                        // valid end
                        if (data.content.end) {
                            logRunStop('<br><span style="color: green;">[Rolling Log Finish]</span>');
                            return;
                        }

                        return;
                    }

                    // append content
                    fromLineNum = data.content.toLineNum + 1;
                    $('#logConsole').append(data.content.logContent);
                    pullFailCount = 0;

                    // scroll to bottom
                    scrollTo(0, document.body.scrollHeight);        // $('#logConsolePre').scrollTop( document.body.scrollHeight + 300 );

                } else {
                    console.log('pullLog fail:'+data.msg);
                }
            }
        });
    }

    // pull first page
    pullLog();

    // handler already callback, end
    if (handleCode > 0) {
        logRunStop('<br><span style="color: green;">[Load Log Finish]</span>');
        return;
    }

    // round until end
    var logRun = setInterval(function () {
        pullLog()
    }, 3000);
    function logRunStop(content){
        $('#logConsoleRunning').hide();
        logRun = window.clearInterval(logRun);
        $('#logConsole').append(content);
    }

});
