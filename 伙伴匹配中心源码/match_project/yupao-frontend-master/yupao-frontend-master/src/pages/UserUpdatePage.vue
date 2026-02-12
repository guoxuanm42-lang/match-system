<template>
  <template v-if="user">
    <van-cell title="昵称" is-link to="/user/edit" :value="user.username"  @click="toEdit('username', '昵称', user.username)"/>
    <van-cell title="账号" :value="user.userAccount"/>
    <van-cell title="头像" is-link to="/user/edit">
      <img style="height: 48px" :src="user.avatarUrl"/>
    </van-cell>
    <van-cell title="性别" is-link :value="formatGender(user.gender)" @click="toEdit('gender', '性别', user.gender)"/>
    <van-cell title="电话" is-link to="/user/edit" :value="user.phone" @click="toEdit('phone', '电话', user.phone)"/>
    <van-cell title="邮箱" is-link to="/user/edit" :value="user.email" @click="toEdit('email', '邮箱', user.email)"/>
    <van-cell title="标签" is-link to="/user/edit" :value="formatTags(user.tags)" @click="toEdit('tags', '标签', user.tags)"/>
    <van-cell title="星球编号" :value="user.planetCode"/>
    <van-cell title="注册时间" :value="user.createTime"/>
  </template>
</template>

<script setup lang="ts">
/**
 * 模块用途：用户资料页，展示个人信息并提供跳转到字段编辑页的入口。
 *
 * 交互：页面进入时加载当前用户；点击某个字段跳转到 /user/edit 进行编辑。
 *
 * 数据来源：GET /user/current 获取用户信息；字段编辑提交由 /user/edit 页面调用 POST /user/update。
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

onMounted(async () => {
  user.value = await getCurrentUser();
})

const router = useRouter();

/**
 * 格式化性别展示文案。
 *
 * 交互：用于页面渲染；不触发额外交互。
 *
 * 数据来源：gender 字段来自后端用户信息。
 *
 * @param gender 性别值（0 男 / 1 女 / 其他未知）
 * @returns 性别展示文案
 */
const formatGender = (gender: number | undefined) => {
  if (gender === 0) return "男";
  if (gender === 1) return "女";
  return "未知";
};

/**
 * 格式化标签展示文案。
 *
 * 交互：用于页面渲染；不触发额外交互。
 *
 * 数据来源：tags 字段来自后端用户信息（JSON 字符串）。
 *
 * @param tags 标签 JSON 字符串
 * @returns 格式化后的标签展示字符串
 */
const formatTags = (tags: string | undefined) => {
  if (!tags) return "未设置";
  try {
    const parsed = JSON.parse(tags);
    if (Array.isArray(parsed)) {
      return parsed.join(" / ");
    }
  } catch (error) {
    return tags;
  }
  return tags;
};

/**
 * 跳转到编辑页并携带编辑字段信息。
 *
 * 交互：用户点击某个字段的 cell 触发；跳转到 /user/edit。
 *
 * 数据来源：参数来自当前页面已加载的 user 字段，写入 route.query。
 *
 * @param editKey 要编辑的字段 key
 * @param editName 字段展示名
 * @param currentValue 当前字段值
 */
const toEdit = (editKey: string, editName: string, currentValue: string | number | undefined) => {
  router.push({
    path: '/user/edit',
    query: {
      editKey,
      editName,
      currentValue: currentValue ?? '',
    }
  })
}
</script>

<style scoped>

</style>
