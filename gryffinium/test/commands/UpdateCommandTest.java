package commands;

import commands.uml.CreateCommand;
import commands.uml.UpdateCommand;
import dto.ElementTypeDto;
import dto.entities.ClassDto;
import dto.entities.EntityDto;
import dto.entities.EnumDto;
import dto.entities.operations.MethodDto;
import dto.entities.operations.OperationDto;
import dto.entities.variables.AttributeDto;
import dto.entities.variables.ParameterDto;
import dto.entities.variables.ValueDto;
import dto.links.BinaryAssociationDto;
import dto.links.DependencyDto;
import dto.links.GeneralizationDto;
import dto.links.RealizationDto;
import org.junit.Assert;
import org.junit.Test;
import play.libs.Json;
import scala.xml.PrettyPrinter;
import tyrex.services.UUID;
import uml.Visibility;
import uml.links.Generalization;
import uml.links.Realization;

public class UpdateCommandTest extends CommandTest
{
    @Test
    public void testClass_Success()
    {
        String id = createClass();

        String name = UUID.create();
        ClassDto dto = new ClassDto();
        dto.setId(id);
        dto.setName(name);

        UpdateCommand c = new UpdateCommand(Json.toJson(dto),
                ElementTypeDto.CLASS);

        c.execute(p);


        Assert.assertEquals(name,
                p.getDiagram().getEntity(id).getName());

    }


    @Test
    public void testEnum_Success()
    {
        String id = createEnum();

        String name = UUID.create();
        EnumDto dto = new EnumDto();
        dto.setId(id);
        dto.setName(name);

        UpdateCommand c = new UpdateCommand(Json.toJson(dto),
                ElementTypeDto.ENUM);

        c.execute(p);


        Assert.assertEquals(name,
                p.getDiagram().getEntity(id).getName());

    }

    @Test
    public void testInterface_Success()
    {
        String id = createInterface();

        String name = UUID.create();
        EntityDto dto = new EntityDto();
        dto.setId(id);
        dto.setName(name);

        UpdateCommand c = new UpdateCommand(Json.toJson(dto),
                ElementTypeDto.INTERFACE);

        c.execute(p);


        Assert.assertEquals(name,
                p.getDiagram().getEntity(id).getName());

    }

    @Test
    public void testBinaryAssociation_Success()
    {
        String id = createBinaryAssociation();
        String name = UUID.create();
        BinaryAssociationDto dto = new BinaryAssociationDto();
        dto.setId(id);
        dto.setName(name);

        UpdateCommand c = new UpdateCommand(Json.toJson(dto)
                , ElementTypeDto.BINARY_ASSOCIATION);

        c.execute(p);

        Assert.assertEquals(name,
                p.getDiagram().getAssociation(id).getName());
    }

    @Test
    public void testGeneralization_Success()
    {
        String source = createClass();
        String target = createClass();
        String newTarget = createClass();

        GeneralizationDto dto = new GeneralizationDto();
        dto.setSourceId(source);
        dto.setTargetId(target);

        CreateCommand c = new CreateCommand(Json.toJson(dto),
                ElementTypeDto.GENERALIZATION);

        String id = c.execute(p).get(0).get("id").asText();

        GeneralizationDto dto2 = new GeneralizationDto();
        dto2.setId(id);
        dto2.setTargetId(newTarget);

        UpdateCommand uc = new UpdateCommand(Json.toJson(dto2),
                ElementTypeDto.GENERALIZATION);

        uc.execute(p);

        Generalization g = (Generalization) p.getDiagram().getRelationship(id);

        Assert.assertEquals(newTarget,
                g.getParent().getId());
    }

    @Test
    public void testRealization_Success()
    {
        String source = createClass();
        String target = createInterface();
        String newTarget = createInterface();

        RealizationDto dto = new RealizationDto();
        dto.setSourceId(source);
        dto.setTargetId(target);

        CreateCommand c = new CreateCommand(Json.toJson(dto),
                ElementTypeDto.REALIZATION);

        String id = c.execute(p).get(0).get("id").asText();

        GeneralizationDto dto2 = new GeneralizationDto();
        dto2.setId(id);
        dto2.setTargetId(newTarget);

        UpdateCommand uc = new UpdateCommand(Json.toJson(dto2),
                ElementTypeDto.REALIZATION);

        uc.execute(p);

        Realization g = (Realization) p.getDiagram().getRelationship(id);

        Assert.assertEquals(newTarget,
                g.getInterface().getId());
    }

    @Test
    public void testDependency_Success()
    {
        String source = createClass();
        String target = createClass();


        DependencyDto dto = new DependencyDto();
        dto.setSourceId(source);
        dto.setTargetId(target);

        CreateCommand c = new CreateCommand(Json.toJson(dto),
                ElementTypeDto.DEPENDENCY);

        String id = c.execute(p).get(0).get("id").asText();
        String name = UUID.create();
        DependencyDto dto2 = new DependencyDto();
        dto2.setId(id);
        dto2.setName(name);

        UpdateCommand uc = new UpdateCommand(Json.toJson(dto2),
                ElementTypeDto.DEPENDENCY);

        uc.execute(p);

        Assert.assertEquals(name,
                p.getDiagram().getDependency(id).getLabel().getName());
    }


    @Test
    public void testValue_Success()
    {
        String testEnum = createEnum();

        ValueDto dto = new ValueDto();
        dto.setParentId(testEnum);

        CreateCommand c = new CreateCommand(Json.toJson(dto),
                ElementTypeDto.VALUE);
        c.execute(p);

        ValueDto dto2 = new ValueDto();
        dto2.setOldValue("value0");
        dto2.setValue("test");
        dto2.setParentId(testEnum);

        UpdateCommand uc = new UpdateCommand(Json.toJson(dto2),
                ElementTypeDto.VALUE);

        uc.execute(p);

        uml.entities.Enum e =
                (uml.entities.Enum) p.getDiagram().getEntity(testEnum);

        Assert.assertEquals("test", e.getValues().get(0));
    }

    @Test
    public void testAttribute_Success()
    {
        String testClass = createClass();

        AttributeDto dto = new AttributeDto();
        dto.setParentId(testClass);
        dto.setStatic(false);
        dto.setVisibility(Visibility.PUBLIC.getName());
        dto.setType("String");

        CreateCommand c = new CreateCommand(Json.toJson(dto),
                ElementTypeDto.ATTRIBUTE);
        String id = c.execute(p).get(0).get("id").asText();

        String name = UUID.create();
        AttributeDto dto2 = new AttributeDto();
        dto2.setName(name);
        dto2.setId(id);
        dto2.setParentId(testClass);

        UpdateCommand uc = new UpdateCommand(Json.toJson(dto2),
                ElementTypeDto.ATTRIBUTE);

        uc.execute(p);

        uml.entities.Class c1 =
                (uml.entities.Class) p.getDiagram().getEntity(testClass);

        Assert.assertEquals(name, c1.getAttribute(id).getName());
    }

    @Test
    public void testMethod_Success()
    {
        String testClass = createClass();

        String id = createMethod(testClass);

        String name = UUID.create();
        MethodDto dto = new MethodDto();
        dto.setParentId(testClass);
        dto.setId(id);
        dto.setName(name);

        UpdateCommand uc = new UpdateCommand(Json.toJson(dto),
                ElementTypeDto.METHOD);

        uc.execute(p);

        uml.entities.Class c1 =
                (uml.entities.Class) p.getDiagram().getEntity(testClass);

        Assert.assertEquals(name, c1.getMethodById(id).getName());
    }


    @Test
    public void testParameter_Success()
    {
        String testClass = createClass();
        String createMethod = createMethod(testClass);

        ParameterDto dto = new ParameterDto();
        dto.setMethodId(createMethod);
        dto.setParentId(testClass);
        dto.setType("String");
        dto.setConstant(false);

        CreateCommand c = new CreateCommand(Json.toJson(dto),
                ElementTypeDto.PARAMETER);
        String id = c.execute(p).get(0).get("id").asText();

        ParameterDto dto2 = new ParameterDto();
        dto2.setName("test");
        dto2.setId(id);
        dto2.setMethodId(createMethod);
        dto2.setParentId(testClass);

        UpdateCommand uc = new UpdateCommand(Json.toJson(dto2),
                ElementTypeDto.PARAMETER);

        uc.execute(p);

        uml.entities.Class c1 =
                (uml.entities.Class) p.getDiagram().getEntity(testClass);

        Assert.assertEquals("test",
                c1.getMethodById(createMethod).getParam(id).getName());

    }

}
