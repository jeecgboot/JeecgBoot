import { renderQrCode } from './drawCanvas';
import { drawLogo } from './drawLogo';
import { RenderQrCodeParams } from './typing';
export const toCanvas = (options: RenderQrCodeParams) => {
  return renderQrCode(options)
    .then(() => {
      return options;
    })
    .then(drawLogo) as Promise<string>;
};
