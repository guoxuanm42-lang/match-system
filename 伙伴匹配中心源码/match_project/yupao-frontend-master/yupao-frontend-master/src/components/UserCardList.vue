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
