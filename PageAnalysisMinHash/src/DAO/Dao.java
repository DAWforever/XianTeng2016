package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Config.Config;
import DAO.DBConnection;
import Model.DT_CLEAR_DATA;
import Model.MINHASH_CODE;

public class Dao {
	DBConnection dbConnection;
	Connection conn;
	ResultSet rs;
	PreparedStatement ps;
	public static int PAGESIZE = Integer.parseInt(Config.getValue("PAGESIZE"));
	
	public Connection getConnection(){
		dbConnection = new DBConnection();
		Connection connection = dbConnection.getConnect();
		return connection;
	}
	
	public int getStart(String tablename){
		conn = getConnection();
		try {
			Statement stmt = conn.createStatement();
			String sql = "select max(RAWID) from " + tablename;
			rs = stmt.executeQuery(sql);
			while (rs.next()){
				if (rs.getString(1) == null)
					return 0;
				else
					return Integer.parseInt(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			dbConnection.connectClose(conn, ps, rs);
		}
		return 0;
	}
	
	public List getRAWDATA(int offset, int page){
		List dataResult = new ArrayList();
		conn = getConnection();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			int start = page*PAGESIZE + offset;
			String sql = "select * from DT_RAW_DATA where id > "+ start +" order by id limit " + PAGESIZE;
			rs = stmt.executeQuery(sql);
			dataResult.add(rs);
			dataResult.add(conn);
		} catch (Exception e) {
			try {
				System.out.println("id " + rs.getInt(1));
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally{
			//dbConnection.connectClose(conn, ps, rs);
		}
		
		return dataResult;
	}
	
	public void saveCLEARDATA(ArrayList<DT_CLEAR_DATA> result){
		
		conn = getConnection();
		
		String sql = "insert into DT_CLEAR_DATA values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			ps = conn.prepareStatement(sql);
			for (DT_CLEAR_DATA data : result){
				ps.setString(1, data.getID());
				ps.setInt(2, data.getRawID());
				ps.setString(3, data.getListID());
				ps.setString(4, data.getCategory());
				ps.setString(5, data.getSource());
				ps.setString(6, data.getSiteName());
				ps.setString(7, data.getChannel());
				ps.setString(8, data.getUrlName());
				ps.setString(9, data.getUrl());
				ps.setString(10, data.getTitle());
				ps.setString(11, data.getHerfText());
				ps.setString(12, data.getText());
				ps.setString(13, data.getHtmlText());
				ps.setString(14, data.getTextCode());
				ps.setString(15, data.getInputTime());
				ps.setString(16, data.getPubDate());
				ps.setString(17, data.getBatchNo());
				ps.setBoolean(18, data.getDuplicate_tag());
				
				ps.addBatch();
			}
			
			ps.executeBatch();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			dbConnection.connectClose(conn, ps, rs);
		}
	}
	
	public int getAndSetBatchNo() {
		int bacthno = 0;
		int newbatchno = 0;
		conn = getConnection();
		try {

			String sql = "select paravalue from DT_MANAGE where parano = 'ClearBatchNo' order by paravalue asc" ;
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()){
				bacthno = Integer.parseInt(rs.getString(1));
			}
			newbatchno = bacthno + 1;
			sql = "update DT_MANAGE set paravalue = " + newbatchno
					+ " where parano = 'ClearBatchNo'";
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbConnection.connectClose(conn, ps, rs);
		}
		return newbatchno;
	}

	public void saveMINHASHCODE(ArrayList<MINHASH_CODE> hashcodeSet) {
		conn = getConnection();
		
		String sql = "insert into MINHASH_CODE values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			ps = conn.prepareStatement(sql);
			for (MINHASH_CODE data : hashcodeSet){
				ps.setString(1, data.getID());
				ps.setString(2, "1:" + data.getHash_code().get(0));
				ps.setString(3, "2:" + data.getHash_code().get(1));
				ps.setString(4, "3:" + data.getHash_code().get(2));
				ps.setString(5, "4:" + data.getHash_code().get(3));
				ps.setString(6, "5:" + data.getHash_code().get(4));
				ps.setString(7, "6:" + data.getHash_code().get(5));
				ps.setString(8, "7:" + data.getHash_code().get(6));
				ps.setString(9, "8:" + data.getHash_code().get(7));
				ps.setString(10, "9:" + data.getHash_code().get(8));
				ps.setString(11, "10:" + data.getHash_code().get(9));
				ps.setString(12, "11:" + data.getHash_code().get(10));
				ps.setString(13, "12:" + data.getHash_code().get(11));
				ps.setString(14, "13:" + data.getHash_code().get(12));
				ps.setString(15, "14:" + data.getHash_code().get(13));
				ps.setString(16, "15:" + data.getHash_code().get(14));
				ps.setString(17, "16:" + data.getHash_code().get(15));
				ps.setString(18, "17:" + data.getHash_code().get(16));
				ps.setString(19, "18:" + data.getHash_code().get(17));
				ps.setString(20, "19:" + data.getHash_code().get(18));
				ps.setString(21, "20:" + data.getHash_code().get(19));
				ps.addBatch();
			}
			
			ps.executeBatch();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			dbConnection.connectClose(conn, ps, rs);
		}
		
	}

	public int getTableSize(String tablename) {
		conn = getConnection();
		try {
			Statement stmt = conn.createStatement();
			String sql = "select count(*) from " + tablename;
			rs = stmt.executeQuery(sql);
			while (rs.next()){
				if (rs.getString(1) == null)
					return 0;
				else
					return Integer.parseInt(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			dbConnection.connectClose(conn, ps, rs);
		}
		return 0;
	}
	
	public static void main(String[] args) {
		Dao dao = new Dao();
		System.out.println(dao.getTableSize("DT_RAW_DATA"));
	}

	/*
	public ArrayList<MINHASH_CODE> getMinHashCode(int start, int end) {
		ArrayList<MINHASH_CODE> result = new ArrayList<MINHASH_CODE>();
		conn = getConnection();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String sql = "select * from MINHASH_CODE where rawid > "+ start +" order by rawid limit " + (end - start);
			rs = stmt.executeQuery(sql);
			rs.previous();
			while(rs.next()){
				MINHASH_CODE mhc = new MINHASH_CODE();
				mhc.setId(rs.getInt(1));
				for (int i = 2; i < 22; i++){
					mhc.getHash_code_str().add(rs.getString(i));
				}
				result.add(mhc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			dbConnection.connectClose(conn, ps, rs);
		}
		
		return result;
	}

	 */
	/*
	public void saveDuplicateTag(ArrayList<DUPLICATE_TAG> dt_list) {
		// TODO Auto-generated method stub
		conn = getConnection();
		
		String sql = "insert into DUPLICATE_TAG values (?,?)";
		try {
			ps = conn.prepareStatement(sql);
			for (DUPLICATE_TAG data : dt_list){
				ps.setString(1, Config.getValue("NODENAME") + "_" + data.getRawid());
				ps.setBoolean(2, data.getTag());
				ps.addBatch();
			}
			
			ps.executeBatch();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			dbConnection.connectClose(conn, ps, rs);
		}
		
	}
	*/
}
