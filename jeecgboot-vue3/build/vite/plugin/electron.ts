// import electron from 'vite-plugin-electron/simple'
//
// export function configElectronPlugin(_viteEnv: ViteEnv, isBuild: boolean) {
//   return electron({
//     main: {
//       // 主进程入口
//       entry: 'electron/main.ts',
//       vite: {
//         build: {
//           sourcemap: !isBuild,
//           outDir: 'dist/electron',
//         },
//       },
//       onstart: ({startup}) => {
//         // 开发热重载
//         startup()
//       },
//     },
//     preload: {
//       input: 'electron/preload/index.ts',
//       vite: {
//         build: {
//           sourcemap: !isBuild,
//           outDir: 'dist/electron/preload',
//         },
//       },
//       onstart: ({startup}) => {
//         // 开发热重载
//         startup()
//       },
//     }
//   })
//
// }
