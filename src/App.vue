

<template>
  <div id="app">
    <SurveyNav />
    <router-view />
  </div>
</template>

<script>
import SurveyNav from './components/SurveyNav.vue';

export default {
  components: {
    SurveyNav,
  },
};
</script>

<style>
body {
  font-family: 'San Francisco', Arial, sans-serif;
}
</style>
