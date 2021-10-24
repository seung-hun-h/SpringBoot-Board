import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: "/",
    redirect: "/posts",
    name: "TheContainer",
    component: () => import("@/components/layout/TheContainer"),
    children: [
      {
        path: "/posts",
        name: "Posts",
        component: () => import("@/components/posts/Posts")
      },
      {
        path: "/posts/:postId",
        name: "PostsDetail",
        component: () => import("@/components/posts/PostsDetail")
      },
      {
        path: "/posts/reg",
        name: "PostsReg",
        component: () => import("@/components/posts/PostsReg")
      },
      {
        path: "/login",
        name: "Login",
        component: () => import("@/components/layout/Login")
      },
      {
        path: "/join",
        name: "Join",
        component: () => import("@/components/layout/Join")
      }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
