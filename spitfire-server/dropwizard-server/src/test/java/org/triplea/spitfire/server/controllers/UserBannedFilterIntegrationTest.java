package org.triplea.spitfire.server.controllers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.database.rider.core.api.dataset.DataSet;
import java.net.URI;
import java.util.Map;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.triplea.http.client.HttpClient;
import org.triplea.http.client.HttpInteractionException;
import org.triplea.http.client.SystemIdHeader;
import org.triplea.http.client.lobby.login.LobbyLoginFeignClient;
import org.triplea.http.client.lobby.login.LoginRequest;
import org.triplea.spitfire.server.SpitfireServerTest;
import org.triplea.spitfire.server.SpitfireServerTestExtension;

/**
 * In this test we'll verify that user-banned filter is configured. We'll do ban by system-id as
 * that is easier to control and does not need us to ban localhost. We'll verify the ban against the
 * login endpoint as that will be a common first endpoint for banned users to attempt to access
 * after being booted and banned.
 */
@SuppressWarnings("UnmatchedTest")
@Disabled
@DataSet(
    value = SpitfireServerTestExtension.LOBBY_USER_DATASET + ", integration/banned_user.yml",
    useSequenceFiltering = false)
class UserBannedFilterIntegrationTest extends SpitfireServerTest {

  private static final LoginRequest LOGIN_REQUEST =
      LoginRequest.builder().password("pass").name("name").build();

  private static final String BANNED_SYSTEM_ID = "system-id";

  private final LobbyLoginFeignClient loginClient;

  UserBannedFilterIntegrationTest(final URI localhost) {
    loginClient = new HttpClient<>(LobbyLoginFeignClient.class, localhost).get();
  }

  @Test
  void banned() {
    assertThrows(
        HttpInteractionException.class,
        () -> loginClient.login(headersWithSystemId(BANNED_SYSTEM_ID), LOGIN_REQUEST));
  }

  private static Map<String, Object> headersWithSystemId(final String systemId) {
    return Map.of(SystemIdHeader.SYSTEM_ID_HEADER, systemId);
  }

  @Test
  void notBanned() {
    assertDoesNotThrow(
        () -> loginClient.login(headersWithSystemId("some other system-id"), LOGIN_REQUEST));
  }
}
