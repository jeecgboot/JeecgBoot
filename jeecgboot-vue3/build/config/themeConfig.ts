import { generate } from '@ant-design/colors';
import setting from '../../src/settings/projectSetting';
// 构建期默认主题色/主题模式：从 src/settings/projectSetting.ts 动态读取，保持与运行时默认值一致
// 运行时会从 store 拿真实值并显式传入，这里只做 build 阶段的占位与默认色板生成
// 注意：projectSetting.ts 及其依赖链（enums、designSetting、getConfigByMenuType）均已改为相对路径导入，
// 因为 vite 8 的 config 文件打包(rolldown)不解析 tsconfig 路径别名(/@/、@/ 等)
export const primaryColor = setting.themeColor;
export const darkMode = setting.themeMode;
type Fn = (...arg: any) => any;

type GenerateTheme = 'default' | 'dark';

export interface GenerateColorsParams {
  mixLighten: Fn;
  mixDarken: Fn;
  tinycolor: any;
  color?: string;
}

export function generateAntColors(color: string, theme: GenerateTheme = 'default') {
  return generate(color, {
    theme,
  });
}

export function getThemeColors(color?: string) {
  const tc = color || primaryColor;
  const lightColors = generateAntColors(tc);
  const primary = lightColors[5];
  const modeColors = generateAntColors(primary, 'dark');

  return [...lightColors, ...modeColors];
}

export function generateColors({
  color = primaryColor,
  mixLighten,
  mixDarken,
  tinycolor,
}: GenerateColorsParams) {
  const arr = new Array(19).fill(0);
  const lightens = arr.map((_t, i) => {
    return mixLighten(color, i / 5);
  });

  const darkens = arr.map((_t, i) => {
    return mixDarken(color, i / 5);
  });

  const alphaColors = arr.map((_t, i) => {
    return tinycolor(color)
      .setAlpha(i / 20)
      .toRgbString();
  });

  const shortAlphaColors = alphaColors.map((item) => item.replace(/\s/g, '').replace(/0\./g, '.'));

  const tinycolorLightens = arr
    .map((_t, i) => {
      return tinycolor(color)
        .lighten(i * 5)
        .toHexString();
    })
    .filter((item) => item !== '#ffffff');

  const tinycolorDarkens = arr
    .map((_t, i) => {
      return tinycolor(color)
        .darken(i * 5)
        .toHexString();
    })
    .filter((item) => item !== '#000000');
  return [
    ...lightens,
    ...darkens,
    ...alphaColors,
    ...shortAlphaColors,
    ...tinycolorDarkens,
    ...tinycolorLightens,
  ].filter((item) => !item.includes('-'));
}
