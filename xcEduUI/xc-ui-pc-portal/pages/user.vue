<template>
  <div>
    用户管理导航页面,<br>
    <!--指定子页面，嵌套路由-->
    <nuxt-link :to="'/user/one'">one页面</nuxt-link>
    <br>
    <nuxt-link :to="'/user/100'">修改</nuxt-link>
    <nuxt-child/>
  </div>
</template>
<script>
  export default {
    layout:'test'
  }
</script>
<style>

</style>
