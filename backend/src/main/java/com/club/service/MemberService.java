package com.club.service;

import com.club.common.PageResult;
import com.club.entity.Member;
import com.club.exception.BusinessException;
import com.club.mapper.MemberMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService {
    @Autowired
    private MemberMapper mapper;

    public PageResult page(int page, int size, String keyword) {
        PageHelper.startPage(page, size);
        List<Member> rows = mapper.selectList(keyword);
        PageInfo<Member> info = new PageInfo<>(rows);
        return new PageResult(info.getTotal(), rows);
    }

    public List<Member> list(String keyword) {
        return mapper.selectList(keyword);
    }

    public Member getById(Integer id) {
        return mapper.selectById(id);
    }

    @Transactional
    public void create(Member member) {
        mapper.insert(member);
    }

    @Transactional
    public void update(Integer id, Member member) {
        if (mapper.selectById(id) == null) {
            throw new BusinessException("数据不存在");
        }
        member.setId(id);
        mapper.update(member);
    }

    @Transactional
    public void delete(Integer id) {
        mapper.deleteById(id);
    }
}