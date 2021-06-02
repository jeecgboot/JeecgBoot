export const WEEK_MAP_EN = {
  'SUN': '0',
  'MON': '1',
  'TUE': '2',
  'WED': '3',
  'THU': '4',
  'FRI': '5',
  'SAT': '6'
}

export const replaceWeekName = (c) => {
  // console.info('after: ' + c)
  if (c) {
    Object.keys(WEEK_MAP_EN).forEach(k => {
      c = c.replace(new RegExp(k, 'g'), WEEK_MAP_EN[k])
    })
    c = c.replace(new RegExp('7', 'g'), '0')
  }
  // console.info('after: ' + c)
  return c
}
