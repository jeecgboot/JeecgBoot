package org.jeecg.modules.online.cgreport.model;

import java.util.Arrays;
import java.util.List;

import org.jeecg.modules.online.cgreport.entity.OnlCgreportHead;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportItem;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportParam;

/**
 * @Description: 用于接收修改或新增报表信息的model
 * @author: sunjianlei
 * @date: 2019-03-11
 * @version: V1.0
 */
public class OnlCgreportModel {

	private OnlCgreportHead head;

	private List<OnlCgreportParam> params;
	private String deleteParamIds;

	private List<OnlCgreportItem> items;
	private String deleteItemIds;

	public OnlCgreportHead getHead() {
		return head;
	}

	public void setHead(OnlCgreportHead head) {
		this.head = head;
	}

	public List<OnlCgreportParam> getParams() {
		return params;
	}

	public void setParams(List<OnlCgreportParam> params) {
		this.params = params;
	}

	public List<OnlCgreportItem> getItems() {
		return items;
	}

	public void setItems(List<OnlCgreportItem> items) {
		this.items = items;
	}

	public String getDeleteParamIds() {
		return deleteParamIds;
	}

	public List<String> getDeleteParamIdList() {
		return Arrays.asList(deleteParamIds.split(","));
	}

	public void setDeleteParamIds(String deleteParamIds) {
		this.deleteParamIds = deleteParamIds;
	}

	public String getDeleteItemIds() {
		return deleteItemIds;
	}

	public List<String> getDeleteItemIdList() {
		return Arrays.asList(deleteItemIds.split(","));
	}

	public void setDeleteItemIds(String deleteItemIds) {
		this.deleteItemIds = deleteItemIds;
	}

	@Override
	public String toString() {
		return "OnlCgreportModel [head=" + head + ", params=" + params + ", deleteParamIds=" + deleteParamIds + ", items=" + items + ", deleteItemIds=" + deleteItemIds + "]";
	}

}
