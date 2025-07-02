import axios from 'axios';
import { ElMessage } from 'element-plus';

const service = axios.create({
  baseURL: 'http://localhost:80',  // 网关地址
  timeout: 5000
});

// 请求拦截器
service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    console.error('Request error:', error);
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data;
    
    // 如果后端返回的不是200，说明有错误
    if (res.code !== 200) {
      // 如果是401，说明token无效或过期
      if (res.code === 401) {
        // 清除本地存储的token
        localStorage.removeItem('token');
        // 刷新页面，重新登录
        window.location.reload();
      }
      ElMessage.error(res.message || '请求失败');
      return Promise.reject(new Error(res.message || '请求失败'));
    }
    
    // 正常返回数据中的data字段
    return res.data;
  },
  error => {
    console.error('Response error:', error);
    // 如果是401错误，清除token并重新登录
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('token');
      window.location.reload();
    }
    ElMessage.error(error.response?.data?.message || error.message || '网络请求失败');
    return Promise.reject(error);
  }
);

export default service; 