var {add}=require('./model01.js');
var Vue =require('./vue.min.js');
var VM=new Vue({
    //el接管视图区域
    el:'#app',
    data:{
        name:'传智播客计算器',
        num1:0,
        num2:0,
        result:0,
        url:'www.itcast.com',
        size:25
    },
    methods:{
        count:function () {
            this.result=add(Number.parseInt(this.num1),parseInt(this.num2));
        }
    }
})
