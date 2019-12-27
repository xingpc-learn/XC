package com.xuecheng.manage_course.web.controller;

import com.xuecheng.api.system.SysDictionaryControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_course.service.SysDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_course.web.controller
 * @ClassName: SysDictionaryController
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/31 23:24
 * @Version: 1.0
 */
@RestController
@RequestMapping("/sys/dictionary")
public class SysDictionaryController implements SysDictionaryControllerApi {

    @Autowired
    SysDictionaryService sysDictionaryService;

    @Override
    @GetMapping("/get/{type}")
    public SysDictionary getByType(@PathVariable("type") String type) {
        return sysDictionaryService.getDictionaryList(type);
    }
}
