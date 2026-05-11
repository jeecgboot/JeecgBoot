import tinycolor from 'tinycolor2';

export function mixLighten(colorStr: string, weight: number) {
  return mix('fff', colorStr, weight);
}

export function mixDarken(colorStr: string, weight: number) {
  return mix('000', colorStr, weight);
}

export function mix(color1: string, color2: string, weight: number, alpha1?: number, alpha2?: number) {
  color1 = dropPrefix(color1);
  color2 = dropPrefix(color2);
  if (weight === undefined) weight = 0.5;
  if (alpha1 === undefined) alpha1 = 1;
  if (alpha2 === undefined) alpha2 = 1;
  const w = 2 * weight - 1;
  const alphaDelta = alpha1 - alpha2;
  const w1 = ((w * alphaDelta === -1 ? w : (w + alphaDelta) / (1 + w * alphaDelta)) + 1) / 2;
  const w2 = 1 - w1;
  const rgb1 = toNum3(color1);
  const rgb2 = toNum3(color2);
  const r = Math.round(w1 * rgb1[0] + w2 * rgb2[0]);
  const g = Math.round(w1 * rgb1[1] + w2 * rgb2[1]);
  const b = Math.round(w1 * rgb1[2] + w2 * rgb2[2]);
  return '#' + pad2(r) + pad2(g) + pad2(b);
}

export function toNum3(colorStr: string) {
  colorStr = dropPrefix(colorStr);
  if (colorStr.length === 3) {
    colorStr = colorStr[0] + colorStr[0] + colorStr[1] + colorStr[1] + colorStr[2] + colorStr[2];
  }
  const r = parseInt(colorStr.slice(0, 2), 16);
  const g = parseInt(colorStr.slice(2, 4), 16);
  const b = parseInt(colorStr.slice(4, 6), 16);
  return [r, g, b];
}

export function dropPrefix(colorStr: string) {
  return colorStr.replace('#', '');
}

export function pad2(num: number) {
  let t = num.toString(16);
  if (t.length === 1) t = '0' + t;
  return t;
}

export { tinycolor };
