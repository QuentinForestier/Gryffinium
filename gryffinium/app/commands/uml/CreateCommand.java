package commands.uml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import graphical.GraphicalElementType;
import graphical.entities.*;
import graphical.links.GraphicalBinaryAssociation;
import models.Project;
import play.libs.Json;
import uml.Visibility;
import uml.entities.*;
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
        // TODO : Catch exception
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
