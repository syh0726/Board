import { createRouter, createWebHistory } from 'vue-router'
import SignUpView from "@/views/SignUpView.vue";
import SignInView from "@/views/SignInView.vue";
import WriteView from "@/components/post/PostWrite.vue";
import PostView from "@/views/PostView.vue";
import PostEditView from "@/views/PostEditView.vue";
import PostListHeader from "@/components/post/PostListHeader.vue";
import CategoryView from "@/views/CategoryView.vue";
import SettingUserView from "@/views/SettingPageView.vue";
import MemberActivity from "@/components/member/MemberActivity.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      alias:['','/'],
      name: 'home',
      component:CategoryView,
      children:[{
        path:'/',
        name:'free',
        component: PostListHeader,
      },{
        path:'/free',
        name:'free',
        component: PostListHeader,
      },{
        path:'/trade',
        name:'trade',
        component: PostListHeader,
      },{
        path:'/information',
        name:'information',
        component: PostListHeader,
      },
      ]
    },
    {
      path:'/posts/write',
      name:'write',
      component: WriteView
    },
    {
      path:'/posts/:postId/edit',
      name:'post-edit',
      component: PostEditView
    },
    {
      path:'/posts/:postId',
      name:'post',
      component: PostView,
    },
    {
      path: '/signUp',
      name: 'signUp',
      component: SignUpView
    },
    {
      path:'/signIn',
      name:'signIn',
      component : SignInView
    },
    {
      path:'/setting',
      name:'setting',
      component : SettingUserView
    },
    {
      path:'/member/:memberId',
      name:"member-activity",
      component: MemberActivity
    }
  ]
})

export default router
