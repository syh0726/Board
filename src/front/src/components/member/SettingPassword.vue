<template>
  <el-form label-width="120px"
  :rules="rules">
    <el-form-item label="현재 비밀번호:" prop="originPassword">
      <el-input type="password" v-model="PasswordForm.originPassword"></el-input>
    </el-form-item>

    <el-form-item label="변경할 비밀번호" prop="newPassword">
      <el-input type="password" v-model="PasswordForm.newPassword"></el-input>
    </el-form-item>

    <el-form-item label="비밀번호 확인" prop="checkPassword">
      <el-input type="password" v-model="checkPassword"></el-input>
    </el-form-item>

    <div class="d-flex justify-content-end">
      <el-form-item>
        <el-button type="primary" @click="onChangePassword">변경</el-button>
      </el-form-item>
    </div>
  </el-form>
</template>

<script setup lang="ts">
import {reactive, ref} from "vue";
  import axios from "axios";
import type {FormRules} from "element-plus";

  const PasswordForm=ref({
      originPassword:'',
      newPassword:'',
  })

  const checkPassword=ref('');

  const isCheck=ref(false);

  const onChangePassword = async function (){
     try{
       await axios.put("/api/auth/setting/password",PasswordForm.value,{
        withCredentials:true
     });
       alert("비밀번호 변경 성공");
     }catch(error){
       console.log(error);
       alert("비밀번호 변경 실패");
     }
  }
  const validateOriginPassword = (rule:any,value: any, callback: any)=>{
    if(PasswordForm.value.originPassword ===''){
      callback(new Error('Please input the password'));
      isCheck.value=false;
    }else{
      callback();
      isCheck.value=true;
    }
  }
  const validateNewPassword = (rule:any,value: any, callback: any)=>{
    if(PasswordForm.value.newPassword ===''){
      callback(new Error('Please input the check password'));
      isCheck.value=false;
    }else if(PasswordForm.value.newPassword!==checkPassword.value) {
      callback(new Error('Two inputs don`t match!'));
    }else
      callback();
    isCheck.value=true;
  }

  const validateCheckPassword = (rule:any,value: any, callback: any)=>{
    if(checkPassword.value === ''){
      callback(new Error('Please input the check password'));
      isCheck.value=false;
    }else if(PasswordForm.value.newPassword!==checkPassword.value) {
      callback(new Error('Two inputs don`t match!'));
    }else
      callback();
    isCheck.value=true;
  }

const rules = reactive<FormRules>({
  originPassword: [{validator: validateOriginPassword, trigger: 'blur'}],
  newPassword: [{validator: validateNewPassword, trigger: 'blur'}],
  checkPassword: [{validator: validateCheckPassword, trigger: 'blur'}],
})


</script>



<style scoped>

</style>