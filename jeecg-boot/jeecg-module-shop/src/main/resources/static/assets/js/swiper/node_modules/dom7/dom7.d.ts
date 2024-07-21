export interface Dom7Array {
  /* ====== DEFAULT ARRAY METHODS ====== */
  /**
   * Gets or sets the length of the array. This is a number one higher than the highest element defined in an array.
   */
  length: number;

  /**
   * Removes the last element from an array and returns it.
   */
  pop(): Element | undefined;
  /**
   * Appends new elements to an array, and returns the new length of the array.
   * @param items New elements of the Array.
   */
  push(...items: Element[]): number;
  /**
   * Combines two or more arrays.
   * @param items Additional items to add to the end of array1.
   */
  concat(...items: ConcatArray<Element>[]): Element[];
  /**
   * Combines two or more arrays.
   * @param items Additional items to add to the end of array1.
   */
  concat(...items: (Element | ConcatArray<Element>)[]): Element[];
  /**
   * Adds all the elements of an array separated by the specified separator string.
   * @param separator A string used to separate one element of an array from the next in the resulting String. If omitted, the array elements are separated with a comma.
   */
  join(separator?: string): string;
  /**
   * Reverses the elements in an Array.
   */
  reverse(): Element[];
  /**
   * Removes the first element from an array and returns it.
   */
  shift(): Element | undefined;
  /**
   * Returns a section of an array.
   * @param start The beginning of the specified portion of the array.
   * @param end The end of the specified portion of the array. This is exclusive of the element at the index 'end'.
   */
  slice(start?: number, end?: number): Element[];
  /**
   * Sorts an array.
   * @param compareFn Function used to determine the order of the elements. It is expected to return
   * a negative value if first argument is less than second argument, zero if they're equal and a positive
   * value otherwise. If omitted, the elements are sorted in ascending, ASCII character order.
   * ```ts
   * [11,2,22,1].sort((a, b) => a - b)
   * ```
   */
  sort(compareFn?: (a: Element, b: Element) => number): this;
  /**
   * Removes elements from an array and, if necessary, inserts new elements in their place, returning the deleted elements.
   * @param start The zero-based location in the array from which to start removing elements.
   * @param deleteCount The number of elements to remove.
   */
  splice(start: number, deleteCount?: number): Element[];
  /**
   * Removes elements from an array and, if necessary, inserts new elements in their place, returning the deleted elements.
   * @param start The zero-based location in the array from which to start removing elements.
   * @param deleteCount The number of elements to remove.
   * @param items Elements to insert into the array in place of the deleted elements.
   */
  splice(start: number, deleteCount: number, ...items: Element[]): Element[];
  /**
   * Inserts new elements at the start of an array.
   * @param items  Elements to insert at the start of the Array.
   */
  unshift(...items: Element[]): number;
  /**
   * Returns the index of the first occurrence of a value in an array.
   * @param searchElement The value to locate in the array.
   * @param fromIndex The array index at which to begin the search. If fromIndex is omitted, the search starts at index 0.
   */
  indexOf(searchElement: Element, fromIndex?: number): number;
  /**
   * Returns the index of the last occurrence of a specified value in an array.
   * @param searchElement The value to locate in the array.
   * @param fromIndex The array index at which to begin the search. If fromIndex is omitted, the search starts at the last index in the array.
   */
  lastIndexOf(searchElement: Element, fromIndex?: number): number;
  /**
   * Determines whether all the members of an array satisfy the specified test.
   * @param callbackfn A function that accepts up to three arguments. The every method calls
   * the callbackfn function for each element in the array until the callbackfn returns a value
   * which is coercible to the Boolean value false, or until the end of the array.
   * @param thisArg An object to which the this keyword can refer in the callbackfn function.
   * If thisArg is omitted, undefined is used as the this value.
   */
  every(
    callbackfn: (value: Element, index: number, array: Element[]) => unknown,
    thisArg?: any,
  ): boolean;
  /**
   * Determines whether the specified callback function returns true for any element of an array.
   * @param callbackfn A function that accepts up to three arguments. The some method calls
   * the callbackfn function for each element in the array until the callbackfn returns a value
   * which is coercible to the Boolean value true, or until the end of the array.
   * @param thisArg An object to which the this keyword can refer in the callbackfn function.
   * If thisArg is omitted, undefined is used as the this value.
   */
  some(
    callbackfn: (value: Element, index: number, array: Element[]) => unknown,
    thisArg?: any,
  ): boolean;
  /**
   * Performs the specified action for each element in an array.
   * @param callbackfn  A function that accepts up to three arguments. forEach calls the callbackfn function one time for each element in the array.
   * @param thisArg  An object to which the this keyword can refer in the callbackfn function. If thisArg is omitted, undefined is used as the this value.
   */
  forEach(
    callbackfn: (value: Element, index: number, array: Element[]) => void,
    thisArg?: any,
  ): void;
  /**
   * Calls a defined callback function on each element of an array, and returns an array that contains the results.
   * @param callbackfn A function that accepts up to three arguments. The map method calls the callbackfn function one time for each element in the array.
   * @param thisArg An object to which the this keyword can refer in the callbackfn function. If thisArg is omitted, undefined is used as the this value.
   */
  map<U>(
    callbackfn: (value: Element, index: number, array: Element[]) => U,
    thisArg?: any,
  ): U[];

  /* ====== DOM7 ARRAY METHODS ====== */

  /**  Retrieve one of the elements matched by the Dom7Array object (jQuery syntax). **/
  [index: number]: Element;

  // CLASSES
  /** Add class to elements */
  addClass(className: string): Dom7Array;
  /** Remove specified class */
  removeClass(className: string): Dom7Array;
  /** Determine whether any of the matched elements are assigned the given class */
  hasClass(className: string): Dom7Array;
  /** Remove (if class is present) or add (if not) one or more classes from each element in the set of matched elements */
  toggleClass(className: string): Dom7Array;

  // ATTRIBUTES AND PROPERTIES
  /** Get property value */
  prop(propName: string): any;
  /** Set single property value */
  prop(propName: string, propValue: any): Dom7Array;
  /** Set multiple properties */
  prop(propertiesObject: any): Dom7Array;
  /** Get attribute value */
  attr(attrName: string): string;
  /** Set single attribute value */
  attr(attrName: string, attrValue: string): Dom7Array;
  /** Set multiple attributes */
  attr(attributesObject: any): Dom7Array;
  /** Remove specified attribute */
  removeAttr(attrName: string): Dom7Array;
  /** Get the current value of the first element in the set of matched elements */
  val(): any;
  /** Set the value of every matched element */
  val(newValue: any): Dom7Array;

  // DATA
  /** Store arbitrary data associated with the matched elements */
  data(key: string, value: any): Dom7Array;
  /** Return the value at the named data store for the first element in the collection, as set by data(key, value) or by an HTML5 data-* attribute */
  data(key: string): any;
  /** Remove specified data */
  removeData(key: string): void;
  /** Returns element's data set (set of data- attributes) as plain Object */
  dataset(): any;

  // CSS TRASFORMS, TRANSITIONS
  /** Adds prefixed CSS transform property */
  transform(CSSTransformString: string): Dom7Array;
  /** Set CSS transition-duration property to collection */
  transition(transitionDuration: number): Dom7Array;

  // EVENTS
  /** Add event handler function to one or more events to the selected elements */
  on(
    eventName: string,
    handler: (event: Event) => void,
    useCapture?: boolean,
  ): Dom7Array;
  /** Live/delegated event handler */
  on(
    eventName: string,
    delegatedTarget: string,
    handler: (event: Event) => void,
    useCapture?: boolean,
  ): Dom7Array;
  /** Add event handler function to one or more events to the selected elements that will be executed only once */
  once(
    eventName: string,
    handler: (event: Event) => void,
    useCapture?: boolean,
  ): Dom7Array;
  /** Live/delegated event handler that will be executed only once */
  once(
    eventName: string,
    delegatedTarget: string,
    handler: (event: Event) => void,
    useCapture?: boolean,
  ): Dom7Array;
  /** Remove event handler */
  off(
    eventName: string,
    handler: (event: Event) => void,
    useCapture?: boolean,
  ): Dom7Array;
  /** Remove live/delegated event handler */
  off(
    eventName: string,
    delegatedTarget: string,
    handler: (event: Event) => void,
    useCapture?: boolean,
  ): Dom7Array;
  /** Execute all handlers added to the matched elements for the specified event */
  trigger(eventName: string, eventData?: any): Dom7Array;
  /** Adds transitionEnd event handler to collection */
  transitionEnd(callback: () => void): Dom7Array;
  /** Adds animationEnd event handler to collection */
  animationEnd(callback: () => void): Dom7Array;

  // STYLES
  /** Get the current computed width for the first element in the set of matched elements */
  width(): number;
  /** Set width for the first element in the set of matched elements */
  width(value: string | number): Dom7Array;
  /** Get the current computed width for the first element in the set of matched elements, including padding and border, and margin (if includeMargin is true) */
  outerWidth(includeMargin?: boolean): number;
  /** Set width for the first element in the set of matched elements, including padding and border, and margin (if includeMargin is true) */
  outerWidth(value: string | number): Dom7Array;
  /** Get the current computed height for the first element in the set of matched elements */
  height(): number;
  /** Set height for the first element in the set of matched elements */
  height(value: string | number): Dom7Array;
  /** Get the current computed height for the first element in the set of matched elements, including padding and border, and margin (if includeMargin is true) */
  outerHeight(includeMargin?: boolean): number;
  /** Set height for the first element in the set of matched elements, including padding and border, and margin (if includeMargin is true) */
  outerHeight(value: string | number): Dom7Array;
  /** Get the current coordinates of the first element relative to the document */
  offset(): { top: number; left: number };
  /** Set the coordinates of the first element relative to the document */
  offset(value: string | number): Dom7Array;
  /** Set "display:none" to the matched elements */
  hide(): void;
  /** Set "display:block" to the matched elements */
  show(): void;
  /** Get value of specified CSS property for the first element */
  css(property: string): string | number;
  /** Set specified CSS property to the matched elements */
  css(property: string, value: string | number): Dom7Array;
  /** Set multiple CSS properties to the matched elements */
  css(propertiesObject: any): Dom7Array;

  // SCROLL
  /** Get scrollTop position of element */
  scrollTop(): number;
  /** Set scrollTop "position" with animation during "duration" (in ms). Scroll top position will be set immediately if duration is not specified. If you have specified "callback" function, then it will be executed after scrolling completed */
  scrollTop(
    position: number,
    duration?: number,
    callback?: () => void,
  ): Dom7Array;
  /** Get scrollLeft position of element */
  scrollLeft(): number;
  /** Set scrollLeft "position" with animation during "duration" (in ms). Scroll left postion will be set immediately if duration is not specified. If you have specified "callback" function, then it will be executed after scrolling completed */
  scrollLeft(
    position: number,
    duration?: number,
    callback?: () => void,
  ): Dom7Array;
  /** Set scroll left and scroll top with animation during "duration" (in ms). Scroll postion will be set immediately if duration is not specified. If you have specified "callback" function, then it will be executed after scrolling completed */
  scrollTo(
    left: number,
    top: number,
    duration?: number,
    callback?: () => void,
  ): Dom7Array;

  // DOM MANIPULATION
  /** Add HTML element to the set of matched elements */
  add(html: string): Dom7Array;
  /** Create a new Dom7Array collection with elements added to the set of matched elements */
  add(...elements: Array<Element | Dom7Array>): Dom7Array;
  /** Iterate over collection, executing a callback function for each matched element */
  each(callback: (element: any, index: number) => void): Dom7Array;
  /** Get the HTML contents of the first element in the set of matched elements */
  html(): string;
  /** Set the HTML contents of every matched element */
  html(newInnerHTML: string): Dom7Array;
  /** Get the text contents of the first element in the set of matched elements */
  text(): string;
  /** Set the text contents of every matched element */
  text(newTextContent: string): Dom7Array;
  /** `.is(CSSSelector)` :
   * Check the current matched set of elements against CSS selector
   *
   * `.is(HTMLElement)` :
   * Check the current matched set of elements against HTML element or Dom7Array collection
   * */
  is(CSSSelector: string | Element | Dom7Array): boolean;
  /** Return the position of the first element within the Dom7Array collection relative to its sibling elements */
  index(): number;
  /** Reduce the set of matched elements to the one at the specified index */
  eq(index: number): Dom7Array;
  /** `.append(HTMLString)` :
   * Insert content, specified by the parameter, to the end of each element in the set of matched elements
   *
   * `.append(HTMLElement)` :
   * Insert specified HTML element to the end of element in the set of matched elements
   * */
  append(element: string | Element | Dom7Array): Dom7Array;
  /** Insert content/elements, to the end of element specified in parameter */
  appendTo(element: string | Element | Dom7Array): Dom7Array;
  /** `.prepend(newHTML)` :
   * Insert content, specified by the parameter, to the beginning of each element in the set of matched elements
   *
   * `.prepend(HTMLElement)` :
   * Insert specified HTML element to the beginning of element in the set of matched elements
   * */
  prepend(element: string | Element | Dom7Array): Dom7Array;
  /** Insert content/elements, to the beginning of element specified in parameter */
  prependTo(element: string | Element | Dom7Array): Dom7Array;
  /** Insert every element in the set of matched elements before the target. Target could be specified as CSS selector or HTML element or Dom7Array collection */
  insertBefore(element: string | Element | Dom7Array): Dom7Array;
  /** Insert every element in the set of matched elements after the target. Target could be specified as CSS selector or HTML element or Dom7Array collection */
  insertAfter(element: string | Element | Dom7Array): Dom7Array;
  /** Get the immediately following sibling of each element in the set of matched elements. If a selector is provided, it retrieves the next sibling only if it matches that selector */
  next(selector?: string): Dom7Array;
  /** Get all following siblings of each element in the set of matched elements, optionally filtered by a selector */
  nextAll(selector?: string): Dom7Array;
  /** Get the immediately preceding sibling of each element in the set of matched elements, optionally filtered by a selector */
  prev(selector?: string): Dom7Array;
  /** Get all preceding siblings of each element in the set of matched elements, optionally filtered by a selector */
  prevAll(selector?: string): Dom7Array;
  /** Get the siblings of each element in the set of matched elements, optionally filtered by a selector */
  siblings(selector?: string): Dom7Array;
  /** Get the first parent of each element in the current set of matched elements, optionally filtered by a selector */
  parent(selector?: string): Dom7Array;
  /** Get the ancestors of each element in the current set of matched elements, optionally filtered by a selector */
  parents(selector?: string): Dom7Array;
  /** For each element in the set, get the first element that matches the selector by testing the element itself and traversing up through its ancestors in the DOM tree */
  closest(selector?: string): Dom7Array;
  /** Get the descendants of each element in the current set of matched elements, filtered by a selector */
  find(selector?: string): Dom7Array;
  /** Get the children of each element in the set of matched elements, optionally filtered by a selector */
  children(selector?: string): Dom7Array;
  /** Filter collection of elements */
  filter(callback: (element: any, index: number) => boolean): Dom7Array;
  /** Remove/detach matched elements from the Dom */
  remove(): Dom7Array;
  /** Remove all child nodes of the set of matched elements from the DOM. Alias for `.html('')` */
  empty(): Dom7Array;

  // SHORTCUTS
  /** Trigger "click" event on collection */
  click(): Dom7Array;
  /** Add "click" event handler to collection */
  click(handler: (event: Event) => void): Dom7Array;
  /** Trigger "blur" event on collection */
  blur(): Dom7Array;
  /** Add "blur" event handler to collection */
  blur(handler: (event: Event) => void): Dom7Array;
  /** Trigger "focus" event on collection */
  focus(): Dom7Array;
  /** Add "focus" event handler to collection */
  focus(handler: (event: Event) => void): Dom7Array;
  /** Trigger "focusin" event on collection */
  focusin(): Dom7Array;
  /** Add "focusin" event handler to collection */
  focusin(handler: (event: Event) => void): Dom7Array;
  /** Trigger "focusout" event on collection */
  focusout(): Dom7Array;
  /** Add "focusout" event handler to collection */
  focusout(handler: (event: Event) => void): Dom7Array;
  /** Trigger "keyup" event on collection */
  keyup(): Dom7Array;
  /** Add "keyup" event handler to collection */
  keyup(handler: (event: Event) => void): Dom7Array;
  /** Trigger "keydown" event on collection */
  keydown(): Dom7Array;
  /** Add "keydown" event handler to collection */
  keydown(handler: (event: Event) => void): Dom7Array;
  /** Trigger "keypress" event on collection */
  keypress(): Dom7Array;
  /** Add "keypress" event handler to collection */
  keypress(handler: (event: Event) => void): Dom7Array;
  /** Trigger "submit" event on collection */
  submit(): Dom7Array;
  /** Add "submit" event handler to collection */
  submit(handler: (event: Event) => void): Dom7Array;
  /** Trigger "change" event on collection */
  change(): Dom7Array;
  /** Add "change" event handler to collection */
  change(handler: (event: Event) => void): Dom7Array;
  /** Trigger "mousedown" event on collection */
  mousedown(): Dom7Array;
  /** Add "mousedown" event handler to collection */
  mousedown(handler: (event: Event) => void): Dom7Array;
  /** Trigger "mousemove" event on collection */
  mousemove(): Dom7Array;
  /** Add "mousemove" event handler to collection */
  mousemove(handler: (event: Event) => void): Dom7Array;
  /** Trigger "mouseup" event on collection */
  mouseup(): Dom7Array;
  /** Add "mouseup" event handler to collection */
  mouseup(handler: (event: Event) => void): Dom7Array;
  /** Trigger "mouseenter" event on collection */
  mouseenter(): Dom7Array;
  /** Add "mouseenter" event handler to collection */
  mouseenter(handler: (event: Event) => void): Dom7Array;
  /** Trigger "mouseleave" event on collection */
  mouseleave(): Dom7Array;
  /** Add "mouseleave" event handler to collection */
  mouseleave(handler: (event: Event) => void): Dom7Array;
  /** Trigger "mouseout" event on collection */
  mouseout(): Dom7Array;
  /** Add "mouseout" event handler to collection */
  mouseout(handler: (event: Event) => void): Dom7Array;
  /** Trigger "mouseover" event on collection */
  mouseover(): Dom7Array;
  /** Add "mouseover" event handler to collection */
  mouseover(handler: (event: Event) => void): Dom7Array;
  /** Trigger "touchstart" event on collection */
  touchstart(): Dom7Array;
  /** Add "touchstart" event handler to collection */
  touchstart(handler: (event: Event) => void): Dom7Array;
  /** Trigger "touchend" event on collection */
  touchend(): Dom7Array;
  /** Add "touchend" event handler to collection */
  touchend(handler: (event: Event) => void): Dom7Array;
  /** Trigger "touchmove" event on collection */
  touchmove(): Dom7Array;
  /** Add "touchmove" event handler to collection */
  touchmove(handler: (event: Event) => void): Dom7Array;
  /** Add "resize" event handler to collection */
  resize(handler: (event: Event) => void): Dom7Array;
  /** Add "scroll" event handler to collection */
  scroll(handler: (event: Event) => void): Dom7Array;
  /** Perform a custom animation of a set of CSS properties */
  animate(properties: any, parameters: any): Dom7Array;
}

export interface Dom7 {
  (): Dom7Array;
  (selector: string, context?: Element | Dom7Array): Dom7Array;
  (element: Element): Dom7Array;
  (element: Document): Dom7Array;
  (elementArray: Element[]): Dom7Array;
  (event: EventTarget): Dom7Array;
  fn: any;
}

declare const Dom7: Dom7;

declare const add: () => void;
declare const addClass: () => void;
declare const animate: () => void;
declare const animationEnd: () => void;
declare const append: () => void;
declare const appendTo: () => void;
declare const attr: () => void;
declare const blur: () => void;
declare const change: () => void;
declare const children: () => void;
declare const click: () => void;
declare const closest: () => void;
declare const css: () => void;
declare const data: () => void;
declare const dataset: () => void;
declare const detach: () => void;
declare const each: () => void;
declare const empty: () => void;
declare const eq: () => void;
declare const filter: () => void;
declare const find: () => void;
declare const focus: () => void;
declare const focusin: () => void;
declare const focusout: () => void;
declare const hasClass: () => void;
declare const height: () => void;
declare const hide: () => void;
declare const html: () => void;
declare const insertAfter: () => void;
declare const insertBefore: () => void;
declare const is: () => void;
declare const keydown: () => void;
declare const keypress: () => void;
declare const keyup: () => void;
declare const mousedown: () => void;
declare const mouseenter: () => void;
declare const mouseleave: () => void;
declare const mousemove: () => void;
declare const mouseout: () => void;
declare const mouseover: () => void;
declare const mouseup: () => void;
declare const next: () => void;
declare const nextAll: () => void;
declare const off: () => void;
declare const offset: () => void;
declare const on: () => void;
declare const once: () => void;
declare const outerHeight: () => void;
declare const outerWidth: () => void;
declare const parent: () => void;
declare const parents: () => void;
declare const prepend: () => void;
declare const prependTo: () => void;
declare const prev: () => void;
declare const prevAll: () => void;
declare const prop: () => void;
declare const remove: () => void;
declare const removeAttr: () => void;
declare const removeClass: () => void;
declare const removeData: () => void;
declare const resize: () => void;
declare const scroll: () => void;
declare const scrollLeft: () => void;
declare const scrollTo: () => void;
declare const scrollTop: () => void;
declare const show: () => void;
declare const siblings: () => void;
declare const stop: () => void;
declare const styles: () => void;
declare const submit: () => void;
declare const text: () => void;
declare const toggleClass: () => void;
declare const touchend: () => void;
declare const touchmove: () => void;
declare const touchstart: () => void;
declare const transform: () => void;
declare const transition: () => void;
declare const transitionEnd: () => void;
declare const trigger: () => void;
declare const val: () => void;
declare const value: () => void;
declare const width: () => void;

export {
  Dom7 as $,
  add,
  addClass,
  animate,
  animationEnd,
  append,
  appendTo,
  attr,
  blur,
  change,
  children,
  click,
  closest,
  css,
  data,
  dataset,
  detach,
  each,
  empty,
  eq,
  filter,
  find,
  focus,
  focusin,
  focusout,
  hasClass,
  height,
  hide,
  html,
  insertAfter,
  insertBefore,
  is,
  keydown,
  keypress,
  keyup,
  mousedown,
  mouseenter,
  mouseleave,
  mousemove,
  mouseout,
  mouseover,
  mouseup,
  next,
  nextAll,
  off,
  offset,
  on,
  once,
  outerHeight,
  outerWidth,
  parent,
  parents,
  prepend,
  prependTo,
  prev,
  prevAll,
  prop,
  remove,
  removeAttr,
  removeClass,
  removeData,
  resize,
  scroll,
  scrollLeft,
  scrollTo,
  scrollTop,
  show,
  siblings,
  stop,
  styles,
  submit,
  text,
  toggleClass,
  touchend,
  touchmove,
  touchstart,
  transform,
  transition,
  transitionEnd,
  trigger,
  val,
  value,
  width,
};
export default Dom7;
