package com.hsb.note.service;

import com.hsb.note.dao.UserDao;
import com.hsb.note.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author SipooHe
 * @date 2020/3/25 19:50
 */
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public boolean isExist(String username) {
        User user = getUserByUsername(username);
        return user != null;
    }

    public void addUser(User user) {
        userDao.save(user);
    }
}
