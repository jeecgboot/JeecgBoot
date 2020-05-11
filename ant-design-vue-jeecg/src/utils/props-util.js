/**
 * 该文件截取自 "ant-design-vue/es/_util/props-util.js" 文件，并对其做出特殊修改
 */
function classNames() {
  let classes = []

  for (let i = 0; i < arguments.length; i++) {
    let arg = arguments[i]
    if (!arg) continue

    let argType = typeof arg

    if (argType === 'string' || argType === 'number') {
      classes.push(arg)
    } else if (Array.isArray(arg) && arg.length) {
      let inner = classNames.apply(null, arg)
      if (inner) {
        classes.push(inner)
      }
    } else if (argType === 'object') {
      for (let key in arg) {
        if (arg.hasOwnProperty(key) && arg[key]) {
          classes.push(key)
        }
      }
    }
  }
  return classes.join(' ')
}

const camelizeRE = /-(\w)/g

function camelize(str) {
  return str.replace(camelizeRE, (_, c) => (c ? c.toUpperCase() : ''))
}


function objectCamelize(obj) {
  let res = {}
  Object.keys(obj).forEach(k => (res[camelize(k)] = obj[k]))
  return res
}

function parseStyleText(cssText = '', camel) {
  const res = {}
  const listDelimiter = /;(?![^(]*\))/g
  const propertyDelimiter = /:(.+)/
  cssText.split(listDelimiter).forEach(function (item) {
    if (item) {
      const tmp = item.split(propertyDelimiter)
      if (tmp.length > 1) {
        const k = camel ? camelize(tmp[0].trim()) : tmp[0].trim()
        res[k] = tmp[1].trim()
      }
    }
  })
  return res
}

export function getClass(ele) {
  let data = {}
  if (ele.data) {
    data = ele.data
  } else if (ele.$vnode && ele.$vnode.data) {
    data = ele.$vnode.data
  }
  const tempCls = data.class || {}
  const staticClass = data.staticClass
  let cls = {}
  staticClass &&
  staticClass.split(' ').forEach(c => {
    cls[c.trim()] = true
  })
  if (typeof tempCls === 'string') {
    tempCls.split(' ').forEach(c => {
      cls[c.trim()] = true
    })
  } else if (Array.isArray(tempCls)) {
    classNames(tempCls)
      .split(' ')
      .forEach(c => {
        cls[c.trim()] = true
      })
  } else {
    cls = { ...cls, ...tempCls }
  }
  return cls
}

export function getStyle(ele, camel) {

  getClass(ele)

  let data = {}
  if (ele.data) {
    data = ele.data
  } else if (ele.$vnode && ele.$vnode.data) {
    data = ele.$vnode.data
  }

  // update-begin-author:sunjianlei date:20200303 for: style 和 staticStyle 可以共存
  let style = data.style || {}
  let staticStyle = data.staticStyle
  staticStyle = staticStyle ? objectCamelize(data.staticStyle) : {}
  // update-end-author:sunjianlei date:20200303 for: style 和 staticStyle 可以共存

  if (typeof style === 'string') {
    style = parseStyleText(style, camel)
  } else if (camel && style) {
    // 驼峰化
    style = objectCamelize(style)
  }
  return { ...staticStyle, ...style }
}

