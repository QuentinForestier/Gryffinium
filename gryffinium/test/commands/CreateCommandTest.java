package commands;

import akka.protobufv3.internal.Value;
import com.fasterxml.jackson.databind.JsonNode;
import commands.uml.CreateCommand;
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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.libs.Json;
import uml.AssociationClass;
import uml.ClassDiagram;
import uml.Visibility;
import uml.entities.InnerClass;
import uml.entities.InnerInterface;
import uml.entities.Interface;
import uml.links.*;

import java.util.Arrays;

public class CreateCommandTest extends CommandTest
{

    @Test
    public void testClass_Success()
    {
        String id = createClass();
        Assert.assertEquals(Class.class.getSimpleName(),
                p.getDiagram().getEntity(id).getClass().getSimpleName());

    }

    @Test
    public void testClass_TypeAlreadyExists_ShouldRename_Success()
    {

        String first = createClass();

        String second = createClass();

        Assert.assertNotEquals(first, second);
    }

    @Test
    public void testEnum_Success()
    {
        String id = createEnum();
        Assert.assertEquals(Enum.class.getSimpleName(),
                p.getDiagram().getEntity(id).getClass().getSimpleName());
    }

    @Test
    public void testEnum_TypeAlreadyExisting_ShouldRename_Success()
    {
        String first = createEnum();
        String second = createEnum();

        Assert.assertNotEquals(first, second);
    }

    @Test
    public void testInterface_Success()
    {
        String id = createInterface();
        Assert.assertEquals(Interface.class.getSimpleName(),
                p.getDiagram().getEntity(id).getClass().getSimpleName());
    }

    @Test
    public void testInterface_TypeAlreadyExisting_ShouldRename_Success()
    {

        String first = createInterface();
        String second = createInterface();

        Assert.assertNotEquals(first, second);
    }


    @Test
    public void testInnerClass_Success()
    {
        String id = createClass();


        InnerClassDto idto = new InnerClassDto();
        idto.setAbstract(true);
        idto.setX(100);
        idto.setY(100);
        idto.setWidth(100);
        idto.setHeight(100);
        idto.setOuter(id);
        idto.setStatic(true);

        CreateCommand c2 = new CreateCommand(Json.toJson(idto),
                ElementTypeDto.INNER_CLASS);
        String id2 = c2.execute(p).get(0).get("id").asText();
        Assert.assertEquals(InnerClass.class.getSimpleName(),
                p.getDiagram().getEntity(id2).getClass().getSimpleName());
    }

    @Test
    public void testInnerInterface_Success()
    {
        String id = createInterface();


        InnerInterfaceDto idto = new InnerInterfaceDto();
        idto.setX(100);
        idto.setY(100);
        idto.setWidth(100);
        idto.setHeight(100);
        idto.setOuter(id);
        idto.setStatic(true);

        CreateCommand c2 = new CreateCommand(Json.toJson(idto),
                ElementTypeDto.INNER_INTERFACE);
        String id2 = c2.execute(p).get(0).get("id").asText();
        Assert.assertEquals(InnerInterface.class.getSimpleName(),
                p.getDiagram().getEntity(id2).getClass().getSimpleName());
    }


    @Test
    public void testAggregation_Success()
    {
        String source = createClass();
        String target = createClass();

        BinaryAssociationDto dto = new BinaryAssociationDto();
        dto.setSourceId(source);
        dto.setTargetId(target);
        dto.setDirected(true);

        CreateCommand c = new CreateCommand(Json.toJson(dto),
                ElementTypeDto.AGGREGATION);

        String id = c.execute(p).get(0).get("id").asText();
        Assert.assertEquals(Aggregation.class.getSimpleName(),
                p.getDiagram().getAssociation(id).getClass().getSimpleName());
    }

    @Test
    public void testComposition_Success()
    {
        String source = createClass();
        String target = createClass();

        BinaryAssociationDto dto = new BinaryAssociationDto();
        dto.setSourceId(source);
        dto.setTargetId(target);
        dto.setDirected(true);

        CreateCommand c = new CreateCommand(Json.toJson(dto),
                ElementTypeDto.COMPOSITION);

        String id = c.execute(p).get(0).get("id").asText();
        Assert.assertEquals(Composition.class.getSimpleName(),
                p.getDiagram().getAssociation(id).getClass().getSimpleName());
    }

    @Test
    public void testBinaryAssociation_Success()
    {
        String id = createBinaryAssociation();
        Assert.assertEquals(BinaryAssociation.class.getSimpleName(),
                p.getDiagram().getAssociation(id).getClass().getSimpleName());
    }

    @Test
    public void testGeneralization_Success()
    {
        String source = createClass();
        String target = createClass();

        GeneralizationDto dto = new GeneralizationDto();
        dto.setSourceId(source);
        dto.setTargetId(target);

        CreateCommand c = new CreateCommand(Json.toJson(dto),
                ElementTypeDto.GENERALIZATION);

        String id = c.execute(p).get(0).get("id").asText();
        Assert.assertEquals(Generalization.class.getSimpleName(),
                p.getDiagram().getRelationship(id).getClass().getSimpleName());
    }

    @Test
    public void testRealization_Success()
    {
        String source = createClass();
        String target = createInterface();

        RealizationDto dto = new RealizationDto();
        dto.setSourceId(source);
        dto.setTargetId(target);

        CreateCommand c = new CreateCommand(Json.toJson(dto),
                ElementTypeDto.REALIZATION);

        String id = c.execute(p).get(0).get("id").asText();
        Assert.assertEquals(Realization.class.getSimpleName(),
                p.getDiagram().getRelationship(id).getClass().getSimpleName());
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
        Assert.assertEquals(Dependency.class.getSimpleName(),
                p.getDiagram().getDependency(id).getClass().getSimpleName());
    }

    @Test
    public void testAssociationClass_Success()
    {
        String source = createClass();
        String target = createClass();

        BinaryAssociationDto dto = new BinaryAssociationDto();
        dto.setSourceId(source);
        dto.setTargetId(target);
        dto.setDirected(true);

        ClassDto classDto = new ClassDto();
        classDto.setX(100);
        classDto.setY(100);
        classDto.setWidth(100);
        classDto.setHeight(100);

        AssociationClassDto associationClassDto = new AssociationClassDto();
        associationClassDto.setAssociationDto(dto);
        associationClassDto.setClassDto(classDto);

        CreateCommand c = new CreateCommand(Json.toJson(associationClassDto),
                ElementTypeDto.ASSOCIATION_CLASS);

        String id = c.execute(p).get(4).get("id").asText();
        Assert.assertEquals(AssociationClass.class.getSimpleName(),
                p.getDiagram().getAssociationClass(id).getClass().getSimpleName());
    }

    @Test
    public void testMutliAssociation_Success()
    {
        String class1 = createClass();
        String class2 = createClass();
        String class3 = createClass();

        MultiAssociationDto dto = new MultiAssociationDto();
        dto.setTargets(Json.newArray().add(class1).add(class2).add(class3));
        dto.setX(100.0);
        dto.setY(100.0);

        CreateCommand c = new CreateCommand(Json.toJson(dto),
                ElementTypeDto.MULTI_ASSOCIATION);

        String id = c.execute(p).get(0).get("id").asText();

        Assert.assertEquals(MultiAssociation.class.getSimpleName(),
                p.getDiagram().getMultiAssociation(id).getClass().getSimpleName());

    }

    @Test
    public void testInner_Success()
    {
        String class1 = createClass();
        String class2 = createClass();

        InnerDto dto = new InnerDto();
        dto.setSourceId(class1);
        dto.setTargetId(class2);

        CreateCommand c = new CreateCommand(Json.toJson(dto),
                ElementTypeDto.INNER);

        String id = c.execute(p).get(0).get("id").asText();

        Assert.assertEquals(Inner.class.getSimpleName(),
                p.getDiagram().getInner(id).getClass().getSimpleName());
        Assert.assertEquals(InnerClass.class.getSimpleName(),
                p.getDiagram().getInner(id).getInner().getClass().getSimpleName());
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

        uml.entities.Enum e =
                (uml.entities.Enum) p.getDiagram().getEntity(testEnum);

        Assert.assertEquals(1, e.getValues().size());
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
        c.execute(p);

        uml.entities.Class c1 =
                (uml.entities.Class) p.getDiagram().getEntity(testClass);

        Assert.assertEquals(1, c1.getAttributes().size());
    }

    @Test
    public void testMethod_Success()
    {
        String testClass = createClass();

        createMethod(testClass);

        uml.entities.Class c1 =
                (uml.entities.Class) p.getDiagram().getEntity(testClass);

        Assert.assertEquals(1, c1.getMethods().size());
    }

    @Test
    public void testConstructor_Success()
    {
        String testClass = createClass();

        OperationDto dto = new OperationDto();
        dto.setParentId(testClass);
        dto.setVisibility(Visibility.PUBLIC.getName());

        CreateCommand c = new CreateCommand(Json.toJson(dto),
                ElementTypeDto.CONSTRUCTOR);

        c.execute(p);

        uml.entities.Class c1 =
                (uml.entities.Class) p.getDiagram().getEntity(testClass);

        Assert.assertEquals(1, c1.getConstructors().size());
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
        c.execute(p);

        uml.entities.Class c1 =
                (uml.entities.Class) p.getDiagram().getEntity(testClass);

        Assert.assertEquals(1, c1.getMethodById(createMethod).getParams().size());

    }




}
