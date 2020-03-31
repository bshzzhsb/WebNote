package com.hsb.note.dao;

import com.hsb.note.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author SipooHe
 * @date 2020/3/25 19:49
 */
public interface UserDao extends JpaRepository<User, Integer> {

    User findByUsername(String username);
}
