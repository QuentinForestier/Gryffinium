package uml.links;

import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import commands.CommandType;
import dto.ElementTypeDto;
import dto.links.BinaryAssociationDto;
import play.libs.Json;
import uml.ClassDiagram;
import uml.entities.Entity;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlSeeAlso({Composition.class})
@XmlType(name="Aggregation")
public class Aggregation extends BinaryAssociation
{
    public Aggregation(){
        super();
    }
    public Aggregation(Entity from, Entity to, String name, boolean isDirected)
    {
        super(from, to, name, isDirected);
    }

    public Aggregation(Entity from, Entity to)
    {
        super(from, to);
    }

    public Aggregation(BinaryAssociationDto gba, ClassDiagram cd){
        super(gba, cd);
    }
    @Override
    public ArrayNode getCreationCommands()
    {
        ArrayNode result = Json.newArray();
        result.add(Command.createResponse(toDto(), ElementTypeDto.AGGREGATION, CommandType.SELECT_COMMAND));
        result.add(getSource().getCreationCommands(this));
        result.add(getTarget().getCreationCommands(this));
        return result;
    }
}
