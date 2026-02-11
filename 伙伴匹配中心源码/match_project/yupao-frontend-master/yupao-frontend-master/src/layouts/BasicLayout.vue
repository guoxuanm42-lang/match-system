<template>
  <div class="top-header">
    <div class="top-header__left">
      <span class="top-header__title">{{ title }}</span>
    </div>
    <div class="top-header__nav">
      <router-link to="/" class="top-nav__item" :class="{ 'is-active': isActive('/') }">主页</router-link>
      <router-link to="/team" class="top-nav__item" :class="{ 'is-active': isActive('/team') }">队伍</router-link>
      <router-link to="/user" class="top-nav__item" :class="{ 'is-active': isActive('/user') }">个人</router-link>
    </div>
    <div class="top-header__right">
      <van-icon name="search" size="18" @click="onClickRight"/>
    </div>
  </div>
  <div id="content" class="app-content">
    <div class="page-container">
      <router-view/>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from "vue-router";
import {ref} from "vue";
import routes from "../config/route";

const router = useRouter();
const currentRoute = useRoute();
const DEFAULT_TITLE = 'ethan|伙伴匹配系统';
const title = ref(DEFAULT_TITLE);

/**
 * 根据路由切换标题
 */
router.beforeEach(() => {
  title.value = DEFAULT_TITLE;
})

const onClickLeft = () => {
  router.back();
};

const onClickRight = () => {
  router.push('/search')
};

const isActive = (path: string) => {
  return currentRoute.path === path;
};

</script>

<style scoped>
#content {
  padding: 24px 20px 28px;
}

.top-header {
  position: sticky;
  top: 0;
  z-index: 10;
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  align-items: center;
  gap: 12px;
  padding: 10px 20px;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(8px);
  border-bottom: 1px solid rgba(37, 99, 235, 0.12);
}

.top-header__left {
  display: flex;
  align-items: center;
}

.top-header__title {
  font-weight: 700;
  font-size: 16px;
  color: var(--app-text);
}

.top-header__nav {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
}

.top-header__right {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  color: var(--app-text-muted);
}

.top-nav__item {
  color: var(--app-text-muted);
  text-decoration: none;
  font-weight: 600;
  font-size: 14px;
  padding: 6px 4px;
  border-bottom: 2px solid transparent;
  transition: color 0.15s ease, border-color 0.15s ease;
}

.top-nav__item.is-active {
  color: var(--app-primary);
  border-color: var(--app-primary);
}
</style>
