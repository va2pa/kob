<template>
  <Navbar />
  <router-view/>
</template>

<script>
  import Navbar from "./components/NavBar.vue"
  import "bootstrap/dist/css/bootstrap.min.css"
  import "bootstrap/dist/js/bootstrap"
  // import $ from "jquery"
  export default {
    name: "App",
    components: {
      Navbar,
    },
    setup(){
          // $.ajax({
          //   url: "http://127.0.0.1:5000/user/account/token/",
          //   type: "post",
          //   data: {
          //       username: "li1",
          //       password: "li"
          //     },
          //   success(resp) {
          //     console.log(resp);
          //   },
          //   error(resp) {
          //     console.log(resp);
          //   }
          // });

          // $.ajax({
          //   url: "http://127.0.0.1:5000/user/account/info/",
          //   type: "get",
          //   headers: {
          //     Authorization: "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1ZjliMWQzYjBhODU0YmVmOGFjMThmZmZjMTQyYzZhOSIsInN1YiI6IjIiLCJpc3MiOiJzZyIsImlhdCI6MTY3MDA1MzQwMCwiZXhwIjoxNjcxMjYzMDAwfQ.-iDHkzOFefHIzZouUXMMTL02vaDNj-5w9ez7ezvOvm4"
          //   },
          //   success(resp){
          //     console.log(resp);
          //   },
          //   error(resp){
          //     console.log(resp);
          //   }
          // });

          // $.ajax({
          //   url: "http://127.0.0.1:5000/user/account/register/",
          //   type: "post",
          //   data: {
          //     username: "wwwww",
          //     password: "12345",
          //     confirmPassword: "12345"
          //   },
          //   success(resp){
          //     console.log(resp);
          //   },
          //   error(resp){
          //     console.log(resp);
          //   }
          // });
    }
}
</script>

<style>
  body {
    background-image: url("assets/image/background.png");
    background-size: cover;
  }
</style>