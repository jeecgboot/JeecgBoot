(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? module.exports = factory() :
    typeof define === 'function' && define.amd ? define(factory) :
    (global = global || self, (global.FullCalendarLocales = global.FullCalendarLocales || {}, global.FullCalendarLocales.th = factory()));
}(this, function () { 'use strict';

    var th = {
        code: "th",
        buttonText: {
            prev: "ย้อน",
            next: "ถัดไป",
            today: "วันนี้",
            month: "เดือน",
            week: "สัปดาห์",
            day: "วัน",
            list: "แผนงาน"
        },
        allDayText: "ตลอดวัน",
        eventLimitText: "เพิ่มเติม",
        noEventsMessage: "ไม่มีกิจกรรมที่จะแสดง"
    };

    return th;

}));
