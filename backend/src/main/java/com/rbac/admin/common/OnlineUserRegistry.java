package com.rbac.admin.common;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OnlineUserRegistry {

    private final Map<String, OnlineUser> sessions = new ConcurrentHashMap<>();

    public void register(String token, String username, String ip) {
        sessions.put(token, new OnlineUser(username, ip, LocalDateTime.now()));
    }

    public void removeByToken(String token) {
        sessions.remove(token);
    }

    public void removeByUsername(String username) {
        sessions.values().removeIf(u -> u.username().equals(username));
    }

    public List<OnlineUser> list() {
        return new ArrayList<>(sessions.values());
    }

    public int count() {
        return sessions.size();
    }

    public record OnlineUser(String username, String ip, LocalDateTime loginTime) {
    }
}
