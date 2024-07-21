/*
	jQuery Tags Input Plugin 1.3.3
	Copyright (c) 2011 XOXCO, Inc
	Documentation for this plugin lives here:
	http://xoxco.com/clickable/jquery-tags-input
	Licensed under the MIT license:
	http://www.opensource.org/licenses/mit-license.php
	ben@xoxco.com
*/
(function (factory) {
    if (typeof define === 'function' && define.amd) {
        // AMD. Register as an anonymous module.
        define(['jquery'], factory);
    } else if (typeof exports === 'object') {
        // Node/CommonJS style for Browserify
        module.exports = factory;
    } else if (window.layui && layui.define) {  // layui加载
        layui.define('jquery', function (exports) {
            var $ = layui.jquery;
            layui.link(layui.cache.base + 'tagsInput/tagsInput.css');
            exports('tagsInput', factory($));
        });
    } else {
        // Browser globals
        factory(jQuery);
    }
}(function ($) {
    var jQuery = $;
    var delimiter = new Array();
    var tags_callbacks = new Array();
    $.fn.doAutosize = function (o) {
        var minWidth = $(this).data('minwidth'),
            maxWidth = $(this).data('maxwidth'),
            val = '',
            input = $(this),
            testSubject = $('#' + $(this).data('tester_id'));

        if (val === (val = input.val())) {
            return;
        }

        // Enter new content into testSubject
        var escaped = val.replace(/&/g, '&amp;').replace(/\s/g, ' ').replace(/</g, '&lt;').replace(/>/g, '&gt;');
        testSubject.html(escaped);
        // Calculate new width + whether to change
        var testerWidth = testSubject.width(),
            newWidth = (testerWidth + o.comfortZone) >= minWidth ? testerWidth + o.comfortZone : minWidth,
            currentWidth = input.width(),
            isValidWidthChange = (newWidth < currentWidth && newWidth >= minWidth)
                || (newWidth > minWidth && newWidth < maxWidth);

        // Animate width
        if (isValidWidthChange) {
            input.width(newWidth);
        }


    };
    $.fn.resetAutosize = function (options) {
        var minWidth = $(this).data('minwidth') || options.minInputWidth || $(this).width(),
            maxWidth = $(this).data('maxwidth') || options.maxInputWidth || ($(this).closest('.tagsinput').width() - options.inputPadding),
            val = '',
            input = $(this),
            testSubject = $('<tester/>').css({
                position: 'absolute',
                top: -9999,
                left: -9999,
                width: 'auto',
                fontSize: input.css('fontSize'),
                fontFamily: input.css('fontFamily'),
                fontWeight: input.css('fontWeight'),
                letterSpacing: input.css('letterSpacing'),
                whiteSpace: 'nowrap'
            }),
            testerId = $(this).attr('id') + '_autosize_tester';
        if (!$('#' + testerId).length > 0) {
            testSubject.attr('id', testerId);
            testSubject.appendTo('body');
        }

        // input.data('minwidth', minWidth);
        input.data('maxwidth', maxWidth);
        input.data('tester_id', testerId);
        // input.css('width', minWidth);
    };

    $.fn.addTag = function (value, options) {
        options = jQuery.extend({focus: false, callback: true}, options);
        this.each(function () {
            var id = $(this).attr('id');

            var tagslist = $(this).val().split(delimiter[id]);
            if (tagslist[0] == '') {
                tagslist = new Array();
            }

            value = jQuery.trim(value);

            if (options.unique) {
                var skipTag = $(this).tagExist(value);
                if (skipTag == true) {
                    //Marks fake input as not_valid to let styling it
                    $('#' + id + '_tag').addClass('not_valid');
                }
            } else {
                var skipTag = false;
            }

            if (value != '' && skipTag != true) {
                var closeText = '<span class="default-close-text">&times;</span>';
                if (layui) {
                    closeText = '<i class="layui-icon layui-icon-close"></i>';
                }
                $('<span>').addClass('tag').append(
                    $('<span>').text(value).append('&nbsp;&nbsp;'),
                    $('<a>', {
                        href: 'javascript:;',
                        title: '移除',
                        html: closeText
                    }).click(function () {
                        return $('#' + id).removeTag(escape(value));
                    })
                ).insertBefore('#' + id + '_addTag');

                tagslist.push(value);

                $('#' + id + '_tag').val('');
                $('#' + id + '_tag').parent().find('.tagsinput-tip-list').remove();
                if (options.focus) {
                    $('#' + id + '_tag').focus();
                } else {
                    $('#' + id + '_tag').blur();
                }

                $.fn.tagsInput.updateTagsField(this, tagslist);

                if (options.callback && tags_callbacks[id] && tags_callbacks[id]['onAddTag']) {
                    var f = tags_callbacks[id]['onAddTag'];
                    f.call(this, value);
                }
                if (tags_callbacks[id] && tags_callbacks[id]['onChange']) {
                    var i = tagslist.length;
                    var f = tags_callbacks[id]['onChange'];
                    f.call(this, $(this), tagslist[i - 1]);
                }
            }

        });

        return false;
    };

    $.fn.removeTag = function (value) {
        value = unescape(value);
        this.each(function () {
            var id = $(this).attr('id');

            var old = $(this).val().split(delimiter[id]);

            $('#' + id + '_tagsinput .tag').remove();
            str = '';
            for (i = 0; i < old.length; i++) {
                if (old[i] != value) {
                    str = str + delimiter[id] + old[i];
                }
            }

            $.fn.tagsInput.importTags(this, str);

            if (tags_callbacks[id] && tags_callbacks[id]['onRemoveTag']) {
                var f = tags_callbacks[id]['onRemoveTag'];
                f.call(this, value);
            }
        });

        return false;
    };

    $.fn.tagExist = function (val) {
        var id = $(this).attr('id');
        var tagslist = $(this).val().split(delimiter[id]);
        return (jQuery.inArray(val, tagslist) >= 0); //true when tag exists, false when not
    };

    // clear all existing tags and import new ones from a string
    $.fn.importTags = function (str) {
        var id = $(this).attr('id');
        $('#' + id + '_tagsinput .tag').remove();
        $.fn.tagsInput.importTags(this, str);
    }

    $.fn.tagsInput = function (options) {
        if (options && options.skin == 'tagsinput-default' && options.focusWithClick == undefined) {
            options.focusWithClick = false;
        }
        var settings = jQuery.extend({
            interactive: true,
            defaultText: '+请输入',
            minChars: 0,
            width: '',
            height: '',
            autocomplete: {type: 'get', data: {}},
            hide: true,
            delimiter: ',',
            unique: true,
            removeWithBackspace: false,
            focusWithClick: true,
            placeholderColor: '#666666',
            autosize: true,
            comfortZone: 20,
            inputPadding: 6 * 2,
            skin: ''
        }, options);

        var uniqueIdCounter = 0;

        this.each(function () {
            // If we have already initialized the field, do not do it again
            if (typeof $(this).attr('data-tagsinput-init') !== 'undefined') {
                return;
            }

            // Mark the field as having been initialized
            $(this).attr('data-tagsinput-init', true);

            if (settings.hide) {
                $(this).hide();
                $(this).addClass('ew-tagsinput-hide');
            }
            var id = $(this).attr('id');
            if (!id || delimiter[$(this).attr('id')]) {
                id = $(this).attr('id', 'tags' + new Date().getTime() + (uniqueIdCounter++)).attr('id');
            }

            var data = jQuery.extend({
                pid: id,
                real_input: '#' + id,
                holder: '#' + id + '_tagsinput',
                input_wrapper: '#' + id + '_addTag',
                fake_input: '#' + id + '_tag'
            }, settings);

            delimiter[id] = data.delimiter;

            if (settings.onAddTag || settings.onRemoveTag || settings.onChange) {
                tags_callbacks[id] = new Array();
                tags_callbacks[id]['onAddTag'] = settings.onAddTag;
                tags_callbacks[id]['onRemoveTag'] = settings.onRemoveTag;
                tags_callbacks[id]['onChange'] = settings.onChange;
            }

            var markup = '<div id="' + id + '_tagsinput" class="tagsinput ' + settings.skin + '"><div id="' + id + '_addTag">';

            if (settings.interactive) {
                markup = markup + '<input id="' + id + '_tag" value="" placeholder="' + settings.defaultText + '" autocomplete="off" />';
            }

            markup = markup + '</div></div>';

            $(markup).insertAfter(this);
            $(data.holder).css('width', settings.width);
            $(data.holder).css('min-height', settings.height);
            $(data.holder).css('height', settings.height);

            if ($(data.real_input).val() != '') {
                $.fn.tagsInput.importTags($(data.real_input), $(data.real_input).val());
            }
            if (settings.interactive) {
                $(data.fake_input).resetAutosize(settings);

                if (settings.focusWithClick) {
                    $(data.holder).bind('click', data, function (event) {
                        $(event.data.fake_input).focus();
                    });
                }

                if (settings.autocomplete_url != undefined) {
                    $(data.fake_input).on('input', function () {
                        var name = $(this).val();
                        if (!name) {
                            $(data.fake_input).parent().find('.tagsinput-tip-list').remove();
                            return;
                        }
                        settings.autocomplete.data.name = name;
                        $.ajax({
                            url: settings.autocomplete_url,
                            type: settings.autocomplete.type,
                            data: settings.autocomplete.data,
                            dataType: 'json',
                            success: function (res) {
                                if (res.code == 200) {
                                    var $tipList = $(data.fake_input).parent().find('.tagsinput-tip-list');
                                    if (res.data.length == 0) {
                                        $tipList.remove();
                                    }
                                    var htmlStr = '';
                                    for (var i = 0; i < res.data.length; i++) {
                                        var item = res.data[i];
                                        htmlStr += '<li data-value="' + item + '">' + item + '</li>';
                                    }
                                    if ($tipList.length <= 0) {
                                        $(data.fake_input).after('<ul class="tagsinput-tip-list"></ul>');
                                        $tipList = $(data.fake_input).parent().find('.tagsinput-tip-list');
                                        $tipList.on('click', '>li', function () {
                                            var value = $(this).data('value');
                                            $('#' + id).addTag(value, {focus: true, unique: (settings.unique)});
                                            $tipList.remove();
                                        });
                                    }
                                    $tipList.html(htmlStr);
                                } else {
                                    console.error(res);
                                }
                            }
                        });
                    });

                } else {
                    // if a user tabs out of the field, create a new tag
                    // this is only available if autocomplete is not used.
                    $(data.fake_input).bind('blur', data, function (event) {
                        // var d = $(this).attr('data-default');
                        if ($(event.data.fake_input).val() != ''/* && $(event.data.fake_input).val() != d*/) {
                            if ((event.data.minChars <= $(event.data.fake_input).val().length) && (!event.data.maxChars || (event.data.maxChars >= $(event.data.fake_input).val().length)))
                                $(event.data.real_input).addTag($(event.data.fake_input).val(), {
                                    focus: false,
                                    unique: (settings.unique)
                                });
                        }
                        return false;
                    });

                }
                // if user types a default delimiter like comma,semicolon and then create a new tag
                $(data.fake_input).bind('keypress', data, function (event) {
                    if (_checkDelimiter(event)) {
                        event.preventDefault();
                        if ((event.data.minChars <= $(event.data.fake_input).val().length) && (!event.data.maxChars || (event.data.maxChars >= $(event.data.fake_input).val().length)))
                            $(event.data.real_input).addTag($(event.data.fake_input).val(), {
                                focus: true,
                                unique: (settings.unique)
                            });
                        $(event.data.fake_input).resetAutosize(settings);
                        return false;
                    } else if (event.data.autosize) {
                        $(event.data.fake_input).doAutosize(settings);

                    }
                });
                //Delete last tag on backspace
                data.removeWithBackspace && $(data.fake_input).bind('keydown', function (event) {
                    if (event.keyCode == 8 && $(this).val() == '') {
                        event.preventDefault();
                        var last_tag = $(this).closest('.tagsinput').find('.tag:last').text();
                        var id = $(this).attr('id').replace(/_tag$/, '');
                        last_tag = last_tag.replace(/[\s]+x$/, '');
                        last_tag = last_tag.replace(/\s/g, '');
                        $('#' + id).removeTag(escape(last_tag));
                        $(this).trigger('focus');
                    }
                });
                $(data.fake_input).blur();

                //Removes the not_valid class when user changes the value of the fake input
                if (data.unique) {
                    $(data.fake_input).keydown(function (event) {
                        if (event.keyCode == 8 || String.fromCharCode(event.which).match(/\w+|[áéíóúÁÉÍÓÚñÑ,/]+/)) {
                            $(this).removeClass('not_valid');
                        }
                    });
                }
            } // if settings.interactive
        });

        return this;

    };

    $.fn.tagsInput.updateTagsField = function (obj, tagslist) {
        var id = $(obj).attr('id');
        $(obj).val(tagslist.join(delimiter[id]));
    };

    $.fn.tagsInput.importTags = function (obj, val) {
        $(obj).val('');
        var id = $(obj).attr('id');
        var tags = val.split(delimiter[id]);
        for (i = 0; i < tags.length; i++) {
            $(obj).addTag(tags[i], {focus: false, callback: false});
        }
        if (tags_callbacks[id] && tags_callbacks[id]['onChange']) {
            var f = tags_callbacks[id]['onChange'];
            f.call(obj, obj, tags[i]);
        }
    };

    /**
     * check delimiter Array
     * @param event
     * @returns {boolean}
     * @private
     */
    var _checkDelimiter = function (event) {
        var found = false;
        if (event.which == 13) {
            return true;
        }

        if (typeof event.data.delimiter === 'string') {
            if (event.which == event.data.delimiter.charCodeAt(0)) {
                found = true;
            }
        } else {
            $.each(event.data.delimiter, function (index, delimiter) {
                if (event.which == delimiter.charCodeAt(0)) {
                    found = true;
                }
            });
        }

        return found;
    }
}));
