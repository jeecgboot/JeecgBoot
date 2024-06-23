import {
  AccountParams,
  DeptListItem,
  MenuParams,
  RoleParams,
  TestPageParams,
  RolePageParams,
  MenuListGetResultModel,
  DeptListGetResultModel,
  AccountListGetResultModel,
  RolePageListGetResultModel,
  RoleListGetResultModel,
  TestListGetResultModel,
} from './model/systemModel';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  AccountList = '/mock/system/getAccountList',
  IsAccountExist = '/mock/system/accountExist',
  DeptList = '/mock/system/getDeptList',
  setRoleStatus = '/mock/system/setRoleStatus',
  MenuList = '/mock/system/getMenuList',
  RolePageList = '/mock/system/getRoleListByPage',
  DemoTableList = '/mock/system/getDemoTableListByPage',
  TestPageList = '/mock/system/getTestListByPage',
  GetAllRoleList = '/mock/system/getAllRoleList',
}

export const getAccountList = (params: AccountParams) => defHttp.get<AccountListGetResultModel>({ url: Api.AccountList, params });

export const getDeptList = (params?: DeptListItem) => defHttp.get<DeptListGetResultModel>({ url: Api.DeptList, params });

export const getMenuList = (params?: MenuParams) => defHttp.get<MenuListGetResultModel>({ url: Api.MenuList, params });

export const getRoleListByPage = (params?: RolePageParams) => defHttp.get<RolePageListGetResultModel>({ url: Api.RolePageList, params });

export const getAllRoleList = (params?: RoleParams) => defHttp.get<RoleListGetResultModel>({ url: Api.GetAllRoleList, params });

export const setRoleStatus = (id: number, status: string) => defHttp.post({ url: Api.setRoleStatus, params: { id, status } });

export const getTestListByPage = (params?: TestPageParams) => defHttp.get<TestListGetResultModel>({ url: Api.TestPageList, params });

export const getDemoTableListByPage = (params) => defHttp.get({ url: Api.DemoTableList, params });

export const isAccountExist = (account: string) => defHttp.post({ url: Api.IsAccountExist, params: { account } }, { errorMessageMode: 'none' });
