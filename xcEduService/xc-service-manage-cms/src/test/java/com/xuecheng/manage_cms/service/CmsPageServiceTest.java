package com.xuecheng.manage_cms.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_cms.service
 * @ClassName: CmsPageServiceTest
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/28 20:25
 * @Version: 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageServiceTest {

    @Autowired
    CmsPageService cmsPageService;

    @Test
    public void testGestHtml(){
        System.out.println(cmsPageService.getPageHtml("5d3bf44c2b7de41d403ae3f1"));
    }

}
