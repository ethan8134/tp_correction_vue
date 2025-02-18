<template>
  <div class="error-message">
    <p>{{ message }}</p>
  </div>
</template>

<script>
export default {
  props: {
    message: String,
  },
};
</script>

<style scoped>
.error-message {
  color: red;
  font-weight: bold;
}
</style>
