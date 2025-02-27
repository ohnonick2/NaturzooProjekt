package net.ohnonick2.naturzooprojekt.utils;

import java.time.LocalDateTime;

public class ActiveUserSession {
    private String username;
    private String sessionId;
    private String ip;
    private LocalDateTime loginTime;

    public ActiveUserSession(String username, String sessionId, String ip, LocalDateTime loginTime) {
        this.username = username;
        this.sessionId = sessionId;
        this.ip = ip;
        this.loginTime = loginTime;
    }

    public String getUsername() { return username; }
    public String getSessionId() { return sessionId; }
    public String getIp() { return ip; }
    public LocalDateTime getLoginTime() { return loginTime; }
}

