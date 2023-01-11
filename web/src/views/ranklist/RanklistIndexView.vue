<template>
    <content-field>
        <table class="table table-hover" >
            <thead>
                <tr>
                    <th>玩家</th>
                    <th>天梯分</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="user in users" :key="user.id">
                    <td>
                        <img :src="user.photo" alt="" class="record-user-photo">
                        &nbsp;
                        <span class="record-user-username">{{user.username}}</span>
                    </td>
                    <td>
                        {{ user.rating }}
                    </td>
                </tr>
            </tbody>
        </table>
        <nav aria-label="..." >
            <ul class="pagination" style="float: right">
                
                <li :class="current_page === 1 ? 'page-item disabled' : 'page-item'">
                    <a class="page-link" href="#" @click="click_page(-1)">上一页</a>
                </li>
                <li :class="current_page === page ? 'page-item active' : 'page-item'" v-for="page in pages" :key="page">
                    <a class="page-link" href="#" @click="click_page(page)">{{ page }}</a>
                </li>
                <li :class="current_page === max_pages ? 'page-item disabled' : 'page-item'">
                    <a class="page-link" href="#" @click="click_page(-2)">下一页</a>
                </li>
            </ul>
        </nav>
    </content-field>
</template>

<script>
import ContentField from "../../components/ContentField.vue"
import { ref } from 'vue';
import { useStore } from 'vuex';
import $ from 'jquery';

export default {
    components: {
        ContentField
    },
    setup() {
        const store = useStore();
        let users = ref([]);
        let total_users = 0;
        let pages = ref([]);
        const count = 10;
        let current_page = ref(1);
        let max_pages = ref(0);

        const click_page = page => {
            if(page === -1) {
                page = current_page.value - 1;
            }else if (page === -2) {
                page = current_page.value + 1;
            }
            if (page >= 1 && page <= max_pages.value) {
                pull_page(page);
            }
        }

        const update_pages = () => {
            max_pages.value = parseInt(Math.ceil(total_users / count));
            let new_pages = [];
            let start = 0;
            let end = 0;
            if(current_page.value <= 2) {
                start = 1;
                end = 5;
            }else if (current_page.value > max_pages.value - 2) {
                start = max_pages.value - 4;
                end = max_pages.value;
            }else {
                start = current_page.value - 2;
                end = current_page.value + 2;
            }
            for (let i = start; i <= end; i ++) {
                if(i >=1 && i <= max_pages.value) {
                    new_pages.push(i);
                }
            }
            pages.value = new_pages;
        }
        const pull_page = page => {
            current_page.value = page;
            $.ajax({
                url: "http://127.0.0.1:5000/ranklist/getlist/",
                type: "get",
                data: {
                    page,
                    count
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token
                },
                success(resp){
                    console.log(resp);
                    users.value = resp.items;
                    total_users = resp.total;
                    update_pages();
                },
                error(resp) {
                    console.log(resp);
                }
            });
        }
        pull_page(current_page.value);

        return {
            users,
            pages,
            current_page,
            max_pages,
            click_page
        }
    }
}
</script>

<style scoped>
img.record-user-photo {
    width: 4vh;
    border-radius: 50%;
}
</style>