package com.mvc.notice.service;

import java.util.List;

import com.mvc.notice.vo.NoticeVO;

public interface NoticeService {

	List<NoticeVO> NoticeList(NoticeVO noticeVO);
	
}
