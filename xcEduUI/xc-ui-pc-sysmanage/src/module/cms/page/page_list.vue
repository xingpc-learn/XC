<template>
  <div>
    <!--查询表单-->
    <el-form v-model="params">
      <!--查询条件选项框,站点选项框-->
      <span>站点：</span>
      <el-select v-model="params.siteId" placeholder="请选择站点" style="width: 15%;">
        <el-option
          v-for="item in siteList"
          :key="item.siteId"
          :label="item.siteName"
          :value="item.siteId">
        </el-option>
      </el-select>
      <!--查询条件选项框,模板选项框-->
      <span>模板：</span>
      <el-select v-model="params.templateId" placeholder="请选择模板" style="width: 15%;">
        <el-option
          v-for="item in templateList"
          :key="item.templateId"
          :label="item.templateName"
          :value="item.templateId">
        </el-option>
      </el-select>
      <!--模糊匹配，别名查询输入框-->
      <span>页面别名：</span>
      <el-input v-model="params.pageAliase" placeholder="请输入页面别名" style="width: 15%"></el-input>
      <!--精确匹配，页面类型查询输入框-->
      <span>页面类型：</span>
      <el-input v-model="params.pageType" placeholder="页面类型(动态：1；静态：0)" style="width: 20%"></el-input>
      <!--编写页面静态部分，即view部分-->
      <el-button type="primary" size="small" @click="query">查询</el-button>
      <!--新增页面按钮,并附带本页面包含参数-->
      <router-link class="mui-tab-item" :to="{path: '/cms/page/add/',query:{
        page: this.params.page,
        siteId: this.params.siteId,
        templateId: this.params.templateId,
        pageAliase: this.params.pageAliase
      }}">
        <el-button type="primary" size="small">新增页面</el-button>
      </router-link>
    </el-form>

    <el-table
      :data="list"
      stripe
      style="width: 100%">
      <el-table-column type="index" width="60">
      </el-table-column>
      <el-table-column
                       prop="pageName" label="页面名称" width="120">
      </el-table-column>
      <el-table-column
                       prop="pageAliase"  label="别名"  width="120">
      </el-table-column>
      <el-table-column
                       prop="pageType"  label="页面类型"  width="100">
      </el-table-column>
      <el-table-column
                       prop="pageWebPath"  label="访问路径"  width="200">
      </el-table-column>
      <el-table-column
                       prop="pagePhysicalPath"  label="物理路径"  width="240">
      </el-table-column>
      <el-table-column
                       prop="pageCreateTime"  label="创建时间"  width="260">
      </el-table-column>
      <el-table-column
        prop="pageOperator"  label="操作"  width="220">
        <!--slot插槽-->
        <template slot-scope="page">
          <el-button
            size="mini"  @click="edit(page.row.pageId)"
          >编辑</el-button>
          <el-button
            size="mini" @click="del(page.row.pageId)"
          >删除</el-button>
          <el-button
            size="mini" @click="preview(page.row.pageId)"
          >预览</el-button>
          <el-button
            size="mini" @click="post(page.row.pageId)"
          >发布</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      layout="prev, pager, next"
      :total="total"
      :page-size="params.size"
      :current-page="params.page"
      @current-change="changePage"
      style="float: right">
    </el-pagination>
  </div>
</template>

<script>
  //导入cms的api接口
  import * as cmsApi from '../api/cms'
  /*编写页面静态部分，即model及vm部分。*/
  export default {
    data() {
      return {
        //页面列表
        list: [],
        //站点列表
        siteList: [],
        //模板列表
        templateList: [],
        total: 50,
        params: {
          //页码
          page: 1 ,
          //每页显示记录数
          size: 7,
          // 站点
          siteId: '',
          //  模板
          templateId: '',
          //  别名
          pageAliase: '',
          //页面类型
          pageType:''
        }
      }
    },
    methods: {
      //  分页查询,接收page页码
      changePage: function (page) {
        this.params.page=page;
        this.query()
      },
      //  查询
      query: function () {
        // alert("测试")
        cmsApi.page_list(this.params.page, this.params.size, this.params).then((res) => {
          this.total = res.queryResult.total;
          this.list = res.queryResult.list
        })
      },
      edit: function (pageId) {
        //切换路由值edit页面
        this.$router.push({path: '/cms/page/edit/'+pageId,query:{
          page: this.params.page,
            siteId: this.params.siteId
          }})
      },
      preview: function(pageId){
        window.open("http://www.xuecheng.com/cms/preview/"+pageId);
      },
      post: function(pageId){
        this.$confirm('是否发布此页面?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(() => {
          cmsApi.page_post(pageId).then((res) => {
            if (res.success) {
              console.log('发布页面pageId='+pageId);
              this.$message.success('发布成功，请稍后刷新查看');
            } else {
              this.$message.error('发布失败')
            }
          });
        });
      },
      del: function (pageId) {
        this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          // type: 'warning'
        }).then(() => {
          cmsApi.page_del(pageId).then((res) => {
            if (res.success) {
              /*this.$message({
                type: 'success',
                message: '提交成功!'
              });*/
              this.$message.success('删除成功');
              //删除成功，重新查询列表
              this.query();
            } else {
              /*this.$message({
           type: 'info',
           message: '已取消删除'
         });*/
              this.$message.error('删除失败')
            }
          });
        });
      }
    },
    created(){
      //从路由上获取参数,支持查询参数回显
      this.params.page=Number.parseInt(this.$route.query.page||1);
      this.params.siteId=this.$route.query.siteId||'';
      this.params.templateId=this.$route.query.templateId||'';
      this.params.pageAliase=this.$route.query.pageAliase||'';
    },
    //当页面渲染完成装载调用方法查询
    mounted(){
      //默认查询页面列表
      this.query();
      // 初始化站点列表
      cmsApi.site_list().then((res)=>{
        this.siteList=res.queryResult.list
      });
      //初始化模板列表
      cmsApi.template_list().then((res)=>{
        this.templateList=res.queryResult.list
      });
    }
  }
</script>
<style>
  /*编写页面样式，不是必须*/
</style>
