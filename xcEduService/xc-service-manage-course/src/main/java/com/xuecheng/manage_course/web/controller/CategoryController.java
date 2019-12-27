package com.xuecheng.manage_course.web.controller;

import com.xuecheng.api.category.CategoryControllerApi;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_course.web.controller
 * @ClassName: CategoryController
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/31 21:53
 * @Version: 1.0
 */
@RestController
@RequestMapping("/category")
public class CategoryController implements CategoryControllerApi {

    @Autowired
    CategoryService categoryService;

    @Override
    @GetMapping("/list")
    public CategoryNode findList() {
        return categoryService.getCategoryList();
    }
}
