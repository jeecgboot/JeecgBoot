import enquireJs from 'enquire.js'

const enquireScreen = function (call) {
  // tablet
  const handler = {
    match: function () {
      call && call(0)
    },
    unmatch: function () {
      call && call(-1)
    }
  }
  // mobile
  const handler2 = {
    match: () => {
      call && call(1)
    }
  }
  enquireJs.register('screen and (max-width: 1087.99px)', handler)
  enquireJs.register('screen and (max-width: 767.99px)', handler2)
}

export default enquireScreen