package commands.entities.classes;

import com.fasterxml.jackson.databind.JsonNode;
import commands.Command;
import models.Project;
import play.libs.Json;
import uml.entities.Class;

public class UpdateClassCommand implements Command
{
    private Integer width;
    private Integer height;
    private Integer x;
    private Integer y = 500;

    private String name;
    private Boolean isAbstract = false;


    private String newName;

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isAbstract()
    {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract)
    {
        isAbstract = anAbstract;
    }

    public String getNewName()
    {
        return newName;
    }

    public void setNewName(String newName)
    {
        this.newName = newName;
    }

    @Override
    public JsonNode execute(Project project)
    {
        Class c = (Class) project.getDiagram().getEntity(name);

        if (x != null)
            c.setX(x);

        if (y != null)
            c.setY(y);

        if (width != null)
            c.setWidth(width);

        if (height != null)
            c.setHeight(height);

        if (isAbstract != null)
            c.setAbstract(isAbstract);

        c.setName(newName);


        return Json.toJson(c);
    }
}
