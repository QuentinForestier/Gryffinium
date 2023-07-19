/*! JointJS v3.5.5 (2022-04-08) - JavaScript diagramming library


This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/


const fontFamiliy = 'Helvetica, sans-serif';
const fontSize = 15
const umlColor = '#FFF7E1';
const selectedUmlColor = '#bfb9a8';


this.joint = this.joint || {};
this.joint.shapes = this.joint.shapes || {};
(function (exports, ElementView_mjs, LinkView_mjs, Link_mjs, basic_mjs) {
        'use strict';

        let Form = basic_mjs.Generic.define('uml.Form', {
            attrs: {
                body: {
                    width: 'calc(w)',
                    height: 'calc(h)',
                },
                foreignObject: {
                    width: '200',
                    height: '100',
                    x: 6,
                    y: 6
                }
            },
        }, {
            markup: [
                {
                    tagName: 'rect',
                    selector: 'body'
                },
                {
                    tagName: 'foreignObject',
                    selector: 'foreignObject',
                    children: [
                        {
                            tagName: 'div',
                            namespaceURI: 'http://www.w3.org/1999/xhtml',
                            selector: 'background',
                            style: {
                                backgroundColor: umlColor,
                                border: '1px solid black',
                                height: '100%'
                            },
                            children: [
                                {
                                    tagName: 'div',
                                    selector: 'header'
                                },

                                {
                                    tagName: 'input',
                                    selector: 'header',
                                    style: {
                                        fontStyle:'italic',
                                        color: '#000000',
                                        fontSize: fontSize + 2,
                                        fontFamily: fontFamiliy,
                                        textAlign: 'center',
                                        border:'none',
                                        width:'100%',
                                        backgroundColor: umlColor,
                                        padding: 'none',
                                    },
                                },
                                {
                                    tagName: 'hr',
                                    selector:'line',
                                    style:{
                                        height:'0px',
                                        border:0,
                                        borderTop: 'solid 1px #000000',
                                        backgroundColor:'#000000',
                                        margin:0,
                                    }
                                },
                                {
                                    tagName: 'div',
                                    selector: 'attrs',
                                    style: {
                                        color: '#000000',
                                        fontSize: fontSize,
                                        fontFamily: fontFamiliy,
                                        padding: '10px',
                                        margin: 0,

                                    },
                                    children:[
                                        {
                                            tagName: 'input',
                                            selector:'attr1',
                                            style:{
                                                width:'auto',
                                                height:'100%',
                                                border:'none',
                                                display:'inline',
                                                padding:'none',
                                                backgroundColor:umlColor,
                                            },

                                            textContent: 'attr1'
                                        },
                                        {
                                            tagName: 'input',
                                            selector:'attr2',
                                            style:{
                                                width:'100%',
                                                height:'100%',
                                            },
                                            textContent: 'attr1'
                                        }
                                    ]
                                },
                                {
                                    tagName: 'hr',
                                    selector:'line2',
                                    style:{
                                        height:'0px',
                                        border:0,
                                        borderTop: 'solid 1px #000000',
                                        backgroundColor:'#000000',
                                        margin:0,
                                    }
                                },
                            ]
                        }
                    ]
                }
            ]
        });

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
                    'font-family': fontFamiliy,

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
            alreadyDeleted: false,
            visibility: 'public',
            toolsBox: undefined,
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
                '<text class="uml-class-attrs-text"/>',
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

            selected: function (flag) {
                if (flag) {
                    this.attr({
                        '.uml-class-name-rect': {
                            fill: selectedUmlColor,
                        }
                    });
                    this.attr({
                        '.uml-class-attrs-rect': {
                            fill: selectedUmlColor,
                        }
                    });
                    this.attr({
                        '.uml-class-methods-rect': {
                            fill: selectedUmlColor,
                        }
                    });
                    this.attr({
                        '.uml-class-values-rect': {
                            fill: selectedUmlColor,
                        }
                    });
                } else {
                    this.attr({
                        '.uml-class-name-rect': {
                            fill: umlColor,
                        }
                    });
                    this.attr({
                        '.uml-class-attrs-rect': {
                            fill: umlColor,
                        }
                    });
                    this.attr({
                        '.uml-class-methods-rect': {
                            fill: umlColor,
                        }
                    });
                    this.attr({
                        '.uml-class-values-rect': {
                            fill: umlColor,
                        }
                    });
                }
            },

            getClassName: function () {
                return this.get('name');
            },

            getId: function () {
                return this.get('id');
            },

            update: function () {
                this.trigger('change:name');
            },

            updateFromMessage: function (message) {
                if (message.name) {
                    this.set('name', message.name);
                }


                if (message.width && message.height) {
                    this.set('size', {width: message.width, height: message.height});
                    this.trigger('change:size');
                } else if (message.width) {
                    this.set('size', {width: message.width, height: this.get('size').height});
                    this.trigger('change:size');
                } else if (message.height) {
                    this.set('size', {width: this.get('size').width, height: message.height});
                    this.trigger('change:size');
                }


                if (message.x && message.y) {
                    this.set('position', {x: message.x, y: message.y});
                }

                if (message.visibility) {
                    this.set('visibility', message.visibility);
                }

                if (message.isAbstract !== null) {
                    this.set('isAbstract', message.isAbstract);
                }
            },

            addAttribute: function (attribute) {
                this.get('attributes').push(attribute);
                this.trigger('change:attributes');
            },

            setAttributes: function (attributes) {
                this.set('attributes', attributes);
                this.trigger('change:attributes');
            },
            removeAttribute: function (id) {
                this.set('attributes', this.get('attributes').filter(attr => attr.id !== id));
                this.trigger('change:attributes');
            },
            updateAttributesFromMessage: function (message) {
                let index = this.get('attributes').map(function (x) {
                    return x.id;
                }).indexOf(message.id);
                if (index !== -1) {
                    let current = this.get('attributes')[index];

                    if (message.name) {
                        current.name = message.name;
                    }
                    if (message.visibility) {
                        current.setVisibility(message.visibility);
                    }
                    if (message.type) {
                        current.type = message.type;
                    }
                    if (message.isConstant !== null) {
                        current.isConstant = message.isConstant;
                    }
                    if (message.isStatic !== null) {
                        current.isStatic = message.isStatic;
                    }

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
            removeMethod: function (id) {
                this.set('methods', this.get('methods').filter(attr => attr.id !== id));
                this.trigger('change:methods');
            },
            updateMethodFromMessage: function (message) {
                let index = this.get('methods').map(function (x) {
                    return x.id;
                }).indexOf(message.id);
                if (index !== -1) {
                    let current = this.get('methods')[index];

                    if (message.name) {
                        current.name = message.name;
                    }
                    if (message.visibility) {
                        current.setVisibility(message.visibility);
                    }
                    if (message.type) {
                        current.type = message.type;
                    }
                    if (message.isAbstract !== null) {
                        current.isAbstract = message.isAbstract;
                    }
                    if (message.isStatic !== null) {
                        current.isStatic = message.isStatic;
                    }

                    this.trigger('change:methods');
                }
            },
            getOperation: function (id) {
                let op = this.get('methods').find(attr => attr.id === id);
                if (!op) {
                    op = this.get('constructors').find(attr => attr.id === id);
                }
                return op;
            },

            addConstructor: function (constructor) {
                this.get('constructors').push(constructor);
                this.trigger('change:methods');
            },

            setConstructors: function (constructors) {
                this.set('constructors', constructors);
                this.trigger('change:methods');
            },
            removeConstructor: function (id) {
                this.set('constructors', this.get('constructors').filter(attr => attr.id !== id));
                this.trigger('change:methods');
            },
            updateConstructorsFromMessage: function (message) {
                let index = this.get('constructors').map(function (x) {
                    return x.id;
                }).indexOf(message.id);
                if (index !== -1) {
                    let current = this.get('constructors')[index];

                    if (message.name) {
                        current.name = message.name;
                    }
                    if (message.visibility) {
                        current.setVisibility(message.visibility);
                    }

                    this.trigger('change:methods');
                }
            },

            updateRectangles: function (auto = false) {

                let attrs = this.get('attrs');


                let rects = [
                    {type: 'name', text: this.getClassName(), id: this.getId()},
                    {type: 'values', text: this.getEnumValues(), id: this.getId()},
                    {type: 'attrs', text: this.get('attributes'), id: this.getId()},
                    {type: 'methods', text: this.get('constructors').concat(this.get('methods')), id: this.getId()}
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
                    maxLineLength = Math.max(maxLineLength, 120)
                    this.set('size', {width: maxLineLength + 5, height: offsetY});
                } else {
                    this.set('size', {width: this.get('size').width, height: offsetY});
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
                    data: {
                        id: this.get('id'),
                        x: this.get('position').x,
                        y: this.get('position').y,
                        width: this.get('size').width,
                        height: this.get('size').height
                    },
                    type: 'UPDATE_COMMAND',
                    entityType: this.getType(),
                };
            },

            getSize: function () {
                return this.get('size');
            },

            removeCommand: function () {
                if (!this.get('alreadyDeleted')) {
                    this.set('alreadyDeleted', true);
                    return {
                        data: {id: this.get('id')},
                        type: 'REMOVE_COMMAND',
                        entityType: this.getType(),
                    };
                }
                return null;
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
                this.set('values', this.get('values').filter(val => val.name !== value));
                this.trigger('change:attributes');
            },
            updateValue: function (currentValue, newValue) {
                let index = this.get('values').map(x => x.name).indexOf(currentValue);
                if (index !== -1) {
                    this.get('values')[index].name = newValue;
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
            },
            linkId: undefined,
            alreadyDeleted: false,

            verticesChanged: false,
            toolsBox: undefined,
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
                },
            }],
            initialize: function () {
                this.on('change', function () {
                    this.trigger('uml-update');
                }, this);

                Link_mjs.Link.prototype.initialize.apply(this, arguments);

            },

            removeCommand: function () {
                if (!this.get('alreadyDeleted')) {
                    this.set('alreadyDeleted', true);
                    return {
                        data: {id: this.get('linkId')},
                        type: 'REMOVE_COMMAND',
                        entityType: this.getType(),
                    };
                }
                return null;
            },

            getId: function () {
                return this.get('linkId');
            },

            isAlreadyDeleted: function () {
                return this.get('alreadyDeleted');
            },

            hasVerticesChanged: function () {
                return this.get('verticesChanged');
            },
            updateFromMessage: function (message) {

                if (message.id) {

                    this.set('linkId', message.id);
                }

                if (message.sourceId) {
                    this.set('source', {id: message.sourceId});
                }
                if (message.targetId) {
                    this.set('target', {id: message.targetId});
                }

                if (message.vertices) {
                    this.set('vertices', JSON.parse(message.vertices));
                }

            },

            getType: function () {
                return 'CUSTOM_LINK';
            },


        });


        let CustomLinkView = LinkView_mjs.LinkView.extend({

            initialize: function () {

                LinkView_mjs.LinkView.prototype.initialize.apply(this, arguments);

                this.listenTo(this.model, 'uml-update', function () {
                    this.update();
                });

            }
        });

        let LabeledLink = CustomLink.define('uml.LabeledLink', {
            labelDistance: 0.5,
            labelOffset: {x: 0, y: -10},
            name: 'name',
            labelsChanged: [],
        }, {
            initialize: function () {
                CustomLink.prototype.initialize.apply(this, arguments);

                this.on('change', function (link) {
                    if (link.get('labels') !== undefined && link.get('labels').length > 0) {
                        for (let i = 0; i < link.get('labels').length; i++) {
                            try {
                                if (link._previousAttributes.labels !== undefined
                                    && link._previousAttributes.labels[i] !== undefined
                                    && (link.attributes.labels[i].position.distance !== link._previousAttributes.labels[i].position.distance ||
                                        link.attributes.labels[i].position.offset.x !== link._previousAttributes.labels[i].position.offset.x ||
                                        link.attributes.labels[i].position.offset.y !== link._previousAttributes.labels[i].position.offset.y ||
                                        (link.attributes.labels[i].position.offset !== link._previousAttributes.labels[i].position.offset && link.attributes.labels[i].position.offset.x === undefined))
                                    && !this.get('labelsChanged').includes(i)) {
                                    link.get('labelsChanged').push(i);
                                    return
                                }
                            } catch (e) {

                            }
                        }
                    }
                })

                // label name link
                this.appendLabel({
                    attrs: {
                        text: {
                            text: this.get('name'),
                        },
                        rect: {
                            fillOpacity: 0,
                        }
                    },
                    position: {
                        distance: parseFloat(this.get('labelDistance')),
                        offset: this.get('labelOffset'),
                        args: {
                            keepGradient: true,
                            ensureLegibility: true
                        }
                    }
                })
            },
            getType: function () {
                return 'LABELED_LINK';
            },

            getName: function () {
                return this.get('name');
            },

            getLabelsChanged: function () {
                return this.get('labelsChanged');
            },
            setLabel: function (id, {text, distance, offset}) {
                this.label(id, {
                    attrs: {
                        text: {
                            text: text,
                        }
                    },
                    position: {
                        distance: parseFloat(distance),
                        offset: offset,
                    }
                })
            },
            updateFromMessage: function (message) {
                CustomLink.prototype.updateFromMessage.apply(this, arguments);

                if (message.distance) {
                    this.set('labelDistance', parseFloat(message.distance));
                }

                if (message.offset) {
                    this.set('labelOffset', JSON.parse(message.offset));
                }

                if (message.name) {
                    this.set('name', message.name);
                }

                this.setLabel(0, {
                    text: this.get('name'),
                    distance: this.get('labelDistance'),
                    offset: this.get('labelOffset')
                });
            }
        });

        let LabeledLinkView = CustomLinkView;

        let Dependency = LabeledLink.define('uml.Dependency', {
            attrs: {
                line: {
                    targetMarker: {
                        'fill': 'white',
                        'type': 'path',
                        'd': 'M 20 -10 0 0 20 10 0 0 Z'
                    },
                    strokeDasharray: '5,5',
                }
            }
        }, {
            getType: function () {
                return 'DEPENDENCY';
            }
        });

        let DependencyView = LabeledLinkView;

        let Association = LabeledLink.define('uml.Association', {
            targetRole: undefined,
        }, {
            initialize: function () {
                LabeledLink.prototype.initialize.apply(this, arguments);

                // label target name
                this.appendLabel({
                    attrs: {
                        text: {
                            text: this.get('targetRole') ? this.get('targetRole').name : 'target',
                        },
                        rect: {
                            fillOpacity: 0,
                        }
                    },
                    position: {
                        distance: 0.95,
                        offset: {
                            x: 0,
                            y: -10
                        },
                        args: {
                            keepGradient: true,
                            ensureLegibility: true
                        }
                    }
                })

                // label multiplicity target
                this.appendLabel({
                    attrs: {
                        text: {
                            text: this.get('targetRole') ? this.get('targetRole').multiplicity : '*',
                        },
                        rect: {
                            fillOpacity: 0,
                        }
                    },
                    position: {
                        distance: 0.98,
                        offset: {
                            x: 0,
                            y: 10
                        },
                        args: {
                            keepGradient: true,
                            ensureLegibility: true
                        }
                    }
                })
            },
            getTargetRole: function () {
                return this.get('targetRole');
            },
            getType: function () {
                return 'ASSOCIATION';
            },

            updateRole: function (id, role) {
                if (this.get('targetRole') && this.get('targetRole').id === id) {
                    this.get('targetRole').set(role);
                    this.setLabel(1, {
                        text: this.get('targetRole').name,
                        distance: this.get('targetRole').distanceName,
                        offset: this.get('targetRole').offsetName
                    });
                    this.setLabel(2, {
                        text: this.get('targetRole').multiplicity,
                        distance: this.get('targetRole').distanceMultiplicity,
                        offset: this.get('targetRole').offsetMultiplicity
                    });
                }
            },
            updateFromMessage: function (message) {
                LabeledLink.prototype.updateFromMessage.apply(this, arguments);

                if (message.targetId) {
                    this.target({id: message.targetId});
                }
            }
        });

        let AssociationView = LabeledLinkView;

        let BinaryAssociation = Association.define('uml.BinaryAssociation', {
            isDirected: false,
            sourceRole: undefined,
        }, {
            initialize: function () {
                Association.prototype.initialize.apply(this, arguments);
                // label source name
                this.appendLabel({
                    attrs: {
                        text: {
                            text: this.get('sourceRole') ? this.get('sourceRole').name : 'source',
                        },
                        rect: {
                            fillOpacity: 0,
                        }
                    },
                    position: {
                        distance: 0.05,
                        offset: {
                            x: 0,
                            y: -10
                        },
                        args: {
                            keepGradient: true,
                            ensureLegibility: true
                        }
                    }
                })

                // label multiplicity target
                this.appendLabel({
                    attrs: {
                        text: {
                            text: this.get('sourceRole') ? this.get('sourceRole').multiplicity : '*',
                        },
                        rect: {
                            fillOpacity: 0,
                        }
                    },
                    position: {
                        distance: 0.02,
                        offset: {
                            x: 0,
                            y: 10
                        },
                        args: {
                            keepGradient: true,
                            ensureLegibility: true
                        }
                    }
                })
                this.setTargetArrow();
            },
            setTargetArrow: function () {
                if (this.get('isDirected')) {
                    this.attributes.attrs.line.targetMarker = {
                        'type': 'path',
                        'd': 'M 20 -10 0 0 20 10 0 0 Z'
                    };
                } else {
                    this.attributes.attrs.line.targetMarker = {
                        'd': ''
                    };
                }
                try {
                    this.trigger('change');
                } catch (e) {

                }
            },
            getType: function () {
                return 'BINARY_ASSOCIATION'
            },
            getSourceRole: function () {
                return this.get('sourceRole');
            },
            getDirected: function () {
                return this.get('isDirected');
            },
            setDirected: function (isDirected) {
                this.set('isDirected', isDirected);
            },
            updateRole: function (id, role) {
                Association.prototype.updateRole.call(this, id, role);
                if (this.get('sourceRole') && this.get('sourceRole').id === id) {
                    this.get('sourceRole').set(role);
                    this.setLabel(3, {
                        text: this.get('sourceRole').name,
                        distance: this.get('sourceRole').distanceName,
                        offset: this.get('sourceRole').offsetName
                    });
                    this.setLabel(4, {
                        text: this.get('sourceRole').multiplicity,
                        distance: this.get('sourceRole').distanceMultiplicity,
                        offset: this.get('sourceRole').offsetMultiplicity
                    });
                }
            },
            updateFromMessage: function (message) {
                Association.prototype.updateFromMessage.call(this, message);

                if (message.isDirected !== null) {
                    this.set('isDirected', message.isDirected);
                    this.setTargetArrow();
                }

                if (message.sourceId) {
                    this.source({id: message.sourceId});
                }
            }

        });

        let BinaryAssociationView = AssociationView;

        let AssociationClass = Association.define('uml.AssociationClass', {
            associatedClass: undefined,
        },{
            getType: function () {
                return 'ASSOCIATION_CLASS';
            }
        });


        let UnaryAssociation = Association.define('uml.UnaryAssociation', {
            parent: undefined,
        }, {
            initialize: function () {
                Association.prototype.initialize.apply(this, arguments);

            },
            getType: function () {
                return 'UNARY_ASSOCIATION';
            },
            removeCommand: function () {
                if (!this.get('alreadyDeleted')) {
                    this.set('alreadyDeleted', true);
                    return {
                        data: {id: this.get('linkId'), sourceId: this.get('source').id,},
                        type: 'REMOVE_COMMAND',
                        entityType: this.getType(),

                    };
                }
                return null;
            },
        });

        let UnaryAssociationView = AssociationView;

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
        }, {
            getType: function () {
                return 'GENERALIZATION'
            }
        });

        let GeneralizationView = CustomLinkView;

        let Realization = CustomLink.define('uml.Realization', {
            attrs: {
                line: {
                    targetMarker: {
                        'type': 'path',
                        'd': 'M 20 -10 L 0 0 L 20 10 z',
                        'fill': 'white'
                    },
                    strokeDasharray: '5,5',
                }
            }
        }, {
            getType: function () {
                return 'REALIZATION'
            }
        });

        let RealizationView = CustomLinkView;

        let Aggregation = BinaryAssociation.define('uml.Aggregation', {
            attrs: {
                line: {
                    sourceMarker: {
                        d: 'M 40 0 L 20 10 L 0 00 L 20 -10 z',
                        fill: 'white'
                    }
                }
            }
        }, {
            getType: function () {
                return 'AGGREGATION'
            }
        });

        let AggregationView = BinaryAssociationView;

        let Composition = Aggregation.define('uml.Composition', {
            attrs: {
                line: {
                    sourceMarker: {
                        fill: 'black'
                    }
                }
            }
        }, {
            getType: function () {
                return 'COMPOSITION'
            }
        });

        let CompositionView = AggregationView;

        let Inner = CustomLink.define('uml.Inner', {
            attrs: {
                line: {
                    sourceMarker: {
                        'type': 'path',
                        'd': 'M 0, 0 a 13,13 0 1,0 26,0 a 13,13 0 1,0 -26,0 L 0, 0 M 13, 13 L 13, -13 M 0, 0 L 26, 0',
                        'fill': umlColor
                    }
                },
            }
        }, {
            getType: function () {
                return 'INNER'
            }
        })

        let InnerView = CustomLinkView;


        let MutliAssociation = joint.shapes.standard.Rectangle.define('uml.MultiAssociation', {
            attrs: {
                body: {
                    fill: umlColor,
                    size: {
                        width: 50,
                        height: 50
                    }
                }
            },

            associations: [],
            id: undefined,
            alreadyDeleted: false,
        }, {
            initialize: function () {

                this.on('change', function () {
                    this.trigger('uml-update');
                }, this);


                basic_mjs.Generic.prototype.initialize.apply(this, arguments);
            },

            getId: function () {
                return this.get('id');
            },

            getType: function () {
                return 'MULTI_ASSOCIATION';
            },
            updateFromMessage: function (message) {
                if(message.x && message.y) {
                    this.set('position', {x: message.x, y: message.y});
                }
            }
        });

        let SimpleLink = CustomLink.define('uml.SimpleLink', {
            attrs: {
                line: {
                    strokeDasharray: '5,5',
                }
            }
        },{});

        exports.Abstract = Abstract;
        exports.AbstractView = AbstractView;
        exports.Aggregation = Aggregation;
        exports.AggregationView = AggregationView;
        exports.BinaryAssociation = BinaryAssociation;
        exports.BinaryAssociationView = BinaryAssociationView;
        exports.Class = Class;
        exports.ClassView = ClassView;
        exports.Composition = Composition;
        exports.CompositionView = CompositionView;
        exports.Depencency = Dependency;
        exports.DependencyView = DependencyView;
        exports.Generalization = Generalization;
        exports.GeneralizationView = GeneralizationView;
        exports.Realization = Realization;
        exports.RealizationView = RealizationView;
        exports.Inner = Inner;
        exports.InnerView = InnerView;
        exports.Interface = Interface;
        exports.InterfaceView = InterfaceView;
        exports.Enum = Enum;
        exports.EnumView = EnumView;
        exports.UnaryAssociation = UnaryAssociation;
        exports.UnaryAssociationView = UnaryAssociationView;
        exports.MultiAssociation = MutliAssociation;
        exports.SimpleLink = SimpleLink;
        exports.Form = Form;
    }
    (this.joint.shapes.uml = this.joint.shapes.uml || {}, joint.dia, joint.dia, joint.dia, joint.shapes.basic)
)
;