//定义add函数
function add(x, y) {
    return x+y;
}
function add2(x, y) {
    return x+y+2;
}
//导出方法
module.exports.add=add;
//多个方法导出
// module.exports ={add,add2};//如果有多个方法这样导出
// module.exports.add2 = add2//如果有多个方法也可以这样导出
