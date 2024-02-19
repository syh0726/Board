<template>
  <div class="Setting">
    <el-form>
      <el-form-item label="이메일">
        <el-input v-model="SettingMember.email" disabled></el-input>
      </el-form-item>
    </el-form>

    <el-form>
      <el-form-item label="닉네임">
        <el-input v-model="SettingMember.nickName"></el-input>
        <el-button type="primary" @click="onChangeNickname">닉네임 변경</el-button>
      </el-form-item>
    </el-form>

    <el-form>
      <el-form-item label="휴대폰">
        <el-input v-model="SettingMember.phoneNumber"></el-input>
        <el-button type="primary" @click="onChangePhone">휴대폰 변경</el-button>
      </el-form-item>
    </el-form>


  </div>
</template>

<script setup lang="ts">
import {onMounted, onUnmounted, ref} from "vue";
  import axios from "axios";

  const SettingMember =ref({
    email:'',
    nickName:'',
    phoneNumber:''
});


  onMounted(async ()=>{
    const response=await axios.get("/api/auth/profile",{
      withCredentials:true
    });

    SettingMember.value=response.data;
  })

  const onChangeNickname=async function (){
      try{
        await axios.put("/api/auth/setting/nickname",SettingMember.value.nickName,{
        withCredentials:true,
          headers:{
            'Content-Type':'application/json',
          },
        })
        alert("닉네임 변경 성공!");
      }catch (error){
        console.log(error);
        alert("닉네임 변경 실패");
      }

  }

const onChangePhone=async function (){
  try{
    await axios.put("/api/auth/setting/phone",SettingMember.value.phoneNumber,{
      withCredentials:true,
      headers:{
        'Content-Type':'application/json',
      },
    })
    alert("휴대폰 번호 변경 성공!");
  }catch (error){
    console.log(error);
    alert("휴대폰 번호 변경 실패");
  }

}


</script>

<style scoped>
.Setting{
  margin-left: 50px;
}

.Setting .el-input{
  width: 200px;
}
</style>