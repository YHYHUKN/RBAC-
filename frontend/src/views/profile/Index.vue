<template>
  <div class="page-container">
    <el-row :gutter="16">
      <el-col :xs="24" :lg="8">
        <el-card shadow="hover" class="profile-card">
          <div class="avatar-wrap">
            <el-avatar :size="84" class="avatar">{{ nameChar }}</el-avatar>
            <h2 class="pname">{{ info.name || info.username }}</h2>
            <el-tag :type="roleTag" effect="dark">{{ roleText }}</el-tag>
            <p class="pmeta">{{ info.username }}</p>
          </div>
          <el-divider />
          <ul class="quick">
            <li><span>登录次数</span><b>{{ stats.loginCount }}</b></li>
            <li><span>待办事项</span><b>{{ stats.todoCount }}</b></li>
            <li><span>未读消息</span><b>{{ stats.msgCount }}</b></li>
          </ul>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="16">
        <el-card shadow="hover" class="profile-card">
          <div class="toolbar">
            <h3>基本资料</h3>
            <el-button type="primary" :icon="Edit" @click="onSave">保存修改</el-button>
          </div>
          <el-descriptions :column="2" border class="desc">
            <el-descriptions-item label="用户名">{{ info.username }}</el-descriptions-item>
            <el-descriptions-item label="姓名">
              <el-input v-model="info.name" size="small" class="inline-input" />
            </el-descriptions-item>
            <el-descriptions-item label="邮箱">
              <el-input v-model="info.email" size="small" class="inline-input" />
            </el-descriptions-item>
            <el-descriptions-item label="手机号">
              <el-input v-model="info.phone" size="small" class="inline-input" />
            </el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="info.status === 1 ? 'success' : 'info'">
                {{ info.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="最近登录">{{ stats.lastLogin }}</el-descriptions-item>
          </el-descriptions>

          <el-alert
            class="tip"
            title="演示环境：修改仅作用于本地状态，不会持久化到后端。"
            type="warning"
            :closable="false"
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref, computed } from 'vue'
import { Edit } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { profileApi } from '@/api'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const info = reactive({ username: '', name: '', email: '', phone: '', status: 1 })
const stats = reactive({ loginCount: 0, todoCount: 0, msgCount: 0, lastLogin: '' })

const nameChar = computed(() => (info.name || info.username || '?').charAt(0))
const roleText = computed(() => (userStore.roles.includes('ADMIN') ? '管理员' : '普通用户'))
const roleTag = computed(() => (userStore.roles.includes('ADMIN') ? 'danger' : 'info'))

async function load() {
  const res = await profileApi.info()
  const d = res.data
  Object.assign(info, d.user)
  Object.assign(stats, d.stats)
}

function onSave() {
  ElMessage.success('资料已更新（演示）')
}

onMounted(load)
</script>

<style scoped>
.profile-card {
  border-radius: 12px;
}
.avatar-wrap {
  text-align: center;
  padding: 12px 0;
}
.avatar {
  background: linear-gradient(135deg, #2563eb, #7c3aed);
}
.pname {
  margin: 12px 0 8px;
  font-size: 20px;
}
.pmeta {
  color: #9ca3af;
  font-size: 13px;
  margin: 6px 0 0;
}
.quick {
  list-style: none;
  padding: 0;
  margin: 0;
}
.quick li {
  display: flex;
  justify-content: space-between;
  padding: 12px 4px;
  border-bottom: 1px solid #f1f5f9;
  font-size: 14px;
  color: #4b5563;
}
.quick li b {
  color: #1f2937;
  font-size: 16px;
}
.inline-input {
  max-width: 220px;
}
.tip {
  margin-top: 16px;
}
</style>
