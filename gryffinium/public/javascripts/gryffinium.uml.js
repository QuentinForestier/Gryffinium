//region Const
const fontFamiliy = 'Helvetica, sans-serif';
const fontSize = 15
const umlColor = '#FFF7E1';


const standardInput = {
    style: {
        display: 'flex',
        height: '100%',
        border: 'none',
        padding: 'none',
        backgroundColor: umlColor,
    },
    size: 8,
}


const resizeLGrip = new joint.elementTools.Button({
    markup: [{
        tagName: 'rect',
        selector: 'button',
        attributes: {
            width: 10,
            height: 10,
            fill: '#777777',
            cursor: 'w-resize'
        }
    }],
    x: '0%',
    y: '50%',
    offset: {
        x: -5,
        y: -5
    },
    rotate: true,
    action: function (evt) {
        let elem = this.model;
        resizingStartPos = function (x, y) {

            let coords = UMLController.paper.snapToGrid(x, y);
            let size = elem.attributes.size;
            let position = elem.attributes.position;

            let deltaWidth = position.x - coords.x;

            if (size.width + deltaWidth <= 100)
                deltaWidth = 0;

            return elem.setSizeAndPosition(
                {width: size.width + deltaWidth, height: elem.getHeight()},
                {x: position.x - deltaWidth, y: position.y});
        }
    }
});
const resizeRGrip = new joint.elementTools.Button({
    markup: [{
        tagName: 'rect',
        selector: 'button',
        attributes: {
            width: 10,
            height: 10,
            fill: '#777777',
            cursor: 'e-resize'
        }
    }],
    x: '100%',
    y: '50%',
    offset: {
        x: -5,
        y: -5
    },
    rotate: true,
    action: function (evt) {
        let elem = this.model;
        resizingStartPos = function (x, y) {
            let size = elem.attributes.size;
            let position = elem.attributes.position;

            let coords = UMLController.paper.snapToGrid(x, y);

            let deltaWidth = coords.x - (position.x + size.width);

            if (size.width + deltaWidth <= 100)
                deltaWidth = 0;

            return elem.setSizeAndPosition({width: size.width + deltaWidth, height: elem.getHeight()});

        }
    }
});

const linkButton = new joint.elementTools.Connect({
    x: '100%',
    y: '0%',
    offset: {x: -5, y: -5},
    action: function (evt, _view, tool) {
        return tool.dragstart(evt);
    }
})

const multiAssociationToolsView = new joint.dia.ToolsView({
    tools: [
        new joint.elementTools.Boundary(),
    ]
})

const elementToolsView = new joint.dia.ToolsView({
    tools: [
        new joint.elementTools.Boundary(),
        new joint.elementTools.Remove(),
        resizeLGrip,
        resizeRGrip,
        linkButton,


    ]
});

const myTargetArrowhead = new joint.linkTools.TargetArrowhead();
const mySourceArrowhead = new joint.linkTools.SourceArrowhead();

myTargetArrowhead.el.setAttribute('d', 'M -20, 0 a 6,6 0 1,0 12, 0 a 6,6 0 1,0 -12,0')
myTargetArrowhead.el.setAttribute('stroke-width', '1')
mySourceArrowhead.el.setAttribute('d', 'M 0, 0 a 6,6 0 1,0 12, 0 a 6,6 0 1,0 -12,0')
mySourceArrowhead.el.setAttribute('stroke-width', '1')

const linkToolsView = new joint.dia.ToolsView({
    tools: [
        new joint.linkTools.Vertices(),
        mySourceArrowhead, myTargetArrowhead,
        new joint.linkTools.Boundary(), new joint.linkTools.Remove(),
    ]
});

const unaryToolsView = new joint.dia.ToolsView({
    tools: [
        new joint.linkTools.Vertices(),
        new joint.linkTools.Boundary(), new joint.linkTools.Remove(),
    ]
});
//endregion

//region Global attributes
let resizingStartPos = undefined;

let selectMultiAssociationMode = false;
let selectedForMulti = [];
//endregion

//region Entities
const Entity = joint.dia.Element.define('Entity', {
        attrs: {

            body: {
                width: 'calc(w)',
                height: 'calc(h)',

            },
            foreignObject: {
                width: '200',
                height: '100',
                x: 0,
                y: 0,

            },
            background: {
                style: {
                    backgroundColor: umlColor,
                    border: '1px solid black',
                    height: '100%'
                },

            },
            headerContainer: {
                style: {
                    padding: 3,
                    display: 'flex',
                    borderBottom: 'solid 1px black',
                    flexDirection: 'column',
                },
            },
            entityTag: {
                style: {
                    fontWeight: 'bold',
                    //fontStyle:'italic',
                    fontSize: fontSize + 2,
                    alignSelf: 'center',
                    border: 'none',
                    margin: 'auto',
                    backgroundColor: 'inherit',
                    textAlign: 'center',
                    padding: 0,
                    height: fontSize,
                },


            },
            header: {
                style: {
                    fontWeight: 'bold',
                    //fontStyle:'italic',
                    fontSize: fontSize + 2,
                    alignSelf: 'center',
                    border: 'none',
                    margin: 'auto',
                    backgroundColor: 'inherit',
                    textAlign: 'center',
                    padding: 0
                },
                size: '10',
                value: 'Header'
            },
            lock: {
                style: {
                    width: 25,
                    height: 25,
                    position: 'fixed',
                    borderRadius: 100,
                    backgroundColor: 'blue',
                    border: 'solid 1px white',
                }
            },


            attrsContainer: {
                style: {
                    display: 'flex',
                    fontSize: fontSize,
                    flexDirection: 'column',
                    padding: 3,
                },
            },
            methodsContainer: {
                style: {
                    display: 'flex',
                    fontSize: fontSize,
                    //borderTop: 'solid 1px black',
                    flexDirection: 'column',
                    padding: 3,
                },
            },

            id: undefined,
            name: undefined,
            umlAttributes: [],
            methods: [],

            visibility: 'public',

            alreadyDeleted: false,
            toolsBox: undefined,
            height: undefined,


            hideAttrs: false,
            hideMethods: false,


        },
    },
    {
        markup: [],
        initialize: function () {
            this.on('change:name', function () {
                this.trigger('uml-update');
            })

            joint.dia.Element.prototype.initialize.apply(this, arguments);
            this.updateMarkup();
        },
        updateMarkup: function () {
            let markup = [
                {
                    tagName: 'foreignObject',
                    selector: 'foreignObject',
                    style: {
                        color: '#000000',
                        fontFamily: fontFamiliy,
                        fontSize: fontSize,
                    },
                    children: [
                        {
                            tagName: 'div',
                            namespaceURI: 'http://www.w3.org/1999/xhtml',
                            selector: 'background',
                            children: this.sectionsMarkup(),
                        }
                    ]
                }
            ]


            this.set('markup', markup);
            this.autoHeight();
            this.trigger('uml-update');
        },

        generateSectionMarkup: function (name, type, list) {
            let section = {
                tagName: 'div',
                selector: name,
                children: [],
            }
            console.log('generate');
            for (let val of list) {

                let obj = this.generateInput(type, {parentId: this.get('id'), text: type + val.id, id: val.id});

                section.children.push(obj.markup);
            }

            return section;
        },

        sectionsMarkup: function () {


            let sections = [
                {
                    tagName: 'div',
                    selector: 'headerContainer',
                    children: [
                        {
                            tagName: 'input',
                            selector: 'header',
                        },
                        {
                            tagName: 'div',
                            selector: 'lock',
                        }
                    ]
                }
            ]

            let a = this.generateSectionMarkup('attrsContainer', 'Attr', this.attr('umlAttributes'));

            if (a.children.length > 0 && !this.attr('hideAttrs')) {

                sections.push(a);
                this.attr()['attrsContainer'].style.borderBottom = "solid 1px black";
            }

            let m = this.generateSectionMarkup('methodsContainer', 'Meth', this.attr('methods'));

            if (m.children.length > 0 && !this.attr('hideMethods')) {

                sections.push(m);
                this.attr()['methodsContainer'].style.borderBottom = "solid 1px black";
            }

            // Add << >> title on entity if needed
            if (this.getHeaderName() !== '')
                sections[0].children.splice(0, 0, {
                    tagName: 'p',
                    selector: 'entityTag',
                    textContent: this.getHeaderName(),
                })

            return sections;

        },

        nbVisibleElements: function () {
            let nb = this.getHeaderName() === '' ? 1 : 2; // Header
            nb += this.attr('umlAttributes').length * (this.attr('hideAttrs') ? 0 : 1);
            nb += this.attr('methods').length * (this.attr('hideMethods') ? 0 : 1);

            return nb;
        },

        generateInput: function (type, data) {

            let markup = {
                tagName: 'input',
                selector: type + data.id,
            };

            let attr = {...standardInput};
            attr.value = data.text;

            this.attr()[type + data.id] = attr;

            return {attr, markup};

        },
        setWidth: function (width = 100) {
            this.get('attrs').foreignObject.width = width;
        },
        getWidth: function () {
            return this.get('attrs').foreignObject.width;
        },
        autoWidth: function (elements) {
            const span = document.getElementById('measure')
            span.fontSize = fontSize
            span.fontFamily = fontFamiliy

            let maxLineLength = 0;
            elements.forEach(function (elem) {

                    span.innerText = elem.toString();
                    let lineSize = $(span).width();

                    maxLineLength = Math.max(maxLineLength, lineSize)
                    maxLineLength = (Math.round(maxLineLength / 10) * 10)
                }
            );

            this.setWidth(Math.max(120, maxLineLength));
        },

        setHeight: function (height = 100) {
            this.get('attrs').foreignObject.height = Math.max(height, 100);
        },
        getHeight: function () {
            return this.get('attrs').foreignObject.height;
        },
        autoHeight: function () {

            let nbElements = this.nbVisibleElements();

            this.setHeight(nbElements * (19.6) + 45);
            this.trigger('uml-update');
        },

        setX: function (x) {
            this.get('position').x = x;
            this.trigger('uml-update');
        },

        getX: function () {
            return this.get('position').x;
        },

        setY: function (y) {
            this.get('position').y = y;
            this.trigger('uml-update');
        },

        getY: function () {
            return this.get('position').y;
        },

        setColor: function (color) {
            this.get('attrs').background.style.backgroundColor = color;
        },
        getColor: function () {
            return this.get('attrs').background.style.backgroundColor;
        },

        selected: function (isSelected) {
            // TODO
        },

        getEntityName: function () {
            return this.get('attrs').header.value;
        },
        setEntityName: function (name) {
            this.get('attrs').header.value = name;
            this.trigger('uml-update')
        },

        getHeaderName: function () {
            return '';
        },

        getInputValue: function (type, id) {
            return this.get('attrs')[type + id].value;
        },
        setInputValue: function (type, id, text) {
            let selector = type + id;
            this.attr(selector + '/value', text);
            this.trigger('uml-update')
        },

        addAttribute: function (attribute) {
            this.attr('umlAttributes').push(attribute);
            this.updateMarkup();
        },

        setAttributes: function (attributes) {
            this.set('umlAttributes', attributes);
            this.updateMarkup();
        },

        removeAttribute: function (id) {
            this.set('umlAttributes', this.get('umlAttributes').filter(attr => attr.id !== id));
            this.updateMarkup();
        },

        updateAttribute: function (attribute) {
            let index = this.get('umlAttributes').map(function (x) {
                return x.id;
            }).indexOf(attribute.id);

            if (index !== -1) {
                this.get('umlAttributes')[index].update(attribute);
                this.setInputValue('Attr', attribute.id, attribute.toString());
            }

        },

        getId: function () {
            return this.get('id');
        },

        getType: function () {
            return 'CLASS';
        },

        update: function (modification) {
            if (modification.name) {
                this.setEntityName(modification.name);
            }

            if (modification.width) {
                this.setWidth(modification.width);
            }

            if (modification.height) {
                this.setHeight(modification.height);
            }

            if (modification.x) {
                this.setX(modification.x);
            }

            if (modification.y) {
                this.setY(modification.y);
            }

            if (modification.color) {
                this.setColor(modification.color);
            }

            if (modification.visibility) {
                //this.setVisibility(modification.visibility);
            }

        }


    }, {
        attributes: {
            value: {
                set: function (text, _, node) {
                    if ('value' in node) node.value = text;
                }
            }
        }
    });

export let Interface = Entity.define('Interface', {}, {
    initialize: function () {
        Entity.prototype.initialize.apply(this, arguments);
    },
    getType: function () {
        return ElementType.Interface.name;
    },
    getHeaderName: function () {
        return '<<Interface>>';
    },

})

let ConstructableEntity = Entity.define('ConstructableEntity', {
        constructors: [],
    },
    {
        initialize: function () {
            Entity.prototype.initialize.apply(this, arguments);
        },

        getType: function () {
            return 'CONSTRUCTABLE_ENTITY';
        },
        sectionsMarkup: function () {

            let sections = Entity.prototype.sectionsMarkup.apply(this, arguments);

            let m = Entity.prototype.generateSectionMarkup.call(this, 'methodsContainer', 'Meth', this.attr('constructors') + this.attr('methods'));

            if (m.children.length > 0 && !this.attr('hideMethods')) {
                for (let sec of sections) {
                    if (sec.selector === 'methodsContainer') {
                        sec.children = m.children;
                    }
                }
            }


            return sections;

        },

        nbVisibleElements: function () {
            let nb = Entity.prototype.nbVisibleElements.apply(this, arguments);
            console.log(this.get('constructors'))
            nb += this.get('constructors').length * (this.attr('hideMethods') ? 0 : 1);
            return nb;
        },
    }
);

export let Enum = ConstructableEntity.define('Enum', {
        valuesContainer: {
            style: {
                display: 'flex',
                fontSize: fontSize,
                //borderBottom: 'solid 1px black',
                flexDirection: 'column',
                padding: 3,

            },
        },
        hideVal: false,
        values: [],
    },
    {
        initialize: function () {
            ConstructableEntity.prototype.initialize.apply(this, arguments);
        },

        getType: function () {
            return ElementType.Enum.name;
        },

        sectionsMarkup: function () {

            let sections = ConstructableEntity.prototype.sectionsMarkup.apply(this, arguments);

            let v = ConstructableEntity.prototype.generateSectionMarkup.call(this, 'valuesContainer', 'Val', this.get('values'));

            if (v.children.length > 0 && !this.attr('hideValues')) {
                sections.splice(0, 0, v);
                this.get('valuesContainer').style.borderBottom = "solid 1px black";
            }


            return sections;

        },

        nbVisibleElements: function () {
            let nb = ConstructableEntity.prototype.nbVisibleElements.apply(this, arguments);
            nb += this.get('values').length * (this.attr('hideVals') ? 0 : 1);
            return nb;
        },

        getHeaderName: function () {
            return '<<Enum>>';
        },
    }
);

export let Class = ConstructableEntity.define('Class', {
        isAbstract: false,
    },
    {
        initialize: function () {
            ConstructableEntity.prototype.initialize.apply(this, arguments);
        },

        getType: function () {
            return ElementType.Class.name;
        },

        update: function (modification) {
            ConstructableEntity.prototype.update.call(this, modification);

            if (modification.isAbstract) {
                //this.setIsAbstract(modification.isAbstract);
            }
        }
    }
);

//endregion

//region Links

let CustomLink = joint.dia.Link.define('CustomLink', {
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
    },
    {
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

            joint.dia.Link.prototype.initialize.apply(this, arguments);

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

let LabeledLink = CustomLink.define('uml.LabeledLink', {
        labelDistance: 0.5,
        labelOffset: {x: 0, y: -10},
        name: 'name',
        labelsChanged: [],
    },
    {
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

export let Dependency = LabeledLink.define('Dependency', {
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

export let Association = LabeledLink.define('Association', {
        targetRole: undefined,
    },
    {
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
    }
);

export let BinaryAssociation = Association.define('BinaryAssociation', {
        isDirected: false,
        sourceRole: undefined,
    },
    {
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

    }
);

export let AssociationClass = Association.define('AssociationClass', {
        associatedClass: undefined,
    }, {
        getType: function () {
            return 'ASSOCIATION_CLASS';
        }
    }
);

export let UnaryAssociation = Association.define('UnaryAssociation', {
        parent: undefined,
    },
    {
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

export let Generalization = CustomLink.define('Generalization', {
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

export let Realization = CustomLink.define('Realization', {
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

export let Aggregation = BinaryAssociation.define('Aggregation', {
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

export let Composition = Aggregation.define('Composition', {
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

export let Inner = CustomLink.define('Inner', {
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

export let MultiAssociation = joint.shapes.standard.Rectangle.define('MultiAssociation', {
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
        if (message.x && message.y) {
            this.set('position', {x: message.x, y: message.y});
        }
    }
});

export let SimpleLink = CustomLink.define('uml.SimpleLink', {
    attrs: {
        line: {
            strokeDasharray: '5,5',
        }
    }
}, {});

//endregion

//region Controller

export class UMLController {

    entities = new Map();

    onChatMessage = undefined;
    sendMessage = undefined;

    movingVertices = undefined;

    static graph = undefined;
    static paper = undefined;

    dragStartPosition = null;
    moving = null;

    selectedLink = new BinaryAssociation();
    selectedEntity = undefined;
    selectedElement = undefined;

    constructor(document, onChatMessage, sendMessage) {
        this.onChatMessage = onChatMessage;
        this.sendMessage = sendMessage;
        this.graph = new joint.dia.Graph();
        this.paper = new joint.dia.Paper({
            el: document.getElementById('paper'),
            width: document.documentElement.clientWidth,
            height: document.documentElement.clientHeight,
            gridSize: 10,
            model: this.graph,
            drawGrid: true,
            interactive: {
                labelMove: true,
            },
            guard: function (evt) {
                return evt.target instanceof HTMLInputElement;
            },
            defaultConnector: {
                name: 'jumpover',
                size: 5,
            },
            defaultLink: new BinaryAssociation(),
            defaultAnchor: {
                name: 'perpendicular',
                args: {
                    perpendicular: true,
                    fixed: true,
                }
            },
            defaultLinkAnchor: {
                name: 'connectionPerpendicular',
            },
            linkPinning: false,
            elementView: joint.dia.ElementView.extend({
                events: {
                    'change input': 'onChange'
                },
                initialize: function () {

                    joint.dia.ElementView.prototype.initialize.apply(this, arguments);

                    this.listenTo(this.model, 'uml-update', function () {
                        this.update();
                        this.resize();
                    });

                },
                onChange: function (evt) {
                    let input = evt.target;
                    this.model.attr(input.attributes['joint-selector'].value + '/value', input.value);
                }
            }),
            linkView: joint.dia.LinkView.extend({
                initialize: function () {

                    joint.dia.LinkView.prototype.initialize.apply(this, arguments);

                    this.listenTo(this.model, 'uml-update', function () {
                        this.update();
                    });

                }
            })
        });
        this.initEvents();
    }

    onVerticesChange(link) {
        this.movingVertices = link;
    }

    create(command) {
        let elem = undefined;
        let parent = undefined;

        switch (command.elementType) {
            case ElementType.Class.name:
                elem = new Class({
                    id: command.id,
                    name: command.name,
                    toolsBox: elementToolsView,
                    position: {
                        x: command.x,
                        y: command.y
                    },
                    width: command.width,
                    height: command.height
                });
                elem.setEntityName(command.name);
                break
            case ElementType.Interface.name:
                elem = new Interface({
                    id: command.id,
                    name: command.name,
                    toolsBox: elementToolsView,
                    position: {
                        x: command.x,
                        y: command.y
                    },
                    width: command.width,
                    height: command.height
                });
                elem.setEntityName(command.name);
                break
            case ElementType.Enum.name:
                elem = new Enum({
                    id: command.id,
                    name: command.name,
                    toolsBox: elementToolsView,
                    position: {
                        x: command.x,
                        y: command.y
                    },
                    width: command.width,
                    height: command.height
                });
                elem.setEntityName(command.name);
                break
            case ElementType.AssociationClass.name:
                elem = new SimpleLink({
                    source: {id: command.classDto.id},
                    target: {id: elements.get(command.associationDto.id).get('id')},
                    toolsBox: undefined,
                })
                break;
            case ElementType.Generalization.name:
                elem = new Generalization({
                    source: {
                        id: command.sourceId,
                        connectionPoints: {
                            name: 'anchor',
                            args: {
                                align: 'top',
                            }
                        },
                        anchor: {
                            name: 'top',
                        }
                    },
                    target: {
                        id: command.targetId,
                        anchor: {
                            name: 'bottom'
                        },
                        connectionPoints: {
                            name: 'anchor',
                            args: {
                                align: 'bottom',
                            }
                        },
                    },
                    router: {
                        name: 'orthogonal',
                    },
                    linkId: command.id,
                    vertices: JSON.parse(command.vertices),
                    toolsBox: linkToolsView,
                });
                elem.on('change:vertices', (link) => this.onVerticesChange(link));
                break;
            case ElementType.Realization.name:
                elem = new Realization({
                    source: {
                        id: command.sourceId,
                        connectionPoints: {
                            name: 'anchor',
                            args: {
                                align: 'top',
                            }
                        },
                        anchor: {
                            name: 'top',
                        }
                    },
                    target: {
                        id: command.targetId,
                        anchor: {
                            name: 'bottom'
                        },
                        connectionPoints: {
                            name: 'anchor',
                            args: {
                                align: 'bottom',
                            }
                        },
                    },
                    router: {
                        name: 'orthogonal',
                    },
                    linkId: command.id,
                    vertices: JSON.parse(command.vertices),
                    toolsBox: linkToolsView,
                });
                    elem.on('change:vertices', (link) => this.onVerticesChange(link));
                break;
            case ElementType.BinaryAssociation.name:
                elem = new BinaryAssociation({
                    source: {id: command.sourceId,},
                    target: {id: command.targetId},
                    sourceRole: new Role(command.sourceRoleId, "", "*", 0.05,
                        {x: 0, y: -10}, 0.05, {x: 0, y: 10}),
                    targetRole: new Role(command.targetRoleId, "", "*", 0.95,
                        {x: 0, y: -10}, 0.95, {x: 0, y: 10}),
                    labelDistance: command.distance === null ? 0.5 : command.distance,
                    labelOffset: command.offset === null ? {
                        x: 0,
                        y: -10
                    } : JSON.parse(command.offset),
                    linkId: command.id,
                    isDirected: command.isDirected,
                    name: command.name,
                    vertices: JSON.parse(command.vertices),
                    toolsBox: linkToolsView,
                });

                elem.on('change:vertices', (link) => this.onVerticesChange(link));
                break;
            case ElementType.UnaryAssociation.name:
                elem = new uml.UnaryAssociation({
                    source: {
                        id: command.sourceId,
                        anchor: {
                            name: 'midSide'
                        }
                    },
                    target: {id: command.targetId},
                    targetRole: new Role(command.targetRoleId, "", "*", 0.95,
                        {x: 0, y: -10}, 0.95, {x: 0, y: 10}),
                    labelDistance: command.distance === null ? 0.5 : command.distance,
                    labelOffset: command.offset === null ? {
                        x: 0,
                        y: -10
                    } : JSON.parse(command.offset),
                    linkId: command.id,
                    name: "",
                    vertices: JSON.parse(command.vertices),
                    toolsBox: unaryToolsView
                });


                elem.on('change:vertices', (link) => this.onVerticesChange(link));
                break;
            case ElementType.Aggregation.name:
                elem = new Aggregation({
                    source: {id: command.sourceId},
                    target: {id: command.targetId},
                    sourceRole: new Role(command.sourceRoleId, "", "*", 0.05,
                        {x: 0, y: -10}, 0.05, {x: 0, y: 10}),
                    targetRole: new Role(command.targetRoleId, "", "*", 0.95,
                        {x: 0, y: -10}, 0.95, {x: 0, y: 10}),
                    labelDistance: command.distance === null ? 0.5 : command.distance,
                    labelOffset: command.offset === null ? {
                        x: 0,
                        y: -10
                    } : JSON.parse(command.offset),
                    linkId: command.id,
                    isDirected: command.isDirected,
                    name: command.name,
                    vertices: JSON.parse(command.vertices),
                    toolsBox: linkToolsView,
                });
                elem.on('change:vertices', (link) => this.onVerticesChange(link));
                break;
            case ElementType.Composition.name:
                elem = new Composition({
                    source: {id: command.sourceId},
                    target: {id: command.targetId},
                    sourceRole: new Role(command.sourceRoleId, "", "*", 0.05,
                        {x: 0, y: -10}, 0.05, {x: 0, y: 10}),
                    targetRole: new Role(command.targetRoleId, "", "*", 0.95,
                        {x: 0, y: -10}, 0.95, {x: 0, y: 10}),
                    labelDistance: command.distance === null ? 0.5 : command.distance,
                    labelOffset: command.offset === null ? {
                        x: 0,
                        y: -10
                    } : JSON.parse(command.offset),
                    linkId: command.id,
                    isDirected: command.isDirected,
                    name: command.name,
                    vertices: JSON.parse(command.vertices),
                    toolsBox: linkToolsView,
                });
                elem.on('change:vertices', (link) => this.onVerticesChange(link));
                break;
            case ElementType.MultiAssociation.name:

                elem = new MultiAssociation({
                    id: command.id,
                    position: {
                        x: command.x,
                        y: command.y
                    },
                    size: {
                        width: 50,
                        height: 50
                    },
                    angle: 45
                });


                break;
            case ElementType.Inner.name:
                elem = new Inner({
                    source: {id: command.sourceId},
                    target: {id: command.targetId},
                    linkId: command.id,
                    vertices: JSON.parse(command.vertices),
                    toolsBox: linkToolsView,
                });
                elem.on('change:vertices', (link) => this.onVerticesChange(link));
                break;
            case ElementType.Dependency.name:
                elem = new Dependency({
                    source: {id: command.sourceId},
                    target: {id: command.targetId},
                    labelDistance: command.distance === null ? 0.5 : command.distance,
                    labelOffset: command.offset === null ? {
                        x: 0,
                        y: -10
                    } : JSON.parse(command.offset),
                    linkId: command.id,
                    name: command.name,
                    vertices: JSON.parse(command.vertices),
                    toolsBox: linkToolsView,
                });
                elem.on('change:vertices', (link) => this.onVerticesChange(link));
                break;
            case ElementType.Attribute.name:
                let attribute = new Attribute(command.id, command.name, command.type, command.visibility.toLowerCase(), command.isConstant, command.isStatic);
                parent = elements.get(command.parentId);
                parent.addAttribute(attribute);
                /*if (parent === selectedElement) {
                    generateAttributesInterface(document.getElementById("container-attributes"), selectedElement, sendMessage, true)
                }*/
                break;
            case ElementType.Method.name:
                let m = new Method(command.id, command.name, command.type, command.visibility.toLowerCase(), command.isAbstract, command.isStatic);
                parent = elements.get(command.parentId);
                parent.addMethod(m);
                /*if (parent === selectedElement) {
                    generateMethodsInterface(document.getElementById("container-methods"), selectedElement, sendMessage, true)
                }*/
                break;
            case ElementType.Constructor.name:
                let constructor = new Constructor(command.id, command.name, command.visibility.toLowerCase());
                parent = elements.get(command.parentId);
                parent.addConstructor(constructor);
                /*if (parent === selectedElement) {
                    generateMethodsInterface(document.getElementById("container-methods"), selectedElement, sendMessage, true)
                }*/
                break;
            case ElementType.Value.name:
                let value = new Value(command.value);
                parent = elements.get(command.parentId);
                parent.addValue(value);
                /*if (parent === selectedElement) {
                    generateValuesInterface(document.getElementById("container-values"), selectedElement, sendMessage, true)
                }*/
                break;
            case ElementType.Parameter.name:
                let parameter = new Parameter(command.id, command.name, command.type);
                elements.get(command.parentId).getOperation(command.methodId).addParameter(parameter);
                elements.get(command.parentId).update();
                /*if (elements.get(command.parentId) === selectedElement) {
                    generateParametersInterface(selectedElement, elements.get(command.parentId).getOperation(command.methodId), sendMessage)
                }*/
                break;
            case ElementType.Role.name:
                let role = new Role(command.id, command.name, command.multiplicity, command.distanceName, command.offsetName, command.distanceMultiplicity, command.offsetMultiplicity);
                parent = elements.get(command.associationId);
                parent.updateRole(command.id, role);

                /*if (parent === selectedElement) {
                    generateModifierInterface(modifierOffCanvas, modifierHeader, modifierBody, selectedElement, sendMessage, true);
                }*/
                break;
        }

        if (elem !== undefined) {

            this.entities.set(elem.getId(), elem);
            elem.addTo(this.graph);
        }

    }

    delete(command) {

    }

    update(command) {
        switch (command.elementType) {
            case ElementType.Class.name:
            case ElementType.Interface.name:
            case ElementType.Enum.name:
            case ElementType.AssociationClass.name:
            case ElementType.Generalization.name:
            case ElementType.Realization.name:
            case ElementType.BinaryAssociation.name:
            case ElementType.Aggregation.name:
            case ElementType.Composition.name:
            case ElementType.Dependency.name:
            case ElementType.Inner.name:
            case ElementType.MultiAssociation.name:
            case ElementType.UnaryAssociation.name:
                if (command.visibility !== undefined) {
                    command.visibility = Visibility.getVisibility(command.visibility);
                }
                this.entities.get(command.id).update(command);
                break;
            case ElementType.Attribute.name:
                this.entities.get(command.parentId).updateAttribute(command);
                break;
        }
    }

    onMessage(message) {
        for (let command of message.commands) {
            switch (command.commandType) {
                case 'CHAT_MESSAGE_COMMAND':
                    this.onChatMessage(command);
                    break;
                case 'SELECT_COMMAND':
                case 'CREATE_COMMAND':
                    this.create(command);
                    break;
                case 'UPDATE_COMMAND':
                    this.update(command);
                    break;
                case 'REMOVE_COMMAND':
                    this.delete(command);
                    break;
            }
        }
    }

    onMouseMove(evt) {
        if (this.dragStartPosition) {
            let newPosX = evt.offsetX - this.dragStartPosition.x;
            let newPosY = evt.offsetY - this.dragStartPosition.y;
            if (newPosX > 0) {
                newPosX = 0;
            }
            if (newPosY > 0) {
                newPosY = 0;
            }
            this.paper.translate(newPosX, newPosY);
        }

        if (resizingStartPos) {
            resizingStartPos(evt.offsetX, evt.offsetY);
        }
    }

    onMouseUp(evt) {
        evt.preventDefault();
        if (resizingStartPos) {
            let cmd = resizingStartPos(evt.offsetX, evt.offsetY);
            sendMessage(cmd.data, cmd.entityType, cmd.type);

            resizingStartPos = null;
        }
        if (this.moving) {
            sendMessage({
                    id: this.moving.get('id'),
                    x: this.moving.get('position').x,
                    y: this.moving.get('position').y,
                    width: this.moving.get('size').width,
                    height: this.moving.get('size').height
                },
                this.moving.getType(),
                'UPDATE_COMMAND');
            this.moving = null;
        }

        if (this.movingVertices) {
            let view = this.paper.findViewByModel(this.movingVertices);
            sendMessage({
                id: this.movingVertices.getId(),
                sourceId: this.movingVertices.get('source').id,
                sourceAnchor: view.sourceAnchor,
                targetAnchor: view.targetAnchor,
                vertices: JSON.stringify(this.movingVertices.get('vertices')),
            }, this.movingVertices.getType(), 'UPDATE_COMMAND');
            this.movingVertices = undefined;
        }
    }

    selectLinkType(linkType) {
        switch (linkType) {
            case ElementType.Inner:
                this.paper.options.defaultLink = new Inner();
                break;
            case ElementType.Aggregation:
                this.paper.options.defaultLink = new Aggregation();
                break;
            case ElementType.BinaryAssociation:
                this.paper.options.defaultLink = new BinaryAssociation();
                break;
            case ElementType.Composition:
                this.paper.options.defaultLink = new Composition();
                break;
            case ElementType.Dependency:
                this.paper.options.defaultLink = new Dependency();
                break;
            case ElementType.Generalization:
                this.paper.options.defaultLink = new Generalization();
                break;
            case ElementType.Realization:
                this.paper.options.defaultLink = new Realization();
                break;
            case ElementType.AssociationClass:
                this.paper.options.defaultLink = new AssociationClass();
                break;

        }
    }

    selectEntityType(entityType) {
        switch (entityType) {
            case ElementType.Class:
                this.selectedEntity = ElementType.Class.name;
                break;
            case ElementType.Interface:
                this.selectedEntity = ElementType.Interface.name;
                break;
            case ElementType.Enum:
                this.selectedEntity = ElementType.Enum.name;
                break;
            default:
                this.selectedEntity = undefined;

        }
    }

    initEvents() {
        //region Graph
        this.graph.on("remove", (cell) => this.remove(cell));
        this.graph.on('change:position', (cell) => this.changePosition(cell));

        //endregion
        //region Paper
        this.paper.on("blank:mousewheel", (evt, x, y, delta) => this.zoom(evt, x, y, delta));
        this.paper.on('blank:pointerdown', (event, x, y) => this.blankPointerDown(event, x, y));
        this.paper.on('cell:pointerup blank:pointerup', (event, x, y) => this.pointerUp(event, x, y));

        this.paper.on('element:pointerdblclick', (elementView) => this.elemDoubleClick(elementView));
        this.paper.on('element:mouseenter', (elementView) => this.elemMouseEnter(elementView));
        this.paper.on('element:mouseleave', (elementView) => this.elemMouseLeave(elementView));
        this.paper.on('element:pointerdown', (elementView) => this.elemPointerDown(elementView));

        this.paper.on('link:connect', (linkView) => this.linkConnect(linkView));
        this.paper.on('link:pointerup', (linkView) => this.linkPointerUp(linkView));
        this.paper.on('link:pointerdown', (linkView) => this.linkPointerDown(linkView));
        //endregion

    }

    remove(cell) {
        if (!cell.get('alreadyDeleted') && cell.getId()) {
            let cmd = cell.removeCommand();
            if (cmd !== null)
                this.sendMessage(cmd.data, cmd.entityType, cmd.type);
        }
    }

    changePosition(cell) {
        if (cell.get('position').x < 0)
            cell.set('position', {x: 0, y: cell.get('position').y});
        if (cell.get('position').y < 0)
            cell.set('position', {x: cell.get('position').x, y: 0});
        this.moving = cell;
    }

    zoom(evt, x, y, delta) {
        evt.preventDefault();
        const oldScale = this.paper.scale().sx;
        const newScale = oldScale + 0.2 * delta * oldScale

        if (newScale > 0.2 && newScale < 5) {
            this.paper.scale(newScale, newScale, x, y);
            let tx = -x * newScale + evt.offsetX;
            let ty = -y * newScale + evt.offsetY;
            if (tx > 0)
                tx = 0;
            if (ty > 0)
                ty = 0;
            this.paper.translate(tx, ty);
        }
    }

    blankPointerDown(event, x, y) {
        document.activeElement.blur();
        if (!selectMultiAssociationMode) {
            this.selectedElement = undefined;
            //generateModifierInterface(modifierOffCanvas, modifierHeader, modifierBody, selectedElement, sendMessage);
            let scale = this.paper.scale();
            if (this.selectedEntity) {
                this.sendMessage({
                        x: x - 100,
                        y: y - 50,
                        width: 200,
                        height: 100,
                    },
                    this.selectedEntity,
                    'CREATE_COMMAND');
            } else if (event.button === 1) {
                this.dragStartPosition = {x: x * scale.sx, y: y * scale.sy};
            }
        }
    }

    pointerUp(cellView, evt, x, y) {
        this.dragStartPosition = null;
        // TODO
        if (!selectMultiAssociationMode) {
            this.paper.removeTools();
        }
    }


    elemDoubleClick(elementView) {
        elementView.model.autoWidth();
        this.sendMessage({
                id: elementView.model.getId(),
                width: elementView.model.getWidth(),
                height: elementView.model.getHeight(),
            },
            elementView.model.getType(), 'UPDATE_COMMAND');
    }

    elemMouseEnter(elementView) {
        if (!selectMultiAssociationMode) {
            this.paper.removeTools();
            elementView.addTools(elementView.model.get('toolsBox'));
        }
    }

    elemMouseLeave(elementView) {
        if (elementView.model !== this.selectedElement && !selectMultiAssociationMode) {
            this.paper.removeTools();
        }
    }

    elemPointerDown(elementView) {
        document.activeElement.blur();
        if (selectMultiAssociationMode) {
            if (selectedForMulti.includes(elementView.model)) {
                selectedForMulti.splice(selectedForMulti.indexOf(elementView.model), 1);
                elementView.removeTools();
                elementView.model.selected(false);
            } else {
                selectedForMulti.push(elementView.model);
                elementView.model.selected(true);
            }

        } else {
            this.paper.removeTools();
            elementView.addTools(elementView.model.get('toolsBox'));
            if (this.selectedElement === elementView.model)
                return;
            this.selectedElement = elementView.model;

            //if (selectedElement !== undefined && selectedElement.getId() !== undefined)
            //    generateModifierInterface(modifierOffCanvas, modifierHeader, modifierBody, selectedElement, sendMessage);
        }
    }

    linkConnect(linkView) {
        let source = this.entities.get(linkView.model.get('source').id);
        let target = this.entities.get(linkView.model.get('target').id);
        if (linkView.model.get('linkId') === undefined) {
            if (linkView.model.getType() === "ASSOCIATION_CLASS") {
                let x = (Math.max(source.get('position').x, target.getX()) - Math.min(source.getX(), target.getX())) / 2 + Math.min(source.getWidth(), target.getWidth());
                let y = Math.min(source.getY(), target.getY()) - 150;

                this.sendMessage({
                    classDto: {
                        x: x,
                        y: y,
                        width: 200,
                        height: 100,
                    },
                    associationDto: {
                        sourceId: source.id,
                        targetId: target.id,
                        isDirected: false,
                        name: linkView.model.getType(),
                    }
                }, linkView.model.getType(), 'CREATE_COMMAND')
            } else {
                this.sendMessage({
                    sourceId: source.id,
                    targetId: target.id,
                    isDirected: false,
                    name: linkView.model.getType(),
                }, linkView.model.getType(), 'CREATE_COMMAND');
            }
            linkView.model.remove();
        } else {
            this.sendMessage({
                sourceId: source.id,
                targetId: target.id,
                id: linkView.model.get('linkId'),
            }, linkView.model.getType(), 'UPDATE_COMMAND');
        }
    }

    linkPointerUp(linkView) {
        try {
            if (linkView.model.getLabelsChanged() !== []) {
                let indexes = linkView.model.get('labelsChanged');
                linkView.model.set('labelsChanged', []);
                for (let index of indexes) {
                    if (index !== 0) {
                        let roleIndex = Math.ceil(index / 2) - 1;
                        let label = linkView.model.get('labels')[index];
                        let role = roleIndex === 0 ? linkView.model.getTargetRole() : linkView.model.getSourceRole();
                        this.sendMessage({
                            id: role.id,
                            distanceMultiplicity: index % 2 ? undefined : label.position.distance,
                            offsetMultiplicity: index % 2 ? undefined : JSON.stringify(label.position.offset),
                            distanceName: index % 2 ? label.position.distance : undefined,
                            offsetName: index % 2 ? JSON.stringify(label.position.offset) : undefined,
                            associationId: linkView.model.getId(),
                            sourceId: linkView.model.get('source').id,
                        }, 'ROLE', 'UPDATE_COMMAND');
                    } else {
                        let label = linkView.model.get('labels')[0];
                        this.sendMessage({
                            id: linkView.model.getId(),
                            distance: label.position.distance,
                            offset: JSON.stringify(label.position.offset),
                            sourceId: linkView.model.get('source').id,
                        }, linkView.model.getType(), 'UPDATE_COMMAND');
                    }
                }
            }
        } catch (e) {
            // Nothing to catch, just ignore it
        }
    }

    linkPointerDown(linkView) {
        this.paper.removeTools();
        linkView.addTools(linkView.model.get('toolsBox'));

        if (this.selectedElement === linkView.model)
            return;
        this.selectedElement = linkView.model;

        //if (selectedElement !== undefined && selectedElement.getId() !== undefined)
        //generateModifierInterface(modifierOffCanvas, modifierHeader, modifierBody, selectedElement, sendMessage);
    }


}

//endregion

/** Utility elements **/

export class ElementType {
    static Class = new ElementType("CLASS");
    static Enum = new ElementType("ENUM");

    static Interface = new ElementType("INTERFACE");
    static AssociationClass = new ElementType("ASSOCIATION_CLASS");
    static Generalization = new ElementType("GENERALIZATION");
    static Realization = new ElementType("REALIZATION");
    static BinaryAssociation = new ElementType("BINARY_ASSOCIATION");
    static UnaryAssociation = new ElementType("UNARY_ASSOCIATION");
    static Aggregation = new ElementType("AGGREGATION");
    static Composition = new ElementType("COMPOSITION");

    static MultiAssociation = new ElementType("MULTI_ASSOCIATION");
    static Inner = new ElementType("INNER");
    static Dependency = new ElementType("DEPENDENCY");

    static Role = new ElementType("ROLE");

    static Value = new ElementType("VALUE");

    static Attribute = new ElementType("ATTRIBUTE");
    static Parameter = new ElementType("PARAMETER");

    static Method = new ElementType("METHOD");
    static Constructor = new ElementType("CONSTRUCTOR");

    constructor(name) {
        this.name = name;
    }

}

export class Visibility {
    static Public = new Visibility("+");
    static Private = new Visibility("-");
    static Protected = new Visibility("#");
    static Package = new Visibility("~");

    constructor(symbol) {
        this.symbol = symbol;
    }

    toString() {
        return this.symbol;
    }

    static getVisibility(name) {
        switch (name) {
            case "public":
                return Visibility.Public;
            case "private":
                return Visibility.Private;
            case "protected":
                return Visibility.Protected;
            case "package":
                return Visibility.Package;
        }
    }
}

export class Type {
    constructor(id, name) {
        this.id = id;
        this.name = name;
    }

    toString() {
        return this.name;
    }
}

export class Attribute {
    constructor(id, name, type, visibility, isConstant, isStatic) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.visibility = Visibility.getVisibility(visibility);
        this.isConstant = isConstant;
        this.isStatic = isStatic;
    }

    setVisibility(visibility) {
        this.visibility = Visibility.getVisibility(visibility);
    }

    update(modification) {
        if (modification.id) {
            this.id = modification.id;
        }
        if (modification.name) {
            this.name = modification.name;
        }
        if (modification.type) {
            this.type = modification.type;
        }
        if (modification.visibility) {
            this.visibility = Visibility.getVisibility(modification.visibility);
        }
        if (modification.isConstant) {
            this.isConstant = modification.isConstant;
        }
        if (modification.isStatic) {
            this.isStatic = modification.isStatic;
        }


    }

    toString() {

        let result = this.visibility + " " + this.name + " : " + this.type;
        if (this.isConstant || this.isStatic) {
            result += `{${this.isConstant ? "const" : ''}${this.isConstant && this.isStatic ? ', ' : ''}${this.isStatic ? "static" : ""}}`;
        }
        return result;
    }
}

export class Constructor {
    constructor(id, name, visibility) {
        this.id = id;
        this.name = name;
        this.visibility = Visibility.getVisibility(visibility);
        this.parameters = [];
    }

    setVisibility(visibility) {
        this.visibility = Visibility.getVisibility(visibility);
    }

    addParameter(parameter) {
        this.parameters.push(parameter);
    }

    update(modification) {
        if (modification.id) {
            this.id = modification.id;
        }
        if (modification.name) {
            this.name = modification.name;
        }
        if (modification.visibility) {
            this.visibility = Visibility.getVisibility(modification.visibility);
        }
    }

    toString() {
        return this.visibility + " " + this.name + this.paramsToString();
    }

    updateParametersFromMessage(message) {
        let parameter = this.parameters.find(p => p.id === message.id);
        if (message.name !== null) {
            parameter.name = message.name;
        }
        if (message.type !== null) {
            parameter.type = message.type;
        }
    }

    paramsToString() {
        let params = "";
        this.parameters.forEach(p => {
            params += p.name + " : " + p.type + ", ";
        });
        return "(" + params.substring(0, params.length - 2) + ")";
    }

    removeParameter(id) {
        let parameter = this.parameters.find(p => p.id === id);
        this.parameters.splice(this.parameters.indexOf(parameter), 1);
    }
}

export class Method {
    constructor(id, name, type, visibility, isAbstract, isStatic) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.visibility = Visibility.getVisibility(visibility);
        this.isAbstract = isAbstract;
        this.isStatic = isStatic;
        this.parameters = [];
    }

    setVisibility(visibility) {
        this.visibility = Visibility.getVisibility(visibility);
    }

    toString() {
        let result = this.visibility + " " + this.name + " : " + this.type;
        if (this.isAbstract || this.isStatic) {
            result += `{${this.isAbstract ? "abstract" : ''}${this.isConstant && this.isStatic ? ', ' : ''}${this.isStatic ? "static" : ""}}`;
        }
        return result;
    }

    paramsToString() {
        let params = "";
        this.parameters.forEach(p => {
            params += p.name + " : " + p.type + ", ";
        });
        return "(" + params.substring(0, params.length - 2) + ")";
    }

    update(modification) {
        if (modification.id) {
            this.id = modification.id;
        }
        if (modification.name) {
            this.name = modification.name;
        }
        if (modification.type) {
            this.type = modification.type;
        }
        if (modification.visibility) {
            this.visibility = Visibility.getVisibility(modification.visibility);
        }
        if (modification.isAbstract) {
            this.isAbstract = modification.isAbstract;
        }
        if (modification.isStatic) {
            this.isStatic = modification.isStatic;
        }
    }

    addParameter(parameter) {
        this.parameters.push(parameter);
    }

    updateParametersFromMessage(message) {
        let parameter = this.parameters.find(p => p.id === message.id);
        if (message.name !== null) {
            parameter.name = message.name;
        }
        if (message.type !== null) {
            parameter.type = message.type;
        }
    }

    removeParameter(id) {
        let parameter = this.parameters.find(p => p.id === id);
        this.parameters.splice(this.parameters.indexOf(parameter), 1);
    }
}

export class Value {
    constructor(name) {
        this.name = name;
    }

    update(modification) {
        if (modification.name) {
            this.name = modification.name;
        }
    }

    toString() {
        return this.name;
    }
}

export class Parameter {
    constructor(id, name, type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

}

export class Role {
    constructor(id, name, multiplicity, distanceName, offsetName, distanceMultiplicity, offsetMultiplicity) {
        this.id = id;
        this.name = name;
        this.multiplicity = multiplicity;
        this.distanceName = parseFloat(distanceName);
        try {
            this.offsetName = JSON.parse(offsetName);
        } catch (e) {
            this.offsetName = offsetName;
        }
        this.distanceMultiplicity = distanceMultiplicity;
        try {
            this.offsetMultiplicity = JSON.parse(offsetMultiplicity);

        } catch (e) {
            this.offsetMultiplicity = offsetMultiplicity;
        }
    }

    set(role) {
        if (role.id !== null && role.id !== undefined) {
            this.id = role.id;
        }
        if (role.name !== null && role.name !== undefined) {
            this.name = role.name;
        }
        if (role.multiplicity !== null && role.multiplicity !== undefined) {
            this.multiplicity = role.multiplicity;
        }
        if (role.distanceName !== null && role.distanceName !== undefined) {
            this.distanceName = parseFloat(role.distanceName);
        }
        if (role.offsetName !== null && role.offsetName !== undefined) {
            try {
                this.offsetName = JSON.parse(role.offsetName);
            } catch (e) {
                this.offsetName = role.offsetName;
            }
        }
        if (role.distanceMultiplicity !== null && role.distanceMultiplicity !== undefined) {
            this.distanceMultiplicity = role.distanceMultiplicity;
        }
        if (role.offsetMultiplicity !== null && role.offsetMultiplicity !== undefined) {
            try {
                this.offsetMultiplicity = JSON.parse(role.offsetMultiplicity);

            } catch (e) {
                this.offsetMultiplicity = role.offsetMultiplicity;
            }
        }
    }
}

/** END Utility elements**/
