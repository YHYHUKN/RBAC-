<template>
  <div class="page-container">
    <div class="toolbar">
      <h3>用户管理</h3>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增用户</el-button>
    </div>
    <el-table :data="list" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button size="small" @click="onEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
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

    <el-dialog v-model="dialog" :title="editId ? '编辑用户' : '新增用户'" width="460px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" :disabled="!!editId" />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog = false">取消</el-button>
        <el-button type="primary" @click="onSubmit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { userApi } from '@/api'

const list = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const dialog = ref(false)
const editId = ref(null)
const form = reactive({ username: '', name: '', email: '', phone: '', status: 1 })

async function load() {
  const res = await userApi.list(page.value, size.value)
  list.value = res.data.list
  total.value = res.data.total
}

function onPage(p) {
  page.value = p
  load()
}

function onAdd() {
  editId.value = null
  Object.assign(form, { username: '', name: '', email: '', phone: '', status: 1 })
  dialog.value = true
}

function onEdit(row) {
  editId.value = row.id
  Object.assign(form, {
    username: row.username,
    name: row.name,
    email: row.email,
    phone: row.phone,
    status: row.status
  })
  dialog.value = true
}

async function onSubmit() {
  if (editId.value) {
    await userApi.update(editId.value, { ...form })
    ElMessage.success('已更新')
  } else {
    await userApi.save({ ...form })
    ElMessage.success('已新增')
  }
  dialog.value = false
  load()
}

async function onDelete(row) {
  await ElMessageBox.confirm(`确认删除用户 ${row.username}？`, '提示', { type: 'warning' })
  await userApi.remove(row.id)
  ElMessage.success('已删除')
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
