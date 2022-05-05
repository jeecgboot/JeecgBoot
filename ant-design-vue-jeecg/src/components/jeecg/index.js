import JModal from './JModal'
import JFormContainer from './JFormContainer.vue'
import JPopup from './JPopup.vue'
import JMarkdownEditor from './JMarkdownEditor'
import JCodeEditor from './JCodeEditor.vue'
import JEditor from './JEditor.vue'
import JEditableTable from './JEditableTable.vue'
import JAreaLinkage from './JAreaLinkage.vue'
import JSuperQuery from './JSuperQuery.vue'
import JUpload from './JUpload.vue'
import JTreeSelect from './JTreeSelect.vue'
import JCategorySelect from './JCategorySelect.vue'
import JImageUpload from './JImageUpload.vue'
import JImportModal from './JImportModal.vue'
import JTreeDict from './JTreeDict.vue'
import JCheckbox from './JCheckbox.vue'
import JCron from './JCron.vue'
import JDate from './JDate.vue'
import JEllipsis from './JEllipsis.vue'
import JInput from './JInput.vue'
import JPopupOnlReport from './modal/JPopupOnlReport.vue'
import JFilePop from './minipop/JFilePop.vue'
import JInputPop from './minipop/JInputPop.vue'
import JSelectMultiple from './JSelectMultiple.vue'
import JSlider from './JSlider.vue'
import JSwitch from './JSwitch.vue'
import JTime from './JTime.vue'
import JTreeTable from './JTreeTable.vue'
import JEasyCron from '@/components/jeecg/JEasyCron'
//jeecgbiz
import JSelectDepart from '../jeecgbiz/JSelectDepart.vue'
import JSelectMultiUser from '../jeecgbiz/JSelectMultiUser.vue'
import JSelectPosition from '../jeecgbiz/JSelectPosition.vue'
import JSelectRole from '../jeecgbiz/JSelectRole.vue'
import JSelectUserByDep from '../jeecgbiz/JSelectUserByDep.vue'
//引入需要全局注册的js函数和变量
import { Modal, notification,message } from 'ant-design-vue'
import lodash_object from 'lodash'
import debounce from 'lodash/debounce'
import pick from 'lodash.pick'
import data from 'china-area-data'

export default {
  install(Vue) {
    Vue.use(JModal)
    Vue.component('JMarkdownEditor', JMarkdownEditor)
    Vue.component('JPopupOnlReport', JPopupOnlReport)
    Vue.component('JFilePop', JFilePop)
    Vue.component('JInputPop', JInputPop)
    Vue.component('JAreaLinkage', JAreaLinkage)
    Vue.component('JCategorySelect', JCategorySelect)
    Vue.component('JCheckbox', JCheckbox)
    Vue.component('JCodeEditor', JCodeEditor)
    Vue.component('JCron', JCron)
    Vue.component('JDate', JDate)
    Vue.component('JEditableTable', JEditableTable)
    Vue.component('JEditor', JEditor)
    Vue.component('JEllipsis', JEllipsis)
    Vue.component('JFormContainer', JFormContainer)
    Vue.component('JImageUpload', JImageUpload)
    Vue.component('JImportModal', JImportModal)
    Vue.component('JInput', JInput)
    Vue.component('JPopup', JPopup)
    Vue.component('JSelectMultiple', JSelectMultiple)
    Vue.component('JSlider', JSlider)
    Vue.component('JSuperQuery', JSuperQuery)
    Vue.component('JSwitch', JSwitch)
    Vue.component('JTime', JTime)
    Vue.component('JTreeDict', JTreeDict)
    Vue.component('JTreeSelect', JTreeSelect)
    Vue.component('JTreeTable', JTreeTable)
    Vue.component('JUpload', JUpload)

    //jeecgbiz
    Vue.component('JSelectDepart', JSelectDepart)
    Vue.component('JSelectMultiUser', JSelectMultiUser)
    Vue.component('JSelectPosition', JSelectPosition)
    Vue.component('JSelectRole', JSelectRole)
    Vue.component('JSelectUserByDep', JSelectUserByDep)
    Vue.component(JEasyCron.name, JEasyCron)

    //注册全局js函数和变量
    Vue.prototype.$Jnotification = notification
    Vue.prototype.$Jmodal = Modal
    Vue.prototype.$Jmessage = message
    Vue.prototype.$Jlodash = lodash_object
    Vue.prototype.$Jdebounce= debounce
    Vue.prototype.$Jpick = pick
    Vue.prototype.$Jpcaa = data
  }
}