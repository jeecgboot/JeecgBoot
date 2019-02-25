package org.jeecg.common.util;

import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
public class PasswordUtil {

	/**
	 * JAVA6支持以下任意一种算法 PBEWITHMD5ANDDES PBEWITHMD5ANDTRIPLEDES
	 * PBEWITHSHAANDDESEDE PBEWITHSHA1ANDRC2_40 PBKDF2WITHHMACSHA1
	 * */

	/**
	 * 定义使用的算法为:PBEWITHMD5andDES算法
	 */
	public static final String ALGORITHM = "PBEWithMD5AndDES";//加密算法
	public static final String Salt = "63293188";//密钥

	/**
	 * 定义迭代次数为1000次
	 */
	private static final int ITERATIONCOUNT = 1000;

	/**
	 * 获取加密算法中使用的盐值,解密中使用的盐值必须与加密中使用的相同才能完成操作. 盐长度必须为8字节
	 * 
	 * @return byte[] 盐值
	 * */
	public static byte[] getSalt() throws Exception {
		// 实例化安全随机数
		SecureRandom random = new SecureRandom();
		// 产出盐
		return random.generateSeed(8);
	}

	public static byte[] getStaticSalt() {
		// 产出盐
		return Salt.getBytes();
	}

	/**
	 * 根据PBE密码生成一把密钥
	 * 
	 * @param password
	 *            生成密钥时所使用的密码
	 * @return Key PBE算法密钥
	 * */
	private static Key getPBEKey(String password) {
		// 实例化使用的算法
		SecretKeyFactory keyFactory;
		SecretKey secretKey = null;
		try {
			keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
			// 设置PBE密钥参数
			PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
			// 生成密钥
			secretKey = keyFactory.generateSecret(keySpec);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return secretKey;
	}

	/**
	 * 加密明文字符串
	 * 
	 * @param plaintext
	 *            待加密的明文字符串
	 * @param password
	 *            生成密钥时所使用的密码
	 * @param salt
	 *            盐值
	 * @return 加密后的密文字符串
	 * @throws Exception
	 */
	public static String encrypt(String plaintext, String password, String salt) {

		Key key = getPBEKey(password);
		byte[] encipheredData = null;
		PBEParameterSpec parameterSpec = new PBEParameterSpec(salt.getBytes(), ITERATIONCOUNT);
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);

			cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

			encipheredData = cipher.doFinal(plaintext.getBytes());
		} catch (Exception e) {
		}
		return bytesToHexString(encipheredData);
	}

	/**
	 * 解密密文字符串
	 * 
	 * @param ciphertext
	 *            待解密的密文字符串
	 * @param password
	 *            生成密钥时所使用的密码(如需解密,该参数需要与加密时使用的一致)
	 * @param salt
	 *            盐值(如需解密,该参数需要与加密时使用的一致)
	 * @return 解密后的明文字符串
	 * @throws Exception
	 */
	public static String decrypt(String ciphertext, String password, String salt) {

		Key key = getPBEKey(password);
		byte[] passDec = null;
		PBEParameterSpec parameterSpec = new PBEParameterSpec(salt.getBytes(), ITERATIONCOUNT);
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);

			cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

			passDec = cipher.doFinal(hexStringToBytes(ciphertext));
		}

		catch (Exception e) {
			// TODO: handle exception
		}
		return new String(passDec);
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 * 
	 * @param src
	 *            字节数组
	 * @return
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * 将十六进制字符串转换为字节数组
	 * 
	 * @param hexString
	 *            十六进制字符串
	 * @return
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}


}