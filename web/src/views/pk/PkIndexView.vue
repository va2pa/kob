<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'"/>
    <MatchGround v-if="$store.state.pk.status === 'matching'"/>
    <ResultBoard v-if="$store.state.pk.loser != 'none'"/>
</template>

<script>
import PlayGround from "../../components/PlayGround.vue"
import MatchGround from "../../components/MatchGround.vue"
import ResultBoard from "../../components/ResultBoard.vue"
import { onMounted, onUnmounted } from 'vue'
import { useStore } from 'vuex'
export default {
    components: {
        PlayGround,
        MatchGround,
        ResultBoard
    },
    setup(){
        const store = useStore();
        const socket_url = `ws://127.0.0.1:5000/websocket/${store.state.user.token}/`;
        let socket = null;

        store.commit("updateLoser", "none");
        store.commit("updateIsRecord", false);

        onMounted(() => {
            store.commit("updateOpponent", {
                username: "我的对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
            });
            socket = new WebSocket(socket_url);
            socket.onopen = () => {
                console.log("open");
                store.commit("updateSocket", socket);
            };

            socket.onmessage = msg => {
                const data = JSON.parse(msg.data);
                if (data.event === "match-success") {
                    store.commit("updateOpponent", {
                        username: data.opponent_username,
                        photo: data.opponent_photo
                    });
                    store.commit("updateGame",data.game);
                    setTimeout(() => {
                        store.commit("updateStatus", "playing");
                    }, 2000);
                    console.log(data);
                }else if(data.event === "move") {
                    console.log(data);
                    const gameObject = store.state.pk.gameObject;
                    const [snake0, snake1] = gameObject.snakes;
                    snake0.set_direction(data.a_direction);
                    snake1.set_direction(data.b_direction);
                }else if(data.event === "result") {
                    console.log(data);
                    const gameObject = store.state.pk.gameObject;
                    const [snake0, snake1] = gameObject.snakes;
                    if(data.loser === "all" || data.loser === "A") {
                        snake0.status = "die";
                    }
                    if(data.loser === "all" || data.loser === "B") {
                        snake1.status = "die";
                    }
                    store.commit("updateLoser", data.loser);
                }
            };

            socket.onclose = () => {
                console.log("close");
            }
        });

        onUnmounted(() => {
            socket.close();
            store.commit("updateStatus", "matching");
        });
    }
}
</script>

<style scoped>

</style>