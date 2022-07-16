package uml.entities.operations;

import com.fasterxml.jackson.databind.node.ArrayNode;
import dto.entities.operations.OperationDto;
import play.libs.Json;
import tyrex.services.UUID;
import uml.ClassDiagram;
import uml.Visibility;
import uml.entities.Entity;
import uml.entities.variables.Parameter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;

public abstract class Operation
{
    private String id;
    private String name;

    private ArrayList<Parameter> params = new ArrayList<>();

    private Visibility visibility;

    private Entity parent;

    public Operation(){

    }

    public Operation(String name, Visibility visibility)
    {
        this.name = name;
        this.visibility = visibility;
        this.id = UUID.create();
    }

    public Operation(String name)
    {
        this(name, Visibility.PUBLIC);
    }

    public Operation(OperationDto go, ClassDiagram cd)
    {
        fromDto(go, cd);
        this.id = UUID.create();
    }

    @XmlTransient
    public Entity getParent()
    {
        return parent;
    }

    public void setParent(Entity parent)
    {
        this.parent = parent;
    }

    @XmlAttribute
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    @XmlAttribute
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @XmlElement(name="parameter")
    public ArrayList<Parameter> getParams()
    {
        return params;
    }

    public void setParams(ArrayList<Parameter> params)
    {
        this.params = params;
    }

    @XmlAttribute
    public Visibility getVisibility()
    {
        return visibility;
    }

    public void setVisibility(Visibility visibility)
    {
        this.visibility = visibility;
    }


    public void fromDto(OperationDto go, ClassDiagram cd)
    {
        if (go.getId() != null)
            setId(go.getId());
        if (go.getName() != null)
            setName(go.getName());

        if (go.getVisibility() != null)
            this.setVisibility(Visibility.valueOf(go.getVisibility().toUpperCase()));

        if(go.getParentId() != null)
            this.setParent(cd.getEntity(go.getParentId()));
    }

    public void addParam(Parameter param)
    {
        params.add(param);
    }

    public void removeParam(String id){
        for(Parameter param : params){
            if(param.getId().equals(id)){
                params.remove(param);
                return;
            }
        }
    }

    public Parameter getParam(String id){
        for(Parameter param : params){
            if(param.getId().equals(id)){
                return param;
            }
        }
        return null;
    }

    public abstract OperationDto toDto(Entity e);
    public abstract ArrayNode getCreationCommand(Entity e);

    public ArrayNode getParametersCreationCommands(Entity e){
        ArrayNode result = Json.newArray();
        for(Parameter param : params){
            result.add(param.getCreationCommand(e, this));
        }
        return result;
    }

    public void load(ClassDiagram cd){
        for(Parameter param : params){
            param.load(cd);
        }
    }
}
