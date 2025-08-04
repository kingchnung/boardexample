package com.mvc.notice.service;

import java.util.List;

import com.mvc.notice.dao.NoticeDAO;
import com.mvc.notice.vo.NoticeVO;

public class NoticeServiceImpl implements NoticeService{
	private static NoticeServiceImpl service = null;
	private NoticeDAO dao;
	
	private NoticeServiceImpl() {
		dao = NoticeDAO.getInstance();
	}
	
	public static NoticeServiceImpl getInstance() {
		if(service == null) {
			service = new NoticeServiceImpl();
		}
		return service;
	}

	@Override
	public List<NoticeVO> noticeList() {
		List<NoticeVO> list = dao.noticeList();
		return list;
	}
	
	@Override
	public List<NoticeVO> noticeList(NoticeVO noticeVO) {
		List<NoticeVO> list = dao.noticeList(noticeVO);
		return list;
	}

	@Override
	public int noticeInsert(NoticeVO noticeVO) {
		int result = dao.noticeInsert(noticeVO);
		return result;
	}

	@Override
	public void viewCntUpdate(NoticeVO noticeVO) {
		dao.viewCnt(noticeVO);
		
	}

	@Override
	public NoticeVO noticeDetail(NoticeVO noticeVO) {
		NoticeVO notice = dao.noticeDetail(noticeVO);
		notice.setContent(notice.getContent().replaceAll("\n", "<br />"));
		return notice;
	}
	
	
}
