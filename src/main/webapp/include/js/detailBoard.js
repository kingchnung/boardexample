/**
 * 
 */
let buttonCheck = ""; //수정 버튼과 삭제 버튼을 구별하기 위한 변수

const pwdInit = function(value) {
	$("#pwdFormArea").css("visibility", value);
}

const btnInit = function(){
	$("#message")
		.removeClass("text-danger")
		.addClass("text-primary")
		.text("작성시 입력한 비밀번호를 입력해 주세요.");
}

pwdInit("hidden");

$("#updateFormBtn").on("click", function(){
	/*actionProcess("#dataForm", "get", "/board/updateForm.do");*/
	
	pwdInit("visible");
	btnInit();
	buttonCheck = "updateButton";
});

$("#insertFormBtn").on("click", function(){
	locationProcess("/board/insertForm.do");
});
$("#boardListBtn").on("click", function(){
	locationProcess("/board/getBoardList.do");
});

$("#boardDeleteBtn").on("click", function(){
	/*if(confirm("정말 삭제하시겠습니까?"))
		actionProcess("#dataForm", "post", "/board/deleteBoard.do");*/
	
	pwdInit("visible");
	btnInit();
	buttonCheck = "deleteButton";
});

/*비밀번호 입력창에서 키 처리 이벤트*/
$("#passwd").on("keyup", () => {
	btnInit();
});

$("#passwdBtn").on("click", function(){
	if(!dataCheck("#passwd", "#message", "비밀번호를")) return;
	else {
		$.ajax({
			url : "/board/passwdCheck.do",
			data : $("#pwdForm").serialize(),
			method : "post",
			dataType : "text"
		}).done(function(resultData) {
			const boardNumber = parseInt(resultData);
			
			if(boardNumber === 0) {
				$("#message")
				.removeClass("text-primary")
				.addClass("text-danger")
				.text("작성시 입력한 비밀번호가 일치하지 않습니다.");
				$("#passwd").select();
			} else if(boardNumber === 1) {
				$("message")
				.removeClass("text-danger text-primary")
				.text("");
				
				if(buttonCheck === "updateButton") {
					actionProcess("#dataForm", "get", "/board/updateForm.do");
				} else if(buttonCheck === "deleteButton") {
					if(confirm("정말 삭제하시겠습니까?")) {
						actionProcess("#dataForm", "post", "/board/deleteBoard.do");
					}
				}
			}
		}).fail(function(){
			alert('시스템 오류 입니다. 관리자에게 문의 하세요');
		});
	}
});

$("#passwdCancelBtn").on("click", function(){
	$("passwd").val("");
	pwdInit("hidden");
	buttonCheck="";
});