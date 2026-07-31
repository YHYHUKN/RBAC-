import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    username: '',
    name: '',
    roles: [],
    menus: []
  }),
  actions: {
    setToken(token) {
      this.token = token
      localStorage.setItem('token', token)
    },
    setUserInfo(info) {
      this.username = info.username || ''
      this.name = info.name || ''
      this.roles = info.roles || []
      this.menus = info.menus || []
    },
    logout() {
      this.token = ''
      this.username = ''
      this.name = ''
      this.roles = []
      this.menus = []
      localStorage.removeItem('token')
    }
  }
})
