export const WEEK_MAP_EN = {
  'SUN': '1',
  'MON': '2',
  'TUE': '3',
  'WED': '4',
  'THU': '5',
  'FRI': '6',
  'SAT': '7'
}

export const replaceWeekName = (c) => {
  // console.info('after: ' + c)
  if (c) {
    Object.keys(WEEK_MAP_EN).forEach(k => {
      c = c.replace(new RegExp(k, 'g'), WEEK_MAP_EN[k])
    })
    // c = c.replace(new RegExp('7', 'g'), '0')
  }
  // console.info('after: ' + c)
  return c
}
