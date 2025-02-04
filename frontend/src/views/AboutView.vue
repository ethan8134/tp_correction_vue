<template>
  <div class="about">
    <ProjetForm />

  </div>
</template>

<script setup>
import ProjetForm from "@/components/ProjetForm.vue";
</script>

<style>
@media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
</style>
