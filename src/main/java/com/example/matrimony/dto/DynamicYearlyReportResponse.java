package com.example.matrimony.dto;

import java.util.List;

public class DynamicYearlyReportResponse {

    private int year;
    private List<PlanYearlyStats> plans;
    private double totalRevenue;

    public DynamicYearlyReportResponse(int year,
                                       List<PlanYearlyStats> plans,
                                       double totalRevenue) {
        this.year = year;
        this.plans = plans;
        this.totalRevenue = totalRevenue;
    }

    public int getYear() { return year; }
    public List<PlanYearlyStats> getPlans() { return plans; }
    public double getTotalRevenue() { return totalRevenue; }
}