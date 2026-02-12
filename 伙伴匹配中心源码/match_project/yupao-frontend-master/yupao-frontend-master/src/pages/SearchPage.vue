<template>
  <form action="/">
    <van-search
        v-model="searchText"
        show-action
        placeholder="请输入要搜索的标签"
        @search="onSearch"
        @cancel="onCancel"
    />
  </form>
  <van-divider content-position="left">已选标签</van-divider>
  <div v-if="activeIds.length === 0">请选择标签</div>
  <van-row gutter="16" style="padding: 0 16px">
    <van-col v-for="tag in activeIds">
      <van-tag closeable type="primary" @close="doClose(tag)">
        {{ tag }}
      </van-tag>
    </van-col>
  </van-row>
  <van-divider content-position="left">选择标签</van-divider>
  <van-tree-select
      v-model:active-id="activeIds"
      v-model:main-active-index="activeIndex"
      :items="tagList"
  />
  <div style="padding: 12px">
    <van-button block type="primary" @click="doSearchResult">搜索</van-button>
  </div>
</template>

<script setup lang="ts">
/**
 * 模块用途：标签筛选页，用户选择标签后跳转到搜索结果页展示匹配用户。
 *
 * 交互：输入框搜索用于过滤可选标签；点击标签可选择/取消；点击“搜索”跳转到结果页并携带 tags 参数。
 *
 * 数据来源：标签候选项来自本地 originTagList；搜索结果页会基于 route.query.tags 调用后端接口。
 */
import {ref} from 'vue';
import {useRouter} from "vue-router";

const router = useRouter()

const searchText = ref('');

const originTagList = [{
  text: '性别',
  children: [
    {text: '男', id: '男'},
    {text: '女', id: '女'},
  ],
},
  {
    text: '年级',
    children: [
      {text: '大一', id: '大一'},
      {text: '大二', id: '大二'},
      {text: '大3', id: '大3'},
      {text: '大4', id: '大4'},
      {text: '大5', id: '大5aaaaaaa'},
      {text: '大6', id: '大6aaaaaaa'},
    ],
  },
]

// 标签列表
let tagList = ref(originTagList);

/**
 * 根据输入内容过滤可选标签。
 *
 * 交互：由 van-search 的 @search 触发；仅更新本页展示的候选标签列表。
 *
 * 数据来源：searchText（输入框 v-model）与 originTagList（本地静态标签）。
 *
 * @param val 搜索框输入值
 */
const onSearch = (val: string) => {
  tagList.value = originTagList.map(parentTag => {
    const tempChildren = [...parentTag.children];
    const tempParentTag = {...parentTag};
    tempParentTag.children = tempChildren.filter(item => item.text.includes(searchText.value));
    return tempParentTag;
  });

}

/**
 * 取消搜索并重置候选标签列表。
 *
 * 交互：由 van-search 的 @cancel 触发；清空输入并恢复默认候选标签。
 *
 * 数据来源：originTagList（本地静态标签）。
 */
const onCancel = () => {
  searchText.value = '';
  tagList.value = originTagList;
};

// 已选中的标签
const activeIds = ref([]);
const activeIndex = ref(0);

/**
 * 移除已选标签。
 *
 * 交互：用户关闭已选标签的 close 按钮触发；更新 activeIds 并影响最终搜索条件。
 *
 * 数据来源：activeIds（本页已选标签集合）。
 *
 * @param tag 要移除的标签 id
 */
const doClose = (tag: string) => {
  activeIds.value = activeIds.value.filter(item => {
    return item !== tag;
  })
}

/**
 * 跳转到搜索结果页。
 *
 * 交互：用户点击“搜索”按钮触发；跳转到 /user/list 并携带 tags 查询参数。
 *
 * 数据来源：activeIds（已选标签）。
 */
const doSearchResult = () => {
  router.push({
    path: '/user/list',
    query: {
      tags: activeIds.value
    }
  })
}

</script>

<style scoped>

</style>
