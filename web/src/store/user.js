import $ from 'jquery'

export default {
    state: {
        id: "",
        username: "",
        photo: "",
        token: "",
        is_login: false
    },
    mutations: {
        updateUser(state, user){
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;
            state.is_login = user.is_login;
        },
        updateToken(state, token){
            state.token = token;
        },
        logout(state){
            state.id = "";
            state.username = "";
            state.photo = "";
            state.token = "";
            state.is_login = false;
            localStorage.removeItem("token");
        }
    },
    actions: {
        login(conotext, data){
            $.ajax({
                url: "http://127.0.0.1:5000/user/account/token/",
                type: "post",
                data: {
                    username: data.username,
                    password: data.password,
                },
                success(resp){
                    localStorage.setItem("token", resp.token);
                    conotext.commit("updateToken", resp.token);
                    data.success(resp);
                },
                error(resp){
                    data.error(resp);
                }
            });
        },
        getInfo(conotext, data){
            $.ajax({
                url: "http://127.0.0.1:5000/user/account/info/",
                tpye: "get",
                headers: {
                    Authorization: "Bearer " + conotext.state.token
                },
                success(resp){
                    conotext.commit("updateUser", {
                        ...resp,
                        is_login: true
                    });
                    data.success(resp);
                },
                error(resp){
                    console.log(conotext.state.token);
                    data.error(resp);
                }
            });
        }
    },
    modules: {
    }
}