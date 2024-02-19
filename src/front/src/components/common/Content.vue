<script setup lang="ts">

import {RouterLink} from "vue-router";
</script>

<template>
  <el-main class="content">
        <RouterView/>
  </el-main>
</template>

<style scoped>
.content {
  height: 100%;
  width: 90%;
  max-width: 980px;
  margin: 40px auto 0 auto;

}
</style>