<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default  {
  name:  'App',
  created()
  {
    let uri = window.location.search.substring(1);
    let params = new URLSearchParams(uri);
    console.log(uri, params.get("var_name"));
  }
}
</script>
