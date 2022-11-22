<template>
  <div>
    <div>
      用户名: {{ username }}
    </div>
    <div>
      得分: {{ rating }}
    </div>
  </div>
  <router-view/>
</template>

<script>
  import { ref } from "vue";
  import $ from "jquery";
  export default {
    name: "App",
    setup(){
      let username = ref("");
      let rating = ref("");
      $.ajax({
        url: "http://localhost:5000/user/info/",
        method: "GET",
        success: (res) => {
          username.value = res.username;
          rating.value = res.rating;
        }
      });
      return{
        username,
        rating
      }
  }
}
</script>

<style>
  body {
    background-image: url("assets/image/background.png");
    background-size: cover;
  }
</style>