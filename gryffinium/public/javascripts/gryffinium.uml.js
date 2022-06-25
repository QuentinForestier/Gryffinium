let uml = joint.shapes.uml;


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

    static Value = new ElementType("VALUE");

    static Attribute = new ElementType("ATTRIBUTE");

    static Method = new ElementType("METHOD");
    static Constructor = new ElementType("CONSTRUCTOR");

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

export class Constructor{
    constructor(id, name, visibility) {
        this.id = id;
        this.name = name;
        this.visibility = Visibility.getVisibility(visibility);
    }

    setVisibility(visibility){
        this.visibility = Visibility.getVisibility(visibility);
    }

    toString() {
        return this.visibility + " " + this.name;
    }
}

export class Method{
    constructor(id, name, type, visibility, isAbstract, isStatic) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.visibility = Visibility.getVisibility(visibility);
        this.isAbstract = isAbstract;
        this.isStatic = isStatic;
    }

    setVisibility(visibility){
        this.visibility = Visibility.getVisibility(visibility);
    }

    toString() {
        return this.visibility + " " + this.name + " : " + this.type;
    }
}

export class Value{
    constructor(name) {
        this.name = name;
    }

    toString(){
        return this.name;
    }
}

