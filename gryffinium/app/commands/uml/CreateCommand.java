package commands.uml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import graphical.GraphicalElementType;
import graphical.entities.*;
import graphical.entities.operations.GraphicalMethod;
import graphical.entities.operations.GraphicalOperation;
import graphical.links.*;
import graphical.entities.variables.*;
import models.Project;
import play.libs.Json;
import uml.entities.*;
import uml.entities.operations.Constructor;
import uml.entities.operations.Method;
import uml.entities.operations.Operation;
import uml.entities.variables.*;
import uml.entities.Class;
import uml.entities.Enum;
import uml.links.BinaryAssociation;
import uml.links.Generalization;
import uml.links.Realization;

public class CreateCommand implements Command
{
    JsonNode data;

    GraphicalElementType elementType;

    public CreateCommand(JsonNode data, GraphicalElementType elementType)
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
                GraphicalClass ge = Json.fromJson(data,
                        GraphicalClass.class);
                Class c = new Class(ge);
                project.getDiagram().addEntity(c);
                result.add(createResponse(c, elementType));
                break;
            case INNER_CLASS:
                GraphicalInnerClass gic = Json.fromJson(data,
                        GraphicalInnerClass.class);
                InnerClass ic = new InnerClass(gic, project.getDiagram());
                project.getDiagram().addEntity(ic);
                result.add(createResponse(ic, elementType));
                break;
            case ASSOCIATION_CLASS:
                GraphicalAssociationClass gac =
                        Json.fromJson(data,
                                GraphicalAssociationClass.class);
                AssociationClass ac = new AssociationClass(gac,
                        project.getDiagram());
                project.getDiagram().addEntity(ac);
                result.add(createResponse(ac, elementType));
                break;
            case ENUM:
                GraphicalEnum gen = Json.fromJson(data,
                        GraphicalEnum.class);
                Enum e = new Enum(gen);
                project.getDiagram().addEntity(e);
                result.add(createResponse(e, elementType));
                break;
            case INTERFACE:
                GraphicalEntity gc = Json.fromJson(data,
                        GraphicalEntity.class);
                Interface i = new Interface(gc);
                project.getDiagram().addEntity(i);
                result.add(createResponse(i, elementType));
                break;
            case INNER_INTERFACE:
                GraphicalInnerInterface gi = Json.fromJson(data,
                        GraphicalInnerInterface.class);
                InnerInterface ii = new InnerInterface(gi,
                        project.getDiagram());
                project.getDiagram().addEntity(ii);
                result.add(createResponse(ii, elementType));
                break;


            case BINARY_ASSOCIATION:
            case AGGREGATION:
            case COMPOSITION:
                GraphicalBinaryAssociation gba = Json.fromJson(data,
                        GraphicalBinaryAssociation.class);
                BinaryAssociation ba = new BinaryAssociation(gba,
                        project.getDiagram());
                project.getDiagram().addAssociation(ba);
                gba.setId(ba.getId());
                gba.setMultiplicitySource(ba.getSource().getMultiplicity().toString());
                gba.setMultiplicityTarget(ba.getTarget().getMultiplicity().toString());

                result.add(createResponse(gba, elementType));
                break;
            case MUTLI_ASSOCIATION:
                break;
            case DEPENDENCY:
                break;
            case GENERALIZATION:
                GraphicalLink gl = Json.fromJson(data, GraphicalLink.class);
                Generalization g = new Generalization(gl, project.getDiagram());

                gl.setId(g.getId());
                project.getDiagram().addRelationship(g);

                result.add(createResponse(gl, elementType));
                break;
            case REALIZATION:
                GraphicalLink glr = Json.fromJson(data, GraphicalLink.class);
                Realization r = new Realization(glr, project.getDiagram());

                glr.setId(r.getId());
                project.getDiagram().addRelationship(r);

                result.add(createResponse(glr, elementType));
                break;
            case INNER:
                break;

            case VALUE:
                GraphicalValue gv = Json.fromJson(data,
                        GraphicalValue.class);

                Enum eParent =
                        (Enum) project.getDiagram().getEntity(gv.getParentId());
                gv.setValue("value" + eParent.getValues().size());
                eParent.addValue(gv.getValue());

                result.add(createResponse(gv, elementType));
                break;

            case ATTRIBUTE:
                GraphicalAttribute ga = Json.fromJson(data,
                        GraphicalAttribute.class);
                Attribute a = new Attribute(ga, project.getDiagram());
                project.getDiagram().getEntity(ga.getParentId()).addAttribute(a);
                ga.setId(a.getId());
                result.add(createResponse(ga, elementType));
                break;
            case PARAMETER:
                GraphicalParameter gp = Json.fromJson(data,
                        GraphicalParameter.class);
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
                result.add(createResponse(gp, elementType));
                break;


            case CONSTRUCTOR:
                GraphicalOperation go = Json.fromJson(data,
                        GraphicalOperation.class);
                ConstructableEntity parent =
                        (ConstructableEntity) project.getDiagram().getEntity(go.getParentId());
                go.setName(parent.getName());
                Constructor ctor = new Constructor(go, project.getDiagram());
                parent.addConstructor(ctor);
                go.setId(ctor.getId());
                result.add(createResponse(go, elementType));
                break;
            case METHOD:
                GraphicalMethod gm = Json.fromJson(data,
                        GraphicalMethod.class);
                Method m = new Method(gm, project.getDiagram());
                project.getDiagram().getEntity(gm.getParentId()).addMethod(m);
                gm.setId(m.getId());
                result.add(createResponse(gm, elementType));
                break;
        }
        return result;
    }


}
