package com.partick.shardingjdbcdemo;

import com.partick.shardingjdbcdemo.domain.User;
import com.partick.shardingjdbcdemo.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author partick_peng
 */
@RestController
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/create")
    public Object create() {
        for (int i = 10; i < 20; i++) {
            User user = new User();
            user.setUserId(i+0L);
            user.setUserName("test_partick_" + i);
            user.setIsDeleted((short) 0);
            userService.create(user);
        }
        return "创建成功";
    }

    @PostMapping("/createBatch")
    public Object createBatch() {
        userService.createBatch();
        return "批量插入成功";
    }

    @GetMapping("/searchByUserId")
    public Object searchByUserId() {
        User user = userService.searchByUserId(11L);
        return user;
    }

    @GetMapping("/updateByUserId")
    public Object updateByUserId() {
        User user = userService.searchByUserId(11L);
        user.setIsDeleted((short) 1);
        userService.updateByUserId(user);
        return "修改成功";
    }
}
