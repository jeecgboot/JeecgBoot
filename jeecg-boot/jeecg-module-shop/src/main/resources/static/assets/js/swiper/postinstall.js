/* eslint-disable max-len -- for better formatting */
var env = process.env;

var ADBLOCK = is(env.ADBLOCK);
var COLOR = is(env.npm_config_color);
var DISABLE_OPENCOLLECTIVE = is(env.DISABLE_OPENCOLLECTIVE);
var SILENT = ['silent', 'error', 'warn'].indexOf(env.npm_config_loglevel) !== -1;
var OPEN_SOURCE_CONTRIBUTOR = is(env.OPEN_SOURCE_CONTRIBUTOR);

// you could add a PR with an env variable for your CI detection
var CI = [
  'BUILD_NUMBER',
  'CI',
  'CONTINUOUS_INTEGRATION',
  'DRONE',
  'RUN_ID'
].some(function (it) { return is(env[it]); });

var BANNER = '\u001b[35m\u001b[1mLove Swiper? Support Vladimir\'s work by donating or pledging: \u001B[0m\n' +
'\u001b[22m\u001b[39m\u001b[32m> On Patreon https://patreon.com/swiperjs \u001B[0m\n' +
'\u001b[22m\u001b[39m\u001b[32m> On Open Collective https://opencollective.com/swiper';

function is(it) {
  return !!it && it !== '0' && it !== 'false';
}

function isBannerRequired() {
  return !(ADBLOCK || CI || DISABLE_OPENCOLLECTIVE || SILENT || OPEN_SOURCE_CONTRIBUTOR);
}

function showBanner() {
  // eslint-disable-next-line no-console,no-control-regex -- output
  console.log(COLOR ? BANNER : BANNER.replace(/\u001B\[\d+m/g, ''));
}

if (isBannerRequired()) showBanner();
