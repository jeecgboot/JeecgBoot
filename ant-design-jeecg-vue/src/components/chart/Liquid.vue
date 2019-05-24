<template>
  <div>
    <v-chart
      :forceFit="true"
      :height="height"
      :width="width"
      :data="data"
      :scale="scale"
      :padding="0">
      <v-tooltip/>
      <v-interval
        :shape="['liquid-fill-gauge']"
        position="transfer*value"
        color=""
        :v-style="{
          lineWidth: 8,
          opacity: 0.75
        }"
        :tooltip="[
          'transfer*value',
          (transfer, value) => {
            return {
              name: transfer,
              value,
            };
          },
        ]"
      ></v-interval>
      <v-guide
        v-for="(row, index) in data"
        :key="index"
        type="text"
        :top="true"
        :position="{
          gender: row.transfer,
          value: 45
        }"
        :content="row.value + '%'"
        :v-style="{
          fontSize: 100,
          textAlign: 'center',
          opacity: 0.75,
        }"
      />
    </v-chart>
  </div>
</template>

<script>

  const sourceDataConst = [
    { transfer: '一月', value: 813 },
    { transfer: '二月', value: 233 },
    { transfer: '三月', value: 561 }
  ]

  export default {
    name: 'Liquid',
    props: {
      height: {
        type: Number,
        default: 0
      },
      width: {
        type: Number,
        default: 0
      }
    },
    data() {
      return {
        data: sourceDataConst,
        scale: []
      }
    }
  }
</script>

<style scoped>

</style>