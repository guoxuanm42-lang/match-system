<template>
  <div
      id="teamCardList"
  >
    <van-card
        v-for="team in props.teamList"
        :thumb="ikun"
        :desc="team.description"
        :title="`${team.name}`"
    >
      <template #tags>
        <van-tag plain type="primary" class="app-tag">
          {{
            teamStatusEnum[team.status]
          }}
        </van-tag>
      </template>
      <template #bottom>
        <div>
          {{ `队伍人数: ${team.hasJoinNum}/${team.maxNum}` }}
        </div>
        <div v-if="team.expireTime">
          {{ '过期时间: ' + team.expireTime }}
        </div>
        <div>
          {{ '创建时间: ' + team.createTime }}
        </div>
      </template>
      <template #footer>
        <van-button size="small" type="primary" v-if="team.userId !== currentUser?.id && !team.hasJoin" plain
                    @click="preJoinTeam(team)">
          加入队伍
        </van-button>
        <van-button v-if="team.userId === currentUser?.id" size="small" plain
                    @click="doUpdateTeam(team.id)">更新队伍
        </van-button>
        <!-- 仅加入队伍可见 -->
        <van-button v-if="team.userId !== currentUser?.id && team.hasJoin" size="small" plain
                    @click="doQuitTeam(team.id)">退出队伍
        </van-button>
        <van-button v-if="team.userId === currentUser?.id" size="small" type="danger" plain
                    @click="doDeleteTeam(team.id)">解散队伍
        </van-button>
      </template>
    </van-card>
    <van-dialog v-model:show="showPasswordDialog" title="请输入密码" show-cancel-button @confirm="doJoinTeam" @cancel="doJoinCancel">
      <van-field v-model="password" placeholder="请输入密码"/>
    </van-dialog>
  </div>

</template>

<script setup lang="ts">
/**
 * 模块用途：队伍卡片列表组件，展示队伍信息并提供加入/退出/更新/解散等操作入口。
 *
 * 交互：点击加入队伍会根据队伍状态直接加入或弹出密码输入框；退出/解散会调用后端接口并 Toast 提示；更新跳转到更新页。
 *
 * 数据来源：props.teamList（来自页面接口返回，例如 GET /team/list）；当前用户来自 GET /user/current；操作接口包括 POST /team/join、POST /team/quit、POST /team/delete。
 */
import {TeamType} from "../models/team";
import {teamStatusEnum} from "../constants/team";
import ikun from '../assets/ikun.png';
import myAxios from "../plugins/myAxios";
import {Dialog, Toast} from "vant";
import {onMounted, ref} from "vue";
import {getCurrentUser} from "../services/user";
import {useRouter} from "vue-router";

interface TeamCardListProps {
  teamList: TeamType[];
}

const props = withDefaults(defineProps<TeamCardListProps>(), {
  // @ts-ignore
  teamList: [] as TeamType[],
});

const showPasswordDialog = ref(false);
const password = ref('');
const joinTeamId = ref(0);
const currentUser = ref();

const router = useRouter();

onMounted(async () => {
  currentUser.value = await getCurrentUser();
})

/**
 * 预处理加入队伍动作。
 *
 * 交互：用户点击“加入队伍”触发；公开队伍直接加入，加密队伍弹出密码输入框。
 *
 * 数据来源：team 来自 props.teamList，status 字段决定后续交互分支。
 *
 * @param team 当前操作的队伍
 */
const preJoinTeam = (team: TeamType) => {
  joinTeamId.value = team.id;
  if (team.status === 0) {
    doJoinTeam()
  } else {
    showPasswordDialog.value = true;
  }
}

/**
 * 取消加入队伍（重置弹窗状态）。
 *
 * 交互：用户关闭/取消密码弹窗触发；清空密码并重置 joinTeamId。
 *
 * 数据来源：本地状态 joinTeamId/password/showPasswordDialog。
 */
const doJoinCancel = () => {
  joinTeamId.value = 0;
  password.value = '';
}

/**
 * 加入队伍。
 *
 * 交互：在公开队伍场景下由 preJoinTeam 直接触发；在加密队伍场景下由密码弹窗 confirm 触发；
 * 成功 Toast 并重置弹窗状态，失败 Toast 展示后端错误描述。
 *
 * 数据来源：joinTeamId 与 password 来自本地状态；后端 POST /team/join。
 *
 * @returns Promise<void>
 */
const doJoinTeam = async () => {
  if (!joinTeamId.value) {
    return;
  }
  const res = await myAxios.post('/team/join', {
    teamId: joinTeamId.value,
    password: password.value
  });
  if (res?.code === 0) {
    Toast.success('加入成功');
    doJoinCancel();
  } else {
    Toast.fail('加入失败' + (res.description ? `，${res.description}` : ''));
  }
}

/**
 * 跳转至更新队伍页。
 *
 * 交互：队长点击“更新队伍”触发，路由跳转到 /team/update 并携带 id。
 *
 * 数据来源：参数 id 来自 props.teamList 中的队伍信息。
 *
 * @param id 队伍 id
 */
const doUpdateTeam = (id: number) => {
  router.push({
    path: '/team/update',
    query: {
      id,
    }
  })
}

/**
 * 退出队伍。
 *
 * 交互：成员点击“退出队伍”触发；成功 Toast，失败 Toast 展示后端错误描述。
 *
 * 数据来源：后端 POST /team/quit。
 *
 * @param id 队伍 id
 * @returns Promise<void>
 */
const doQuitTeam = async (id: number) => {
  const res = await myAxios.post('/team/quit', {
    teamId: id
  });
  if (res?.code === 0) {
    Toast.success('操作成功');
  } else {
    Toast.fail('操作失败' + (res.description ? `，${res.description}` : ''));
  }
}

/**
 * 解散队伍。
 *
 * 交互：队长点击“解散队伍”触发；成功 Toast，失败 Toast 展示后端错误描述。
 *
 * 数据来源：后端 POST /team/delete。
 *
 * @param id 队伍 id
 * @returns Promise<void>
 */
const doDeleteTeam = async (id: number) => {
  const res = await myAxios.post('/team/delete', {
    id,
  });
  if (res?.code === 0) {
    Toast.success('操作成功');
  } else {
    Toast.fail('操作失败' + (res.description ? `，${res.description}` : ''));
  }
}

</script>

<style scoped>
#teamCardList :deep(.app-tag) {
  margin-right: 8px;
  margin-top: 8px;
  border-color: rgba(37, 99, 235, 0.25);
  color: #2563eb;
}

#teamCardList :deep(.van-image__img) {
  height: 128px;
  object-fit: unset;
}
</style>
