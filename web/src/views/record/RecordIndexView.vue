<template>
    <content-field>
        <table class="table table-hover" style="text-align: center;">
            <thead>
                <tr>
                    <th>A</th>
                    <th>B</th>
                    <th>对战结果</th>
                    <th>对战时间</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="record in records" :key="record.record.id">
                    <td>
                        <img :src="record.a_photo" alt="" class="record-user-photo">
                        &nbsp;
                        <span class="record-user-username">{{record.a_username}}</span>
                    </td>
                    <td>
                        <img :src="record.b_photo" alt="" class="record-user-photo">
                        &nbsp;
                        <span class="record-user-username">{{record.b_username}}</span>
                    </td>
                    <td>
                        {{ record.result}}
                    </td>
                    <td>
                        {{ record.record.create_time }}
                    </td>
                    <td>
                        <button type="button" class="btn btn-secondary">查看录像</button>
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
import { useStore } from 'vuex';
import { ref } from 'vue';
import $ from 'jquery';

export default {
    components: {
        ContentField
    },
    setup() {
        const store = useStore();
        const count = 10;
        let current_page = ref(1);
        let records = ref([]);
        let total_record = 0;
        let pages = ref([]);
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
        console.log(total_record);
        const pull_page = page => {
            current_page.value = page;
            console.log(current_page);
            $.ajax({
                url: "http://127.0.0.1:5000/record/getlist/",
                type: "get",
                data: {
                    page,
                    count
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token
                },
                success(resp){
                    records.value = resp.items;
                    total_record = resp.total;
                    console.log(records.value);
                    max_pages.value = parseInt(Math.ceil(total_record / count));

                    update_pages();
                },
                error(resp) {
                    console.log(resp);
                }
            });
        }
        pull_page(current_page.value);
        return {
            records,
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