<template>
  <el-container class="layout">
    <el-aside width="232px" class="aside">
      <div class="logo">
        <div class="logo-badge">
          <el-icon><Lock /></el-icon>
        </div>
        <div class="logo-text">
          <span class="logo-title">RBAC</span>
          <span class="logo-sub">权限中心 / ACCESS GRID</span>
        </div>
      </div>

      <el-menu
        :default-active="$route.path"
        router
        class="side-menu"
      >
        <template v-for="m in menus" :key="m.id">
          <el-sub-menu v-if="m.children && m.children.length" :index="String(m.id)">
            <template #title>
              <el-icon v-if="m.icon"><component :is="m.icon" /></el-icon>
              <span>{{ m.name }}</span>
            </template>
            <el-menu-item v-for="c in m.children" :key="c.id" :index="c.path">
              <el-icon v-if="c.icon"><component :is="c.icon" /></el-icon>
              <span>{{ c.name }}</span>
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item v-else :index="m.path">
            <el-icon v-if="m.icon"><component :is="m.icon" /></el-icon>
            <span>{{ m.name }}</span>
          </el-menu-item>
        </template>
      </el-menu>

      <div class="aside-foot">
        <span class="dot" /> SYSTEM ONLINE
      </div>
    </el-aside>

    <el-container>
      <el-header class="header">
        <div class="header-left">
          <span class="crumb-label">LOCATION</span>
          <span class="crumb">/ {{ currentTitle || '面板' }}</span>
        </div>
        <div class="header-right">
          <el-dropdown @command="onSwitchRole" trigger="click">
            <span class="user-trigger">
              <el-avatar :size="30" class="user-avatar">{{ nameChar }}</el-avatar>
              <span class="uname">{{ name }} <em class="role-tag">{{ roleText }}</em></span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="admin"><el-icon><User /></el-icon> 切换为 管理员</el-dropdown-item>
                <el-dropdown-item command="user"><el-icon><UserFilled /></el-icon> 切换为 普通用户</el-dropdown-item>
                <el-dropdown-item divided command="logout"><el-icon><SwitchButton /></el-icon> 退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { authApi } from '@/api'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const menus = computed(() => userStore.menus || [])
const name = computed(() => userStore.name || userStore.username || '')
const nameChar = computed(() => (name.value ? name.value.charAt(0) : '?'))
const roleText = computed(() => (userStore.roles.includes('ADMIN') ? 'ADMIN' : 'USER'))
const currentTitle = computed(() => route.meta?.title || '')

onMounted(async () => {
  if (!userStore.menus.length && userStore.token) {
    try {
      const res = await authApi.info()
      if (res.code === 200) userStore.setUserInfo(res.data)
      else router.replace('/login')
    } catch (e) {
      router.replace('/login')
    }
  }
})

async function onSwitchRole(cmd) {
  if (cmd === 'logout') {
    userStore.logout()
    router.replace('/login')
    return
  }
  try {
    const res = await authApi.login(cmd, '123456')
    if (res.code !== 200) {
      ElMessage.error(res.message || '切换失败')
      return
    }
    userStore.setToken(res.data.token)
    userStore.setUserInfo(res.data.user)
    ElMessage.success(`已切换为 ${cmd}`)
    router.replace('/dashboard')
  } catch (e) {
    ElMessage.error('切换失败')
  }
}
</script>

<style scoped>
.layout { height: 100%; }

/* ---------- 侧边栏 ---------- */
.aside {
  position: relative;
  display: flex;
  flex-direction: column;
  background: linear-gradient(180deg, rgba(15, 10, 34, 0.92), rgba(11, 7, 24, 0.92));
  border-right: 1px solid var(--border-purple);
  backdrop-filter: blur(16px);
  box-shadow: 8px 0 28px rgba(0, 0, 0, 0.45);
}
.aside::after {
  content: '';
  position: absolute;
  top: 0; right: -1px;
  width: 1px; height: 100%;
  background: linear-gradient(180deg, transparent, var(--neon-magenta), transparent);
  box-shadow: var(--glow-magenta);
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 20px 18px 16px;
  border-bottom: 1px solid var(--border-purple);
}
.logo-badge {
  display: grid;
  place-items: center;
  width: 38px; height: 38px;
  border-radius: 10px;
  color: #fff;
  font-size: 20px;
  background: linear-gradient(135deg, var(--neon-magenta), var(--neon-purple));
  box-shadow: var(--glow-magenta);
}
.logo-text { display: flex; flex-direction: column; line-height: 1.2; }
.logo-title {
  font-family: 'Orbitron', sans-serif;
  font-weight: 900;
  font-size: 20px;
  color: #fff;
  letter-spacing: 3px;
  text-shadow: var(--glow-magenta);
}
.logo-sub {
  font-family: 'Rajdhani', sans-serif;
  font-size: 10px;
  letter-spacing: 2px;
  color: var(--text-muted);
}

.side-menu {
  flex: 1;
  border-right: none !important;
  background: transparent !important;
  padding: 10px 8px;
}
.side-menu :deep(.el-sub-menu__title),
.side-menu :deep(.el-menu-item) {
  color: var(--text-muted);
  border-radius: 9px;
  margin: 3px 6px;
  height: 46px;
  transition: all 0.2s ease;
}
.side-menu :deep(.el-sub-menu__title:hover),
.side-menu :deep(.el-menu-item:hover) {
  color: var(--text-strong);
  background: rgba(168, 85, 247, 0.12) !important;
}
.side-menu :deep(.el-menu-item.is-active) {
  color: #fff !important;
  background: linear-gradient(135deg, rgba(255, 43, 214, 0.28), rgba(168, 85, 247, 0.18)) !important;
  box-shadow: var(--glow-purple), inset 0 0 0 1px var(--border-neon);
}
.side-menu :deep(.el-menu-item.is-active::before) {
  content: '';
  position: absolute;
  left: -6px; top: 12px;
  width: 4px; height: 22px;
  background: var(--neon-magenta);
  border-radius: 2px;
  box-shadow: var(--glow-magenta);
}
.side-menu :deep(.el-icon) { font-size: 17px; }

.aside-foot {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 18px;
  font-family: 'Rajdhani', sans-serif;
  font-size: 11px;
  letter-spacing: 2px;
  color: var(--text-muted);
  border-top: 1px solid var(--border-purple);
}
.aside-foot .dot {
  width: 8px; height: 8px;
  border-radius: 50%;
  background: var(--neon-cyan);
  box-shadow: 0 0 8px var(--neon-cyan);
  animation: pulse 1.8s ease-in-out infinite;
}
@keyframes pulse { 0%,100% { opacity: 1; } 50% { opacity: 0.35; } }

/* ---------- 顶栏 ---------- */
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 60px;
  padding: 0 22px;
  background: rgba(11, 7, 24, 0.72);
  border-bottom: 1px solid var(--border-purple);
  backdrop-filter: blur(16px);
}
.header-left { display: flex; align-items: baseline; gap: 10px; }
.crumb-label {
  font-family: 'Rajdhani', sans-serif;
  font-size: 11px;
  letter-spacing: 2px;
  color: var(--neon-cyan);
}
.crumb {
  font-family: 'Orbitron', sans-serif;
  font-size: 14px;
  letter-spacing: 1px;
  color: var(--text-strong);
}
.header-right { display: flex; align-items: center; }

.user-trigger {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  outline: none;
  padding: 5px 12px;
  border-radius: 10px;
  border: 1px solid var(--border-purple);
  background: rgba(168, 85, 247, 0.08);
  transition: all 0.2s ease;
}
.user-trigger:hover {
  border-color: var(--neon-magenta);
  box-shadow: var(--glow-purple);
}
.user-avatar {
  background: linear-gradient(135deg, var(--neon-magenta), var(--neon-purple)) !important;
  font-family: 'Orbitron', sans-serif;
  color: #fff !important;
  box-shadow: var(--glow-magenta);
}
.uname { font-size: 14px; color: var(--text-strong); }
.role-tag {
  font-family: 'Rajdhani', sans-serif;
  font-style: normal;
  font-size: 10px;
  letter-spacing: 1px;
  padding: 1px 6px;
  margin-left: 4px;
  border-radius: 4px;
  color: var(--neon-cyan);
  border: 1px solid rgba(34, 224, 230, 0.4);
  background: rgba(34, 224, 230, 0.08);
}

/* ---------- 主区 ---------- */
.main {
  background: transparent !important;
  padding: 0;
}
</style>
