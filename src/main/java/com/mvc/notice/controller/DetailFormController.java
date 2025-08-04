package com.mvc.notice.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mvc.common.controller.Controller;
import com.mvc.notice.service.NoticeService;
import com.mvc.notice.service.NoticeServiceImpl;
import com.mvc.notice.vo.NoticeVO;

public class DetailFormController implements Controller {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String num = request.getParameter("noticeNo");
		NoticeVO noticeVO = new NoticeVO();
		noticeVO.setNoticeNo(Integer.parseInt(num));
		
		NoticeService service = NoticeServiceImpl.getInstance();
		service.viewCntUpdate(noticeVO);
		
		NoticeVO notice = service.noticeDetail(noticeVO);
		
		request.setAttribute("detail", notice);
		
		return "/notice/detailNotice";
	}

}
