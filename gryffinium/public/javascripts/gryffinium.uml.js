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

        link.source(element.source.getCell());
        link.target(element.target.getCell());
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


        this.cell.appendLabel({
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
        })
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
    constructor(element, position, cell = new uml.Class()) {
        cell.attributes.name = element.name;
        cell.attributes.size = {width: 100, height: 100};
        cell.attributes.position = position;


        cell.updateRectangles();
        super(element, cell)
    }
}

export class AbstractClass extends Class {
    constructor(element, position) {
        super(element, position, new uml.Abstract({}))
    }
}

export class Interface extends Class {
    constructor(element, position) {
        super(element, position, new uml.Interface({}))
    }
}


