<template>
  <el-header class="header">
    <el-menu mode="horizontal" class="d-flex justify-content-between" background-color="#333" text-color="#fff" active-text-color="#ffd04b" router>
     <div class="d-flex align-items-center align-content-center">
        <el-menu-item index="/free" @click="Category('FREE')">자유</el-menu-item>
        <el-menu-item index="/information" @click="Category('INFORMATION')">정보</el-menu-item>
        <el-menu-item index="/trade" @click="Category('TRADE')">거래</el-menu-item>
     </div>
      <div v-if="isUserLogin" class="d-flex align-items-center align-content-center">
        <el-menu-item @click="router.push({name:'member-activity',params:{memberId:auth.getProfile.id}})">나의 활동</el-menu-item>
        <el-menu-item index="/setting">정보 변경</el-menu-item>
        <el-menu-item index="logout" @click="onLogout">로그 아웃</el-menu-item>
      </div>
      <div v-else class="d-flex align-items-center align-content-center">
        <el-menu-item index="/signIn">로그인</el-menu-item>
        <el-menu-item index="/signUp">회원 가입</el-menu-item>
      </div>
    </el-menu>
  </el-header>
</template>

<script lang="ts" setup>
  import {useAuthStore} from "@/stores/auth";
  import {useRouter} from "vue-router";
  import {ref} from "vue";
  import axios from "axios";
  import {usePostsStore} from "@/stores/posts";

  const auth=useAuthStore();
  const postsStore=usePostsStore();
  const router=useRouter();
  const isUserLogin=ref(auth.isLoggedIn);



  const onLogout=function (){
    axios.post("/api/auth/logout",{},{
      withCredentials: true
    }).then((response)=>{
      console.log("로그아웃 성공")
      router.push({name:'home'});
      auth.logout();
    }).catch(()=>{
      console.error("실패")
    });
  };

  router.afterEach(()=>{
    isUserLogin.value=auth.isLoggedIn;
  })

  const Category=(c:any)=>{
    postsStore.setCategory(c);
  }

</script>


<style>
header{
  padding:0;
  height: 60px;
}
</style>
