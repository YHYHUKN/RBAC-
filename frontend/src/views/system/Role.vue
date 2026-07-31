<template>
  <div class="page-container">
    <div class="toolbar">
      <h3>角色管理</h3>
    </div>
    <el-table :data="list" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="code" label="角色编码" width="120" />
      <el-table-column prop="name" label="角色名称" />
      <el-table-column prop="remark" label="备注" />
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button size="small" @click="onPerm(row)">分配权限</el-button>
          <el-button size="small" type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog" title="分配菜单权限" width="420px">
      <el-tree
        ref="treeRef"
        :data="menuTree"
        :props="{ label: 'name', children: 'children' }"
        show-checkbox
        node-key="id"
        :default-checked-keys="checkedKeys"
      />
      <template #footer>
        <el-button @click="dialog = false">取消</el-button>
        <el-button type="primary" @click="onSubmitPerm">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { roleApi, menuApi } from '@/api'

const list = ref([])
const menuTree = ref([])
const dialog = ref(false)
const currentRole = ref(null)
const checkedKeys = ref([])
const treeRef = ref(null)

async function load() {
  const res = await roleApi.list()
  list.value = res.data
}
async function loadMenus() {
  const res = await menuApi.tree()
  menuTree.value = res.data
}

function onPerm(row) {
  currentRole.value = row
  checkedKeys.value = (row.menus || []).map((m) => m.id)
  dialog.value = true
}
async function onSubmitPerm() {
  const keys = treeRef.value.getCheckedKeys()
  await roleApi.update(currentRole.value.id, { menuIds: keys })
  ElMessage.success('权限已更新')
  dialog.value = false
  load()
}
async function onDelete(row) {
  await ElMessageBox.confirm(`确认删除角色 ${row.name}？`, '提示', { type: 'warning' })
  await roleApi.remove(row.id)
  ElMessage.success('已删除')
  load()
}

onMounted(async () => {
  await load()
  await loadMenus()
})
</script>

<style scoped></style>
