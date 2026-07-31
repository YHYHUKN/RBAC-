<template>
  <div class="page-container">
    <div class="toolbar">
      <h3>部门管理</h3>
    </div>
    <el-table
      :data="tree"
      border
      default-expand-all
      row-key="id"
      :tree-props="{ children: 'children' }"
    >
      <el-table-column prop="name" label="部门名称" />
      <el-table-column prop="leader" label="负责人" width="120" />
      <el-table-column prop="phone" label="联系电话" width="140" />
      <el-table-column prop="sort" label="排序" width="80" />
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button size="small" @click="onEdit(row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog" title="编辑部门" width="420px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="部门名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="form.leader" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" />
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
import { ElMessage } from 'element-plus'
import { deptApi } from '@/api'

const tree = ref([])
const dialog = ref(false)
const editId = ref(null)
const form = reactive({ name: '', leader: '', phone: '' })

async function load() {
  const res = await deptApi.list()
  tree.value = res.data
}
function onEdit(row) {
  editId.value = row.id
  Object.assign(form, { name: row.name, leader: row.leader, phone: row.phone })
  dialog.value = true
}
async function onSubmit() {
  await deptApi.update(editId.value, { ...form })
  ElMessage.success('已保存')
  dialog.value = false
  load()
}

onMounted(load)
</script>

<style scoped></style>
