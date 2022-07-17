package uml.links;

import com.fasterxml.jackson.databind.node.ArrayNode;
import dto.links.LinkDto;

import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({Generalization.class, Realization.class})
public abstract class ClassRelationship extends Link
{

    ClassRelationship()
    {
        super();
    }


}
