<template>
  <el-row >
    <el-col :span="4"><el-button type="primary" @click="postWrite">글 작성</el-button></el-col>
    <el-col :span="20" @keyup.enter="onGetPosts() "><el-input v-model="search" placeholder="검색어를 입력해주세요">
      <template #append>
        <el-select v-model="select" placeholder="Select" style="width: 115px">
          <el-option label="제목+내용" value="ALL"></el-option>
          <el-option label="제목" value="TITLE"></el-option>
          <el-option label="내용" value="CONTENT"></el-option>
          <el-option label="닉네임" value="NICKNAME"></el-option>
        </el-select>
      </template>
      <template #prepend>
        <el-button @click="onGetPosts()" :icon="Search" />
      </template>
    </el-input>
    </el-col>
  </el-row>
    <PostList :key="postsKey"></PostList>
</template>

<script setup lang="ts">
  import {useAuthStore} from "@/stores/auth";
  import { Search } from '@element-plus/icons-vue'
  import {useRouter} from "vue-router";
  import {onBeforeMount, onMounted, onUnmounted, provide, ref} from "vue";
  import PostList from "@/components/post/PostList.vue";
  import axios from "axios";
  import {usePostsStore} from "@/stores/posts";
  import {usePageStore} from "@/stores/page";

  const auth=useAuthStore();
  const postsStore=usePostsStore();
  const pageStore=usePageStore();
  const router=useRouter();
  const selectCategory=ref(postsStore.getCategory);
  const postsKey=ref(0);

  const search=ref('');
  const select=ref('ALL');

  //마운트 되기전에 게시글을 불러옴
  onBeforeMount(async ()=> {
    await onGetPosts();
  });

  const onGetPosts=async function (){
    try {
      const category=selectCategory.value;
      const page=pageStore.getCurrentPage;
      const path=`?page=${page}&condition=${select.value}&keyword=${search.value}`;
      const response = await axios.get(`/api/${category}`+path);
      const postResponseDto = response.data;
      postsStore.clean();
      postsStore.addPost(postResponseDto.postListItems);
      pageStore.setMaxPage(postResponseDto.maxPage);
      postsKey.value++;
    } catch (error) {
      console.error(error);
    }
  }

  provide('onGetPosts',onGetPosts);
  const postWrite=function (){
    if(!auth.isLoggedIn){
      alert("로그인 해주세요")
    } else{
      router.push({name : 'write'});
    }
  }

</script>


<style scoped>

</style>