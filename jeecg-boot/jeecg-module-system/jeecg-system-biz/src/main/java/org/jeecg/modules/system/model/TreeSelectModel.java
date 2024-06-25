package org.jeecg.modules.system.model;

import java.io.Serializable;
import java.util.List;

/**
  * 树形下拉框
  * @author: jeecg-boot
 */
public class TreeSelectModel implements Serializable {

	private static final long serialVersionUID = 9016390975325574747L;

	private String key;
	
	private String title;
	/**
	 * 是否叶子节点
	 */
	private boolean isLeaf;
	
	private String icon;
	
	private String parentId;
	
	private String value;
	
	private String code;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	private List<TreeSelectModel> children;

	public List<TreeSelectModel> getChildren() {
		return children;
	}

	public void setChildren(List<TreeSelectModel> children) {
		this.children = children;
	}

}
