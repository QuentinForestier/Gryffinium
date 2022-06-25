package commands.uml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import graphical.GraphicalElementType;
import graphical.entities.*;
import graphical.entities.operations.GraphicalMethod;
import graphical.entities.operations.GraphicalOperation;
import graphical.links.*;
import graphical.entities.variables.*;
import models.Project;
import play.libs.Json;
import uml.Visibility;
import uml.entities.*;
import uml.entities.operations.Constructor;
import uml.entities.operations.Method;
import uml.entities.variables.*;
import uml.entities.Class;
import uml.entities.Enum;
import uml.links.BinaryAssociation;

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
    public JsonNode execute(Project project)
    {
        ObjectNode result = null;

        switch (elementType)
        {
            case CLASS:
                GraphicalClass ge = Json.fromJson(data,
                        GraphicalClass.class);
                Class c = new Class(ge);
                project.getDiagram().addEntity(c);
                result = (ObjectNode) Json.toJson(c);
                break;
            case INNER_CLASS:
                GraphicalInnerClass gic = Json.fromJson(data,
                        GraphicalInnerClass.class);
                InnerClass ic = new InnerClass(gic, project.getDiagram());
                project.getDiagram().addEntity(ic);
                result = (ObjectNode) Json.toJson(ic);
                break;
            case ASSOCIATION_CLASS:
                GraphicalAssociationClass gac =
                        Json.fromJson(data,
                                GraphicalAssociationClass.class);
                AssociationClass ac = new AssociationClass(gac,
                        project.getDiagram());
                project.getDiagram().addEntity(ac);
                result = (ObjectNode) Json.toJson(ac);
                break;
            case ENUM:
                GraphicalEnum gen = Json.fromJson(data,
                        GraphicalEnum.class);
                Enum e = new Enum(gen);
                project.getDiagram().addEntity(e);
                result = (ObjectNode) Json.toJson(e);
                break;
            case INTERFACE:
                GraphicalEntity gc = Json.fromJson(data,
                        GraphicalEntity.class);
                Interface i = new Interface(gc);
                project.getDiagram().addEntity(i);
                result = (ObjectNode) Json.toJson(i);
                break;
            case INNER_INTERFACE:
                GraphicalInnerInterface gi = Json.fromJson(data,
                        GraphicalInnerInterface.class);
                InnerInterface ii = new InnerInterface(gi,
                        project.getDiagram());
                project.getDiagram().addEntity(ii);
                result = (ObjectNode) Json.toJson(ii);
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

                Enum eParent =
                        (Enum) project.getDiagram().getEntity(gv.getParentId());
                gv.setValue("value" + eParent.getValues().size());
                eParent.addValue(gv.getValue());

                result = (ObjectNode) Json.toJson(gv);
                break;

            case ATTRIBUTE:
                GraphicalAttribute ga = Json.fromJson(data,
                        GraphicalAttribute.class);
                Attribute a = new Attribute(ga, project.getDiagram());
                project.getDiagram().getEntity(ga.getParentId()).addAttribute(a);
                ga.setId(a.getId());
                result = (ObjectNode) Json.toJson(ga);
                break;
            case PARAMETER:
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
                result = (ObjectNode) Json.toJson(go);
                break;
            case METHOD:
                GraphicalMethod gm = Json.fromJson(data,
                        GraphicalMethod.class);
                Method m = new Method(gm, project.getDiagram());
                project.getDiagram().getEntity(gm.getParentId()).addMethod(m);
                gm.setId(m.getId());
                result = (ObjectNode) Json.toJson(gm);
                break;
        }

        result.put("elementType", elementType.toString());

        return result;
    }


}
