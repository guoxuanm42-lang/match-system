<template>
  <div class="index-header">
    <van-cell center title="心动模式" class="match-cell">
      <template #right-icon>
        <van-switch v-model="isMatchMode" size="24" />
      </template>
    </van-cell>
    <van-button class="refresh-button" type="primary" plain round size="small" @click="onRefresh">
      换一批
    </van-button>
  </div>
  <user-card-list :user-list="userList" :loading="loading"/>
  <van-empty v-if="!userList || userList.length < 1" description="数据为空"/>
</template>

<script setup lang="ts">
/**
 * 模块用途：首页用户推荐页，支持普通推荐与心动模式两种浏览方式。
 *
 * 交互：切换“心动模式”会触发重新加载；点击“换一批”在普通模式下翻页，在心动模式下重新拉取匹配结果。
 *
 * 数据来源：普通模式 GET /user/recommend；心动模式 GET /user/match；用户 tags 字段为 JSON 字符串，前端解析为数组展示。
 */
import { ref, watchEffect } from 'vue';
import myAxios from "../plugins/myAxios";
import {Toast} from "vant";
import UserCardList from "../components/UserCardList.vue";
import {UserType} from "../models/user";

const isMatchMode = ref<boolean>(false);

const userList = ref([]);
const loading = ref(true);
const pageNum = ref(1);

/**
 * 加载用户列表数据。
 *
 * 交互：页面进入或模式切换时触发；请求中会驱动 loading，失败时 Toast 提示。
 *
 * 数据来源：心动模式 GET /user/match?num=10；普通模式 GET /user/recommend?pageSize=8&pageNum=...。
 *
 * @returns Promise<void>
 */
const loadData = async () => {
  let userListData;
  loading.value = true;
  // 心动模式，根据标签匹配用户
  if (isMatchMode.value) {
    const num = 10;
    userListData = await myAxios.get('/user/match', {
      params: {
        num,
      },
    })
        .then(function (response) {
          console.log('/user/match succeed', response);
          return response?.data;
        })
        .catch(function (error) {
          console.error('/user/match error', error);
          Toast.fail('请求失败');
        })
  } else {
    // 普通模式，直接分页查询用户
    userListData = await myAxios.get('/user/recommend', {
      params: {
        pageSize: 8,
        pageNum: pageNum.value,
      },
    })
        .then(function (response) {
          console.log('/user/recommend succeed', response);
          return response?.data?.records;
        })
        .catch(function (error) {
          console.error('/user/recommend error', error);
          Toast.fail('请求失败');
        })
  }
  if (userListData) {
    userListData.forEach((user: UserType) => {
      if (user.tags) {
        user.tags = JSON.parse(user.tags);
      }
    })
    userList.value = userListData;
  }
  loading.value = false;
}

watchEffect(() => {
  loadData();
})

/**
 * 刷新推荐列表。
 *
 * 交互：用户点击“换一批”触发；普通模式下 pageNum 递增后重新加载，心动模式下直接重新拉取。
 *
 * 数据来源：复用 loadData 内的接口调用。
 */
const onRefresh = () => {
  if (isMatchMode.value) {
    loadData();
    return;
  }
  pageNum.value = Math.max(1, pageNum.value + 1);
  loadData();
}

</script>

<style scoped>
.index-header {
  display: flex;
  align-items: center;
  gap: 12px;
}

.match-cell {
  flex: 1;
}

.refresh-button {
  height: 32px;
  padding: 0 12px;
}
</style>
