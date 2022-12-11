<template>
    <div class="container">
        <div class="row">
            <div class="col-3">
                <div class="card" style="margin-top: 20px">
                    <div class="card-body">
                        <img :src="$store.state.user.photo" alt="" style="width: 100%">
                    </div>
                </div>
            </div>
            <div class="col-9">
                <div class="card" style="margin-top: 20px">
                    <div class="card-header">
                        <span style="font-size: 135%">我的Bot</span>
                        <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal" data-bs-target="#add-bot-btn">
                            创建Bot
                        </button>

                        <div class="modal fade" id="add-bot-btn" tabindex="-1">
                            <div class="modal-dialog modal-xl">
                                <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title">创建Bot</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <div class="mb-3">
                                        <label for="add-bot-title" class="form-label">名称</label>
                                        <input v-model="newbot.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入Bot名称">
                                    </div>
                                    <div class="mb-3">
                                        <label for="add-bot-description" class="form-label">简介</label>
                                        <textarea v-model="newbot.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入Bot简介"></textarea>
                                    </div>
                                    <div class="mb-3">
                                        <label for="add-bot-code" class="form-label">代码</label>
                                        <VAceEditor
                                            v-model:value="newbot.content"
                                            @init="editorInit"
                                            lang="c_cpp"
                                            :options="{
                                                fontSize: 18,
                                            }"
                                            theme="textmate"
                                            style="height: 300px" />
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <div class="error_message">{{ newbot.error_message }}</div>
                                    <button type="button" class="btn btn-primary" @click="add_bot">创建</button>
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>名称</th>
                                    <th>创建时间</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="bot in bots" :key="bot.id">
                                    <td>{{ bot.title }}</td>
                                    <td>{{ bot.create_time }}</td>
                                    <td>
                                        <button type="button" class="btn btn-secondary" style="margin-right: 10px" data-bs-toggle="modal" :data-bs-target="'#update-bot-btn-' + bot.id">修改</button>
                                        <button type="button" class="btn btn-danger" @click="remove_bot(bot)">删除</button>
                                        
                                        <div class="modal fade" :id="'update-bot-btn-' + bot.id" tabindex="-1">
                                            <div class="modal-dialog modal-xl">
                                                <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title">修改Bot</h5>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                </div>
                                                <div class="modal-body">
                                                    <div class="mb-3">
                                                        <label for="add-bot-title" class="form-label">名称</label>
                                                        <input v-model="bot.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入Bot名称">
                                                    </div>
                                                    <div class="mb-3">
                                                        <label for="add-bot-description" class="form-label">简介</label>
                                                        <textarea v-model="bot.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入Bot简介"></textarea>
                                                    </div>
                                                    <div class="mb-3">
                                                        <label for="add-bot-code" class="form-label">代码</label>
                                                        <VAceEditor
                                                            v-model:value="bot.content"
                                                            @init="editorInit"
                                                            lang="c_cpp"
                                                            theme="textmate"
                                                            style="height: 300px" />
                                                    </div>
                                                </div>
                                                <div class="modal-footer">
                                                    <div class="error_message">{{ bot.error_message }}</div>
                                                    <button type="button" class="btn btn-primary" @click="update_bot(bot)">修改</button>
                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                                </div>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
</template>

<script>
import { ref, reactive } from 'vue'
import $ from 'jquery'
import { useStore } from 'vuex'
import { Modal } from 'bootstrap/dist/js/bootstrap'
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';

export default {
    components:{
        VAceEditor
    },
    setup(){
        ace.config.set(
            "basePath", 
            "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")
        const store = useStore();
        let bots = ref([]);
        let newbot = reactive({
            error_message: "",
            title: "",
            description: "",
            content: ""
        });
        const refresh_bots = () => {
            $.ajax({
                url: "http://127.0.0.1:5000/user/bot/getlist/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token
                },
                success(resp){
                    bots.value = resp;
                }
            });
        }
        refresh_bots();

        const add_bot = () => {
            newbot.error_message = "";
            $.ajax({
                url: "http://127.0.0.1:5000/user/bot/add/",
                type: "post",
                contentType: "application/json;charset=utf-8",
                headers: {
                    Authorization: "Bearer " + store.state.user.token
                },
                data: JSON.stringify({
                    title: newbot.title,
                    description: newbot.description,
                    content: newbot.content
                }),
                success(){
                    newbot.title = "";
                    newbot.description = "";
                    newbot.content = "";
                    Modal.getInstance("#add-bot-btn").hide();
                    refresh_bots();
                },
                error(resp){
                    newbot.error_message = resp.responseJSON.message;
                }
            });
        }

        const update_bot = (bot) => {
            newbot.error_message = "";
            $.ajax({
                url: "http://127.0.0.1:5000/user/bot/update/",
                type: "post",
                contentType: "application/json;charset=utf-8",
                headers: {
                    Authorization: "Bearer " + store.state.user.token
                },
                data: JSON.stringify({
                    bot_id: bot.id,
                    title: bot.title,
                    description: bot.description,
                    content: bot.content
                }),
                success(){
                    Modal.getInstance("#update-bot-btn-" + bot.id).hide();
                    refresh_bots();
                },
                error(resp){
                    bot.error_message = resp.responseJSON.message;
                }
            });
        }

        const remove_bot = (bot) => {
            $.ajax({
                url: "http://127.0.0.1:5000/user/bot/remove/",
                type: "post",
                headers: {
                    Authorization: "Bearer " + store.state.user.token
                },
                data: {
                    botId: bot.id
                },
                success(){
                    refresh_bots();
                }
            });
        }
        return {
            bots,
            newbot,
            add_bot,
            remove_bot,
            update_bot
        }
    }
}
</script>

<style scoped>
div.error_message{
    color: red;
}
</style>