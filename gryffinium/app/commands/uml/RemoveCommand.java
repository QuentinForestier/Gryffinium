package commands.uml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import graphical.GraphicalElement;
import graphical.GraphicalElementType;
import graphical.entities.*;
import graphical.entities.operations.GraphicalMethod;
import graphical.entities.operations.GraphicalOperation;
import graphical.entities.variables.GraphicalAttribute;
import graphical.entities.variables.GraphicalParameter;
import graphical.entities.variables.GraphicalValue;
import graphical.links.GraphicalBinaryAssociation;
import models.Project;
import play.libs.Json;
import uml.entities.*;
import uml.entities.Enum;
import uml.entities.operations.Constructor;
import uml.entities.operations.Method;
import uml.entities.operations.Operation;
import uml.entities.variables.Parameter;
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
    public ArrayNode execute(Project project)
    {
        ArrayNode result = Json.newArray();

        GraphicalElement ge = null;
        switch (elementType)
        {
            case CLASS:
                ge = Json.fromJson(data,
                        GraphicalClass.class);
                project.getDiagram().removeEntity(project.getDiagram().getEntity(ge.getId()));
                result.add(createResponse(ge, elementType));
                break;
            case INNER_CLASS:
                ge = Json.fromJson(data,
                        GraphicalInnerClass.class);
                project.getDiagram().removeEntity(project.getDiagram().getEntity(ge.getId()));
                result.add(createResponse(ge, elementType));
                break;
            case ASSOCIATION_CLASS:
                ge = Json.fromJson(data,
                        GraphicalAssociationClass.class);
                project.getDiagram().removeEntity(project.getDiagram().getEntity(ge.getId()));
                result.add(createResponse(ge, elementType));
                break;
            case ENUM:
                ge = Json.fromJson(data,
                        GraphicalEnum.class);
                project.getDiagram().removeEntity(project.getDiagram().getEntity(ge.getId()));
                result.add(createResponse(ge, elementType));
                break;
            case INTERFACE:
                ge = Json.fromJson(data,
                        GraphicalEntity.class);
                project.getDiagram().removeEntity(project.getDiagram().getEntity(ge.getId()));
                result.add(createResponse(ge, elementType));
                break;
            case INNER_INTERFACE:
                ge = Json.fromJson(data,
                        GraphicalInnerInterface.class);
                project.getDiagram().removeEntity(project.getDiagram().getEntity(ge.getId()));
                result.add(createResponse(ge, elementType));
                break;


            case BINARY_ASSOCIATION:
            case AGGREGATION:
            case COMPOSITION:
                GraphicalBinaryAssociation gba = Json.fromJson(data,
                        GraphicalBinaryAssociation.class);
                project.getDiagram().removeAssociation(project.getDiagram().getAssociation(gba.getId()));
                result.add(createResponse(gba, elementType));
                break;
            case MUTLI_ASSOCIATION:
                break;
            case DEPENDENCY:
                break;
            case GENERALIZATION:
                break;
            case REALIZATION:
                break;
            case INNER:
                break;

            case VALUE:
                GraphicalValue gv = Json.fromJson(data,
                        GraphicalValue.class);

                Enum e =
                        (Enum) project.getDiagram().getEntity(gv.getParentId());
                e.removeValue(gv.getValue());
                result.add(createResponse(gv, elementType));
                break;

            case ATTRIBUTE:
                GraphicalAttribute ga = Json.fromJson(data,
                        GraphicalAttribute.class);
                Entity parent =
                        project.getDiagram().getEntity(ga.getParentId());
                parent.removeAttribute(parent.getAttribute(ga.getId()));
                result.add(createResponse(ga, elementType));
                break;
            case PARAMETER:
                GraphicalParameter gp = Json.fromJson(data,
                        GraphicalParameter.class);
                Entity entity =
                        project.getDiagram().getEntity(gp.getParentId());
                Operation op = entity.getMethodById(gp.getMethodId());
                if (op == null)
                {
                    ConstructableEntity ce = (ConstructableEntity) entity;
                    op = ce.getConstructorById(gp.getMethodId());
                }
                op.removeParam(gp.getId());
                result.add(createResponse(gp, elementType));
                break;


            case CONSTRUCTOR:
                GraphicalOperation go = Json.fromJson(data,
                        GraphicalOperation.class);
                ConstructableEntity ce =
                        (ConstructableEntity) project.getDiagram().getEntity(go.getParentId());
                ce.removeConstructor(ce.getConstructorById(go.getId()));
                result.add(createResponse(go, elementType));
                break;
            case METHOD:
                GraphicalMethod gm = Json.fromJson(data,
                        GraphicalMethod.class);
                Entity parent2 =
                        project.getDiagram().getEntity(gm.getParentId());
                parent2.removeMethod(parent2.getMethodById(gm.getId()));
                result.add(createResponse(gm, elementType));
                break;
        }

        return result;
    }
}
