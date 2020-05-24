package Entities;

public class Label extends Entity
{
  private String node_id;
  private String url;
  private String name;
  private String color;
  private Boolean Default;
  private String description;

  public Integer getId()
  {
    return id;
  }

  public void setId(Integer id)
  {
    this.id = id;
  }

  public String getNode_id()
  {
    return node_id;
  }

  public void setNode_id(String node_id)
  {
    this.node_id = node_id;
  }

  public String getUrl()
  {
    return url.replace("api.github.com/repos/", "github.com/");
  }

  public void setUrl(String url)
  {
    this.url = url;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getColor()
  {
    return color;
  }

  public void setColor(String color)
  {
    this.color = color;
  }

  public Boolean getDefault()
  {
    return Default;
  }

  public void setDefault(Boolean aDefault)
  {
    Default = aDefault;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  @Override
  public String toString() {
    return String.format("Label: [name: %s]", getName());
  }
}
