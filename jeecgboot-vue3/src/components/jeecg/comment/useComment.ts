import { useMessage } from '/@/hooks/web/useMessage';
import { defHttp } from '/@/utils/http/axios';
import { useGlobSetting } from '/@/hooks/setting';
const globSetting = useGlobSetting();
const baseUploadUrl = globSetting.uploadUrl;
import { ref, toRaw, unref, reactive } from 'vue';
import { uploadMyFile } from '/@/api/common/api';

import excel from '/@/assets/svg/fileType/excel.svg';
import other from '/@/assets/svg/fileType/other.svg';
import pdf from '/@/assets/svg/fileType/pdf.svg';
import txt from '/@/assets/svg/fileType/txt.svg';
import word from '/@/assets/svg/fileType/word.svg';
import image from '/@/assets/svg/fileType/image.png';
import { getFileAccessHttpUrl } from '/@/utils/common/compUtils';
import { createImgPreview } from '/@/components/Preview';
import data from "emoji-mart-vue-fast/data/apple.json";
import { EmojiIndex } from "emoji-mart-vue-fast/src";
import { encryptByBase64 } from '/@/utils/cipher';

enum Api {
  list = '/sys/comment/listByForm',
  addText = '/sys/comment/addText',
  deleteOne = '/sys/comment/deleteOne',
  fileList = '/sys/comment/fileList',
  logList = '/sys/dataLog/queryDataVerList',
  queryById = '/sys/comment/queryById',
  getFileViewDomain = '/sys/comment/getFileViewDomain',
}

// 文件预览地址的domain 在后台配置的
let onlinePreviewDomain = '';

/**
 * 获取文件预览的domain
 */
const getViewFileDomain = () => defHttp.get({ url: Api.getFileViewDomain });

/**
 * 列表接口
 * @param params
 */
export const list = (params) => defHttp.get({ url: Api.list, params });

export function getGloablEmojiIndex(){
  if(window['myEmojiIndex']){
    console.log("----走window['myEmojiIndex']缓存，不new新对象！")
    return window['myEmojiIndex'];
  }
  
  window['myEmojiIndex'] = new EmojiIndex(data, {
    function() {
      return true;
    },
    exclude:['recent','people','nature','foods','activity','places','objects','symbols','flags']
  });
  return window['myEmojiIndex'];
}

/**
 * 查询单条记录
 * @param params
 */
export const queryById = (id) => {
  let params = { id: id };
  return defHttp.get({ url: Api.queryById, params },{ isTransformResponse: false });
};

/**
 * 文件列表接口
 * @param params
 */
export const fileList = (params) => defHttp.get({ url: Api.fileList, params });

/**
 * 删除单个
 */
export const deleteOne = (params) => {
  return defHttp.delete({ url: Api.deleteOne, params }, { joinParamsToUrl: true });
};

/**
 * 保存
 * @param params
 */
export const saveOne = (params) => {
  let url = Api.addText;
  return defHttp.post({ url: url, params }, { isTransformResponse: false });
};

/**
 * 数据日志列表接口
 * @param params
 */
export const getLogList = (params) => defHttp.get({ url: Api.logList, params }, {isTransformResponse: false});


/**
 * 文件上传接口
 */
export const uploadFileUrl = `${baseUploadUrl}/sys/comment/addFile`;

export function useCommentWithFile(props) {
  let uploadData = {
    biz: 'comment',
    commentId: '',
  };
  const { createMessage } = useMessage();
  const buttonLoading = ref(false);

  //确定按钮触发
  async function saveCommentAndFiles(obj, fileList) {
    buttonLoading.value = true;
    setTimeout(() => {
      buttonLoading.value = false;
    }, 500);
    await saveComment(obj);
    await uploadFiles(fileList);
  }

  /**
   * 保存评论
   */
  async function saveComment(obj) {
    const {fromUserId, toUserId, commentId, commentContent} = obj;
    let commentData = {
      tableName: props.tableName,
      tableDataId: props.dataId,
      fromUserId,
      commentContent,
      toUserId: '',
      commentId: ''
    };
    if(toUserId){
      commentData.toUserId = toUserId;
    }
    if(commentId){
      commentData.commentId = commentId;
    }
    uploadData.commentId = '';
    const res = await saveOne(commentData);
    if (res.success) {
      uploadData.commentId = res.result;
    } else {
      createMessage.warning(res.message);
      return Promise.reject('保存评论失败');
    }
  }

  async function uploadOne(file) {
    let url = uploadFileUrl;
    const formData = new FormData();
    formData.append('file', file);
    formData.append('tableName', props.tableName);
    formData.append('tableDataId', props.dataId);
    Object.keys(uploadData).map((k) => {
      formData.append(k, uploadData[k]);
    });
    return new Promise((resolve, reject) => {
      uploadMyFile(url, formData).then((res: any) => {
        console.log('uploadMyFile', res);
        if (res && res.data) {
          if (res.data.result == 'success') {
            resolve(1);
          } else {
            createMessage.warning(res.data.message);
            reject();
          }
        } else {
          reject();
        }
      });
    });
  }

  /**
   * QQYUN-4310【文件】从文件库选择文件功能未做
   * @param file
   */
  async function saveSysFormFile(file){
    let url = '/sys/comment/addFile';
    let params = {
      fileId: file.id,
      commentId: uploadData.commentId
    }
    await defHttp.post({url, params}, { joinParamsToUrl: true, isTransformResponse: false });
  }

  async function uploadFiles(fileList) {
    if (fileList && fileList.length > 0) {
      for (let i = 0; i < fileList.length; i++) {
        let file = toRaw(fileList[i]);
        if(file.exist === true){
          await saveSysFormFile(file);
        }else{
          await uploadOne(file.originFileObj);
        }
      }
    }
  }

  return {
    saveCommentAndFiles,
    buttonLoading,
  };
}

export function uploadMu(fileList) {
  const formData = new FormData();
  // let arr = []
  for(let file of fileList){
    formData.append('files[]', file.originFileObj);
  }
  console.log(formData)
  let url = `${baseUploadUrl}/sys/comment/addFile2`;
  uploadMyFile(url, formData).then((res: any) => {
    console.log('uploadMyFile', res);
  });
}

/**
 * 显示文件列表
 */
export function useFileList() {
  const imageSrcMap = reactive({});
  const typeMap = {
    xls: excel,
    xlsx: excel,
    pdf: pdf,
    txt: txt,
    docx: word,
    doc: word,
    image
  };
   function getBackground(item) {
    console.log('获取文件背景图', item);
    if (isImage(item)) {
      return 'none'
    } else {
      const name = item.name;
      if(!name){
        return 'none';
      }
      const suffix = name.substring(name.lastIndexOf('.') + 1);
      console.log('suffix', suffix)
      let bg = typeMap[suffix];
      if (!bg) {
        bg = other;
      }
      return bg;
    }
  }
  
  function getImageTypeIcon() {
    return typeMap['image'];
  }

  function getBase64(file, id){
    return new Promise((resolve, reject) => {
      //声明js的文件流
      let reader = new FileReader();
      if(file){
        //通过文件流将文件转换成Base64字符串
        reader.readAsDataURL(file);
        //转换成功后
        reader.onload = function () {
          let base = reader.result;
          console.log('base', base)
          imageSrcMap[id] = base;
          console.log('imageSrcMap', imageSrcMap)
          resolve(base)
        }
      }else{
        reject();
      }
    })
  }
  function handleImageSrc(file){
    if(isImage(file)){
      let id = file.uid;
      getBase64(file, id);
    }
  }

  function downLoad(file) {
    let url = getFileAccessHttpUrl(file.url);
    if (url) {
      window.open(url);
    }
  }

  function getFileSize(item) {
    let size = item.fileSize;
    if (!size) {
      return '0B';
    }
    let temp = Math.round(size / 1024);
    return temp + ' KB';
  }

  const selectFileList = ref<any[]>([]);
  function beforeUpload(file) {
    handleImageSrc(file);
    selectFileList.value = [...selectFileList.value, file];
    console.log('selectFileList', unref(selectFileList));
    return false
  }

  function handleRemove(file) {
    const index = selectFileList.value.indexOf(file);
    const newFileList = selectFileList.value.slice();
    newFileList.splice(index, 1);
    selectFileList.value = newFileList;
  }

  function isImage(item){
    const type = item.type||'';
    if (type.indexOf('image') >= 0) {
      return true;
    }
    return false;
  }

  function getImageSrc(file){
    if(file.exist){
      return getFileAccessHttpUrl(file.url);
    }
    if(isImage(file)){
      let id = file.uid;
      if(id){
        if(imageSrcMap[id]){
          return imageSrcMap[id];
        }
      }else if(file.url){
        //数据库中地址
        let url = getFileAccessHttpUrl(file.url);
        return url;
      }
    }
    return ''
  }

  /**
   * 显示图片
   * @param item
   */
  function getImageAsBackground(item){
    let url;
    if(item.exist){
      url = getFileAccessHttpUrl(item.url);
    }else{
      url = getImageSrc(item);
    }
    if(url){
      return {
        "backgroundImage": "url('"+url+"')"
      }
    }
    return {}
  }

  /**
   * 预览列表 cell 图片
   * @param text
   */
  async function viewImage(file) {
    if(isImage(file)){
      let text = getImageSrc(file)
      if (text) {
        let imgList = [text];
        createImgPreview({ imageList: imgList });
      }
    }else{
      if(file.url){
        //数据库中地址
        let url = getFileAccessHttpUrl(file.url);
        await initViewDomain();
        //本地测试需要将文件地址的localhost/127.0.0.1替换成IP, 或是直接修改全局domain
        //url = url.replace('localhost', '192.168.1.100')
        //update-begin---author:scott ---date:2024-06-03  for：【TV360X-952】升级到kkfileview4.1.0---
        let previewUrl = encodeURIComponent(encryptByBase64(url));
        window.open(onlinePreviewDomain+'?url='+previewUrl);
        //update-end---author:scott ---date::2024-06-03  for：【TV360X-952】升级到kkfileview4.1.0----
      }
    }
  }

  /**
   * 初始化domain
   */
  async function initViewDomain(){
    if(!onlinePreviewDomain){
      onlinePreviewDomain = await getViewFileDomain();
    }
    if(!onlinePreviewDomain.startsWith('http')){
      onlinePreviewDomain = 'http://'+ onlinePreviewDomain;
    }
  }

  return {
    selectFileList,
    getBackground,
    getFileSize,
    downLoad,
    beforeUpload,
    handleRemove,
    isImage,
    getImageSrc,
    getImageAsBackground,
    viewImage,
    getImageTypeIcon
  };
}

/**
 * 用于emoji渲染
 */
export function useEmojiHtml(globalEmojiIndex){
  const COLONS_REGEX = new RegExp('([^:]+)?(:[a-zA-Z0-9-_+]+:(:skin-tone-[2-6]:)?)','g');

  function getHtml(text) {
    if(!text){
      return ''
    }
    return text.replace(COLONS_REGEX, function (match, p1, p2) {
      const before = p1 || ''
      if (endsWith(before, 'alt="') || endsWith(before, 'data-text="')) {
        return match
      }
      let emoji = globalEmojiIndex.findEmoji(p2)
      if (!emoji) {
        return match
      }
      return before + emoji2Html(emoji)
    })
    return text;
  }

  function endsWith(str, temp){
    return str.endsWith(temp)
  }

  function emoji2Html(emoji) {
    let style = `position: absolute;top: -3px;left: 3px;width: 18px; height: 18px;background-position: ${emoji.getPosition()}`
    return `<span style="width: 24px" class="emoji-mart-emoji"><span class="my-emoji-icon emoji-set-apple emoji-type-image" style="${style}"> </span> </span>`
  }
  
  return {
    globalEmojiIndex,
    getHtml
  }
}

/**
 * 获取modal窗体高度
 */
export function getModalHeight(){
  return window.innerHeight;
}
