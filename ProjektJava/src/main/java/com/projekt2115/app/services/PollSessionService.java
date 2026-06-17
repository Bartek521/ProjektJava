package com.projekt2115.app.services;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PollSessionService {
    private List<String> pendingVotes = new ArrayList<>();
    public  void addVote(String vote){
        pendingVotes.add(vote);
    }
    public List<String> getPendingVotes() {
        return pendingVotes;
    }
    public void clear() {
        pendingVotes.clear();
    }
}
