<template>
  <div class="coord-picker">
    <div class="map-container">
      <a-input-group compact style="display: flex">
        <a-auto-complete
          v-model="query"
          :data-source="tips"
          placeholder="请输入小区名称"
          style="flex: 1"
          @search="autoComplete"
        />
        <a-button
          @click="search(true)"
          type="primary"
        >
          确认
        </a-button>
      </a-input-group>
      <amap
        ref="myMap"
        cache-key="coord-picker-map"
        mmap-style="amap://styles/whitesmoke"
        async
        :center.sync="center"
        :zoom.sync="zoom"
        is-hotspot
      >
        <amap-marker v-if="position" :position.sync="position" draggable />
      </amap>
    </div>
  </div>
</template>

<script>
import { loadAmap, loadPlugins } from '@amap/amap-vue'

export default {
  name: 'amapSeach',
  props: {
    query: String,
    cityCode: {
      type: String,
      default: null
    }
  },

  data() {
    return {
      center: null,
      position: null,
      zoom: 10,
      searching: false,
      tips: [],
      results: [],
      pageSize: 10

    }
  },
  computed: {
    wrapper() {
      return this.$refs.wrapper
    }
  },
  created() {
    this.readyMap()
  },
  wacth: {},
  methods: {
    readyMap() {
      this.$ready = new Promise(async resolve => {
        const AMap = await loadAmap()
        await loadPlugins(['AMap.PlaceSearch', 'AMap.AutoComplete', 'AMap.Geolocation'])
        this.ps = new AMap.PlaceSearch({
          city: this.cityCode,
          pageSize: this.pageSize
        })
        this.ac = new AMap.AutoComplete()
        resolve()
        if (this.query) {
          //TODO 存在一点小问题，新增按钮时没有复位gis地图
          this.search()
        }
      })
    },
    async search(clear = false) {
      await this._ready
      if (clear) {
        this.results = []
      }
      this.searching = true
      const { query } = this
      this.ps.search(query, (status, result) => {
        this.searching = false
        if (query !== this.query) return

        if (status === 'complete' && result.poiList) {
          this.results = result.poiList.pois
          this.results.forEach(e => {
            if (query === e.name) {
              this.focus(e)
            }
          })
        } else {
          this.results = []
        }
      })
    },
    async autoComplete(kw) {
      if (!kw) {
        this.tips = []
      } else {
        this.ac.search(kw, (status, result) => {
          if (kw !== this.query) return
          if (status === 'complete' && result.tips) {
            this.tips = Array.from(new Set(result.tips.map(tip => tip.name)))
          } else {
            this.tips = []
          }
        })
      }
    },
    focus(poi) {
      const pos = [poi.location.lng, poi.location.lat]
      this.position = [...pos]
      this.center = [...pos]
      this.$emit('getPosition', poi)
    },
    reset() {
      this.results = []
      this.tips = []
    }
  },
  watch: {}
}
</script>

<style lang="less" scoped>
.map-container {
  width: 100%;
  height: 30vh;
}
</style>