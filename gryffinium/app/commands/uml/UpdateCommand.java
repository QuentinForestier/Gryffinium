package commands.uml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import commands.CommandType;
import dto.AssociationClassDto;
import dto.entities.*;
import dto.entities.operations.MethodDto;
import dto.entities.operations.OperationDto;
import dto.entities.variables.AttributeDto;
import dto.entities.variables.ParameterDto;
import dto.entities.variables.ValueDto;
import dto.links.*;
import dto.links.components.RoleDto;
import models.Project;
import models.ProjectUser;
import play.libs.Json;
import uml.entities.*;
import uml.entities.Class;
import uml.entities.Enum;
import uml.entities.operations.Constructor;
import uml.entities.operations.Operation;
import uml.entities.variables.Parameter;
import uml.links.MultiAssociation;

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


                c.fromDto(ge);

                if (ge.getName() != null)
                {
                    c.getSubscribers().forEach(subscriber ->
                            result.add(subscriber.getUpdateNameCommand()));
                }
                result.add(Command.createResponse(ge, elementType,
                        CommandType.UPDATE_COMMAND));

                break;
            case INNER_CLASS:
                InnerClassDto gic = Json.fromJson(data,
                        InnerClassDto.class);
                project.getDiagram().getEntity(gic.getId()).fromDto(gic);
                result.add(Command.createResponse(gic, elementType,
                        CommandType.UPDATE_COMMAND));
                break;
            case ASSOCIATION_CLASS:
                AssociationClassDto gac =
                        Json.fromJson(data,
                                AssociationClassDto.class);
                project.getDiagram().getAssociationClass(gac.getId()).fromDto(gac, project.getDiagram());
                result.add(Command.createResponse(gac, elementType,
                        CommandType.UPDATE_COMMAND));
                break;
            case ENUM:
                EnumDto gen = Json.fromJson(data,
                        EnumDto.class);
                Enum e = (Enum) project.getDiagram().getEntity(gen.getId());

                if (!e.getName().equals(gen.getName()))
                {
                    result.addAll(updateConstructorName(e, gen.getName()));
                }
                if (gen.getName() != null)
                {
                    e.getSubscribers().forEach(subscriber ->
                            result.add(subscriber.getUpdateNameCommand()));
                }
                e.fromDto(gen);
                result.add(Command.createResponse(gen, elementType,
                        CommandType.UPDATE_COMMAND));
                break;
            case INTERFACE:
                EntityDto gc = Json.fromJson(data,
                        EntityDto.class);
                Interface i =
                        (Interface) project.getDiagram().getEntity(gc.getId());
                i.fromDto(gc);
                if (gc.getName() != null)
                {
                    i.getSubscribers().forEach(subscriber ->
                            result.add(subscriber.getUpdateNameCommand()));
                }
                result.add(Command.createResponse(gc, elementType,
                        CommandType.UPDATE_COMMAND));
                break;
            case INNER_INTERFACE:
                InnerInterfaceDto gi = Json.fromJson(data,
                        InnerInterfaceDto.class);
                project.getDiagram().getEntity(gi.getId()).fromDto(gi);
                result.add(Command.createResponse(gi, elementType,
                        CommandType.UPDATE_COMMAND));
                break;


            case BINARY_ASSOCIATION:
            case AGGREGATION:
            case COMPOSITION:
                BinaryAssociationDto gba = Json.fromJson(data,
                        BinaryAssociationDto.class);
                project.getDiagram().getAssociation(gba.getId()).fromDto(gba,
                        project.getDiagram());
                result.add(Command.createResponse(gba, elementType,
                        CommandType.UPDATE_COMMAND));
                break;
            case MULTI_ASSOCIATION:
                MultiAssociationDto gma = Json.fromJson(data,
                        MultiAssociationDto.class);
                project.getDiagram().getMultiAssociation(gma.getId()).fromDto(gma, project.getDiagram());
                result.add(Command.createResponse(gma, elementType,
                        CommandType.UPDATE_COMMAND));
                break;

            case UNARY_ASSOCIATION:
                UnaryAssociationDto gua = Json.fromJson(data,
                        UnaryAssociationDto.class);
                project.getDiagram().getMultiAssociation(gua.getSourceId()).getUnaryAssociation(gua.getId()).fromDto(gua, project.getDiagram());
                result.add(Command.createResponse(gua, elementType,
                        CommandType.UPDATE_COMMAND));
                break;
            case DEPENDENCY:
                DependencyDto gd = Json.fromJson(data,
                        DependencyDto.class);
                project.getDiagram().getDependency(gd.getId()).fromDto(gd,
                        project.getDiagram());
                result.add(Command.createResponse(gd, elementType,
                        CommandType.UPDATE_COMMAND));
                break;
            case GENERALIZATION:
                GeneralizationDto gdto = Json.fromJson(data,
                        GeneralizationDto.class);
                project.getDiagram().getRelationship(gdto.getId()).fromDto(gdto,
                        project.getDiagram());
                result.add(Command.createResponse(gdto, elementType,
                        CommandType.UPDATE_COMMAND));
                break;
            case REALIZATION:
                RealizationDto rdto = Json.fromJson(data,
                        RealizationDto.class);
                project.getDiagram().getRelationship(rdto.getId()).fromDto(rdto,
                        project.getDiagram());
                result.add(Command.createResponse(rdto, elementType,
                        CommandType.UPDATE_COMMAND));
                break;

            case INNER:
                InnerDto innerDto = Json.fromJson(data,
                        InnerDto.class);
                project.getDiagram().getInner(innerDto.getId()).fromDto(innerDto, project.getDiagram());
                result.add(Command.createResponse(innerDto, elementType,
                        CommandType.UPDATE_COMMAND));
                break;

            case VALUE:
                ValueDto gv = Json.fromJson(data,
                        ValueDto.class);

                Enum eParent =
                        (Enum) project.getDiagram().getEntity(gv.getParentId());
                eParent.updateValue(gv.getOldValue(), gv.getValue());
                result.add(Command.createResponse(gv, elementType,
                        CommandType.UPDATE_COMMAND));
                break;

            case ATTRIBUTE:
                AttributeDto ga = Json.fromJson(data,
                        AttributeDto.class);

                project.getDiagram().getEntity(ga.getParentId()).getAttribute(ga.getId()).fromDto(ga, project.getDiagram());
                result.add(Command.createResponse(ga, elementType,
                        CommandType.UPDATE_COMMAND));
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
                p.fromDto(gp, project.getDiagram());
                result.add(Command.createResponse(gp, elementType,
                        CommandType.UPDATE_COMMAND));
                break;


            case CONSTRUCTOR:
                OperationDto go = Json.fromJson(data,
                        OperationDto.class);

                ConstructableEntity parent =
                        (ConstructableEntity) project.getDiagram().getEntity(go.getParentId());

                parent.getConstructorById(go.getId()).fromDto(go,
                        project.getDiagram());
                result.add(Command.createResponse(go, elementType,
                        CommandType.UPDATE_COMMAND));
                break;
            case METHOD:
                MethodDto gm = Json.fromJson(data,
                        MethodDto.class);
                project.getDiagram().getEntity(gm.getParentId()).getMethodById(gm.getId()).fromDto(gm, project.getDiagram());
                result.add(Command.createResponse(gm, elementType,
                        CommandType.UPDATE_COMMAND));
                break;
            case ROLE:
                RoleDto gr = Json.fromJson(data,
                        RoleDto.class);
                try
                {
                    project.getDiagram().getAssociation(gr.getAssociationId()).getRole(gr.getId()).fromDto(gr, project.getDiagram());
                }
                catch (Exception error)
                {
                    for (MultiAssociation ma :
                            project.getDiagram().getMultiAssociations())
                    {
                        if (ma.getUnaryAssociation(gr.getAssociationId()) != null)
                        {
                            ma.getUnaryAssociation(gr.getAssociationId()).getRole(gr.getId()).fromDto(gr, project.getDiagram());
                        }
                    }
                }
                result.add(Command.createResponse(gr, elementType,
                        CommandType.UPDATE_COMMAND));
                break;
        }

        return result;
    }

    @Override
    public Boolean canExecute(ProjectUser user)
    {
        return user.getCanWrite();
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
            result.add(Command.createResponse(dto, CONSTRUCTOR,
                    CommandType.UPDATE_COMMAND));
        }

        return result;
    }


}
