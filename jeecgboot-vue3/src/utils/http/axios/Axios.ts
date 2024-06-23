import type { AxiosRequestConfig, AxiosInstance, AxiosResponse, AxiosError } from 'axios';
import type { RequestOptions, Result, UploadFileParams, UploadFileCallBack } from '/#/axios';
import type { CreateAxiosOptions } from './axiosTransform';
import axios from 'axios';
import qs from 'qs';
import { AxiosCanceler } from './axiosCancel';
import { isFunction } from '/@/utils/is';
import { cloneDeep } from 'lodash-es';
import { ContentTypeEnum } from '/@/enums/httpEnum';
import { RequestEnum } from '/@/enums/httpEnum';
import { useGlobSetting } from '/@/hooks/setting';
import { useMessage } from '/@/hooks/web/useMessage';

const { createMessage } = useMessage();
export * from './axiosTransform';

/**
 * @description:  axios module
 */
export class VAxios {
  private axiosInstance: AxiosInstance;
  private readonly options: CreateAxiosOptions;

  constructor(options: CreateAxiosOptions) {
    this.options = options;
    this.axiosInstance = axios.create(options);
    this.setupInterceptors();
  }

  /**
   * @description:  Create axios instance
   */
  private createAxios(config: CreateAxiosOptions): void {
    this.axiosInstance = axios.create(config);
  }

  private getTransform() {
    const { transform } = this.options;
    return transform;
  }

  getAxios(): AxiosInstance {
    return this.axiosInstance;
  }

  /**
   * @description: Reconfigure axios
   */
  configAxios(config: CreateAxiosOptions) {
    if (!this.axiosInstance) {
      return;
    }
    this.createAxios(config);
  }

  /**
   * @description: Set general header
   */
  setHeader(headers: any): void {
    if (!this.axiosInstance) {
      return;
    }
    Object.assign(this.axiosInstance.defaults.headers, headers);
  }

  /**
   * @description: Interceptor configuration
   */
  private setupInterceptors() {
    const transform = this.getTransform();
    if (!transform) {
      return;
    }
    const { requestInterceptors, requestInterceptorsCatch, responseInterceptors, responseInterceptorsCatch } = transform;

    const axiosCanceler = new AxiosCanceler();

    // 请求侦听器配置处理
    this.axiosInstance.interceptors.request.use((config: AxiosRequestConfig) => {
      // If cancel repeat request is turned on, then cancel repeat request is prohibited
      // @ts-ignore
      const { ignoreCancelToken } = config.requestOptions;

      const ignoreCancel = ignoreCancelToken !== undefined ? ignoreCancelToken : this.options.requestOptions?.ignoreCancelToken;

      !ignoreCancel && axiosCanceler.addPending(config);
      if (requestInterceptors && isFunction(requestInterceptors)) {
        config = requestInterceptors(config, this.options);
      }
      return config;
    }, undefined);

    // 请求拦截器错误捕获
    requestInterceptorsCatch &&
      isFunction(requestInterceptorsCatch) &&
      this.axiosInstance.interceptors.request.use(undefined, requestInterceptorsCatch);

    // 响应结果拦截器处理
    this.axiosInstance.interceptors.response.use((res: AxiosResponse<any>) => {
      res && axiosCanceler.removePending(res.config);
      if (responseInterceptors && isFunction(responseInterceptors)) {
        res = responseInterceptors(res);
      }
      return res;
    }, undefined);

    // 响应结果拦截器错误捕获
    responseInterceptorsCatch &&
      isFunction(responseInterceptorsCatch) &&
      this.axiosInstance.interceptors.response.use(undefined, responseInterceptorsCatch);
  }

  /**
   * 文件上传
   */
  //--@updateBy-begin----author:liusq---date:20211117------for:增加上传回调参数callback------
  uploadFile<T = any>(config: AxiosRequestConfig, params: UploadFileParams, callback?: UploadFileCallBack) {
    //--@updateBy-end----author:liusq---date:20211117------for:增加上传回调参数callback------
    const formData = new window.FormData();
    const customFilename = params.name || 'file';

    if (params.filename) {
      formData.append(customFilename, params.file, params.filename);
    } else {
      formData.append(customFilename, params.file);
    }
    const glob = useGlobSetting();
    config.baseURL = glob.uploadUrl;
    if (params.data) {
      Object.keys(params.data).forEach((key) => {
        const value = params.data![key];
        if (Array.isArray(value)) {
          value.forEach((item) => {
            formData.append(`${key}[]`, item);
          });
          return;
        }

        formData.append(key, params.data[key]);
      });
    }

    return this.axiosInstance
      .request<T>({
        ...config,
        method: 'POST',
        data: formData,
        headers: {
          'Content-type': ContentTypeEnum.FORM_DATA,
          ignoreCancelToken: true,
        },
      })
      .then((res: any) => {
        //--@updateBy-begin----author:liusq---date:20210914------for:上传判断是否包含回调方法------
        if (callback?.success && isFunction(callback?.success)) {
          callback?.success(res?.data);
          //--@updateBy-end----author:liusq---date:20210914------for:上传判断是否包含回调方法------
        } else if (callback?.isReturnResponse) {
          //--@updateBy-begin----author:liusq---date:20211117------for:上传判断是否返回res信息------
          return Promise.resolve(res?.data);
          //--@updateBy-end----author:liusq---date:20211117------for:上传判断是否返回res信息------
        } else {
          if (res.data.success == true && res.data.code == 200) {
            createMessage.success(res.data.message);
          } else {
            createMessage.error(res.data.message);
          }
        }
      });
  }

  // 支持表单数据
  supportFormData(config: AxiosRequestConfig) {
    const headers = config.headers || this.options.headers;
    const contentType = headers?.['Content-Type'] || headers?.['content-type'];

    if (contentType !== ContentTypeEnum.FORM_URLENCODED || !Reflect.has(config, 'data') || config.method?.toUpperCase() === RequestEnum.GET) {
      return config;
    }

    return {
      ...config,
      data: qs.stringify(config.data, { arrayFormat: 'brackets' }),
    };
  }

  get<T = any>(config: AxiosRequestConfig, options?: RequestOptions): Promise<T> {
    return this.request({ ...config, method: 'GET' }, options);
  }

  post<T = any>(config: AxiosRequestConfig, options?: RequestOptions): Promise<T> {
    return this.request({ ...config, method: 'POST' }, options);
  }

  put<T = any>(config: AxiosRequestConfig, options?: RequestOptions): Promise<T> {
    return this.request({ ...config, method: 'PUT' }, options);
  }

  delete<T = any>(config: AxiosRequestConfig, options?: RequestOptions): Promise<T> {
    return this.request({ ...config, method: 'DELETE' }, options);
  }

  request<T = any>(config: AxiosRequestConfig, options?: RequestOptions): Promise<T> {
    let conf: CreateAxiosOptions = cloneDeep(config);
    const transform = this.getTransform();

    const { requestOptions } = this.options;

    const opt: RequestOptions = Object.assign({}, requestOptions, options);

    const { beforeRequestHook, requestCatchHook, transformRequestHook } = transform || {};
    if (beforeRequestHook && isFunction(beforeRequestHook)) {
      conf = beforeRequestHook(conf, opt);
    }
    conf.requestOptions = opt;

    conf = this.supportFormData(conf);

    return new Promise((resolve, reject) => {
      this.axiosInstance
        .request<any, AxiosResponse<Result>>(conf)
        .then((res: AxiosResponse<Result>) => {
          if (transformRequestHook && isFunction(transformRequestHook)) {
            try {
              const ret = transformRequestHook(res, opt);
              //zhangyafei---添加回调方法
              config.success && config.success(res.data);
              //zhangyafei---添加回调方法
              resolve(ret);
            } catch (err) {
              reject(err || new Error('request error!'));
            }
            return;
          }
          resolve(res as unknown as Promise<T>);
        })
        .catch((e: Error | AxiosError) => {
          if (requestCatchHook && isFunction(requestCatchHook)) {
            reject(requestCatchHook(e, opt));
            return;
          }
          if (axios.isAxiosError(e)) {
            // 在此处重写来自axios的错误消息
          }
          reject(e);
        });
    });
  }


  /**
   * 【用于评论功能】自定义文件上传-请求
   * @param url
   * @param formData
   */
  uploadMyFile<T = any>(url, formData) {
    const glob = useGlobSetting();
    return this.axiosInstance
      .request<T>({
        url: url,
        baseURL: glob.uploadUrl,
        method: 'POST',
        data: formData,
        headers: {
          'Content-type': ContentTypeEnum.FORM_DATA,
          ignoreCancelToken: true,
        },
      });
  }
}
