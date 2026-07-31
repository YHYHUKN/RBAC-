<template>
  <div class="cyber-bg"></div>
  <router-view />
</template>

<script setup></script>

<style>
/* 确保路由内容位于动态背景之上 */
#app > .cyber-bg { position: fixed; }
#app > router-view,
#app > *:not(.cyber-bg) {
  position: relative;
  z-index: 1;
}
</style>
