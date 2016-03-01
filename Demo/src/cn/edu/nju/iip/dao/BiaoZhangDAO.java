package cn.edu.nju.iip.dao;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.edu.nju.iip.model.BiaoZhangData;

@Service
public class BiaoZhangDAO extends DAO{
	
private static final Logger logger = LoggerFactory.getLogger(BiaoZhangDAO.class);
	
	public void saveBiaoZhangData(BiaoZhangData BData) {
		try{
			begin();
			getSession().save(BData);
			commit();
		}catch(Exception e) {
			rollback();
			logger.error("saveBiaoZhangData failed!",e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<BiaoZhangData> getBiaoZhangDataList(String unitName) {
		List<BiaoZhangData> list = null;
		try{
			begin();
			Query query = getSession().createQuery("from BiaoZhangData where Corp_Name=:Corp_Name");
			query.setString("Corp_Name", unitName);
			list = query.list();
			commit();
		}catch(Exception e) {
			rollback();
			logger.error("getJudgeDoc failed!",e);
		}
		return list;
	}
	
	public static void main(String[] args) {
		BiaoZhangDAO dao = new BiaoZhangDAO();
		logger.info(dao.getBiaoZhangDataList("重庆工业设备安装集团有限公司").size()+"");
	}

}
