
<template>
  <post-list-header :key="route.fullPath"></post-list-header>
</template>

<script setup lang="ts">
import PostListHeader from "@/components/post/PostListHeader.vue";
import {useRoute} from "vue-router";

const route=useRoute();

</script>


<style scoped>

</style>