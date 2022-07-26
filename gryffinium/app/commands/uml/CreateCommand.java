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
import dto.links.*;
import dto.entities.variables.*;
import models.Project;
import models.ProjectUser;
import play.libs.Json;
import uml.AssociationClass;
import uml.entities.*;
import uml.entities.operations.Constructor;
import uml.entities.operations.Method;
import uml.entities.operations.Operation;
import uml.entities.variables.*;
import uml.entities.Class;
import uml.entities.Enum;
import uml.links.*;

public class CreateCommand implements Command
{
    JsonNode data;

    dto.ElementTypeDto elementType;

    public CreateCommand(JsonNode data, dto.ElementTypeDto elementType)
    {
        this.data = data;
        this.elementType = elementType;
    }

    public void setElement(JsonNode data)
    {
        this.data = data;
    }

    public JsonNode getElement()
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
                Class c = new Class(Json.fromJson(data, ClassDto.class));
                project.getDiagram().addEntity(c);
                result.add(Command.createResponse(c.toDto(), elementType, CommandType.CREATE_COMMAND));
                break;
            case INNER_CLASS:
                InnerClass ic = new InnerClass(
                        Json.fromJson(data, InnerClassDto.class),
                        project.getDiagram());

                project.getDiagram().addEntity(ic);
                result.add(Command.createResponse(ic.toDto(), elementType, CommandType.CREATE_COMMAND));
                break;
            case ASSOCIATION_CLASS:
                AssociationClass ac = new AssociationClass(
                        Json.fromJson(data, AssociationClassDto.class),
                        project.getDiagram());

                project.getDiagram().addEntity(ac.getAssociatedClass());
                project.getDiagram().addAssociation(ac.getAssociation());
                project.getDiagram().addAssociationClass(ac);
                result.addAll(ac.getAssociation().getCreationCommands());
                result.addAll(ac.getAssociatedClass().getCreationCommands());
                result.add(Command.createResponse(ac.toDto(), elementType, CommandType.CREATE_COMMAND));
                break;
            case ENUM:
                Enum e = new Enum(Json.fromJson(data, EnumDto.class));
                project.getDiagram().addEntity(e);
                result.add(Command.createResponse(e.toDto(), elementType, CommandType.CREATE_COMMAND));
                break;
            case INTERFACE:
                Interface i = new Interface(Json.fromJson(data,
                        EntityDto.class));
                project.getDiagram().addEntity(i);
                result.add(Command.createResponse(i.toDto(), elementType, CommandType.CREATE_COMMAND));
                break;
            case INNER_INTERFACE:
                InnerInterface ii = new InnerInterface(
                        Json.fromJson(data, InnerInterfaceDto.class),
                        project.getDiagram());
                project.getDiagram().addEntity(ii);
                result.add(Command.createResponse(ii.toDto(), elementType, CommandType.CREATE_COMMAND));
                break;
            case UNARY_ASSOCIATION:
                UnaryAssociation ua = new UnaryAssociation(
                        Json.fromJson(data, UnaryAssociationDto.class),
                        project.getDiagram());
                project.getDiagram().addAssociation(ua);
                ua.getParent().addUnaryAssociation(ua);
                result.add(Command.createResponse(ua.toDto(), elementType, CommandType.CREATE_COMMAND));
                break;

            case BINARY_ASSOCIATION:
                BinaryAssociation ba = new BinaryAssociation(
                        Json.fromJson(data, BinaryAssociationDto.class),
                        project.getDiagram());

                project.getDiagram().addAssociation(ba);
                result.add(Command.createResponse(ba.toDto(), elementType, CommandType.CREATE_COMMAND));
                break;
            case AGGREGATION:
                Aggregation ag = new Aggregation(
                        Json.fromJson(data, BinaryAssociationDto.class),
                        project.getDiagram());

                project.getDiagram().addAssociation(ag);
                result.add(Command.createResponse(ag.toDto(), elementType, CommandType.CREATE_COMMAND));
                break;
            case COMPOSITION:
                Composition co = new Composition(
                        Json.fromJson(data, BinaryAssociationDto.class),
                        project.getDiagram());
                project.getDiagram().addAssociation(co);

                result.add(Command.createResponse(co.toDto(), elementType, CommandType.CREATE_COMMAND));
                break;
            case MULTI_ASSOCIATION:
                MultiAssociation ma = new MultiAssociation(
                        Json.fromJson(data, MultiAssociationDto.class),
                        project.getDiagram());
                project.getDiagram().addMultiAssociation(ma);
                result.add(Command.createResponse(ma.toDto(), elementType, CommandType.CREATE_COMMAND));
                for(UnaryAssociation unary : ma.getUnaryAssociations())
                {
                    result.addAll(unary.getCreationCommands());
                }
                break;
            case DEPENDENCY:
                Dependency d = new Dependency(
                        Json.fromJson(data, DependencyDto.class),
                        project.getDiagram());

                project.getDiagram().addDependency(d);
                result.add(Command.createResponse(d.toDto(), elementType, CommandType.CREATE_COMMAND));
                break;
            case GENERALIZATION:
                Generalization g = new Generalization(
                        Json.fromJson(data, GeneralizationDto.class),
                        project.getDiagram());

                project.getDiagram().addRelationship(g);
                result.add(Command.createResponse(g.toDto(), elementType, CommandType.CREATE_COMMAND));
                break;
            case REALIZATION:
                Realization r = new Realization(
                        Json.fromJson(data, RealizationDto.class),
                        project.getDiagram());

                project.getDiagram().addRelationship(r);
                result.add(Command.createResponse(r.toDto(), elementType, CommandType.CREATE_COMMAND));
                break;
            case INNER:
                Inner inner = new Inner(
                        Json.fromJson(data, InnerDto.class),
                        project.getDiagram());

                project.getDiagram().addInner(inner);
                result.add(Command.createResponse(inner.toDto(), elementType, CommandType.CREATE_COMMAND));
                break;

            case VALUE:
                ValueDto gv = Json.fromJson(data,
                        ValueDto.class);

                Enum eParent =
                        (Enum) project.getDiagram().getEntity(gv.getParentId());
                // TODO : check name uniqueness
                gv.setValue("value" + eParent.getValues().size());
                eParent.addValue(gv.getValue());

                result.add(Command.createResponse(gv, elementType, CommandType.CREATE_COMMAND));
                break;

            case ATTRIBUTE:
                AttributeDto ga = Json.fromJson(data,
                        AttributeDto.class);
                Attribute a = new Attribute(ga, project.getDiagram());
                Entity attrParent =
                        project.getDiagram().getEntity(ga.getParentId());
                attrParent.addAttribute(a);

                result.add(Command.createResponse(a.toDto(attrParent),
                        elementType, CommandType.CREATE_COMMAND));
                break;
            case PARAMETER:
                ParameterDto gp = Json.fromJson(data,
                        ParameterDto.class);
                Parameter p = new Parameter(gp, project.getDiagram());
                Entity paramParent =
                        project.getDiagram().getEntity(gp.getParentId());
                Operation op = paramParent.getOperationById(gp.getMethodId());

                op.addParam(p);
                result.add(Command.createResponse(p.toDto(paramParent, op),
                        elementType, CommandType.CREATE_COMMAND));
                break;


            case CONSTRUCTOR:
                OperationDto go = Json.fromJson(data,
                        OperationDto.class);
                ConstructableEntity ctorParent =
                        (ConstructableEntity) project.getDiagram().getEntity(go.getParentId());

                Constructor ctor = new Constructor(go, project.getDiagram());
                ctorParent.addConstructor(ctor);

                result.add(Command.createResponse(ctor.toDto(ctorParent),
                        elementType, CommandType.CREATE_COMMAND));
                break;
            case METHOD:
                MethodDto gm = Json.fromJson(data,
                        MethodDto.class);
                Method m = new Method(gm, project.getDiagram());

                Entity methParent =
                        project.getDiagram().getEntity(gm.getParentId());
                methParent.addMethod(m);
                result.add(Command.createResponse(m.toDto(methParent),
                        elementType, CommandType.CREATE_COMMAND));
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
