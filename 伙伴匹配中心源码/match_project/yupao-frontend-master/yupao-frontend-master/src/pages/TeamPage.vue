<template>
  <div id="teamPage">
    <van-search v-model="searchText" placeholder="搜索队伍" @search="onSearch" />
    <van-tabs v-model:active="active" @change="onTabChange">
      <van-tab title="公开" name="public" />
      <van-tab title="加密" name="private" />
    </van-tabs>
    <div style="margin-bottom: 16px" />
    <van-button class="add-button" type="primary" icon="plus" @click="toAddTeam" />
    <team-card-list :teamList="teamList" />
    <van-empty v-if="teamList?.length < 1" description="数据为空"/>
  </div>
</template>

<script setup lang="ts">
/**
 * 模块用途：队伍大厅页，支持按关键字搜索与按状态（公开/加密）筛选队伍列表。
 *
 * 交互：页面进入时自动加载队伍；切换 Tab 或执行搜索会重新请求列表；点击加号跳转创建队伍页。
 *
 * 数据来源：后端 GET /team/list（searchText、status）；路由跳转由本地 router 配置提供。
 */
import {useRouter} from "vue-router";
import TeamCardList from "../components/TeamCardList.vue";
import {onMounted, ref} from "vue";
import myAxios from "../plugins/myAxios";
import {Toast} from "vant";

const active = ref('public')
const router = useRouter();
const searchText = ref('');

/**
 * 切换队伍筛选状态。
 *
 * 交互：由 van-tabs 的 @change 触发；会重新加载队伍列表。
 *
 * 数据来源：当前搜索词 searchText；后端 GET /team/list。
 *
 * @param name Tab name（public/private）
 */
const onTabChange = (name: string) => {
  // 查公开
  if (name === 'public') {
    listTeam(searchText.value, 0);
  } else {
    // 查加密
    listTeam(searchText.value, 2);
  }
}

/**
 * 跳转到创建队伍页。
 *
 * 交互：用户点击“+”按钮触发，路由跳转到 /team/add。
 *
 * 数据来源：本地路由配置。
 */
const toAddTeam = () => {
  router.push({
    path: "/team/add"
  })
}

const teamList = ref([]);

/**
 * 查询队伍列表。
 *
 * 交互：页面初始化/搜索/筛选时触发；成功更新列表，失败 Toast 提示。
 *
 * 数据来源：后端 GET /team/list（searchText、status、pageNum）。
 *
 * @param val 搜索关键字
 * @param status 队伍状态（0 公开 / 2 加密）
 * @returns Promise<void>
 */
const listTeam = async (val = '', status = 0) => {
  const res = await myAxios.get("/team/list", {
    params: {
      searchText: val,
      pageNum: 1,
      status,
    },
  });
  if (res?.code === 0) {
    teamList.value = res.data;
  } else {
    Toast.fail('加载队伍失败，请刷新重试');
  }
}

// 页面加载时只触发一次
onMounted( () => {
  listTeam();
})

/**
 * 执行队伍搜索。
 *
 * 交互：由 van-search 的 @search 触发；会重新加载队伍列表。
 *
 * 数据来源：搜索关键字 val；后端 GET /team/list。
 *
 * @param val 搜索关键字
 */
const onSearch = (val: string) => {
  listTeam(val);
};

</script>

<style scoped>
#teamPage {

}
</style>
