package uml.links;

import dto.links.LabeledLinkDto;
import dto.links.LinkDto;
import uml.ClassDiagram;
import uml.links.components.Label;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({Association.class, Dependency.class})
public abstract class LabeledLink extends Link
{

    private Label label = new Label();

    public LabeledLink()
    {
        super();
    }

    public LabeledLink(String name){
        this.getLabel().setName(name);
    }

    @XmlElement
    public Label getLabel()
    {
        return label;
    }

    public void setLabel(Label label)
    {
        this.label = label;
    }

    public void fromDto(LabeledLinkDto dto, ClassDiagram cd)
    {
        super.fromDto(dto, cd);
        if(dto.getName() != null)
        {
            this.getLabel().setName(dto.getName());
        }
        if(dto.getDistance() != null)
        {
            this.getLabel().setDistance(dto.getDistance());
        }
        if(dto.getOffset() != null)
        {
            this.getLabel().setOffset(dto.getOffset());
        }
    }
}
