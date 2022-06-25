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
(function (exports, ElementView_mjs, LinkView_mjs, Link_mjs, basic_mjs) {
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
        constructors: [],
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

            this.on('change:name change:attributes change:methods change:values change:size', function () {
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

        addConstructor: function (constructor) {
            this.get('constructors').push(constructor);
            this.trigger('change:methods');
        },

        setConstructors: function (constructors) {
            this.set('constructors', constructors);
            this.trigger('change:methods');
        },
        removeConstructor: function (constructor) {
            this.set('constructors', this.get('constructors').filter(attr => attr.id !== constructor.id));
            this.trigger('change:methods');
        },
        updateConstructor: function (constructor) {
            let index = this.get('constructors').map(function (x) {
                return x.id;
            }).indexOf(constructor.id);
            if (index !== -1) {
                let current = this.get('constructors')[index];

                this.get('constructors')[index] = constructor;
                this.trigger('change:methods');

                return {
                    reference: {parentId: this.get('id'), id: current.id},
                    data: {
                        method: constructor
                    },
                    type: 'UpdateCommand',
                    entityType: this.getType(),
                };
            }
        },

        updateRectangles: function (auto = false) {

            let attrs = this.get('attrs');

            console.log();

            let rects = [
                {type: 'name', text: this.getClassName()},
                {type: 'values', text: this.getEnumValues()},
                {type: 'attrs', text: this.get('attributes')},
                {type: 'methods', text: this.get('constructors').concat(this.get('methods'))}
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
                    maxLineLength = (Math.round(maxLineLength / 10) * 10)
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
                this.set('size', {width: maxLineLength+5, height: offsetY});
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
        },
        values: []
    }, {
        getClassName: function () {
            return ['<<Enum>>', this.get('name')];
        },
        getEnumValues: function () {
            return this.get('values');
        },
        addValue: function (value) {
            this.get('values').push(value);
            this.trigger('change:values');
        },
        setValues: function (values) {
            this.set('values', values);
            this.trigger('change:values');
        },
        removeValue: function (value) {
            this.set('values', this.get('values').filter(val => val.id !== value.id));
            this.trigger('change:values');
        },
        updateValue: function (value) {
            let index = this.get('values').map(function (x) {
                return x.id;
            }).indexOf(value.id);
            if (index !== -1) {
                this.get('values')[index] = value;
                this.trigger('change:values');
            }
        },
        getType: function () {
            return 'ENUM';
        },

    })

    let EnumView = ClassView;

    let CustomLink = Link_mjs.Link.define('uml.CustomLink', {
        attrs: {
            line: {
                strokeWidth: 1,
                stroke: '#333333',
                connection: true,
            },
            wrapper: {
                connection: true,
                strokeWidth: 10,

            },
        }
    }, {
        markup: [{
            tagName: 'path',
            selector: 'wrapper',
            attributes: {
                'fill': 'none',
                'cursor': 'pointer',
                'stroke': 'transparent'
            }
        }, {
            tagName: 'path',
            selector: 'line',
            attributes: {
                'fill': 'none',
                'pointer-events': 'none'
            }
        }]
    });


    let Association = CustomLink.define('uml.Association', {
        isDirected: false,
    }, {
        getType: function () {
            return 'ASSOCIATION'
        },
        setDirected: function (isDirected) {
            this.set('isDirected', isDirected);
        },
        swap: function () {
            let tmp = this.get('source');
            this.set('source', this.get('target'));
            this.set('target', tmp);

        }
    });

    let Generalization = CustomLink.define('uml.Generalization', {
        attrs: {
            line: {
                targetMarker: {
                    'type': 'path',
                    'd': 'M 20 -10 L 0 0 L 20 10 z',
                    'fill': 'white'
                }
            },
        }
    });


    let Realization = Generalization.define('uml.Realization', {
        attrs: {
            line: {
                strokeDasharray: '5,5',
            }
        }
    });

    let Aggregation = Association.define('uml.Aggregation', {
        attrs: {
            line: {
                targetMarker: {
                    d: 'M 40 0 L 20 10 L 0 00 L 20 -10 z',
                    fill: 'white'
                }
            }
        }
    });

    let Composition = Aggregation.define('uml.Composition', {
        attrs: {
            line: {
                targetMarker: {
                    fill: 'black'
                }
            }
        }
    });


    exports.Abstract = Abstract;
    exports.AbstractView = AbstractView;
    exports.Aggregation = Aggregation;
    exports.Association = Association;
    exports.Class = Class;
    exports.ClassView = ClassView;
    exports.Composition = Composition;
    exports.CustomLink = CustomLink;
    exports.Generalization = Generalization;
    exports.Realization = Realization;
    exports.Interface = Interface;
    exports.InterfaceView = InterfaceView;
    exports.Enum = Enum;
    exports.EnumView = EnumView;

}(this.joint.shapes.uml = this.joint.shapes.uml || {}, joint.dia, joint.dia, joint.dia, joint.shapes.basic));