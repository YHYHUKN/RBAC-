import axios from 'axios'
import { mockRequest } from '@/mock'

const USE_MOCK = import.meta.env.VITE_MOCK === 'true'
const API_BASE = import.meta.env.VITE_API_BASE || ''

const instance = axios.create({
  baseURL: API_BASE,
  timeout: 10000
})

instance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (err) => Promise.reject(err)
)

instance.interceptors.response.use(
  (res) => res.data,
  (err) => Promise.reject(err)
)

/**
 * 统一请求入口：
 * - 若开启 Mock（VITE_MOCK=true），直接走本地 Mock 数据，保证离线/CloudStudio 也能完整演示；
 * - 否则请求真实后端（VITE_API_BASE 指向 Render 等 PaaS 地址）。
 */
export async function request(config) {
  if (USE_MOCK) {
    return mockRequest({
      url: config.url,
      method: (config.method || 'get').toLowerCase(),
      data: config.data,
      params: config.params
    })
  }
  return instance.request(config)
}

export default request
