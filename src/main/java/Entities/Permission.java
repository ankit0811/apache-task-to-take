package Entities;

public class Permission
{
  private Boolean admin;
  private Boolean push;
  private Boolean pull;

  public Boolean getAdmin()
  {
    return admin;
  }

  public void setAdmin(Boolean admin)
  {
    this.admin = admin;
  }

  public Boolean getPush()
  {
    return push;
  }

  public void setPush(Boolean push)
  {
    this.push = push;
  }

  public Boolean getPull()
  {
    return pull;
  }

  public void setPull(Boolean pull)
  {
    this.pull = pull;
  }
}
