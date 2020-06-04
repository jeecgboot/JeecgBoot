import { getAction, deleteAction, putAction, postAction, httpAction } from '@/api/manage'
import Vue from 'vue'

const getDetailMenus = (params)=>getAction("/companyBasic/Menus",params);
const loadCompanyBaseInfo = (params)=>getAction("/companyBasic/loadBaseInfo",params);
const loadQualifications = (params)=>postAction("/companyQualification/queryByCompanyId",params);
const loadBaiduMap = (params)=>getAction("/envtax/companyEnvTax/loadBaiduMap",params);
//查詢 最新的企业归档基本信息
const queryLatestArchivedData = (params)=>getAction("/company/apply/queryLatestArchivedData",params);


export {
  getDetailMenus,
  loadCompanyBaseInfo,
  loadQualifications,
  loadBaiduMap,
  queryLatestArchivedData
}