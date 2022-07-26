package commands.uml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import commands.CommandType;
import dto.AssociationClassDto;
import dto.ElementTypeDto;
import dto.entities.*;
import dto.entities.operations.MethodDto;
import dto.entities.operations.OperationDto;
import dto.entities.variables.AttributeDto;
import dto.entities.variables.ParameterDto;
import dto.entities.variables.ValueDto;
import dto.links.*;
import models.Project;
import models.ProjectUser;
import play.libs.Json;
import uml.AssociationClass;
import uml.entities.*;
import uml.entities.Enum;
import uml.entities.operations.Operation;
import uml.links.Inner;
import uml.links.MultiAssociation;
import uml.links.UnaryAssociation;

public class RemoveCommand implements Command
{
    JsonNode data;
    dto.ElementTypeDto elementType;

    public RemoveCommand(JsonNode data, dto.ElementTypeDto elementType)
    {
        this.data = data;
        this.elementType = elementType;
    }

    @Override
    public ArrayNode execute(Project project)
    {
        ArrayNode result = Json.newArray();

        dto.ElementDto ge = null;
        switch (elementType)
        {
            case CLASS:
                ge = Json.fromJson(data,
                        ClassDto.class);
                project.getDiagram().removeEntity(project.getDiagram().getEntity(ge.getId()));
                result.add(Command.createResponse(ge, elementType,
                        CommandType.REMOVE_COMMAND));
                break;
            case INNER_CLASS:
                ge = Json.fromJson(data,
                        InnerClassDto.class);
                project.getDiagram().removeEntity(project.getDiagram().getEntity(ge.getId()));
                result.add(Command.createResponse(ge, elementType,
                        CommandType.REMOVE_COMMAND));
                break;
            case ASSOCIATION_CLASS:
                AssociationClassDto acdto = Json.fromJson(data,
                        AssociationClassDto.class);
                AssociationClass ac = project.getDiagram().getAssociationClass(acdto.getId());
                project.getDiagram().removeAssociationClass(ac);
                result.add(Command.createResponse(acdto, elementType,
                        CommandType.REMOVE_COMMAND));
                result.add(Command.createResponse(ac.getAssociatedClass().toDto(), ElementTypeDto.CLASS, CommandType.REMOVE_COMMAND));
                result.add(Command.createResponse(ac.getAssociation().toDto(), ElementTypeDto.BINARY_ASSOCIATION, CommandType.REMOVE_COMMAND));
                break;
            case ENUM:
                ge = Json.fromJson(data,
                        EnumDto.class);
                project.getDiagram().removeEntity(project.getDiagram().getEntity(ge.getId()));
                result.add(Command.createResponse(ge, elementType,
                        CommandType.REMOVE_COMMAND));
                break;
            case INTERFACE:
                ge = Json.fromJson(data,
                        EntityDto.class);
                project.getDiagram().removeEntity(project.getDiagram().getEntity(ge.getId()));
                result.add(Command.createResponse(ge, elementType,
                        CommandType.REMOVE_COMMAND));
                break;
            case INNER_INTERFACE:
                ge = Json.fromJson(data,
                        InnerInterfaceDto.class);
                project.getDiagram().removeEntity(project.getDiagram().getEntity(ge.getId()));
                result.add(Command.createResponse(ge, elementType,
                        CommandType.REMOVE_COMMAND));
                break;


            case BINARY_ASSOCIATION:
            case AGGREGATION:
            case COMPOSITION:
                BinaryAssociationDto gba = Json.fromJson(data,
                        BinaryAssociationDto.class);
                project.getDiagram().removeAssociation(project.getDiagram().getAssociation(gba.getId()));
                result.add(Command.createResponse(gba, elementType,
                        CommandType.REMOVE_COMMAND));
                break;
            case MULTI_ASSOCIATION:
                MultiAssociationDto gma = Json.fromJson(data,
                        MultiAssociationDto.class);
                project.getDiagram().removeMultiAssociation(project.getDiagram().getMultiAssociation(gma.getId()));
                result.add(Command.createResponse(gma, elementType,
                        CommandType.REMOVE_COMMAND));
                break;
            case UNARY_ASSOCIATION:
                UnaryAssociationDto gua = Json.fromJson(data,
                        UnaryAssociationDto.class);
                MultiAssociation multiAssociation =
                        project.getDiagram().getMultiAssociation(gua.getSourceId());
                UnaryAssociation unaryAssociation =
                        multiAssociation.getUnaryAssociation(gua.getId());

                multiAssociation.removeUnaryAssociation(unaryAssociation);
                result.add(Command.createResponse(gua, elementType,
                        CommandType.REMOVE_COMMAND));

                if (multiAssociation.getUnaryAssociations().size() < 3)
                {
                    for (UnaryAssociation ua :
                            multiAssociation.getUnaryAssociations())
                    {
                        result.add(Command.createResponse(ua.toDto(),
                                ElementTypeDto.UNARY_ASSOCIATION,
                                CommandType.REMOVE_COMMAND));
                    }
                    multiAssociation.getUnaryAssociations().clear();
                    result.add(Command.createResponse(multiAssociation.toDto(),
                            ElementTypeDto.MULTI_ASSOCIATION,
                            CommandType.REMOVE_COMMAND));
                }


                break;
            case DEPENDENCY:
                DependencyDto gd = Json.fromJson(data,
                        DependencyDto.class);
                project.getDiagram().removeDependency(project.getDiagram().getDependency(gd.getId()));
                result.add(Command.createResponse(gd, elementType,
                        CommandType.REMOVE_COMMAND));
                break;
            case GENERALIZATION:
                GeneralizationDto gdto = Json.fromJson(data,
                        GeneralizationDto.class);
                project.getDiagram().removeRelationship(project.getDiagram().getRelationship(gdto.getId()));
                result.add(Command.createResponse(gdto, elementType,
                        CommandType.REMOVE_COMMAND));
                break;
            case REALIZATION:
                RealizationDto rdto = Json.fromJson(data,
                        RealizationDto.class);
                project.getDiagram().removeRelationship(project.getDiagram().getRelationship(rdto.getId()));
                result.add(Command.createResponse(rdto, elementType,
                        CommandType.REMOVE_COMMAND));
                break;
            case INNER:
                InnerDto innerDto = Json.fromJson(data,
                        InnerDto.class);
                Inner inner = project.getDiagram().getInner(innerDto.getId());
                project.getDiagram().removeEntity((Entity) inner.getInner());
                project.getDiagram().addEntity(inner.convertToEntity(inner.getInner()));
                project.getDiagram().removeInner(project.getDiagram().getInner(innerDto.getId()));
                result.add(Command.createResponse(innerDto, elementType,
                        CommandType.REMOVE_COMMAND));
                break;

            case VALUE:
                ValueDto gv = Json.fromJson(data,
                        ValueDto.class);

                Enum e =
                        (Enum) project.getDiagram().getEntity(gv.getParentId());
                e.removeValue(gv.getValue());
                result.add(Command.createResponse(gv, elementType,
                        CommandType.REMOVE_COMMAND));
                break;

            case ATTRIBUTE:
                AttributeDto ga = Json.fromJson(data,
                        AttributeDto.class);
                Entity parent =
                        project.getDiagram().getEntity(ga.getParentId());
                parent.removeAttribute(parent.getAttribute(ga.getId()));
                result.add(Command.createResponse(ga, elementType,
                        CommandType.REMOVE_COMMAND));
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
                op.removeParam(gp.getId());
                result.add(Command.createResponse(gp, elementType,
                        CommandType.REMOVE_COMMAND));
                break;


            case CONSTRUCTOR:
                OperationDto go = Json.fromJson(data,
                        OperationDto.class);
                ConstructableEntity ce =
                        (ConstructableEntity) project.getDiagram().getEntity(go.getParentId());
                ce.removeConstructor(ce.getConstructorById(go.getId()));
                result.add(Command.createResponse(go, elementType,
                        CommandType.REMOVE_COMMAND));
                break;
            case METHOD:
                MethodDto gm = Json.fromJson(data,
                        MethodDto.class);
                Entity parent2 =
                        project.getDiagram().getEntity(gm.getParentId());
                parent2.removeMethod(parent2.getMethodById(gm.getId()));
                result.add(Command.createResponse(gm, elementType,
                        CommandType.REMOVE_COMMAND));
                break;
        }

        return result;
    }

    @Override
    public Boolean canExecute(ProjectUser user)
    {
        return user.getCanWrite();
    }

}
