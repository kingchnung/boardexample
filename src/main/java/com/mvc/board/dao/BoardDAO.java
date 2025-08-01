package com.mvc.board.dao;

import static com.mvc.common.util.DBUtil.getConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mvc.board.vo.BoardVO;

public class BoardDAO {
	private static BoardDAO instance = null;
	
	public static BoardDAO getInstance(){
		if(instance == null) {
			instance = new BoardDAO();
		}
		return instance;
	}
	
	private BoardDAO() {} // 임의로 인스턴스 생성 막기 위해 생성자 private
	
	private BoardVO addBoard(ResultSet rs) throws SQLException{
		BoardVO boardVO = new BoardVO();
		boardVO.setNum(rs.getInt("num"));
		boardVO.setAuthor(rs.getString("author"));
		boardVO.setTitle(rs.getString("title"));
		boardVO.setWriteday(rs.getString("writeday"));
		boardVO.setReadcnt(rs.getInt("readcnt"));
		
		return boardVO;
	}
	
	public List<BoardVO> boardList() {
		List<BoardVO> list = new ArrayList<>();
		StringBuilder query = new StringBuilder();
		query.append("SELECT num, author, title, ");
		query.append("TO_CHAR(writeday, 'YYYY/MM/DD') writeday, readcnt ");
		query.append("FROM board ");
		query.append("order by num desc ");
		
		try(Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query.toString());
				ResultSet rs = pstmt.executeQuery();) {
			
			while(rs.next()) {
				list.add(addBoard(rs));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		
		return list;
	}
	
	public List<BoardVO> boardList(BoardVO boardVO) {
		List<BoardVO> list = new ArrayList<>();
		String search = boardVO.getSearch();
		String keyword = boardVO.getKeyword();
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT num, author, title, ");
		query.append("TO_CHAR(writeday, 'YYYY/MM/DD') writeday, readcnt ");
		query.append("FROM board ");
		
		switch(search) {
		case "title" -> query.append(" WHERE title LIKE ? ");
		case "author" -> query.append(" WHERE author LIKE ? ");
		}
		query.append("ORDER BY num desc ");
		
		try(Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query.toString());) {
			
			if(!search.equals("all")) {
				pstmt.setString(1, "%" + keyword + "%");
			}
			
			try(ResultSet rs = pstmt.executeQuery()) {
				
				while(rs.next()) {
					list.add(addBoard(rs));
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		
		return list;
	}
	
	public int boardInsert(BoardVO boardVO) {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO board(num, author, title, content, passwd) ");
		query.append("VALUES(board_seq.nextval,  ?,  ?,  ?,  ?) ");
		
		int result = 0;
		
		try(Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query.toString());) {
			
			pstmt.setString(1, boardVO.getAuthor());
			pstmt.setString(2, boardVO.getTitle());
			pstmt.setString(3, boardVO.getContent());
			pstmt.setString(4, boardVO.getPasswd());
			
			result = pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return result;
	}

	public void readCount(BoardVO boardVO) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE board SET readcnt = readcnt + 1 ");
		query.append("WHERE num = ? ");
		
		try(Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query.toString());) {
			
			pstmt.setInt(1, boardVO.getNum());
			pstmt.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

	public BoardVO boardDetail(BoardVO boardVO) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT num, author, title, content, ");
		query.append("TO_CHAR(writeday, 'YYYY-MM-DD HH24:MI:SS') writeday, readcnt ");
		query.append("FROM board WHERE num = ? ");
		BoardVO resultData = null;
		
		try(Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query.toString());) {
			
			pstmt.setInt(1, boardVO.getNum());
			
			try(ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					resultData = addBoard(rs);
					resultData.setContent(rs.getString("content"));
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		
		return resultData;
	}

	public int boardUpdate(BoardVO boardVO) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE board SET title = ?, content = ? ");
		if(boardVO.getPasswd() != "") query.append(", passwd = ? ");
		query.append("WHERE num = ? ");
		
		int result = 0;
		try(Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query.toString());){
			
			pstmt.setString(1, boardVO.getTitle());
			pstmt.setString(2, boardVO.getContent());
			
			if(boardVO.getPasswd() != "") {
				pstmt.setString(3,	boardVO.getPasswd());
				pstmt.setInt(4, boardVO.getNum());
			} else {
				pstmt.setInt(3, boardVO.getNum());
			}
			
			result = pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		
		return result;
	}

	public int boardDelete(BoardVO boardVO) {
		StringBuilder query = new StringBuilder();
		query.append("DELETE FROM board WHERE num = ? ");
		
		int result = 0;
		try(Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query.toString());) {
			
			pstmt.setInt(1, boardVO.getNum());
			
			result = pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return result;
	}

	public int boardpasswdCheck(BoardVO boardVO) {
		String query = """
				SELECT CASE
				WHEN EXISTS(SELECT 1 FROM board WHERE num = ? AND passwd = ?)
				THEN 1
				ELSE 0
				END AS result
				FROM dual
				""";
		
		int result = 0;
		
		try(Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query);) {
			
			pstmt.setInt(1, boardVO.getNum());
			pstmt.setString(2, boardVO.getPasswd());
			
			try(ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					result = rs.getInt("result");
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return result;
	}
}
