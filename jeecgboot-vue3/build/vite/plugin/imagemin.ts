// 【图片压缩插件】
// Image resource files used to compress the output of the production environment
// https://github.com/anncwb/vite-plugin-imagemin
import viteImagemin from 'vite-plugin-imagemin';

export function configImageminPlugin() {
  const plugin = viteImagemin({
    gifsicle: {
      optimizationLevel: 7,
      interlaced: false,
    },
    optipng: {
      optimizationLevel: 7,
    },
    mozjpeg: {
      quality: 20,
    },
    pngquant: {
      quality: [0.8, 0.9],
      speed: 4,
    },
    svgo: {
      plugins: [
        {
          name: 'removeViewBox',
        },
        {
          name: 'removeEmptyAttrs',
          active: false,
        },
      ],
    },
  });
  return plugin;
}
