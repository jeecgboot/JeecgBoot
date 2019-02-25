/**
 * 弃用
 */
import { setStore, getStore, clearStore } from "@/utils/storage"

export const TokenKey = 'Access-Token'

export function getToken() {
  return getStore(TokenKey)
}

export function setToken(token) {
  // key, token, timeout = 86400s
  return setStore(TokenKey, token, 86400)
}

export function removeToken() {
  return clearStore(TokenKey)
}