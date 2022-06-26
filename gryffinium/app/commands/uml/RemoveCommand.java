package commands.uml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import graphical.GraphicalElement;
import graphical.GraphicalElementType;
import graphical.entities.*;
import graphical.entities.operations.GraphicalMethod;
import graphical.entities.operations.GraphicalOperation;
import graphical.entities.variables.GraphicalAttribute;
import graphical.entities.variables.GraphicalValue;
import graphical.links.GraphicalBinaryAssociation;
import models.Project;
import play.libs.Json;
import uml.entities.*;
import uml.entities.Class;
import uml.entities.Enum;
import uml.entities.operations.Constructor;
import uml.entities.operations.Method;
import uml.entities.variables.Attribute;
import uml.links.BinaryAssociation;

public class RemoveCommand implements Command
{
    JsonNode data;
    GraphicalElementType elementType;

    public RemoveCommand(JsonNode data, GraphicalElementType elementType)
    {
        this.data = data;
        this.elementType = elementType;
    }

    @Override
    public JsonNode execute(Project project)
    {
        ObjectNode result = null;

        GraphicalElement ge = null;
        switch (elementType)
        {
            case CLASS:
                ge = Json.fromJson(data,
                        GraphicalClass.class);
                project.getDiagram().removeEntity(project.getDiagram().getEntity(ge.getId()));
                result = (ObjectNode) Json.toJson(ge);
                break;
            case INNER_CLASS:
                ge = Json.fromJson(data,
                        GraphicalInnerClass.class);
                project.getDiagram().removeEntity(project.getDiagram().getEntity(ge.getId()));
                result = (ObjectNode) Json.toJson(ge);
                break;
            case ASSOCIATION_CLASS:
                ge = Json.fromJson(data,
                        GraphicalAssociationClass.class);
                project.getDiagram().removeEntity(project.getDiagram().getEntity(ge.getId()));
                result = (ObjectNode) Json.toJson(ge);
                break;
            case ENUM:
                ge = Json.fromJson(data,
                        GraphicalEnum.class);
                project.getDiagram().removeEntity(project.getDiagram().getEntity(ge.getId()));
                result = (ObjectNode) Json.toJson(ge);
                break;
            case INTERFACE:
                ge = Json.fromJson(data,
                        GraphicalEntity.class);
                project.getDiagram().removeEntity(project.getDiagram().getEntity(ge.getId()));
                result = (ObjectNode) Json.toJson(ge);
                break;
            case INNER_INTERFACE:
                ge = Json.fromJson(data,
                        GraphicalInnerInterface.class);
                project.getDiagram().removeEntity(project.getDiagram().getEntity(ge.getId()));
                result = (ObjectNode) Json.toJson(ge);
                break;


            case BINARY_ASSOCIATION:
            case AGGREGATION:
            case COMPOSITION:
                GraphicalBinaryAssociation gba = Json.fromJson(data,
                        GraphicalBinaryAssociation.class);
                BinaryAssociation ba = new BinaryAssociation(gba,
                        project.getDiagram());
                project.getDiagram().addAssociation(ba);
                result = (ObjectNode) Json.toJson(ba);
                break;
            case MUTLI_ASSOCIATION:
                break;
            case DEPENDENCY:
                break;
            case GENEREALIZATION:
                break;
            case REALIZATION:
                break;
            case INNER:
                break;

            case VALUE:
                GraphicalValue gv = Json.fromJson(data,
                        GraphicalValue.class);

                Enum e = (Enum) project.getDiagram().getEntity(gv.getParentId());
                e.removeValue(gv.getValue());
                result = (ObjectNode) Json.toJson(gv);
                break;

            case ATTRIBUTE:
                GraphicalAttribute ga = Json.fromJson(data,
                        GraphicalAttribute.class);
                Entity parent = project.getDiagram().getEntity(ga.getParentId());
                parent.removeAttribute(parent.getAttribute(ga.getId()));
                result = (ObjectNode) Json.toJson(ga);
                break;
            case PARAMETER:
                break;


            case CONSTRUCTOR:
                GraphicalOperation go = Json.fromJson(data,
                        GraphicalOperation.class);
                ConstructableEntity ce = (ConstructableEntity) project.getDiagram().getEntity(go.getParentId());
                ce.removeConstructor(ce.getConstructorById(go.getId()));
                result = (ObjectNode) Json.toJson(go);
                break;
            case METHOD:
                GraphicalMethod gm = Json.fromJson(data,
                        GraphicalMethod.class);
                Entity parent2 = project.getDiagram().getEntity(gm.getParentId());
                parent2.removeMethod(parent2.getMethodById(gm.getId()));
                result = (ObjectNode) Json.toJson(gm);
                break;
        }

        result.put("elementType", elementType.toString());

        return result;
    }
}
