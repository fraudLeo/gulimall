package com.leo.gulimall.product.web;

import com.leo.gulimall.product.entity.CategoryEntity;
import com.leo.gulimall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @GetMapping({"/","/index.html"})
    public Object indexPage(Model model) {
        List<CategoryEntity> level1CategoryEntityList = categoryService.getLevel1Categorys();
        model.addAttribute("categories",level1CategoryEntityList);
        return "index";
    }
}
