<template>
  <div>
    <el-form :model="pageForm" label-width="80px" :rules="pageFormRules" ref="pageForm" status-icon>
      <el-form-item label="所属站点" prop="siteId">
        <el-select v-model="pageForm.siteId" placeholder="请选择站点">
          <el-option
            v-for="item in siteList"
            :key="item.siteId"
            :label="item.siteName"
            :value="item.siteId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="选择模版" prop="templateId">
        <el-select v-model="pageForm.templateId" placeholder="请选择模板">
          <el-option
            v-for="item in templateList"
            :key="item.templateId"
            :label="item.templateName"
            :value="item.templateId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="页面名称" prop="pageName">
        <el-input v-model="pageForm.pageName" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="别名" prop="pageAliase">
        <el-input v-model="pageForm.pageAliase" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="访问路径" prop="pageWebPath">
        <el-input v-model="pageForm.pageWebPath" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="物理路径" prop="pagePhysicalPath">
        <el-input v-model="pageForm.pagePhysicalPath" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="数据Url" prop="dataUrl">
        <el-input v-model="pageForm.dataUrl" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="类型">
        <el-radio-group v-model="pageForm.pageType">
          <el-radio class="radio" label="0">静态</el-radio>
          <el-radio class="radio" label="1">动态</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker type="datetime" placeholder="创建时间" v-model="pageForm.pageCreateTime">
        </el-date-picker>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog‐footer">
      <el-button type="primary" @click="editSubmit">提交</el-button>
      <el-button @click="go_back">返回</el-button>
    </div>
  </div>
</template>

<script>
  //导入cms的api接口
  import * as cmsApi from '../api/cms'
  /*编写页面静态部分，即model及vm部分。*/
  export default {
    data() {
      return {
        //站点列表
        siteList:[],
        //模版列表
        templateList:[],
        //新增界面数据
        pageForm: {
          siteId:'',
          templateId:'',
          pageName: '',
          pageAliase: '',
          pageWebPath: '',
          pageParameter:'',
          pagePhysicalPath:'',
          pageType:'',
          dataUrl:'',
          pageCreateTime: new Date()
        },
        pageFormRules: {
          siteId:[
            {required: true, message: '请选择站点', trigger: 'blur'}
          ],
          templateId:[
            {required: true, message: '请选择模版', trigger: 'blur'}
          ],
          pageName: [
            {required: true, message: '请输入页面名称', trigger: 'blur'}
          ],
          pageAliase: [
            {required: true, message: '请输入页面别名', trigger: 'blur'}
          ],
          pageWebPath: [
            {required: true, message: '请输入访问路径', trigger: 'blur'}
          ],
          pagePhysicalPath: [
            {required: true, message: '请输入物理路径', trigger: 'blur'}
          ],
          dataUrl: [
            {required: true, message: '请输入数据Url', trigger: 'blur'}
          ]
        },
        //页面id
        pageId: ''
      }
    },
    methods: {
      editSubmit() {
        this.$refs.pageForm.validate((valid) => {
          if (valid) {
            this.$confirm('确认修改吗?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              // type: 'warning'
            }).then(() => {
              cmsApi.page_edit(this.pageId,this.pageForm).then((res)=>{
                console.log(res);
                if (res.success){
                  /*this.$message({
                    type: 'success',
                    message: '提交成功!'
                  });*/
                  this.$message.success('修改成功');
                  //返回查询页面
                  this.go_back();
                }else {
                  /*this.$message({
               type: 'info',
               message: '已取消删除'
             });*/
                  this.$message.error('修改失败')
                }
              });
            });
          }
        });
      },
      go_back(){
        //返回功能，切换路由
        this.$router.push({
          path: '/cms/page/list',query:{
            //获取上路由上的参数，this.$route.query
            page: this.$route.query.page,
            siteId: this.$route.query.siteId,
            templateId: this.$route.query.templateId,
            pageAliase: this.$route.query.pageAliase
          }
        })
      }
    },
    //当页面创建完成初始化数据
    created:function(){
      //页面跳转至修改页面时，获取路径参数this.$route.params
      this.pageId=this.$route.params.pageId;
      //根据页面查询的页面信息，回显
      cmsApi.page_get(this.pageId).then((res)=>{
        console.log(res);
        if (res){
          this.pageForm=res;
        }
      });
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
