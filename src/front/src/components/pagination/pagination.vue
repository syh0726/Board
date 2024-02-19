
<template>
  <div class="d-flex justify-content-center mt-3">
    <el-pagination background layout="prev, pager, next"
                   :page-size:="pageSize"
                   :pager-count="11"
                   :total="maxPage*pageSize"
                   prev-text="Previous"
                   next-text="Next"
                   v-model:current-page="currentPage"
                   @click="onPaging(currentPage)"
    />
  </div>
</template>

<script setup lang="ts">
import {inject, onMounted, ref} from "vue";
import {usePageStore} from "@/stores/page";
import {usePostsStore} from "@/stores/posts";

  const pageStore = usePageStore();
  const maxPage=ref(0);
  const currentPage=ref(1);
  const pageSize=10;
  const onGetPosts:any = inject('onGetPosts');


  onMounted(()=>{
    maxPage.value=pageStore.getMaxPage;
    currentPage.value=pageStore.getCurrentPage;
  })
  const onPaging =async (page:number) => {
    pageStore.setCurrentPage(page);
    await onGetPosts();
  }


</script>
<style scoped>

</style>