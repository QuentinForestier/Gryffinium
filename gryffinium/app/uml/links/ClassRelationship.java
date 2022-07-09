package uml.links;

import com.fasterxml.jackson.databind.node.ArrayNode;
import play.libs.Json;
import uml.ClassDiagram;

public class ClassRelationship
{
    private Integer id;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    ClassRelationship()
    {
    }

    ClassRelationship(dto.links.LinkDto gl, ClassDiagram cd)
    {
        setGraphical(gl, cd);
    }

    public void setGraphical(dto.links.LinkDto gl, ClassDiagram cd)
    {
        if (gl.getId() != null)
        {
            this.id = gl.getId();
        }
    }

    public ArrayNode getCreationCommands(){
        //TODO  implement
        return Json.newArray();
    }
}
