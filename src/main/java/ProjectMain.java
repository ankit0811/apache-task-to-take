import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.logging.Logger;

/*
* Controller for the program
* 1. get and set all repositories (without any filters)
* 2. filter out repositories based on the config values and set valid issue list
* 3. generate html file using velocity template
* */

public class ProjectMain
{
  private static final Logger LOG = Logger.getLogger(ProjectMain.class.getName());
  private static final VelocityEngine ve = new VelocityEngine();

  public static void generateHTML(ExtractGitHubIssues extractGitHubIssues) throws IOException
  {
    ve.init();

    Template t = ve.getTemplate("./src/main/resources/table_template.vm" );

    VelocityContext context = new VelocityContext();

    context.put("repositories", extractGitHubIssues.getRepositories());
    context.put("refreshDate", new Timestamp(System.currentTimeMillis()));
    StringWriter writer = new StringWriter();
    t.merge(context, writer);

    LOG.info("Generating HTML file. Pls check resources for table_template.html file");

    FileWriter fwriter =
        new FileWriter("./src/main/resources/table_template.html", false);
    fwriter.write(writer.toString());
    fwriter.close();

  }

  public static void main(String[] args) throws IOException
  {
    ExtractGitHubIssues extractGitHubIssues = new ExtractGitHubIssues();
    extractGitHubIssues.setRepositories(ExtractGitHubIssues.GIT_END_POINT_FOR_REPO_WITH_PAGE_NUMBER, 1, true);
    try {
      extractGitHubIssues.setConcernedLabelsAndIssues();
    } catch (Exception e) {
      LOG.warning("Exception in setting labels and issues");
      e.printStackTrace();
    }
    generateHTML(extractGitHubIssues);
  }


}
