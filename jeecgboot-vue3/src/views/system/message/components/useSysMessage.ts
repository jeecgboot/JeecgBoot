import { ref, reactive, nextTick } from 'vue';
import { defHttp } from '/@/utils/http/axios';
import { getDictItemsByCode } from '/@/utils/dict/index';
import { useRouter, useRoute } from 'vue-router'
import { useAppStore } from '/@/store/modules/app';
import { useTabs } from '/@/hooks/web/useTabs';
import { useModal } from '/@/components/Modal';
import {useMessage} from "/@/hooks/web/useMessage";

/**
 * 列表接口
 * @param params
 */
const queryMessageList = (params) => {
  const url = '/sys/annountCement/vue3List';
  return defHttp.get({ url, params });
};

/**
 * 获取消息列表数据
 *
 * setLocaleText 设置未读消息
 */
export function useSysMessage(setLocaleText) {
  const { createMessage } = useMessage();
  const rangeDateArray = getDictItemsByCode('rangeDate');
  console.log('+++++++++++++++++++++');
  console.log('rangeDateArray', rangeDateArray);
  console.log('+++++++++++++++++++++');

  const messageList = ref<any[]>([]);
  const pageNo = ref(1)
  let pageSize = 10;

  const searchParams = reactive({
    fromUser: '',
    rangeDateKey: '',
    rangeDate: [],
    starFlag: '',
    noticeType: ''
  });


  function getQueryParams() {
    let { fromUser, rangeDateKey, rangeDate, starFlag, noticeType } = searchParams;
    let params = {
      fromUser,
      starFlag,
      rangeDateKey,
      beginDate: '',
      endDate: '',
      pageNo: pageNo.value,
      pageSize,
      noticeType
    };
    if (rangeDateKey == 'zdy') {
      params.beginDate = rangeDate[0]+' 00:00:00';
      params.endDate = rangeDate[1]+' 23:59:59';
    }
    return params;
  }

  // 数据是否加载完了
  const loadEndStatus = ref(false);

  //请求数据
  async function loadData() {
    if(loadEndStatus.value === true){
      return;
    }
    let params = getQueryParams();
    const data = await queryMessageList(params);
    console.log('获取结果', data);
    if(!data || data.length<=0){
      loadEndStatus.value = true;
      setLocaleText();
      return;
    }
    if(data.length<pageSize){
      loadEndStatus.value = true;
    }
    pageNo.value = pageNo.value+1
    let temp:any[] = messageList.value;
    temp.push(...data);
    messageList.value = temp;
    setLocaleText();
  }

  //重置
  function reset(){
    messageList.value = []
    pageNo.value = 1;
    loadEndStatus.value = false;
  }

  //标星
  async function updateStarMessage(item){
    const url = '/sys/sysAnnouncementSend/edit';
    let starFlag = '1';
    if(item.starFlag==starFlag){
      starFlag = '0'
    }
    const params = {
      starFlag,
      id: item.sendId
    }
    //update-begin-author:taoyan date:2023-3-6 for: QQYUN-4491【应用】一些小问题  4、标星不需要提示吧
    const data:any = await defHttp.put({url, params}, {isTransformResponse: false});
    if(data.success === true){
    }else{
      createMessage.warning(data.message)
    }
    //update-end-author:taoyan date:2023-3-6 for: QQYUN-4491【应用】一些小问题  4、标星不需要提示吧
  }


  const loadingMoreStatus = ref(false);
  async function onLoadMore() {
    loadingMoreStatus.value = true;
    await loadData();
    loadingMoreStatus.value = false;
  }

  function noRead(item) {
    if (item.readFlag === '1') {
      return false;
    }
    return true;
  }

  // 消息类型
  function getMsgCategory(item) {
    if(item.busType=='email'){
      return '邮件提醒:';
    } else if(item.busType=='bpm'){
      return '流程催办:';
    } else if(item.busType=='bpm_cc'){
      return '流程抄送:';
    }else if(item.busType=='bpm_task'){
      return '流程任务:';
    } else if (item.msgCategory == '2') {
      return '系统消息:';
    } else if (item.msgCategory == '1') {
      return '通知公告:';
    }
    return '';
  }

  // QQYUN-4472 来消息了没有提醒--查看详情改为去处理
  function getHrefText(item) {
    if(item.busType === 'bpm'|| item.busType === 'bpm_task' || item.busType === 'tenant_invite'){
      //判断是否是查看详情
      if (item.msgAbstract) {
        try {
          const json = JSON.parse(item.msgAbstract);
          if (json.taskDetail) {
            return '查看详情';
          }
        } catch (e) {
          console.error('getHrefText:msgAbstract参数不是JSON格式', item.msgAbstract);
        }
      }
      return '去处理'
    }else{
      return '查看详情'
    }
  }

  return {
    messageList,
    reset,
    loadData,
    loadEndStatus,
    searchParams,
    updateStarMessage,
    onLoadMore,
    noRead,
    getMsgCategory,
    getHrefText

  };
}

/**
 * 用于消息跳转
 */
export function useMessageHref(emit, props){
  //const [registerHistoryModal, { openModal: openHistoryModal }] = useModal();
  //const [registerTaskModal, { openModal: openTaskModal }] = useModal();
  // 注册表单弹窗
  //const [registerDesignFormModal, { openModal: openDesignFormModal }] = useModal();
  const messageHrefArray: any[] = getDictItemsByCode('messageHref');
  const router = useRouter();
  const appStore = useAppStore();
  const rt = useRoute();
  const { close: closeTab, closeSameRoute } = useTabs();

  //*********************************[QQYUN-6713]系统通知打开弹窗修改，动态设置弹窗begin******************************************
  //当前表单弹窗
  const currentModal = ref<string | null>(null);
  //当前表单参数
  const modalParams = ref<Recordable>({});
  //表单注册缓存
  const modalRegCache = ref<Recordable>({});
  //组件绑定参数
  const bindParams = ref<Recordable>({});

  /**
   * 根据类型打开不同弹窗
   * @param type
   * @param params
   */
  async function handleOpenType(type, params) {
    currentModal.value = null;
    modalParams.value = { ...params };
    switch (type) {
      case 'task':
        //流程办理
        bindParams.value = { actionType: 'todo' };
        currentModal.value = 'ProcessTaskHandleModal';
        break;
      case 'history':
        bindParams.value = {};
        //历史流程
        currentModal.value = 'MyTaskHandleModal';
        break;
      case 'design':
        //表单设计
        currentModal.value = 'DesformViewModal';
        bindParams.value = {
          showRecordCopy: false,
          showRecordShare: false,
          showRecordSysPrint: false,
          showDesignFormBtn: false,
        };
        break;
      case 'cgform':
        //Online表单
        currentModal.value = 'OnlineAutoModal';
        bindParams.value = {
          id: params.formId,
        }
        break;
      default:
        currentModal.value = null;
        break;
    }
    //注册表单弹窗
    initModalRegister();
    await nextTick(() => {
      if (modalRegCache.value[currentModal.value!]?.isRegister) {
        console.log('已注冊，走缓存');
        modalRegCache.value[currentModal.value!].modalMethods.openModal(true, modalParams.value);
      }
    });
  }

  /**
   * 初始化弹窗注册
   */
  function initModalRegister() {
    //如果当前选择表单为null，就不处理
    if (!currentModal.value) {
      return;
    }
    //判断缓存中是否存在，不存在就走缓存逻辑
    if (!modalRegCache.value[currentModal.value]) {
      const [registerModal, modalMethods] = useModal();
      modalRegCache.value[currentModal.value] = {
        isRegister: false,
        register: bindRegisterModal(registerModal, modalMethods),
        modalMethods,
      };
    }
  }

  /**
   * 绑定注册弹窗
   * @param regFn
   * @param modalMethod
   */
  function bindRegisterModal(regFn, modalMethod) {
    return async (...args) => {
      console.log('开始注册：', currentModal.value);
      await regFn(...args);
      console.log('注册完成：', currentModal.value);
      //打开弹窗
      modalMethod.openModal(true, modalParams.value);
      //设置缓存标识
      modalRegCache.value[currentModal.value!].isRegister = true;
    };
  }
  //*************************************[QQYUN-6713]系统通知打开弹窗修改，动态设置弹窗end*********************************************
  // const defaultPath = '/monitor/mynews';
  //const bpmPath = '/task/handle/'

  async function goPage(record, openModalFun?){
    if(!record.busType || record.busType == 'msg_node'){
      if(!openModalFun){
        // 从首页的消息通知跳转
        await goPageFromOuter(record);
      }else{
        // 从消息页面列表点击详情查看 直接打开modal
        openModalFun()
      }
      // update-begin-author:taoyan date:2023-5-10 for: QQYUN-4744【系统通知】6、系统通知@人后，对方看不到是哪个表单@的，没有超链接
    }else if(record.busType == 'comment'){
      // de
      let msgAbstract = record.msgAbstract;
      if(msgAbstract){
        try {
          let data = JSON.parse(msgAbstract.toString());
          if(data.type == 'designForm'){
            showDesignFormModal(data);
          } else {
            showOnlineCgformModal(data);
          }
        }catch (e) {
          console.error('打开评论表单，但是msgAbstract参数不是JSON格式', msgAbstract)
          if(openModalFun){
            openModalFun();
          }
        }
      }
      // update-end-author:taoyan date:2023-5-10 for: QQYUN-4744【系统通知】6、系统通知@人后，对方看不到是哪个表单@的，没有超链接
    }else if(record.busType == 'tenant_invite'){
      if(props.isLowApp===true){
        router.push({ name:"myapps-settings-user", query:{ page:'tenantSetting' }})
      }else{
        router.push({ name:"system-usersetting", query:{ page:'tenantSetting' }})
      }
    }else{
      if(props && props.isLowApp===true){
        openLowAppFlowModal(record)
      }else{
        await goPageWithBusType(record)
      }
    }
/*    busId: "1562035005173587970"
    busType: "email"
    openPage: "modules/eoa/email/modals/EoaEmailInForm"
    openType: "component"*/
  }

  /**
   * 打开表单设计器 表单弹窗
   * @param data
   */
  function showDesignFormModal(data) {
    handleOpenType('design', {
      mode: 'detail',
      desformCode: data.code,
      dataId: data.dataId,
      isOnline: false,
    });
  }

  /**
   * 打开Online表单 弹窗
   * @param data
   */
  function showOnlineCgformModal(data) {
    handleOpenType('cgform', {
      formId: data.formId,
      isUpdate: true,
      disableSubmit: true,
      record: {
        id: data.dataId,
      },
    });
  }

  /**
   * 判断是不是表单的评论消息
   * @param record
   */
  function isFormComment(record) {
    if(record.busType == 'comment'){
      let msgAbstract = record.msgAbstract;
      if(msgAbstract){
        try {
          let data = JSON.parse(msgAbstract);
          if(['cgform', 'designForm'].includes(data.type)){
            return true
          }
        }catch (e) {
          console.error('打开评论表单，但是msgAbstract参数不是JSON格式', msgAbstract)
        }
      }
    }
    return false
  }

  /**
   * 如果是工作流任务 在lowApp中 直接打开modal
   */
  function openLowAppFlowModal(record){
    const { busType, busId, msgAbstract } = record;
    let temp = messageHrefArray.filter(item=>item.value === busType);
    if(!temp || temp.length==0){
      console.error('当前业务类型不识别', busType);
      return;
    }
    if(busType.indexOf('bpm')<0){
      console.error('low-app不支持跳转邮箱', busType);
      return;
    }
    //固定参数 detailId 用于查询表单数据
    let query:any = {
      detailId: busId
    };
    // 额外参数处理
    if(msgAbstract){
      try {
        let json = JSON.parse(msgAbstract);
        Object.keys(json).map(k=>{
          query[k] = json[k]
        });
      }catch (e) {
        console.error('msgAbstract参数不是JSON格式', msgAbstract)
      }
    }
    console.log("busType = ", busType)
    handleOpenType('task', {
      record: {
        id: busId,
        procInsId: query.procInsId,
        processDefinitionId: query.processDefinitionId,
        isDetail: query.taskDetail || 'bpm_cc' == busType
      }
    })
  }

  /**
   * 根据busType不同跳转不同页面
   * @param record
   */
  async function goPageWithBusType(record){
    const { busType, busId, msgAbstract } = record;
    let temp = messageHrefArray.filter(item=>item.value === busType);
    if(!temp || temp.length==0){
      console.error('当前业务类型不识别', busType);
      return;
    }
    let path = temp[0].text;
    path = path.replace('{DETAIL_ID}', busId)
    //固定参数 detailId 用于查询表单数据
    let query:any = {
      detailId: busId
    };
    // 额外参数处理
    if(msgAbstract){
      try {
        let json = JSON.parse(msgAbstract);
        Object.keys(json).map(k=>{
          query[k] = json[k]
        });
      }catch (e) {
        console.error('msgAbstract参数不是JSON格式', msgAbstract)
      }
    }
    if(query.taskDetail){
      // 查看任务详情的弹窗
      await showHistory(query.procInsId)
    }else{
      // 跳转路由
      appStore.setMessageHrefParams(query);
      if(rt.path.indexOf(path)>=0){
        await closeTab();
        await router.replace({ path: path, query:{ time: new Date().getTime() } });
      }else{
        closeSameRoute(path)
        await router.push({ path: path });
      }
    }
  }

  /**
   * 从首页的消息通知跳转消息列表打开modal
   * @param record
   */
  async function goPageFromOuter(record){
    //没有定义业务类型 直接跳转我的消息页面
    emit('detail', record)
  }

  //===============================================================================================================
  //update-begin-author:taoyan date:2022-12-31 for:   QQYUN-3485 【查看流程】做一个查看页面，非办理页面，只通过流程实例参数即可
  async function showHistory(processInstanceId) {
    let { formData, formUrl } = await getTaskInfoForHistory({ processInstanceId });
    formData['PROCESS_TAB_TYPE'] = 'history';
    handleOpenType('history', {
      formData,
      formUrl,
      title: '流程历史',
    });
  }

  const nodeInfoUrl = '/act/process/extActProcessNode/getHisProcessNodeInfo'
  const taskNodeInfo = (params) => defHttp.get({ url: nodeInfoUrl, params });

  async function getTaskInfoForHistory(record) {
    //查询条件
    let params = { procInstId: record.processInstanceId };
    const result = await taskNodeInfo(params);
    console.log('获取历史任务信息', result);
    let formData: any = {
      dataId: result.dataId,
      taskId: record.id,
      taskDefKey: record.taskId,
      procInsId: record.processInstanceId,
      tableName: result.tableName,
      vars: result.records,
    };
    let tempFormUrl = result.formUrl;
    console.log('获取流程节点表单URL', tempFormUrl);
    //节点配置表单URL，VUE组件类型对应的拓展参数
    if (tempFormUrl && tempFormUrl.indexOf('?') != -1 && !isURL(tempFormUrl) && tempFormUrl.indexOf('{{DOMAIN_URL}}') == -1) {
      tempFormUrl = result.formUrl.split('?')[0];
      console.log('获取流程节点表单URL（去掉参数）', tempFormUrl);
      formData.extendUrlParams = getQueryVariable(result.formUrl);
    }
    return {
      formData,
      formUrl: tempFormUrl,
    };
  }

  /**
   * 获取URL上参数
   * @param url
   */
  function getQueryVariable(url) {
    if (!url) return;

    let t,
      n,
      r,
      i = url.split('?')[1],
      s = {};
    (t = i.split('&')), (r = null), (n = null);
    for (let o in t) {
      let u = t[o].indexOf('=');
      u !== -1 && ((r = t[o].substr(0, u)), (n = t[o].substr(u + 1)), (s[r] = n));
    }
    return s;
  }

  /**
   * URL地址
   * @param {*} s
   */
  function isURL(s) {
    return /^http[s]?:\/\/.*/.test(s);
  }
  //update-end-author:taoyan date:2022-12-31 for:   QQYUN-3485 【查看流程】做一个查看页面，非办理页面，只通过流程实例参数即可
  //===============================================================================================================

  return {
    goPage,
    isFormComment,
    modalRegCache,
    currentModal,
    bindParams,
  }
}
