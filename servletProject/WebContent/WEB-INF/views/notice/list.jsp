<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="pageObject" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 리스트</title>

	<!-- 부트스트랩 라이브러리 등록 - CDN 방식 -->
<!--   <meta name="viewport" content="width=device-width, initial-scale=1"> -->
<!--   <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"> -->
<!--   <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script> -->
<!--   <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script> -->

<style type="text/css">
.dataRow:hover{
	cursor:pointer;
	background: #eee;
}
</style>

<script type="text/javascript">
// 객체 선택에 문제가 있다. 아래  Document가 다 로딩이 되면 실행되는 스크립트 작성
// jquery -> $(function(){처리문 만들기;}) = jquery(function(){처리문 만들기;})
$(function(){ // jquery에서 익명함수를 전달해서 저장해놨다가 Document가 로딩이 되면 호출해서 처리한다.
	// 데이터 한줄 선택하기와 이벤트 처리
	$(".dataRow").click(function(){
		//alert($(this));
		//$(this): 이벤트가 일어난(현재 처리되고 있는 객체) 자신을 의미
		//.find(selector): .앞에 객체 안에서 선택한 것을 찾아라.
		//.text(): 객체(태그) 사이에 문자를 가져온다.
		//.text(data):객체(태그) 사이에 문자를 세팅한다.
		var no = $(this).find(".no").text();
		location = "view.do?no=" + no + "&inc=1&page=${pageObject.page}" + "&perPageNum=${pageObject.perPageNum}";
	});
});
</script>
</head>
<body>
<div class="container">
<h1>공지사항 리스트</h1>
<table class="table">
	<tr>
		<th>번호</th>
		<th>제목</th>
		<th>공지시작일</th>
		<th>공지종료일</th>
		<th>최근수정일</th>
	</tr>
	<c:forEach items="${list }" var="vo">
		<tr class="dataRow">
			<td class="no">${vo.no }</td>
			<td>${vo.title }</td>
			<td>${vo.startDate }</td>
			<td>${vo.endDate }</td>
			<td>${vo.updateDate }</td>
		</tr>
	</c:forEach>
	<tr>
	<td colspan="5">
		<a href="writeForm.do?perPageNum=${pageObject.perPageNum }" class="btn btn-default">공지등록</a>
	</tr>
	<tr>
		<td colspan="5">
			<pageObject:pageNav listURI="list.do" pageObject="${pageObject }" />	
		</td>
	</tr>
</table>
</div>
</body>
</html>