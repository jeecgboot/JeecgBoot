export function checkFileType(file: File, accepts: string[]) {
  // update-begin--author:liaozhiyang---date:20250318---for：【issues/7954】BasicUpload组件上传文件，限制上传格式校验出错
  const mimePatterns: string[] = [];
  const suffixList: string[] = [];
  // 分类处理 accepts
  for (const item of accepts) {
    if (item.includes('/')) {
      mimePatterns.push(item);
    } else {
      // 支持.png 或 png（带点后缀或者不带点后缀）
      const suffix = item.startsWith('.') ? item.slice(1) : item;
      suffixList.push(suffix);
    }
  }
  // 后缀匹配逻辑
  let suffixMatch = false;
  if (suffixList.length > 0) {
    const suffixRegex = new RegExp(`\\.(${suffixList.join('|')})$`, 'i');
    suffixMatch = suffixRegex.test(file.name);
  }
  // MIME类型匹配逻辑
  let mimeMatch = false;
  if (mimePatterns.length > 0 && file.type) {
    mimeMatch = mimePatterns.some((pattern) => {
      // 先转义特殊字符，再处理通配符
      const regexPattern = pattern
        .replace(/[.+?^${}()|[\]\\]/g, '\\$&') // 先转义特殊字符
        .replace(/\*/g, '.*'); // 再替换通配符
      const regex = new RegExp(`^${regexPattern}$`, 'i');
      return regex.test(file.type);
    });
  }
  if (mimePatterns.length && suffixList.length) {
    return suffixMatch || mimeMatch;
  } else if (mimePatterns.length) {
    return mimeMatch;
  } else if (suffixList.length) {
    return suffixMatch;
  }
  // update-end--author:liaozhiyang---date:20250318---for：【issues/7954】BasicUpload组件上传文件，限制上传格式校验出错
}

export function checkImgType(file: File) {
  return isImgTypeByName(file.name);
}

export function isImgTypeByName(name: string) {
  return /\.(jpg|jpeg|png|gif)$/i.test(name);
}

export function getBase64WithFile(file: File) {
  return new Promise<{
    result: string;
    file: File;
  }>((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve({ result: reader.result as string, file });
    reader.onerror = (error) => reject(error);
  });
}
