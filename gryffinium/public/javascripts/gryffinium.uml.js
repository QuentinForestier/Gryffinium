/** Other things **/
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
/** END Other things **/

/** Graphical elements **/
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
                    border: '2px solid blue',
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

            this.updateMarkup();
            joint.dia.Element.prototype.initialize.apply(this, arguments);
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


/** END Graphical elements **/

/** Controller **/

export class UMLController {

    entities = new Map();

    onChatMessage = undefined;
    sendMessage = undefined;

    constructor(onChatMessage, sendMessage) {
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
                    name: name,
                    toolsBox: elementToolsView,
                    position: {
                        x: command.x,
                        y: command.y
                    },
                    width: command.width,
                    height: command.height
                });
                break
            case ElementType.Interface.name:
                elem = new Interface({
                    id: command.id,
                    name: name,
                    toolsBox: elementToolsView,
                    position: {
                        x: command.x,
                        y: command.y
                    },
                    width: command.width,
                    height: command.height
                });
                break
            case ElementType.Enum.name:
                elem = new Enum({
                    id: command.id,
                    name: name,
                    toolsBox: elementToolsView,
                    position: {
                        x: command.x,
                        y: command.y
                    },
                    width: command.width,
                    height: command.height
                });
                break

        }

        if (elem !== undefined) {
            this.entities.set(elem.getId(), elem);
        }
        return elem;
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
}

/** END Controller **/

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
