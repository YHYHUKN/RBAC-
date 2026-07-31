// 本地 Mock 数据层：用于 CloudStudio / 离线场景的完整演示。
// 数据结构与后端 RBAC 接口保持一致，保证切换到真实后端时前端无需改动。

const MENUS_ADMIN = [
  { id: 1, parentId: 0, name: '仪表盘', type: 1, path: '/dashboard', component: 'views/dashboard/index', icon: 'Odometer', permission: null, sort: 1, children: [] },
  {
    id: 2, parentId: 0, name: '系统管理', type: 0, path: '/system', component: null, icon: 'Setting', permission: null, sort: 2,
    children: [
      { id: 3, parentId: 2, name: '用户管理', type: 1, path: '/system/user', component: 'views/system/user', icon: 'User', permission: 'sys:user', sort: 1, children: [] },
      { id: 4, parentId: 2, name: '角色管理', type: 1, path: '/system/role', component: 'views/system/role', icon: 'Role', permission: 'sys:role', sort: 2, children: [] },
      { id: 5, parentId: 2, name: '菜单管理', type: 1, path: '/system/menu', component: 'views/system/menu', icon: 'Menu', permission: 'sys:menu', sort: 3, children: [] },
      { id: 6, parentId: 2, name: '部门管理', type: 1, path: '/system/dept', component: 'views/system/dept', icon: 'OfficeBuilding', permission: 'sys:dept', sort: 4, children: [] },
      { id: 8, parentId: 2, name: '数据字典', type: 1, path: '/system/dict', component: 'views/system/dict', icon: 'Collection', permission: 'sys:dict', sort: 5, children: [] },
      { id: 9, parentId: 2, name: '文件管理', type: 1, path: '/system/file', component: 'views/system/file', icon: 'Folder', permission: 'sys:file', sort: 6, children: [] }
    ]
  },
  { id: 7, parentId: 0, name: '日志管理', type: 0, path: '/logs', component: null, icon: 'Document', permission: null, sort: 3, children: [] },
  { id: 10, parentId: 0, name: '个人中心', type: 1, path: '/profile', component: 'views/profile/index', icon: 'UserFilled', permission: null, sort: 4, children: [] },
  { id: 11, parentId: 0, name: '系统监控', type: 1, path: '/monitor', component: 'views/monitor/index', icon: 'Monitor', permission: null, sort: 5, children: [] },
  { id: 12, parentId: 0, name: '定时任务', type: 1, path: '/job', component: 'views/job/index', icon: 'Timer', permission: null, sort: 6, children: [] },
  { id: 13, parentId: 0, name: '公告管理', type: 1, path: '/announcement', component: 'views/announcement/index', icon: 'Bell', permission: null, sort: 7, children: [] }
]

const MENUS_USER = [
  { id: 1, parentId: 0, name: '仪表盘', type: 1, path: '/dashboard', component: 'views/dashboard/index', icon: 'Odometer', permission: null, sort: 1, children: [] },
  { id: 10, parentId: 0, name: '个人中心', type: 1, path: '/profile', component: 'views/profile/index', icon: 'UserFilled', permission: null, sort: 4, children: [] }
]

const ROLES = [
  { id: 1, code: 'ADMIN', name: '管理员', remark: '超级管理员，拥有全部权限', menus: MENUS_ADMIN.filter((m) => m.id !== 1), createTime: '2026-07-31 10:00:00' },
  { id: 2, code: 'USER', name: '普通用户', remark: '仅可访问仪表盘与个人中心', menus: [], createTime: '2026-07-31 10:00:00' }
]

const DEPTS = [
  { id: 1, name: '总公司', parentId: 0, leader: '张三', phone: '13800000000', sort: 1, children: [
    { id: 2, name: '技术部', parentId: 1, leader: '李四', phone: '13800000001', sort: 1, children: [] },
    { id: 3, name: '市场部', parentId: 1, leader: '王五', phone: '13800000002', sort: 2, children: [] }
  ] }
]

let USERS = [
  { id: 1, username: 'admin', name: '超级管理员', email: 'admin@example.com', phone: '13800000000', status: 1, deptId: 1, roles: [{ id: 1, code: 'ADMIN', name: '管理员', remark: '', menus: [], createTime: '2026-07-31 10:00:00' }] },
  { id: 2, username: 'user', name: '普通用户', email: 'user@example.com', phone: '13800000003', status: 1, deptId: 2, roles: [{ id: 2, code: 'USER', name: '普通用户', remark: '', menus: [], createTime: '2026-07-31 10:00:00' }] },
  { id: 3, username: 'zhangsan', name: '张三', email: 'zhang@example.com', phone: '13800000004', status: 1, deptId: 2, roles: [{ id: 2, code: 'USER', name: '普通用户', remark: '', menus: [], createTime: '2026-07-31 10:00:00' }] },
  { id: 4, username: 'lisi', name: '李四', email: 'li@example.com', phone: '13800000005', status: 1, deptId: 2, roles: [{ id: 2, code: 'USER', name: '普通用户', remark: '', menus: [], createTime: '2026-07-31 10:00:00' }] },
  { id: 5, username: 'wangwu', name: '王五', email: 'wang@example.com', phone: '13800000006', status: 0, deptId: 3, roles: [{ id: 2, code: 'USER', name: '普通用户', remark: '', menus: [], createTime: '2026-07-31 10:00:00' }] }
]
let userIdSeq = 6

const LOGS = [
  { id: 1, username: 'admin', action: '登录系统', method: 'POST', params: '{}', ip: '127.0.0.1', status: 'SUCCESS', createTime: '2026-07-31 22:40:01' },
  { id: 2, username: 'user', action: '查询用户列表', method: 'GET', params: '{}', ip: '127.0.0.1', status: 'SUCCESS', createTime: '2026-07-31 22:41:12' },
  { id: 3, username: 'admin', action: '新增用户', method: 'POST', params: '{}', ip: '127.0.0.1', status: 'SUCCESS', createTime: '2026-07-31 22:42:30' }
]
let logIdSeq = 4

// ===== 数据字典 =====
let DICT_TYPES = [
  { id: 1, name: '用户状态', type: 'user_status', remark: '账号启停状态', createTime: '2026-07-31 11:00:00' },
  { id: 2, name: '通知类型', type: 'notice_type', remark: '系统通知分类', createTime: '2026-07-31 11:05:00' },
  { id: 3, name: '性别', type: 'gender', remark: '人员性别', createTime: '2026-07-31 11:10:00' }
]
let dictTypeSeq = 4

let DICT_DATA = [
  { id: 1, type: 'user_status', label: '启用', value: '1', sort: 1 },
  { id: 2, type: 'user_status', label: '禁用', value: '0', sort: 2 },
  { id: 3, type: 'notice_type', label: '系统公告', value: 'NOTICE', sort: 1 },
  { id: 4, type: 'notice_type', label: '活动通知', value: 'ACTIVITY', sort: 2 },
  { id: 5, type: 'gender', label: '男', value: 'M', sort: 1 },
  { id: 6, type: 'gender', label: '女', value: 'F', sort: 2 }
]
let dictDataSeq = 7

// ===== 文件管理 =====
let FILES = [
  { id: 1, originalName: '需求文档.pdf', size: 204800, type: 'application/pdf', path: '/uploads/需求文档.pdf', uploader: 'admin', createTime: '2026-07-31 15:10:00' },
  { id: 2, originalName: '架构图.png', size: 512000, type: 'image/png', path: '/uploads/架构图.png', uploader: 'admin', createTime: '2026-07-31 15:12:00' },
  { id: 3, originalName: '测试用例.xlsx', size: 102400, type: 'application/vnd.ms-excel', path: '/uploads/测试用例.xlsx', uploader: 'lisi', createTime: '2026-07-31 16:00:00' }
]
let fileIdSeq = 4

// ===== 定时任务 =====
let JOBS = [
  { id: 1, jobName: 'demoHeartbeat', jobGroup: 'DEFAULT', cron: '0/30 * * * * ?', description: '演示任务：每 30 秒打印一次心跳', status: 1, beanName: 'sampleJob', createTime: '2026-07-31 18:00:00' },
  { id: 2, jobName: 'dailyReport', jobGroup: 'DEFAULT', cron: '0 0 1 * * ?', description: '每日凌晨生成运营报表', status: 1, beanName: 'sampleJob', createTime: '2026-07-31 18:05:00' },
  { id: 3, jobName: 'clearLogs', jobGroup: 'DEFAULT', cron: '0 0 2 * * ?', description: '每日凌晨清理 30 天前的操作日志', status: 0, beanName: 'sampleJob', createTime: '2026-07-31 18:10:00' }
]
let jobIdSeq = 4
let jobRunSeq = 1
const JOB_LOGS = []

// ===== 公告 =====
let ANNOUNCEMENTS = [
  { id: 1, title: '系统升级维护通知', type: '公告', content: '为提升系统稳定性，将于本周六凌晨进行升级维护，预计耗时 2 小时。', publisher: 'admin', status: 1, priority: 3, createTime: '2026-07-31 09:00:00', publishTime: '2026-07-31 09:30:00' },
  { id: 2, title: '新功能上线：定时任务', type: '通知', content: '管理后台新增定时任务模块，支持任务的增删改查与暂停/恢复/立即执行。', publisher: 'admin', status: 1, priority: 2, createTime: '2026-07-31 14:00:00', publishTime: '2026-07-31 14:20:00' },
  { id: 3, title: '季度团建报名', type: '通知', content: '请各部门于周五前在系统内完成团建报名。', publisher: 'admin', status: 0, priority: 1, createTime: '2026-07-31 17:00:00', publishTime: null }
]
let announcementIdSeq = 4

const dayTrend = () => {
  const labels = []
  const values = []
  for (let i = 6; i >= 0; i--) {
    const d = new Date(Date.now() - i * 86400000)
    labels.push(`${d.getMonth() + 1}/${d.getDate()}`)
    values.push(Math.floor(20 + Math.random() * 80))
  }
  return { labels, values }
}

function ok(data) {
  return { code: 200, message: 'success', data }
}

function fail(message) {
  return { code: 500, message }
}

function buildUserInfo(username) {
  if (username === 'admin') {
    return { username: 'admin', name: '超级管理员', roles: ['ADMIN'], menus: MENUS_ADMIN }
  }
  return { username: 'user', name: '普通用户', roles: ['USER'], menus: MENUS_USER }
}

function extractId(url, prefix) {
  const m = url.match(new RegExp(`^${prefix}/(\\d+)`))
  return m ? Number(m[1]) : null
}

function now() {
  const d = new Date()
  const p = (n) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())} ${p(d.getHours())}:${p(d.getMinutes())}:${p(d.getSeconds())}`
}

export async function mockRequest({ url, method, data, params }) {
  await new Promise((r) => setTimeout(r, 120))
  const u = url || ''
  const m = (method || 'get').toLowerCase()

  // 登录
  if (u === '/api/auth/login' && m === 'post') {
    const body = data || {}
    if ((body.username === 'admin' || body.username === 'user') && body.password === '123456') {
      const info = buildUserInfo(body.username)
      return ok({ token: `mock-token-${body.username}`, user: info })
    }
    return fail('用户名或密码错误（演示账号：admin/user，密码 123456）')
  }

  // 当前用户信息
  if (u === '/api/auth/info' && m === 'get') {
    const token = localStorage.getItem('token') || ''
    const username = token.includes('admin') ? 'admin' : 'user'
    return ok(buildUserInfo(username))
  }

  // 个人中心
  if (u === '/api/profile' && m === 'get') {
    const token = localStorage.getItem('token') || ''
    const username = token.includes('admin') ? 'admin' : 'user'
    const u0 = USERS.find((x) => x.username === username) || USERS[0]
    return ok({
      user: { username: u0.username, name: u0.name, email: u0.email, phone: u0.phone, status: u0.status },
      stats: { loginCount: 128, lastLogin: now(), todoCount: 3, msgCount: 5 }
    })
  }

  // 看板统计
  if (u === '/api/dashboard/stats' && m === 'get') {
    const trend = dayTrend()
    return ok({
      userCount: USERS.length,
      roleCount: ROLES.length,
      menuCount: MENUS_ADMIN.length + MENUS_ADMIN[1].children.length,
      logCount: LOGS.length,
      trend,
      roleDist: ROLES.map((r) => ({ name: r.name, value: USERS.filter((x) => x.roles.some((y) => y.code === r.code)).length }))
    })
  }

  // 用户列表
  if (u === '/api/users' && m === 'get') {
    const page = Number(params?.page || 1)
    const size = Number(params?.size || 10)
    const start = (page - 1) * size
    return ok({ total: USERS.length, list: USERS.slice(start, start + size) })
  }
  if (u === '/api/users' && m === 'post') {
    const body = data || {}
    const newUser = { id: userIdSeq++, ...body, roles: [{ id: 2, code: 'USER', name: '普通用户', remark: '', menus: [], createTime: now() }] }
    USERS.push(newUser)
    LOGS.unshift({ id: logIdSeq++, username: 'admin', action: '新增用户', method: 'POST', params: '{}', ip: '127.0.0.1', status: 'SUCCESS', createTime: now() })
    return ok(newUser)
  }
  let uid = extractId(u, '/api/users')
  if (uid && m === 'put') {
    const idx = USERS.findIndex((x) => x.id === uid)
    if (idx >= 0) USERS[idx] = { ...USERS[idx], ...(data || {}) }
    return ok(USERS[idx])
  }
  if (uid && m === 'delete') {
    USERS = USERS.filter((x) => x.id !== uid)
    return ok(true)
  }

  // 角色
  if (u === '/api/roles' && m === 'get') return ok(ROLES)
  let rid = extractId(u, '/api/roles')
  if (rid && m === 'put') {
    const idx = ROLES.findIndex((x) => x.id === rid)
    if (idx >= 0) ROLES[idx] = { ...ROLES[idx], ...(data || {}) }
    return ok(ROLES[idx])
  }
  if (rid && m === 'delete') return ok(true)

  // 菜单树
  if (u === '/api/menus/tree' && m === 'get') return ok(MENUS_ADMIN)

  // 部门
  if (u === '/api/depts' && m === 'get') return ok(DEPTS)
  let did = extractId(u, '/api/depts')
  if (did && m === 'put') return ok(true)

  // 日志
  if (u === '/api/logs' && m === 'get') {
    const page = Number(params?.page || 1)
    const size = Number(params?.size || 10)
    const start = (page - 1) * size
    return ok({ total: LOGS.length, list: LOGS.slice(start, start + size) })
  }

  // ===== 数据字典 =====
  if (u === '/api/dict/types' && m === 'get') {
    const keyword = (params?.keyword || '').toLowerCase()
    const list = keyword
      ? DICT_TYPES.filter((t) => t.name.toLowerCase().includes(keyword) || t.type.toLowerCase().includes(keyword))
      : DICT_TYPES
    const page = Number(params?.page || 1)
    const size = Number(params?.size || 10)
    const start = (page - 1) * size
    return ok({ total: list.length, list: list.slice(start, start + size) })
  }
  if (u === '/api/dict/data' && m === 'get') {
    const type = params?.type || ''
    return ok(DICT_DATA.filter((d) => d.type === type).sort((a, b) => a.sort - b.sort))
  }
  if (u === '/api/dict/type' && m === 'post') {
    const body = data || {}
    if (body.id) {
      const idx = DICT_TYPES.findIndex((t) => t.id === body.id)
      DICT_TYPES[idx] = { ...DICT_TYPES[idx], ...body }
      return ok(DICT_TYPES[idx])
    }
    const t = { id: dictTypeSeq++, createTime: now(), ...body }
    DICT_TYPES.push(t)
    return ok(t)
  }
  if (u === '/api/dict/data' && m === 'post') {
    const body = data || {}
    if (body.id) {
      const idx = DICT_DATA.findIndex((d) => d.id === body.id)
      DICT_DATA[idx] = { ...DICT_DATA[idx], ...body }
      return ok(DICT_DATA[idx])
    }
    const d = { id: dictDataSeq++, ...body }
    DICT_DATA.push(d)
    return ok(d)
  }
  let dtId = extractId(u, '/api/dict/type')
  if (dtId && m === 'delete') {
    DICT_TYPES = DICT_TYPES.filter((t) => t.id !== dtId)
    return ok(true)
  }
  let ddId = extractId(u, '/api/dict/data')
  if (ddId && m === 'delete') {
    DICT_DATA = DICT_DATA.filter((d) => d.id !== ddId)
    return ok(true)
  }

  // ===== 文件管理 =====
  if (u === '/api/files' && m === 'get') {
    const name = (params?.name || '').toLowerCase()
    const list = name ? FILES.filter((f) => f.originalName.toLowerCase().includes(name)) : FILES
    return ok(list)
  }
  if (u === '/api/files' && m === 'post') {
    const body = data || {}
    const f = {
      id: fileIdSeq++,
      originalName: body.originalName || `文件${fileIdSeq}.dat`,
      size: body.size || Math.floor(10000 + Math.random() * 900000),
      type: body.type || 'application/octet-stream',
      path: `/uploads/${body.originalName || 'file'}`,
      uploader: body.uploader || 'admin',
      createTime: now()
    }
    FILES.unshift(f)
    return ok(f)
  }
  let fid = extractId(u, '/api/files')
  if (fid && m === 'delete') {
    FILES = FILES.filter((f) => f.id !== fid)
    return ok(true)
  }

  // ===== 系统监控 =====
  if (u === '/api/monitor' && m === 'get') {
    const cpu = +(20 + Math.random() * 60).toFixed(1)
    const memUsed = +(1 + Math.random() * 3).toFixed(2)
    const memTotal = 8
    const diskUsed = +(20 + Math.random() * 40).toFixed(1)
    const jvmUsed = +(120 + Math.random() * 200).toFixed(0)
    return ok({
      cpu,
      memory: { used: memUsed, total: memTotal, usage: +((memUsed / memTotal) * 100).toFixed(1) },
      disk: { used: diskUsed, total: 100, usage: diskUsed },
      jvm: { used: jvmUsed, total: 512, usage: +((jvmUsed / 512) * 100).toFixed(1) },
      threads: { total: 24 + Math.floor(Math.random() * 8), peak: 40, daemon: 18 },
      os: 'Windows 11',
      onlineUsers: 3,
      runtime: '3h 12m'
    })
  }

  // ===== 定时任务 =====
  if (u === '/api/job/list' && m === 'get') {
    const keyword = (params?.keyword || '').toLowerCase()
    const list = keyword ? JOBS.filter((j) => j.jobName.toLowerCase().includes(keyword)) : JOBS
    const page = Number(params?.page || 1)
    const size = Number(params?.size || 10)
    const start = (page - 1) * size
    return ok({ total: list.length, list: list.slice(start, start + size) })
  }
  if (u === '/api/job' && m === 'post') {
    const body = data || {}
    if (body.id) {
      const idx = JOBS.findIndex((j) => j.id === body.id)
      JOBS[idx] = { ...JOBS[idx], ...body }
      return ok(JOBS[idx])
    }
    const j = { id: jobIdSeq++, createTime: now(), status: 1, jobGroup: 'DEFAULT', beanName: 'sampleJob', ...body }
    JOBS.push(j)
    return ok(j)
  }
  let jid = extractId(u, '/api/job')
  if (jid && m === 'delete') {
    JOBS = JOBS.filter((j) => j.id !== jid)
    return ok(true)
  }
  if (jid && u.endsWith('/pause') && m === 'post') {
    const j = JOBS.find((x) => x.id === jid)
    if (j) j.status = 0
    return ok(true)
  }
  if (jid && u.endsWith('/resume') && m === 'post') {
    const j = JOBS.find((x) => x.id === jid)
    if (j) j.status = 1
    return ok(true)
  }
  if (jid && u.endsWith('/run') && m === 'post') {
    JOB_LOGS.unshift({ id: jobRunSeq++, jobName: JOBS.find((x) => x.id === jid)?.jobName, time: now(), result: 'SUCCESS' })
    return ok(true)
  }

  // ===== 公告 =====
  if (u === '/api/announcement/list' && m === 'get') {
    const keyword = (params?.keyword || '').toLowerCase()
    const list = keyword ? ANNOUNCEMENTS.filter((a) => a.title.toLowerCase().includes(keyword)) : ANNOUNCEMENTS
    const page = Number(params?.page || 1)
    const size = Number(params?.size || 10)
    const start = (page - 1) * size
    return ok({ total: list.length, list: list.slice(start, start + size) })
  }
  if (u === '/api/announcement' && m === 'post') {
    const body = data || {}
    if (body.id) {
      const idx = ANNOUNCEMENTS.findIndex((a) => a.id === body.id)
      ANNOUNCEMENTS[idx] = { ...ANNOUNCEMENTS[idx], ...body }
      return ok(ANNOUNCEMENTS[idx])
    }
    const a = { id: announcementIdSeq++, createTime: now(), status: 0, priority: 1, type: '通知', ...body }
    ANNOUNCEMENTS.unshift(a)
    return ok(a)
  }
  let aid = extractId(u, '/api/announcement')
  if (aid && m === 'delete') {
    ANNOUNCEMENTS = ANNOUNCEMENTS.filter((a) => a.id !== aid)
    return ok(true)
  }
  if (aid && u.endsWith('/publish') && m === 'post') {
    const a = ANNOUNCEMENTS.find((x) => x.id === aid)
    if (a) { a.status = 1; a.publishTime = now() }
    return ok(true)
  }
  if (aid && u.endsWith('/unpublish') && m === 'post') {
    const a = ANNOUNCEMENTS.find((x) => x.id === aid)
    if (a) { a.status = 0; a.publishTime = null }
    return ok(true)
  }

  return fail(`Mock 未实现接口: ${m.toUpperCase()} ${u}`)
}
