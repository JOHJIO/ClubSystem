package com.club.service;

import com.club.common.PageResult;
import com.club.entity.Activity;
import com.club.exception.BusinessException;
import com.club.mapper.ActivityMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ActivityService {
    @Autowired
    private ActivityMapper mapper;

    public PageResult page(int page, int size, String keyword) {
        PageHelper.startPage(page, size);
        List<Activity> rows = mapper.selectList(keyword);
        PageInfo<Activity> info = new PageInfo<>(rows);
        return new PageResult(info.getTotal(), rows);
    }

    public List<Activity> list(String keyword) {
        return mapper.selectList(keyword);
    }

    public Activity getById(Integer id) {
        return mapper.selectById(id);
    }

    @Transactional
    public void create(Activity activity) {
        mapper.insert(activity);
    }

    @Transactional
    public void update(Integer id, Activity activity) {
        if (mapper.selectById(id) == null) {
            throw new BusinessException("数据不存在");
        }
        activity.setId(id);
        mapper.update(activity);
    }

    @Transactional
    public void delete(Integer id) {
        mapper.deleteById(id);
    }
}