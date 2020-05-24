package Entities;

public class Milestone
{
    private String url;
    private String html_url;
    private String labels_url;
    private Integer id;
    private String node_id;
    private Integer number;
    private String title;
    private String description;
    private User creator;
    private Integer open_issues;
    private Integer closed_issues;
    private String state;
    private String created_at;
    private String updated_at;
    private String due_on;
    private String closed_at;

  public String getUrl()
  {
    return url;
  }

  public void setUrl(String url)
  {
    this.url = url;
  }

  public String getHtml_url()
  {
    return html_url;
  }

  public void setHtml_url(String html_url)
  {
    this.html_url = html_url;
  }

  public String getLabels_url()
  {
    return labels_url;
  }

  public void setLabels_url(String labels_url)
  {
    this.labels_url = labels_url;
  }

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

  public Integer getNumber()
  {
    return number;
  }

  public void setNumber(Integer number)
  {
    this.number = number;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public User getCreator()
  {
    return creator;
  }

  public void setCreator(User creator)
  {
    this.creator = creator;
  }

  public Integer getOpen_issues()
  {
    return open_issues;
  }

  public void setOpen_issues(Integer open_issues)
  {
    this.open_issues = open_issues;
  }

  public Integer getClosed_issues()
  {
    return closed_issues;
  }

  public void setClosed_issues(Integer closed_issues)
  {
    this.closed_issues = closed_issues;
  }

  public String getState()
  {
    return state;
  }

  public void setState(String state)
  {
    this.state = state;
  }

  public String getCreated_at()
  {
    return created_at;
  }

  public void setCreated_at(String created_at)
  {
    this.created_at = created_at;
  }

  public String getUpdated_at()
  {
    return updated_at;
  }

  public void setUpdated_at(String updated_at)
  {
    this.updated_at = updated_at;
  }

  public String getDue_on()
  {
    return due_on;
  }

  public void setDue_on(String due_on)
  {
    this.due_on = due_on;
  }

  public String getClosed_at()
  {
    return closed_at;
  }

  public void setClosed_at(String closed_at)
  {
    this.closed_at = closed_at;
  }
}
