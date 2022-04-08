package com.partick.shardingjdbcdemo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.partick.shardingjdbcdemo.domain.User;
import com.partick.shardingjdbcdemo.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author partick_peng
 */
@Service
@Slf4j
public class UserServiceImpl {

    @Resource
    private UserMapper userMapper;

    public Integer create(User user) {
        int rows = userMapper.insert(user);
        return rows;
    }

    public User searchByUserId(Long userId) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        User user = userMapper.selectOne(wrapper);
        return user;
    }

    public void updateByUserId(User user) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq( "user_id", user.getUserId());
        userMapper.update(user, updateWrapper);
    }
}
