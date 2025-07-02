<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <img src="/SteamFo_files/cropped-网站-logo-生成-11.png" alt="Logo" class="logo">
          <h2>登录</h2>
        </div>
      </template>
      
      <el-form 
        :model="loginForm" 
        :rules="loginRules" 
        ref="loginFormRef"
        label-width="0"
        @keyup.enter="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="用户名"
            prefix-icon="User"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="密码"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>

        <el-form-item>
          <el-button 
            type="primary" 
            :loading="loading" 
            class="login-button"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>

        <div class="form-footer">
          <el-link type="primary" :underline="false" @click="goToRegister">
            还没有账号？立即注册
          </el-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../store/user';
import { ElMessage } from 'element-plus';
import { User, Lock } from '@element-plus/icons-vue';

const router = useRouter();
const userStore = useUserStore();
const loginFormRef = ref(null);
const loading = ref(false);

const loginForm = ref({
  username: '',
  password: ''
});

const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ]
};

const handleLogin = async () => {
  if (!loginFormRef.value) return;
  
  try {
    await loginFormRef.value.validate();
    loading.value = true;
    
    await userStore.login(loginForm.value.username, loginForm.value.password);
    ElMessage.success('登录成功');
    // 登录成功后跳转到首页
    router.push('/');
  } catch (error) {
    console.error('Login failed:', error);
    // 如果是网络错误
    if (error.message === 'Network Error') {
      ElMessage.error('网络连接失败，请检查网络设置');
    } else {
      ElMessage.error(error.response?.data?.message || error.message || '登录失败，请检查用户名和密码');
    }
  } finally {
    loading.value = false;
  }
};

const goToRegister = () => {
  router.push('/register');
};
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f5f5;
}

.login-card {
  width: 400px;
  border-radius: 8px;
}

.card-header {
  text-align: center;
}

.logo {
  height: 40px;
  margin-bottom: 16px;
}

.login-button {
  width: 100%;
}

.form-footer {
  margin-top: 16px;
  text-align: center;
}

:deep(.el-input__wrapper) {
  background-color: #f9f9f9;
}

:deep(.el-input__inner) {
  height: 45px;
}
</style> 