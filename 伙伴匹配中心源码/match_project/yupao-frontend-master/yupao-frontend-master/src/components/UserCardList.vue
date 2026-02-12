<template>
  <van-skeleton title avatar :row="3" :loading="props.loading" v-for="user in props.userList">
    <van-card
        :desc="user.profile"
        :title="`${user.username}（${user.planetCode}）`"
        :thumb="user.avatarUrl"
    >
      <template #tags>
        <van-tag plain type="primary" class="app-tag" v-for="tag in user.tags">
          {{ tag }}
        </van-tag>
      </template>
      <template #footer>
        <van-button size="small" type="primary" plain class="contact-button" @click="showContact(user)">联系我</van-button>
      </template>
    </van-card>
  </van-skeleton>
</template>

<script setup lang="ts">
/**
 * 模块用途：用户卡片列表组件，用于在首页/搜索结果页以卡片形式展示用户信息。
 *
 * 交互：点击“联系我”弹出 Dialog 展示手机号与邮箱。
 *
 * 数据来源：props.userList（来自页面接口返回，例如 GET /user/recommend、GET /user/search/tags、GET /user/match）。
 */
import {Dialog} from "vant";
import {UserType} from "../models/user";

interface UserCardListProps {
  loading: boolean;
  userList: UserType[];
}

const props = withDefaults(defineProps<UserCardListProps>(), {
  loading: true,
  // @ts-ignore
  userList: [] as UserType[],
});

/**
 * 展示用户联系方式弹窗。
 *
 * 交互：用户点击卡片按钮触发；使用 Dialog.alert 弹出联系方式。
 *
 * 数据来源：user.phone 与 user.email 来自 props.userList。
 *
 * @param user 当前卡片对应的用户
 */
const showContact = (user: UserType) => {
  const phone = user.phone || "暂无";
  const email = user.email || "暂无";
  Dialog.alert({
    title: "联系信息",
    message: `手机号：${phone}\n邮箱：${email}`,
  });
};

</script>

<style scoped>
.app-tag {
  margin-right: 8px;
  margin-top: 8px;
  border-color: rgba(37, 99, 235, 0.25);
  color: #2563eb;
}

.contact-button {
  padding: 0 16px;
  height: 36px;
  font-size: 14px;
}
</style>
