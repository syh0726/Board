<template>
      <el-form @keyup.enter="onSignIn()"
               label-width="120px"
               :rules="rules"
      >
          <el-form-item label="Email" prop="email">
            <el-input v-model="signinForm.email" size="large" placeholder="이메일을 입력하세요"></el-input>
          </el-form-item>

          <el-form-item label="Password" prop="password">
            <el-input v-model="signinForm.password" size="large" type="password" placeholder="비밀번호를 입력하세요"></el-input>
          </el-form-item>

        <el-form-item>
          <div v-if="checkForm">
            <el-button type="primary" @click="onSignIn()">로그인</el-button>
          </div>
          <div v-else>
            <el-button type="primary" disabled>로그인</el-button>
          </div>
        </el-form-item>
      </el-form>
</template>

<script setup lang="ts">

import axios from "axios";
import {useRouter} from "vue-router";
import {reactive, ref} from "vue";
import {useAuthStore} from "@/stores/auth";
import type {FormRules} from "element-plus";

const signinForm=ref({
  email:'',
  password:'',
})
const checkForm=ref(false);
const router=useRouter()
const authStore=useAuthStore()
const onSignIn= async function() {
  try {
    const response=await axios.post("api/auth/signin", signinForm.value);
    authStore.login(response.data);
    router.replace({name: "home"});
  }catch(error){
    alert("로그인 실패");
    console.error("실패")
  }
};
const validateEmail = (rule:any,value: any, callback: any)=>{
  if(signinForm.value.email ===''){
    callback(new Error('Please input the email'));
    checkForm.value=false;
  }else{
    callback();
    checkForm.value=true;
  }
}

const validatePassword = (rule:any,value: any, callback: any)=>{
  if(signinForm.value.password ===''){
    callback(new Error('Please input the password'));
    checkForm.value=false;
  }else{
    callback();
    checkForm.value=true;
  }
}

const rules = reactive<FormRules<typeof signinForm>>({
  email: [{validator: validateEmail, trigger: 'blur'}],
  password: [{validator: validatePassword, trigger: 'blur'}],
})

</script>

<style scoped>

</style>