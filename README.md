# 游戏资源分享平台

基于Spring Cloud Alibaba微服务架构和Vue.js的前后端分离游戏资源分享平台。

## 技术栈

### 后端
- JDK 17
- Spring Boot 3.x
- Spring Cloud Alibaba 2022.x
- MySQL 8.0
- Redis 6.x
- MyBatis Plus
- Nacos (注册中心和配置中心)
- Gateway (网关)
- Sentinel (熔断限流)

### 前端
- Vue 3
- Element Plus
- Vite
- Axios
- Vue Router
- Pinia

## 项目结构

```
yhh-game-platform/
├── backend/                    # 后端微服务
│   ├── gateway-service/        # 网关服务
│   ├── user-service/          # 用户服务
│   ├── resource-service/      # 资源服务
│   ├── comment-service/       # 评论服务
│   ├── common/                # 公共模块
│   └── docker/                # Docker配置
├── frontend/                  # 前端项目
│   ├── src/
│   ├── public/
│   └── package.json
├── database/                  # 数据库脚本
└── docs/                      # 文档
```

## 功能模块

1. **用户管理模块**
   - 用户注册/登录
   - 用户信息管理
   - 权限管理

2. **资源管理模块**
   - 游戏资源展示
   - 资源详情页
   - 资源搜索
   - 管理员资源上传

3. **评论模块**
   - 用户评论
   - 评论管理
   - 评论点赞

4. **系统管理**
   - 分类管理
   - 系统配置

## 快速开始

### 环境要求
- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Redis 6.0+
- Nacos 2.0+

### 启动步骤

1. 启动Nacos服务
2. 启动MySQL和Redis
3. 执行数据库脚本
4. 启动后端服务
5. 启动前端服务

详细启动说明请参考各模块的README文档。 