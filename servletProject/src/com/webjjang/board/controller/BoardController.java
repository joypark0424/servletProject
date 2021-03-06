package com.webjjang.board.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.webjjang.board.vo.BoardReplyVO;
import com.webjjang.board.vo.BoardVO;
import com.webjjang.main.controller.Beans;
import com.webjjang.main.controller.Controller;
import com.webjjang.main.controller.ExeService;
import com.webjjang.util.PageObject;
import com.webjjang.util.filter.AuthorityFilter;

public class BoardController implements Controller{

	private final String MODULE = "board";
	private String jspInfo = null;
	
	@Override
	public String execute(HttpServletRequest request) throws Exception{
		System.out.println("BoardController.execute()");
		
		//페이지를 위한 처리
		PageObject pageObject = PageObject.getInstance(request);
		request.setAttribute("pageObject", pageObject); // 페이지를 보여주기 위해 서버객체에 담는다.
		
		switch (AuthorityFilter.url) {

		// 1. 게시판 리스트
		case "/" + MODULE +"/list.do":
			// service - dao --> request에 저장까지 해준다.
			list(request,pageObject);
		// "board/list" 넘긴다. -> /WEB-INF/views/ + board/list + .jsp를 이용해서 HTML을 만든다.
			jspInfo = MODULE + "/list";			
			break;

		// 2. 게시판 글보기
		case "/" + MODULE +"/view.do":
			// service - dao --> request에 저장까지 해준다.
			//글보기 데이터 가져오기. 뎃글 때문에 글번호르 다시 돌려준다.
//			Long no = view(request);
			replyList(view(request), pageObject, request);
		// "board/view" 넘긴다. -> /WEB-INF/views/ + board/view + .jsp를 이용해서 HTML을 만든다.
			jspInfo = MODULE + "/view";			
			break;
	
		//3-1. 게시판 글쓰기 폼
		case "/" + MODULE +"/writeForm.do":
		// "board/view" 넘긴다. -> /WEB-INF/views/ + board/view + .jsp를 이용해서 HTML을 만든다.
		jspInfo = MODULE + "/writeForm";			
		break;
		
		// 3-2. 게시판 글쓰기 처리
		case "/" + MODULE +"/write.do":
			// service - dao --> request에 저장까지 해준다.
			write(request);
		jspInfo = "redirect:list.do?page=1&perPageNum=" + pageObject.getPerPageNum();
		break;
		
		//4-1. 게시판 글수정 폼
		case "/" + MODULE +"/updateForm.do":
			// service - dao --> request에 저장까지 해준다.
			updateForm(request);
		// "board/view" 넘긴다. -> /WEB-INF/views/ + board/view + .jsp를 이용해서 HTML을 만든다.
		jspInfo = MODULE + "/updateForm";			
		break;

		//4-2. 게시판 글수정 처리
		case "/" + MODULE +"/update.do":
			// service - dao --> request에 저장까지 해준다.
//			Long no = update(request);
//		 "board/view" 넘긴다. -> /WEB-INF/views/ + board/view + .do로 자동 이동.
		jspInfo = "redirect:view.do?no=" + update(request) + "&inc=0&page="
				+ pageObject.getPage() + "&perPageNum=" + pageObject.getPerPageNum();			
		break;

		//5. 게시판 글삭제 처리
		case "/" + MODULE +"/delete.do":
			// service - dao --> request에 저장까지 해준다.
			delete(request);
		jspInfo = "redirect:list.do?page=1&perPageNum=" + pageObject.getPerPageNum();
		// list.do 로 자동이동
		break;

		//6. 게시판 댓글 등록 처리
		case "/" + MODULE +"/replyWrite.do":
			// service - dao --> request에 저장까지 해준다.
			replyWrite(request);
			System.out.println("BoardController [query] - " + request.getQueryString());
			// list.do 로 자동이동
		jspInfo = "redirect:view.do?" + request.getQueryString() + "&inc=0";
		break;

		//7. 게시판 댓글 수정 처리
		case "/" + MODULE +"/replyUpdate.do":
			// service - dao --> request에 저장까지 해준다.
//			delete(request);
			// list.do 로 자동이동
			System.out.println("BoardController [query] - " + request.getQueryString());
			jspInfo = "redirect:view.do?no=" + request.getParameter("no");
		break;
		
		default:
			throw new Exception("페이지 오류 404 - 존재하지 않는 페이지 입니다.");
		}
		
		// jsp의 정보를 가지고 리턴한다.
		return jspInfo;
	}
	
	// 1. 게시판 리스트 처리
	private void list(HttpServletRequest request, PageObject pageObject) throws Exception {
		// 여기가 자바 코드입니다. servlet-controller(*)-Service-DAO

		//request.getServletPath(); -> AuthorityFilter 76줄에서 실행한 후 url 저장하도록 하고 있다.
		@SuppressWarnings("unchecked")
		List<BoardVO> list = (List<BoardVO>) ExeService.execute(Beans.get(AuthorityFilter.url), pageObject);
		// 서버객체 request에 담는다.
		request.setAttribute("list", list);

	}
	
	// 2. 게시판 글보기 처리
	// return type을 Long으로 한 이유 : 댓글을 가져오려면 글번호가 필요하다. 타입이 Long
	private Long view (HttpServletRequest request) throws Exception {
		// 여기가 자바 코드입니다. servlet - Controller - Service - DAO -> /board/view.do

		// 넘어오는 데이터 받기 - 글번호

		String strNo = request.getParameter("no");
		long no = Long.parseLong(strNo);

		String strInc = request.getParameter("inc");
		long inc = Long.parseLong(strInc);

		// 게시판 글보기 데이터 1개 가져오기
		BoardVO vo = (BoardVO)ExeService.execute(Beans.get(AuthorityFilter.url), new Long[]{no,inc});
		// 서버객체 request에 담는다.
		request.setAttribute("vo", vo);

		// 글번호를 리턴한다.
		return no;
	}
	
	//3. 게시판 글쓰기 처리
	private void write(HttpServletRequest request) throws Exception{
		// 1. 데이터 수집
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String writer = request.getParameter("writer");
		//vo객체를 만들어서 넣는다.
		BoardVO vo = new BoardVO();
		vo.setTitle(title);
		vo.setContent(content);
		vo.setWriter(writer);

		//DB에 데이터를 저장하는 처리를 한다.
		Integer result = (Integer)ExeService.execute(Beans.get(AuthorityFilter.url), vo);
		
		System.out.println("BoardController.write().result : " + result);
	}
	
	//4-1. 게시판 글수정 폼
	private void updateForm(HttpServletRequest request) throws Exception{
		// 자바 부분
		// 1.넘어오는 데이터 받기 - 글번호
		String strNo = request.getParameter("no");
		long no = Long.parseLong(strNo);
		// 2. 글번호에 맞는 데이터 가져오기 -> BoardViewService => /board/view.jsp
		String url = "/board/view.do"; // 현재 URL과 다르므로 강제 세팅함.
		BoardVO vo = (BoardVO)ExeService.execute(Beans.get(url), new Long[]{no,0L});
		// 3. 서버 객체에 넣기
		request.setAttribute("vo", vo);

	}

	//4-2. 게시판 글수정 처리
	private Long  update(HttpServletRequest request) throws Exception{
		//순수한 자바 코드 부분입니다.---------------

		// 1. 데이터 수집
		String strNo = request.getParameter("no");
		long no = Long.parseLong(strNo);
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String writer = request.getParameter("writer");
		// 2. DB처리 - update.jsp ->service -> dao
		BoardVO vo = new BoardVO();
		vo.setNo(no);
		vo.setTitle(title);
		vo.setContent(content);
		vo.setWriter(writer);

		//DB에 데이터를 저장하는 처리를 한다.
		String url = request.getServletPath();
		Integer result = (Integer)ExeService.execute(Beans.get(url), vo);

		if(result < 1) throw new Exception("게시판 글수정 - 수정할 데이터가 존재하지 않습니다.");
	
		return no;
	}

	//5. 게시판 글삭제
	private void delete(HttpServletRequest request) throws Exception{
		// 1. 데이터 수집
		String strNo = request.getParameter("no");
		long no = Long.parseLong(strNo);

		// 2. DB처리를 한다. -> delete.jsp
		String url = request.getServletPath();
		Integer result = (Integer)ExeService.execute(Beans.get(url), no);
		if(result == 0 ) throw new Exception("게시판 글삭제 오류 - 존재하지 않는 글은 삭제 불가합니다.");
	}
	
	// 6. 댓글 리스트 가져오기
		private void replyList(Long no, PageObject pageObject, HttpServletRequest request) 
		throws Exception{
			//DB에서 데이터 가져오기
			//연결URL => /board/view/do -> 게시판 글보기
			// 댓글 리스트는 URL이 존재하지 않으나 데이터를 가져오기 위해 강제 세팅해준다.
//			request.setAttribute("list", Beans.get("/board/replyList.do").service(new Object[] {no, pageObject}));
			// 처리되는 정보를 출력하는 프록시 구조의 프로그램을 거쳐 간다.
			request.setAttribute("list", 
					ExeService.execute(Beans.get("/board/replyList.do"), new Object[] {no,pageObject}));
		}
		// 7. 댓글 등록
		private void replyWrite(HttpServletRequest request) throws Exception{
			// 데이터 수집
			String strNo = request.getParameter("no");
			String content = request.getParameter("content");
			String writer = request.getParameter("writer");
			//VO 객체 생성과 저장
			BoardReplyVO vo = new BoardReplyVO();
			vo.setNo(Long.parseLong(strNo));
			vo.setContent(content);
			vo.setWriter(writer);
			// 정보를 출력하는 필터 처리가 된다.
			ExeService.execute(Beans.get(AuthorityFilter.url), vo);
			
			
		}

}
