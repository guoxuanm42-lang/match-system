<template>
  <template v-if="user">
    <van-cell title="当前用户" :value="user?.username" />
    <van-cell title="修改信息" is-link to="/user/update" />
    <van-cell title="我创建的队伍" is-link to="/user/team/create" />
    <van-cell title="我加入的队伍" is-link to="/user/team/join" />
    <van-button class="logout-button" type="danger" block round @click="onLogout">
      退出登录
    </van-button>
  </template>
</template>

<script setup lang="ts">
/**
 * 模块用途：个人中心页，展示当前用户信息入口，并提供退出登录与队伍入口跳转。
 *
 * 交互：页面进入时加载当前用户；点击“退出登录”会调用注销接口并跳转登录页。
 *
 * 数据来源：GET /user/current 获取当前用户；POST /user/logout 退出登录。
 */
import {useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import myAxios from "../plugins/myAxios";
import {Toast} from "vant";
import {getCurrentUser} from "../services/user";

// const user = {
//   id: 1,
//   username: '鱼皮',
//   userAccount: 'dogYupi',
//   avatarUrl: 'https://636f-codenav-8grj8px727565176-1256524210.tcb.qcloud.la/img/logo.png',
//   gender: '男',
//   phone: '123112312',
//   email: '12345@qq.com',
//   planetCode: '1234',
//   createTime: new Date(),
// }

const user = ref();

const router = useRouter();

onMounted(async () => {
  user.value = await getCurrentUser();
})

/**
 * 跳转到编辑页并携带编辑字段信息。
 *
 * 交互：由页面上的“编辑”入口触发（目前页面主要使用 is-link 跳转，此方法可复用）。
 *
 * 数据来源：参数来自页面点击位置传入，最终写入 route.query。
 *
 * @param editKey 要编辑的字段 key
 * @param editName 字段展示名
 * @param currentValue 当前字段值
 */
const toEdit = (editKey: string, editName: string, currentValue: string) => {
  router.push({
    path: '/user/edit',
    query: {
      editKey,
      editName,
      currentValue,
    }
  })
}

/**
 * 退出登录。
 *
 * 交互：用户点击“退出登录”触发；成功 Toast 并跳转登录页；失败 Toast 提示。
 *
 * 数据来源：后端 POST /user/logout。
 *
 * @returns Promise<void>
 */
const onLogout = async () => {
  const res = await myAxios.post('/user/logout');
  if (res?.code === 0) {
    Toast.success('已退出登录');
    window.location.href = '/user/login';
  } else {
    Toast.fail('退出失败');
  }
}
</script>

<style scoped>
.logout-button {
  margin-top: 16px;
}
</style>
