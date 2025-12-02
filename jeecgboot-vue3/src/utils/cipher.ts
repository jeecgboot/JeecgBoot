import { encrypt, decrypt } from 'crypto-js/aes';
import { parse } from 'crypto-js/enc-utf8';
import pkcs7 from 'crypto-js/pad-pkcs7';
import ECB from 'crypto-js/mode-ecb';
import md5 from 'crypto-js/md5';
import UTF8 from 'crypto-js/enc-utf8';
import Base64 from 'crypto-js/enc-base64';
import CryptoJS from 'crypto-js';

export interface EncryptionParams {
  key: string;
  iv: string;
}

export class AesEncryption {
  private key;
  private iv;

  constructor(opt: Partial<EncryptionParams> = {}) {
    const { key, iv } = opt;
    if (key) {
      this.key = parse(key);
    }
    if (iv) {
      this.iv = parse(iv);
    }
  }

  get getOptions() {
    return {
      mode: ECB,
      padding: pkcs7,
      iv: this.iv,
    };
  }

  encryptByAES(cipherText: string) {
    return encrypt(cipherText, this.key, this.getOptions).toString();
  }

  decryptByAES(cipherText: string) {
    return decrypt(cipherText, this.key, this.getOptions).toString(UTF8);
  }
}

export function encryptByBase64(cipherText: string) {
  return UTF8.parse(cipherText).toString(Base64);
}

export function decodeByBase64(cipherText: string) {
  return Base64.parse(cipherText).toString(UTF8);
}

export function encryptByMd5(password: string) {
  return md5(password).toString();
}

// ================== 密码加密相关 ==================
// 密码加密统一使用 AES CBC 模式，前后端 key 和 iv 必须保持一致
// AES_KEY 和 AES_IV 需与后端配置完全一致，否则加密/解密会失败
// ================== 密码加密相关 ===========BEGIN=======

// AES加密key和iv常量
export const AES_KEY = '1234567890adbcde';
export const AES_IV = '1234567890hjlkew';

/**
 * AES CBC 加密，使用全局常量 AES_KEY 和 AES_IV
 * @param plainText 明文
 * @returns 加密后的密文
 */
export function encryptAESCBC(plainText: string): string {
  const key = parse(AES_KEY);
  const iv = parse(AES_IV);
  return encrypt(plainText, key, {
    iv: iv,
    mode: CryptoJS.mode.CBC,
    padding: pkcs7
  }).toString();
}
// ================== 密码加密相关 =============END=====
