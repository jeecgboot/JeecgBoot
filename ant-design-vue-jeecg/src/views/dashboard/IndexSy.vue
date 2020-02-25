<template>
  <div class="IndexSy">
    <div class="block">
      <Part1 />
    </div>
    <div class="block">
      <Part2 />
    </div>
    <div class="block">
      <Part3 />
    </div>
    <div class="block">
      <Part4 />
    </div>
    <div class="block">
      <Part5 />
    </div>
    <div class="block">
      <Part6 />
    </div>
  </div>
</template>

<script>
import Part1 from '@/components/indexSy/Part1'
import Part2 from '@/components/indexSy/Part2'
import Part3 from '@/components/indexSy/Part3'
import Part4 from '@/components/indexSy/Part4'
import Part5 from '@/components/indexSy/Part5'
import Part6 from '@/components/indexSy/Part6'
export default {
  components: {
    Part1,
    Part2,
    Part3,
    Part4,
    Part5,
    Part6,
  },
  data () {
    return {}
  },
  methods: {},
  mounted () { }
}
</script>

<style scoped lang="less">
.IndexSy {
  width: 100%;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  .block {
    margin-bottom: 20px;
    background-color: #fff;
    width: 49%;
    height: 370px;
  }
}
</style>