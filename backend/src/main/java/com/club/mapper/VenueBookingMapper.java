package com.club.mapper;

import com.club.entity.VenueBooking;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VenueBookingMapper {
    List<VenueBooking> selectList(@Param("keyword") String keyword);
    VenueBooking selectById(Integer id);
    int insert(VenueBooking venueBooking);
    int update(VenueBooking venueBooking);
    int deleteById(Integer id);
}