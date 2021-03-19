package com.webjjang.util;

import javax.servlet.http.HttpServletRequest;

/*
 * 페이지 처리를 위한 객체
 * 
 * 과정명 : 스마트 웹 자바(JAVA) 개발자
 * 작성자 : 박소현
 * 작성일 : 2021-03-02
 */

public class PageObject {

	// 현재 페이지를 DB에서 가져 올때 필요한 정보
	private long page; // 현재 페이지
	private long perPageNum; // 페이지당 보여지는 글의 갯수
	private long startRow, endRow; // 현재 페이지의 시작 줄번호, 끝번호
	
	//JSP 화면에 하단부분 페이지 표시를 할때 필요한 정보.
	private long perGroupPageNum; // 기본값 10을 생성자에서 셋팅
	private long startPage;
	private long endPage;
	private long totalPage;
	private long totalRow; // DB에서 구한다.
	
	// 검색에 필요한 변수 선언
	private String key;
	private String word;
	
	// 공지 분류 (기간) 를 적용시키는 변수 - period
	// pre : 현재공지, old : 지난공지 , res : 예약공지, all : 전체공지 
	private String period;
	
	// 메시지 처리를 위한 변수
	// 받는 사람의 id
	private String accepter;
	
	public PageObject(long page, long perPageNum) {
		this.page = page;
		this.perPageNum = perPageNum;
		// 시작 줄번호와 끝 줄번호 계산
		// 현재 페이지의 이전 페이지 까지 데이터를 skip시키고 그 다음 번호 시작 번호로 한다.
		startRow = (page-1)*perPageNum + 1;
		endRow = startRow + perPageNum -1;
		
		// JSP 하단 부분에 몇개의 페이지를 표시할지 정한다.
		perGroupPageNum = 10;
		System.out.println("PageObject(페이지,페이지 당 글수)");
	}
	
	// Spring에서는 파라메터 값을 전달 받을때 기본 생성자와 setter()를 이용해서 데이터를 전달 받는다.
	public PageObject() {
		// 처음 리스트로 들어올 때는 데이터가 안 넘어 오므로 초기값을 1페이지에
		// 한페이당 10개의 글을 보이도록 셋팅한다.
		this.page = 1;
		this.perPageNum = 10;
		// JSP 하단 부분에 몇개의 페이지를 표시할지 정한다.
		this.startPage = 1;
		this.endPage = 1;
		this.perGroupPageNum = 10;
		// service 프로그램에서 setTotalRow(DB의 데이터 개수)호출 해야 한다. 그래야 자동 계산이 된다.
		// 기본적으로 찾는 공지 분류 - 현재 공지 :pre
		this.period = "pre";
		
	}
	
	//객체로 만들어 주는 메소드
	public static PageObject getInstance(HttpServletRequest request) throws Exception {
		// 페이지 처리를 위한 프로그램
		// 페이지 처리를 위한 객체 사용
		PageObject pageObject = new PageObject();
		// 페이지에 대한 정보를 받는다.
		// page는 jsp에서 기본객체로 사용하고 있다. -> 페이지의 정보가 담겨져 있다.
		String strCurPage = request.getParameter("page");
		// 넘어오는 페이지가 있는 경우는 넘어오는 페이지를 현재 페이지로 셋팅. 그렇지 않으면 1이 셋팅된다.
		if(strCurPage != null) pageObject.setPage(Integer.parseInt(strCurPage));
		// 한페이지에 표시할 데이터의 수를 받는다.
		String strPerPageNum = request.getParameter("perPageNum");
		// 한 페이지당 표시할 데이터의 수가 안넘어오면 10으로 셋팅된다. 넘어오면 넘어 오는 데이터를 사용한다.
		if(strPerPageNum != null) pageObject.setPerPageNum(Integer.parseInt(strPerPageNum));
		// 넘어온 데이터 확인
		System.out.println( "BoardController.execute() [page = " 
		+ strCurPage + ", perPageNum = " + strPerPageNum+ "]");
		
		//검색을 위한 데이터전달
		pageObject.setKey(request.getParameter("key"));
		pageObject.setWord(request.getParameter("word"));
		
		// PageObject - 확인
		System.out.println("PageObject.getInstance() [pageObject = " + pageObject + " ]");

		return pageObject;
	}
	
	public long getPage() {
		return page;
	}
	public void setPage(long page) {
		this.page = page;
	}
	public long getPerPageNum() {
		return perPageNum;
	}
	public void setPerPageNum(long perPageNum) {
		this.perPageNum = perPageNum;
	}
	// setStartRow()와  setEndRow()를 계산에 의해서 결정이 되므로 받는 않아서 만들지 않는다.
	public long getStartRow() {
		return startRow;
	}
	public long getEndRow() {
		return endRow;
	}
	
	public long getPerGroupPageNum() {
		return perGroupPageNum;
	}

	public void setPerGroupPageNum(long perGroupPageNum) {
		this.perGroupPageNum = perGroupPageNum;
	}

	public long getStartPage() {
		return startPage;
	}

	public long getEndPage() {
		return endPage;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public long getTotalRow() {
		return totalRow;
	}

	// 페이지에 따른 데이터의 startRow와 endRow, totalPage, startPage, endPage
	public void setTotalRow(long totalRow) {
		this.totalRow = totalRow;
		
		// 시작 줄번호와 끝 줄번호 계산
		// 현재 페이지의 이전 페이지 까지 데이터를 skip시키고 그 다음 번호 시작 번호로 한다.
		startRow = (page-1)*perPageNum + 1;
		endRow = startRow + perPageNum -1;
		
		// 리스트 화면 하단 부분에 나타내는 페이지를 처리하기 위한 데이터들을 계산
		// 전체 페이지를 계산할 수 있다.
		totalPage = (totalRow-1)/perPageNum + 1;
		// startPage, endPage
		startPage 
		= (page - 1) / perGroupPageNum * perGroupPageNum + 1;
		endPage = startPage + perGroupPageNum - 1;
		// endPage 가 총 페이지 수를 넘을 수 없다.
		if(endPage > totalPage) endPage = totalPage;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getAccepter() {
		return accepter;
	}

	public void setAccepter(String accepter) {
		this.accepter = accepter;
	}

	@Override
	public String toString() {
		return "PageObject [page=" + page + ", perPageNum=" + perPageNum + ", startRow=" + startRow + ", endRow="
				+ endRow + ", perGroupPageNum=" + perGroupPageNum + ", startPage=" + startPage + ", endPage=" + endPage
				+ ", totalPage=" + totalPage + ", totalRow=" + totalRow + ", key=" + key + ", word=" + word
				+ ", period=" + period + ", accepter=" + accepter + "]";
	}

	
}
