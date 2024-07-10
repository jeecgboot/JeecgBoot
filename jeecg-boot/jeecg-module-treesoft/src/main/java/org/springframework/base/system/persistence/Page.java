package org.springframework.base.system.persistence;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class Page<T> {
    public static final String ASC = "asc";
    public static final String DESC = "desc";
    protected int pageNo = 1;
    protected int pageSize = -1;
    protected String orderBy = null;
    protected String order = null;
    protected boolean autoCount = true;
    protected List<T> result = new ArrayList<>();
    protected long totalCount = -1L;
    protected String columns;
    protected String primaryKey;
    protected String tableName;
    private String executeTime;
    private String operator;

    public Page(int pageSize) {
        this.pageSize = pageSize;
    }

    public Page(int pageNo, int pageSize, String orderBy, String order) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
        this.order = order;
    }


    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
        if (pageNo < 1) {
            this.pageNo = 1;
        }

    }

    public Page<T> pageNo(int thePageNo) {
        this.setPageNo(thePageNo);
        return this;
    }

    public Page<T> pageSize(int thePageSize) {
        this.setPageSize(thePageSize);
        return this;
    }

    public int getFirst() {
        return (this.pageNo - 1) * this.pageSize + 1;
    }

    public Page<T> orderBy(String theOrderBy) {
        this.setOrderBy(theOrderBy);
        return this;
    }

    public void setOrder(String order) {
        String lowcaseOrder = StringUtils.lowerCase(order);
        String[] orders = StringUtils.split(lowcaseOrder, ',');
        String[] var7 = orders;
        int var6 = orders.length;

        for (int var5 = 0; var5 < var6; ++var5) {
            String orderStr = var7[var5];
            if (!StringUtils.equals("desc", orderStr) && !StringUtils.equals("asc", orderStr)) {
                throw new IllegalArgumentException("排序方向" + orderStr + "不是合法值");
            }
        }

        this.order = lowcaseOrder;
    }

    public Page<T> order(String theOrder) {
        this.setOrder(theOrder);
        return this;
    }

    public boolean isOrderBySetted() {
        return StringUtils.isNotBlank(this.orderBy) && StringUtils.isNotBlank(this.order);
    }

    public boolean isAutoCount() {
        return this.autoCount;
    }

    public Page<T> autoCount(boolean theAutoCount) {
        this.setAutoCount(theAutoCount);
        return this;
    }

    public long getTotalPages() {
        if (this.totalCount < 0L) {
            return -1L;
        } else {
            long count = this.totalCount / (long) this.pageSize;
            if (this.totalCount % (long) this.pageSize > 0L) {
                ++count;
            }
            return count;
        }
    }

    public boolean isHasNext() {
        return (long) (this.pageNo + 1) <= this.getTotalPages();
    }

    public int getNextPage() {
        return this.isHasNext() ? this.pageNo + 1 : this.pageNo;
    }

    public boolean isHasPre() {
        return this.pageNo - 1 >= 1;
    }

    public int getPrePage() {
        return this.isHasPre() ? this.pageNo - 1 : this.pageNo;
    }

}
