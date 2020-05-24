package Entities;

import org.apache.commons.lang.StringEscapeUtils;
import java.util.Arrays;

public class Issue
{
  private String url;
  private String repository_url;
  private String labels_url;
  private String comments_url;
  private String events_url;
  private String html_url;
  private Integer id;
  private String node_id;
  private Integer number;
  private String title;
  private User user;
  private Label[] labels;
  private String state;
  private Boolean locked;
  private User assignee;
  private User[] assignees;
  private Milestone milestone;
  private Integer comments;
  private String created_at;
  private String updated_at;
  private String closed_at;
  private String author_association;
  private PullRequest pull_request;
  private String body;
  private Integer score;

  public String getUrl()
  {
    return url;
  }

  public void setUrl(String url)
  {
    this.url = url;
  }

  public String getRepository_url()
  {
    return repository_url;
  }

  public void setRepository_url(String repository_url)
  {
    this.repository_url = repository_url;
  }

  public String getLabels_url()
  {
    return labels_url;
  }

  public void setLabels_url(String labels_url)
  {
    this.labels_url = labels_url;
  }

  public String getComments_url()
  {
    return comments_url;
  }

  public void setComments_url(String comments_url)
  {
    this.comments_url = comments_url;
  }

  public String getEvents_url()
  {
    return events_url;
  }

  public void setEvents_url(String events_url)
  {
    this.events_url = events_url;
  }

  public String getHtml_url()
  {
    return html_url;
  }

  public void setHtml_url(String html_url)
  {
    this.html_url = html_url;
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

  public User getUser()
  {
    return user;
  }

  public void setUser(User user)
  {
    this.user = user;
  }

  public Label[] getLabels()
  {
    return labels;
  }

  public void setLabels(Label[] labels)
  {
    this.labels = labels;
  }

  public String getState()
  {
    return state;
  }

  public void setState(String state)
  {
    this.state = state;
  }

  public Boolean getLocked()
  {
    return locked;
  }

  public void setLocked(Boolean locked)
  {
    this.locked = locked;
  }

  public User getAssignee()
  {
    return assignee;
  }

  public void setAssignee(User assignee)
  {
    this.assignee = assignee;
  }

  public User[] getAssignees()
  {
    return assignees;
  }

  public void setAssignees(User[] assignees)
  {
    this.assignees = assignees;
  }

  public Milestone getMilestone()
  {
    return milestone;
  }

  public void setMilestone(Milestone milestone)
  {
    this.milestone = milestone;
  }

  public Integer getComments()
  {
    return comments;
  }

  public void setComments(Integer comments)
  {
    this.comments = comments;
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

  public String getClosed_at()
  {
    return closed_at;
  }

  public void setClosed_at(String closed_at)
  {
    this.closed_at = closed_at;
  }

  public String getAuthor_association()
  {
    return author_association;
  }

  public void setAuthor_association(String author_association)
  {
    this.author_association = author_association;
  }

  public PullRequest getPull_request()
  {
    return pull_request;
  }

  public void setPull_request(PullRequest pull_request)
  {
    this.pull_request = pull_request;
  }

  public String getBody()
  {
    return StringEscapeUtils.escapeHtml(body);
  }

  public void setBody(String body)
  {
    this.body = body;
  }

  public Integer getScore()
  {
    return score;
  }

  public void setScore(Integer score)
  {
    this.score = score;
  }

  @Override
  public String toString() {
    return String.format("[Issue]: id%d, url:%s, number:%d, label:%s, html_url:%s",
                         getId(), getUrl(), getNumber(), Arrays.toString(getLabels()), getHtml_url());
  }
}
