import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    open: true,
    proxy: {
      '/api': {
        target: 'http://localhost:80', // 后端网关地址
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api/, '')
      }
    }
  },
}); 