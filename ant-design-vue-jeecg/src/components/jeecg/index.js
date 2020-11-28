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
import JTreeDict from './JTreeDict.vue'
import JCheckbox from './JCheckbox.vue'
import JCron from './JCron.vue'
import JSelectMultiple from './JSelectMultiple.vue'
import JPopupOnlReport from './modal/JPopupOnlReport.vue'

export default {
  install(Vue) {
    Vue.component('JFormContainer', JFormContainer)
    Vue.component('JPopup', JPopup)
    Vue.component(JModal.name, JModal)
    Vue.component('JMarkdownEditor', JMarkdownEditor)
    Vue.component('JEditor', JEditor)
    Vue.component('JCodeEditor', JCodeEditor)
    Vue.component('JEditableTable', JEditableTable)
    Vue.component('JAreaLinkage', JAreaLinkage)
    Vue.component('JSuperQuery', JSuperQuery)
    Vue.component('JUpload', JUpload)
    Vue.component('JTreeSelect', JTreeSelect)
    Vue.component('JCategorySelect', JCategorySelect)
    Vue.component('JImageUpload', JImageUpload)
    Vue.component('JTreeDict', JTreeDict)
    Vue.component('JCheckbox', JCheckbox)
    Vue.component('JCron', JCron)
    Vue.component('JPopupOnlReport', JPopupOnlReport)
    Vue.component('JSelectMultiple', JSelectMultiple)
  }
}