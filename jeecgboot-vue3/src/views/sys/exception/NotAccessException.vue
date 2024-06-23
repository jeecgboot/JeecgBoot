<template>
  <Exception :status="status" />
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { Exception } from '/@/views/sys/exception/index';
  import { ExceptionEnum } from '/@/enums/exceptionEnum';

  const status = ref(ExceptionEnum.PAGE_NOT_ACCESS);
</script>
