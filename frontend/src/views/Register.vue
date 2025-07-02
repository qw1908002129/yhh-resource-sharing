<template>
  <div class="register-container">
    <el-card class="register-card">
      <template #header>
        <div class="card-header">
          <img src="/SteamFo_files/cropped-网站-logo-生成-11.png" alt="Logo" class="logo">
          <h2>注册</h2>
        </div>
      </template>
      
      <el-form 
        :model="registerForm" 
        :rules="registerRules" 
        ref="registerFormRef"
        label-width="0"
      >
        <el-form-item prop="username">
          <el-input
            v-model="registerForm.username"
            placeholder="用户名"
            prefix-icon="User"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="密码"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>

        <el-form-item prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="确认密码"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>

        <el-form-item prop="email">
          <el-input
            v-model="registerForm.email"
            placeholder="邮箱"
            prefix-icon="Message"
          />
        </el-form-item>

        <el-form-item prop="nickname">
          <el-input
            v-model="registerForm.nickname"
            placeholder="昵称"
            prefix-icon="User"
          />
        </el-form-item>

        <!-- 隐藏的管理员注册选项，按住Shift键3秒显示 -->
        <el-form-item v-if="showAdminOption">
          <el-checkbox v-model="registerForm.isAdmin">注册为管理员</el-checkbox>
        </el-form-item>

        <el-form-item>
          <el-button 
            type="primary" 
            :loading="loading" 
            class="register-button"
            @click="handleRegister"
          >
            注册
          </el-button>
        </el-form-item>

        <div class="form-footer">
          <el-link type="primary" :underline="false" @click="goToLogin">
            已有账号？立即登录
          </el-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../store/user';
import { ElMessage } from 'element-plus';
import { User, Lock, Message } from '@element-plus/icons-vue';
import axios from 'axios';

const router = useRouter();
const userStore = useUserStore();
const registerFormRef = ref(null);
const loading = ref(false);
const showAdminOption = ref(false);
let shiftTimer = null;

const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  nickname: '',
  isAdmin: false
});

// 监听Shift键
const handleKeyDown = (e) => {
  if (e.key === 'Shift') {
    shiftTimer = setTimeout(() => {
      showAdminOption.value = true;
    }, 3000);
  }
};

const handleKeyUp = (e) => {
  if (e.key === 'Shift') {
    clearTimeout(shiftTimer);
  }
};

onMounted(() => {
  window.addEventListener('keydown', handleKeyDown);
  window.addEventListener('keyup', handleKeyUp);
});

onUnmounted(() => {
  window.removeEventListener('keydown', handleKeyDown);
  window.removeEventListener('keyup', handleKeyUp);
});

const validatePass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入密码'));
  } else {
    if (registerForm.value.confirmPassword !== '') {
      registerFormRef.value.validateField('confirmPassword');
    }
    callback();
  }
};

const validatePass2 = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'));
  } else if (value !== registerForm.value.password) {
    callback(new Error('两次输入密码不一致!'));
  } else {
    callback();
  }
};

const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { validator: validatePass, trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { validator: validatePass2, trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ]
};

const handleRegister = async () => {
  if (!registerFormRef.value) return;
  
  try {
    await registerFormRef.value.validate();
    loading.value = true;
    
    const { isAdmin, ...registerData } = registerForm.value;
    const requestData = {
      ...registerData,
      role: isAdmin ? 'ADMIN' : 'USER'
    };
    
    await axios.post('/user/register', requestData);
    ElMessage.success('注册成功，请登录');
    router.push('/login');
  } catch (error) {
    console.error('Registration failed:', error);
    ElMessage.error(error.message || '注册失败，请稍后重试');
  } finally {
    loading.value = false;
  }
};

const goToLogin = () => {
  router.push('/login');
};
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f5f5;
}

.register-card {
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

.register-button {
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