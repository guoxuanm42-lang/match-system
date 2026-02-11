<template>
  <div class="login-page">
    <van-form class="login-card" @submit="onSubmit">
      <div class="login-title">账号注册</div>
      <van-cell-group inset>
        <van-field
            v-model="userAccount"
            name="userAccount"
            label="账号"
            placeholder="请输入账号"
            :rules="[{ required: true, message: '请填写账号' }]"
        />
        <van-field
            v-model="planetCode"
            name="planetCode"
            label="编号"
            placeholder="请输入星球编号"
            :rules="[{ required: true, message: '请填写星球编号' }]"
        />
        <van-field
            v-model="userPassword"
            type="password"
            name="userPassword"
            label="密码"
            placeholder="请输入密码"
            :rules="[{ required: true, message: '请填写密码' }]"
        />
        <van-field
            v-model="checkPassword"
            type="password"
            name="checkPassword"
            label="确认密码"
            placeholder="请再次输入密码"
            :rules="[{ required: true, message: '请再次输入密码' }]"
        />
      </van-cell-group>
      <div class="login-actions">
        <van-button class="login-submit" round block type="primary" native-type="submit">
          注册
        </van-button>
      </div>
      <div class="login-footer">
        已有账号？
        <span class="login-link" @click="toLogin">去登录</span>
      </div>
    </van-form>
  </div>
</template>

<script setup lang="ts">
import {ref} from "vue";
import {useRouter} from "vue-router";
import myAxios from "../plugins/myAxios";
import {Toast} from "vant";

const router = useRouter();

const userAccount = ref('');
const planetCode = ref('');
const userPassword = ref('');
const checkPassword = ref('');

const onSubmit = async () => {
  const res = await myAxios.post('/user/register', {
    userAccount: userAccount.value,
    userPassword: userPassword.value,
    checkPassword: checkPassword.value,
    planetCode: planetCode.value,
  })
  if (res.code === 0 && res.data) {
    Toast.success('注册成功，请登录');
    router.push('/user/login');
  } else {
    Toast.fail(res?.description || '注册失败');
  }
};

const toLogin = () => {
  router.push('/user/login');
};
</script>

<style scoped>
.login-page {
  min-height: calc(100vh - 140px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 12px 28px;
}

.login-card {
  width: min(520px, 100%);
  min-height: 300px;
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
</style>
