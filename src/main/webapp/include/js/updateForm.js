/**
 * 
 */
$("#boardUpdateBtn").on("click", function(){
	actionProcess("#f_updateForm", "post", "/board/updateForm.do")
});