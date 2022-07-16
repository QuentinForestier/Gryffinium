package dto;


public abstract class ElementDto
{
    private String id;

    public ElementDto(){}
    public ElementDto(String id){
        this.id = id;
    }
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

}
