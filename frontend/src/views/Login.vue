<template>
  <div class="login-wrap">
    <!-- 左侧品牌展示面板 -->
    <div class="brand-panel">
      <div class="brand-grid"></div>
      <div class="brand-content">
        <div class="brand-logo">
          <el-icon><Lock /></el-icon>
        </div>
        <h1 class="brand-title">RBAC<span>// ACCESS GRID</span></h1>
        <p class="brand-desc">
          企业级角色权限控制中心<br />
          基于 Spring Boot + Vue3 的全栈权限治理平台
        </p>
        <ul class="brand-feats">
          <li><el-icon><Key /></el-icon> 角色 / 菜单 / 部门 多维权限编排</li>
          <li><el-icon><Monitor /></el-icon> 服务监控 · 在线会话 · 任务调度</li>
          <li><el-icon><DataLine /></el-icon> 操作审计 · 数据字典 · 文件中枢</li>
        </ul>
      </div>
      <div class="brand-foot">v3.3 · SECURE KERNEL ONLINE</div>
    </div>

    <!-- 右侧登录卡片 -->
    <div class="login-side">
      <el-card class="login-card tech-corner" shadow="always">
        <div class="card-head">
          <div class="card-bar"></div>
          <div class="title">身份核验 / SIGN IN</div>
          <div class="subtitle">Vibe Coding 全栈演示 · 请完成身份验证</div>
        </div>

        <el-form :model="form" @submit.prevent="onSubmit">
          <el-form-item>
            <el-input v-model="form.username" placeholder="用户名 / USERNAME" :prefix-icon="User" size="large" />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="form.password"
              type="password"
              placeholder="密码 / PASSWORD"
              :prefix-icon="Lock"
              size="large"
              show-password
              @keyup.enter="onSubmit"
            />
          </el-form-item>
          <el-button type="primary" :loading="loading" class="login-btn" size="large" @click="onSubmit">
            <span class="btn-glow"></span> 登 录 / ENTER
          </el-button>
        </el-form>

        <div class="tips">
          演示账号：<b>admin</b> / <b>user</b> &nbsp;·&nbsp; 密码均为 <b>123456</b>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock, Key, Monitor, DataLine } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { authApi } from '@/api'

const router = useRouter()
const userStore = useUserStore()
const form = reactive({ username: 'admin', password: '123456' })
const loading = ref(false)

async function onSubmit() {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    const res = await authApi.login(form.username, form.password)
    if (res.code !== 200) {
      ElMessage.error(res.message || '登录失败')
      return
    }
    userStore.setToken(res.data.token)
    userStore.setUserInfo(res.data.user)
    ElMessage.success('登录成功')
    router.replace('/dashboard')
  } catch (e) {
    ElMessage.error(e?.response?.data?.message || e?.message || '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-wrap {
  height: 100%;
  display: flex;
  align-items: stretch;
  justify-content: center;
  position: relative;
  z-index: 1;
}

/* ---------- 左侧品牌面板 ---------- */
.brand-panel {
  position: relative;
  flex: 1;
  max-width: 560px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 0 56px;
  overflow: hidden;
  border-right: 1px solid var(--border-purple);
  background: linear-gradient(135deg, rgba(255, 43, 214, 0.10), rgba(124, 58, 237, 0.06));
}
.brand-grid {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(168, 85, 247, 0.12) 1px, transparent 1px),
    linear-gradient(90deg, rgba(168, 85, 247, 0.12) 1px, transparent 1px);
  background-size: 40px 40px;
  mask-image: linear-gradient(120deg, #000, transparent 80%);
  -webkit-mask-image: linear-gradient(120deg, #000, transparent 80%);
}
.brand-content { position: relative; z-index: 1; }
.brand-logo {
  display: grid;
  place-items: center;
  width: 56px; height: 56px;
  font-size: 28px;
  color: #fff;
  border-radius: 14px;
  background: linear-gradient(135deg, var(--neon-magenta), var(--neon-purple));
  box-shadow: var(--glow-magenta);
  margin-bottom: 22px;
}
.brand-title {
  font-family: 'Orbitron', sans-serif;
  font-size: 38px;
  font-weight: 900;
  letter-spacing: 2px;
  color: #fff;
  text-shadow: var(--glow-magenta);
  margin: 0 0 14px;
}
.brand-title span {
  display: block;
  font-size: 14px;
  letter-spacing: 6px;
  color: var(--neon-cyan);
  margin-top: 6px;
}
.brand-desc {
  color: var(--text-base);
  font-size: 15px;
  line-height: 1.7;
  margin: 0 0 26px;
}
.brand-feats { list-style: none; padding: 0; margin: 0; }
.brand-feats li {
  display: flex;
  align-items: center;
  gap: 10px;
  color: var(--text-muted);
  font-size: 14px;
  padding: 8px 0;
}
.brand-feats .el-icon {
  color: var(--neon-magenta);
  font-size: 18px;
  filter: drop-shadow(var(--glow-magenta));
}
.brand-foot {
  position: relative;
  z-index: 1;
  margin-top: 40px;
  font-family: 'Rajdhani', sans-serif;
  font-size: 11px;
  letter-spacing: 3px;
  color: var(--text-muted);
}
@media (max-width: 880px) {
  .brand-panel { display: none; }
}

/* ---------- 右侧登录区 ---------- */
.login-side {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}
.login-card {
  width: 400px;
  max-width: 92vw;
  padding: 14px 26px 26px;
  position: relative;
}
.card-head { text-align: center; margin: 16px 0 22px; }
.card-bar {
  width: 46px; height: 4px;
  margin: 0 auto 16px;
  border-radius: 2px;
  background: linear-gradient(90deg, var(--neon-magenta), var(--neon-purple));
  box-shadow: var(--glow-magenta);
}
.title {
  font-family: 'Orbitron', sans-serif;
  font-size: 22px;
  font-weight: 700;
  letter-spacing: 1px;
  color: var(--text-strong);
  text-shadow: var(--glow-magenta);
}
.subtitle {
  margin-top: 8px;
  color: var(--text-muted);
  font-size: 13px;
  letter-spacing: 0.5px;
}
.login-btn {
  width: 100%;
  position: relative;
  overflow: hidden;
  letter-spacing: 2px;
  font-weight: 700;
}
.btn-glow {
  position: absolute;
  inset: 0;
  background: linear-gradient(120deg, transparent 30%, rgba(255, 255, 255, 0.35) 50%, transparent 70%);
  transform: translateX(-120%);
  animation: sheen 3.5s ease-in-out infinite;
}
@keyframes sheen { 0%, 60% { transform: translateX(-120%); } 100% { transform: translateX(120%); } }
.tips {
  margin-top: 18px;
  text-align: center;
  font-size: 13px;
  color: var(--text-muted);
}
.tips b { color: var(--neon-cyan); }
</style>
