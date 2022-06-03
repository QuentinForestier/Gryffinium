package commands.entities.classes;

import com.fasterxml.jackson.databind.JsonNode;
import commands.Command;
import models.Project;
import play.libs.Json;
import uml.entities.Class;

public class CreateClassCommand implements Command
{
    private int width;
    private int height;
    private int x;
    private int y;

    private String name;
    private boolean isAbstract = false;


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

    @Override
    public JsonNode execute(Project project)
    {
        Class c = new Class(name, isAbstract);

        c.setX(x);
        c.setY(y);
        c.setWidth(width);
        c.setHeight(height);

        project.getDiagram().addEntity(c);

        return Json.toJson(c);
    }
}
