package com.webjjang.member.service;

import com.webjjang.main.controller.Service;
import com.webjjang.member.dao.MemberDAO;
import com.webjjang.member.vo.MemberVO;

public class MemberJoinService implements Service {

	private MemberDAO dao;
	
	public MemberJoinService() {
		System.out.println("MemberWriteService.MemberWriteService - 객체 생성");
	}

	@Override
	// 넘어오는 데이터 : MemberVO
	public Object service(Object obj) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("MemberViewService.service().obj : " + obj);
		// dao가 null이면 dao.gradeModify() 메서드를 호출해서 사용하려고 하면 NullPointException이 일어난다.
		return dao.join((MemberVO) obj);
	}

	@Override
	public void setDAO(Object dao) {
		// TODO Auto-generated method stub
		// Init.init() 객체 생성 후 조립할때 실행 - 서버가 시작이 되면서 확인 - 이클립스 console창에 확인
		System.out.println("MemberJoinService.setDAO().dao : " + dao);
		this.dao = (MemberDAO) dao;

	}

}
