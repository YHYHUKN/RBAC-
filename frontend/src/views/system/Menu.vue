<template>
  <div class="page-container">
    <div class="toolbar">
      <h3>菜单管理</h3>
    </div>
    <el-table
      :data="tree"
      border
      default-expand-all
      row-key="id"
      :tree-props="{ children: 'children' }"
    >
      <el-table-column prop="name" label="菜单名称" />
      <el-table-column prop="path" label="路由路径" />
      <el-table-column label="图标" width="100">
        <template #default="{ row }">
          <el-icon v-if="row.icon"><component :is="row.icon" /></el-icon>
        </template>
      </el-table-column>
      <el-table-column prop="sort" label="排序" width="80" />
      <el-table-column prop="permission" label="权限标识" />
    </el-table>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { menuApi } from '@/api'

const tree = ref([])
onMounted(async () => {
  const res = await menuApi.tree()
  tree.value = res.data
})
</script>

<style scoped></style>
