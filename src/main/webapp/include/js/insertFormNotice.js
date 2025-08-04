/**
 * 
 */
$("#noticeInsert").on("click", function(){
	if(!chkData("#writer", "이름을")) return;
	else if(!chkData("#title", "제목을")) return;
	else if(!chkData("#content", "작성한 내용을")) return;
	else {
		actionProcess("#f_writeForm", "post", "/notice/insertNotice.do");	
	}
});

$("#noticeListBtn").on("click", () => {
	locationProcess("/notice/getNoticeList.do");
});