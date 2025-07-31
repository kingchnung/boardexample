package com.mvc.board.dao;

import static com.mvc.common.util.DBUtil.getConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
}
