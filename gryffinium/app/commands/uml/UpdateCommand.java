package commands.uml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
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
import uml.entities.Class;
import uml.entities.Enum;
import uml.entities.operations.Constructor;
import uml.entities.operations.Operation;
import uml.entities.variables.Parameter;
import uml.links.BinaryAssociation;

public class UpdateCommand implements Command
{
    JsonNode data;

    GraphicalElementType elementType;

    public UpdateCommand(JsonNode data, GraphicalElementType elementType)
    {
        this.data = data;
        this.elementType = elementType;
    }

    public void setData(JsonNode data)
    {
        this.data = data;
    }

    public JsonNode getData()
    {
        return data;
    }

    @Override
    public ArrayNode execute(Project project)
    {
        ArrayNode result = Json.newArray();

        switch (elementType)
        {
            case CLASS:
                GraphicalClass ge = Json.fromJson(data,
                        GraphicalClass.class);
                Class c = (Class) project.getDiagram().getEntity(ge.getId());

                if (!c.getName().equals(ge.getName()) && ge.getName() != null)
                {
                    result.addAll(updateConstructorName(c, ge.getName()));
                }
                c.setGraphical(ge);

                result.add(createResponse(ge, elementType));
                break;
            case INNER_CLASS:
                GraphicalInnerClass gic = Json.fromJson(data,
                        GraphicalInnerClass.class);
                project.getDiagram().getEntity(gic.getId()).setGraphical(gic);
                result.add(createResponse(gic, elementType));
                break;
            case ASSOCIATION_CLASS:
                GraphicalAssociationClass gac =
                        Json.fromJson(data,
                                GraphicalAssociationClass.class);
                project.getDiagram().getEntity(gac.getId()).setGraphical(gac);
                result.add(createResponse(gac, elementType));
                break;
            case ENUM:
                GraphicalEnum gen = Json.fromJson(data,
                        GraphicalEnum.class);
                Enum e = (Enum) project.getDiagram().getEntity(gen.getId());

                if (!e.getName().equals(gen.getName()))
                {
                    result.addAll(updateConstructorName(e, gen.getName()));
                }
                e.setGraphical(gen);
                result.add(createResponse(gen, elementType));
                break;
            case INTERFACE:
                GraphicalEntity gc = Json.fromJson(data,
                        GraphicalEntity.class);
                project.getDiagram().getEntity(gc.getId()).setGraphical(gc);
                result.add(createResponse(gc, elementType));
                break;
            case INNER_INTERFACE:
                GraphicalInnerInterface gi = Json.fromJson(data,
                        GraphicalInnerInterface.class);
                project.getDiagram().getEntity(gi.getId()).setGraphical(gi);
                result.add(createResponse(gi, elementType));
                break;


            case BINARY_ASSOCIATION:
            case AGGREGATION:
            case COMPOSITION:
                GraphicalBinaryAssociation gba = Json.fromJson(data,
                        GraphicalBinaryAssociation.class);

                project.getDiagram().getAssociation(gba.getId()).setGraphical(gba);
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

                Enum eParent =
                        (Enum) project.getDiagram().getEntity(gv.getParentId());
                eParent.updateValue(gv.getOldValue(), gv.getValue());
                result.add(createResponse(gv, elementType));
                break;

            case ATTRIBUTE:
                GraphicalAttribute ga = Json.fromJson(data,
                        GraphicalAttribute.class);

                project.getDiagram().getEntity(ga.getParentId()).getAttribute(ga.getId()).setGraphical(ga, project.getDiagram());
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
                Parameter p = op.getParam(gp.getId());
                p.setGraphical(gp, project.getDiagram());
                result.add(createResponse(gp, elementType));
                break;


            case CONSTRUCTOR:
                GraphicalOperation go = Json.fromJson(data,
                        GraphicalOperation.class);

                ConstructableEntity parent =
                        (ConstructableEntity) project.getDiagram().getEntity(go.getParentId());

                parent.getConstructorById(go.getId()).setGraphical(go,
                        project.getDiagram());
                result.add(createResponse(go, elementType));
                break;
            case METHOD:
                GraphicalMethod gm = Json.fromJson(data,
                        GraphicalMethod.class);
                project.getDiagram().getEntity(gm.getParentId()).getMethodById(gm.getId()).setGraphical(gm, project.getDiagram());
                result.add(createResponse(gm, elementType));
                break;
        }

        return result;
    }

    private ArrayNode updateConstructorName(ConstructableEntity ce, String name)
    {
        ArrayNode result = Json.newArray();
        for (Constructor ctor : ce.getConstructors())
        {
            ctor.setName(name);
            GraphicalOperation go = new GraphicalOperation();
            go.setParentId(ce.getId());
            go.setId(ctor.getId());
            go.setName(ctor.getName());
            go.setVisibility(ctor.getVisibility().getName());
            result.add(createResponse(go, GraphicalElementType.CONSTRUCTOR));
        }

        return result;
    }


}
