<template>
  <div class="login-page">
    <van-form class="login-card" @submit="onSubmit">
      <div class="login-title">账号登录</div>
      <van-cell-group inset>
        <van-field
            v-model="userAccount"
            name="userAccount"
            label="账号"
            placeholder="请输入账号"
            :rules="[{ required: true, message: '请填写用户名' }]"
        />
        <van-field
            v-model="userPassword"
            type="password"
            name="userPassword"
            label="密码"
            placeholder="请输入密码"
            :rules="[{ required: true, message: '请填写密码' }]"
        />
      </van-cell-group>
      <div class="login-actions">
        <van-button class="login-submit" round block type="primary" native-type="submit">
          提交
        </van-button>
      </div>
      <div class="login-footer">
        没有账号？
        <span class="login-link" @click="toRegister">去注册</span>
      </div>
    </van-form>
  </div>
</template>

<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {ref} from "vue";
import myAxios from "../plugins/myAxios";
import {Toast} from "vant";

const router = useRouter();
const route = useRoute();

const userAccount = ref('');
const userPassword = ref('');

const onSubmit = async () => {
  const res = await myAxios.post('/user/login', {
    userAccount: userAccount.value,
    userPassword: userPassword.value,
  })
  console.log(res, '用户登录');
  if (res.code === 0 && res.data) {
    Toast.success('登录成功');
    // 跳转到之前的页面
    const redirectUrl = route.query?.redirect as string ?? '/';
    window.location.href = redirectUrl;
  } else {
    Toast.fail('登录失败');
  }
};

const toRegister = () => {
  router.push('/user/register');
};

</script>

<style scoped>
.login-page {
  min-height: calc(100vh - 140px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 12px 30px;
}

.login-card {
  width: min(520px, 100%);
  min-height: 260px;
  background: rgba(255, 255, 255, 0.92);
  border: 1px solid rgba(37, 99, 235, 0.16);
  box-shadow: 0 16px 36px rgba(15, 23, 42, 0.1);
  border-radius: 18px;
  padding: 22px 24px 18px;
  backdrop-filter: blur(6px);
}

.login-title {
  font-size: 18px;
  font-weight: 700;
  color: var(--app-text);
  margin-bottom: 10px;
  text-align: center;
}

.login-actions {
  margin-top: 12px;
}

.login-submit {
  height: 44px;
  font-size: 16px;
  border-radius: 999px;
}

.login-footer {
  margin-top: 14px;
  text-align: center;
  color: var(--app-text-muted);
  font-size: 13px;
}

.login-link {
  color: var(--app-primary);
  cursor: pointer;
  margin-left: 4px;
}

:deep(.van-cell-group--inset) {
  margin: 0;
  padding: 0;
  border-radius: 14px;
}

:deep(.van-field) {
  padding-top: 12px;
  padding-bottom: 12px;
}

:deep(.van-field__label) {
  font-weight: 600;
}

:deep(.van-field__control) {
  font-size: 15px;
}
::deep(.van-field__value) {
  min-width: 360px;
}

</style>
