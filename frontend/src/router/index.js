import { createRouter, createWebHashHistory } from 'vue-router'
import Layout from '@/layout/Layout.vue'
import Login from '@/views/Login.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { public: true }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/Index.vue'),
        meta: { title: '仪表盘' }
      },
      {
        path: 'system/user',
        name: 'User',
        component: () => import('@/views/system/User.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'system/role',
        name: 'Role',
        component: () => import('@/views/system/Role.vue'),
        meta: { title: '角色管理' }
      },
      {
        path: 'system/menu',
        name: 'Menu',
        component: () => import('@/views/system/Menu.vue'),
        meta: { title: '菜单管理' }
      },
      {
        path: 'system/dept',
        name: 'Dept',
        component: () => import('@/views/system/Dept.vue'),
        meta: { title: '部门管理' }
      },
      {
        path: 'logs',
        name: 'Logs',
        component: () => import('@/views/logs/Index.vue'),
        meta: { title: '操作日志' }
      },
      {
        path: 'system/dict',
        name: 'Dict',
        component: () => import('@/views/system/Dict.vue'),
        meta: { title: '数据字典' }
      },
      {
        path: 'system/file',
        name: 'File',
        component: () => import('@/views/system/File.vue'),
        meta: { title: '文件管理' }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/profile/Index.vue'),
        meta: { title: '个人中心' }
      },
      {
        path: 'monitor',
        name: 'Monitor',
        component: () => import('@/views/monitor/Index.vue'),
        meta: { title: '系统监控' }
      },
      {
        path: 'job',
        name: 'Job',
        component: () => import('@/views/job/Index.vue'),
        meta: { title: '定时任务' }
      },
      {
        path: 'announcement',
        name: 'Announcement',
        component: () => import('@/views/announcement/Index.vue'),
        meta: { title: '公告管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.public) {
    next()
  } else if (!token) {
    next('/login')
  } else {
    next()
  }
})

export default router
