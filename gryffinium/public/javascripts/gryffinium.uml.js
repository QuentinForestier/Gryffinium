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
export const Entity = joint.dia.Element.define('Entity', {
    attrs: {

        body: {
            width: 'calc(w)',
            height: 'calc(h)',

        },
        foreignObject: {
            width: '200',
            height:'100',
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

        valuesContainer: {
            style: {
                display: 'flex',
                fontSize: fontSize,
                //borderBottom: 'solid 1px black',
                flexDirection: 'column',
                padding: 3,

            },
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

        id:undefined,
        name: undefined,

        alreadyDeleted:false,
        toolsBox: undefined,

    },
}, {
    markup: [

    ],

    initialize: function () {
        this.on('change:name', function(){
            this.trigger('uml-update');
        })

        this.updateMarkup(true, true, true);
        this.autoHeight([1,2,3,4,5,6])
        joint.shapes.standard.Rectangle.prototype.initialize.apply(this, arguments);
    },
    updateMarkup: function (showVal, showAttr, showMeth) {
        let attributes = {
            tagName: 'div',
            selector: 'attrsContainer',
            children: []
        };

        let values = {
            tagName: 'div',
            selector: 'valuesContainer',
            children: []
        };

        let methods = {
            tagName: 'div',
            selector: 'methodsContainer',
            children: []
        };

        for (let i = 0; i < 2; ++i) {

            let obj = this.generateInput("Val", {parentId: 1, text: "val" + i, id: i});

            values.children.push(obj.markup);
        }

        for (let i = 0; i < 2; ++i) {

            let obj = this.generateInput("Attr", {parentId: 1, text: "attr" + i, id: i});

            attributes.children.push(obj.markup);
        }


        for (let i = 0; i < 2; ++i) {

            let obj = this.generateInput("Meth", {parentId: 1, text: "meth" + i, id: i});

            methods.children.push(obj.markup);
        }

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
            },
        ]

        if (values.children.length > 0 && showVal) {
            sections.push(values);
            this.attr()['attrsContainer'].style.borderTop = "solid 1px black";
            this.attr()['methodsContainer'].style.borderTop = "solid 1px black";
        }

        if (attributes.children.length > 0 && showAttr) {
            sections.push(attributes);
            this.attr()['methodsContainer'].style.borderTop = "solid 1px black";
        }
        //values.children.length > 0 && showVal ? sections.push(values) : undefined;
        methods.children.length > 0 && showMeth ? sections.push(methods) : undefined;


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
                        children: sections,
                    }
                ]
            }
        ]


        this.set('markup', markup);
        this.trigger('uml-update');
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
    setWidth: function(width = 100){
        this.get('attrs').foreignObject.width = width;
    },
    getWidth: function(){
        return this.get('attrs').foreignObject.width;
    },
    autoWidth: function(elements){
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

    setHeight: function(height = 100){
        this.get('attrs').foreignObject.height = height;
    },
    getHeight:function(){
        return this.get('attrs').foreignObject.height;
    },
    autoHeight: function(elements){

        this.setHeight(elements.length * (19.6) + 55);
    },

    setColor: function(color){
        this.get('attrs').background.style.backgroundColor = color;
    },
    getColor: function(){
        return this.get('attrs').background.style.backgroundColor;
    },

    selected: function(isSelected){
        // TODO
    },

    getEntityName: function(){
        return this.get('attrs').header.value;
    },
    setEntityName: function(name){
      this.get('attrs').header.value = name;
        this.trigger('uml-update')
    },

    getInputValue: function(type, id){
        return this.get('attrs')[type+id].value;
    },
    setInputValue: function(type, id, text){
        let selector = type+id;
        this.attr(selector + '/value', text);
        this.trigger('uml-update')
    },

    /*getId: function(){
        return this.get('id');
    },*/

    /*getType: function(){
        return 'CLASS';
    }*/


}, {
    attributes:{
        value:{
            set: function(text, _, node){
                if('value' in node) node.value = text;
            }
        }
    }
});





/** END Graphical elements **/

/** Utility elements **/

/** END Utility elements**/


/** Hidden elements **/

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

/** END Hidden elements **/
