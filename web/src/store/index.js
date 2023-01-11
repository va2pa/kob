import { createStore } from 'vuex'
import ModelUser from './user'
import ModelPk from './pk'
import ModelRecord from './record'

export default createStore({
  state: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user: ModelUser,
    pk: ModelPk,
    record: ModelRecord
  }
})
