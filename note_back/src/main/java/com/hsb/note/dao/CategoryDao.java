package com.hsb.note.dao;

import com.hsb.note.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author SipooHe
 * @date 2020/3/26 16:48
 */
public interface CategoryDao extends JpaRepository<Category, Integer> {
}
