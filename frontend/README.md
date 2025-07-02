# yhh-game-frontend

基于 Vue3 + Element Plus + Vite 的前端项目

## 启动方式

1. 安装依赖

```bash
npm install
```

2. 启动开发服务器

```bash
npm run dev
```

3. 打包构建

```bash
npm run build
```

## 主要依赖
- vue3
- element-plus
- vue-router
- pinia
- axios
- vite

## 目录结构

```
frontend/
  ├── src/
  │   ├── main.js           # 入口文件
  │   ├── App.vue           # 根组件
  │   ├── router/           # 路由配置
  │   └── views/            # 页面视图
  ├── public/               # 公共资源
  ├── index.html            # 入口HTML
  ├── package.json          # 依赖配置
  └── ...
```

## 说明
- 默认API请求代理到 `/api`，请确保Vite代理配置与后端网关一致。
- 首页和详情页已搭建基础结构，可根据实际接口完善。 