package dto;

import com.sun.javadoc.Type;

public enum ElementTypeDto
{
    // Entities
    CLASS,
    INNER_CLASS,
    ASSOCIATION_CLASS,
    ENUM,
    INTERFACE,
    INNER_INTERFACE,

    // Links
    BINARY_ASSOCIATION,
    MUTLI_ASSOCIATION,
    AGGREGATION,
    COMPOSITION,
    DEPENDENCY,
    GENERALIZATION,
    REALIZATION,
    INNER,

    ROLE,

    // Enum Values
    VALUE,

    // Attributes
    ATTRIBUTE,
    PARAMETER,

    // Operations
    CONSTRUCTOR,
    METHOD,

}
