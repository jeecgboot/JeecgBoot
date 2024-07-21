/**
 * Intro.js v2.9.3
 * https://github.com/usablica/intro.js
 *
 * Copyright (C) 2017 Afshin Mehrabani (@afshinmeh)
 */

(function (f) {
    if (typeof exports === "object" && typeof module !== "undefined") {
        module.exports = f();
        // deprecated function
        // @since 2.8.0
        module.exports.introJs = function () {
            console.warn('Deprecated: please use require("introJs.js") directly, instead of the introJs method of the function');
            // introJs()
            return f().apply(this, arguments);
        };
    } else if (typeof define === "function" && define.amd) {
        define([], f);
    } else if (window.layui && layui.define) {  // layui加载
        layui.define(function (exports) {
            layui.link(layui.cache.base + 'introJs/introJs.css');
            exports('introJs', f());
        });
    } else {
        var g;
        if (typeof window !== "undefined") {
            g = window;
        } else if (typeof global !== "undefined") {
            g = global;
        } else if (typeof self !== "undefined") {
            g = self;
        } else {
            g = this;
        }
        g.introJs = f();
    }
})(function () {
    //Default config/variables
    var VERSION = '2.9.3';

    /**
     * IntroJs main class
     *
     * @class IntroJs
     */
    function IntroJs(obj) {
        this._targetElement = obj;
        this._introItems = [];

        this._options = {
            /* Next button label in tooltip box */
            nextLabel: '下一步 &rarr;',
            /* Previous button label in tooltip box */
            prevLabel: '&larr; 上一步',
            /* Skip button label in tooltip box */
            skipLabel: '跳过',
            /* Done button label in tooltip box */
            doneLabel: '完成',
            /* Hide previous button in the first step? Otherwise, it will be disabled button. */
            hidePrev: false,
            /* Hide next button in the last step? Otherwise, it will be disabled button. */
            hideNext: false,
            /* Default tooltip box position */
            tooltipPosition: 'bottom',
            /* Next CSS class for tooltip boxes */
            tooltipClass: '',
            /* CSS class that is added to the helperLayer */
            highlightClass: '',
            /* Close introduction when pressing Escape button? */
            exitOnEsc: true,
            /* Close introduction when clicking on overlay layer? */
            exitOnOverlayClick: true,
            /* Show step numbers in introduction? */
            showStepNumbers: true,
            /* Let user use keyboard to navigate the tour? */
            keyboardNavigation: true,
            /* Show tour control buttons? */
            showButtons: true,
            /* Show tour bullets? */
            showBullets: true,
            /* Show tour progress? */
            showProgress: false,
            /* Scroll to highlighted element? */
            scrollToElement: true,
            /*
             * Should we scroll the tooltip or target element?
             *
             * Options are: 'element' or 'tooltip'
             */
            scrollTo: 'element',
            /* Padding to add after scrolling when element is not in the viewport (in pixels) */
            scrollPadding: 30,
            /* Set the overlay opacity */
            overlayOpacity: 0.2,
            /* Precedence of positions, when auto is enabled */
            positionPrecedence: ["bottom", "top", "right", "left"],
            /* Disable an interaction with element? */
            disableInteraction: false,
            /* Set how much padding to be used around helper element */
            helperElementPadding: 10,
            /* Default hint position */
            hintPosition: 'top-middle',
            /* Hint button label */
            hintButtonLabel: 'Got it',
            /* Adding animation to hints? */
            hintAnimation: true,
            /* additional classes to put on the buttons */
            buttonClass: "introjs-button"
        };
    }

    /**
     * Initiate a new introduction/guide from an element in the page
     *
     * @api private
     * @method _introForElement
     * @param {Object} targetElm
     * @param {String} group
     * @returns {Boolean} Success or not?
     */
    function _introForElement(targetElm, group) {
        var allIntroSteps = targetElm.querySelectorAll("*[data-intro]"),
            introItems = [];

        if (this._options.steps) {
            //use steps passed programmatically
            _forEach(this._options.steps, function (step) {
                var currentItem = _cloneObject(step);

                //set the step
                currentItem.step = introItems.length + 1;

                //use querySelector function only when developer used CSS selector
                if (typeof (currentItem.element) === 'string') {
                    //grab the element with given selector from the page
                    currentItem.element = document.querySelector(currentItem.element);
                }

                //intro without element
                if (typeof (currentItem.element) === 'undefined' || currentItem.element === null) {
                    var floatingElementQuery = document.querySelector(".introjsFloatingElement");

                    if (floatingElementQuery === null) {
                        floatingElementQuery = document.createElement('div');
                        floatingElementQuery.className = 'introjsFloatingElement';

                        document.body.appendChild(floatingElementQuery);
                    }

                    currentItem.element = floatingElementQuery;
                    currentItem.position = 'floating';
                }

                currentItem.scrollTo = currentItem.scrollTo || this._options.scrollTo;

                if (typeof (currentItem.disableInteraction) === 'undefined') {
                    currentItem.disableInteraction = this._options.disableInteraction;
                }

                if (currentItem.element !== null) {
                    introItems.push(currentItem);
                }
            }.bind(this));

        } else {
            //use steps from data-* annotations
            var elmsLength = allIntroSteps.length;
            var disableInteraction;

            //if there's no element to intro
            if (elmsLength < 1) {
                return false;
            }

            _forEach(allIntroSteps, function (currentElement) {

                // PR #80
                // start intro for groups of elements
                if (group && (currentElement.getAttribute("data-intro-group") !== group)) {
                    return;
                }

                // skip hidden elements
                if (currentElement.style.display === 'none') {
                    return;
                }

                var step = parseInt(currentElement.getAttribute('data-step'), 10);

                if (typeof (currentElement.getAttribute('data-disable-interaction')) !== 'undefined') {
                    disableInteraction = !!currentElement.getAttribute('data-disable-interaction');
                } else {
                    disableInteraction = this._options.disableInteraction;
                }

                if (step > 0) {
                    introItems[step - 1] = {
                        element: currentElement,
                        intro: currentElement.getAttribute('data-intro'),
                        step: parseInt(currentElement.getAttribute('data-step'), 10),
                        tooltipClass: currentElement.getAttribute('data-tooltipclass'),
                        highlightClass: currentElement.getAttribute('data-highlightclass'),
                        position: currentElement.getAttribute('data-position') || this._options.tooltipPosition,
                        scrollTo: currentElement.getAttribute('data-scrollto') || this._options.scrollTo,
                        disableInteraction: disableInteraction
                    };
                }
            }.bind(this));

            //next add intro items without data-step
            //todo: we need a cleanup here, two loops are redundant
            var nextStep = 0;

            _forEach(allIntroSteps, function (currentElement) {

                // PR #80
                // start intro for groups of elements
                if (group && (currentElement.getAttribute("data-intro-group") !== group)) {
                    return;
                }

                if (currentElement.getAttribute('data-step') === null) {

                    while (true) {
                        if (typeof introItems[nextStep] === 'undefined') {
                            break;
                        } else {
                            nextStep++;
                        }
                    }

                    if (typeof (currentElement.getAttribute('data-disable-interaction')) !== 'undefined') {
                        disableInteraction = !!currentElement.getAttribute('data-disable-interaction');
                    } else {
                        disableInteraction = this._options.disableInteraction;
                    }

                    introItems[nextStep] = {
                        element: currentElement,
                        intro: currentElement.getAttribute('data-intro'),
                        step: nextStep + 1,
                        tooltipClass: currentElement.getAttribute('data-tooltipclass'),
                        highlightClass: currentElement.getAttribute('data-highlightclass'),
                        position: currentElement.getAttribute('data-position') || this._options.tooltipPosition,
                        scrollTo: currentElement.getAttribute('data-scrollto') || this._options.scrollTo,
                        disableInteraction: disableInteraction
                    };
                }
            }.bind(this));
        }

        //removing undefined/null elements
        var tempIntroItems = [];
        for (var z = 0; z < introItems.length; z++) {
            if (introItems[z]) {
                // copy non-falsy values to the end of the array
                tempIntroItems.push(introItems[z]);
            }
        }

        introItems = tempIntroItems;

        //Ok, sort all items with given steps
        introItems.sort(function (a, b) {
            return a.step - b.step;
        });

        //set it to the introJs object
        this._introItems = introItems;

        //add overlay layer to the page
        if (_addOverlayLayer.call(this, targetElm)) {
            //then, start the show
            _nextStep.call(this);

            if (this._options.keyboardNavigation) {
                DOMEvent.on(window, 'keydown', _onKeyDown, this, true);
            }
            //for window resize
            DOMEvent.on(window, 'resize', _onResize, this, true);
        }
        return false;
    }

    function _onResize() {
        this.refresh.call(this);
    }

    /**
     * on keyCode:
     * https://developer.mozilla.org/en-US/docs/Web/API/KeyboardEvent/keyCode
     * This feature has been removed from the Web standards.
     * Though some browsers may still support it, it is in
     * the process of being dropped.
     * Instead, you should use KeyboardEvent.code,
     * if it's implemented.
     *
     * jQuery's approach is to test for
     *   (1) e.which, then
     *   (2) e.charCode, then
     *   (3) e.keyCode
     * https://github.com/jquery/jquery/blob/a6b0705294d336ae2f63f7276de0da1195495363/src/event.js#L638
     *
     * @param type var
     * @return type
     */
    function _onKeyDown(e) {
        var code = (e.code === null) ? e.which : e.code;

        // if code/e.which is null
        if (code === null) {
            code = (e.charCode === null) ? e.keyCode : e.charCode;
        }

        if ((code === 'Escape' || code === 27) && this._options.exitOnEsc === true) {
            //escape key pressed, exit the intro
            //check if exit callback is defined
            _exitIntro.call(this, this._targetElement);
        } else if (code === 'ArrowLeft' || code === 37) {
            //left arrow
            _previousStep.call(this);
        } else if (code === 'ArrowRight' || code === 39) {
            //right arrow
            _nextStep.call(this);
        } else if (code === 'Enter' || code === 13) {
            //srcElement === ie
            var target = e.target || e.srcElement;
            if (target && target.className.match('introjs-prevbutton')) {
                //user hit enter while focusing on previous button
                _previousStep.call(this);
            } else if (target && target.className.match('introjs-skipbutton')) {
                //user hit enter while focusing on skip button
                if (this._introItems.length - 1 === this._currentStep && typeof (this._introCompleteCallback) === 'function') {
                    this._introCompleteCallback.call(this);
                }

                _exitIntro.call(this, this._targetElement);
            } else if (target && target.getAttribute('data-stepnumber')) {
                // user hit enter while focusing on step bullet
                target.click();
            } else {
                //default behavior for responding to enter
                _nextStep.call(this);
            }

            //prevent default behaviour on hitting Enter, to prevent steps being skipped in some browsers
            if (e.preventDefault) {
                e.preventDefault();
            } else {
                e.returnValue = false;
            }
        }
    }

    /*
      * makes a copy of the object
      * @api private
      * @method _cloneObject
     */
    function _cloneObject(object) {
        if (object === null || typeof (object) !== 'object' || typeof (object.nodeType) !== 'undefined') {
            return object;
        }
        var temp = {};
        for (var key in object) {
            if (typeof (window.jQuery) !== 'undefined' && object[key] instanceof window.jQuery) {
                temp[key] = object[key];
            } else {
                temp[key] = _cloneObject(object[key]);
            }
        }
        return temp;
    }

    /**
     * Go to specific step of introduction
     *
     * @api private
     * @method _goToStep
     */
    function _goToStep(step) {
        //because steps starts with zero
        this._currentStep = step - 2;
        if (typeof (this._introItems) !== 'undefined') {
            _nextStep.call(this);
        }
    }

    /**
     * Go to the specific step of introduction with the explicit [data-step] number
     *
     * @api private
     * @method _goToStepNumber
     */
    function _goToStepNumber(step) {
        this._currentStepNumber = step;
        if (typeof (this._introItems) !== 'undefined') {
            _nextStep.call(this);
        }
    }

    /**
     * Go to next step on intro
     *
     * @api private
     * @method _nextStep
     */
    function _nextStep() {
        this._direction = 'forward';

        if (typeof (this._currentStepNumber) !== 'undefined') {
            _forEach(this._introItems, function (item, i) {
                if (item.step === this._currentStepNumber) {
                    this._currentStep = i - 1;
                    this._currentStepNumber = undefined;
                }
            }.bind(this));
        }

        if (typeof (this._currentStep) === 'undefined') {
            this._currentStep = 0;
        } else {
            ++this._currentStep;
        }

        var nextStep = this._introItems[this._currentStep];
        var continueStep = true;

        if (typeof (this._introBeforeChangeCallback) !== 'undefined') {
            continueStep = this._introBeforeChangeCallback.call(this, nextStep.element);
        }

        // if `onbeforechange` returned `false`, stop displaying the element
        if (continueStep === false) {
            --this._currentStep;
            return false;
        }

        if ((this._introItems.length) <= this._currentStep) {
            //end of the intro
            //check if any callback is defined
            if (typeof (this._introCompleteCallback) === 'function') {
                this._introCompleteCallback.call(this);
            }
            _exitIntro.call(this, this._targetElement);
            return;
        }

        _showElement.call(this, nextStep);
    }

    /**
     * Go to previous step on intro
     *
     * @api private
     * @method _previousStep
     */
    function _previousStep() {
        this._direction = 'backward';

        if (this._currentStep === 0) {
            return false;
        }

        --this._currentStep;

        var nextStep = this._introItems[this._currentStep];
        var continueStep = true;

        if (typeof (this._introBeforeChangeCallback) !== 'undefined') {
            continueStep = this._introBeforeChangeCallback.call(this, nextStep.element);
        }

        // if `onbeforechange` returned `false`, stop displaying the element
        if (continueStep === false) {
            ++this._currentStep;
            return false;
        }

        _showElement.call(this, nextStep);
    }

    /**
     * Update placement of the intro objects on the screen
     * @api private
     */
    function _refresh() {
        // re-align intros
        _setHelperLayerPosition.call(this, document.querySelector('.introjs-helperLayer'));
        _setHelperLayerPosition.call(this, document.querySelector('.introjs-tooltipReferenceLayer'));
        _setHelperLayerPosition.call(this, document.querySelector('.introjs-disableInteraction'));

        // re-align tooltip
        if (this._currentStep !== undefined && this._currentStep !== null) {
            var oldHelperNumberLayer = document.querySelector('.introjs-helperNumberLayer'),
                oldArrowLayer = document.querySelector('.introjs-arrow'),
                oldtooltipContainer = document.querySelector('.introjs-tooltip');
            _placeTooltip.call(this, this._introItems[this._currentStep].element, oldtooltipContainer, oldArrowLayer, oldHelperNumberLayer);
        }

        //re-align hints
        _reAlignHints.call(this);
        return this;
    }

    /**
     * Exit from intro
     *
     * @api private
     * @method _exitIntro
     * @param {Object} targetElement
     * @param {Boolean} force - Setting to `true` will skip the result of beforeExit callback
     */
    function _exitIntro(targetElement, force) {
        var continueExit = true;

        // calling onbeforeexit callback
        //
        // If this callback return `false`, it would halt the process
        if (this._introBeforeExitCallback !== undefined) {
            continueExit = this._introBeforeExitCallback.call(this);
        }

        // skip this check if `force` parameter is `true`
        // otherwise, if `onbeforeexit` returned `false`, don't exit the intro
        if (!force && continueExit === false) return;

        //remove overlay layers from the page
        var overlayLayers = targetElement.querySelectorAll('.introjs-overlay');

        if (overlayLayers && overlayLayers.length) {
            _forEach(overlayLayers, function (overlayLayer) {
                overlayLayer.style.opacity = 0;
                window.setTimeout(function () {
                    if (this.parentNode) {
                        this.parentNode.removeChild(this);
                    }
                }.bind(overlayLayer), 500);
            }.bind(this));
        }

        //remove all helper layers
        var helperLayer = targetElement.querySelector('.introjs-helperLayer');
        if (helperLayer) {
            helperLayer.parentNode.removeChild(helperLayer);
        }

        var referenceLayer = targetElement.querySelector('.introjs-tooltipReferenceLayer');
        if (referenceLayer) {
            referenceLayer.parentNode.removeChild(referenceLayer);
        }

        //remove disableInteractionLayer
        var disableInteractionLayer = targetElement.querySelector('.introjs-disableInteraction');
        if (disableInteractionLayer) {
            disableInteractionLayer.parentNode.removeChild(disableInteractionLayer);
        }

        //remove intro floating element
        var floatingElement = document.querySelector('.introjsFloatingElement');
        if (floatingElement) {
            floatingElement.parentNode.removeChild(floatingElement);
        }

        _removeShowElement();

        //remove `introjs-fixParent` class from the elements
        var fixParents = document.querySelectorAll('.introjs-fixParent');
        _forEach(fixParents, function (parent) {
            _removeClass(parent, /introjs-fixParent/g);
        });

        //clean listeners
        DOMEvent.off(window, 'keydown', _onKeyDown, this, true);
        DOMEvent.off(window, 'resize', _onResize, this, true);

        //check if any callback is defined
        if (this._introExitCallback !== undefined) {
            this._introExitCallback.call(this);
        }

        //set the step to zero
        this._currentStep = undefined;
    }

    /**
     * Render tooltip box in the page
     *
     * @api private
     * @method _placeTooltip
     * @param {HTMLElement} targetElement
     * @param {HTMLElement} tooltipLayer
     * @param {HTMLElement} arrowLayer
     * @param {HTMLElement} helperNumberLayer
     * @param {Boolean} hintMode
     */
    function _placeTooltip(targetElement, tooltipLayer, arrowLayer, helperNumberLayer, hintMode) {
        var tooltipCssClass = '',
            currentStepObj,
            tooltipOffset,
            targetOffset,
            windowSize,
            currentTooltipPosition;

        hintMode = hintMode || false;

        //reset the old style
        tooltipLayer.style.top = null;
        tooltipLayer.style.right = null;
        tooltipLayer.style.bottom = null;
        tooltipLayer.style.left = null;
        tooltipLayer.style.marginLeft = null;
        tooltipLayer.style.marginTop = null;

        arrowLayer.style.display = 'inherit';

        if (typeof (helperNumberLayer) !== 'undefined' && helperNumberLayer !== null) {
            helperNumberLayer.style.top = null;
            helperNumberLayer.style.left = null;
        }

        //prevent error when `this._currentStep` is undefined
        if (!this._introItems[this._currentStep]) return;

        //if we have a custom css class for each step
        currentStepObj = this._introItems[this._currentStep];
        if (typeof (currentStepObj.tooltipClass) === 'string') {
            tooltipCssClass = currentStepObj.tooltipClass;
        } else {
            tooltipCssClass = this._options.tooltipClass;
        }

        tooltipLayer.className = ('introjs-tooltip ' + tooltipCssClass).replace(/^\s+|\s+$/g, '');
        tooltipLayer.setAttribute('role', 'dialog');

        currentTooltipPosition = this._introItems[this._currentStep].position;

        // Floating is always valid, no point in calculating
        if (currentTooltipPosition !== "floating") {
            currentTooltipPosition = _determineAutoPosition.call(this, targetElement, tooltipLayer, currentTooltipPosition);
        }

        var tooltipLayerStyleLeft;
        targetOffset = _getOffset(targetElement);
        tooltipOffset = _getOffset(tooltipLayer);
        windowSize = _getWinSize();

        _addClass(tooltipLayer, 'introjs-' + currentTooltipPosition);

        switch (currentTooltipPosition) {
            case 'top-right-aligned':
                arrowLayer.className = 'introjs-arrow bottom-right';

                var tooltipLayerStyleRight = 0;
                _checkLeft(targetOffset, tooltipLayerStyleRight, tooltipOffset, tooltipLayer);
                tooltipLayer.style.bottom = (targetOffset.height + 20) + 'px';
                break;

            case 'top-middle-aligned':
                arrowLayer.className = 'introjs-arrow bottom-middle';

                var tooltipLayerStyleLeftRight = targetOffset.width / 2 - tooltipOffset.width / 2;

                // a fix for middle aligned hints
                if (hintMode) {
                    tooltipLayerStyleLeftRight += 5;
                }

                if (_checkLeft(targetOffset, tooltipLayerStyleLeftRight, tooltipOffset, tooltipLayer)) {
                    tooltipLayer.style.right = null;
                    _checkRight(targetOffset, tooltipLayerStyleLeftRight, tooltipOffset, windowSize, tooltipLayer);
                }
                tooltipLayer.style.bottom = (targetOffset.height + 20) + 'px';
                break;

            case 'top-left-aligned':
            // top-left-aligned is the same as the default top
            case 'top':
                arrowLayer.className = 'introjs-arrow bottom';

                tooltipLayerStyleLeft = (hintMode) ? 0 : 15;

                _checkRight(targetOffset, tooltipLayerStyleLeft, tooltipOffset, windowSize, tooltipLayer);
                tooltipLayer.style.bottom = (targetOffset.height + 20) + 'px';
                break;
            case 'right':
                tooltipLayer.style.left = (targetOffset.width + 20) + 'px';
                if (targetOffset.top + tooltipOffset.height > windowSize.height) {
                    // In this case, right would have fallen below the bottom of the screen.
                    // Modify so that the bottom of the tooltip connects with the target
                    arrowLayer.className = "introjs-arrow left-bottom";
                    tooltipLayer.style.top = "-" + (tooltipOffset.height - targetOffset.height - 20) + "px";
                } else {
                    arrowLayer.className = 'introjs-arrow left';
                }
                break;
            case 'left':
                if (!hintMode && this._options.showStepNumbers === true) {
                    tooltipLayer.style.top = '15px';
                }

                if (targetOffset.top + tooltipOffset.height > windowSize.height) {
                    // In this case, left would have fallen below the bottom of the screen.
                    // Modify so that the bottom of the tooltip connects with the target
                    tooltipLayer.style.top = "-" + (tooltipOffset.height - targetOffset.height - 20) + "px";
                    arrowLayer.className = 'introjs-arrow right-bottom';
                } else {
                    arrowLayer.className = 'introjs-arrow right';
                }
                tooltipLayer.style.right = (targetOffset.width + 20) + 'px';

                break;
            case 'floating':
                arrowLayer.style.display = 'none';

                //we have to adjust the top and left of layer manually for intro items without element
                tooltipLayer.style.left = '50%';
                tooltipLayer.style.top = '50%';
                tooltipLayer.style.marginLeft = '-' + (tooltipOffset.width / 2) + 'px';
                tooltipLayer.style.marginTop = '-' + (tooltipOffset.height / 2) + 'px';

                if (typeof (helperNumberLayer) !== 'undefined' && helperNumberLayer !== null) {
                    helperNumberLayer.style.left = '-' + ((tooltipOffset.width / 2) + 18) + 'px';
                    helperNumberLayer.style.top = '-' + ((tooltipOffset.height / 2) + 18) + 'px';
                }

                break;
            case 'bottom-right-aligned':
                arrowLayer.className = 'introjs-arrow top-right';

                tooltipLayerStyleRight = 0;
                _checkLeft(targetOffset, tooltipLayerStyleRight, tooltipOffset, tooltipLayer);
                tooltipLayer.style.top = (targetOffset.height + 20) + 'px';
                break;

            case 'bottom-middle-aligned':
                arrowLayer.className = 'introjs-arrow top-middle';

                tooltipLayerStyleLeftRight = targetOffset.width / 2 - tooltipOffset.width / 2;

                // a fix for middle aligned hints
                if (hintMode) {
                    tooltipLayerStyleLeftRight += 5;
                }

                if (_checkLeft(targetOffset, tooltipLayerStyleLeftRight, tooltipOffset, tooltipLayer)) {
                    tooltipLayer.style.right = null;
                    _checkRight(targetOffset, tooltipLayerStyleLeftRight, tooltipOffset, windowSize, tooltipLayer);
                }
                tooltipLayer.style.top = (targetOffset.height + 20) + 'px';
                break;

            // case 'bottom-left-aligned':
            // Bottom-left-aligned is the same as the default bottom
            // case 'bottom':
            // Bottom going to follow the default behavior
            default:
                arrowLayer.className = 'introjs-arrow top';

                tooltipLayerStyleLeft = 0;
                _checkRight(targetOffset, tooltipLayerStyleLeft, tooltipOffset, windowSize, tooltipLayer);
                tooltipLayer.style.top = (targetOffset.height + 20) + 'px';
        }
    }

    /**
     * Set tooltip left so it doesn't go off the right side of the window
     *
     * @return boolean true, if tooltipLayerStyleLeft is ok.  false, otherwise.
     */
    function _checkRight(targetOffset, tooltipLayerStyleLeft, tooltipOffset, windowSize, tooltipLayer) {
        if (targetOffset.left + tooltipLayerStyleLeft + tooltipOffset.width > windowSize.width) {
            // off the right side of the window
            tooltipLayer.style.left = (windowSize.width - tooltipOffset.width - targetOffset.left) + 'px';
            return false;
        }
        tooltipLayer.style.left = tooltipLayerStyleLeft + 'px';
        return true;
    }

    /**
     * Set tooltip right so it doesn't go off the left side of the window
     *
     * @return boolean true, if tooltipLayerStyleRight is ok.  false, otherwise.
     */
    function _checkLeft(targetOffset, tooltipLayerStyleRight, tooltipOffset, tooltipLayer) {
        if (targetOffset.left + targetOffset.width - tooltipLayerStyleRight - tooltipOffset.width < 0) {
            // off the left side of the window
            tooltipLayer.style.left = (-targetOffset.left) + 'px';
            return false;
        }
        tooltipLayer.style.right = tooltipLayerStyleRight + 'px';
        return true;
    }

    /**
     * Determines the position of the tooltip based on the position precedence and availability
     * of screen space.
     *
     * @param {Object}    targetElement
     * @param {Object}    tooltipLayer
     * @param {String}    desiredTooltipPosition
     * @return {String}   calculatedPosition
     */
    function _determineAutoPosition(targetElement, tooltipLayer, desiredTooltipPosition) {

        // Take a clone of position precedence. These will be the available
        var possiblePositions = this._options.positionPrecedence.slice();

        var windowSize = _getWinSize();
        var tooltipHeight = _getOffset(tooltipLayer).height + 10;
        var tooltipWidth = _getOffset(tooltipLayer).width + 20;
        var targetElementRect = targetElement.getBoundingClientRect();

        // If we check all the possible areas, and there are no valid places for the tooltip, the element
        // must take up most of the screen real estate. Show the tooltip floating in the middle of the screen.
        var calculatedPosition = "floating";

        /*
        * auto determine position
        */

        // Check for space below
        if (targetElementRect.bottom + tooltipHeight + tooltipHeight > windowSize.height) {
            _removeEntry(possiblePositions, "bottom");
        }

        // Check for space above
        if (targetElementRect.top - tooltipHeight < 0) {
            _removeEntry(possiblePositions, "top");
        }

        // Check for space to the right
        if (targetElementRect.right + tooltipWidth > windowSize.width) {
            _removeEntry(possiblePositions, "right");
        }

        // Check for space to the left
        if (targetElementRect.left - tooltipWidth < 0) {
            _removeEntry(possiblePositions, "left");
        }

        // @var {String}  ex: 'right-aligned'
        var desiredAlignment = (function (pos) {
            var hyphenIndex = pos.indexOf('-');
            if (hyphenIndex !== -1) {
                // has alignment
                return pos.substr(hyphenIndex);
            }
            return '';
        })(desiredTooltipPosition || '');

        // strip alignment from position
        if (desiredTooltipPosition) {
            // ex: "bottom-right-aligned"
            // should return 'bottom'
            desiredTooltipPosition = desiredTooltipPosition.split('-')[0];
        }

        if (possiblePositions.length) {
            if (desiredTooltipPosition !== "auto" &&
                possiblePositions.indexOf(desiredTooltipPosition) > -1) {
                // If the requested position is in the list, choose that
                calculatedPosition = desiredTooltipPosition;
            } else {
                // Pick the first valid position, in order
                calculatedPosition = possiblePositions[0];
            }
        }

        // only top and bottom positions have optional alignments
        if (['top', 'bottom'].indexOf(calculatedPosition) !== -1) {
            calculatedPosition += _determineAutoAlignment(targetElementRect.left, tooltipWidth, windowSize, desiredAlignment);
        }

        return calculatedPosition;
    }

    /**
     * auto-determine alignment
     * @param {Integer}  offsetLeft
     * @param {Integer}  tooltipWidth
     * @param {Object}   windowSize
     * @param {String}   desiredAlignment
     * @return {String}  calculatedAlignment
     */
    function _determineAutoAlignment(offsetLeft, tooltipWidth, windowSize, desiredAlignment) {
        var halfTooltipWidth = tooltipWidth / 2,
            winWidth = Math.min(windowSize.width, window.screen.width),
            possibleAlignments = ['-left-aligned', '-middle-aligned', '-right-aligned'],
            calculatedAlignment = '';

        // valid left must be at least a tooltipWidth
        // away from right side
        if (winWidth - offsetLeft < tooltipWidth) {
            _removeEntry(possibleAlignments, '-left-aligned');
        }

        // valid middle must be at least half
        // width away from both sides
        if (offsetLeft < halfTooltipWidth ||
            winWidth - offsetLeft < halfTooltipWidth) {
            _removeEntry(possibleAlignments, '-middle-aligned');
        }

        // valid right must be at least a tooltipWidth
        // width away from left side
        if (offsetLeft < tooltipWidth) {
            _removeEntry(possibleAlignments, '-right-aligned');
        }

        if (possibleAlignments.length) {
            if (possibleAlignments.indexOf(desiredAlignment) !== -1) {
                // the desired alignment is valid
                calculatedAlignment = desiredAlignment;
            } else {
                // pick the first valid position, in order
                calculatedAlignment = possibleAlignments[0];
            }
        } else {
            // if screen width is too small
            // for ANY alignment, middle is
            // probably the best for visibility
            calculatedAlignment = '-middle-aligned';
        }

        return calculatedAlignment;
    }

    /**
     * Remove an entry from a string array if it's there, does nothing if it isn't there.
     *
     * @param {Array} stringArray
     * @param {String} stringToRemove
     */
    function _removeEntry(stringArray, stringToRemove) {
        if (stringArray.indexOf(stringToRemove) > -1) {
            stringArray.splice(stringArray.indexOf(stringToRemove), 1);
        }
    }

    /**
     * Update the position of the helper layer on the screen
     *
     * @api private
     * @method _setHelperLayerPosition
     * @param {Object} helperLayer
     */
    function _setHelperLayerPosition(helperLayer) {
        if (helperLayer) {
            //prevent error when `this._currentStep` in undefined
            if (!this._introItems[this._currentStep]) return;

            var currentElement = this._introItems[this._currentStep],
                elementPosition = _getOffset(currentElement.element),
                widthHeightPadding = this._options.helperElementPadding;

            // If the target element is fixed, the tooltip should be fixed as well.
            // Otherwise, remove a fixed class that may be left over from the previous
            // step.
            if (_isFixed(currentElement.element)) {
                _addClass(helperLayer, 'introjs-fixedTooltip');
            } else {
                _removeClass(helperLayer, 'introjs-fixedTooltip');
            }

            if (currentElement.position === 'floating') {
                widthHeightPadding = 0;
            }

            //set new position to helper layer
            helperLayer.style.cssText = 'width: ' + (elementPosition.width + widthHeightPadding) + 'px; ' +
                'height:' + (elementPosition.height + widthHeightPadding) + 'px; ' +
                'top:' + (elementPosition.top - widthHeightPadding / 2) + 'px;' +
                'left: ' + (elementPosition.left - widthHeightPadding / 2) + 'px;';

        }
    }

    /**
     * Add disableinteraction layer and adjust the size and position of the layer
     *
     * @api private
     * @method _disableInteraction
     */
    function _disableInteraction() {
        var disableInteractionLayer = document.querySelector('.introjs-disableInteraction');

        if (disableInteractionLayer === null) {
            disableInteractionLayer = document.createElement('div');
            disableInteractionLayer.className = 'introjs-disableInteraction';
            this._targetElement.appendChild(disableInteractionLayer);
        }

        _setHelperLayerPosition.call(this, disableInteractionLayer);
    }

    /**
     * Setting anchors to behave like buttons
     *
     * @api private
     * @method _setAnchorAsButton
     */
    function _setAnchorAsButton(anchor) {
        anchor.setAttribute('role', 'button');
        anchor.tabIndex = 0;
    }

    /**
     * Show an element on the page
     *
     * @api private
     * @method _showElement
     * @param {Object} targetElement
     */
    function _showElement(targetElement) {
        if (typeof (this._introChangeCallback) !== 'undefined') {
            this._introChangeCallback.call(this, targetElement.element);
        }

        var self = this,
            oldHelperLayer = document.querySelector('.introjs-helperLayer'),
            oldReferenceLayer = document.querySelector('.introjs-tooltipReferenceLayer'),
            highlightClass = 'introjs-helperLayer',
            nextTooltipButton,
            prevTooltipButton,
            skipTooltipButton,
            scrollParent;

        //check for a current step highlight class
        if (typeof (targetElement.highlightClass) === 'string') {
            highlightClass += (' ' + targetElement.highlightClass);
        }
        //check for options highlight class
        if (typeof (this._options.highlightClass) === 'string') {
            highlightClass += (' ' + this._options.highlightClass);
        }

        if (oldHelperLayer !== null) {
            var oldHelperNumberLayer = oldReferenceLayer.querySelector('.introjs-helperNumberLayer'),
                oldtooltipLayer = oldReferenceLayer.querySelector('.introjs-tooltiptext'),
                oldArrowLayer = oldReferenceLayer.querySelector('.introjs-arrow'),
                oldtooltipContainer = oldReferenceLayer.querySelector('.introjs-tooltip');

            skipTooltipButton = oldReferenceLayer.querySelector('.introjs-skipbutton');
            prevTooltipButton = oldReferenceLayer.querySelector('.introjs-prevbutton');
            nextTooltipButton = oldReferenceLayer.querySelector('.introjs-nextbutton');

            //update or reset the helper highlight class
            oldHelperLayer.className = highlightClass;
            //hide the tooltip
            oldtooltipContainer.style.opacity = 0;
            oldtooltipContainer.style.display = "none";

            if (oldHelperNumberLayer !== null) {
                var lastIntroItem = this._introItems[(targetElement.step - 2 >= 0 ? targetElement.step - 2 : 0)];

                if (lastIntroItem !== null && (this._direction === 'forward' && lastIntroItem.position === 'floating') || (this._direction === 'backward' && targetElement.position === 'floating')) {
                    oldHelperNumberLayer.style.opacity = 0;
                }
            }

            // scroll to element
            scrollParent = _getScrollParent(targetElement.element);

            if (scrollParent !== document.body) {
                // target is within a scrollable element
                _scrollParentToElement(scrollParent, targetElement.element);
            }

            // set new position to helper layer
            _setHelperLayerPosition.call(self, oldHelperLayer);
            _setHelperLayerPosition.call(self, oldReferenceLayer);

            //remove `introjs-fixParent` class from the elements
            var fixParents = document.querySelectorAll('.introjs-fixParent');
            _forEach(fixParents, function (parent) {
                _removeClass(parent, /introjs-fixParent/g);
            });

            //remove old classes if the element still exist
            _removeShowElement();

            //we should wait until the CSS3 transition is competed (it's 0.3 sec) to prevent incorrect `height` and `width` calculation
            if (self._lastShowElementTimer) {
                window.clearTimeout(self._lastShowElementTimer);
            }

            self._lastShowElementTimer = window.setTimeout(function () {
                //set current step to the label
                if (oldHelperNumberLayer !== null) {
                    oldHelperNumberLayer.innerHTML = targetElement.step;
                }
                //set current tooltip text
                oldtooltipLayer.innerHTML = targetElement.intro;
                //set the tooltip position
                oldtooltipContainer.style.display = "block";
                _placeTooltip.call(self, targetElement.element, oldtooltipContainer, oldArrowLayer, oldHelperNumberLayer);

                //change active bullet
                if (self._options.showBullets) {
                    oldReferenceLayer.querySelector('.introjs-bullets li > a.active').className = '';
                    oldReferenceLayer.querySelector('.introjs-bullets li > a[data-stepnumber="' + targetElement.step + '"]').className = 'active';
                }
                oldReferenceLayer.querySelector('.introjs-progress .introjs-progressbar').style.cssText = 'width:' + _getProgress.call(self) + '%;';
                oldReferenceLayer.querySelector('.introjs-progress .introjs-progressbar').setAttribute('aria-valuenow', _getProgress.call(self));

                //show the tooltip
                oldtooltipContainer.style.opacity = 1;
                if (oldHelperNumberLayer) oldHelperNumberLayer.style.opacity = 1;

                //reset button focus
                if (typeof skipTooltipButton !== "undefined" && skipTooltipButton !== null && /introjs-donebutton/gi.test(skipTooltipButton.className)) {
                    // skip button is now "done" button
                    skipTooltipButton.focus();
                } else if (typeof nextTooltipButton !== "undefined" && nextTooltipButton !== null) {
                    //still in the tour, focus on next
                    nextTooltipButton.focus();
                }

                // change the scroll of the window, if needed
                _scrollTo.call(self, targetElement.scrollTo, targetElement, oldtooltipLayer);
            }, 350);

            // end of old element if-else condition
        } else {
            var helperLayer = document.createElement('div'),
                referenceLayer = document.createElement('div'),
                arrowLayer = document.createElement('div'),
                tooltipLayer = document.createElement('div'),
                tooltipTextLayer = document.createElement('div'),
                bulletsLayer = document.createElement('div'),
                progressLayer = document.createElement('div'),
                buttonsLayer = document.createElement('div');

            helperLayer.className = highlightClass;
            referenceLayer.className = 'introjs-tooltipReferenceLayer';

            // scroll to element
            scrollParent = _getScrollParent(targetElement.element);

            if (scrollParent !== document.body) {
                // target is within a scrollable element
                _scrollParentToElement(scrollParent, targetElement.element);
            }

            //set new position to helper layer
            _setHelperLayerPosition.call(self, helperLayer);
            _setHelperLayerPosition.call(self, referenceLayer);

            //add helper layer to target element
            this._targetElement.appendChild(helperLayer);
            this._targetElement.appendChild(referenceLayer);

            arrowLayer.className = 'introjs-arrow';

            tooltipTextLayer.className = 'introjs-tooltiptext';
            tooltipTextLayer.innerHTML = targetElement.intro;

            bulletsLayer.className = 'introjs-bullets';

            if (this._options.showBullets === false) {
                bulletsLayer.style.display = 'none';
            }

            var ulContainer = document.createElement('ul');
            ulContainer.setAttribute('role', 'tablist');

            var anchorClick = function () {
                self.goToStep(this.getAttribute('data-stepnumber'));
            };

            _forEach(this._introItems, function (item, i) {
                var innerLi = document.createElement('li');
                var anchorLink = document.createElement('a');

                innerLi.setAttribute('role', 'presentation');
                anchorLink.setAttribute('role', 'tab');

                anchorLink.onclick = anchorClick;

                if (i === (targetElement.step - 1)) {
                    anchorLink.className = 'active';
                }

                _setAnchorAsButton(anchorLink);
                anchorLink.innerHTML = "&nbsp;";
                anchorLink.setAttribute('data-stepnumber', item.step);

                innerLi.appendChild(anchorLink);
                ulContainer.appendChild(innerLi);
            });

            bulletsLayer.appendChild(ulContainer);

            progressLayer.className = 'introjs-progress';

            if (this._options.showProgress === false) {
                progressLayer.style.display = 'none';
            }
            var progressBar = document.createElement('div');
            progressBar.className = 'introjs-progressbar';
            progressBar.setAttribute('role', 'progress');
            progressBar.setAttribute('aria-valuemin', 0);
            progressBar.setAttribute('aria-valuemax', 100);
            progressBar.setAttribute('aria-valuenow', _getProgress.call(this));
            progressBar.style.cssText = 'width:' + _getProgress.call(this) + '%;';

            progressLayer.appendChild(progressBar);

            buttonsLayer.className = 'introjs-tooltipbuttons';
            if (this._options.showButtons === false) {
                buttonsLayer.style.display = 'none';
            }

            tooltipLayer.className = 'introjs-tooltip';
            tooltipLayer.appendChild(tooltipTextLayer);
            tooltipLayer.appendChild(bulletsLayer);
            tooltipLayer.appendChild(progressLayer);

            //add helper layer number
            var helperNumberLayer = document.createElement('span');
            if (this._options.showStepNumbers === true) {
                helperNumberLayer.className = 'introjs-helperNumberLayer';
                helperNumberLayer.innerHTML = targetElement.step;
                referenceLayer.appendChild(helperNumberLayer);
            }

            tooltipLayer.appendChild(arrowLayer);
            referenceLayer.appendChild(tooltipLayer);

            //next button
            nextTooltipButton = document.createElement('a');

            nextTooltipButton.onclick = function () {
                if (self._introItems.length - 1 !== self._currentStep) {
                    _nextStep.call(self);
                }
            };

            _setAnchorAsButton(nextTooltipButton);
            nextTooltipButton.innerHTML = this._options.nextLabel;

            //previous button
            prevTooltipButton = document.createElement('a');

            prevTooltipButton.onclick = function () {
                if (self._currentStep !== 0) {
                    _previousStep.call(self);
                }
            };

            _setAnchorAsButton(prevTooltipButton);
            prevTooltipButton.innerHTML = this._options.prevLabel;

            //skip button
            skipTooltipButton = document.createElement('a');
            skipTooltipButton.className = this._options.buttonClass + ' introjs-skipbutton ';
            _setAnchorAsButton(skipTooltipButton);
            skipTooltipButton.innerHTML = this._options.skipLabel;

            skipTooltipButton.onclick = function () {
                if (self._introItems.length - 1 === self._currentStep && typeof (self._introCompleteCallback) === 'function') {
                    self._introCompleteCallback.call(self);
                }

                if (self._introItems.length - 1 !== self._currentStep && typeof (self._introExitCallback) === 'function') {
                    self._introExitCallback.call(self);
                }

                if (typeof (self._introSkipCallback) === 'function') {
                    self._introSkipCallback.call(self);
                }

                _exitIntro.call(self, self._targetElement);
            };

            buttonsLayer.appendChild(skipTooltipButton);

            //in order to prevent displaying next/previous button always
            if (this._introItems.length > 1) {
                buttonsLayer.appendChild(prevTooltipButton);
                buttonsLayer.appendChild(nextTooltipButton);
            }

            tooltipLayer.appendChild(buttonsLayer);

            //set proper position
            _placeTooltip.call(self, targetElement.element, tooltipLayer, arrowLayer, helperNumberLayer);

            // change the scroll of the window, if needed
            _scrollTo.call(this, targetElement.scrollTo, targetElement, tooltipLayer);

            //end of new element if-else condition
        }

        // removing previous disable interaction layer
        var disableInteractionLayer = self._targetElement.querySelector('.introjs-disableInteraction');
        if (disableInteractionLayer) {
            disableInteractionLayer.parentNode.removeChild(disableInteractionLayer);
        }

        //disable interaction
        if (targetElement.disableInteraction) {
            _disableInteraction.call(self);
        }

        // when it's the first step of tour
        if (this._currentStep === 0 && this._introItems.length > 1) {
            if (typeof skipTooltipButton !== "undefined" && skipTooltipButton !== null) {
                skipTooltipButton.className = this._options.buttonClass + ' introjs-skipbutton';
            }
            if (typeof nextTooltipButton !== "undefined" && nextTooltipButton !== null) {
                nextTooltipButton.className = this._options.buttonClass + ' introjs-nextbutton';
            }

            if (this._options.hidePrev === true) {
                if (typeof prevTooltipButton !== "undefined" && prevTooltipButton !== null) {
                    prevTooltipButton.className = this._options.buttonClass + ' introjs-prevbutton introjs-hidden';
                }
                if (typeof nextTooltipButton !== "undefined" && nextTooltipButton !== null) {
                    _addClass(nextTooltipButton, 'introjs-fullbutton');
                }
            } else {
                if (typeof prevTooltipButton !== "undefined" && prevTooltipButton !== null) {
                    prevTooltipButton.className = this._options.buttonClass + ' introjs-prevbutton introjs-disabled';
                }
            }

            if (typeof skipTooltipButton !== "undefined" && skipTooltipButton !== null) {
                skipTooltipButton.innerHTML = this._options.skipLabel;
            }
        } else if (this._introItems.length - 1 === this._currentStep || this._introItems.length === 1) {
            // last step of tour
            if (typeof skipTooltipButton !== "undefined" && skipTooltipButton !== null) {
                skipTooltipButton.innerHTML = this._options.doneLabel;
                // adding donebutton class in addition to skipbutton
                _addClass(skipTooltipButton, 'introjs-donebutton');
            }
            if (typeof prevTooltipButton !== "undefined" && prevTooltipButton !== null) {
                prevTooltipButton.className = this._options.buttonClass + ' introjs-prevbutton';
            }

            if (this._options.hideNext === true) {
                if (typeof nextTooltipButton !== "undefined" && nextTooltipButton !== null) {
                    nextTooltipButton.className = this._options.buttonClass + ' introjs-nextbutton introjs-hidden';
                }
                if (typeof prevTooltipButton !== "undefined" && prevTooltipButton !== null) {
                    _addClass(prevTooltipButton, 'introjs-fullbutton');
                }
            } else {
                if (typeof nextTooltipButton !== "undefined" && nextTooltipButton !== null) {
                    nextTooltipButton.className = this._options.buttonClass + ' introjs-nextbutton introjs-disabled';
                }
            }
        } else {
            // steps between start and end
            if (typeof skipTooltipButton !== "undefined" && skipTooltipButton !== null) {
                skipTooltipButton.className = this._options.buttonClass + ' introjs-skipbutton';
            }
            if (typeof prevTooltipButton !== "undefined" && prevTooltipButton !== null) {
                prevTooltipButton.className = this._options.buttonClass + ' introjs-prevbutton';
            }
            if (typeof nextTooltipButton !== "undefined" && nextTooltipButton !== null) {
                nextTooltipButton.className = this._options.buttonClass + ' introjs-nextbutton';
            }
            if (typeof skipTooltipButton !== "undefined" && skipTooltipButton !== null) {
                skipTooltipButton.innerHTML = this._options.skipLabel;
            }
        }

        prevTooltipButton.setAttribute('role', 'button');
        nextTooltipButton.setAttribute('role', 'button');
        skipTooltipButton.setAttribute('role', 'button');

        //Set focus on "next" button, so that hitting Enter always moves you onto the next step
        if (typeof nextTooltipButton !== "undefined" && nextTooltipButton !== null) {
            nextTooltipButton.focus();
        }

        _setShowElement(targetElement);

        if (typeof (this._introAfterChangeCallback) !== 'undefined') {
            this._introAfterChangeCallback.call(this, targetElement.element);
        }
    }

    /**
     * To change the scroll of `window` after highlighting an element
     *
     * @api private
     * @method _scrollTo
     * @param {String} scrollTo
     * @param {Object} targetElement
     * @param {Object} tooltipLayer
     */
    function _scrollTo(scrollTo, targetElement, tooltipLayer) {
        if (scrollTo === 'off') return;
        var rect;

        if (!this._options.scrollToElement) return;

        if (scrollTo === 'tooltip') {
            rect = tooltipLayer.getBoundingClientRect();
        } else {
            rect = targetElement.element.getBoundingClientRect();
        }

        if (!_elementInViewport(targetElement.element)) {
            var winHeight = _getWinSize().height;
            var top = rect.bottom - (rect.bottom - rect.top);

            // TODO (afshinm): do we need scroll padding now?
            // I have changed the scroll option and now it scrolls the window to
            // the center of the target element or tooltip.

            if (top < 0 || targetElement.element.clientHeight > winHeight) {
                window.scrollBy(0, rect.top - ((winHeight / 2) - (rect.height / 2)) - this._options.scrollPadding); // 30px padding from edge to look nice

                //Scroll down
            } else {
                window.scrollBy(0, rect.top - ((winHeight / 2) - (rect.height / 2)) + this._options.scrollPadding); // 30px padding from edge to look nice
            }
        }
    }

    /**
     * To remove all show element(s)
     *
     * @api private
     * @method _removeShowElement
     */
    function _removeShowElement() {
        var elms = document.querySelectorAll('.introjs-showElement');

        _forEach(elms, function (elm) {
            _removeClass(elm, /introjs-[a-zA-Z]+/g);
        });
    }

    /**
     * To set the show element
     * This function set a relative (in most cases) position and changes the z-index
     *
     * @api private
     * @method _setShowElement
     * @param {Object} targetElement
     */
    function _setShowElement(targetElement) {
        var parentElm;
        // we need to add this show element class to the parent of SVG elements
        // because the SVG elements can't have independent z-index
        if (targetElement.element instanceof SVGElement) {
            parentElm = targetElement.element.parentNode;

            while (targetElement.element.parentNode !== null) {
                if (!parentElm.tagName || parentElm.tagName.toLowerCase() === 'body') break;

                if (parentElm.tagName.toLowerCase() === 'svg') {
                    _addClass(parentElm, 'introjs-showElement introjs-relativePosition');
                }

                parentElm = parentElm.parentNode;
            }
        }

        _addClass(targetElement.element, 'introjs-showElement');

        var currentElementPosition = _getPropValue(targetElement.element, 'position');
        if (currentElementPosition !== 'absolute' &&
            currentElementPosition !== 'relative' &&
            currentElementPosition !== 'fixed') {
            //change to new intro item
            _addClass(targetElement.element, 'introjs-relativePosition');
        }

        parentElm = targetElement.element.parentNode;
        while (parentElm !== null) {
            if (!parentElm.tagName || parentElm.tagName.toLowerCase() === 'body') break;

            //fix The Stacking Context problem.
            //More detail: https://developer.mozilla.org/en-US/docs/Web/Guide/CSS/Understanding_z_index/The_stacking_context
            var zIndex = _getPropValue(parentElm, 'z-index');
            var opacity = parseFloat(_getPropValue(parentElm, 'opacity'));
            var transform = _getPropValue(parentElm, 'transform') || _getPropValue(parentElm, '-webkit-transform') || _getPropValue(parentElm, '-moz-transform') || _getPropValue(parentElm, '-ms-transform') || _getPropValue(parentElm, '-o-transform');
            if (/[0-9]+/.test(zIndex) || opacity < 1 || (transform !== 'none' && transform !== undefined)) {
                _addClass(parentElm, 'introjs-fixParent');
            }

            parentElm = parentElm.parentNode;
        }
    }

    /**
     * Iterates arrays
     *
     * @param {Array} arr
     * @param {Function} forEachFnc
     * @param {Function} completeFnc
     * @return {Null}
     */
    function _forEach(arr, forEachFnc, completeFnc) {
        // in case arr is an empty query selector node list
        if (arr) {
            for (var i = 0, len = arr.length; i < len; i++) {
                forEachFnc(arr[i], i);
            }
        }

        if (typeof (completeFnc) === 'function') {
            completeFnc();
        }
    }

    /**
     * Mark any object with an incrementing number
     * used for keeping track of objects
     *
     * @param Object obj   Any object or DOM Element
     * @param String key
     * @return Object
     */
    var _stamp = (function () {
        var keys = {};
        return function stamp(obj, key) {

            // get group key
            key = key || 'introjs-stamp';

            // each group increments from 0
            keys[key] = keys[key] || 0;

            // stamp only once per object
            if (obj[key] === undefined) {
                // increment key for each new object
                obj[key] = keys[key]++;
            }

            return obj[key];
        };
    })();

    /**
     * DOMEvent Handles all DOM events
     *
     * methods:
     *
     * on - add event handler
     * off - remove event
     */
    var DOMEvent = (function () {
        function DOMEvent() {
            var events_key = 'introjs_event';

            /**
             * Gets a unique ID for an event listener
             *
             * @param Object obj
             * @param String type        event type
             * @param Function listener
             * @param Object context
             * @return String
             */
            this._id = function (obj, type, listener, context) {
                return type + _stamp(listener) + (context ? '_' + _stamp(context) : '');
            };

            /**
             * Adds event listener
             *
             * @param Object obj
             * @param String type        event type
             * @param Function listener
             * @param Object context
             * @param Boolean useCapture
             * @return null
             */
            this.on = function (obj, type, listener, context, useCapture) {
                var id = this._id.apply(this, arguments),
                    handler = function (e) {
                        return listener.call(context || obj, e || window.event);
                    };

                if ('addEventListener' in obj) {
                    obj.addEventListener(type, handler, useCapture);
                } else if ('attachEvent' in obj) {
                    obj.attachEvent('on' + type, handler);
                }

                obj[events_key] = obj[events_key] || {};
                obj[events_key][id] = handler;
            };

            /**
             * Removes event listener
             *
             * @param Object obj
             * @param String type        event type
             * @param Function listener
             * @param Object context
             * @param Boolean useCapture
             * @return null
             */
            this.off = function (obj, type, listener, context, useCapture) {
                var id = this._id.apply(this, arguments),
                    handler = obj[events_key] && obj[events_key][id];

                if (!handler) {
                    return;
                }

                if ('removeEventListener' in obj) {
                    obj.removeEventListener(type, handler, useCapture);
                } else if ('detachEvent' in obj) {
                    obj.detachEvent('on' + type, handler);
                }

                obj[events_key][id] = null;
            };
        }

        return new DOMEvent();
    })();

    /**
     * Append a class to an element
     *
     * @api private
     * @method _addClass
     * @param {Object} element
     * @param {String} className
     * @returns null
     */
    function _addClass(element, className) {
        if (element instanceof SVGElement) {
            // svg
            var pre = element.getAttribute('class') || '';

            element.setAttribute('class', pre + ' ' + className);
        } else {
            if (element.classList !== undefined) {
                // check for modern classList property
                var classes = className.split(' ');
                _forEach(classes, function (cls) {
                    element.classList.add(cls);
                });
            } else if (!element.className.match(className)) {
                // check if element doesn't already have className
                element.className += ' ' + className;
            }
        }
    }

    /**
     * Remove a class from an element
     *
     * @api private
     * @method _removeClass
     * @param {Object} element
     * @param {RegExp|String} classNameRegex can be regex or string
     * @returns null
     */
    function _removeClass(element, classNameRegex) {
        if (element instanceof SVGElement) {
            var pre = element.getAttribute('class') || '';

            element.setAttribute('class', pre.replace(classNameRegex, '').replace(/^\s+|\s+$/g, ''));
        } else {
            element.className = element.className.replace(classNameRegex, '').replace(/^\s+|\s+$/g, '');
        }
    }

    /**
     * Get an element CSS property on the page
     * Thanks to JavaScript Kit: http://www.javascriptkit.com/dhtmltutors/dhtmlcascade4.shtml
     *
     * @api private
     * @method _getPropValue
     * @param {Object} element
     * @param {String} propName
     * @returns Element's property value
     */
    function _getPropValue(element, propName) {
        var propValue = '';
        if (element.currentStyle) { //IE
            propValue = element.currentStyle[propName];
        } else if (document.defaultView && document.defaultView.getComputedStyle) { //Others
            propValue = document.defaultView.getComputedStyle(element, null).getPropertyValue(propName);
        }

        //Prevent exception in IE
        if (propValue && propValue.toLowerCase) {
            return propValue.toLowerCase();
        } else {
            return propValue;
        }
    }

    /**
     * Checks to see if target element (or parents) position is fixed or not
     *
     * @api private
     * @method _isFixed
     * @param {Object} element
     * @returns Boolean
     */
    function _isFixed(element) {
        var p = element.parentNode;

        if (!p || p.nodeName === 'HTML') {
            return false;
        }

        if (_getPropValue(element, 'position') === 'fixed') {
            return true;
        }

        return _isFixed(p);
    }

    /**
     * Provides a cross-browser way to get the screen dimensions
     * via: http://stackoverflow.com/questions/5864467/internet-explorer-innerheight
     *
     * @api private
     * @method _getWinSize
     * @returns {Object} width and height attributes
     */
    function _getWinSize() {
        if (window.innerWidth !== undefined) {
            return {width: window.innerWidth, height: window.innerHeight};
        } else {
            var D = document.documentElement;
            return {width: D.clientWidth, height: D.clientHeight};
        }
    }

    /**
     * Check to see if the element is in the viewport or not
     * http://stackoverflow.com/questions/123999/how-to-tell-if-a-dom-element-is-visible-in-the-current-viewport
     *
     * @api private
     * @method _elementInViewport
     * @param {Object} el
     */
    function _elementInViewport(el) {
        var rect = el.getBoundingClientRect();

        return (
            rect.top >= 0 &&
            rect.left >= 0 &&
            (rect.bottom + 80) <= window.innerHeight && // add 80 to get the text right
            rect.right <= window.innerWidth
        );
    }

    /**
     * Add overlay layer to the page
     *
     * @api private
     * @method _addOverlayLayer
     * @param {Object} targetElm
     */
    function _addOverlayLayer(targetElm) {
        var overlayLayer = document.createElement('div'),
            styleText = '',
            self = this;

        //set css class name
        overlayLayer.className = 'introjs-overlay';

        //check if the target element is body, we should calculate the size of overlay layer in a better way
        if (!targetElm.tagName || targetElm.tagName.toLowerCase() === 'body') {
            styleText += 'top: 0;bottom: 0; left: 0;right: 0;position: fixed;';
            overlayLayer.style.cssText = styleText;
        } else {
            //set overlay layer position
            var elementPosition = _getOffset(targetElm);
            if (elementPosition) {
                styleText += 'width: ' + elementPosition.width + 'px; height:' + elementPosition.height + 'px; top:' + elementPosition.top + 'px;left: ' + elementPosition.left + 'px;';
                overlayLayer.style.cssText = styleText;
            }
        }

        targetElm.appendChild(overlayLayer);

        overlayLayer.onclick = function () {
            if (self._options.exitOnOverlayClick === true) {
                _exitIntro.call(self, targetElm);
            }
        };

        window.setTimeout(function () {
            styleText += 'opacity: ' + self._options.overlayOpacity.toString() + ';';
            overlayLayer.style.cssText = styleText;
        }, 10);

        return true;
    }

    /**
     * Removes open hint (tooltip hint)
     *
     * @api private
     * @method _removeHintTooltip
     */
    function _removeHintTooltip() {
        var tooltip = document.querySelector('.introjs-hintReference');

        if (tooltip) {
            var step = tooltip.getAttribute('data-step');
            tooltip.parentNode.removeChild(tooltip);
            return step;
        }
    }

    /**
     * Start parsing hint items
     *
     * @api private
     * @param {Object} targetElm
     * @method _startHint
     */
    function _populateHints(targetElm) {

        this._introItems = [];

        if (this._options.hints) {
            _forEach(this._options.hints, function (hint) {
                var currentItem = _cloneObject(hint);

                if (typeof (currentItem.element) === 'string') {
                    //grab the element with given selector from the page
                    currentItem.element = document.querySelector(currentItem.element);
                }

                currentItem.hintPosition = currentItem.hintPosition || this._options.hintPosition;
                currentItem.hintAnimation = currentItem.hintAnimation || this._options.hintAnimation;

                if (currentItem.element !== null) {
                    this._introItems.push(currentItem);
                }
            }.bind(this));
        } else {
            var hints = targetElm.querySelectorAll('*[data-hint]');

            if (!hints || !hints.length) {
                return false;
            }

            //first add intro items with data-step
            _forEach(hints, function (currentElement) {
                // hint animation
                var hintAnimation = currentElement.getAttribute('data-hintanimation');

                if (hintAnimation) {
                    hintAnimation = (hintAnimation === 'true');
                } else {
                    hintAnimation = this._options.hintAnimation;
                }

                this._introItems.push({
                    element: currentElement,
                    hint: currentElement.getAttribute('data-hint'),
                    hintPosition: currentElement.getAttribute('data-hintposition') || this._options.hintPosition,
                    hintAnimation: hintAnimation,
                    tooltipClass: currentElement.getAttribute('data-tooltipclass'),
                    position: currentElement.getAttribute('data-position') || this._options.tooltipPosition
                });
            }.bind(this));
        }

        _addHints.call(this);

        /*
        todo:
        these events should be removed at some point
        */
        DOMEvent.on(document, 'click', _removeHintTooltip, this, false);
        DOMEvent.on(window, 'resize', _reAlignHints, this, true);
    }

    /**
     * Re-aligns all hint elements
     *
     * @api private
     * @method _reAlignHints
     */
    function _reAlignHints() {
        _forEach(this._introItems, function (item) {
            if (typeof (item.targetElement) === 'undefined') {
                return;
            }

            _alignHintPosition.call(this, item.hintPosition, item.element, item.targetElement);
        }.bind(this));
    }

    /**
     * Get a queryselector within the hint wrapper
     *
     * @param {String} selector
     * @return {NodeList|Array}
     */
    function _hintQuerySelectorAll(selector) {
        var hintsWrapper = document.querySelector('.introjs-hints');
        return (hintsWrapper) ? hintsWrapper.querySelectorAll(selector) : [];
    }

    /**
     * Hide a hint
     *
     * @api private
     * @method _hideHint
     */
    function _hideHint(stepId) {
        var hint = _hintQuerySelectorAll('.introjs-hint[data-step="' + stepId + '"]')[0];

        _removeHintTooltip.call(this);

        if (hint) {
            _addClass(hint, 'introjs-hidehint');
        }

        // call the callback function (if any)
        if (typeof (this._hintCloseCallback) !== 'undefined') {
            this._hintCloseCallback.call(this, stepId);
        }
    }

    /**
     * Hide all hints
     *
     * @api private
     * @method _hideHints
     */
    function _hideHints() {
        var hints = _hintQuerySelectorAll('.introjs-hint');

        _forEach(hints, function (hint) {
            _hideHint.call(this, hint.getAttribute('data-step'));
        }.bind(this));
    }

    /**
     * Show all hints
     *
     * @api private
     * @method _showHints
     */
    function _showHints() {
        var hints = _hintQuerySelectorAll('.introjs-hint');

        if (hints && hints.length) {
            _forEach(hints, function (hint) {
                _showHint.call(this, hint.getAttribute('data-step'));
            }.bind(this));
        } else {
            _populateHints.call(this, this._targetElement);
        }
    }

    /**
     * Show a hint
     *
     * @api private
     * @method _showHint
     */
    function _showHint(stepId) {
        var hint = _hintQuerySelectorAll('.introjs-hint[data-step="' + stepId + '"]')[0];

        if (hint) {
            _removeClass(hint, /introjs-hidehint/g);
        }
    }

    /**
     * Removes all hint elements on the page
     * Useful when you want to destroy the elements and add them again (e.g. a modal or popup)
     *
     * @api private
     * @method _removeHints
     */
    function _removeHints() {
        var hints = _hintQuerySelectorAll('.introjs-hint');

        _forEach(hints, function (hint) {
            _removeHint.call(this, hint.getAttribute('data-step'));
        }.bind(this));
    }

    /**
     * Remove one single hint element from the page
     * Useful when you want to destroy the element and add them again (e.g. a modal or popup)
     * Use removeHints if you want to remove all elements.
     *
     * @api private
     * @method _removeHint
     */
    function _removeHint(stepId) {
        var hint = _hintQuerySelectorAll('.introjs-hint[data-step="' + stepId + '"]')[0];

        if (hint) {
            hint.parentNode.removeChild(hint);
        }
    }

    /**
     * Add all available hints to the page
     *
     * @api private
     * @method _addHints
     */
    function _addHints() {
        var self = this;

        var hintsWrapper = document.querySelector('.introjs-hints');

        if (hintsWrapper === null) {
            hintsWrapper = document.createElement('div');
            hintsWrapper.className = 'introjs-hints';
        }

        /**
         * Returns an event handler unique to the hint iteration
         *
         * @param {Integer} i
         * @return {Function}
         */
        var getHintClick = function (i) {
            return function (e) {
                var evt = e ? e : window.event;

                if (evt.stopPropagation) {
                    evt.stopPropagation();
                }

                if (evt.cancelBubble !== null) {
                    evt.cancelBubble = true;
                }

                _showHintDialog.call(self, i);
            };
        };

        _forEach(this._introItems, function (item, i) {
            // avoid append a hint twice
            if (document.querySelector('.introjs-hint[data-step="' + i + '"]')) {
                return;
            }

            var hint = document.createElement('a');
            _setAnchorAsButton(hint);

            hint.onclick = getHintClick(i);

            hint.className = 'introjs-hint';

            if (!item.hintAnimation) {
                _addClass(hint, 'introjs-hint-no-anim');
            }

            // hint's position should be fixed if the target element's position is fixed
            if (_isFixed(item.element)) {
                _addClass(hint, 'introjs-fixedhint');
            }

            var hintDot = document.createElement('div');
            hintDot.className = 'introjs-hint-dot';
            var hintPulse = document.createElement('div');
            hintPulse.className = 'introjs-hint-pulse';

            hint.appendChild(hintDot);
            hint.appendChild(hintPulse);
            hint.setAttribute('data-step', i);

            // we swap the hint element with target element
            // because _setHelperLayerPosition uses `element` property
            item.targetElement = item.element;
            item.element = hint;

            // align the hint position
            _alignHintPosition.call(this, item.hintPosition, hint, item.targetElement);

            hintsWrapper.appendChild(hint);
        }.bind(this));

        // adding the hints wrapper
        document.body.appendChild(hintsWrapper);

        // call the callback function (if any)
        if (typeof (this._hintsAddedCallback) !== 'undefined') {
            this._hintsAddedCallback.call(this);
        }
    }

    /**
     * Aligns hint position
     *
     * @api private
     * @method _alignHintPosition
     * @param {String} position
     * @param {Object} hint
     * @param {Object} element
     */
    function _alignHintPosition(position, hint, element) {
        // get/calculate offset of target element
        var offset = _getOffset.call(this, element);
        var iconWidth = 20;
        var iconHeight = 20;

        // align the hint element
        switch (position) {
            default:
            case 'top-left':
                hint.style.left = offset.left + 'px';
                hint.style.top = offset.top + 'px';
                break;
            case 'top-right':
                hint.style.left = (offset.left + offset.width - iconWidth) + 'px';
                hint.style.top = offset.top + 'px';
                break;
            case 'bottom-left':
                hint.style.left = offset.left + 'px';
                hint.style.top = (offset.top + offset.height - iconHeight) + 'px';
                break;
            case 'bottom-right':
                hint.style.left = (offset.left + offset.width - iconWidth) + 'px';
                hint.style.top = (offset.top + offset.height - iconHeight) + 'px';
                break;
            case 'middle-left':
                hint.style.left = offset.left + 'px';
                hint.style.top = (offset.top + (offset.height - iconHeight) / 2) + 'px';
                break;
            case 'middle-right':
                hint.style.left = (offset.left + offset.width - iconWidth) + 'px';
                hint.style.top = (offset.top + (offset.height - iconHeight) / 2) + 'px';
                break;
            case 'middle-middle':
                hint.style.left = (offset.left + (offset.width - iconWidth) / 2) + 'px';
                hint.style.top = (offset.top + (offset.height - iconHeight) / 2) + 'px';
                break;
            case 'bottom-middle':
                hint.style.left = (offset.left + (offset.width - iconWidth) / 2) + 'px';
                hint.style.top = (offset.top + offset.height - iconHeight) + 'px';
                break;
            case 'top-middle':
                hint.style.left = (offset.left + (offset.width - iconWidth) / 2) + 'px';
                hint.style.top = offset.top + 'px';
                break;
        }
    }

    /**
     * Triggers when user clicks on the hint element
     *
     * @api private
     * @method _showHintDialog
     * @param {Number} stepId
     */
    function _showHintDialog(stepId) {
        var hintElement = document.querySelector('.introjs-hint[data-step="' + stepId + '"]');
        var item = this._introItems[stepId];

        // call the callback function (if any)
        if (typeof (this._hintClickCallback) !== 'undefined') {
            this._hintClickCallback.call(this, hintElement, item, stepId);
        }

        // remove all open tooltips
        var removedStep = _removeHintTooltip.call(this);

        // to toggle the tooltip
        if (parseInt(removedStep, 10) === stepId) {
            return;
        }

        var tooltipLayer = document.createElement('div');
        var tooltipTextLayer = document.createElement('div');
        var arrowLayer = document.createElement('div');
        var referenceLayer = document.createElement('div');

        tooltipLayer.className = 'introjs-tooltip';

        tooltipLayer.onclick = function (e) {
            //IE9 & Other Browsers
            if (e.stopPropagation) {
                e.stopPropagation();
            }
            //IE8 and Lower
            else {
                e.cancelBubble = true;
            }
        };

        tooltipTextLayer.className = 'introjs-tooltiptext';

        var tooltipWrapper = document.createElement('p');
        tooltipWrapper.innerHTML = item.hint;

        var closeButton = document.createElement('a');
        closeButton.className = this._options.buttonClass;
        closeButton.setAttribute('role', 'button');
        closeButton.innerHTML = this._options.hintButtonLabel;
        closeButton.onclick = _hideHint.bind(this, stepId);

        tooltipTextLayer.appendChild(tooltipWrapper);
        tooltipTextLayer.appendChild(closeButton);

        arrowLayer.className = 'introjs-arrow';
        tooltipLayer.appendChild(arrowLayer);

        tooltipLayer.appendChild(tooltipTextLayer);

        // set current step for _placeTooltip function
        this._currentStep = hintElement.getAttribute('data-step');

        // align reference layer position
        referenceLayer.className = 'introjs-tooltipReferenceLayer introjs-hintReference';
        referenceLayer.setAttribute('data-step', hintElement.getAttribute('data-step'));
        _setHelperLayerPosition.call(this, referenceLayer);

        referenceLayer.appendChild(tooltipLayer);
        document.body.appendChild(referenceLayer);

        //set proper position
        _placeTooltip.call(this, hintElement, tooltipLayer, arrowLayer, null, true);
    }

    /**
     * Get an element position on the page
     * Thanks to `meouw`: http://stackoverflow.com/a/442474/375966
     *
     * @api private
     * @method _getOffset
     * @param {Object} element
     * @returns Element's position info
     */
    function _getOffset(element) {
        var body = document.body;
        var docEl = document.documentElement;
        var scrollTop = window.pageYOffset || docEl.scrollTop || body.scrollTop;
        var scrollLeft = window.pageXOffset || docEl.scrollLeft || body.scrollLeft;
        var x = element.getBoundingClientRect();
        return {
            top: x.top + scrollTop,
            width: x.width,
            height: x.height,
            left: x.left + scrollLeft
        };
    }

    /**
     * Find the nearest scrollable parent
     * copied from https://stackoverflow.com/questions/35939886/find-first-scrollable-parent
     *
     * @param Element element
     * @return Element
     */
    function _getScrollParent(element) {
        var style = window.getComputedStyle(element);
        var excludeStaticParent = (style.position === "absolute");
        var overflowRegex = /(auto|scroll)/;

        if (style.position === "fixed") return document.body;

        for (var parent = element; (parent = parent.parentElement);) {
            style = window.getComputedStyle(parent);
            if (excludeStaticParent && style.position === "static") {
                continue;
            }
            if (overflowRegex.test(style.overflow + style.overflowY + style.overflowX)) return parent;
        }

        return document.body;
    }

    /**
     * scroll a scrollable element to a child element
     *
     * @param Element parent
     * @param Element element
     * @return Null
     */
    function _scrollParentToElement(parent, element) {
        parent.scrollTop = element.offsetTop - parent.offsetTop;
    }

    /**
     * Gets the current progress percentage
     *
     * @api private
     * @method _getProgress
     * @returns current progress percentage
     */
    function _getProgress() {
        // Steps are 0 indexed
        var currentStep = parseInt((this._currentStep + 1), 10);
        return ((currentStep / this._introItems.length) * 100);
    }

    /**
     * Overwrites obj1's values with obj2's and adds obj2's if non existent in obj1
     * via: http://stackoverflow.com/questions/171251/how-can-i-merge-properties-of-two-javascript-objects-dynamically
     *
     * @param obj1
     * @param obj2
     * @returns obj3 a new object based on obj1 and obj2
     */
    function _mergeOptions(obj1, obj2) {
        var obj3 = {},
            attrname;
        for (attrname in obj1) {
            obj3[attrname] = obj1[attrname];
        }
        for (attrname in obj2) {
            obj3[attrname] = obj2[attrname];
        }
        return obj3;
    }

    var introJs = function (targetElm) {
        var instance;

        if (typeof (targetElm) === 'object') {
            //Ok, create a new instance
            instance = new IntroJs(targetElm);

        } else if (typeof (targetElm) === 'string') {
            //select the target element with query selector
            var targetElement = document.querySelector(targetElm);

            if (targetElement) {
                instance = new IntroJs(targetElement);
            } else {
                throw new Error('There is no element with given selector.');
            }
        } else {
            instance = new IntroJs(document.body);
        }
        // add instance to list of _instances
        // passing group to _stamp to increment
        // from 0 onward somewhat reliably
        introJs.instances[_stamp(instance, 'introjs-instance')] = instance;

        return instance;
    };

    /**
     * Current IntroJs version
     *
     * @property version
     * @type String
     */
    introJs.version = VERSION;

    /**
     * key-val object helper for introJs instances
     *
     * @property instances
     * @type Object
     */
    introJs.instances = {};

    //Prototype
    introJs.fn = IntroJs.prototype = {
        clone: function () {
            return new IntroJs(this);
        },
        setOption: function (option, value) {
            this._options[option] = value;
            return this;
        },
        setOptions: function (options) {
            this._options = _mergeOptions(this._options, options);
            return this;
        },
        start: function (group) {
            _introForElement.call(this, this._targetElement, group);
            return this;
        },
        goToStep: function (step) {
            _goToStep.call(this, step);
            return this;
        },
        addStep: function (options) {
            if (!this._options.steps) {
                this._options.steps = [];
            }

            this._options.steps.push(options);

            return this;
        },
        addSteps: function (steps) {
            if (!steps.length) return;

            for (var index = 0; index < steps.length; index++) {
                this.addStep(steps[index]);
            }

            return this;
        },
        goToStepNumber: function (step) {
            _goToStepNumber.call(this, step);

            return this;
        },
        nextStep: function () {
            _nextStep.call(this);
            return this;
        },
        previousStep: function () {
            _previousStep.call(this);
            return this;
        },
        exit: function (force) {
            _exitIntro.call(this, this._targetElement, force);
            return this;
        },
        refresh: function () {
            _refresh.call(this);
            return this;
        },
        onbeforechange: function (providedCallback) {
            if (typeof (providedCallback) === 'function') {
                this._introBeforeChangeCallback = providedCallback;
            } else {
                throw new Error('Provided callback for onbeforechange was not a function');
            }
            return this;
        },
        onchange: function (providedCallback) {
            if (typeof (providedCallback) === 'function') {
                this._introChangeCallback = providedCallback;
            } else {
                throw new Error('Provided callback for onchange was not a function.');
            }
            return this;
        },
        onafterchange: function (providedCallback) {
            if (typeof (providedCallback) === 'function') {
                this._introAfterChangeCallback = providedCallback;
            } else {
                throw new Error('Provided callback for onafterchange was not a function');
            }
            return this;
        },
        oncomplete: function (providedCallback) {
            if (typeof (providedCallback) === 'function') {
                this._introCompleteCallback = providedCallback;
            } else {
                throw new Error('Provided callback for oncomplete was not a function.');
            }
            return this;
        },
        onhintsadded: function (providedCallback) {
            if (typeof (providedCallback) === 'function') {
                this._hintsAddedCallback = providedCallback;
            } else {
                throw new Error('Provided callback for onhintsadded was not a function.');
            }
            return this;
        },
        onhintclick: function (providedCallback) {
            if (typeof (providedCallback) === 'function') {
                this._hintClickCallback = providedCallback;
            } else {
                throw new Error('Provided callback for onhintclick was not a function.');
            }
            return this;
        },
        onhintclose: function (providedCallback) {
            if (typeof (providedCallback) === 'function') {
                this._hintCloseCallback = providedCallback;
            } else {
                throw new Error('Provided callback for onhintclose was not a function.');
            }
            return this;
        },
        onexit: function (providedCallback) {
            if (typeof (providedCallback) === 'function') {
                this._introExitCallback = providedCallback;
            } else {
                throw new Error('Provided callback for onexit was not a function.');
            }
            return this;
        },
        onskip: function (providedCallback) {
            if (typeof (providedCallback) === 'function') {
                this._introSkipCallback = providedCallback;
            } else {
                throw new Error('Provided callback for onskip was not a function.');
            }
            return this;
        },
        onbeforeexit: function (providedCallback) {
            if (typeof (providedCallback) === 'function') {
                this._introBeforeExitCallback = providedCallback;
            } else {
                throw new Error('Provided callback for onbeforeexit was not a function.');
            }
            return this;
        },
        addHints: function () {
            _populateHints.call(this, this._targetElement);
            return this;
        },
        hideHint: function (stepId) {
            _hideHint.call(this, stepId);
            return this;
        },
        hideHints: function () {
            _hideHints.call(this);
            return this;
        },
        showHint: function (stepId) {
            _showHint.call(this, stepId);
            return this;
        },
        showHints: function () {
            _showHints.call(this);
            return this;
        },
        removeHints: function () {
            _removeHints.call(this);
            return this;
        },
        removeHint: function (stepId) {
            _removeHint.call(this, stepId);
            return this;
        },
        showHintDialog: function (stepId) {
            _showHintDialog.call(this, stepId);
            return this;
        }
    };

    return introJs;
});
