import { createApp } from 'vue';
import { createPinia } from 'pinia';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import App from './App.vue';
import router from './router';
import axios from 'axios';

// 配置 axios 默认值
axios.defaults.baseURL = 'http://localhost:5173';  // 使用前端开发服务器地址，它会自动代理到后端
axios.defaults.timeout = 10000;
axios.defaults.headers.common['Content-Type'] = 'application/json';
axios.defaults.headers.common['Accept'] = 'application/json';

// 添加请求拦截器
axios.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// 添加响应拦截器
axios.interceptors.response.use(
  response => {
    console.log('Response:', response);
    // 如果响应成功且业务状态码为200，返回data字段
    if (response.data.code === 200) {
      return response.data.data;
    }
    // 如果业务状态码不为200，抛出业务错误信息
    return Promise.reject(new Error(response.data.message || '操作失败'));
  },
  error => {
    if (error.response?.status === 401) {
      // 清除本地存储的token
      localStorage.removeItem('token');
      // 跳转到登录页
      router.push('/login');
      return Promise.reject(new Error('请重新登录'));
    }
    
    // 处理其他错误
    const errorMessage = error.response?.data?.message || error.message || '请求失败';
    return Promise.reject(new Error(errorMessage));
  }
);

const app = createApp(App);
const pinia = createPinia();

app.use(pinia);
app.use(router);
app.use(ElementPlus);

app.mount('#app'); 