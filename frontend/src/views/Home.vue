<template>
  <div class="app-container">
    <!-- 头部导航 -->
    <el-header class="header">
      <div class="header-content">
        <div class="left">
          <div class="logo">
            <img src="/SteamFo_files/cropped-网站-logo-生成-11.png" alt="SteamFo" />
          </div>
          <el-menu mode="horizontal" :router="true" class="menu">
            <el-menu-item index="/">首页</el-menu-item>
            <el-menu-item index="/guide">入站必看</el-menu-item>
            <el-menu-item index="/mobile">手机游戏</el-menu-item>
            <el-menu-item index="/pc">电脑游戏</el-menu-item>
            <el-menu-item index="/steam">Steam游戏</el-menu-item>
            <el-menu-item index="/switch">折扣手游</el-menu-item>
          </el-menu>
        </div>
        <div class="right">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索游戏"
            class="search-input"
            :prefix-icon="Search"
            @keyup.enter="handleSearch"
          />
          <el-button 
            v-if="isAdmin"
            type="primary" 
            @click="showUploadDialog = true"
            :icon="Plus"
          >
            上传资源
          </el-button>
          <el-avatar :size="40" :src="userAvatar" @click="handleUserClick" />
        </div>
      </div>
    </el-header>

    <!-- 主要内容区域 -->
    <div class="home-container">
      <el-row :gutter="20">
        <el-col :span="6" v-for="item in resources" :key="item.id">
          <el-card shadow="hover" class="resource-card" @click="goDetail(item.id)">
            <img :src="item.coverImage" class="cover" alt="cover" />
            <div class="title">{{ item.title }}</div>
            <div class="desc">{{ item.description }}</div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 上传资源对话框 -->
    <el-dialog 
      v-model="showUploadDialog" 
      title="上传资源" 
      width="500px"
    >
      <el-form 
        :model="uploadForm" 
        :rules="uploadRules"
        ref="uploadFormRef"
        label-width="80px"
      >
        <el-form-item label="标题" prop="title">
          <el-input v-model="uploadForm.title" placeholder="请输入资源标题"/>
        </el-form-item>
        
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="uploadForm.categoryId" placeholder="请选择分类">
            <el-option
              v-for="item in categories"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="描述" prop="description">
          <el-input 
            v-model="uploadForm.description" 
            type="textarea" 
            :rows="4"
            placeholder="请输入资源描述"
          />
        </el-form-item>

        <el-form-item label="封面图" prop="coverImage">
          <el-upload
            class="avatar-uploader"
            :show-file-list="false"
            :before-upload="beforeImageUpload"
            :http-request="handleImageUpload"
          >
            <img v-if="uploadForm.coverImage" :src="uploadForm.coverImage" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item label="下载链接" prop="downloadUrl">
          <el-input v-model="uploadForm.downloadUrl" placeholder="请输入资源下载链接"/>
        </el-form-item>

        <el-form-item label="下载密码" prop="downloadPassword">
          <el-input v-model="uploadForm.downloadPassword" placeholder="请输入下载密码（可选）"/>
        </el-form-item>

        <el-form-item label="版本" prop="version">
          <el-input v-model="uploadForm.version" placeholder="请输入版本号"/>
        </el-form-item>

        <el-form-item label="平台" prop="platform">
          <el-select v-model="uploadForm.platform" placeholder="请选择平台">
            <el-option label="PC" value="PC" />
            <el-option label="Mobile" value="Mobile" />
            <el-option label="Switch" value="Switch" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showUploadDialog = false">取消</el-button>
          <el-button type="primary" @click="submitUpload" :loading="uploading">确认上传</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { Search, Plus } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useUserStore } from '../store/user';
import request from '../utils/request';

const router = useRouter();
const resources = ref([]);
const searchKeyword = ref('');
const userStore = useUserStore();
const isAdmin = computed(() => userStore.isAdmin);
const categories = ref([]);
const showUploadDialog = ref(false);
const uploadFormRef = ref(null);
const uploading = ref(false);

const uploadForm = ref({
  title: '',
  description: '',
  coverImage: '',
  categoryId: null,
  downloadUrl: '',
  downloadPassword: '',
  version: '',
  platform: '',
  status: 1,
  createUserId: null // 这个值应该从用户状态中获取
});

const uploadRules = {
  title: [
    { required: true, message: '请输入标题', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入描述', trigger: 'blur' },
    { min: 10, max: 500, message: '长度在 10 到 500 个字符', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择分类', trigger: 'change' }
  ],
  coverImage: [
    { required: true, message: '请上传封面图片', trigger: 'change' }
  ],
  downloadUrl: [
    { required: true, message: '请输入下载链接', trigger: 'blur' }
  ],
  version: [
    { required: true, message: '请输入版本号', trigger: 'blur' }
  ],
  platform: [
    { required: true, message: '请选择平台', trigger: 'change' }
  ]
};

const userAvatar = computed(() => 
  userStore.userInfo?.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
);

const fetchResources = async () => {
  try {
    const res = await request.get('/resource/list');
    // 处理图片URL，直接访问资源服务端口
    const processedResources = (res.records || []).map(item => ({
      ...item,
      coverImage: item.coverImage ? 
        (item.coverImage.startsWith('http') ? 
          item.coverImage : 
          `http://localhost:8082${item.coverImage}`) : 
        '/SteamFo_files/thumbnail.png'
    }));
    resources.value = processedResources;
  } catch (error) {
    console.error('Failed to fetch resources:', error);
    ElMessage.error('获取资源列表失败');
  }
};

const goDetail = (id) => {
  router.push({ name: 'ResourceDetail', params: { id } });
};

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    // TODO: 实现搜索功能
    console.log('搜索:', searchKeyword.value);
  }
};

const handleUserClick = () => {
  if (!userStore.isLoggedIn) {
    router.push('/login');
  } else {
    ElMessageBox.confirm(
      '确定要退出登录吗？',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    ).then(() => {
      userStore.logout();
      ElMessage.success('已退出登录');
      router.push('/login');
    }).catch(() => {});
  }
};

const fetchCategories = async () => {
  try {
    const res = await request.get('/resource/category/list');
    categories.value = res || [];
  } catch (error) {
    console.error('Failed to fetch categories:', error);
    ElMessage.error('获取分类列表失败');
  }
};

// 图片上传前的验证
const beforeImageUpload = (file) => {
  const isImage = file.type.startsWith('image/');
  const isLt2M = file.size / 1024 / 1024 < 2;

  if (!isImage) {
    ElMessage.error('只能上传图片文件!');
    return false;
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!');
    return false;
  }
  return true;
};

// 处理图片上传
const handleImageUpload = async (options) => {
  try {
    const formData = new FormData();
    formData.append('file', options.file);
    
    const res = await request.post('/resource/upload/cover', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    
    // 使用直接访问资源服务的URL路径用于预览
    uploadForm.value.coverImage = res.startsWith('http') ? 
      res : 
      `http://localhost:8082${res}`;
    ElMessage.success('图片上传成功');
  } catch (error) {
    console.error('上传图片失败:', error);
    ElMessage.error(error.message || '上传图片失败');
  }
};

// 提交表单
const submitUpload = async () => {
  if (!uploadFormRef.value) return;
  
  await uploadFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        uploading.value = true;
        // 添加当前用户ID
        uploadForm.value.createUserId = userStore.userInfo?.id;
        
        // 处理图片路径，提交时使用相对路径
        const submitData = {
          ...uploadForm.value,
          coverImage: uploadForm.value.coverImage.includes('localhost:8082/uploads/') ?
            uploadForm.value.coverImage.replace('http://localhost:8082', '') :
            uploadForm.value.coverImage
        };
        
        await request.post('/resource', submitData);
        ElMessage.success('资源上传成功');
        showUploadDialog.value = false;
        fetchResources(); // 刷新资源列表
        // 重置表单
        uploadForm.value = {
          title: '',
          description: '',
          coverImage: '',
          categoryId: null,
          downloadUrl: '',
          downloadPassword: '',
          version: '',
          platform: '',
          status: 1,
          createUserId: null
        };
      } catch (error) {
        console.error('上传资源失败:', error);
        ElMessage.error(error.message || '上传资源失败');
      } finally {
        uploading.value = false;
      }
    }
  });
};

onMounted(async () => {
  await Promise.all([
    fetchResources(),
    fetchCategories()
  ]);
  if (userStore.token && !userStore.userInfo) {
    userStore.fetchUserInfo();
  }
});
</script>

<style scoped>
.app-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.header {
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.left {
  display: flex;
  align-items: center;
}

.logo {
  margin-right: 20px;
  display: flex;
  align-items: center;
}

.logo img {
  height: 40px;
  width: auto;
  object-fit: contain;
}

.menu {
  border-bottom: none;
}

.right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.search-input {
  width: 200px;
}

.home-container {
  padding: 92px 32px 32px;
  max-width: 1200px;
  margin: 0 auto;
}

.resource-card {
  cursor: pointer;
  margin-bottom: 24px;
  transition: all 0.3s;
  border-radius: 8px;
  overflow: hidden;
}

.resource-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

.cover {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 8px 8px 0 0;
  transition: transform 0.3s;
}

.resource-card:hover .cover {
  transform: scale(1.05);
}

.title {
  font-weight: bold;
  margin: 16px 16px 8px;
  font-size: 18px;
  color: #333;
}

.desc {
  color: #666;
  font-size: 14px;
  line-height: 1.5;
  margin: 0 16px 16px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.avatar-uploader {
  width: 178px;
  height: 178px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
  line-height: 178px;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
  object-fit: cover;
}

.dialog-footer {
  padding: 20px 0 0;
  text-align: right;
}
</style> 