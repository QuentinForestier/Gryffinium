let uml = joint.shapes.uml;


export class ElementType {
    static Class = new ElementType("CLASS");
    static Enum = new ElementType("ENUM");

    static Interface = new ElementType("INTERFACE");
    static AssociationClass = new ElementType("ASSOCIATION_CLASS");
    static Generalization = new ElementType("GENERALIZATION");
    static Realization = new ElementType("REALIZATION");
    static BinaryAssociation = new ElementType("BINARY_ASSOCIATION");
    static Aggregation = new ElementType("AGGREGATION");
    static Composition = new ElementType("COMPOSITION");

    static MultiAssociation = new ElementType("MUTLI_ASSOCIATION");
    static InnerClass = new ElementType("INNER_CLASS");
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

        return this.visibility + " " + this.name + " : " + this.type;
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

    paramsToString() {
        let params = "";
        this.parameters.forEach(p => {
            params += p.name + " : " + p.type + ", ";
        });
        return "(" + params.substring(0, params.length - 2) + ")";
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
        return this.visibility + " " + this.name + this.paramsToString() + " : " + this.type;
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
        this.distanceName = distanceName;
        this.offsetName = offsetName;
        this.distanceMultiplicity = distanceMultiplicity;
        this.offsetMultiplicity = offsetMultiplicity;
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
            this.distanceName = role.distanceName;
        }
        if (role.offsetName !== null && role.offsetName !== undefined) {
            this.offsetName = role.offsetName;
        }
        if (role.distanceMultiplicity !== null && role.distanceMultiplicity !== undefined) {
            this.distanceMultiplicity = role.distanceMultiplicity;
        }
        if (role.offsetMultiplicity !== null && role.offsetMultiplicity !== undefined) {
            this.offsetMultiplicity = role.offsetMultiplicity;
        }
    }
}
