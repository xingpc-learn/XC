package com.xuecheng.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_course.service
 * @ClassName: TeachplanService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/31 11:28
 * @Version: 1.0
 */
@Service
public class CourseService {

    //注入teachplanMapper
    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    CourseBaseRepository courseBaseRepository;

    @Autowired
    TeachplanRepository teachplanRepository;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    CourseMarketRepository courseMarketRepository;

    @Autowired
    CoursePicRepository coursePicRepository;

    @Autowired
    CmsPageClient cmsPageClient;

    @Autowired
    CoursePubRepository coursePubRepository;

    @Autowired
    TeachplanMediaRepository teachplanMediaRepository;

    @Autowired
    TeachplanMediaPubRepository teachplanMediaPubRepository;

    //注入配置文件
    @Value("${course-publish.siteId}")
    private String siteId;
    @Value("${course-publish.templateId}")
    private String templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;
    @Value("${course-publish.pageWebPath}")
    private String pageWebPath;
    @Value("${course-publish.pagePhysicalPath}")
    private String pagePhysicalPath;
    @Value("${course-publish.dataUrlPre}")
    private String dataUrlPre;

    /**
    * 获取课程计划
    * */
    public TeachplanNode findTeachplanList(String courseId){
        return teachplanMapper.selectList(courseId);
    }

    /**
    * 添加课程计划,由于使用mysql需要事务支持
    * */
    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan){
        //校验课程id和课程计划名称
        if (teachplan==null|| StringUtils.isEmpty(teachplan.getCourseid())||StringUtils.isEmpty(teachplan.getPname())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //取出课程id
        String courseid = teachplan.getCourseid();
        //取出父节点id
        String parentid = teachplan.getParentid();
        //判断父节点是否为空，如果父节点为空获取根节点
        if (StringUtils.isEmpty(parentid)){
            //获取根节点
            parentid = getTeachplanRoot(courseid);
        }
        //取出父节点信息
        Optional<Teachplan> optional = teachplanRepository.findById(parentid);
        if (!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //父节点
        Teachplan teachplanParent = optional.get();
        //获取父节点级别
        String parentGrade = teachplanParent.getGrade();
        //设置新增教学计划父节点
        teachplan.setParentid(parentid);
        //发布状态，未发布
        teachplan.setStatus("0");
        //设值新增教学计划的级别
        if (parentGrade.equals("1")){
            teachplan.setGrade("2");
        }else if (parentGrade.equals("2")){
            teachplan.setGrade("3");
        }
        //设置新增计划课程id
        teachplan.setCourseid(teachplanParent.getCourseid());
        teachplanRepository.save(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
    * 获取课程根节点，如果没有则添加根节点
    * */
    public String getTeachplanRoot(String courseId){
        //校验课程id
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()){
            return null;
        }
        CourseBase courseBase = optional.get();
        //取出课程根节点
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseId, "0");
        if (teachplanList==null||teachplanList.size()==0){
            //如果该课程没有根节点则添加根节点
            Teachplan teachplanRoot=new Teachplan();
            teachplanRoot.setCourseid(courseId);
            teachplanRoot.setPname(courseBase.getName());
            teachplanRoot.setParentid("0");
            teachplanRoot.setGrade("1");
            teachplanRoot.setStatus("0");
            teachplanRepository.save(teachplanRoot);
            return teachplanRoot.getId();
        }
        Teachplan teachplan = teachplanList.get(0);
        return teachplan.getId();
    }

    /**
    * 查询我的课程列表
     * page:页码；size：每页容量；courseListRequest：查询条件
    * */
    public QueryResponseResult<CourseInfo> getCourseList(String companyId,int page, int size, CourseListRequest courseListRequest){
        //判断页面参数
        if (page<1){
            page=1;
        }
        if (size==0){
            size=8;
        }
        //分页助手开启
        PageHelper.startPage(page,size );
        //判断查询条件是否为空
        if (courseListRequest==null){
            courseListRequest=new CourseListRequest();
        }
        //企业id
        courseListRequest.setCompanyId(companyId);
        //调用mapper方法查询
        Page<CourseInfo> courseListPage = courseMapper.findCourseListPage(courseListRequest);
        QueryResult<CourseInfo> queryResult =new QueryResult<>();
        queryResult.setList(courseListPage);
        queryResult.setTotal(courseListPage.getTotal());
        QueryResponseResult<CourseInfo> courseInfoQueryResponseResult=new QueryResponseResult<>(CommonCode.SUCCESS, queryResult);
        return courseInfoQueryResponseResult;
    }

    /**
    * 添加课程
    * */
    @Transactional
    public AddCourseResult addCourseBase(CourseBase courseBase){
        //课程状态默认为未发布
        courseBase.setStatus("202001");
        courseBaseRepository.save(courseBase);
        return new AddCourseResult(CommonCode.SUCCESS,courseBase.getId());
    }

    /**
    * 根据id获取基础课程基本信息
    * */
    public CourseBase getCourseBaseById(String courseId){
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(courseId);
        return courseBaseOptional.orElse(null);
    }

    /**
    * 根据id修改更新课程基础信息
    * */
    @Transactional
    public ResponseResult updateCoursebase(String courseId,CourseBase courseBase){
        CourseBase one = this.getCourseBaseById(courseId);
        if (one==null){
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
            return null;
        }
        one.setName(courseBase.getName());
        one.setMt(courseBase.getMt());
        one.setStatus(courseBase.getSt());
        one.setGrade(courseBase.getGrade());
        one.setStudymodel(courseBase.getStudymodel());
        one.setUsers(courseBase.getUsers());
        one.setDescription(courseBase.getDescription());
        CourseBase save = courseBaseRepository.save(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
    * 根据课程id查询课程营销信息
    * */
    public CourseMarket getCourseMarketById(String courseId){
        Optional<CourseMarket> marketOptional = courseMarketRepository.findById(courseId);
        return marketOptional.orElse(null);
    }

    /**
    * 更新课程营销信息，如果没有则添加营销信息
    * */
    @Transactional
    public ResponseResult updateCourseMarket(String courseId,CourseMarket courseMarket){
        //根据id查询课程营销信息，如果不存在则添加
        Optional<CourseMarket> one = courseMarketRepository.findById(courseId);
        CourseMarket savemarket=null;
        if (one.isPresent()){
            savemarket = one.get();
        }
        if (savemarket!=null){
            //修改营销信息
            //判断收费方式，如果免费，将price设置为0
            savemarket.setCharge(courseMarket.getCharge());
            if ("203001".equals(courseMarket.getCharge())){
                savemarket.setPrice(0f);
            }else {
                savemarket.setPrice(courseMarket.getPrice());
            }
            //判断课程有效期
            savemarket.setValid(courseMarket.getValid());
            savemarket.setStartTime(courseMarket.getStartTime());
            if ("204001".equals(courseMarket.getValid())){
                savemarket.setEndTime(null);
            }else {
                savemarket.setEndTime(courseMarket.getEndTime());
            }
            savemarket.setQq(courseMarket.getQq());
        }else{
            //添加营销信息
            savemarket=new CourseMarket();
            savemarket.setId(courseId);
            BeanUtils.copyProperties(courseMarket, savemarket);
        }
        courseMarketRepository.save(savemarket);
        return new ResponseResult(CommonCode.SUCCESS);
    };

    /**
     *添加课程图片信息
     * */
    @Transactional
    public ResponseResult addCoursePic(String courseId, String pic) {
        Optional<CoursePic> picOptional = coursePicRepository.findById(courseId);
        CoursePic coursePic=null;
        if (picOptional.isPresent()){
            coursePic=picOptional.get();
        }
        if (coursePic==null){
            coursePic=new CoursePic();
        }
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
    * 查询课程图片
    * */
    public CoursePic getCoursePic(String courseId) {
        Optional<CoursePic> picOptional = coursePicRepository.findById(courseId);
        return picOptional.orElse(null);
    }

    /**
    * 删除课程图片
    * */
    public ResponseResult deleteCoursePic(String courseId) {
        long line = coursePicRepository.deleteByCourseid(courseId);
        if (line>0){
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    /**
    * 查询课程视图
    * */
    public CourseView getCourseView(String courseId) {
        CourseView courseView=new CourseView();
        //查询课程基本信息
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(courseId);
        if (courseBaseOptional.isPresent()){
            CourseBase courseBase = courseBaseOptional.get();
            courseView.setCourseBase(courseBase);
        }
        //查询课程营销信息
        Optional<CourseMarket> marketOptional = courseMarketRepository.findById(courseId);
        if (marketOptional.isPresent()){
            CourseMarket courseMarket = marketOptional.get();
            courseView.setCourseMarket(courseMarket);
        }
        //查询课程图片信息
        Optional<CoursePic> picOptional = coursePicRepository.findById(courseId);
        if (picOptional.isPresent()){
            CoursePic coursePic = picOptional.get();
            courseView.setCoursePic(coursePic);
        }
        //查询教学计划信息
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        if (teachplanNode!=null){
            courseView.setTeachplanNode(teachplanNode);
        }
        return courseView;
    }

    //根据id查询课程基本信息
    public CourseBase findCourseBaseById(String courseId){
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(courseId);
        if (courseBaseOptional.isPresent()){
            CourseBase courseBase = courseBaseOptional.get();
            return courseBase;
        }
        ExceptionCast.cast(CourseCode.COURSE_GET_NOTEXISTS);
        return null;
    }

    /**
    * 课程预览
    * */
    public CoursePublishResult preview(String courseId) {

        //查询课程基本信息
        CourseBase one = this.findCourseBaseById(courseId);
        //发布课程预览页面
        CmsPage cmsPage=new CmsPage();
        //站点
        cmsPage.setSiteId(siteId);
        //模板
        cmsPage.setTemplateId(templateId);
        //页面名称
        cmsPage.setPageName(courseId+".html");
        //页面别名
        cmsPage.setPageAliase(one.getName());
        //页面访问路径
        cmsPage.setPageWebPath(pageWebPath);
        //页面存储路径
        cmsPage.setPagePhysicalPath(pagePhysicalPath);
        //数据url
        cmsPage.setDataUrl(dataUrlPre+courseId);
        //页面创建时间
        cmsPage.setPageCreateTime(new Date());
        //远程请求cms保存页面信息
        CmsPageResult cmsPageResult = cmsPageClient.save(cmsPage);
        if (!cmsPageResult.isSuccess()){
            return new CoursePublishResult(CommonCode.FAIL,null);
        }
        //页面id
        String pageId = cmsPageResult.getCmsPage().getPageId();
        //页面url
        String pageUrl=previewUrl+pageId;
        return new CoursePublishResult(CommonCode.SUCCESS,pageUrl);
    }

    /**
    * 课程发布
    * */
    @Transactional
    public CoursePublishResult publish(String courseId) {
        //查询课程基本信息
        CourseBase one = this.findCourseBaseById(courseId);
        //发布课程页面信息
        CmsPage cmsPage=new CmsPage();
        //站点
        cmsPage.setSiteId(siteId);
        //模板
        cmsPage.setTemplateId(templateId);
        //页面名称
        cmsPage.setPageName(courseId+".html");
        //页面别名
        cmsPage.setPageAliase(one.getName());
        //页面访问路径
        cmsPage.setPageWebPath(pageWebPath);
        //页面存储路径
        cmsPage.setPagePhysicalPath(pagePhysicalPath);
        //数据url
        cmsPage.setDataUrl(dataUrlPre+courseId);
        //页面创建时间
        cmsPage.setPageCreateTime(new Date());
        //远程调用一键发布
        CmsPostPageResult cmsPostPageResult = cmsPageClient.publish(cmsPage);
        if (!cmsPostPageResult.isSuccess()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //更新课程状态
        CourseBase courseBase = this.saveCourseBasePubState(courseId);
        //页面url
        String pageUrl = cmsPostPageResult.getPageUrl();

        //课程索引
        //创建coursepub对象
        CoursePub coursePub = createCoursePub(courseId);
        //保存coursepub对象数据
        saveCoursePub(courseId, coursePub);
        //保存课程计划媒资信息到待索引表
        saveTeachplanMediaPub(courseId);
        //课程缓存...
        return new CoursePublishResult(CommonCode.SUCCESS,pageUrl);
    }

    /**
    * 保存coursepub
    * */
    private CoursePub saveCoursePub(String id,CoursePub coursePub){
        if (StringUtils.isEmpty(id)){
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        //根据id在pub表中查找，如果存在则获取，否则新建
        CoursePub coursePubNew =null;
        Optional<CoursePub> pubOptional = coursePubRepository.findById(id);
        coursePubNew = pubOptional.orElseGet(CoursePub::new);

        BeanUtils.copyProperties(coursePub, coursePubNew);
        //设置主键
        coursePubNew.setId(id);
        //更新时间戳为最新时间
        coursePubNew.setTimestamp(new Date());
        //发布时间
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date=sdf.format(new Date());
        coursePubNew.setPubTime(date);
        coursePubRepository.save(coursePubNew);
        return coursePubNew;
    }

    /**
    * 创建coursepub对象，将course数据合并为一张表
    * */
    private CoursePub createCoursePub(String id){
        //创建coursepub对象
        CoursePub coursePub=new CoursePub();
        coursePub.setId(id);

        //coursebase课程信息
        Optional<CourseBase> baseOptional = courseBaseRepository.findById(id);
        if (baseOptional.isPresent()){
            CourseBase courseBase = baseOptional.get();
            BeanUtils.copyProperties(courseBase,coursePub);
        }
        //coursepic课程图片
        Optional<CoursePic> picOptional = coursePicRepository.findById(id);
        if (picOptional.isPresent()){
            CoursePic coursePic = picOptional.get();
            BeanUtils.copyProperties(coursePic,coursePub);
        }
        //coursemarket课程营销信息
        Optional<CourseMarket> marketOptional = courseMarketRepository.findById(id);
        if (marketOptional.isPresent()){
            CourseMarket courseMarket = marketOptional.get();
            BeanUtils.copyProperties(courseMarket,coursePub);
        }
        //teachplan教学计划
        TeachplanNode teachplanNode = teachplanMapper.selectList(id);
        //将课程计划转换成json
        String teachplanString = JSON.toJSONString(teachplanNode);
        coursePub.setTeachplan(teachplanString);
        return coursePub;
    }

    /**
    * 更新课程发布状态
    * */
    private CourseBase saveCourseBasePubState(String courseId){
        CourseBase courseBaseById = this.findCourseBaseById(courseId);
        //更新发布状态
        courseBaseById.setStatus("202002");
        return courseBaseRepository.save(courseBaseById);
    }

    /**
    * 保存教学计划和媒资信息关联
    * */
    public ResponseResult saveMedia(TeachplanMedia teachplanMedia){
        if (teachplanMedia==null){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //课程计划id
        String teachplanId = teachplanMedia.getTeachplanId();
        //查询课程计划
        Optional<Teachplan> teachplanOptional = teachplanRepository.findById(teachplanId);
        if (!teachplanOptional.isPresent()){
            ExceptionCast.cast(CourseCode.COURSE_MEDIA_TAACHPLAN_ISNULL);
        }
        Teachplan teachplan = teachplanOptional.get();
        //只允许叶子节点课程计划选择视频
        String grade = teachplan.getGrade();
        if (StringUtils.isEmpty(grade)||!grade.equals("3")){
            ExceptionCast.cast(CourseCode.COURSE_MEDIA_TEACHPLAN_GRADEERROR);
        }
        //判断媒资数据库有没有信息，有则更新，无则新增
        TeachplanMedia one=null;
        Optional<TeachplanMedia> teachplanMediaOptional = teachplanMediaRepository.findById(teachplanId);
        one = teachplanMediaOptional.orElseGet(TeachplanMedia::new);
        //保存媒资信息和课程计划信息
        one.setTeachplanId(teachplanId);
        one.setCourseId(teachplanMedia.getCourseId());
        one.setMediaFileOriginalName(teachplanMedia.getMediaFileOriginalName());
        one.setMediaId(teachplanMedia.getMediaId());
        one.setMediaUrl(teachplanMedia.getMediaUrl());
        teachplanMediaRepository.save(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
    * 保存课程计划媒资信息
    * */
    private void saveTeachplanMediaPub(String courseId){
        //查询课程计划媒资信息
        List<TeachplanMedia> mediaList = teachplanMediaRepository.findByCourseId(courseId);
        //删除课程计划媒资信息发布表中信息
        Optional<TeachplanMediaPub> optional = teachplanMediaPubRepository.findById(courseId);
        if (optional.isPresent()){
            teachplanMediaPubRepository.deleteByCourseId(courseId);
        }
        List<TeachplanMediaPub> mediaPubs=new ArrayList<>();
        //保存课程媒资信息
        for (TeachplanMedia teachplanMedia : mediaList) {
            TeachplanMediaPub mediaPub=new TeachplanMediaPub();
            BeanUtils.copyProperties(teachplanMedia,mediaPub);
            mediaPub.setTimestamp(new Date());
            mediaPubs.add(mediaPub);
        }
        teachplanMediaPubRepository.saveAll(mediaPubs);
    }

}
