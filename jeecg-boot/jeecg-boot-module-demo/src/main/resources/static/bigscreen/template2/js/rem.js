 (function (doc, win) {
     var docEl = doc.documentElement,
         resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
         recalc = function () {
             var clientWidth = docEl.clientWidth;
             if (!clientWidth) return;
             if (clientWidth >= 1920) {
                 docEl.style.fontSize = '100px'; //1rem  = 100px
             } else {
                 docEl.style.fontSize = 100 * (clientWidth / 1920) + 'px';
             }
         };
     if (!doc.addEventListener) return;
     win.addEventListener(resizeEvt, recalc, false);
     doc.addEventListener('DOMContentLoaded', recalc, false);
 })(document, window);