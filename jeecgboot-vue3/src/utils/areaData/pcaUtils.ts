import {areaList} from '@vant/area-data'
import {freezeDeep} from "@/utils/common/compUtils";

// 扁平化的省市区数据
export const pcaa = freezeDeep(usePlatPcaaData())

/**
 * 获取扁平化的省市区数据
 */
function usePlatPcaaData() {
  const {city_list: city, county_list: county, province_list: province} = areaList;
  const dataMap = new Map<string, Recordable>()
  const flatData: Recordable = {'86': province}
  // 省
  Object.keys(province).forEach((code) => {
    flatData[code] = {}
    dataMap.set(code.slice(0, 2), flatData[code])
  })
  // 市区
  Object.keys(city).forEach((code) => {
    flatData[code] = {}
    dataMap.set(code.slice(0, 4), flatData[code])
    // 填充上一级
    const getProvince = dataMap.get(code.slice(0, 2))
    if (getProvince) {
      getProvince[code] = city[code]
    }
  });
  // 县
  Object.keys(county).forEach((code) => {
    // 填充上一级
    const getCity = dataMap.get(code.slice(0, 4))
    if (getCity) {
      getCity[code] = county[code]
    }
  });
  return flatData
}