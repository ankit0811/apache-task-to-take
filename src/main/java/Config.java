import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class Config {
  private final Properties configFile;
  private static final Logger LOG = Logger.getLogger(Config.class.getName());
  private final Properties prop = System.getProperties();

  public Config() {
    configFile = new Properties();
    try {
      if (prop.getProperty("config") == null ) {
        configFile.load(this.getClass().getClassLoader()
                            .getResourceAsStream("config.properties"));
        LOG.info("Reading default config file from resources/config.properties");
      } else {
        String configPath = prop.getProperty("config");
        LOG.info(String.format("Reading user provided config file from %s", configPath));
        configFile.load(new FileInputStream(new File(configPath)));

      }
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