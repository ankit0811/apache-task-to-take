import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

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
  private static final Config cfg = new Config();
  private static final VelocityEngine ve = new VelocityEngine();

  private static final String TARGET_FILE_KEY = "TARGET_FILE";
  private static final String TARGET_FILE = cfg.getProperty(TARGET_FILE_KEY);

  public static void generateHTML(ExtractGitHubIssues extractGitHubIssues) throws IOException
  {
    ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
    ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
    ve.init();

    Template t = ve.getTemplate("table_template.vm" );

    VelocityContext context = new VelocityContext();

    context.put("repositories", extractGitHubIssues.getRepositories());
    context.put("refreshDate", new Timestamp(System.currentTimeMillis()));
    StringWriter writer = new StringWriter();
    t.merge(context, writer);

    LOG.info(String.format("Generating HTML file. Pls check %s file", TARGET_FILE));

    FileWriter fwriter =
        new FileWriter(TARGET_FILE, false);
    fwriter.write(writer.toString());
    fwriter.close();

  }

  public static void main(String[] args) throws Exception
  {
    if (TARGET_FILE.length() < 1) {
      throw new Exception("TARGET_FILE missing in config.properties file. Pls set a valid value for the same.");
    }
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
