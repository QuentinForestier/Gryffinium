let uml = joint.shapes.uml;

class Cell {
    constructor(element, cell) {
        this.cell = cell;
        this.element = element;
    }

    getCell() {
        return this.cell;
    }

    getElement() {
        return this.element;
    }
}


class Link extends Cell {
    constructor(element, line = undefined, vertices = []) {
        let link = new joint.shapes.standard.Link();

        line.strokeWidth = 1;

        link.source(element.source);
        link.target(element.target);
        link.attr({
            line
        });
        link.connector('jumpover', {size: 5});

        link.vertices(vertices)
        link.router('normal', {
            padding: 10,
            startDirections: ['top'],
            endDirections: ['bottom']
        })

        super(element, link)
    }
}

export class Association extends Link {
    constructor(element, vertices = [], sourceMarker = undefined) {
        let line = {
            sourceMarker
        }

        if (element.isDirected) {
            line.targetMarker = {
                'type': 'path',
                'd': 'M 20 -10 0 0 20 10 0 0 Z'
            }
        } else {
            line.targetMarker = {d: ''};
        }
        super(element, line, vertices);


        /*this.cell.appendLabel({
            attrs: {
                text: {
                    text: element.label ? element.label : '    ',
                }
            },
            position: {
                distance: 0.5,
                offset: {
                    x:0,
                    y:-10
                },
                args: {
                    keepGradient: true,
                    ensureLegibility: true
                }
            }
        })

        this.cell.appendLabel({
            attrs: {
                text: {
                    text: element.sourceLabel ? element.sourceLabel : '   ',
                }
            },
            position: {
                distance: 0.1,
                offset: {
                    x:0,
                    y:-20
                },
                args: {
                    keepGradient: true,
                    ensureLegibility: true
                }
            }
        })

        this.cell.appendLabel({
            attrs: {
                text: {
                    text: element.sourceMultiplicity ? element.sourceMultiplicity : '*',
                }
            },
            position: {
                distance: 0.1,
                offset: {
                    x:0,
                    y:20
                },
                args: {
                    keepGradient: true,
                    ensureLegibility: true
                }
            }
        })*/
    }
}

export class Aggregation extends Association {
    constructor(element, vertices = [], color = 'white') {

        let sourceMarker = {
            'type': 'path',
            d: 'M 40 0 L 20 10 L 0 00 L 20 -10 z',
            'fill': color
        }
        super(element, vertices, sourceMarker);
    }

}

export class Composition extends Aggregation {
    constructor(element, vertices = []) {
        super(element, vertices, 'black');
    }
}

export class Generalization extends Link {
    constructor(element, vertices = [], stroke = undefined) {
        let line = {
            targetMarker: {
                'type': 'path',
                'd': 'M 20 -10 L 0 0 L 20 10 z',
                'fill': 'white'
            }
        }
        if (stroke) {
            line.strokeDasharray = stroke;
        }
        super(element, line, vertices);
    }
}

export class Realization extends Generalization {
    constructor(element, vertices = []) {
        super(element, vertices, '5 5');
    }
}


export class Class extends Cell {
    constructor(element, cell) {
        if (cell === undefined) {
            cell = new uml.Class({});
        }
        super(element, cell)
        this.setGraphicalAttributes(element);
    }

    setGraphicalAttributes(element) {
        this.cell.attributes.name = element.name;
        this.cell.attributes.size = {width: element.width, height: element.height};
        this.cell.attributes.position = {x: element.x, y: element.y};
        this.cell.updateRectangles();
    }
}

export class Interface extends Class {
    constructor(element) {
        super(element, new uml.Interface({}))
    }
}


export class ElementType {
    static Class = new ElementType("CLASS");
    static Enum = new ElementType("ENUM");

    static Interface = new ElementType("INTERFACE");
    static AssociationClass = new ElementType("ASSOCIATION_CLASS");
    static Generalization = new ElementType("GENERALIZATION");
    static BinaryAssociation = new ElementType("BINARY_ASSOCIATION");
    static Aggregation = new ElementType("AGGREGATION");
    static Composition = new ElementType("COMPOSITION");

    static MultiAssociation = new ElementType("MUTLI_ASSOCIATION");
    static InnerClass = new ElementType("INNER_CLASS");
    static Dependency = new ElementType("DEPENDENCY");

    constructor(name) {
        this.name = name;
    }

}

export class Visibility{
    static Public = new Visibility("+");
    static Private = new Visibility("-");
    static Protected = new Visibility("#");
    static Package = new Visibility("~");

    constructor(symbol) {
        this.symbol = symbol;
    }

    toString(){
        return this.symbol;
    }

    static getVisibility(name){
        switch(name){
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

    setVisibility(visibility){
        this.visibility = Visibility.getVisibility(visibility);
    }

    toString() {
        return this.visibility + " " + this.name + " : " + this.type;
    }
}

