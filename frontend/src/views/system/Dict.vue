<template>
  <div class="page-container">
    <el-row :gutter="16">
      <el-col :xs="24" :lg="10">
        <el-card shadow="never" class="panel">
          <div class="toolbar">
            <h3>字典类型</h3>
            <el-button type="primary" :icon="Plus" @click="onAddType">新增类型</el-button>
          </div>
          <el-table :data="types" highlight-current-row border @current-change="onSelectType" height="460">
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="type" label="编码" />
            <el-table-column label="操作" width="140">
              <template #default="{ row }">
                <el-button size="small" @click="onEditType(row)">编辑</el-button>
                <el-button size="small" type="danger" @click="onDelType(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="14">
        <el-card shadow="never" class="panel">
          <div class="toolbar">
            <h3>字典数据{{ selectedType ? '：' + selectedType.name : '' }}</h3>
            <el-button type="primary" :icon="Plus" :disabled="!selectedType" @click="onAddData">新增数据</el-button>
          </div>
          <el-alert
            v-if="!selectedType"
            title="请先在左侧选择字典类型"
            type="info"
            :closable="false"
            class="empty-tip"
          />
          <el-table v-else :data="dataList" border height="412">
            <el-table-column prop="label" label="标签" />
            <el-table-column prop="value" label="值" />
            <el-table-column prop="sort" label="排序" width="80" />
            <el-table-column label="操作" width="140">
              <template #default="{ row }">
                <el-button size="small" @click="onEditData(row)">编辑</el-button>
                <el-button size="small" type="danger" @click="onDelData(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="typeDialog" :title="typeEditId ? '编辑类型' : '新增类型'" width="440px">
      <el-form :model="typeForm" label-width="80px">
        <el-form-item label="名称"><el-input v-model="typeForm.name" /></el-form-item>
        <el-form-item label="编码"><el-input v-model="typeForm.type" :disabled="!!typeEditId" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="typeForm.remark" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="typeDialog = false">取消</el-button>
        <el-button type="primary" @click="submitType">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="dataDialog" :title="dataEditId ? '编辑数据' : '新增数据'" width="440px">
      <el-form :model="dataForm" label-width="80px">
        <el-form-item label="标签"><el-input v-model="dataForm.label" /></el-form-item>
        <el-form-item label="值"><el-input v-model="dataForm.value" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="dataForm.sort" :min="0" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dataDialog = false">取消</el-button>
        <el-button type="primary" @click="submitData">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { dictApi } from '@/api'

const types = ref([])
const selectedType = ref(null)
const dataList = ref([])
const typeDialog = ref(false)
const typeEditId = ref(null)
const typeForm = reactive({ name: '', type: '', remark: '' })
const dataDialog = ref(false)
const dataEditId = ref(null)
const dataForm = reactive({ label: '', value: '', sort: 1 })

async function loadTypes() {
  const res = await dictApi.types('', 1, 100)
  types.value = res.data.list
}
async function loadData() {
  if (!selectedType.value) {
    dataList.value = []
    return
  }
  const res = await dictApi.data(selectedType.value.type)
  dataList.value = res.data
}
function onSelectType(row) {
  selectedType.value = row
  loadData()
}

function onAddType() {
  typeEditId.value = null
  Object.assign(typeForm, { name: '', type: '', remark: '' })
  typeDialog.value = true
}
function onEditType(row) {
  typeEditId.value = row.id
  Object.assign(typeForm, { name: row.name, type: row.type, remark: row.remark })
  typeDialog.value = true
}
async function submitType() {
  await dictApi.saveType({ ...typeForm, id: typeEditId.value || undefined })
  ElMessage.success('已保存')
  typeDialog.value = false
  loadTypes()
}
async function onDelType(row) {
  await ElMessageBox.confirm(`确认删除字典类型「${row.name}」？`, '提示', { type: 'warning' })
  await dictApi.deleteType(row.id)
  ElMessage.success('已删除')
  if (selectedType.value?.id === row.id) selectedType.value = null
  loadTypes()
}

function onAddData() {
  dataEditId.value = null
  Object.assign(dataForm, { label: '', value: '', sort: 1 })
  dataDialog.value = true
}
function onEditData(row) {
  dataEditId.value = row.id
  Object.assign(dataForm, { label: row.label, value: row.value, sort: row.sort })
  dataDialog.value = true
}
async function submitData() {
  await dictApi.saveData({ ...dataForm, type: selectedType.value.type, id: dataEditId.value || undefined })
  ElMessage.success('已保存')
  dataDialog.value = false
  loadData()
}
async function onDelData(row) {
  await ElMessageBox.confirm('确认删除该字典数据？', '提示', { type: 'warning' })
  await dictApi.deleteData(row.id)
  ElMessage.success('已删除')
  loadData()
}

onMounted(loadTypes)
</script>

<style scoped>
.panel {
  border-radius: 12px;
}
.empty-tip {
  margin-top: 8px;
}
</style>
