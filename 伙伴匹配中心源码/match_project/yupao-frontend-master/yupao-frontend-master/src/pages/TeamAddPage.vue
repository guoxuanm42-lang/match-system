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
        <van-field name="stepper" label="最大人数">
          <template #input>
            <van-stepper v-model="addTeamData.maxNum" max="10" min="3"/>
          </template>
        </van-field>
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
 * 模块用途：创建队伍页，填写队伍信息并提交创建请求。
 *
 * 交互：用户填写表单并提交；成功 Toast 并跳转队伍大厅；失败 Toast 提示（含未登录/后端异常等）。
 *
 * 数据来源：表单输入；后端接口 POST /team/add。
 */
import {useRouter} from "vue-router";
import {ref} from "vue";
import myAxios from "../plugins/myAxios";
import {Toast} from "vant";

const router = useRouter();
// 展示日期选择器
const showPicker = ref(false);

const initFormData = {
  "name": "",
  "description": "",
  "expireTime": null,
  "maxNum": 3,
  "password": "",
  "status": 0,
}

const minDate = new Date();

// 需要用户填写的表单数据
const addTeamData = ref({...initFormData})

/**
 * 提交创建队伍表单。
 *
 * 交互：用户点击提交触发；成功 Toast 并跳转 /team；失败 Toast 提示。
 *
 * 数据来源：表单输入；后端 POST /team/add。
 *
 * @returns Promise<void>
 */
const onSubmit = async () => {
  if (!addTeamData.value.expireTime) {
    Toast.fail('请选择过期时间');
    return;
  }
  const postData = {
    ...addTeamData.value,
    status: Number(addTeamData.value.status),
  };
  // todo 前端参数校验
  try {
    const res = await myAxios.post("/team/add", postData);
    if (res?.code === 0 && res.data) {
      Toast.success('添加成功');
      router.push({
        path: '/team',
        replace: true,
      });
    } else {
      Toast.fail(res?.description || '添加失败');
    }
  } catch (error) {
    console.error('创建队伍失败', error);
    Toast.fail('提交失败，请检查后端是否正常或已登录');
  }
}
</script>

<style scoped>
#teamPage {

}
</style>
