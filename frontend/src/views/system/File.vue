<template>
  <div class="page-container">
    <el-card shadow="never" class="panel">
      <div class="toolbar">
        <h3>文件管理</h3>
        <el-upload
          :auto-upload="false"
          :show-file-list="false"
          :on-change="onFileChange"
          multiple
        >
          <el-button type="primary" :icon="Upload">上传文件</el-button>
        </el-upload>
      </div>

      <el-table :data="list" border>
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column label="文件名">
          <template #default="{ row }">
            <el-icon class="ficon"><Document /></el-icon>
            <span class="fname">{{ row.originalName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="大小" width="120">
          <template #default="{ row }">{{ formatSize(row.size) }}</template>
        </el-table-column>
        <el-table-column prop="uploader" label="上传者" width="120" />
        <el-table-column prop="createTime" label="上传时间" width="180" />
        <el-table-column label="操作" width="140">
          <template #default="{ row }">
            <el-button size="small" type="primary" plain @click="onDownload(row)">下载</el-button>
            <el-button size="small" type="danger" @click="onDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { Upload, Document } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { fileApi } from '@/api'

const list = ref([])

async function load() {
  const res = await fileApi.list('')
  list.value = res.data
}

function formatSize(bytes) {
  if (!bytes) return '0 B'
  const units = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(1024))
  return `${(bytes / Math.pow(1024, i)).toFixed(1)} ${units[i]}`
}

async function onFileChange(file) {
  await fileApi.upload({
    originalName: file.name,
    size: file.size,
    type: file.raw?.type || 'application/octet-stream',
    uploader: 'admin'
  })
  ElMessage.success(`已上传 ${file.name}`)
  load()
}

function onDownload(row) {
  ElMessage.info(`演示环境：下载 ${row.originalName}`)
}

async function onDelete(row) {
  await ElMessageBox.confirm(`确认删除文件「${row.originalName}」？`, '提示', { type: 'warning' })
  await fileApi.remove(row.id)
  ElMessage.success('已删除')
  load()
}

onMounted(load)
</script>

<style scoped>
.panel {
  border-radius: 12px;
}
.ficon {
  vertical-align: middle;
  margin-right: 6px;
  color: #2563eb;
}
.fname {
  vertical-align: middle;
}
</style>
