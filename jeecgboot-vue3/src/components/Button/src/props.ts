export const buttonProps = {
  color: { type: String, validator: (v) => ['error', 'warning', 'success', ''].includes(v) },
  loading: { type: Boolean },
  disabled: { type: Boolean },
  /**
   * Text before icon.
   */
  preIcon: { type: String },
  /**
   * Text after icon.
   */
  postIcon: { type: String },
  type: { type: String },
  /**
   * preIcon and postIcon icon size.
   * @default: 15
   */
  iconSize: { type: Number, default: 15 },
  isUpload: { type: Boolean, default: false },
  onClick: { type: Function as PropType<(...args) => any>, default: null },
};
