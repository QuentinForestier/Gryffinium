
const fontFamiliy = 'Helvetica, sans-serif';
const fontSize = 15
const umlColor = '#FFF7E1';
export function onChange(sender, idEntity, type, idChild) {
    console.log(sender);
}

const standardInput = {
    style: {
        display: 'flex',
        height: '100%',
        border: 'none',
        padding: 'none',
        backgroundColor: umlColor,
    },
    size: 8,
    onchange: 'onChange("oui")',
}

export const Form = joint.dia.Element.define('uml.Form', {
    attrs: {
        body: {
            width: 'calc(w)',
            height: 'calc(h)',
        },
        foreignObject: {
            width: '200',
            height: '150',
            x: 0,
            y: 0
        },
        background: {
            style: {
                backgroundColor: umlColor,
                border: '1px solid black',
                height: '100%'
            },
        },
        headerDiv: {
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
                backgroundColor: umlColor,
                textAlign: 'center',
                padding: 0
            },
            size: '10',
            value: 'Header'
        },
        values: {
            style: {
                display: 'flex',
                fontSize: fontSize,
                //borderBottom: 'solid 1px black',
                flexDirection: 'column',
                padding: 3,

            },
        },
        attrs: {
            style: {
                display: 'flex',
                fontSize: fontSize,
                flexDirection: 'column',
                padding: 3,
            },
        },
        methods: {
            style: {
                display: 'flex',
                fontSize: fontSize,
                //borderTop: 'solid 1px black',
                flexDirection: 'column',
                padding: 3,
            },
        },
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
                    children: [
                        // Header
                        {
                            tagName: 'div',
                            selector: 'headerDiv',
                            children: [
                                {
                                    tagName: 'input',
                                    selector: 'header',
                                },
                            ]
                        },
                        // Values
                        {
                            tagName: 'div',
                            selector: 'values',
                        },
                        // Attrs
                        {
                            tagName: 'div',
                            selector: 'attrs',
                        },
                        // Methods
                        {
                            tagName: 'div',
                            selector: 'methods',
                        },

                    ]
                }
            ]
        }
    ],

    initialize: function () {

        joint.dia.Element.prototype.initialize.apply(this, arguments);
        this.updateMarkup(true, true, true);
    },



    updateMarkup: function (showVal, showAttr, showMeth) {
        let attributes = {
            tagName: 'div',
            selector: 'attrs',
            children: []
        };

        let values = {
            tagName: 'div',
            selector: 'values',
            children: []
        };

        let methods = {
            tagName: 'div',
            selector: 'methods',
            children: []
        };

        for (let i = 0; i <1; ++i) {

            let obj = this.generateInput("Val", {parentId:1, text:"val"+i, id: i});

            values.children.push(obj.markup);
        }

        for (let i = 0; i <2; ++i) {

            let obj = this.generateInput("Attr", {parentId:1, text:"attr"+i, id: i});

            attributes.children.push(obj.markup);
        }


        for (let i = 0; i <2; ++i) {

            let obj = this.generateInput("Meth", {parentId:1, text:"meth"+i, id: i});

            methods.children.push(obj.markup);
        }

        let sections = [
            {
                tagName: 'div',
                selector: 'headerDiv',
                children: [
                    {
                        tagName: 'input',
                        selector: 'header',
                    },
                ]
            },
        ]

        if( values.children.length > 0 && showVal){
            sections.push(values);
            this.attr()['attrs'].style.borderTop = "solid 1px black";
            this.attr()['methods'].style.borderTop = "solid 1px black";
        }

        if(attributes.children.length > 0 && showAttr){
            sections.push(attributes);
            this.attr()['methods'].style.borderTop = "solid 1px black";
        }
        //values.children.length > 0 && showVal ? sections.push(values) : undefined;
        methods.children.length > 0 && showMeth ? sections.push(methods) : undefined;


        let markup = [
            {
                tagName: 'rect',
                selector: 'body'
            },
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


        console.log(markup);
        this.set('markup', markup);

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


});


