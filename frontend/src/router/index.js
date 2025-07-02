import { createRouter, createWebHistory } from 'vue-router';
import { useUserStore } from '../store/user';

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue'),
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { guest: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { guest: true }
  },
  {
    path: '/resource/:id',
    name: 'ResourceDetail',
    component: () => import('../views/ResourceDetail.vue'),
    props: true,
  },
  // 可扩展：用户中心、登录、注册等
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore();
  
  // 如果是需要登录的页面且用户未登录，重定向到登录页
  if (to.matched.some(record => !record.meta.guest) && !userStore.isLoggedIn) {
    next({ name: 'Login', query: { redirect: to.fullPath } });
  } 
  // 如果是游客页面（登录/注册）且用户已登录，重定向到首页
  else if (to.matched.some(record => record.meta.guest) && userStore.isLoggedIn) {
    next({ name: 'Home' });
  }
  else {
    next();
  }
});

export default router; 