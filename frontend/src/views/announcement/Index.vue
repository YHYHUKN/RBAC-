<template>
  <div class="page-container">
    <div class="toolbar">
      <h3>公告管理 / ANNOUNCEMENTS</h3>
      <div class="toolbar-actions">
        <el-input v-model="keyword" placeholder="搜索标题" :prefix-icon="Search" clearable size="small" style="width: 200px" @keyup.enter="load" />
        <el-button type="primary" :icon="Plus" size="small" @click="openForm()">发布新公告</el-button>
      </div>
    </div>

    <el-table :data="list" v-loading="loading" stripe>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
      <el-table-column prop="type" label="类型" width="100" />
      <el-table-column label="优先级" width="100">
        <template #default="{ row }">
          <el-tag :type="row.priority >= 3 ? 'danger' : row.priority === 2 ? 'warning' : 'info'" effect="dark">
            P{{ row.priority }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="publisher" label="发布人" width="110" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" effect="dark">
            {{ row.status === 1 ? '已发布' : '草稿' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="170" />
      <el-table-column label="操作" width="260" fixed="right">
        <template #default="{ row }">
          <el-button v-if="row.status === 0" size="small" :icon="Upload" type="primary" @click="publish(row)">发布</el-button>
          <el-button v-else size="small" :icon="Download" @click="unpublish(row)">撤回</el-button>
          <el-button size="small" :icon="Edit" @click="openForm(row)">编辑</el-button>
          <el-button size="small" type="danger" :icon="Delete" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="mt">
      <el-pagination
        background
        layout="total, prev, pager, next"
        :total="total"
        :page-size="size"
        :current-page="page"
        @current-change="onPage"
      />
    </div>

    <el-dialog v-model="dialog" :title="form.id ? '编辑公告' : '新建公告'" width="560px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="公告标题" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="form.type" placeholder="选择类型" style="width: 100%">
            <el-option label="公告" value="公告" />
            <el-option label="通知" value="通知" />
            <el-option label="活动" value="活动" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="form.priority" style="width: 100%">
            <el-option label="P1 普通" :value="1" />
            <el-option label="P2 重要" :value="2" />
            <el-option label="P3 紧急" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" :rows="4" placeholder="公告正文" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog = false">取消</el-button>
        <el-button type="primary" @click="submit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Upload, Download, Edit, Delete } from '@element-plus/icons-vue'
import { announcementApi } from '@/api'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const keyword = ref('')

const dialog = ref(false)
const form = reactive({ id: null, title: '', type: '通知', priority: 1, content: '' })

async function load() {
  loading.value = true
  try {
    const res = await announcementApi.list(keyword.value, page.value, size.value)
    if (res.code === 200) {
      list.value = res.data.list
      total.value = res.data.total
    }
  } finally {
    loading.value = false
  }
}

function onPage(p) { page.value = p; load() }

function openForm(row) {
  if (row) Object.assign(form, row)
  else Object.assign(form, { id: null, title: '', type: '通知', priority: 1, content: '' })
  dialog.value = true
}

async function submit() {
  if (!form.title || !form.content) { ElMessage.warning('请填写标题与内容'); return }
  const res = await announcementApi.save({ ...form })
  if (res.code === 200) { ElMessage.success('已保存'); dialog.value = false; load() }
}

async function publish(row) { await announcementApi.publish(row.id); ElMessage.success('已发布'); load() }
async function unpublish(row) { await announcementApi.unpublish(row.id); ElMessage.success('已撤回'); load() }
async function remove(row) {
  await ElMessageBox.confirm(`确认删除公告「${row.title}」？`, '提示', { type: 'warning' })
  const res = await announcementApi.remove(row.id)
  if (res.code === 200) { ElMessage.success('已删除'); load() }
}

load()
</script>

<style scoped>
.toolbar-actions { display: flex; align-items: center; gap: 10px; }
</style>
