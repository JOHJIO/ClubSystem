package com.club.service;

import com.club.common.PageResult;
import com.club.entity.Announcement;
import com.club.exception.BusinessException;
import com.club.mapper.AnnouncementMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnnouncementService {
    @Autowired
    private AnnouncementMapper mapper;

    public PageResult page(int page, int size, String keyword) {
        PageHelper.startPage(page, size);
        List<Announcement> rows = mapper.selectList(keyword);
        PageInfo<Announcement> info = new PageInfo<>(rows);
        return new PageResult(info.getTotal(), rows);
    }

    public List<Announcement> list(String keyword) {
        return mapper.selectList(keyword);
    }

    public Announcement getById(Integer id) {
        return mapper.selectById(id);
    }

    @Transactional
    public void create(Announcement announcement) {
        mapper.insert(announcement);
    }

    @Transactional
    public void update(Integer id, Announcement announcement) {
        if (mapper.selectById(id) == null) {
            throw new BusinessException("数据不存在");
        }
        announcement.setId(id);
        mapper.update(announcement);
    }

    @Transactional
    public void delete(Integer id) {
        mapper.deleteById(id);
    }
}