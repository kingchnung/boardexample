package com.mvc.board.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mvc.board.service.BoardService;
import com.mvc.board.service.BoardServiceImpl;
import com.mvc.board.vo.BoardVO;
import com.mvc.common.controller.Controller;

public class InsertBoardController implements Controller {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String path = null;
		
		// 1. VO 객체에 데이터 바인딩
		BoardVO boardVO = new BoardVO();
		boardVO.setTitle(request.getParameter("title"));
		boardVO.setAuthor(request.getParameter("author"));
		boardVO.setContent(request.getParameter("content"));
		boardVO.setPasswd(request.getParameter("passwd"));
		
		BoardService service = BoardServiceImpl.getInstance();
		int result = service.boardInsert(boardVO);
		
		if(result == 1) {	 //입력 성공시
			path = "/board/getBoardList.do";	 //게시판 리스트 요청
		} else { 		//입력 실패시
			request.setAttribute("errorMsg", "등록 완료에 문제가 있어 잠시 후 다시 입력해 주세요.");
			path = "/board/insertForm"; 		//입력화면으로 이동
		}
		
		// 3. 화면 네비게이션
		return path;
	}
}
