/**
 * 
 */
$("#writeForm").on("click", function(){
	/* 정상작동 확인용*/
	//console.log("글쓰기 버튼 클릭");
	locationProcess("/board/insertForm.do");
});

$(".goDetail").on("click", function(){
	const $row = $(this).closest("tr");
	const num = $row.data("num");
	console.log("num = " + num);
	
	//폼 태그 내 num 요소에 value 설정. post 방식으로 상세 페이지 이동
	$("#num").val(num);
	actionProcess("#detailForm", "post", "/board/detailBoard.do");
	
	//get 방식 상세페이지 이동
	//locationProcess(`/board/detailBoard.do?num=${num}`);
});