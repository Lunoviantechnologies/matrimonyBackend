package com.example.matrimony.dto;

public class MatchSearchRequest {

    private String matchType;   // NEW, MY, NEAR, MORE
    private ProfileFilterRequest filters;
    private String sort;
    private int page;
    private int size;
	public String getMatchType() {
		return matchType;
	}
	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}
	public ProfileFilterRequest getFilters() {
		return filters;
	}
	public void setFilters(ProfileFilterRequest filters) {
		this.filters = filters;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}

    
}
