package java_exercises.exercise8;

import java.util.Arrays;
import java.util.List;

class UserSession {
  private final String userId;
  private final long loginTime;
  private final List<String> permissions;

  public UserSession(String userId, List<String> permissions) {
    this.userId = userId;
    this.loginTime = System.currentTimeMillis();
    this.permissions = List.copyOf(permissions);
  }

  public String getUserId() {
    return userId;
  }

  public long getLoginTime() {
    return loginTime;
  }

  public List<String> getPermissions() {
    return List.copyOf(this.permissions);
  }
}

class SessionPublication {
  public static volatile UserSession currentSession;

  public static synchronized void publishSession() {
    List<String> perms = Arrays.asList("READ");

    UserSession session = new UserSession("sales789", perms);
    currentSession = session;
  }
}