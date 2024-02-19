<template>
  <div class="d-flex justify-content-center">
    <el-button @click="currentTab='SettingMember'">회원 정보 수정</el-button>
    <el-button @click="currentTab='SettingPassword'">비밀번호 변경</el-button>
  </div>
  <div class="mt-5">
    <keep-alive>
      <component v-if="currentTab === 'SettingMember'" :is="SettingMember"></component>
    </keep-alive>
    <keep-alive>
      <component v-if="currentTab === 'SettingPassword'" :is="SettingPassword"></component>
    </keep-alive>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
  import SettingMember from "@/components/member/SettingMember.vue"
  import SettingPassword from "@/components/member/SettingPassword.vue";
import {useAuthStore} from "@/stores/auth";
import {useRouter} from "vue-router";

  const currentTab=ref('SettingMember');
  const authStore=useAuthStore();

  const router=useRouter();
  onMounted(()=>{
  if(!authStore.isLoggedIn){
    router.push({name:'home'});
    alert("비정상적인 접근입니다.");
  }
})

</script>


<style scoped>

</style>