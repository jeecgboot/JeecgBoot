export type ErrorMessageMode = 'none' | 'modal' | 'message' | undefined;
export type SuccessMessageMode = 'none' | 'success' | 'error' | undefined;

export interface RequestOptions {
  // 将请求参数拼接到url
  joinParamsToUrl?: boolean;
  // 格式化请求参数时间
  formatDate?: boolean;
  // 是否处理请求结果
  isTransformResponse?: boolean;
  // 是否返回本地响应头,需要获取响应头时使用此属性
  isReturnNativeResponse?: boolean;
  // 默认将prefix 添加到url
  joinPrefix?: boolean;
  // 接口地址，如果保留为空，则使用默认值
  apiUrl?: string;
  // 请求拼接路径
  urlPrefix?: string;
  // 错误消息提示类型
  errorMessageMode?: ErrorMessageMode;
  // 成功消息提示类型
  successMessageMode?: SuccessMessageMode;
  // 是否添加时间戳
  joinTime?: boolean;
  ignoreCancelToken?: boolean;
  //是否在标头中发送令牌
  withToken?: boolean;
}

export interface Result<T = any> {
  code: number;
  type: 'success' | 'error' | 'warning';
  message: string;
  result: T;
}

//文件上传参数
export interface UploadFileParams {
  // 其他参数
  data?: Recordable;
  // 文件参数接口字段名
  name?: string;
  // 文件
  file: File | Blob;
  // 文件名
  filename?: string;
  [key: string]: any;
}
//文件返回参数
export interface UploadFileCallBack {
  // 成功回调方法
  success?: any;
  // 是否返回响应头,需要获取响应头时使用此属性
  isReturnResponse?: boolean;
}
