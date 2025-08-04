package com.mvc.notice.dao;

import static com.mvc.common.util.DBUtil.getConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mvc.notice.vo.NoticeVO;

public class NoticeDAO {
	private static NoticeDAO instance = null;
	private NoticeDAO() {}
	
	
	public static NoticeDAO getInstance() {
		if(instance == null) {
			instance = new NoticeDAO();
		}
		
		return instance;
	}

	private NoticeVO addNotice(ResultSet rs) throws SQLException {
		NoticeVO noticeVO = new NoticeVO();
		noticeVO.setNoticeNo(rs.getInt("notice_no"));
		noticeVO.setTitle(rs.getString("title"));
		noticeVO.setContent(rs.getString("content"));
		noticeVO.setWriter(rs.getString("writer"));
		noticeVO.setWriteday(rs.getString("writeday"));
		noticeVO.setViewCnt(rs.getInt("view_count"));
//		noticeVO.setIsImportant(rs.getString("is_important"));
		
		return noticeVO;
	}
	
	
	public List<NoticeVO> noticeList() {
		List<NoticeVO> list = new ArrayList<>();
		StringBuilder query = new StringBuilder();
		query.append("SELECT notice_no, title, content, writer, "
				+ "TO_CHAR(writeday, 'YYYY/MM/DD') writeday, view_count, ");
		query.append("is_important FROM notice ORDER BY notice_no desc ");
		
		try(Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query.toString());
				ResultSet rs = pstmt.executeQuery()){
			
			while(rs.next()) {
				list.add(addNotice(rs));
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		
		return list;
	}
	
	public List<NoticeVO> noticeList(NoticeVO noticeVO) {
		List<NoticeVO> list = new ArrayList<>();
		String search = noticeVO.getSearch();
		String keyword = noticeVO.getKeyword();
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT notice_no, title, content, writer, ");
		query.append("TO_CHAR(writeday, 'YYYY/MM/DD') writeday, view_count ");
		query.append("FROM notice ");
		
		switch(search) {
		case "title" -> query.append("WHERE title LIKE ? ");
		case "writer" -> query.append("WHERE writer LIKE ? ");
		}
		query.append("ORDER BY notice_no desc ");
		
		try(Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query.toString());){
			
			if(!search.equals("all")) {
				pstmt.setString(1, "%" + keyword + "%");
			}
			
			try(ResultSet rs = pstmt.executeQuery()) {
				
				while(rs.next()) {
					list.add(addNotice(rs));
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		
		return list;
	}


	public int noticeInsert(NoticeVO noticeVO) {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO notice(notice_no, title, content, writer, is_important) ");
		query.append("VALUES(notice_seq.nextval, ?, ?, ?, ?) ");
		int result = 0;
		
		try(Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query.toString());) {
			
			pstmt.setString(1, noticeVO.getTitle());
			pstmt.setString(2, noticeVO.getContent());
			pstmt.setString(3, noticeVO.getWriter());
			pstmt.setString(4, noticeVO.getIsImportant());
			
			result = pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		
		return result;
	}


	public void viewCnt(NoticeVO noticeVO) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE notice SET view_count = view_count + 1 ");
		query.append("WHERE notice_no = ? ");
		
		try(Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query.toString());){
			
			pstmt.setInt(1, noticeVO.getNoticeNo());
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
	
	public NoticeVO noticeDetail(NoticeVO noticeVO) {
		NoticeVO resultData = null;
		StringBuilder query = new StringBuilder();
		query.append("SELECT notice_no, writer, title, content, ");
		query.append("TO_CHAR(writeday, 'YYYY-MM-DD HH24:MI:SS') writeday, view_count ");
		query.append("FROM notice WHERE notice_no = ? ");
		
		try(Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query.toString())){
			
			pstmt.setInt(1, noticeVO.getNoticeNo());
			
			try(ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					resultData = addNotice(rs);
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
