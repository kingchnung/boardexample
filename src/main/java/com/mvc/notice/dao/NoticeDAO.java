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
		noticeVO.setIsImportant(rs.getString("is_important"));
		
		return noticeVO;
	}
	
	
	public List<NoticeVO> noticeList() {
		List<NoticeVO> list = new ArrayList<NoticeVO>();
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
	
	
}
