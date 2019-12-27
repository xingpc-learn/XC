import Home from '@/module/home/page/home.vue';
import page_list from '@/module/cms/page/page_list.vue';
import page_add from '@/module/cms/page/page_add.vue';
import page_edit from '@/module/cms/page/page_edit.vue';
export default [{
    path: '/cms',
    component: Home,
    //菜单名称
    name: 'CMS内容管理',
    hidden: false,
    children: [
      {path: '/cms/page/list',name: '页面列表',component: page_list,hidden: false},
      {path: '/cms/page/add',name: '新增页面',component: page_add,hidden: true},
      {path: '/cms/page/edit/:pageId',name: '修改页面',component: page_edit,hidden: true}
    ],
    '/api/cms': {
      target: 'http://localhost:31001',
      pathRewrite: {
        '^/api': ''//实际请求去掉/api
      }
    }
  }
]
