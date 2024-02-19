<template>
      <el-form @keyup.enter="onSignUp()"
               label-width="120px"
               :rules="rules"
      >
        <el-form-item label="Email" prop="email">
          <el-input v-model="signupForm.email" placeholder="Email 입력" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="Password" prop="password">
          <el-input v-model="signupForm.password" type="password" placeholder="비밀번호 입력" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="Password Check" prop="checkPassword">
          <el-input v-model="signupForm.checkPassword" type="password" placeholder="비밀번호 재입력"></el-input>
        </el-form-item>

        <el-form-item label="NickName" prop="nickName">
          <el-input v-model="signupForm.nickName" placeholder="닉네임 입력"></el-input>
        </el-form-item>

        <el-form-item label="PhoneNumber" prop="phoneNumber">
          <el-input v-model="signupForm.phoneNumber" placeholder="휴대폰 번호 입력"></el-input>
        </el-form-item>

        <el-form-item>
          <div  v-if="checkForm">
          <el-button type="primary" @click="onSignUp()">회원 가입</el-button>
          </div>
          <div v-else>
            <el-button type="primary" disabled>회원 가입</el-button>
          </div>
        </el-form-item>

      </el-form>

</template>

<script setup lang="ts">

import {reactive, ref} from "vue";
import {useRouter} from "vue-router";
import axios from "axios";
import type {FormRules} from "element-plus";

const router=useRouter();
const checkForm=ref(false);

const signupForm=ref({
  email:'',
  password:'',
  checkPassword:'',
  nickName:'',
  phoneNumber:'',
})
const validateEmail = (rule:any,value: any, callback: any)=>{
  if(signupForm.value.email ===''){
    callback(new Error('Please input the email'));
    checkForm.value=false;
  }else{
    callback();
    checkForm.value=true;
  }
}

const validatePassword = (rule:any,value: any, callback: any)=>{
  if(signupForm.value.password ===''){
    callback(new Error('Please input the password'));
    checkForm.value=false;
  }else{
    callback();
    checkForm.value=true;
  }
}
const validateCheckPassword = (rule:any,value: any, callback: any)=>{
  if(signupForm.value.checkPassword ===''){
    callback(new Error('Please input the check password'));
    checkForm.value=false;
  }else if(signupForm.value.checkPassword!==signupForm.value.password) {
    callback(new Error('Two inputs don`t match!'));
  }else
    callback();
   checkForm.value=true;
}

const validateNickName = (rule:any,value: any, callback: any)=>{
  if(signupForm.value.nickName ===''){
    callback(new Error('Please input the nickName'));
    checkForm.value=false;
  }else{
    callback();
    checkForm.value=true;
  }
}

const validatePhoneNumber = (rule:any,value: any, callback: any)=>{
  if(signupForm.value.phoneNumber ===''){
    callback(new Error('Please input the phoneNumbber'));
    checkForm.value=false;
  }else{
    checkForm.value=true;
    callback();
  }
}

const rules = reactive<FormRules<typeof signupForm>>({
  email: [{ validator: validateEmail ,trigger:'blur'}],
  password:[{validator: validatePassword,trigger:'blur'}],
  checkPassword:[{validator: validateCheckPassword,trigger:'blur'}],
  nickName:[{validator: validateNickName,trigger:'blur'}],
  phoneNumber:[{validator: validatePhoneNumber,trigger:'blur'}],
})
const onSignUp= async function () {
  try {
    await axios.post("api/auth/signup", signupForm.value);
    window.alert("회원가입이 완료되었습니다.")
    router.replace({name: "home"});
  }catch (error){
    alert("이메일 형식이 일치하는지 확인해주세요.\n\n" +
        "비밀번호는 8~15 글자 이내 형식입니다.\n\n" +
        "닉네임은 2~16글자 이내 형시입니다.\n\n" +
        "전화 번호 형식이 일치하는지 확인해주세요.\n\n"
    );
  }
};


</script>

<style scoped>

</style>