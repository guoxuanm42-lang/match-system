<template>
  <user-card-list :user-list="userList" />
  <van-empty v-if="!userList || userList.length < 1" description="搜索结果为空" />
</template>

<script setup lang="ts">
/**
 * 模块用途：标签搜索结果页，根据选中的标签列表查询并展示匹配用户。
 *
 * 交互：页面进入时自动加载结果；请求失败时 Toast 提示；列表为空时展示 Empty。
 *
 * 数据来源：route.query.tags（来自搜索页跳转参数）；后端接口 GET /user/search/tags（tagNameList 多值参数）。
 */
import {onMounted, ref} from 'vue';
import {useRoute} from "vue-router";
import myAxios from "../plugins/myAxios";
import {Toast} from "vant";
import UserCardList from "../components/UserCardList.vue";
import type {UserType} from "../models/user";

const route = useRoute();
const {tags} = route.query;

const userList = ref([]);

/**
 * 加载标签搜索结果。
 *
 * 交互：页面 onMounted 时触发；成功后更新用户列表，失败时 Toast 提示。
 *
 * 数据来源：GET /user/search/tags，参数 tagNameList 来自 route.query.tags。
 *
 * @returns Promise<void>
 */
const loadUserList = async () => {
  const userListData = await myAxios.get('/user/search/tags', {
    params: {
      tagNameList: tags
    },
    paramsSerializer: (params: any) => {
      const searchParams = new URLSearchParams();
      Object.keys(params).forEach((key) => {
        const value = params[key];
        if (Array.isArray(value)) {
          value.forEach((item) => searchParams.append(key, String(item)));
          return;
        }
        if (value !== undefined && value !== null) {
          searchParams.append(key, String(value));
        }
      });
      return searchParams.toString();
    }
  })
      .then(function (response) {
        console.log('/user/search/tags succeed', response);
        return response?.data;
      })
      .catch(function (error) {
        console.error('/user/search/tags error', error);
        Toast.fail('请求失败');
      })
  console.log(userListData)
  if (userListData) {
    userListData.forEach((user: UserType) => {
      if (user.tags) {
        user.tags = JSON.parse(user.tags);
      }
    })
    userList.value = userListData;
  }
}

onMounted(async () => {
  await loadUserList();
})


// const mockUser = {
//   id: 12345,
//   username: '鱼皮',
//   userAccount: '12314',
//   profile: '一名精神小伙，目前还有头发，谢谢大家，阿爸爸阿爸爸阿巴阿巴阿巴',
//   avatarUrl: 'https://636f-codenav-8grj8px727565176-1256524210.tcb.qcloud.la/img/logo.png',
//   gender: 0,
//   phone: '13113113111',
//   email: '592342843721987@xzcxzczxcz.com',
//   userRole: 0,
//   planetCode: '1234',
//   tags: ['java', 'emo', '打工中', 'emo', '打工中'],
//   createTime: new Date(),
// }


</script>

<style scoped>

</style>
