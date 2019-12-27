import http from './../../../base/api/public'
import querystring from 'querystring'
let sysConfig = require('@/../config/sysConfig')
let apiUrl = sysConfig.xcApiUrlPre;
//axios实现了对HTTP方法的封装
//页面列表api
export const page_list  =(page,size,params)=>{
  //将json对象转换成key/value对
  let query=querystring.stringify(params)
  return http.requestQuickGet(apiUrl+'/cms/page/list/'+page+'/'+size+'/?'+query)
};
//站点列表
export const site_list  =()=>{
  return http.requestQuickGet(apiUrl+'/cms/page/site')
};
//模板列表
export const template_list  =()=>{
  return http.requestQuickGet(apiUrl+'/cms/page/template')
};
//添加页面
export const page_add=params=>{
  return http.requestPost(apiUrl+'/cms/page/add',params)
};
//页面查询
export const page_get=id=>{
  return http.requestQuickGet(apiUrl+'/cms/page/get/'+id)
};
//修改页面
export  const  page_edit=(pageId,pageForm)=>{
  return http.requestPut(apiUrl+'/cms/page/edit/'+pageId,pageForm)
};
//删除页面
export const page_del=(id)=>{
  return http.requestDelete(apiUrl+'/cms/page/del/'+id)
};
//发布页面
export const page_post=(id)=>{
  return http.requestPost(apiUrl+'/cms/page/postPage/'+id)
};


