<!DOCTYPE html>
<html>
<head>
    <meta charset=UTF-8">
    <title>Document</title>
</head>
<body>
hello ${name}!
<br>
<hr>
<#--list指令，list列表数据-->
<table border="solid 1px">
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>生日</td>
        <td>存款</td>
    </tr>
    <#list stus as stu>
        <tr>
            <#--if指令-->
            <td>${stu_index+1}</td>
            <td>${stu.name}</td>
            <td <#if stu.age gt 30>style="background-color: gray"
            </#if>
            >${stu.age}</td>
                <#--<td>${stu.birthday?date}</td>-->
                <#--<td>${stu.birthday?string("yyyy年MM月dd日")}</td>-->
                <td>${stu.birthday?datetime}</td>
                <td
                <#if stu.money gt 300>style="background-color: red"
                </#if>
            >${stu.money}</td>
                <#--内建函数，日期函数-->
        </tr>
    </#list>
    集合长度：${stus?size}
</table>
<br>
<hr>
<#--获取map中数据，方式一：map中括号放可以-->
获取map中数据，方式一：map中括号放可以<br>
<#--空值判断varaible？？，缺省值()!-->
<#if stuMap?? && stuMap['stu1']??>
姓名：${stuMap['stu1'].name}<br>
年龄：${stuMap['stu1'].age}<br>
存款：${stuMap['stu1'].money}<br>
</#if>
<#--获取map中数据，方式二：map.key获取值-->
获取map中数据，方式二：map.key获取值<br>
姓名：${(stuMap.stu2.name)!'name'}<br>
年龄：${(stuMap.stu2.age)!'age'}<br>
存款：${(stuMap.stu2.money)!'money'}<br>
<#--map遍历-->
map遍历
<table border="solid 1px">
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>存款</td>
    </tr>
    <#list stuMap?keys as k>
    <tr>
        <td>${k_index+1}</td>
        <td>${stuMap[k].name}</td>
        <td>${stuMap[k].age}</td>
        <td>${stuMap[k].money}</td>
    </tr>
    </#list>
</table>
<br>
<hr>
<#--内奸函数，数值转字符串-->
<#--${point}-->
${point?c}
<br>
<hr>
<#--json格式转对象？eval-->
<#assign text="{'bank':'工商银行','account':'10101920201920212'}" />
    <#assign data=text?eval />
开户行：${data.bank}  账号：${data.account}
</body>
</html>
