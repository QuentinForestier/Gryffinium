package uml.links;

import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import dto.ElementTypeDto;
import dto.links.BinaryAssociationDto;
import play.libs.Json;
import uml.ClassDiagram;
import uml.entities.Entity;

public class Aggregation extends BinaryAssociation
{
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
        result.add(Command.createResponse(toDto(), ElementTypeDto.AGGREGATION));
        return result;
    }
}
