package com.example.matrimony.dto;

import java.util.List;

public class MatchSearchResponse {

    private List<ProfileMatchResponse> content;
    private int totalPages;
    private long totalElements;
    private int page;

    public MatchSearchResponse(List<ProfileMatchResponse> content,
                               int totalPages,
                               long totalElements,
                               int page) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.page=page;
    }

	public List<ProfileMatchResponse> getContent() {
		return content;
	}

	public void setContent(List<ProfileMatchResponse> content) {
		this.content = content;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
    
    
}