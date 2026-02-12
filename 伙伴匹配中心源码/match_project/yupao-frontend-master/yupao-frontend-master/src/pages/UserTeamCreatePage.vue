<template>
  <div id="teamPage">
    <van-search v-model="searchText" placeholder="搜索队伍" @search="onSearch" />
    <van-button type="primary" @click="doJoinTeam">创建队伍</van-button>
    <team-card-list :teamList="teamList" />
    <van-empty v-if="teamList?.length < 1" description="数据为空"/>
  </div>
</template>

<script setup lang="ts">
/**
 * 模块用途：我创建的队伍页，展示当前用户创建过的队伍列表并提供创建入口。
 *
 * 交互：页面进入时加载队伍列表；点击“创建队伍”跳转到创建队伍页；搜索触发列表刷新。
 *
 * 数据来源：后端 GET /team/list/my/create（searchText、pageNum）；路由跳转由本地配置提供。
 */
import {useRouter} from "vue-router";
import TeamCardList from "../components/TeamCardList.vue";
import {onMounted, ref} from "vue";
import myAxios from "../plugins/myAxios";
import {Toast} from "vant";

const router = useRouter();
const searchText = ref('');

/**
 * 跳转到创建队伍页。
 *
 * 交互：用户点击“创建队伍”按钮触发，路由跳转到 /team/add。
 *
 * 数据来源：本地路由配置。
 */
const doJoinTeam = () => {
  router.push({
    path: "/team/add"
  })
}

const teamList = ref([]);

/**
 * 查询我创建的队伍列表。
 *
 * 交互：页面初始化或搜索时触发；成功更新列表，失败 Toast 提示。
 *
 * 数据来源：后端 GET /team/list/my/create。
 *
 * @param val 搜索关键字
 * @returns Promise<void>
 */
const listTeam = async (val = '') => {
  const res = await myAxios.get("/team/list/my/create", {
    params: {
      searchText: val,
      pageNum: 1,
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
 * 数据来源：搜索关键字 val；后端 GET /team/list/my/create。
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
