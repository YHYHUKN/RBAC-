<template>
  <div class="page-container">
    <div class="toolbar">
      <h3>操作日志</h3>
    </div>
    <el-table :data="list" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="username" label="操作用户" width="120" />
      <el-table-column prop="action" label="操作" />
      <el-table-column prop="method" label="方法" width="90" />
      <el-table-column prop="ip" label="IP" width="130" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 'SUCCESS' ? 'success' : 'danger'">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="时间" width="180" />
    </el-table>
    <el-pagination
      class="mt"
      background
      layout="prev,pager,next,total"
      :total="total"
      :current-page="page"
      :page-size="size"
      @current-change="onPage"
    />
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { logApi } from '@/api'

const list = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(10)

async function load() {
  const res = await logApi.list(page.value, size.value)
  list.value = res.data.list
  total.value = res.data.total
}
function onPage(p) {
  page.value = p
  load()
}

onMounted(load)
</script>

<style scoped>
.mt {
  margin-top: 16px;
  justify-content: flex-end;
}
</style>
