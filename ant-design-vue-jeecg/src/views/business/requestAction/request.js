import { getAction, deleteAction, putAction, postAction, httpAction } from '@/api/manage'
import Vue from 'vue'

const getDetailMenus = (params)=>getAction("/companyBasic/Menus",params);
const loadCompanyBaseInfo = (params)=>getAction("/companyBasic/loadBaseInfo",params);
const loadQualifications = (params)=>postAction("/companyQualification/queryByCompanyId",params);
const loadBaiduMap = (params)=>getAction("/envtax/companyEnvTax/loadBaiduMap",params);
//查詢 最新的企业归档基本信息
const queryLatestArchivedData = (params)=>getAction("/company/apply/queryLatestArchivedData",params);
//查询申报前后对比信息
const queryComparisonData = (params)=>getAction("/company/apply/queryComparisonData",params);
const qualificationApply = (params)=>postAction("/company/apply/qualification",params);
//查询资质申报待审核
const queryQualification = (params)=>getAction("/company/apply/queryQualification",params);
//查询资质申报前后对比
const comparisonQualification = (params)=>getAction("/company/apply/comparisonQualification",params);
export {
  getDetailMenus,
  loadCompanyBaseInfo,
  loadQualifications,
  loadBaiduMap,
  queryLatestArchivedData,
  queryComparisonData,
  qualificationApply,
  queryQualification,
  comparisonQualification
}