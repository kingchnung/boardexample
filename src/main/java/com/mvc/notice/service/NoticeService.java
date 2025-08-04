package com.mvc.notice.service;

import java.util.List;

import com.mvc.notice.vo.NoticeVO;

public interface NoticeService {

	List<NoticeVO> noticeList(NoticeVO noticeVO);
	int noticeInsert(NoticeVO noticeVO);
	List<NoticeVO> noticeList();
	void viewCntUpdate(NoticeVO noticeVO);
	NoticeVO noticeDetail(NoticeVO noticeVO);
}
