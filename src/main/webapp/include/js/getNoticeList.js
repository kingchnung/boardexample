/**
 * 
 */
$("#writeForm").on("click", function(){
	locationProcess("/notice/insertFormNotice.do");
});


$("#keyword").on("keydown", function(event){
	if(event.key === "Enter") {
		event.preventDefault();
		$("#searchData").click(); //주석 해제 시 검색 버튼 클릭과 동일한 동작 실행
	}
});

$("#search").on("change", function(){
	const seleted=$(this).val();
	
	if(seleted === "all") {
		$("#keyword").val("").attr("placeholder", "");
	} else {
		$("#keyword").val("").attr("placeholder", "검색어 입력");
	}
});

$("#searchData").on("click", function(){
	const searchType = $("#search").val();
	
	if(searchType !== "all") {
		if(!checkForm("#keyword", "검색어를")) return;
	} else {
		$("#keyword").val("");
	}
	
	actionProcess("#f_search", "post", "/notice/getNoticeList.do");
});