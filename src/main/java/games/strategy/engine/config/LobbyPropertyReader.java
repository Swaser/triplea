package games.strategy.engine.config;

import java.io.File;

import com.google.common.annotations.VisibleForTesting;

/**
 * Reads property values from the lobby configuration file.
 */
public class LobbyPropertyReader {
  private static final String LOBBY_PROPERTIES_FILE = "lobby.properties";

  private final PropertyFileReader propertyFileReader;

  public LobbyPropertyReader() {
    propertyFileReader = new PropertyFileReader(LOBBY_PROPERTIES_FILE);
  }

  @VisibleForTesting
  LobbyPropertyReader(final File propertyFileOverride) {
    propertyFileReader = new PropertyFileReader(propertyFileOverride);
  }


  public int getPort() {
    return Integer.parseInt(propertyFileReader.readProperty("port"));
  }

  public String getPostgresUser() {
    return propertyFileReader.readProperty(PropertyKeys.postgresUser);
  }

  public String getPostgresPassword() {
    return propertyFileReader.readProperty("postgres_password");
  }

  @VisibleForTesting
  interface PropertyKeys {
    String port = "port";
    String postgresUser = "postgres_user";
    String postgresPassword = "postgres_password";
  }
}
