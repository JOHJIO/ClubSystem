package com.club.service;

import com.club.common.PageResult;
import com.club.entity.Venue;
import com.club.exception.BusinessException;
import com.club.mapper.VenueMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VenueService {
    @Autowired
    private VenueMapper mapper;

    public PageResult page(int page, int size, String keyword) {
        PageHelper.startPage(page, size);
        List<Venue> rows = mapper.selectList(keyword);
        PageInfo<Venue> info = new PageInfo<>(rows);
        return new PageResult(info.getTotal(), rows);
    }

    public List<Venue> list(String keyword) {
        return mapper.selectList(keyword);
    }

    public Venue getById(Integer id) {
        return mapper.selectById(id);
    }

    @Transactional
    public void create(Venue venue) {
        mapper.insert(venue);
    }

    @Transactional
    public void update(Integer id, Venue venue) {
        if (mapper.selectById(id) == null) {
            throw new BusinessException("数据不存在");
        }
        venue.setId(id);
        mapper.update(venue);
    }

    @Transactional
    public void delete(Integer id) {
        mapper.deleteById(id);
    }
}