package com.club.service;

import com.club.mapper.DashboardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {
    @Autowired
    private DashboardMapper dashboardMapper;

    public Map<String, Object> stats() {
        Map<String, Object> data = new HashMap<>();
        data.put("clubCount", dashboardMapper.countClubs());
        data.put("memberCount", dashboardMapper.countMembers());
        data.put("activityCount", dashboardMapper.countActivities());
        data.put("incomeTotal", dashboardMapper.sumFinanceIncome());
        data.put("expenseTotal", dashboardMapper.sumFinanceExpense());
        data.put("recentActivities", dashboardMapper.recentActivities());
        return data;
    }
}