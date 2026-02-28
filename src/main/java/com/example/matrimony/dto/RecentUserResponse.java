package com.example.matrimony.dto;

import org.springframework.data.domain.Page;
import com.example.matrimony.entity.Profile;

public class RecentUserResponse {

    private long totalUsers;
    private long joinedIn24Hours;
    private long joinedIn7Days;
    private long joinedIn30Days;
    private Page<Profile> users;   

    
    public RecentUserResponse(long totalUsers,
                              long joinedIn24Hours,
                              long joinedIn7Days,
                              long joinedIn30Days,
                              Page<Profile> users) {

        this.totalUsers = totalUsers;
        this.joinedIn24Hours = joinedIn24Hours;
        this.joinedIn7Days = joinedIn7Days;
        this.joinedIn30Days = joinedIn30Days;
        this.users = users;
    }

    // ✅ Getters & Setters

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getJoinedIn24Hours() {
        return joinedIn24Hours;
    }

    public void setJoinedIn24Hours(long joinedIn24Hours) {
        this.joinedIn24Hours = joinedIn24Hours;
    }

    public long getJoinedIn7Days() {
        return joinedIn7Days;
    }

    public void setJoinedIn7Days(long joinedIn7Days) {
        this.joinedIn7Days = joinedIn7Days;
    }

    public long getJoinedIn30Days() {
        return joinedIn30Days;
    }

    public void setJoinedIn30Days(long joinedIn30Days) {
        this.joinedIn30Days = joinedIn30Days;
    }

    public Page<Profile> getUsers() {
        return users;
    }

    public void setUsers(Page<Profile> users) {
        this.users = users;
    }
}