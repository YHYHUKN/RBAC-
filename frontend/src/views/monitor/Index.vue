<template>
  <div class="page-container">
    <div class="toolbar">
      <h3>系统监控 / SYSTEM MONITOR</h3>
      <div class="toolbar-actions">
        <el-button size="small" :icon="Refresh" :loading="loading" @click="load">刷新</el-button>
        <el-switch
          v-model="auto"
          inline-prompt
          active-text="自动"
          inactive-text="手动"
          style="--el-switch-on-color: #ff2bd6; margin-left: 4px"
          @change="toggleAuto"
        />
      </div>
    </div>

    <!-- 资源环形仪表 -->
    <div class="gauge-grid">
      <div class="panel gauge-card">
        <div class="ring" :style="{ '--p': cpu + '%' }">
          <div class="ring-inner">
            <span class="ring-val">{{ cpu }}<i>%</i></span>
            <span class="ring-label">CPU 负载</span>
          </div>
        </div>
      </div>
      <div class="panel gauge-card">
        <div class="ring ring-cyan" :style="{ '--p': memory.usage + '%' }">
          <div class="ring-inner">
            <span class="ring-val">{{ memory.usage }}<i>%</i></span>
            <span class="ring-label">内存 {{ memory.used }} / {{ memory.total }} GB</span>
          </div>
        </div>
      </div>
      <div class="panel gauge-card">
        <div class="ring ring-purple" :style="{ '--p': disk.usage + '%' }">
          <div class="ring-inner">
            <span class="ring-val">{{ disk.usage }}<i>%</i></span>
            <span class="ring-label">磁盘 {{ disk.used }} / {{ disk.total }} GB</span>
          </div>
        </div>
      </div>
      <div class="panel gauge-card">
        <div class="ring" :style="{ '--p': jvm.usage + '%' }">
          <div class="ring-inner">
            <span class="ring-val">{{ jvm.usage }}<i>%</i></span>
            <span class="ring-label">JVM {{ jvm.used }} / {{ jvm.total }} MB</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 状态卡片 -->
    <div class="stat-row">
      <div class="stat-card panel">
        <div class="stat-icon"><el-icon><Cpu /></el-icon></div>
        <div>
          <div class="stat-value">{{ threads.total }}</div>
          <div class="stat-label">活跃线程 / PEAK {{ threads.peak }}</div>
        </div>
      </div>
      <div class="stat-card panel">
        <div class="stat-icon cyan"><el-icon><User /></el-icon></div>
        <div>
          <div class="stat-value">{{ onlineUsers }}</div>
          <div class="stat-label">在线会话 / SESSIONS</div>
        </div>
      </div>
      <div class="stat-card panel">
        <div class="stat-icon purple"><el-icon><Monitor /></el-icon></div>
        <div>
          <div class="stat-value" style="font-size: 20px">{{ runtime }}</div>
          <div class="stat-label">运行时长 / UPTIME</div>
        </div>
      </div>
      <div class="stat-card panel">
        <div class="stat-icon"><el-icon><Platform /></el-icon></div>
        <div>
          <div class="stat-value" style="font-size: 18px">{{ os }}</div>
          <div class="stat-label">操作系统 / OS</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onUnmounted, reactive, ref } from 'vue'
import { Refresh, Cpu, User, Monitor, Platform } from '@element-plus/icons-vue'
import { monitorApi } from '@/api'

const loading = ref(false)
const auto = ref(false)
let timer = null

const cpu = ref(0)
const memory = reactive({ used: 0, total: 0, usage: 0 })
const disk = reactive({ used: 0, total: 0, usage: 0 })
const jvm = reactive({ used: 0, total: 0, usage: 0 })
const threads = reactive({ total: 0, peak: 0, daemon: 0 })
const onlineUsers = ref(0)
const runtime = ref('-')
const os = ref('-')

async function load() {
  loading.value = true
  try {
    const res = await monitorApi.info()
    if (res.code === 200) {
      const d = res.data
      cpu.value = d.cpu
      Object.assign(memory, d.memory)
      Object.assign(disk, d.disk)
      Object.assign(jvm, d.jvm)
      Object.assign(threads, d.threads)
      onlineUsers.value = d.onlineUsers
      runtime.value = d.runtime
      os.value = d.os
    }
  } finally {
    loading.value = false
  }
}

function toggleAuto(v) {
  if (v) timer = setInterval(load, 5000)
  else if (timer) clearInterval(timer)
}

onUnmounted(() => timer && clearInterval(timer))
load()
</script>

<style scoped>
.toolbar-actions { display: flex; align-items: center; }

.gauge-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 16px;
}
@media (max-width: 1080px) { .gauge-grid { grid-template-columns: repeat(2, 1fr); } }
.gauge-card {
  display: grid;
  place-items: center;
  padding: 22px 10px;
}

.ring {
  --p: 0%;
  width: 146px; height: 146px;
  border-radius: 50%;
  background: conic-gradient(var(--neon-magenta) var(--p), rgba(168, 85, 247, 0.12) 0);
  display: grid;
  place-items: center;
  position: relative;
  filter: drop-shadow(0 0 10px rgba(255, 43, 214, 0.4));
}
.ring.ring-cyan { background: conic-gradient(var(--neon-cyan) var(--p), rgba(34, 224, 230, 0.12) 0); filter: drop-shadow(0 0 10px rgba(34, 224, 230, 0.4)); }
.ring.ring-purple { background: conic-gradient(var(--neon-purple) var(--p), rgba(168, 85, 247, 0.12) 0); filter: drop-shadow(0 0 10px rgba(168, 85, 247, 0.4)); }
.ring::before {
  content: '';
  position: absolute;
  inset: 13px;
  border-radius: 50%;
  background: radial-gradient(circle at 50% 35%, #1a1030, #0b0718);
  border: 1px solid var(--border-purple);
}
.ring-inner { position: relative; z-index: 1; text-align: center; padding: 0 8px; }
.ring-val {
  font-family: 'Orbitron', sans-serif;
  font-size: 30px;
  font-weight: 700;
  color: #fff;
  text-shadow: var(--glow-magenta);
}
.ring-val i { font-size: 14px; font-style: normal; margin-left: 2px; color: var(--text-muted); }
.ring-label { display: block; margin-top: 4px; font-size: 11px; color: var(--text-muted); letter-spacing: 0.5px; }

.stat-row { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; }
@media (max-width: 1080px) { .stat-row { grid-template-columns: repeat(2, 1fr); } }
.stat-card { display: flex; align-items: center; gap: 14px; padding: 18px 20px; }
.stat-icon {
  display: grid;
  place-items: center;
  width: 46px; height: 46px;
  border-radius: 12px;
  font-size: 22px;
  color: #fff;
  background: linear-gradient(135deg, var(--neon-magenta), var(--neon-purple));
  box-shadow: var(--glow-magenta);
}
.stat-icon.cyan { background: linear-gradient(135deg, var(--neon-cyan), #1388b0); box-shadow: 0 0 12px rgba(34, 224, 230, 0.5); }
.stat-icon.purple { background: linear-gradient(135deg, var(--neon-purple), #5b21b6); box-shadow: var(--glow-purple); }
</style>
