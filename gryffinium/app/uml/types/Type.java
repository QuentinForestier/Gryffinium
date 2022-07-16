package uml.types;

import com.fasterxml.jackson.databind.node.ArrayNode;
import uml.ClassDiagram;
import uml.entities.Subscribers;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.LinkedList;

public abstract class Type
{

    private String name;

    private LinkedList<Subscribers> subscribers = new LinkedList<>();

    public Type(String name)
    {
        this.name = name;
    }

    @XmlAttribute
    public String getName()
    {
        return name;
    }

    public void setName(String name, ClassDiagram cd)
    {
        Type tmp = cd.getExistingTypes().getTypeByName(name);
        if (tmp != null && cd.getExistingTypes().isTypeExisting(tmp) && tmp instanceof SimpleType)
        {
            cd.getExistingTypes().removeType(tmp);
            this.subscribers.addAll(tmp.getSubscribers());
        }
        else
        {
            this.name = name;
        }
    }

    public void subscribe(Subscribers subscriber)
    {
        subscribers.add(subscriber);
    }

    public void unsubscribe(Subscribers subscriber)
    {
        subscribers.remove(subscriber);
    }

    public LinkedList<Subscribers> getSubscribers()
    {
        return subscribers;
    }


}
