package com.hsb.note.controller;

import com.hsb.note.pojo.Category;
import com.hsb.note.response.Response;
import com.hsb.note.service.CategoryServicce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author SipooHe
 * @date 2020/3/28 17:08
 */
@Controller
public class CategoryController {

    @Autowired
    private CategoryServicce categoryServicce;

    @CrossOrigin
    @GetMapping("/api/categories")
    @ResponseBody
    public List<Category> getCategoryList() {
        return categoryServicce.getAll();
    }

    @CrossOrigin
    @GetMapping("/api/categories/delete/{id}")
    @ResponseBody
    public Response deleteCategory(@PathVariable("id") int id) {
        categoryServicce.deleteById(id);
        return new Response(200, "success", null);
    }

    @CrossOrigin
    @PostMapping("/api/categories/add")
    @ResponseBody
    public Response addCategory(@RequestBody Category requestCategory) {
        Category category = new Category();
        category.setName(requestCategory.getName());
        categoryServicce.updateCategory(category);
        return new Response(200, "success", null);
    }

    @CrossOrigin
    @PostMapping("/api/categories/update")
    @ResponseBody
    public Response updateCategory(@RequestBody Category requestCategory) {
        Category category = categoryServicce.getCategoryById(requestCategory.getId());
        category.setName(requestCategory.getName());
        categoryServicce.updateCategory(category);
        return new Response(200, "success", null);
    }

}
