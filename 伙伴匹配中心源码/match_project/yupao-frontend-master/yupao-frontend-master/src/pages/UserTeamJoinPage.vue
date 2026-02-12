<template>
  <div id="teamPage">
    <van-search v-model="searchText" placeholder="搜索队伍" @search="onSearch" />
    <team-card-list :teamList="teamList" />
    <van-empty v-if="teamList?.length < 1" description="数据为空"/>
  </div>
</template>

<script setup lang="ts">
/**
 * 模块用途：我加入的队伍页，展示当前用户已加入的队伍列表。
 *
 * 交互：页面进入时加载队伍列表；搜索框输入后触发搜索并刷新列表。
 *
 * 数据来源：后端 GET /team/list/my/join（searchText、pageNum）。
 */
import {useRouter} from "vue-router";
import TeamCardList from "../components/TeamCardList.vue";
import {onMounted, ref} from "vue";
import myAxios from "../plugins/myAxios";
import {Toast} from "vant";

const router = useRouter();
const searchText = ref('');

const teamList = ref([]);

/**
 * 查询我加入的队伍列表。
 *
 * 交互：页面初始化或搜索时触发；成功更新列表，失败 Toast 提示。
 *
 * 数据来源：后端 GET /team/list/my/join。
 *
 * @param val 搜索关键字
 * @returns Promise<void>
 */
const listTeam = async (val = '') => {
  const res = await myAxios.get("/team/list/my/join", {
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
 * 数据来源：搜索关键字 val；后端 GET /team/list/my/join。
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
