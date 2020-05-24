package Entities;

public class IssueWithHeaders
{
  private Integer total_count;
  private Boolean incomplete_results;
  private Issue[] items;

  public Integer getTotal_count()
  {
    return total_count;
  }

  public void setTotal_count(Integer total_count)
  {
    this.total_count = total_count;
  }

  public Boolean getIncomplete_results()
  {
    return incomplete_results;
  }

  public void setIncomplete_results(Boolean incomplete_results)
  {
    this.incomplete_results = incomplete_results;
  }

  public Issue[] getItems()
  {
    return items;
  }

  public void setItems(Issue[] items)
  {
    this.items = items;
  }
}
