import { withInstall } from '/@/utils/index';
import basicDragVerify from './src/DragVerify.vue';
import rotateDragVerify from './src/ImgRotate.vue';

export const BasicDragVerify = withInstall(basicDragVerify);
export const RotateDragVerify = withInstall(rotateDragVerify);
export * from './src/typing';
