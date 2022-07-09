package commands.uml;

import akka.dispatch.sysmsg.Create;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import dto.entities.*;
import dto.entities.operations.MethodDto;
import dto.entities.operations.OperationDto;
import dto.links.*;
import dto.entities.variables.*;
import models.Project;
import play.libs.Json;
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
                ClassDto ge = Json.fromJson(data,
                        ClassDto.class);
                Class c = new Class(ge);
                project.getDiagram().addEntity(c);
                result.add(Command.createResponse(c, elementType));
                break;
            case INNER_CLASS:
                InnerClassDto gic = Json.fromJson(data,
                        InnerClassDto.class);
                InnerClass ic = new InnerClass(gic, project.getDiagram());
                project.getDiagram().addEntity(ic);
                result.add(Command.createResponse(ic, elementType));
                break;
            case ASSOCIATION_CLASS:
                AssociationClassDto gac =
                        Json.fromJson(data,
                                AssociationClassDto.class);
                AssociationClass ac = new AssociationClass(gac,
                        project.getDiagram());
                project.getDiagram().addEntity(ac);
                result.add(Command.createResponse(ac, elementType));
                break;
            case ENUM:
                EnumDto gen = Json.fromJson(data,
                        EnumDto.class);
                Enum e = new Enum(gen);
                project.getDiagram().addEntity(e);
                result.add(Command.createResponse(e, elementType));
                break;
            case INTERFACE:
                EntityDto gc = Json.fromJson(data,
                        EntityDto.class);
                Interface i = new Interface(gc);
                project.getDiagram().addEntity(i);
                result.add(Command.createResponse(i, elementType));
                break;
            case INNER_INTERFACE:
                InnerInterfaceDto gi = Json.fromJson(data,
                        InnerInterfaceDto.class);
                InnerInterface ii = new InnerInterface(gi,
                        project.getDiagram());
                project.getDiagram().addEntity(ii);
                result.add(Command.createResponse(ii, elementType));
                break;


            case BINARY_ASSOCIATION:
                BinaryAssociationDto badto = Json.fromJson(data,
                        BinaryAssociationDto.class);
                BinaryAssociation ba = new BinaryAssociation(badto,
                        project.getDiagram());
                project.getDiagram().addAssociation(ba);
                badto.setId(ba.getId());
                badto.setMultiplicitySource(ba.getSource().getMultiplicity().toString());
                badto.setMultiplicityTarget(ba.getTarget().getMultiplicity().toString());

                result.add(Command.createResponse(badto, elementType));
                break;
            case AGGREGATION:
                BinaryAssociationDto agdto = Json.fromJson(data,
                        BinaryAssociationDto.class);
                Aggregation ag = new Aggregation(agdto,
                        project.getDiagram());
                project.getDiagram().addAssociation(ag);
                agdto.setId(ag.getId());
                agdto.setMultiplicitySource(ag.getSource().getMultiplicity().toString());
                agdto.setMultiplicityTarget(ag.getTarget().getMultiplicity().toString());

                result.add(Command.createResponse(agdto, elementType));
                break;
            case COMPOSITION:
                BinaryAssociationDto cdto = Json.fromJson(data,
                        BinaryAssociationDto.class);
                Composition co = new Composition(cdto,
                        project.getDiagram());
                project.getDiagram().addAssociation(co);
                cdto.setId(co.getId());
                cdto.setMultiplicitySource(co.getSource().getMultiplicity().toString());
                cdto.setMultiplicityTarget(co.getTarget().getMultiplicity().toString());

                result.add(Command.createResponse(cdto, elementType));
                break;
            case MUTLI_ASSOCIATION:
                break;
            case DEPENDENCY:
                break;
            case GENERALIZATION:
                LinkDto gl = Json.fromJson(data, LinkDto.class);
                Generalization g = new Generalization(gl, project.getDiagram());

                gl.setId(g.getId());
                project.getDiagram().addRelationship(g);

                result.add(Command.createResponse(gl, elementType));
                break;
            case REALIZATION:
                LinkDto glr = Json.fromJson(data, LinkDto.class);
                Realization r = new Realization(glr, project.getDiagram());

                glr.setId(r.getId());
                project.getDiagram().addRelationship(r);

                result.add(Command.createResponse(glr, elementType));
                break;
            case INNER:
                break;

            case VALUE:
                ValueDto gv = Json.fromJson(data,
                        ValueDto.class);

                Enum eParent =
                        (Enum) project.getDiagram().getEntity(gv.getParentId());
                gv.setValue("value" + eParent.getValues().size());
                eParent.addValue(gv.getValue());

                result.add(Command.createResponse(gv, elementType));
                break;

            case ATTRIBUTE:
                AttributeDto ga = Json.fromJson(data,
                        AttributeDto.class);
                Attribute a = new Attribute(ga, project.getDiagram());
                project.getDiagram().getEntity(ga.getParentId()).addAttribute(a);
                ga.setId(a.getId());
                result.add(Command.createResponse(ga, elementType));
                break;
            case PARAMETER:
                ParameterDto gp = Json.fromJson(data,
                        ParameterDto.class);
                Parameter p = new Parameter(gp, project.getDiagram());
                Entity entity =
                        project.getDiagram().getEntity(gp.getParentId());
                Operation op = entity.getMethodById(gp.getMethodId());
                if (op == null)
                {
                    ConstructableEntity ce = (ConstructableEntity) entity;
                    op = ce.getConstructorById(gp.getMethodId());
                }
                op.addParam(p);
                gp.setId(p.getId());
                result.add(Command.createResponse(gp, elementType));
                break;


            case CONSTRUCTOR:
                OperationDto go = Json.fromJson(data,
                        OperationDto.class);
                ConstructableEntity parent =
                        (ConstructableEntity) project.getDiagram().getEntity(go.getParentId());
                go.setName(parent.getName());
                Constructor ctor = new Constructor(go, project.getDiagram());
                parent.addConstructor(ctor);
                go.setId(ctor.getId());
                result.add(Command.createResponse(go, elementType));
                break;
            case METHOD:
                MethodDto gm = Json.fromJson(data,
                        MethodDto.class);
                Method m = new Method(gm, project.getDiagram());
                project.getDiagram().getEntity(gm.getParentId()).addMethod(m);
                gm.setId(m.getId());
                result.add(Command.createResponse(gm, elementType));
                break;
        }
        return result;
    }


}
