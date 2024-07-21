# Dom7

### Minimalistic JavaScript library for DOM manipulation, with a jQuery-compatible API

Dom7 - is the default DOM manipulation library built-in [Framework7](https://framework7.io). It utilizes most edge and high-performance methods for DOM manipulation. You don’t need to learn something new, its usage is very simple because it has the same syntax as well known jQuery library with support of the most popular and widely used methods and jQuery-like chaining.

See [Framework7 Dom7](https://framework7.io/docs/dom7.html) documentation for usage examples and available methods.

## Installation

Dom7 can be installed with NPM:

```
npm install dom7 --save
```

## Usage

```js
// import Dom7 and methods you need
import { $, addClass, removeClass, toggleClass, on } from 'dom7';

// install methods
$.fn.addClass = addClass;
$.fn.removeClass = removeClass;
$.fn.toggleClass = toggleClass;
$.fn.on = on;

// use
$('p').addClass('custom-paragraph');

$('p').on('click', function() {
  $(this).toggleClass('custom-paragraph');
});
```

## Documentation

See [full documenation here](https://framework7.io/docs/dom7.html)

## Contribution

Yes please! See the [contributing guidelines](https://github.com/nolimits4web/dom7/blob/master/CONTRIBUTING.md) for details.

## Licence

This project is licensed under the terms of the [MIT license](https://github.com/nolimits4web/dom7/blob/master/LICENSE).
