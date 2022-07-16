package uml.links;


import javax.xml.bind.annotation.XmlType;

@XmlType(name = "MultiAssociation")
public class MultiAssociation
{
    private String id;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
