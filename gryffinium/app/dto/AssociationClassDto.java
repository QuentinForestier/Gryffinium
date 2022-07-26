package dto;

import dto.entities.ClassDto;
import dto.links.BinaryAssociationDto;
import uml.AssociationClass;
import uml.links.BinaryAssociation;

public class AssociationClassDto
{
    String id;
    ClassDto classDto;
    BinaryAssociationDto associationDto;

    public AssociationClassDto()
    {

    }

    public AssociationClassDto(AssociationClass associationClass)
    {
        this.id = associationClass.getId();
        this.classDto = (ClassDto) associationClass.getAssociatedClass().toDto();
        this.associationDto = (BinaryAssociationDto) associationClass.getAssociation().toDto();
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public ClassDto getClassDto()
    {
        return classDto;
    }

    public void setClassDto(ClassDto classDto)
    {
        this.classDto = classDto;
    }

    public BinaryAssociationDto getAssociationDto()
    {
        return associationDto;
    }

    public void setAssociationDto(BinaryAssociationDto associationDto)
    {
        this.associationDto = associationDto;
    }
}
