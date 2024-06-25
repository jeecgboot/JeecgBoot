import { toCanvas } from 'qrcode';
import type { QRCodeRenderersOptions } from 'qrcode';
import { RenderQrCodeParams, ContentType } from './typing';
import { cloneDeep } from 'lodash-es';

export const renderQrCode = ({ canvas, content, width = 0, options: params = {} }: RenderQrCodeParams) => {
  const options = cloneDeep(params);
  // 容错率，默认对内容少的二维码采用高容错率，内容多的二维码采用低容错率
  options.errorCorrectionLevel = options.errorCorrectionLevel || getErrorCorrectionLevel(content);

  return getOriginWidth(content, options).then((_width: number) => {
    options.scale = width === 0 ? undefined : (width / _width) * 4;
    return toCanvas(canvas, content, options);
  });
};

// 得到原QrCode的大小，以便缩放得到正确的QrCode大小
function getOriginWidth(content: ContentType, options: QRCodeRenderersOptions) {
  const _canvas = document.createElement('canvas');
  return toCanvas(_canvas, content, options).then(() => _canvas.width);
}

// 对于内容少的QrCode，增大容错率
function getErrorCorrectionLevel(content: ContentType) {
  if (content.length > 36) {
    return 'M';
  } else if (content.length > 16) {
    return 'Q';
  } else {
    return 'H';
  }
}
