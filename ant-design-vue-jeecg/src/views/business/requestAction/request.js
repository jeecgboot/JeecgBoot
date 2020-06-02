import { getAction, deleteAction, putAction, postAction, httpAction } from '@/api/manage'
import Vue from 'vue'
import axios from 'axios'

const getDetailMenus = (params)=>getAction("/companyBasic/Menus",params);
const loadCompanyBaseInfo = (params)=>getAction("/companyBasic/loadBaseInfo",params);
const loadQualifications = (params)=>postAction("/companyQualification/queryByCompanyId",params);
const loadBaiduMap = (params)=>getAction("/envtax/companyEnvTax/loadBaiduMap",params);

export {
  getDetailMenus,
  loadCompanyBaseInfo,
  loadQualifications,
  loadBaiduMap
}