package dto;


public class ElementDto
{
    private Integer id;

    public ElementDto(){}
    public ElementDto(int id){
        this.id = id;
    }
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

}
