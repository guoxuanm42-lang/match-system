<template>
  <van-form @submit="onSubmit">
    <template v-if="editUser.editKey === 'gender'">
      <van-field name="gender" label="性别">
        <template #input>
          <van-radio-group v-model="editUser.currentValue" direction="horizontal">
            <van-radio name="0">男</van-radio>
            <van-radio name="1">女</van-radio>
          </van-radio-group>
        </template>
      </van-field>
    </template>
    <template v-else-if="editUser.editKey === 'tags'">
      <van-divider content-position="left">已选标签</van-divider>
      <div v-if="selectedTags.length === 0" class="tag-empty">请选择标签</div>
      <van-row gutter="12" class="tag-row">
        <van-col v-for="tag in selectedTags" :key="tag">
          <van-tag closeable type="primary" @close="doClose(tag)">
            {{ tag }}
          </van-tag>
        </van-col>
      </van-row>

      <van-divider content-position="left">性别（单选）</van-divider>
      <van-radio-group v-model="selectedGender" direction="horizontal" class="tag-group">
        <van-radio v-for="item in genderOptions" :key="item" :name="item">{{ item }}</van-radio>
      </van-radio-group>

      <van-divider content-position="left">年级（单选）</van-divider>
      <van-radio-group v-model="selectedGrade" direction="horizontal" class="tag-group">
        <van-radio v-for="item in gradeOptions" :key="item" :name="item">{{ item }}</van-radio>
      </van-radio-group>

      <van-divider content-position="left">方向（可多选 / 可自定义）</van-divider>
      <van-checkbox-group v-model="selectedDirections" class="tag-group">
        <van-checkbox v-for="item in directionOptions" :key="item" :name="item">{{ item }}</van-checkbox>
      </van-checkbox-group>
      <div class="tag-custom">
        <van-field v-model="customDirection" placeholder="自定义方向，例如：数据工程" />
        <van-button size="small" type="primary" plain @click="addDirection">添加</van-button>
      </div>
    </template>
    <van-field
        v-else
        v-model="editUser.currentValue"
        :name="editUser.editKey"
        :label="editUser.editName"
        :placeholder="`请输入${editUser.editName}`"
    />
    <div style="margin: 16px;">
      <van-button round block type="primary" native-type="submit">
        提交
      </van-button>
    </div>
  </van-form>
</template>

<script setup lang="ts">
/**
 * 模块用途：用户字段编辑页，支持基础字段编辑与“标签”组合选择编辑。
 *
 * 交互：从上一页通过 query 传入 editKey/editName/currentValue；用户提交后调用更新接口，成功后返回上一页并 Toast 提示。
 *
 * 数据来源：route.query（编辑上下文）；GET /user/current 获取当前用户 id；POST /user/update 提交更新。
 */
import {useRoute, useRouter} from "vue-router";
import { ref, computed } from "vue";
import myAxios from "../plugins/myAxios";
import {Toast} from "vant";
import {getCurrentUser} from "../services/user";

const route = useRoute();
const router = useRouter();

const editUser = ref({
  editKey: String(Array.isArray(route.query.editKey) ? route.query.editKey[0] : (route.query.editKey ?? '')),
  currentValue: String(Array.isArray(route.query.currentValue) ? route.query.currentValue[0] : (route.query.currentValue ?? '')),
  editName: String(Array.isArray(route.query.editName) ? route.query.editName[0] : (route.query.editName ?? '')),
})

if (editUser.value.editKey === 'gender') {
  editUser.value.currentValue = String(editUser.value.currentValue);
}

const genderOptions = ['男', '女'];
const gradeOptions = ['大一', '大二', '大三', '大四', '大五', '大六'];
const directionOptions = ref(['前端', '后端', '产品', '设计', '算法']);

const selectedGender = ref('');
const selectedGrade = ref('');
const selectedDirections = ref<string[]>([]);
const customDirection = ref('');

if (editUser.value.editKey === 'tags') {
  try {
    const parsed = JSON.parse(String(editUser.value.currentValue));
    if (Array.isArray(parsed)) {
      parsed.forEach((tag: string) => {
        if (tag === '大3') {
          return;
        }
        if (genderOptions.includes(tag)) {
          selectedGender.value = tag;
          return;
        }
        if (gradeOptions.includes(tag)) {
          selectedGrade.value = tag;
          return;
        }
        if (!directionOptions.value.includes(tag)) {
          directionOptions.value.push(tag);
        }
        selectedDirections.value.push(tag);
      });
    }
  } catch (error) {
    // ignore
  }
}

selectedDirections.value = selectedDirections.value.filter(tag => tag !== '大3');

const selectedTags = computed(() => {
  const tags: string[] = [];
  if (selectedGender.value) tags.push(selectedGender.value);
  if (selectedGrade.value) tags.push(selectedGrade.value);
  tags.push(...selectedDirections.value);
  return tags;
});

/**
 * 提交用户字段更新。
 *
 * 交互：用户点击提交触发表单提交；成功 Toast 并返回上一页；失败 Toast 提示。
 *
 * 数据来源：编辑上下文来自 route.query；后端 POST /user/update；tags 字段会以 JSON 字符串提交。
 *
 * @returns Promise<void>
 */
const onSubmit = async () => {
  const currentUser = await getCurrentUser();

  if (!currentUser) {
    Toast.fail('用户未登录');
    return;
  }

  console.log(currentUser, '当前用户')

  const editKey = editUser.value.editKey as string;
  const currentValue = editKey === 'gender'
      ? Number(editUser.value.currentValue)
      : editKey === 'tags'
          ? JSON.stringify(selectedTags.value)
          : editUser.value.currentValue;

  if (editKey === 'gender' && Number.isNaN(currentValue)) {
    Toast.fail('请选择性别');
    return;
  }

  if (editKey === 'tags' && selectedTags.value.length === 0) {
    Toast.fail('请至少选择一个标签');
    return;
  }

  const res = await myAxios.post('/user/update', {
    'id': currentUser.id,
    [editKey]: currentValue,
  })
  console.log(res, '更新请求');
  if (res.code === 0 && res.data > 0) {
    Toast.success('修改成功');
    router.back();
  } else {
    Toast.fail('修改错误');
  }
};

/**
 * 从已选标签中移除一个标签。
 *
 * 交互：用户点击标签的关闭按钮触发；会更新单选/多选的绑定状态，从而影响最终提交的 tags。
 *
 * 数据来源：selectedGender/selectedGrade/selectedDirections。
 *
 * @param tag 要移除的标签名
 */
const doClose = (tag: string) => {
  if (selectedGender.value === tag) {
    selectedGender.value = '';
    return;
  }
  if (selectedGrade.value === tag) {
    selectedGrade.value = '';
    return;
  }
  selectedDirections.value = selectedDirections.value.filter(item => item !== tag);
}

/**
 * 添加自定义方向标签到可选项并选中。
 *
 * 交互：用户点击“添加”触发；会把输入框内容加入方向选项并选中，然后清空输入框。
 *
 * 数据来源：customDirection（输入框）与 directionOptions/selectedDirections（本地状态）。
 */
const addDirection = () => {
  const val = customDirection.value.trim();
  if (!val) return;
  if (!directionOptions.value.includes(val)) {
    directionOptions.value.push(val);
  }
  if (!selectedDirections.value.includes(val)) {
    selectedDirections.value.push(val);
  }
  customDirection.value = '';
}

</script>

<style scoped>
.tag-row {
  padding: 0 12px;
  margin-bottom: 12px;
}

.tag-group {
  padding: 0 12px;
  margin-bottom: 12px;
  display: flex;
  flex-wrap: wrap;
  gap: 8px 12px;
}

.tag-custom {
  padding: 0 12px 6px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.tag-custom :deep(.van-field) {
  flex: 1;
}

.tag-empty {
  padding: 0 12px;
  color: var(--app-text-muted);
  font-size: 13px;
  margin-bottom: 8px;
}
</style>
