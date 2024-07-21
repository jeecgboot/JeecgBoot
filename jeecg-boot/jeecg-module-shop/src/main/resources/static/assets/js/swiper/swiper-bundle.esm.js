/**
 * Swiper 7.2.0
 * Most modern mobile touch slider and framework with hardware accelerated transitions
 * https://swiperjs.com
 *
 * Copyright 2014-2021 Vladimir Kharlampidi
 *
 * Released under the MIT License
 *
 * Released on: October 27, 2021
 */

import Swiper from './core/core.js';
export { default as Swiper, default } from './core/core.js';
import Virtual from './modules/virtual/virtual.js';
import Keyboard from './modules/keyboard/keyboard.js';
import Mousewheel from './modules/mousewheel/mousewheel.js';
import Navigation from './modules/navigation/navigation.js';
import Pagination from './modules/pagination/pagination.js';
import Scrollbar from './modules/scrollbar/scrollbar.js';
import Parallax from './modules/parallax/parallax.js';
import Zoom from './modules/zoom/zoom.js';
import Lazy from './modules/lazy/lazy.js';
import Controller from './modules/controller/controller.js';
import A11y from './modules/a11y/a11y.js';
import History from './modules/history/history.js';
import HashNavigation from './modules/hash-navigation/hash-navigation.js';
import Autoplay from './modules/autoplay/autoplay.js';
import Thumbs from './modules/thumbs/thumbs.js';
import FreeMode from './modules/free-mode/free-mode.js';
import Grid from './modules/grid/grid.js';
import Manipulation from './modules/manipulation/manipulation.js';
import EffectFade from './modules/effect-fade/effect-fade.js';
import EffectCube from './modules/effect-cube/effect-cube.js';
import EffectFlip from './modules/effect-flip/effect-flip.js';
import EffectCoverflow from './modules/effect-coverflow/effect-coverflow.js';
import EffectCreative from './modules/effect-creative/effect-creative.js';
import EffectCards from './modules/effect-cards/effect-cards.js';

// Swiper Class
const modules = [Virtual, Keyboard, Mousewheel, Navigation, Pagination, Scrollbar, Parallax, Zoom, Lazy, Controller, A11y, History, HashNavigation, Autoplay, Thumbs, FreeMode, Grid, Manipulation, EffectFade, EffectCube, EffectFlip, EffectCoverflow, EffectCreative, EffectCards];
Swiper.use(modules);
