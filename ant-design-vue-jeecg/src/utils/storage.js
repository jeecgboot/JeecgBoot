/**
 * Set storage
 *
 * @param name
 * @param content
 * @param maxAge
 */
export const setStore = (name, content, maxAge = null) => {
  if (!global.window || !name) {
    return;
  }

  if (typeof content !== 'string') {
    content = JSON.stringify(content)
  }

  let storage = global.window.localStorage

  storage.setItem(name, content)
  if (maxAge && !isNaN(parseInt(maxAge))) {
    let timeout = parseInt(new Date().getTime() / 1000)
    storage.setItem(`${name}_expire`, timeout + maxAge)
  }
};

/**
 * Get storage
 *
 * @param name
 * @returns {*}
 */
export const getStore = name => {
  if (!global.window || !name) {
    return;
  }

  let content = window.localStorage.getItem(name)
  let _expire = window.localStorage.getItem(`${name}_expire`)

  if (_expire) {
    let now = parseInt(new Date().getTime() / 1000)
    if (now > _expire) {
      return;
    }
  }

  try {
    return JSON.parse(content)
  } catch (e) {
    return content
  }
};

/**
 * Clear storage
 *
 * @param name
 */
export const clearStore = name => {
  if (!global.window || !name) {
    return;
  }

  window.localStorage.removeItem(name)
  window.localStorage.removeItem(`${name}_expire`)
};

/**
 * Clear all storage
 */
export const clearAll = () => {
  if (!global.window || !name) {
    return;
  }

  window.localStorage.clear()
}

