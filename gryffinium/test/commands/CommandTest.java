package commands;

import commands.uml.CreateCommand;
import dto.ElementTypeDto;
import dto.entities.ClassDto;
import dto.entities.EntityDto;
import dto.entities.EnumDto;
import dto.entities.operations.MethodDto;
import dto.links.BinaryAssociationDto;
import models.Project;
import org.junit.Before;
import play.libs.Json;
import uml.ClassDiagram;
import uml.Visibility;

public abstract class CommandTest
{
    Project p = new Project("test");

    @Before
    public void setup()
    {
        p.setDiagram(new ClassDiagram());
    }

    protected String createClass()
    {
        ClassDto dto = new ClassDto();
        dto.setAbstract(true);
        dto.setX(100);
        dto.setY(100);
        dto.setWidth(100);
        dto.setHeight(100);

        CreateCommand c = new CreateCommand(Json.toJson(dto),
                ElementTypeDto.CLASS);
        return c.execute(p).get(0).get("id").asText();
    }

    protected String createInterface()
    {
        EntityDto dto = new EntityDto();
        dto.setX(100);
        dto.setY(100);
        dto.setWidth(100);
        dto.setHeight(100);

        CreateCommand c = new CreateCommand(Json.toJson(dto),
                ElementTypeDto.INTERFACE);
        return c.execute(p).get(0).get("id").asText();
    }

    protected String createEnum()
    {
        EnumDto dto = new EnumDto();
        dto.setX(100);
        dto.setY(100);
        dto.setWidth(100);
        dto.setHeight(100);

        CreateCommand c = new CreateCommand(Json.toJson(dto),
                ElementTypeDto.ENUM);
        return c.execute(p).get(0).get("id").asText();
    }

    protected String createMethod(String parentId)
    {
        MethodDto dto = new MethodDto();
        dto.setParentId(parentId);
        dto.setStatic(false);
        dto.setVisibility(Visibility.PUBLIC.getName());
        dto.setType("String");
        dto.setAbstract(false);

        CreateCommand c = new CreateCommand(Json.toJson(dto),
                ElementTypeDto.METHOD);
        return c.execute(p).get(0).get("id").asText();

    }

    protected String createBinaryAssociation(){
        String source = createClass();
        String target = createClass();

        BinaryAssociationDto dto = new BinaryAssociationDto();
        dto.setSourceId(source);
        dto.setTargetId(target);
        dto.setDirected(true);

        CreateCommand c = new CreateCommand(Json.toJson(dto),
                ElementTypeDto.BINARY_ASSOCIATION);

        return c.execute(p).get(0).get("id").asText();
    }
}
