import { withInstall } from '/@/utils';
import type { ExtractPropTypes } from 'vue';
import button from './src/BasicButton.vue';
import jUploadButton from './src/JUploadButton.vue';
import popConfirmButton from './src/PopConfirmButton.vue';
import { buttonProps } from './src/props';

export const Button = withInstall(button);
export const JUploadButton = withInstall(jUploadButton);
export const PopConfirmButton = withInstall(popConfirmButton);
export declare type ButtonProps = Partial<ExtractPropTypes<typeof buttonProps>>;
