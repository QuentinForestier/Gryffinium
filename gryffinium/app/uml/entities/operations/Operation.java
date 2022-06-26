package uml.entities.operations;

import graphical.entities.operations.GraphicalOperation;
import uml.ClassDiagram;
import uml.Visibility;
import uml.entities.ConstructableEntity;
import uml.entities.Entity;
import uml.entities.variables.Parameter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;

public abstract class Operation
{
    private Integer id;
    private String name;

    private ArrayList<Parameter> params = new ArrayList<>();

    private Visibility visibility;

    public Operation(String name, Visibility visibility)
    {
        this.name = name;
        this.visibility = visibility;
    }

    public Operation(String name)
    {
        this(name, Visibility.PUBLIC);
    }

    public Operation(GraphicalOperation go, ClassDiagram cd)
    {
        setGraphical(go, cd);
    }

    @XmlAttribute
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
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

    @XmlElement
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


    public void setGraphical(GraphicalOperation go, ClassDiagram cd)
    {
        if (go.getId() != null)
            setId(go.getId());
        if (go.getName() != null)
            setName(go.getName());
        if (go.getParameters() != null && go.getId() != null)
        {
            Entity parent = cd.getEntity(go.getParentId());
            Operation o = parent.getOperationById(go.getId());

            if (o == null)
            {
                throw new IllegalArgumentException("Operation not found");
            }

            /*
            for(Integer id : go.getParameters())
            {
                // TODO: get parameter from cd
            }*/
        }
        if (go.getVisibility() != null)
            this.setVisibility(Visibility.valueOf(go.getVisibility().toUpperCase()));


    }
}
