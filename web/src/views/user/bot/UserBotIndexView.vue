<template>
    <content-field>
        我的Bot
    </content-field>
</template>

<script>
import ContentField from "../../../components/ContentField.vue"
import $ from "jquery"
import { useStore } from 'vuex'

export default {
    components: {
        ContentField
    },
    setup(){
        const store = new useStore();
        console.log(store.state.user.token)
        // $.ajax({
        //     url: "http://127.0.0.1:5000/user/bot/add/",
        //     type: 'post',
        //     contentType: "application/json;charset=utf-8",
        //     headers: {
        //         Authorization: "Bearer " + store.state.user.token 
        //     },
        //     data: JSON.stringify({
        //         title: "1",
        //         description: "",
        //         content: ""
        //     }),
        //     success(resp){
        //         console.log(111, resp);
        //     },
        //     error(resp){
        //         console.log(222, resp);
        //     }
        // });
        // $.ajax({
        //     url: "http://127.0.0.1:5000/user/bot/remove/",
        //     type: 'post',
        //     headers: {
        //         Authorization: "Bearer " + store.state.user.token 
        //     },
        //     data: {
        //         botId: 3
        //     },
        //     success(resp){
        //         console.log(111, resp);
        //     },
        //     error(resp){
        //         console.log(222, resp);
        //     }
        // });
        // $.ajax({
        //     url: "http://127.0.0.1:5000/user/bot/update/",
        //     type: 'post',
        //     contentType: "application/json;charset=utf-8",
        //     headers: {
        //         Authorization: "Bearer " + store.state.user.token 
        //     },
        //     data: JSON.stringify({
        //         bot_id: 9,
        //         title: "9990",
        //         description: "999",
        //         content: "999"
        //     }),
        //     success(resp){
        //         console.log(111, resp);
        //     },
        //     error(resp){
        //         console.log(222, resp);
        //     }
        // });
        $.ajax({
            url: "http://127.0.0.1:5000/user/bot/getlist/",
            type: 'get',
            headers: {
                Authorization: "Bearer " + store.state.user.token 
            },
            success(resp){
                console.log(111, resp);
            },
            error(resp){
                console.log(222, resp);
            }
        });
    }
}
</script>

<style scoped>

</style>