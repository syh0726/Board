<template>
  <div>
    <div>
      닉네임 : <span style="font-size:20px">{{nickName}}</span>의 활동 목록
    </div>

    <div>
      <ul>
        <div class="line" style="border:solid 0.5px var(--el-border-color);"></div>
        <li v-for="activityPost in activityPosts" :key="activityPost.postId">
          <div class="d-flex justify-content-between mt-4 mb-4">
            <span @click="router.push({name:'post',params:{postId:activityPost.postId}})">TITLE : {{activityPost.title}}</span> <span>작성일 : {{activityPost.createdAt}}</span>
          </div>
          <div class="line" style="border:solid 0.5px var(--el-border-color);"></div>
        </li>
      </ul>
    </div>
  </div>


</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import axios from "axios";
import {useRoute, useRouter} from "vue-router";

  const route=useRoute();
  const router=useRouter();

  const activityPosts=ref([{
    postId:0,
    title:'',
    createdAt:'',
  }])

  const nickName=ref('');
  const memberId=route.params.memberId;


  onMounted(async ()=>{
    const response=await axios.get(`/api/member/${memberId}`);
    nickName.value=response.data.nickName;
    activityPosts.value=response.data.postList;
  })


</script>

<style scoped>
ul{
  list-style:none;
}
li {
  margin-bottom: 1.5rem;
}
</style>