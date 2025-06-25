// 不能直接使用 process.env，会报错
export const $ps = process;

export const isDev = !!$ps.env.VITE_DEV_SERVER_URL;

export const $env = getEnv();

function getEnv() {
  if (isDev) {
    return $ps.env;
  }
  // 非开发环境，从 JSON 文件中获取环境变量
  const env = require('./env.json');
  return {
    ...$ps.env,
    ...env,
  };
}
