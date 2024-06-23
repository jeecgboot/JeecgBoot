<template>
  <div ref="wrapRef" :style="{ height, width }"></div>
</template>
<script lang="ts">
  import { defineComponent, ref, nextTick, unref, onMounted } from 'vue';

  import { useScript } from '/@/hooks/web/useScript';

  const MAP_URL = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyBQWrGwj4gAzKndcbwD5favT9K0wgty_0&signed_in=true';

  export default defineComponent({
    name: 'GoogleMap',
    props: {
      width: {
        type: String,
        default: '100%',
      },
      height: {
        type: String,
        default: 'calc(100vh - 78px)',
      },
    },
    setup() {
      const wrapRef = ref<HTMLDivElement | null>(null);
      const { toPromise } = useScript({ src: MAP_URL });

      async function initMap() {
        await toPromise();
        await nextTick();
        const wrapEl = unref(wrapRef);
        if (!wrapEl) return;
        const google = (window as any).google;
        const latLng = { lat: 116.404, lng: 39.915 };
        const map = new google.maps.Map(wrapEl, {
          zoom: 4,
          center: latLng,
        });
        new google.maps.Marker({
          position: latLng,
          map: map,
          title: 'Hello World!',
        });
      }

      onMounted(() => {
        initMap();
      });

      return { wrapRef };
    },
  });
</script>
