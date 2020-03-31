package com.hsb.note.service;

import com.hsb.note.dao.CategoryDao;
import com.hsb.note.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author SipooHe
 * @date 2020/3/26 16:52
 */
@Service
public class CategoryServicce {

    @Autowired
    CategoryDao categoryDao;

    public List<Category> getAll() {
        return categoryDao.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Category getCategoryById(int id) {
        return categoryDao.findById(id).get();
    }

    public void updateCategory(Category category) {
        categoryDao.save(category);
    }

    public void deleteById(int id) {
        categoryDao.deleteById(id);
    }
}
