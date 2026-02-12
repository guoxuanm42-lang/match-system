/**
 * 模块用途：集中定义前端路由表，用于 Vue Router 注册页面与标题信息。
 *
 * 交互：用户通过底部导航/按钮跳转触发路由切换；布局组件会基于路由元信息展示标题。
 *
 * 数据来源：页面组件来自 src/pages；路径与标题为本地静态配置。
 */
import Index from "../pages/Index.vue";
import Team from "../pages/TeamPage.vue";
import UserPage from "../pages/UserPage.vue";
import UserUpdatePage from "../pages/UserUpdatePage.vue";
import UserTeamJoinPage from "../pages/UserTeamJoinPage.vue";
import UserTeamCreatePage from "../pages/UserTeamCreatePage.vue";
import SearchPage from "../pages/SearchPage.vue";
import SearchResultPage from "../pages/SearchResultPage.vue";
import UserEditPage from "../pages/UserEditPage.vue";
import UserLoginPage from "../pages/UserLoginPage.vue";
import UserRegisterPage from "../pages/UserRegisterPage.vue";
import TeamAddPage from "../pages/TeamAddPage.vue";
import TeamUpdatePage from "../pages/TeamUpdatePage.vue";


const routes = [
    { path: '/', component: Index },
    { path: '/team', title: '找队伍', component: Team },
    { path: '/team/add', title: '创建队伍', component: TeamAddPage },
    { path: '/team/update', title: '更新队伍', component: TeamUpdatePage },
    { path: '/user', title: '个人信息', component: UserPage },
    { path: '/search', title: '找伙伴', component: SearchPage },
    { path: '/user/list', title: '用户列表', component: SearchResultPage },
    { path: '/user/edit', title: '编辑信息', component: UserEditPage },
    { path: '/user/login', title: '登录', component: UserLoginPage },
    { path: '/user/register', title: '注册', component: UserRegisterPage },
    { path: '/user/update', title: '更新信息', component: UserUpdatePage },
    { path: '/user/team/join', title: '加入队伍', component: UserTeamJoinPage },
    { path: '/user/team/create', title: '创建队伍', component: UserTeamCreatePage },
]

export default routes;
