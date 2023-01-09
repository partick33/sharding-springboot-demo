package com.partick.shardingjdbcdemo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.partick.shardingjdbcdemo.domain.User;
import com.partick.shardingjdbcdemo.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * @author partick_peng
 */
@Service
@Slf4j
public class UserServiceImpl {
    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    @Resource
    private UserMapper userMapper;

    @Resource
    private TransactionTemplate transactionTemplate;


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

    @Transactional(rollbackFor = Exception.class)
    public void createBatch() {
        int size = 1000;
        CountDownLatch latch = new CountDownLatch(size);
        List<User> list = new ArrayList<>();
        for (int i = size; i > 0; i--) {
            User user = new User();
            user.setUserId(i + 100L);
            user.setUserName("taskExecutor" + "--" + i);
            user.setIsDeleted((short) 0);
            list.add(user);
        }
        List<User> userCopyOnWriteArrayList = new CopyOnWriteArrayList<>(list);
        for (User user : userCopyOnWriteArrayList) {
            taskExecutor.execute(()->{
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                        try {
                            userMapper.insert(user);
                        } catch (Exception e) {
                            transactionStatus.setRollbackOnly();
                            e.printStackTrace();
                        }finally {
                            latch.countDown();
                        }
                    }
                });
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
