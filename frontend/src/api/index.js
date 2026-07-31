import request from '@/utils/request'

export const authApi = {
  login: (username, password) =>
    request({ url: '/api/auth/login', method: 'post', data: { username, password } }),
  info: () => request({ url: '/api/auth/info', method: 'get' })
}

export const dashboardApi = {
  stats: () => request({ url: '/api/dashboard/stats', method: 'get' })
}

export const userApi = {
  list: (page, size) => request({ url: '/api/users', method: 'get', params: { page, size } }),
  save: (data) => request({ url: '/api/users', method: 'post', data }),
  update: (id, data) => request({ url: `/api/users/${id}`, method: 'put', data }),
  remove: (id) => request({ url: `/api/users/${id}`, method: 'delete' })
}

export const roleApi = {
  list: () => request({ url: '/api/roles', method: 'get' }),
  update: (id, data) => request({ url: `/api/roles/${id}`, method: 'put', data }),
  remove: (id) => request({ url: `/api/roles/${id}`, method: 'delete' })
}

export const menuApi = {
  tree: () => request({ url: '/api/menus/tree', method: 'get' })
}

export const deptApi = {
  list: () => request({ url: '/api/depts', method: 'get' }),
  update: (id, data) => request({ url: `/api/depts/${id}`, method: 'put', data })
}

export const logApi = {
  list: (page, size) => request({ url: '/api/logs', method: 'get', params: { page, size } })
}

export const profileApi = {
  info: () => request({ url: '/api/profile', method: 'get' })
}

export const dictApi = {
  types: (keyword, page, size) => request({ url: '/api/dict/types', method: 'get', params: { keyword, page, size } }),
  data: (type) => request({ url: '/api/dict/data', method: 'get', params: { type } }),
  saveType: (data) => request({ url: '/api/dict/type', method: 'post', data }),
  saveData: (data) => request({ url: '/api/dict/data', method: 'post', data }),
  deleteType: (id) => request({ url: `/api/dict/type/${id}`, method: 'delete' }),
  deleteData: (id) => request({ url: `/api/dict/data/${id}`, method: 'delete' })
}

export const fileApi = {
  list: (name) => request({ url: '/api/files', method: 'get', params: { name } }),
  upload: (data) => request({ url: '/api/files', method: 'post', data }),
  remove: (id) => request({ url: `/api/files/${id}`, method: 'delete' })
}

export const monitorApi = {
  info: () => request({ url: '/api/monitor', method: 'get' })
}

export const jobApi = {
  list: (keyword, page, size) => request({ url: '/api/job/list', method: 'get', params: { keyword, page, size } }),
  save: (data) => request({ url: '/api/job', method: 'post', data }),
  remove: (id) => request({ url: `/api/job/${id}`, method: 'delete' }),
  pause: (id) => request({ url: `/api/job/${id}/pause`, method: 'post' }),
  resume: (id) => request({ url: `/api/job/${id}/resume`, method: 'post' }),
  run: (id) => request({ url: `/api/job/${id}/run`, method: 'post' })
}

export const announcementApi = {
  list: (keyword, page, size) => request({ url: '/api/announcement/list', method: 'get', params: { keyword, page, size } }),
  save: (data) => request({ url: '/api/announcement', method: 'post', data }),
  remove: (id) => request({ url: `/api/announcement/${id}`, method: 'delete' }),
  publish: (id) => request({ url: `/api/announcement/${id}/publish`, method: 'post' }),
  unpublish: (id) => request({ url: `/api/announcement/${id}/unpublish`, method: 'post' })
}
