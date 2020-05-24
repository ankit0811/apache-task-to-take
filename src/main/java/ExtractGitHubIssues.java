import Entities.Issue;
import Entities.IssueWithHeaders;
import Entities.Label;
import Entities.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ExtractGitHubIssues
{

  private static final Logger LOG = Logger.getLogger(ExtractGitHubIssues.class.getName());

  private static final Config cfg = new Config();

  private static final String GIT_API_KEY = "GIT_API";
  private static final String GIT_API = cfg.getProperty(GIT_API_KEY);

  private static final String ORG_TAG_KEY = "ORG_TAG";
  private static final String ORG_TAG = cfg.getProperty(ORG_TAG_KEY);

  private static final String ORG_NAME_KEY = "ORG_NAME";
  private static final String ORG_NAME = cfg.getProperty(ORG_NAME_KEY);

  private static final String REPO_TAG_KEY = "REPO_TAG";
  private static final String REPO_TAG = cfg.getProperty(REPO_TAG_KEY);

  private static final String GIT_END_POINT_FOR_REPO = String.format(
      "%s/%s/%s/%s",
      GIT_API,
      ORG_TAG,
      ORG_NAME,
      REPO_TAG
  );

  private static final String PAGE_SUFFIX_KEY = "PAGE_SUFFIX";
  private static final String PAGE_SUFFIX = cfg.getProperty(PAGE_SUFFIX_KEY);

  private static final String SEARCH_SUFFIX_KEY = "GIT_SEARCH_SUFFIX";
  private static final String SEARCH_SUFFIX = cfg.getProperty(SEARCH_SUFFIX_KEY);

  public static final String GIT_END_POINT_FOR_REPO_WITH_PAGE_NUMBER =
      GIT_END_POINT_FOR_REPO + "?" + PAGE_SUFFIX;

  private static final String HEADER_LINK = "Link";
  private static final Pattern PAGE_PATTERN = Pattern.compile("(?<=&page=)\\d+");
  private static final String USER_NAME_PASS_KEY = "USER_PASS";

  private static final String FILTER_LABEL_KEY = "FILTER_LABEL";
  private static final String FILTER_LABEL = cfg.getProperty(FILTER_LABEL_KEY);

  private static final String FILTER_REPO_KEY = "FILTER_REPO";
  private static final String FILTER_REPO = cfg.getProperty(FILTER_REPO_KEY);

  private static final String IGNORE_REPO_KEY = "IGNORE_REPO";
  private static final String IGNORE_REPO = cfg.getProperty(IGNORE_REPO_KEY);

  private static final String FILTER_REPO_LANGUAGE_KEY = "FILTER_REPO_LANGUAGE";
  private static final String FILTER_REPO_LANGUAGE = cfg.getProperty(FILTER_REPO_LANGUAGE_KEY);

  private static final ObjectMapper mapper = new ObjectMapper();

  List<Repository> repositories = new ArrayList<>();

  private Integer endPageNumber;

  public Integer getEndPageNumber()
  {
    return endPageNumber;
  }

  public List<Repository> getRepositories()
  {
    return repositories;
  }

  protected void setRepositories(String dataToFetchFromURL, int startPageNumber, boolean checkAndSetLastPage)
      throws IOException
  {
    URL url = new URL(String.format(dataToFetchFromURL, startPageNumber));
    Repository[] tempRepository = mapper.readValue(
        getConnectionInputStream(url, checkAndSetLastPage),
        Repository[].class
    );

    repositories.addAll(Arrays.asList(tempRepository));

    startPageNumber++;
    if (getEndPageNumber() != null && startPageNumber <= getEndPageNumber()) {
      setRepositories(dataToFetchFromURL, startPageNumber, false);
    } else {
      resetEndPageNumber();
    }
  }

  private void setConcernedLabels(Repository repository, boolean checkAndSetLastPage) throws IOException
  {
    int pageNumber = 1;
    Integer lastPage = 1;
    String label_url = repository.getLabels_url()
                                 .replace("{/name}", "?" + PAGE_SUFFIX);

    List<String> userProvidedLabels = Arrays.asList(FILTER_LABEL.split(","));

    while (lastPage != null && pageNumber <= lastPage) {
      Label[] labels = mapper.readValue(
          getConnectionInputStream(new URL(String.format(label_url, pageNumber)), checkAndSetLastPage),
          Label[].class
      );
      Map<Integer, String> labelMap = new HashMap<>();
      List<Label> filteredLabels = Arrays.asList(labels)
                              .stream()
                              .filter(word ->
                                          userProvidedLabels
                                              .stream()
                                              .anyMatch(
                                                  label -> word.getName().toLowerCase().contains(label)))
                              .collect(Collectors.toList());

      for (Label label : filteredLabels) {
        labelMap.put(label.getId(), label.getName());
      }

      repository.getLabels().putAll(labelMap);
      lastPage = getEndPageNumber();
      pageNumber++;
      checkAndSetLastPage = false;
    }
  }


  private void setConcernedIssues(Repository repository, boolean checkAndSetLastPage) throws IOException
  {
    // https://api.github.com/search/issues?q=org:apache+repo:druid+is:open+label:%22Contributions%20Welcome%22"
    for (Map.Entry<Integer, String> entry : repository.getLabels().entrySet()) {
      int pageNumber = 1;
      Integer lastPage = 1;

      while (lastPage != null && pageNumber <= lastPage) {
        String queryUrlWithLabel = String.format(
            GIT_API + SEARCH_SUFFIX + PAGE_SUFFIX,
            repository.getFull_name(),
            URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()),
            pageNumber
        );

        LOG.info(String.format("label:%s queryUrlWithLabel : %s", entry.getValue(), queryUrlWithLabel));
        IssueWithHeaders issues = mapper.readValue(
            getConnectionInputStream(new URL(queryUrlWithLabel), checkAndSetLastPage),
            IssueWithHeaders.class
        );

        Map<String, List<Issue>> concernedIssue = new HashMap<>(repository.getConcernedIssues());
        List<Issue> entryIssues = concernedIssue.getOrDefault(entry.getValue(), new ArrayList<>());
        entryIssues.addAll(Arrays.asList(issues.getItems()));
        concernedIssue.put(entry.getValue(), entryIssues);
        repository.setConcernedIssues(concernedIssue);

        lastPage = getEndPageNumber();
        pageNumber++;
        checkAndSetLastPage = false;
      }
    }
  }


  InputStream getConnectionInputStream(URL url, boolean checkAndSetLastPage) throws IOException
  {
    // To-DO: connection status check
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    addAuthorization(con);
    con.setRequestMethod("GET");
    con.connect();
    if (con.getResponseCode() > 400) {
      try {
        LOG.info(String.format("Failed reading url %s, responseCode: %d.", url, con.getResponseCode()));
        con.disconnect();
        // https://developer.github.com/v3/#rate-limiting
        LOG.info("Sleeping for 60s. As search api has a rate limit of 30 requests per minute");
        Thread.sleep(60000);
      } catch (InterruptedException e) {
        LOG.warning("Exception in sleep.");
      }
      return getConnectionInputStream(url, checkAndSetLastPage);
    }
    LOG.info("con.getHeaderFields().get(\"X-RateLimit-Remaining\") = " + con.getHeaderFields().get("X-RateLimit-Remaining"));

    if (checkAndSetLastPage) {
      setEndPageNumber(con.getHeaderFields());
    }
    return con.getInputStream();
  }

  private void setEndPageNumber(Map<String, List<String>> connHeaderMap)
  {
    if (connHeaderMap.containsKey(HEADER_LINK)) {
      String s = (connHeaderMap.get(HEADER_LINK).get(0).split(","))[1];
      Matcher m = PAGE_PATTERN.matcher(s);
      if (m.find()) {
        endPageNumber = Integer.parseInt(m.group());
        LOG.info(String.format("End page number is : %d", endPageNumber));
      }
    }
  }

  private void resetEndPageNumber()
  {
    endPageNumber = null;
  }

  private void addAuthorization(HttpURLConnection con) throws IOException
  {
    String userCredentials = cfg.getProperty(USER_NAME_PASS_KEY);
    if(StringUtils.isEmpty(userCredentials)) {
      throw new IOException("!!Attention!!: GIT username and password not provided in config,properties."
                          + "Please provide the same as GIT api restricts only 60 calls per hour for unauthorized users.");
    }
    //TO-DO: Need to change the authorization from basic to token based
    String basicAuth = "Basic " + Base64.encode(userCredentials.getBytes());

    con.setRequestProperty("Authorization", basicAuth);
  }

  protected void setConcernedLabelsAndIssues() throws IOException
  {
    LOG.info(String.format("Repository count before filter: %d", repositories.size()));
    LOG.info(String.format("Filtering repository based on the config values for language and repo name."));
    repositories = getFilteredRepositories();
    LOG.info(String.format("Repository count after filter: %d", repositories.size()));
    int count = 0;
    for (Repository repository : repositories) {
      // set concerned labels for each repo
      LOG.info(String.format("For repository {%s}, setting valid labels", repository.getName()));
      setConcernedLabels(repository, true);
      if (repository.getLabels().size() > 0) {
        // reset page number per repository
        resetEndPageNumber();
        LOG.info(String.format("For repository {%s}, setting valid issues", repository.getName()));
        setConcernedIssues(repository, true);
        for (Map.Entry<String, List<Issue>> entry : repository.getConcernedIssues().entrySet()) {
          LOG.info(String.format(
              "For repository {%s}, %d issues found for issue type {%s}",
              repository.getName(), entry.getValue().size(), entry.getKey()
              ));
        }
      }
      count++;
      LOG.info(String.format("\n\nRepository no: %d. Still pending: %d", count, repositories.size() - count));
    }
  }

  protected List<Repository> getFilteredRepositories() {

    List<String> filteredByReposList = Arrays.asList(FILTER_REPO.split(","));
    List<String> filterByLanguage = Arrays.asList(FILTER_REPO_LANGUAGE.split(","));
    List<String> ignoreRepoList = Arrays.asList(IGNORE_REPO.split(","));
    LOG.info(String.format("List of repositories to be filter from config file: %s", filteredByReposList));
    LOG.info(String.format("Language to be filtered from config file: %s", filterByLanguage));
    LOG.info(String.format("Ignoring repositories: %s", ignoreRepoList));
    if (FILTER_REPO.length() > 0 || FILTER_REPO_LANGUAGE.length() > 0 || IGNORE_REPO.length() > 0) {
      return repositories
          .stream()
          .filter(repository -> filteredByReposList
              .stream()
              .anyMatch(repo -> repository.getName() != null &&
                                !repository.getName().toLowerCase().contains("site") &&
                                repository.getName().toLowerCase().contains(repo)))
          .filter(repository -> ignoreRepoList
              .stream()
              .anyMatch(repo -> repository.getName() != null &&
                                !repository.getName().toLowerCase().contains(repo)))
          .filter(repository -> filterByLanguage
              .stream().anyMatch(lang ->
                                     repository.getLanguage() == null ||
                                     repository.getLanguage().toLowerCase().contains(lang)))
          .collect(Collectors.toList());
    }

    return repositories;
  }
}
