package co.edu.unal.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpotifyConfig {
  private static final String CONFIG_FILE = "spotify.properties";
  private static final Properties properties = new Properties();
  private static boolean loaded = false;

  static {
    loadProperties();
  }

  private SpotifyConfig() {
  }

  private static void loadProperties() {
    try (InputStream input = SpotifyConfig.class.getClassLoader()
        .getResourceAsStream(CONFIG_FILE)) {
      if (input == null) {
        log.error("Config file not found: {}", CONFIG_FILE);
        log.error("Make sure spotify.properties exists in src/main/resources");
        return;
      }
      properties.load(input);
      loaded = true;
    } catch (IOException e) {
      log.error("Error loading Spotify configuration: {}", e.getMessage());
    }
  }

  public static String getClientId() {
    return properties.getProperty("spotify.client.id", "");
  }

  public static String getClientSecret() {
    return properties.getProperty("spotify.client.secret", "");
  }

  public static String getRedirectUri() {
    return properties.getProperty("spotify.redirect.uri", "http://[::1]:8080/callback");
  }

  public static boolean isConfigured() {
    return loaded && !getClientId().isEmpty() && !getClientSecret().isEmpty();
  }
}
