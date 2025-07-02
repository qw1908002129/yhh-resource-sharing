import { defineStore } from 'pinia';
import request from '../utils/request';

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: null,
    isAdmin: false
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
  },

  actions: {
    async login(username, password) {
      try {
        const response = await request.post('/user/login', {
          username,
          password
        });
        
        // 直接存储原始token
        this.token = response.token;
        localStorage.setItem('token', response.token);
        
        // 获取用户信息
        await this.fetchUserInfo();
      } catch (error) {
        // 清理可能存在的无效token
        this.token = '';
        localStorage.removeItem('token');
        throw error;
      }
    },

    async fetchUserInfo() {
      try {
        const response = await request.get('/user/info');
        this.userInfo = response;
        this.isAdmin = this.userInfo?.role?.toUpperCase() === 'ADMIN';
      } catch (error) {
        // 清理可能存在的无效token
        this.token = '';
        localStorage.removeItem('token');
        throw error;
      }
    },

    logout() {
      this.token = '';
      this.userInfo = null;
      this.isAdmin = false;
      localStorage.removeItem('token');
    }
  }
}); 