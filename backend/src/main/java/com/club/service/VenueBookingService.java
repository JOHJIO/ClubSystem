package com.club.service;

import com.club.common.PageResult;
import com.club.entity.VenueBooking;
import com.club.exception.BusinessException;
import com.club.mapper.VenueBookingMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VenueBookingService {
    @Autowired
    private VenueBookingMapper mapper;

    public PageResult page(int page, int size, String keyword) {
        PageHelper.startPage(page, size);
        List<VenueBooking> rows = mapper.selectList(keyword);
        PageInfo<VenueBooking> info = new PageInfo<>(rows);
        return new PageResult(info.getTotal(), rows);
    }

    public List<VenueBooking> list(String keyword) {
        return mapper.selectList(keyword);
    }

    public VenueBooking getById(Integer id) {
        return mapper.selectById(id);
    }

    @Transactional
    public void create(VenueBooking venueBooking) {
        mapper.insert(venueBooking);
    }

    @Transactional
    public void update(Integer id, VenueBooking venueBooking) {
        if (mapper.selectById(id) == null) {
            throw new BusinessException("数据不存在");
        }
        venueBooking.setId(id);
        mapper.update(venueBooking);
    }

    @Transactional
    public void delete(Integer id) {
        mapper.deleteById(id);
    }
}