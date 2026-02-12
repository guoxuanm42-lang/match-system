<template>
  <div id="teamAddPage">
    <van-form @submit="onSubmit">
      <van-cell-group inset>
      <van-field
            v-model="addTeamData.name"
            name="name"
            label="队伍名"
            placeholder="请输入队伍名"
            :rules="[{ required: true, message: '请输入队伍名' }]"
        />
        <van-field
            v-model="addTeamData.description"
            rows="4"
            autosize
            label="队伍描述"
            type="textarea"
            placeholder="请输入队伍描述"
        />
        <van-field
            is-link
            readonly
            name="datetimePicker"
            label="过期时间"
            :placeholder="addTeamData.expireTime ?? '点击选择过期时间'"
            @click="showPicker = true"
        />
        <van-popup v-model:show="showPicker" position="bottom">
          <van-datetime-picker
              v-model="addTeamData.expireTime"
              @confirm="showPicker = false"
              type="datetime"
              title="请选择过期时间"
              :min-date="minDate"
          />
        </van-popup>
        <van-field name="radio" label="队伍状态">
          <template #input>
            <van-radio-group v-model="addTeamData.status" direction="horizontal">
              <van-radio name="0">公开</van-radio>
              <van-radio name="1">私有</van-radio>
              <van-radio name="2">加密</van-radio>
            </van-radio-group>
          </template>
        </van-field>
        <van-field
            v-if="Number(addTeamData.status) === 2"
            v-model="addTeamData.password"
            type="password"
            name="password"
            label="密码"
            placeholder="请输入队伍密码"
            :rules="[{ required: true, message: '请填写密码' }]"
        />
      </van-cell-group>
      <div style="margin: 16px;">
        <van-button round block type="primary" native-type="submit">
          提交
        </van-button>
      </div>
    </van-form>
  </div>
</template>

<script setup lang="ts">
/**
 * 模块用途：更新队伍页，加载既有队伍信息并提交更新。
 *
 * 交互：页面进入时按 route.query.id 拉取队伍详情；用户提交后更新队伍信息并跳回队伍大厅，失败时 Toast 提示。
 *
 * 数据来源：route.query.id；后端 GET /team/get 获取详情；POST /team/update 提交更新。
 */
import {useRoute, useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import myAxios from "../plugins/myAxios";
import {Toast} from "vant";

const router = useRouter();
const route = useRoute();

// 展示日期选择器
const showPicker = ref(false);

const minDate = new Date();

const id = Number(route.query.id ?? 0);

// 需要用户填写的表单数据
const addTeamData = ref<Record<string, any>>({})

/**
 * 加载队伍详情用于回显表单。
 *
 * 交互：页面进入时触发；失败 Toast 提示。
 *
 * 数据来源：route.query.id；后端 GET /team/get。
 *
 * @returns Promise<void>
 */
const loadTeam = async () => {
  if (!id || Number.isNaN(id) || id <= 0) {
    Toast.fail('加载队伍失败');
    return;
  }
  const res = await myAxios.get("/team/get", {
    params: {
      id,
    }
  });
  if (res?.code === 0) {
    addTeamData.value = res.data;
  } else {
    Toast.fail('加载队伍失败，请刷新重试');
  }
}

// 获取之前的队伍信息
onMounted(async () => {
  await loadTeam();
})

/**
 * 提交队伍更新表单。
 *
 * 交互：用户点击提交触发；成功 Toast 并跳转 /team；失败 Toast 提示。
 *
 * 数据来源：表单数据 addTeamData；后端 POST /team/update。
 *
 * @returns Promise<void>
 */
const onSubmit = async () => {
  const postData = {
    ...addTeamData.value,
    status: Number(addTeamData.value.status)
  }
  // todo 前端参数校验
  const res = await myAxios.post("/team/update", postData);
  if (res?.code === 0 && res.data){
    Toast.success('更新成功');
    router.push({
      path: '/team',
      replace: true,
    });
  } else {
    Toast.success('更新失败');
  }
}
</script>

<style scoped>
#teamPage {

}
</style>
