package com.club.mapper;

import com.club.entity.Activity;

import java.math.BigDecimal;
import java.util.List;

public interface DashboardMapper {
    int countClubs();
    int countMembers();
    int countActivities();
    BigDecimal sumFinanceIncome();
    BigDecimal sumFinanceExpense();
    List<Activity> recentActivities();
}