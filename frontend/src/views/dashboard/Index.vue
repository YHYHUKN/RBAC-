<template>
  <div class="page-container">
    <div class="stat-cards">
      <el-card v-for="s in stats" :key="s.label" shadow="hover" class="stat-card">
        <div class="stat-label">{{ s.label }}</div>
        <div class="stat-value">{{ s.value }}</div>
      </el-card>
    </div>
    <el-row :gutter="16">
      <el-col :xs="24" :lg="14">
        <el-card shadow="hover">
          <div ref="trendRef" style="height: 320px"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="10">
        <el-card shadow="hover">
          <div ref="pieRef" style="height: 320px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { onMounted, onBeforeUnmount, ref, shallowRef } from 'vue'
import * as echarts from 'echarts'
import { dashboardApi } from '@/api'

const stats = ref([])
const trendRef = ref(null)
const pieRef = ref(null)
const trendChart = shallowRef(null)
const pieChart = shallowRef(null)

async function load() {
  const res = await dashboardApi.stats()
  const d = res.data
  stats.value = [
    { label: '用户总数', value: d.userCount },
    { label: '角色数量', value: d.roleCount },
    { label: '菜单数量', value: d.menuCount },
    { label: '日志条数', value: d.logCount }
  ]
  renderTrend(d.trend)
  renderPie(d.roleDist)
}

function renderTrend(trend) {
  trendChart.value = echarts.init(trendRef.value)
  const magenta = '#ff2bd6'
  trendChart.value.setOption({
    title: { text: '近 7 日访问趋势', left: 'center', textStyle: { color: '#f3ecff', fontFamily: 'Orbitron', fontSize: 14 } },
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(20,13,40,0.92)',
      borderColor: 'rgba(255,43,214,0.38)',
      textStyle: { color: '#cfc4ee' }
    },
    grid: { left: 44, right: 20, top: 52, bottom: 30 },
    xAxis: {
      type: 'category',
      data: trend.labels,
      boundaryGap: false,
      axisLine: { lineStyle: { color: 'rgba(168,85,247,0.4)' } },
      axisLabel: { color: '#8c7fb8' }
    },
    yAxis: {
      type: 'value',
      axisLabel: { color: '#8c7fb8' },
      splitLine: { lineStyle: { color: 'rgba(168,85,247,0.12)' } }
    },
    series: [
      {
        name: '访问量',
        type: 'line',
        smooth: true,
        data: trend.values,
        symbol: 'circle',
        symbolSize: 7,
        itemStyle: { color: magenta },
        lineStyle: { color: magenta, width: 3, shadowColor: 'rgba(255,43,214,0.6)', shadowBlur: 12 },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(255,43,214,0.45)' },
            { offset: 1, color: 'rgba(255,43,214,0.02)' }
          ])
        }
      }
    ]
  })
}

function renderPie(dist) {
  pieChart.value = echarts.init(pieRef.value)
  const palette = ['#ff2bd6', '#a855f7', '#22e0e6', '#7c3aed', '#ff6be8']
  pieChart.value.setOption({
    title: { text: '角色分布', left: 'center', textStyle: { color: '#f3ecff', fontFamily: 'Orbitron', fontSize: 14 } },
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(20,13,40,0.92)',
      borderColor: 'rgba(255,43,214,0.38)',
      textStyle: { color: '#cfc4ee' }
    },
    legend: { bottom: 0, textStyle: { color: '#cfc4ee' } },
    series: [
      {
        name: '角色',
        type: 'pie',
        radius: ['38%', '62%'],
        center: ['50%', '48%'],
        data: dist,
        label: { color: '#cfc4ee', formatter: '{b}: {c}' },
        itemStyle: {
          borderColor: 'rgba(11,7,24,0.9)',
          borderWidth: 2,
          shadowBlur: 16,
          shadowColor: 'rgba(168,85,247,0.5)'
        },
        color: palette
      }
    ]
  })
}

function resize() {
  trendChart.value?.resize()
  pieChart.value?.resize()
}

onMounted(async () => {
  await load()
  window.addEventListener('resize', resize)
})
onBeforeUnmount(() => {
  window.removeEventListener('resize', resize)
  trendChart.value?.dispose()
  pieChart.value?.dispose()
})
</script>

<style scoped>
.stat-card {
  border-radius: 10px;
}
.stat-label {
  color: #6b7280;
  font-size: 13px;
  margin-bottom: 8px;
}
.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #1f2937;
}
</style>
