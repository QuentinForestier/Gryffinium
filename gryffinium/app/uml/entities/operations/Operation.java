package uml.entities.operations;

import uml.Visibility;
import uml.entities.variables.Parameter;
import uml.entities.variables.Variable;

import java.util.ArrayList;

public abstract class Operation
{
    private Integer id;
    private String name;

    private ArrayList<Parameter> params = new ArrayList<>();

    private Visibility visibility;

    public Operation(String name, Visibility visibility){
        this.name = name;
        this.visibility = visibility;
    }

    public Operation(String name){
        this(name, Visibility.PUBLIC);
    }


    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ArrayList<Parameter> getParams()
    {
        return params;
    }

    public void setParams(ArrayList<Parameter> params)
    {
        this.params = params;
    }

    public Visibility getVisibility()
    {
        return visibility;
    }

    public void setVisibility(Visibility visibility)
    {
        this.visibility = visibility;
    }
}
