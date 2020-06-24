/**
 * LunarFullCalendar 公共 js
 *
 * @version 1.0.0
 * @author sunjianlei
 *
 * */

import { getRefPromise } from '@/utils/JEditableTableUtil'

/* 日历的视图类型 */
const calendarViewType = {
  month: 'month', // 月视图
  basicWeek: 'basicWeek',  // 基础周视图
  basicDay: 'basicDay',//  基础天视图
  agendaWeek: 'agendaWeek', // 议程周视图
  agendaDay: 'agendaDay', // 议程天视图
}

/* 定义默认视图 */
const defaultView = calendarViewType.month

/* 定义日历默认配置 */
const defaultSettings = {
  locale: 'zh-cn',
  // 按钮文字
  buttonText: {
    today: '今天',
    month: '月',
    week: '周',
    day: '日'
  },
  // 头部排列方式
  header: {
    left: 'prev,next, today',
    center: 'title',
    right: 'hide, custom, month,agendaWeek,agendaDay'
  },
  //点击今天日列表图
  eventLimitClick: 'day',
  // 隐藏超出的事件
  eventLimit: true,
  // 设置每周开始日期为周日
  firstDay: 0,
  // 默认显示视图
  defaultView,
  timeFormat: 'H:mm',
  axisFormat: 'H:mm',
  // agenda视图下是否显示all-day
  allDaySlot: true,
  // agenda视图下all-day的显示文本
  allDayText: '全天',
  // 时区默认本地的
  timezone: 'local',
  // 周视图和日视同的左侧时间显示
  slotLabelFormat: 'HH:mm',
  // 设置第二天阈值
  nextDayThreshold: '00:00:00',
}

/** 提供了一些增强方法 */
const CalendarMixins = {
  data() {
    return {
      calenderCurrentViewType: defaultView
    }
  },
  methods: {

    getCalendarConfigEventHandler() {
      return {
        // 处理 view changed 事件
        viewRender: (view, element) => {
          let { type } = view

          let lastViewType = this.calenderCurrentViewType
          this.calenderCurrentViewType = type

          if (typeof this.handleViewRender === 'function') {
            this.handleViewRender(type, view, element)
          }

          if (lastViewType !== this.calenderCurrentViewType && typeof this.handleViewChanged === 'function') {
            this.handleViewChanged(type, view, element)
          }

        },
      }
    },

    /** 获取 LunarFullCalendar 实例，ref = baseCalendar */
    getCalendar(fn) {
      return getRefPromise(this, 'baseCalendar').then(fn)
    },

    calendarEmit(name, data) {
      this.getCalendar(ref => ref.$emit(name, data))
    },

    /** 强制重新加载所有的事件（日程）*/
    calendarReloadEvents() {
      this.calendarEmit('reload-events')
    }
  }
}

export { defaultSettings, calendarViewType, CalendarMixins }