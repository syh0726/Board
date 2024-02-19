<template>
  <div class="d-flex flex-column">
    <div>
      <el-table :data="postsStore.getPosts" @row-click="postRead">
        <el-table-column prop="id" label="글 번호"></el-table-column>
        <el-table-column prop="title" label="제목"></el-table-column>
        <el-table-column prop="nickName" label="작성자"> </el-table-column>
        <el-table-column prop="commentNum" label="댓글 수"></el-table-column>
        <el-table-column prop="viewCnt" label="조회수"></el-table-column>
        <el-table-column prop="likeCnt" label="좋아요"></el-table-column>
        <el-table-column prop="createdAt" label="작성시간"></el-table-column>
      </el-table>
    </div>
    <div>
        <Pagination :key="pagingKey"></Pagination>
    </div>
  </div>
</template>

<script setup lang="ts">
  import {useRouter} from "vue-router";
  import {usePostsStore} from "@/stores/posts";
  import Pagination from "@/components/pagination/pagination.vue";
  import {onMounted, onUnmounted, ref} from "vue";

  const router=useRouter();
  const postsStore=usePostsStore();
  const pagingKey=ref(0);

  onMounted(()=>{
    pagingKey.value++;
  })


  const postRead=(row:{id : number}) =>{
    postsStore.setPostId(row.id);
    router.push({name :'post',params:{postId:row.id}});
  }
</script>

<style scoped>

</style>