<template>
  <div class="page-container">
    <div class="toolbar">
      <h3>定时任务 / SCHEDULED JOBS</h3>
      <div class="toolbar-actions">
        <el-input v-model="keyword" placeholder="搜索任务名" :prefix-icon="Search" clearable size="small" style="width: 200px" @keyup.enter="load" />
        <el-button type="primary" :icon="Plus" size="small" @click="openForm()">新建任务</el-button>
      </div>
    </div>

    <el-table :data="list" v-loading="loading" stripe>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="jobName" label="任务名称" min-width="150" />
      <el-table-column prop="jobGroup" label="分组" width="110" />
      <el-table-column prop="cron" label="Cron 表达式" min-width="150">
        <template #default="{ row }"><code class="cron">{{ row.cron }}</code></template>
      </el-table-column>
      <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" effect="dark">
            {{ row.status === 1 ? '运行中' : '已暂停' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="170" />
      <el-table-column label="操作" width="240" fixed="right">
        <template #default="{ row }">
          <el-button v-if="row.status === 1" size="small" :icon="VideoPause" @click="pause(row)">暂停</el-button>
          <el-button v-else size="small" :icon="VideoPlay" @click="resume(row)">恢复</el-button>
          <el-button size="small" :icon="Promotion" @click="run(row)">执行</el-button>
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

    <el-dialog v-model="dialog" :title="form.id ? '编辑任务' : '新建任务'" width="520px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="任务名称">
          <el-input v-model="form.jobName" placeholder="如 demoHeartbeat" />
        </el-form-item>
        <el-form-item label="分组">
          <el-input v-model="form.jobGroup" placeholder="DEFAULT" />
        </el-form-item>
        <el-form-item label="Cron">
          <el-input v-model="form.cron" placeholder="0/30 * * * * ?" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
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
import { Search, Plus, VideoPause, VideoPlay, Promotion, Edit, Delete } from '@element-plus/icons-vue'
import { jobApi } from '@/api'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const keyword = ref('')

const dialog = ref(false)
const form = reactive({ id: null, jobName: '', jobGroup: 'DEFAULT', cron: '', description: '' })

async function load() {
  loading.value = true
  try {
    const res = await jobApi.list(keyword.value, page.value, size.value)
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
  else Object.assign(form, { id: null, jobName: '', jobGroup: 'DEFAULT', cron: '', description: '' })
  dialog.value = true
}

async function submit() {
  if (!form.jobName || !form.cron) { ElMessage.warning('请填写任务名称与 Cron'); return }
  const res = await jobApi.save({ ...form })
  if (res.code === 200) { ElMessage.success('已保存'); dialog.value = false; load() }
}

async function pause(row) { await jobApi.pause(row.id); ElMessage.success('已暂停'); load() }
async function resume(row) { await jobApi.resume(row.id); ElMessage.success('已恢复'); load() }
async function run(row) {
  await jobApi.run(row.id)
  ElMessage.success(`任务「${row.jobName}」已触发执行`)
}
async function remove(row) {
  await ElMessageBox.confirm(`确认删除任务「${row.jobName}」？`, '提示', { type: 'warning' })
  const res = await jobApi.remove(row.id)
  if (res.code === 200) { ElMessage.success('已删除'); load() }
}

load()
</script>

<style scoped>
.toolbar-actions { display: flex; align-items: center; gap: 10px; }
.cron {
  font-family: 'Rajdhani', monospace;
  color: var(--neon-cyan);
  background: rgba(34, 224, 230, 0.08);
  padding: 2px 8px;
  border-radius: 6px;
  border: 1px solid rgba(34, 224, 230, 0.25);
}
</style>
