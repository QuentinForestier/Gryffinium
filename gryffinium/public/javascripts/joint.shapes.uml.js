/*! JointJS v3.5.5 (2022-04-08) - JavaScript diagramming library


This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/


const fontFamiliy = 'Arial, Helvetica, sans-serif';
fontSize = 15
const umlColor = '#FFF7E1';


this.joint = this.joint || {};
this.joint.shapes = this.joint.shapes || {};
(function (exports, ElementView_mjs, Link_mjs, basic_mjs) {
    'use strict';

    let Class = basic_mjs.Generic.define('uml.Class', {
        attrs: {
            rect: {'width': 200},

            '.uml-class-name-rect': {'stroke': 'black', 'stroke-width': 1, 'fill': umlColor},
            '.uml-class-attrs-rect': {'stroke': 'black', 'stroke-width': 1, 'fill': umlColor},
            '.uml-class-methods-rect': {'stroke': 'black', 'stroke-width': 1, 'fill': umlColor},
            '.uml-class-values-rect': {'stroke': 'black', 'stroke-width': 1, 'fill': umlColor},

            '.uml-class-name-text': {
                'ref': '.uml-class-name-rect',
                'ref-y': .5,
                'ref-x': .5,
                'text-anchor': 'middle',
                'y-alignment': 'middle',
                'font-weight': 'bold',
                'fill': 'black',
                'font-size': fontSize,
                'font-family': fontFamiliy
            },
            '.uml-class-attrs-text': {
                'ref': '.uml-class-attrs-rect', 'ref-y': 5, 'ref-x': 5,
                'fill': 'black', 'font-size': fontSize, 'font-family': fontFamiliy
            },
            '.uml-class-methods-text': {
                'ref': '.uml-class-methods-rect', 'ref-y': 5, 'ref-x': 5,
                'fill': 'black', 'font-size': fontSize, 'font-family': fontFamiliy
            },
            '.uml-class-values-text': {
                'ref': '.uml-class-values-rect', 'ref-y': 5, 'ref-x': 5,
                'fill': 'black', 'font-size': fontSize, 'font-family': fontFamiliy
            }
        },

        name: [],
        attributes: [],
        methods: [],
        id: undefined,
        height: undefined,
    }, {
        markup: [
            '<g class="rotatable">',
            '<g class="scalable">',
            '<rect class="uml-class-name-rect"/>',
            '<rect class="uml-class-values-rect"/>',
            '<rect class="uml-class-attrs-rect"/>',
            '<rect class="uml-class-methods-rect"/>',
            '</g>',
            '<text class="uml-class-name-text"/>',
            '<text class="uml-class-values-text"/>',
            '<text class="uml-class-attrs-text text-truncate"/>',
            '<text class="uml-class-methods-text"/>',
            '</g>'
        ].join(''),

        initialize: function () {

            this.on('change:name change:attributes change:methods change:size', function () {
                this.updateRectangles();
                this.trigger('uml-update');
            }, this);

            this.on('change:autoresize', function () {
                this.updateRectangles(true);
                this.trigger('uml-update');
            }, this);

            this.updateRectangles(true);

            basic_mjs.Generic.prototype.initialize.apply(this, arguments);
        },

        getClassName: function () {
            return this.get('name');
        },

        addAttribute: function (attribute) {
            this.get('attributes').push(attribute);
            this.trigger('change:attributes');
        },

        setAttributes: function (attributes) {
            this.set('attributes', attributes);
            this.trigger('change:attributes');
        },
        removeAttribute: function (attribute) {
            this.set('attributes', this.get('attributes').filter(attr => attr.id !== attribute.id));
            this.trigger('change:attributes');
        },
        updateAttribute: function (attribute) {
            let index = this.get('attributes').map(function (x) {
                return x.id;
            }).indexOf(attribute.id);
            if (index !== -1) {
                this.get('attributes')[index] = attribute;
                this.trigger('change:attributes');
            }
        },
        addMethod: function (method) {
            this.get('methods').push(method);
            this.trigger('change:methods');
        },

        setMethods: function (methods) {
            this.set('methods', methods);
            this.trigger('change:methods');
        },
        removeMethod: function (method) {
            this.set('methods', this.get('methods').filter(attr => attr.id !== method.id));
            this.trigger('change:methods');
        },
        updateMethod: function (method) {
            let index = this.get('methods').map(function (x) {
                return x.id;
            }).indexOf(method.id);
            if (index !== -1) {
                let current = this.get('methods')[index];

                this.get('methods')[index] = method;
                this.trigger('change:methods');

                return {
                    reference: {parentId: this.get('id'), id: current.id},
                    data: {
                        method: method
                    },
                    type: 'UpdateCommand',
                    entityType: this.getType(),
                };
            }
        },

        updateRectangles: function (auto = false) {

            let attrs = this.get('attrs');

            let rects = [
                {type: 'name', text: this.getClassName()},
                {type: 'values', text: this.getEnumValues()},
                {type: 'attrs', text: this.get('attributes')},
                {type: 'methods', text: this.get('methods')}
            ];

            let offsetY = 0;
            let width = this.get('size').width;


            const span = document.getElementById('measure')
            span.fontSize = fontSize
            span.fontFamily = fontFamiliy

            let maxLineLength = 0;
            rects.forEach(function (rect) {
                if (rect.type === 'values' && rect.text === "")
                    return;

                let lines = Array.isArray(rect.text) ? rect.text : [rect.text];

                let rectHeight = lines.length * fontSize + fontSize;

                let linesToShow = []

                for (let line of lines) {
                    span.innerText = line.toString();
                    let lineSize = $(span).width();

                    let lastTry = line.toString() + (lineSize <= width || auto ? '' : '...');
                    if (!auto) {
                        while (lineSize > width) {
                            lastTry = lastTry.substring(0, lastTry.length - 4) + '...';
                            span.innerText = lastTry
                            lineSize = $(span).width();
                        }
                    }
                    linesToShow.push(lastTry)
                    maxLineLength = Math.max(maxLineLength, lineSize)
                    maxLineLength = Math.round(maxLineLength / 10) * 10
                }
                attrs['.uml-class-' + rect.type + '-text'].text = linesToShow.join('\n');

                attrs['.uml-class-' + rect.type + '-rect'].height = rectHeight;
                attrs['.uml-class-' + rect.type + '-rect'].transform = 'translate(0,' + offsetY + ')';


                offsetY += rectHeight;

            });
            if (offsetY < 100) {
                offsetY = 100;
            }
            if (auto) {
                maxLineLength = Math.max(maxLineLength, 150)
                this.set('size', {width: maxLineLength, height: offsetY});
            }

            this.set('height', offsetY);

        },

        getEnumValues: function () {
            return "";
        },

        getHeight: function () {
            return this.get('height');
        },

        setSizeAndPosition(size = undefined, position = undefined) {
            if (size)
                this.resize(size.width, size.height);

            if (position)
                this.position(position.x, position.y);


            this.trigger('change:size');

            return {
                reference: {id: this.get('id')},
                data: {
                    x: this.get('position').x,
                    y: this.get('position').y,
                    width: this.get('size').width,
                    height: this.get('size').height
                },
                type: 'UpdateCommand',
                entityType: this.getType(),
            };
        },

        getType: function () {
            return 'CLASS';
        }
    });


    let ClassView = ElementView_mjs.ElementView.extend({

        initialize: function () {

            ElementView_mjs.ElementView.prototype.initialize.apply(this, arguments);

            this.listenTo(this.model, 'uml-update', function () {
                this.update();
                this.resize();
            });
        }
    });

    let Abstract = Class.define('uml.Abstract', {
        attrs: {
            '.uml-class-name-rect': {fill: umlColor},
            '.uml-class-attrs-rect': {fill: umlColor},
            '.uml-class-methods-rect': {fill: umlColor}
        }
    }, {

        getClassName: function () {
            return ['<<Abstract>>', this.get('name')];
        },
        getType: function () {
            return 'ABSTRACT_CLASS';
        }

    });
    let AbstractView = ClassView;

    let Interface = Class.define('uml.Interface', {
        attrs: {
            '.uml-class-name-rect': {fill: umlColor},
            '.uml-class-attrs-rect': {fill: umlColor},
            '.uml-class-methods-rect': {fill: umlColor}
        }
    }, {
        getClassName: function () {
            return ['<<Interface>>', this.get('name')];
        },
        getType: function () {
            return 'INTERFACE';
        }
    });
    let InterfaceView = ClassView;

    let Enum = Class.define('uml.Enum', {
        attrs: {
            '.uml-class-name-rect': {fill: umlColor},
            '.uml-class-attrs-rect': {fill: umlColor},
            '.uml-class-methods-rect': {fill: umlColor}
        }
    }, {
        getClassName: function () {
            return ['<<Enum>>', this.get('name')];
        },
        getEnumValues: function () {
            return ["Val1", "Val2", "Val3"];
        },
        getType: function () {
            return 'ENUM';
        }
    })

    let EnumView = ClassView;

    let Generalization = Link_mjs.Link.define('uml.Generalization', {
        attrs: {'.marker-target': {d: 'M 20 0 L 0 10 L 20 20 z', fill: 'white'}}
    });

    let Implementation = Link_mjs.Link.define('uml.Implementation', {
        attrs: {
            '.marker-target': {d: 'M 20 0 L 0 10 L 20 20 z', fill: 'white'},
            '.connection': {'stroke-dasharray': '3,3'}
        }
    });

    let Aggregation = Link_mjs.Link.define('uml.Aggregation', {
        attrs: {'.marker-target': {d: 'M 40 10 L 20 20 L 0 10 L 20 0 z', fill: 'white'}}
    });

    let Composition = Link_mjs.Link.define('uml.Composition', {
        attrs: {'.marker-target': {d: 'M 40 10 L 20 20 L 0 10 L 20 0 z', fill: 'black'}}
    });

    let Association = Link_mjs.Link.define('uml.Association');

    // Statechart

    let State = basic_mjs.Generic.define('uml.State', {
        attrs: {
            '.uml-state-body': {
                'width': 200, 'height': 200, 'rx': 10, 'ry': 10,
                'fill': '#ecf0f1', 'stroke': '#bdc3c7', 'stroke-width': 3
            },
            '.uml-state-separator': {
                'stroke': '#bdc3c7', 'stroke-width': 2
            },
            '.uml-state-name': {
                'ref': '.uml-state-body', 'ref-x': .5, 'ref-y': 5, 'text-anchor': 'middle',
                'fill': '#000000', 'font-family': 'Courier New', 'font-size': 14
            },
            '.uml-state-events': {
                'ref': '.uml-state-separator', 'ref-x': 5, 'ref-y': 5,
                'fill': '#000000', 'font-family': 'Courier New', 'font-size': 14
            }
        },

        name: 'State',
        events: []

    }, {
        markup: [
            '<g class="rotatable">',
            '<g class="scalable">',
            '<rect class="uml-state-body"/>',
            '</g>',
            '<path class="uml-state-separator"/>',
            '<text class="uml-state-name"/>',
            '<text class="uml-state-events"/>',
            '</g>'
        ].join(''),

        initialize: function () {

            this.on({
                'change:name': this.updateName,
                'change:events': this.updateEvents,
                'change:size': this.updatePath
            }, this);

            this.updateName();
            this.updateEvents();
            this.updatePath();

            basic_mjs.Generic.prototype.initialize.apply(this, arguments);
        },

        updateName: function () {

            this.attr('.uml-state-name/text', this.get('name'));
        },

        updateEvents: function () {

            this.attr('.uml-state-events/text', this.get('events').join('\n'));
        },

        updatePath: function () {

            let d = 'M 0 20 L ' + this.get('size').width + ' 20';

            // We are using `silent: true` here because updatePath() is meant to be called
            // on resize and there's no need to to update the element twice (`change:size`
            // triggers also an update).
            this.attr('.uml-state-separator/d', d, {silent: true});
        }
    });

    let StartState = basic_mjs.Circle.define('uml.StartState', {
        type: 'uml.StartState',
        attrs: {circle: {'fill': '#34495e', 'stroke': '#2c3e50', 'stroke-width': 2, 'rx': 1}}
    });

    let EndState = basic_mjs.Generic.define('uml.EndState', {
        size: {width: 20, height: 20},
        attrs: {
            'circle.outer': {
                transform: 'translate(10, 10)',
                r: 10,
                fill: '#ffffff',
                stroke: '#2c3e50'
            },

            'circle.inner': {
                transform: 'translate(10, 10)',
                r: 6,
                fill: '#34495e'
            }
        }
    }, {
        markup: '<g class="rotatable"><g class="scalable"><circle class="outer"/><circle class="inner"/></g></g>',
    });

    let Transition = Link_mjs.Link.define('uml.Transition', {
        attrs: {
            '.marker-target': {d: 'M 10 0 L 0 5 L 10 10 z', fill: '#34495e', stroke: '#2c3e50'},
            '.connection': {stroke: '#2c3e50'}
        }
    });

    exports.Abstract = Abstract;
    exports.AbstractView = AbstractView;
    exports.Aggregation = Aggregation;
    exports.Association = Association;
    exports.Class = Class;
    exports.ClassView = ClassView;
    exports.Composition = Composition;
    exports.EndState = EndState;
    exports.Generalization = Generalization;
    exports.Implementation = Implementation;
    exports.Interface = Interface;
    exports.InterfaceView = InterfaceView;
    exports.Enum = Enum;
    exports.EnumView = EnumView;
    exports.StartState = StartState;
    exports.State = State;
    exports.Transition = Transition;

}(this.joint.shapes.uml = this.joint.shapes.uml || {}, joint.dia, joint.dia, joint.shapes.basic));