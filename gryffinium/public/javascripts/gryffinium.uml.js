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
        backgroundColor: 'inherit',
        overflow: 'hidden',
        whiteSpace: 'nowrap',
        textOverflow: 'ellipsis',
        width: '90%',
    },
    size: 8,
}


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
                    overflow: 'hidden',
                    whiteSpace: 'nowrap', /* Don't forget this one */
                    textOverflow: 'ellipsis',
                    padding: 0,
                },

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
        },
        id: undefined,
        name: undefined,
        umlAttributes: new Map(),
        methods: new Map(),

        visibility: 'public',

        alreadyDeleted: false,
        toolsBox: undefined,
        height: undefined,


        hideAttrs: false,
        hideMethods: false,
    },
    {
        markup: [],
        initialize: function () {
            this.on('change:name', function () {
                this.trigger('uml-update');
            })
            this.set('umlAttributes', new Map());
            this.set('methods', new Map());


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
            for (let val of list) {

                let obj = this.generateInput(type, this.getId(), val);

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

            let tmp = this.get('umlAttributes');
            let a = this.generateSectionMarkup('attrsContainer', 'Attr', this.get('umlAttributes').values());

            if (a.children.length > 0 && !this.get('hideAttrs')) {

                sections.push(a);
                this.attr('attrsContainer').style.borderBottom = "solid 1px black";
            }

            let m = this.generateSectionMarkup('methodsContainer', 'Meth', this.get('methods').values());

            if (m.children.length > 0 && !this.get('hideMethods')) {

                sections.push(m);
                //this.attr()['methodsContainer'].style.borderBottom = "solid 1px black";
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
            nb += this.get('umlAttributes').size * (this.get('hideAttrs') ? 0 : 1);
            nb += this.get('methods').size * (this.get('hideMethods') ? 0 : 1);

            return nb;
        },
        generateInput: function (type, parent, data) {

            let markup = {
                tagName: 'input',
                selector: type + "_" + data.id,
            };

            let attr = {...standardInput};
            attr.value = data.toString();
            attr.parent = parent;

            this.attr()[type + "_" + data.id] = attr;

            return {attr, markup};

        },
        getVisibleElements: function () {
            let elements = [];
            elements.push(this.getEntityName());
            elements.push(this.getHeaderName());

            if (!this.get('hideAttrs'))
                elements = elements.concat(Array.from(this.get('umlAttributes').values()));

            if (!this.get('hideMethods'))
                elements = elements.concat(Array.from(this.get('methods').values()));

            return elements;
        },
        setWidth: function (width = 100, update = true) {
            this.get('attrs').foreignObject.width = Math.max(width, 100);
            this.get('attrs').header.style.width = '90%';
            if (update)
                this.trigger('uml-update');
        },
        getWidth: function () {
            return parseFloat(this.get('attrs').foreignObject.width);
        },
        autoWidth: function () {
            const span = document.getElementById('measure')
            span.fontSize = fontSize;
            span.fontFamily = fontFamiliy;

            let count = 0;
            let maxLineLength = 0;
            this.getVisibleElements().forEach(function (elem) {
                    let lineSize = 0;
                    span.innerText = elem.toString();
                    if (count > 1) {
                        lineSize = $(span).width() * 1.15;
                    } else {
                        lineSize = $(span).width() * 1.45;
                    }
                    maxLineLength = Math.max(maxLineLength, lineSize)
                    maxLineLength = (Math.round(maxLineLength / 10) * 10)
                    count++;
                }
            );

            this.setWidth(Math.max(100, maxLineLength));

        },
        setHeight: function (height = 100, update = true) {
            this.get('attrs').foreignObject.height = Math.max(height, 100);
            if (update)
                this.trigger('uml-update');
        },
        getHeight: function () {
            return parseFloat(this.get('attrs').foreignObject.height);
        },
        autoHeight: function () {

            let nbElements = this.nbVisibleElements();

            this.setHeight(nbElements * (19.6) + 45);

        },
        setX: function (x, update = true) {
            this.position(x, this.getY());
            if (update)
                this.trigger('uml-update');
        },
        getX: function () {
            return this.get('position').x;
        },
        setY: function (y, update = true) {
            this.position(this.getX(), y);
            if (update)
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
            this.setInputValue('header', '', name);
            this.trigger('uml-update')
        },
        getHeaderName: function () {
            return '';
        },
        getInputValue: function (type, id) {
            return this.get('attrs')[type + "_" + id].value;
        },
        setInputValue: function (type, id, text) {
            let selector = type + "_" + id;
            this.attr(selector + '/value', text);
            this.trigger('uml-update')
        },
        getAttribute: function (id) {
            if (this.get('umlAttributes').has(id))
                return this.get('umlAttributes').get(id);
        },
        addAttribute: function (attribute) {
            this.get('umlAttributes').set(attribute.id, attribute);
            this.updateMarkup();
        },
        removeAttribute: function (id) {
            this.get('umlAttributes').delete(id)
            this.updateMarkup();
        },
        updateAttribute: function (attribute) {

            if (this.get('umlAttributes').has(attribute.id)) {
                this.get('umlAttributes').get(attribute.id).update(attribute);
                this.setInputValue('Attr', attribute.id, attribute.toString());
            }

        },

        getMethod: function (id) {
            if (this.get('methods').has(id))
                return this.get('methods').get(id);
        },
        addMethod: function (method) {
            this.get('methods').set(method.id, method);
            this.updateMarkup();
        },
        removeMethod: function (id) {
            this.get('methods').delete(id)
            this.updateMarkup();
        },
        updateMethod: function (method) {

            if (this.get('methods').has(method.id)) {
                this.get('methods').get(method.id).update(method);
                this.setInputValue('Meth', method.id, this.get('methods').get(method.id).toString());
            }

        },
        getId: function () {
            return this.get('id');
        },
        getType: function () {
            return 'ENTITY';
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

        },
        getOperation(id) {
            if (this.get('methods').has(id))
                return this.get('methods').get(id);
        },
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

let ConstructableEntity = Entity.define('ConstructableEntity',
    {
        constructors: new Map(),
    },
    {
        initialize: function () {
            this.set('constructors', new Map());
            Entity.prototype.initialize.apply(this, arguments);
        },

        getType: function () {
            return 'CONSTRUCTABLE_ENTITY';
        },
        getVisibleElements: function () {
            let elements = Entity.prototype.getVisibleElements.apply(this, arguments);
            if (!this.get('hideMethods')) {
                elements = elements.concat(Array.from(this.get('constructors').values()));
            }
            return elements;
        },
        sectionsMarkup: function () {

            let sections = Entity.prototype.sectionsMarkup.apply(this, arguments);


            let c = Entity.prototype.generateSectionMarkup.call(this, 'methodsContainer', 'Const', this.get('constructors').values());
            let methodEmpty = true;
            if (c.children.length > 0 && !this.get('hideMethods')) {

                for (let sec of sections) {
                    if (sec.selector === 'methodsContainer') {
                        methodEmpty = false;
                        sec.children = [...c.children, ...sec.children];
                    }
                }

                if (methodEmpty) {
                    sections.push(c);
                }
            }


            return sections;

        },
        addConstructor: function (constructor) {
            this.get('constructors').set(constructor.id, constructor);
            this.updateMarkup();
        },
        removeConstructor: function (id) {
            this.get('constructors').delete(id)
            this.updateMarkup();
        },
        updateConstructor: function (constructor) {
            if (this.get('constructors').has(constructor.id)) {
                this.get('constructors').get(constructor.id).update(constructor);
                this.setInputValue('Const', constructor.id, this.get('constructors').get(constructor.id).toString());
            }


        },
        getOperation(id) {
            let op = Entity.prototype.getOperation.call(this, id);
            if (!op && this.get('constructors').has(id)) {
                op = this.get('constructors').get(id);
            }
            return op;
        },
        nbVisibleElements: function () {
            let nb = Entity.prototype.nbVisibleElements.apply(this, arguments);
            nb += this.get('constructors').size * (this.get('hideMethods') ? 0 : 1);
            return nb;
        },
    }
);

export let Enum = ConstructableEntity.define('Enum', {
        attrs: {
            valuesContainer: {
                style: {
                    display: 'flex',
                    fontSize: fontSize,
                    borderBottom: 'solid 1px black',
                    flexDirection: 'column',
                    padding: 3,

                },
            },
        },
        hideVal: false,
        values: new Map(),
    },
    {
        initialize: function () {
            this.set('values', new Map());
            ConstructableEntity.prototype.initialize.apply(this, arguments);
        },

        getType: function () {
            return ElementType.Enum.name;
        },

        sectionsMarkup: function () {

            let sections = ConstructableEntity.prototype.sectionsMarkup.apply(this, arguments);

            let v = ConstructableEntity.prototype.generateSectionMarkup.call(this, 'valuesContainer', 'Val', this.get('values').values());

            if (v.children.length > 0 && !this.get('hideVal')) {
                sections.splice(1, 0, v);

            }


            return sections;

        },

        nbVisibleElements: function () {
            let nb = ConstructableEntity.prototype.nbVisibleElements.apply(this, arguments);
            nb += this.get('values').size * (this.get('hideVal') ? 0 : 1);
            return nb;
        },

        getHeaderName: function () {
            return '<<Enum>>';
        },

        addValue: function (value) {
            if (value.id && this.get('values').has(value.id))
                this.get('values').remove(value.id)
            this.get('values').set(value.name, value);
            this.updateMarkup();
        },
        removeValue: function (name) {
            this.get('values').delete(name);
            this.updateMarkup();
        },
        updateValue: function (value) {
            if (this.get('values').has(value.id)) {
                this.removeValue(value.id);
                this.addValue(value);
                this.setInputValue('Val', value.name, value.toString());
            }
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
        },
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
        update: function (message) {

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
        update: function (message) {
            CustomLink.prototype.update.apply(this, arguments);

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
        update: function (message) {
            LabeledLink.prototype.update.apply(this, arguments);

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
        update: function (message) {
            Association.prototype.update.call(this, message);

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
    update: function (message) {
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

export class GryffiniumManager {

    entities = new Map();

    onChatMessage = undefined;
    onVerticesChange = undefined;
    sendMessage = undefined;

    constructor(onVerticesChange, onChatMessage, sendMessage) {
        this.onVerticesChange = onVerticesChange;
        this.onChatMessage = onChatMessage;
        this.sendMessage = sendMessage;


    }

    create(command) {
        let elem = undefined;
        let parent = undefined;

        switch (command.elementType) {
            case ElementType.Class.name:
                elem = new Class({
                    id: command.id,
                    name: command.name,
                    position: {
                        x: command.x,
                        y: command.y
                    },
                    width: command.width,
                    height: command.height
                });
                elem.setEntityName(command.name);
                elem.setWidth(command.width);
                break
            case ElementType.Interface.name:
                elem = new Interface({
                    id: command.id,
                    name: command.name,
                    position: {
                        x: command.x,
                        y: command.y
                    },
                    width: command.width,
                    height: command.height
                });
                elem.setEntityName(command.name);
                elem.setWidth(command.width);
                break
            case ElementType.Enum.name:
                elem = new Enum({
                    id: command.id,
                    name: command.name,
                    position: {
                        x: command.x,
                        y: command.y
                    },
                    width: command.width,
                    height: command.height
                });
                elem.setEntityName(command.name);
                elem.setWidth(command.width);
                break
            case ElementType.AssociationClass.name:
                elem = new SimpleLink({
                    source: {id: command.classDto.id},
                    target: {id: this.entities.get(command.associationDto.id).get('id')},

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
                });
                elem.on('change:vertices', (link) => this.onVerticesChange(link));
                break;
            case ElementType.Attribute.name:
                let attribute = new Attribute(command.id, command.name, command.type, command.visibility.toLowerCase(), command.isConstant, command.isStatic);
                parent = this.entities.get(command.parentId);
                parent.addAttribute(attribute);
                break;
            case ElementType.Method.name:
                let m = new Method(command.id, command.name, command.type, command.visibility.toLowerCase(), command.isAbstract, command.isStatic);
                parent = this.entities.get(command.parentId);
                parent.addMethod(m);
                break;
            case ElementType.Constructor.name:
                let constructor = new Constructor(command.id, command.name, command.visibility.toLowerCase());
                parent = this.entities.get(command.parentId);
                parent.addConstructor(constructor);
                break;
            case ElementType.Value.name:
                let value = new Value(command.value);
                parent = this.entities.get(command.parentId);
                parent.addValue(value);
                break;
            case ElementType.Parameter.name:
                let parameter = new Parameter(command.id, command.name, command.type);
                let parentEntity = this.entities.get(command.parentId);
                let operation = parentEntity.getOperation(command.methodId);
                operation.addParameter(parameter);
                this.entities.get(command.parentId).setInputValue("Meth", operation.id, operation.toString());
                break;
            case ElementType.Role.name:
                let role = new Role(command.id, command.name, command.multiplicity, command.distanceName, command.offsetName, command.distanceMultiplicity, command.offsetMultiplicity);
                parent = this.entities.get(command.associationId);
                parent.updateRole(command.id, role);
                break;
        }

        if (elem !== undefined) {
            this.entities.set(elem.getId(), elem);

        }
        return elem;

    }

    delete(command) {
        switch (command.elementType) {
            case ElementType.Class.name:
            case ElementType.Interface.name:
            case ElementType.Enum.name:
            case ElementType.Generalization.name:
            case ElementType.BinaryAssociation.name:
            case ElementType.Aggregation.name:
            case ElementType.Composition.name:
            case ElementType.Dependency.name:
            case ElementType.Inner.name:
            case ElementType.MultiAssociation.name:
                let elem = this.entities.get(command.id);
                if (elem !== undefined) {
                    elem.remove();
                    this.entities.delete(elem.getId());
                }
                break;
            case ElementType.AssociationClass.name:
                // TODO AssociationClass
                break;
                // TODO MutliAssociation
                break;
            case ElementType.Attribute.name:
                this.entities.get(command.parentId).removeAttribute(command.id);
                break;
            case ElementType.Method.name:
                this.entities.get(command.parentId).removeMethod(command.id);
                break;
            case ElementType.Constructor.name:
                this.entities.get(command.parentId).removeConstructor(command.id);
                break;
            case ElementType.Value.name:
                this.entities.get(command.parentId).removeValue(command.value);
                break;
            case ElementType.Parameter.name:
                this.entities.get(command.parentId).getOperation(command.methodId).removeParameter(command.id);
                break
        }
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
                let a = new Attribute(command.id, command.name, command.type, command.visibility, command.isConstant, command.isStatic);
                this.entities.get(command.parentId).updateAttribute(a);
                break;
            case ElementType.Method.name:
                let m = new Method(command.id, command.name, command.type, command.visibility, command.isAbstract, command.isStatic);
                this.entities.get(command.parentId).updateMethod(m);
                break;
            case ElementType.Constructor.name:
                let c = new Method(command.id, command.name, command.visibility);
                this.entities.get(command.parentId).updateConstructor(c);
                break;
            case ElementType.Value.name:
                let v = new Value(command.value, command.oldValue);
                this.entities.get(command.parentId).updateValue(v);
                break;
            case ElementType.Parameter.name:
                let parameter = new Parameter(command.id, command.name, command.type);
                let parentEntity = this.entities.get(command.parentId);
                let operation = parentEntity.getOperation(command.methodId);
                operation.updateParametersFromMessage(command);
                this.entities.get(command.parentId).setInputValue((operation instanceof Method ? 'Meth' : 'Const'), operation.id, operation.toString());
                break
            case ElementType.Role.name:
                this.entities.get(command.associationId).updateRole(command.id, command);
                break;
        }
    }

    addAttributeOnElement(elem) {
        if (elem) {
            this.sendMessage({
                    parentId: elem.getId(),
                    name: "attribute",
                    type: "string",
                    visibility: "private",
                    isStatic: false,
                    isConstant: false,
                },
                'ATTRIBUTE',
                'CREATE_COMMAND');
        }
    }


    updateAttribute(update) {
        this.sendMessage(update, 'ATTRIBUTE', 'UPDATE_COMMAND');
    }

    removeAttribute(attribute) {
        this.sendMessage(attribute, 'ATTRIBUTE', 'REMOVE_COMMAND');
    }

    addMethodOnElement(elem) {
        if (elem) {
            this.sendMessage({
                    parentId: elem.getId(),
                    name: "method",
                    visibility: "public",
                    type: "void",
                    isStatic: false,
                    isAbstract: false,
                },
                "METHOD",
                'CREATE_COMMAND');
        }
    }

    updateMethod(update) {

        if (update.parameters) {
            this.updateParameters(update);
        }
        this.sendMessage(update, 'METHOD', 'UPDATE_COMMAND');
    }

    updateParameters(update) {
        let method = this.entities.get(update.parentId).getOperation(update.id);

        for (let i = 0; i < Math.max(update.parameters.length, method.parameters.length); i++) {
            let oldParameter = method.parameters[i];
            let parameter = update.parameters[i];
            if (oldParameter && parameter) {
                this.sendMessage({
                    parentId: update.parentId,
                    methodId: update.id,
                    id: oldParameter.id,
                    name: parameter.name,
                    type: parameter.type,
                }, 'PARAMETER', 'UPDATE_COMMAND')
            } else if (parameter) {
                this.sendMessage({
                    parentId: update.parentId,
                    methodId: update.id,
                    id: parameter.id,
                    name: parameter.name,
                    type: parameter.type,
                }, 'PARAMETER', 'CREATE_COMMAND')
            } else {
                this.sendMessage({
                    parentId: update.parentId,
                    methodId: update.id,
                    id: oldParameter.id,
                }, 'PARAMETER', 'REMOVE_COMMAND')
            }
        }

    }

    removeMethod(method) {
        this.sendMessage(method, 'METHOD', 'REMOVE_COMMAND');
    }

    addConstructorOnElement(elem) {
        if (elem) {
            this.sendMessage({
                    parentId: elem.getId(),
                    visibility: "public",
                    type: "void",
                    isStatic: false,
                    isAbstract: false,
                },
                "CONSTRUCTOR",
                'CREATE_COMMAND');
        }
    }

    updateConstructor(update) {
        if (update.parameters) {
            this.updateParameters(update);
        }
        this.sendMessage(update, 'CONSTRUCTOR', 'UPDATE_COMMAND');
    }

    removeConstructor(constructor) {
        this.sendMessage(constructor, 'CONSTRUCTOR', 'REMOVE_COMMAND');
    }

    addValueOnElement(elem) {
        if (elem) {
            this.sendMessage({
                    parentId: elem.getId(),
                    name: "value",
                },
                'VALUE',
                'CREATE_COMMAND');
        }
    }

    updateValue(update) {
        this.sendMessage(update, 'VALUE', 'UPDATE_COMMAND');
    }

    removeValue(value) {
        this.sendMessage(value, 'VALUE', 'REMOVE_COMMAND');
    }


    removeEntity(cell) {

        this.sendMessage({id: cell.getId()}, cell.getType(), 'REMOVE_COMMAND');
    }

    addEntity(entityType, x, y) {

        this.sendMessage({
                x: x - 100,
                y: y - 50,
                width: 200,
                height: 100,
            },
            entityType,
            'CREATE_COMMAND');
    }

    addLink(linkType, sourceId, targetId) {
        let source = this.entities.get(sourceId);
        let target = this.entities.get(targetId);

        if (linkType === ElementType.AssociationClass.name) {
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
                    name: linkType,
                }
            }, linkType, 'CREATE_COMMAND');
        } else {
            this.sendMessage({
                sourceId: source.id,
                targetId: target.id,
                isDirected: false,
                name: linkType,
            }, linkType, 'CREATE_COMMAND');
        }
    }

    updateEntity(entity, update) {
        update.id = entity.getId();
        this.sendMessage(update, entity.getType(), 'UPDATE_COMMAND');
    }

    updateLink(link, update) {
        update.id = link.getId();
        this.sendMessage(
            update,
            link.getType(), 'UPDATE_COMMAND');
    }


    updateLinksLabels(link) {
        try {
            for (let index of labelsChanged) {
                if (index !== 0) {
                    let roleIndex = Math.ceil(index / 2) - 1;
                    let label = link.get('labels')[index];
                    let role = roleIndex === 0 ? link.getTargetRole() : link.getSourceRole();
                    this.sendMessage({
                        id: role.id,
                        distanceMultiplicity: index % 2 ? undefined : label.position.distance,
                        offsetMultiplicity: index % 2 ? undefined : JSON.stringify(label.position.offset),
                        distanceName: index % 2 ? label.position.distance : undefined,
                        offsetName: index % 2 ? JSON.stringify(label.position.offset) : undefined,
                        associationId: link.getId(),
                        sourceId: link.get('source').id,
                    }, 'ROLE', 'UPDATE_COMMAND');
                } else {
                    let label = link.get('labels')[0];
                    this.sendMessage({
                        id: link.getId(),
                        distance: label.position.distance,
                        offset: JSON.stringify(label.position.offset),
                        sourceId: link.get('source').id,
                    }, link.getType(), 'UPDATE_COMMAND');
                }
            }
        } catch (e) {
            // Ignore
        }
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

    fullText() {
        switch (this.symbol) {
            case "+":
                return "public";
            case "-":
                return "private";
            case "#":
                return "protected";
            case "~":
                return "package";
        }
    }

    static getVisibility(name) {
        switch (name) {
            case "public":
            case "+":
            case Visibility.Public:
                return Visibility.Public;
            case "private":
            case "-":
            case Visibility.Private:
                return Visibility.Private;
            case "protected":
            case "#":
            case Visibility.Protected:
                return Visibility.Protected;
            case "package":
            case "~":
            case Visibility.Package:
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

    static REGEX = /\s*([-+#~])\s*(\w+)\s*:\s*(\w+)/;

    static fromString(text) {

        let match = text.match(Attribute.REGEX);

        if (match === null) {
            throw new Error("Invalid attribute format");
        }

        let attribute = new Attribute();
        attribute.visibility = Visibility.getVisibility(match[1]);
        attribute.name = match[2];
        attribute.type = match[3];

        return attribute;
    }

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

    static CONSTRUCTOR_REGEX = /\s*([-+#~])\s*(\w+)\s*\(([\S\s]*)\)\s*/;

    static fromString(text) {

        let match = text.match(Constructor.CONSTRUCTOR_REGEX);

        if (match === null) {
            throw new Error("Invalid method format");
        }

        let method = new Method();
        method.visibility = Visibility.getVisibility(match[1]);
        method.name = match[2];
        if (match[3] !== "") {
            let match_params = [...match[3].matchAll(Method.PARAMS_REGEX)];

            let params = [];
            if (match_params === []) {
                throw new Error("Invalid parameters format");
            }
            for (let param of match_params) {
                params.push(new Parameter(undefined, param[2], param[3]));
            }
            method.parameters = params;
        }
        return method;
    }

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

    static METHOD_REGEX = /\s*([-+#~])\s*(\w+)\s*\(([\S\s]*)\)\s*:\s*(\w+)/;
    static PARAMS_REGEX = /((?:\s*(\w+)\s*:\s*(\w+)\s*)+)\s*,?\s*/g;

    static fromString(text) {

        let match = text.match(Method.METHOD_REGEX);

        if (match === null) {
            throw new Error("Invalid method format");
        }

        let method = new Method();
        method.visibility = Visibility.getVisibility(match[1]);
        method.name = match[2];
        method.type = match[4];
        if (match[3] !== "") {
            let match_params = [...match[3].matchAll(Method.PARAMS_REGEX)];

            let params = [];
            if (match_params === []) {
                throw new Error("Invalid parameters format");
            }
            for (let param of match_params) {
                params.push(new Parameter(undefined, param[2], param[3]));
            }
            method.parameters = params;
        }
        return method;
    }

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
        let result = this.visibility + " " + this.name + this.paramsToString() + " : " + this.type;
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
    constructor(name, oldValue) {
        this.name = name;
        this.id = oldValue ? oldValue : name;
    }

    update(modification) {
        if (modification.name) {
            this.id = this.name;
            this.name = modification.name;
        }
    }

    toString() {
        return this.name;
    }
}

export class Parameter {

    REGEX = /(\w+)\s*:\s*(\w+)/;

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
