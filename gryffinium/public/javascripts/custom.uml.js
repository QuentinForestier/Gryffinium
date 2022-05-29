export function createLink(element) {
    let line = {
        targetMarker: {
            'type': 'path',
        }
    };
    switch (element.type) {
        case 'aggregation':
            line = {
                targetMarker: {
                    'type': 'path',
                    d: 'M 40 0 L 20 10 L 0 00 L 20 -10 z',
                    'fill': 'white'
                }
            }
            break;
        case 'composition':
            line = {
                targetMarker: {
                    type: 'path',
                    d: 'M 40 0 L 20 10 L 0 00 L 20 -10 z',
                    fill: 'black'
                }
            }
            break;
        case 'realization':
            line = {
                strokeDasharray: '5 5',
                targetMarker: {
                    'type': 'path',
                    'd': 'M 20 -10 L 0 0 L 20 10 z',
                    'fill': 'white'
                }
            }
            break;
        case 'generalization':
            line = {
                targetMarker: {
                    'type': 'path',
                    'd': 'M 20 -10 L 0 0 L 20 10 z',
                    'fill': 'white'
                }
            }
            break;
    }


    if(element.isDirected){
        line.sourceMarker = {
            sourceMarker: {
                'type': 'path',
                'd': 'M 20 -10 0 0 20 10 0 0 Z'
            }
        }
    }


    let link = new joint.shapes.standard.Link();
    link.attr(
      line
    );
    link.source(element.source);
    link.target(element.target);
    link.connector('jumpover', {size: 5});

    return link;
}

