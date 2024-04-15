<template>
  <div id="app">
<!--      <div class="box">-->
          <router-view></router-view>
<!--      </div>-->
  </div>
</template>

<script>


export default {
  name: 'App',
  components: {
  }
}
</script>

<style>
 @import './assets/style/global.css';
 
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  position: absolute;
  height: 100%;
  width: 100%;
    /*display: flex;*/
    /*justify-content: center;*/
    /*align-items: center;*/
}
</style>
