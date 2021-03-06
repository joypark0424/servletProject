package com.webjjang.qna.service;

import com.webjjang.qna.dao.QnaDAO;
import com.webjjang.main.controller.Service;

public class QnaViewService implements Service{

	//dao가 필요하다. 밖에서 생성한 후 넣어준다. - 1. 생성자. 2. setter()
	private QnaDAO dao;

	public QnaViewService() {
		System.out.println("QnaViewService.QnaViewServie() - 질문답변보기 객체 생성");
	}

	@Override
	public void setDAO(Object dao) {
		System.out.println("QnaViewService.setDAO().dao : " + dao);
		this.dao = (QnaDAO) dao;
	}
	
	@Override
	public Object service(Object obj) throws Exception {
		// TODO Auto-generated method stub
		// 글보기와 글수정도 사용을 한다. 글보기할때(list -> view) 조회수1증가 해야하고 
		// 글수정할때는 (update->view) 증가하지 않는다. 데이터가 2개 넘어오게 된다. [글번호, 증가여부]
		// obj[0] - no /obj[1] - inc
		Object[] objs = (Object[]) obj;
		Long no = (Long) objs[0];
		Long inc = (Long) objs[1];
		
		if(inc == 1) dao.increase(no);
		return dao.view(no);
	}

}
