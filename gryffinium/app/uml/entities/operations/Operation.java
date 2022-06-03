package uml.entities.operations;

import uml.Visibility;
import uml.entities.variables.Parameter;
import uml.entities.variables.Variable;

import java.util.ArrayList;

public class Operation
{
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

}
