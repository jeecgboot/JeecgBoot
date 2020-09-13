import { installCell, JVXETypes } from '@/components/jeecg/JVxeTable'
import JVxePopupCell from './JVxePopupCell'
import { DictSearchInputCell, DictSearchSpanCell } from './JVxeSelectDictSearchCell'
import JVxeFileCell from './JVxeFileCell'
import JVxeImageCell from './JVxeImageCell'
import JVxeRadioCell from './JVxeRadioCell'
import JVxeSelectCell from '@comp/jeecg/JVxeTable/components/cells/JVxeSelectCell'
import JVxeTextareaCell from '@comp/jeecg/JVxeTable/components/cells/JVxeTextareaCell'

// 注册online组件
JVXETypes.input_pop = 'input_pop'
JVXETypes.list_multi = 'list_multi'
JVXETypes.sel_search = 'sel_search'
installCell(JVXETypes.input_pop, JVxeTextareaCell)
installCell(JVXETypes.list_multi, JVxeSelectCell)
installCell(JVXETypes.sel_search, JVxeSelectCell)

// 注册【popup】组件（普通封装方式）
JVXETypes.popup = 'popup'
installCell(JVXETypes.popup, JVxePopupCell)

// 注册【字典搜索下拉】组件（高级封装方式）
JVXETypes.selectDictSearch = 'select-dict-search'
installCell(JVXETypes.selectDictSearch, DictSearchInputCell, DictSearchSpanCell)

// 注册【文件上传】组件
JVXETypes.file = 'file'
installCell(JVXETypes.file, JVxeFileCell)

// 注册【图片上传】组件
JVXETypes.image = 'image'
installCell(JVXETypes.image, JVxeImageCell)

// 注册【单选框】组件
JVXETypes.radio = 'radio'
installCell(JVXETypes.radio, JVxeRadioCell)
