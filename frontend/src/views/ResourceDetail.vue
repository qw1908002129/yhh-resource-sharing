<template>
  <div class="detail-container" v-if="resource">
    <el-row :gutter="24">
      <el-col :span="16">
        <el-card>
          <h2>{{ resource.title }}</h2>
          <img v-if="resource.coverImage" :src="resource.coverImage" class="detail-cover" alt="cover" />
          <div class="desc">{{ resource.description }}</div>
          <div class="content" v-html="resource.content"></div>
          <div class="download">
            <el-divider>下载地址</el-divider>
            <el-link :href="resource.downloadUrl" target="_blank" type="primary">点击下载</el-link>
            <span v-if="resource.downloadPassword">（提取码：{{ resource.downloadPassword }}）</span>
          </div>
        </el-card>
        <el-card class="comment-section">
          <h3>评论区</h3>
          <!-- 评论组件可后续扩展 -->
          <div>（评论功能开发中...）</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div>分类：{{ resource.categoryName }}</div>
          <div>标签：<el-tag v-for="tag in resource.tags" :key="tag" style="margin-right: 4px;">{{ tag }}</el-tag></div>
          <div>浏览：{{ resource.viewCount }}</div>
          <div>下载：{{ resource.downloadCount }}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const resource = ref(null);

const fetchResource = async () => {
  const res = await axios.get(`/api/resource/${route.params.id}`);
  resource.value = res.data.data;
};

onMounted(() => {
  fetchResource();
});
</script>

<style scoped>
.detail-container {
  padding: 32px;
}
.detail-cover {
  width: 100%;
  max-width: 600px;
  margin: 16px 0;
  border-radius: 8px;
}
.desc {
  color: #888;
  margin-bottom: 12px;
}
.content {
  margin-bottom: 24px;
}
.download {
  margin-top: 16px;
}
.comment-section {
  margin-top: 32px;
}
</style> 