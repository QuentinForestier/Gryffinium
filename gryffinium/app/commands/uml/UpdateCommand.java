package commands.uml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import graphical.GraphicalElementType;
import graphical.entities.*;
import graphical.links.GraphicalBinaryAssociation;
import graphical.links.GraphicalMultiAssociation;
import models.Project;
import play.libs.Json;
import uml.entities.*;
import uml.entities.Class;
import uml.entities.Enum;
import uml.links.BinaryAssociation;
import uml.links.MultiAssociation;

public class UpdateCommand implements Command
{
    JsonNode data;

    ObjectNode reference;

    GraphicalElementType elementType;

    public UpdateCommand(JsonNode data, GraphicalElementType elementType, ObjectNode reference)
    {
        this.data = data;
        this.elementType = elementType;
        this.reference = reference;
    }

    public void setData(JsonNode data)
    {
        this.data = data;
    }

    public JsonNode getData()
    {
        return data;
    }

    public ObjectNode getReference()
    {
        return reference;
    }

    public void setReference(ObjectNode reference)
    {
        this.reference = reference;
    }

    @Override
    public JsonNode execute(Project project)
    {
        ObjectNode result = null;

        Integer id = this.reference.get("id").asInt();
        // TODO : Catch exception
        System.out.println(elementType);
        switch (elementType)
        {
            case CLASS:
                GraphicalClass ge = Json.fromJson(data,
                        GraphicalClass.class);
                Class c = (Class) project.getDiagram().getEntity(id);
                c.setGraphical(ge);
                result = (ObjectNode) Json.toJson(c);
                break;
            case INNER_CLASS:
                GraphicalInnerClass gic = Json.fromJson(data,
                        GraphicalInnerClass.class);
                InnerClass ic =
                        (InnerClass) project.getDiagram().getEntity(id);
                ic.setGraphical(gic);
                result = (ObjectNode) Json.toJson(ic);
                break;
            case ASSOCIATION_CLASS:
                GraphicalAssociationClass gac =
                        Json.fromJson(data,
                                GraphicalAssociationClass.class);
                AssociationClass ac =
                        (AssociationClass) project.getDiagram().getEntity(id);
                ac.setGraphical(gac);
                result = (ObjectNode) Json.toJson(ac);
                break;
            case ENUM:
                GraphicalEnum gen = Json.fromJson(data,
                        GraphicalEnum.class);
                Enum e = (Enum) project.getDiagram().getEntity(id);
                e.setGraphical(gen);
                result = (ObjectNode) Json.toJson(e);
                break;
            case INTERFACE:
                GraphicalEntity gc = Json.fromJson(data,
                        GraphicalEntity.class);
                Interface i =
                        (Interface) project.getDiagram().getEntity(id);
                i.setGraphical(gc);
                result = (ObjectNode) Json.toJson(i);
                break;
            case INNER_INTERFACE:
                GraphicalInnerInterface gi = Json.fromJson(data,
                        GraphicalInnerInterface.class);
                InnerInterface ii =
                        (InnerInterface) project.getDiagram().getEntity(id);
                ii.setGraphical(gi);
                result = (ObjectNode) Json.toJson(ii);
                break;


            case BINARY_ASSOCIATION:
            case AGGREGATION:
            case COMPOSITION:
                GraphicalBinaryAssociation gba = Json.fromJson(data,
                        GraphicalBinaryAssociation.class);
                BinaryAssociation ba =
                        (BinaryAssociation) project.getDiagram().getAssociation(id);
                ba.setGraphical(gba);
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


            case ATTRIBUTE:
                break;
            case PARAMETER:
                break;


            case CONSTRUCTOR:
                break;
            case METHOD:
                break;
        }

        result.put("elementType", elementType.toString());


        return result;
    }
}
