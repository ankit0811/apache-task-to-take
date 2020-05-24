import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class Config {
  private final Properties configFile;
  private static final Logger LOG = Logger.getLogger(Config.class.getName());

  public Config() {
    configFile = new Properties();
    try {
      configFile.load(this.getClass().getClassLoader()
                          .getResourceAsStream("config.properties"));
    }
    catch (IOException e) {
      LOG.info("Exception reading the config file: config.properties");
      e.printStackTrace();
    }
  }

  public String getProperty(String key) {
    return this.configFile.getProperty(key);

  }

}