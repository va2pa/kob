import { createRouter, createWebHistory } from 'vue-router'

import PkIndexView from '../views/pk/PkIndexView.vue'
import RecordIndexView from '../views/record/RecordIndexView.vue'
import RanklistIndexView from '../views/ranklist/RanklistIndexView.vue'
import UserBotIndexView from '../views/user/bot/UserBotIndexView.vue'
import NotFound from '../views/error/NotFound.vue'
import UserAccountLoginView from '../views/user/account/UserAccountLoginView.vue'
import UserAccountRegisterView from '../views/user/account/UserAccountRegisterView.vue'
import store from '../store/index'

const routes = [
  {
    path: "/",
    name: "home",
    redirect: "/pk/",
    meta: {
      requestAuth: true
    }
  },
  {
    path: "/pk/",
    name: "pk_index",
    component: PkIndexView,
    meta: {
      requestAuth: true
    }
  },
  {
    path: "/record/",
    name: "record_index",
    component: RecordIndexView,
    meta: {
      requestAuth: true
    }
  },
  {
    path: "/ranklist/",
    name: "ranklist_index",
    component: RanklistIndexView,
    meta: {
      requestAuth: true
    }
  },
  {
    path: "/user/bot/",
    name: "user_bot_index",
    component: UserBotIndexView,
    meta: {
      requestAuth: true
    }
  },
  {
    path: "/user/account/login",
    name: "user_account_login",
    component: UserAccountLoginView,
    meta: {
      requestAuth: false
    }
  },
  {
    path: "/user/account/register",
    name: "user_account_register",
    component: UserAccountRegisterView,
    meta: {
      requestAuth: false
    }
  },
  {
    path: "/404/",
    name: "not_found",
    component: NotFound,
    meta: {
      requestAuth: false
    }
  },
  {
    path: "/:catchAll(.*)",
    redirect: "/404/"
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (store.state.user.is_login) {
    next();
  } else {
    const token = localStorage.getItem("token");
    if (token) {
      store.commit("updateToken", token);
      store.dispatch("getInfo", {
        success: () => {
          next();
        },
        error: () => {
          if (to.meta.requestAuth) {
            next({name: "user_account_login"});
          } else {
            next();
          }
        }
      });
    } else {
      if (to.meta.requestAuth) {
        next({name: "user_account_login"});
      } else {
        next();
      }
    }
  }
});

export default router
