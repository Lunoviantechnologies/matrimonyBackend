package com.example.matrimony.dto;

import java.util.List;

public class PageDto<T> {
    private List<T> data;
    private Meta meta;

    public static class Meta {
        private int page;
        private int pageSize;
        private long total;

        public int getPage() { return page; }
        public void setPage(int page) { this.page = page; }
        public int getPageSize() { return pageSize; }
        public void setPageSize(int pageSize) { this.pageSize = pageSize; }
        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }
    }

    public List<T> getData() { return data; }
    public void setData(List<T> data) { this.data = data; }
    public Meta getMeta() { return meta; }
    public void setMeta(Meta meta) { this.meta = meta; }
}
