package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.api.cms
 * @ClassName: CmsPageControllerApi
 * @Author: Administrator
 * @Description: cms页面控制接口
 * @Date: 2019/7/21 17:11
 * @Version: 1.0
 */
@Api(value="cms页面管理接口",description="cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {

    /**
     * @Method findPageList
     * @Description：页面请求接口
     * @param page:当前页
     * @param size：每页内容
     * @param queryPageRequest：站点，模板，页面请求参数信息
     * @Return com.xuecheng.framework.model.response.QueryResponseResult
     */
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
         @ApiImplicitParam(name="page",value="页码",required=true,paramType="path",dataType="int"),
         @ApiImplicitParam(name="size",value="每页记录数",required=true,paramType="path",dataType="int")})
    public QueryResponseResult findPageList(int page, int size, QueryPageRequest queryPageRequest);

    /**
     * 查询站点列表
     * */
    @ApiOperation("站点列表")
    public QueryResponseResult findSiteList();

    /**
     * 查询模板列表
     * */
    @ApiOperation("模板列表")
    public QueryResponseResult findTemplateList();

    /**
    * 添加页面
    * */
    @ApiOperation("添加页面")
    public CmsPageResult add(CmsPage cmsPage);

    /**
    * 根据id查询数据
    * */
    @ApiOperation("通过id查询页面")
    public CmsPage findById(String id);

    /**
    * 修改数据
    * */
    @ApiOperation("修改页面")
    public CmsPageResult edit(String id,CmsPage cmsPage);

    /**
    * 删除页面,根据id删除
    * */
    @ApiOperation("删除页面")
    public ResponseResult delete(String id);

    /**
    * 页面发布
    * */
    @ApiOperation("页面发布")
    public ResponseResult post(String pageId);

    /**
    * 保存页面
    * */
    @ApiOperation("保存页面")
    public CmsPageResult save(CmsPage cmsPage);

    /**
    * 页面一键发布
    * */
    @ApiOperation("页面一键发布")
    public CmsPostPageResult postPageQuick(CmsPage cmsPage);

}
