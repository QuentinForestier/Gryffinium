package commands.uml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import dto.entities.*;
import dto.entities.operations.MethodDto;
import dto.entities.operations.OperationDto;
import dto.entities.variables.AttributeDto;
import dto.entities.variables.ParameterDto;
import dto.entities.variables.ValueDto;
import dto.links.BinaryAssociationDto;
import models.Project;
import play.libs.Json;
import uml.entities.*;
import uml.entities.Class;
import uml.entities.Enum;
import uml.entities.operations.Constructor;
import uml.entities.operations.Operation;
import uml.entities.variables.Parameter;

import static dto.ElementTypeDto.CONSTRUCTOR;

public class UpdateCommand implements Command
{
    JsonNode data;

    dto.ElementTypeDto elementType;

    public UpdateCommand(JsonNode data, dto.ElementTypeDto elementType)
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
                ClassDto ge = Json.fromJson(data,
                        ClassDto.class);
                Class c = (Class) project.getDiagram().getEntity(ge.getId());

                if (!c.getName().equals(ge.getName()) && ge.getName() != null)
                {
                    result.addAll(updateConstructorName(c, ge.getName()));
                }
                c.setGraphical(ge);

                result.add(Command.createResponse(ge, elementType));
                break;
            case INNER_CLASS:
                InnerClassDto gic = Json.fromJson(data,
                        InnerClassDto.class);
                project.getDiagram().getEntity(gic.getId()).setGraphical(gic);
                result.add(Command.createResponse(gic, elementType));
                break;
            case ASSOCIATION_CLASS:
                AssociationClassDto gac =
                        Json.fromJson(data,
                                AssociationClassDto.class);
                project.getDiagram().getEntity(gac.getId()).setGraphical(gac);
                result.add(Command.createResponse(gac, elementType));
                break;
            case ENUM:
                EnumDto gen = Json.fromJson(data,
                        EnumDto.class);
                Enum e = (Enum) project.getDiagram().getEntity(gen.getId());

                if (!e.getName().equals(gen.getName()))
                {
                    result.addAll(updateConstructorName(e, gen.getName()));
                }
                e.setGraphical(gen);
                result.add(Command.createResponse(gen, elementType));
                break;
            case INTERFACE:
                EntityDto gc = Json.fromJson(data,
                        EntityDto.class);
                project.getDiagram().getEntity(gc.getId()).setGraphical(gc);
                result.add(Command.createResponse(gc, elementType));
                break;
            case INNER_INTERFACE:
                InnerInterfaceDto gi = Json.fromJson(data,
                        InnerInterfaceDto.class);
                project.getDiagram().getEntity(gi.getId()).setGraphical(gi);
                result.add(Command.createResponse(gi, elementType));
                break;


            case BINARY_ASSOCIATION:
            case AGGREGATION:
            case COMPOSITION:
                BinaryAssociationDto gba = Json.fromJson(data,
                        BinaryAssociationDto.class);

                project.getDiagram().getAssociation(gba.getId()).setGraphical(gba, project.getDiagram());
                result.add(Command.createResponse(gba, elementType));
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
                ValueDto gv = Json.fromJson(data,
                        ValueDto.class);

                Enum eParent =
                        (Enum) project.getDiagram().getEntity(gv.getParentId());
                eParent.updateValue(gv.getOldValue(), gv.getValue());
                result.add(Command.createResponse(gv, elementType));
                break;

            case ATTRIBUTE:
                AttributeDto ga = Json.fromJson(data,
                        AttributeDto.class);

                project.getDiagram().getEntity(ga.getParentId()).getAttribute(ga.getId()).setGraphical(ga, project.getDiagram());
                result.add(Command.createResponse(ga, elementType));
                break;
            case PARAMETER:
                ParameterDto gp = Json.fromJson(data,
                        ParameterDto.class);
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
                result.add(Command.createResponse(gp, elementType));
                break;


            case CONSTRUCTOR:
                OperationDto go = Json.fromJson(data,
                        OperationDto.class);

                ConstructableEntity parent =
                        (ConstructableEntity) project.getDiagram().getEntity(go.getParentId());

                parent.getConstructorById(go.getId()).setGraphical(go,
                        project.getDiagram());
                result.add(Command.createResponse(go, elementType));
                break;
            case METHOD:
                MethodDto gm = Json.fromJson(data,
                        MethodDto.class);
                project.getDiagram().getEntity(gm.getParentId()).getMethodById(gm.getId()).setGraphical(gm, project.getDiagram());
                result.add(Command.createResponse(gm, elementType));
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
            OperationDto dto = new OperationDto();
            dto.setName(name);
            dto.setId(ctor.getId());
            dto.setParentId(ce.getId());
            result.add(Command.createResponse(dto, CONSTRUCTOR));
        }

        return result;
    }


}
