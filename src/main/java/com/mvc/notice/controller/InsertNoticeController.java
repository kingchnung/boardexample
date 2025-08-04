package com.mvc.notice.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mvc.common.controller.Controller;
import com.mvc.notice.service.NoticeService;
import com.mvc.notice.service.NoticeServiceImpl;
import com.mvc.notice.vo.NoticeVO;

public class InsertNoticeController implements Controller {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String path = null;
		NoticeVO noticeVO = new NoticeVO();
		noticeVO.setTitle(request.getParameter("title"));
		noticeVO.setWriter(request.getParameter("writer"));
		noticeVO.setContent(request.getParameter("content"));
		noticeVO.setIsImportant(request.getParameter("isImportant"));
		
		NoticeService service = NoticeServiceImpl.getInstance();
		int result = service.noticeInsert(noticeVO);
		
		if(result == 1) {
			path = "/notice/getNoticeList.do";
		} else {
			request.setAttribute("errorMsg", "등록 완료에 문제가 있어 잠시 후 다시 입력해주세요.");
			path = "/board/insertForm";
		}
		
		return path;
	}
}
