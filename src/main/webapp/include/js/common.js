/**
 * 
 */
const actionProcess = function(form, method, action) {
	$(form).attr({
		"method" : method,
		"action" : action
	}).submit();
}

const locationProcess = function(url) {
	location.href=url;
}

/* 함수명: chkData(유효성 체크 대상, 메시지 내용) 
 * 출력영역: alert으로.
 * 예시 : if (!chkData("#keyword","검색어를")) return;
 * */ 
function chkData(item, msg) {
	if($(item).val().replace(/\s/g,"")=="") {
		alert(msg+" 입력해 주세요.");
		$(item).val("");
		$(item).focus();
		return false;
	} 
	return true;
}

function dataCheck(item, out, msg) {
	if($(item).val().replace(/\s/g, "") == "") {
		$(out).html(msg + " 입력해 주세요");
		$(item).val("");
		return false;
	}
	return true;
}

function checkForm(item, msg) {
	let message = "";
	if($(item).val().replace(/\s/g, "") === "") {
		message = `${msg} 입력해 주세요.`;
		$(item).attr("placeholder", message);
		return false;
	}
	return true;
}