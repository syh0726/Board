<template>
  <el-row>
    <h3>[{{post.category}}] {{post.title}}</h3>
  </el-row>
  <el-row>
    <span @click="router.push({name:'member-activity',params:{memberId:post.authorId}})" style="margin-right: 3px">작성자:{{post.nickName}} </span>
    | 조회:{{post.viewCnt}}
    | 추천:{{post.likeCnt}}
    | 작성일: {{post.createdAt}}
  </el-row>
  <el-row :style="{minHeight:'300px'}">
    <h3>{{post.content}}</h3>
  </el-row>
  <el-row class="d-flex justify-content-center align-items-center mb-2">
    <el-button type="primary" @click="onPostLike" plain>추천</el-button>
    <el-button >{{post.likeCnt}}</el-button>
    <el-button type="danger" @click="onPostDisLike" plain>비추천</el-button>
  </el-row>
  <el-row v-if="isPostAuthor()" class="d-flex justify-content-end">
    <el-button type="primary" @click="editPost()">수정</el-button>
    <el-button type="danger" @click="onPostDelete">삭제</el-button>
  </el-row>
  <div class="line mt-3" style="border:solid 0.5px var(--el-border-color);"></div>

  <div class="mb-3">댓글:{{commentsNum}}개</div>
  <ul style="width:100%; padding-inline-start: 0; margin-block:0;">
    <li v-for="comment in comments" :key="comment.id">
      <div class="mb-3 d-flex justify-content-between">
        <div>
          <span style="font-weight: bold; font-size: 1.2em;" >{{comment.nickName}}</span>
          <span style="font-size: small; margin-left:8px;">{{comment.createdAt}}</span>
        </div>
        <div v-if="isCommentAuthor(comment)" class="d-flex">
          <el-button type="primary" @click="toggleCommentEdit(comment)" size="small">수정</el-button>
          <el-button type="danger" @click="onCommentDelete(comment)" size="small">삭제</el-button>
        </div>

      </div>
      <div class="mb-3" >{{comment.content}}</div>

      <div v-if="comment.isEdit">
        <el-input type="textarea"
                  v-model="editContent.content"
                  maxlength="1000"
                  show-word-limit
                  placeholder="댓글을 입력해주세요"
                  :autosize="{minRows: 3}">
        </el-input>
        <el-button type="primary" @click="onCommentEdit(comment)" >댓글 수정</el-button>
      </div>

      <div class="line" style="border:solid 0.5px var(--el-border-color);"></div>
    </li>
  </ul>

  <el-input type="textarea"
            v-model="writeContent.content"
            maxlength="1000"
            show-word-limit
            placeholder="댓글을 입력해주세요"
            :autosize="{minRows: 3}">
  </el-input>
  <el-button type="primary" @click="onCommentWrite" >댓글 작성</el-button>
</template>

<script setup lang="ts">
import {onMounted, provide, ref} from "vue";
import axios from "axios";
import {usePostsStore} from "@/stores/posts";
import {useAuthStore} from "@/stores/auth";
import {useRoute, useRouter} from "vue-router";

const postsStore=usePostsStore();
const authStore=useAuthStore();
const route=useRoute();
const router=useRouter();
const postId=route.params.postId;

const isPostAuthor=function (){
  if(authStore.isLoggedIn){
    if(post.value.nickName===authStore.getProfile.nickName||authStore.getProfile.role==='관리자'){
      return true;
    }
  }else{
    return false;
  }
}

const isCommentAuthor=function(comment:any){
  if(authStore.isLoggedIn){
    if(comment.nickName===authStore.getProfile.nickName||authStore.getProfile.role==='관리자'){
      return true;
    }
  }else{
    return false;
  }
}

const post=ref({
  id:0,
  title:'',
  content:'',
  category:'',
  nickName:'',
  viewCnt:0,
  likeCnt:0,
  createdAt:'',
  authorId:0
})

const comments=ref([{
  id:0,
  content:'',
  nickName:'',
  createdAt:'',
  isEdit:false,
}])

//댓글 작성시 보낼 내용물
const writeContent= ref({
  content:'',
});
const editContent= ref({
  content:'',
});

const commentsNum=ref(0);


onMounted(async ()=> {
  try {
    const response = await axios.get(`/api/posts/${postId}`);
    const postResponseDto = response.data;
    const commentResponseDto=postResponseDto.commentListResponseDto;
    post.value=postResponseDto.postResponseData;

    comments.value=commentResponseDto.commentListItems;
    commentsNum.value=commentResponseDto.commentsNum;
  }catch (error){
    console.error(error);
  }
});

const editPost=()=>{
  postsStore.setPostId(post.value.id);
  router.push({name:'post-edit',params:{postId:post.value.id}});
}

const onPostLike=async function (){
  try {
    const likes=await axios.post(`/api/posts/${postId}/like`, {}, {
      withCredentials: true
    });
    post.value.likeCnt=likes.data.likeCnt;
  }catch (error){
    alert("추천 불가능");
  }
}
const onPostDisLike=async function (){
  try {
    const likes=await axios.post(`/api/posts/${postId}/dislike`, {}, {
      withCredentials: true
    });
    post.value.likeCnt=likes.data.likeCnt;
  }catch (error){
    alert("비추천 불가능");
  }
}

const onPostDelete= async function (){
  try{
    await axios.delete(`/api/posts/${postId}`,{
      withCredentials: true
    });

    alert("게시글 삭제 완료");
    router.replace({name:'home'});

  }catch (error){
    alert("삭제 실패");
  }
}

const onCommentWrite=async function () {
  try {
    const response=await axios.post(`/api/posts/${postId}/comments`,writeContent.value, {
      withCredentials: true
    });
    const commentResponseDto=response.data;

    writeContent.value.content= '';
    comments.value=commentResponseDto.commentListItems;
    commentsNum.value=commentResponseDto.commentsNum;
  }catch (error){
    console.log(error);
    alert("댓글 작성 실패");
  }
}
const toggleCommentEdit= function(comment:any){
  comment.isEdit= !comment.isEdit;
}
const onCommentEdit=async function(comment:any){
  try{
    const response=await axios.patch(`/api/posts/${postId}/comments/${comment.id}`,editContent.value,{
      withCredentials: true
    })
    const commentResponseDto=response.data;

    editContent.value.content= '';
    comments.value=commentResponseDto.commentListItems;
    commentsNum.value=commentResponseDto.commentsNum;
  }catch (error){
    alert("댓글 수정 실패");
  }
}


const onCommentDelete=async function (comment:any){
  try{
    const response=await axios.delete(`/api/posts/${postId}/comments/${comment.id}`,{
      withCredentials: true
    })
    const commentResponseDto=response.data;

    writeContent.value.content = '';
    comments.value=commentResponseDto.commentListItems;
    commentsNum.value=commentResponseDto.commentsNum;
  }
  catch (error){
    alert("댓글 삭제 실패");
  }
}

</script>


<style scoped>
ul{
  list-style:none;
}
li {
  margin-bottom: 1.5rem;
}
</style>