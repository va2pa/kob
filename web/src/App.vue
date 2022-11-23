<template>
  <Navbar />
  <router-view/>
</template>

<script>
  import Navbar from "./components/NavBar.vue"
  import "bootstrap/dist/css/bootstrap.min.css"
  import "bootstrap/dist/js/bootstrap"
  export default {
    name: "App",
    components: {
      Navbar,
    }
}
</script>

<style>
  body {
    background-image: url("assets/image/background.png");
    background-size: cover;
  }
</style>