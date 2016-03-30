package cn.edu.nju.iip.etl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import cn.edu.nju.iip.dao.DAO;
import cn.edu.nju.iip.dao.HJQKDAO;
import cn.edu.nju.iip.dao.RawHtmlDAO;
import cn.edu.nju.iip.dao.TBBZDAO;
import cn.edu.nju.iip.dao.TBPPJLDAO;
import cn.edu.nju.iip.model.RawHtml;
import cn.edu.nju.iip.redis.JedisPoolUtils;
import cn.edu.nju.iip.util.CommonUtil;

/**
 * 公路水运建设市场从业企业ETL tool
 * @author wangqiang
 *
 */
public class ConstructComETL implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger(ConstructComETL.class);
	//所属行业
	private String unitType;
	//企业名称list
	private Set<String> unitNameSet;
	//信用相关标签
	private String credit_tag;
	//基类DAO
	private DAO dao;
	
	private RawHtmlDAO RDao = new RawHtmlDAO();
	
	private int count = 0;
	
	/**
	 * 
	 * @param unitNameSet 企业名称集合 
	 * @param INDUSTRY 所属行业
	 * @param credit_tag 相关信用标签
	 */
	public ConstructComETL(String unitType,String credit_tag,DAO dao) {
		this.unitNameSet = CommonUtil.getUnitNameSet(unitType);
		this.unitType = unitType;
		this.credit_tag = credit_tag;
		this.dao = dao;
	}
	
	/**
	 * 根据标签组合取出id集合并剔除已经取过的
	 * @param unitName_tag
	 * @param credit_tag
	 * @return
	 */
	private Set<String> getRawDataId(String unitName) {
		Set<String> id_set = new HashSet<String>();
		try{
			Jedis jedis = JedisPoolUtils.getInstance().getJedis();
			Set<String> already_taged_id_set = jedis.smembers("already_taged_id:"+unitName+"-"+credit_tag);
			id_set = jedis.sinter(unitType+":"+unitName,"信用相关标签:"+credit_tag);
			Iterator<String> it = id_set.iterator();
			while(it.hasNext()) {
				String raw_data_id = it.next();
				//若该doc已经被打过标签则剔除
				if(already_taged_id_set.contains(raw_data_id)) {
					it.remove();
				}
			}
			JedisPoolUtils.getInstance().returnRes(jedis);
		}catch(Exception e) {
			logger.error("getRawDataId error",e);
		}
		return id_set;
	}
	
	/**
	 * 根据id集合取出原始数据集合
	 * @param id_set
	 * @return
	 */
	private List<RawHtml> getRawHtmlList(Set<String> id_set) {
		List<RawHtml> list = new ArrayList<RawHtml>();
		try{
			for(String id:id_set) {
				id = id.split(":")[1];
				RawHtml raw_html = RDao.getRawHtml(Integer.valueOf(id));
				list.add(raw_html);
			}
		}catch(Exception e) {
			logger.error("getRawHtmlList error",e);
		}
		return list;
	}
	
	private void saveData(List<RawHtml> list,String unitName) {
		try{
			for(RawHtml raw_html:list) {
				raw_html.setUnitName(unitName);
				raw_html.setIndustry(unitType);
				boolean flag = dao.saveData(raw_html);
				if(flag) {
					Jedis jedis = JedisPoolUtils.getInstance().getJedis();
					jedis.sadd("already_taged_id:"+unitName+"-"+credit_tag, "jw_raw_data:"+raw_html.getId());
					JedisPoolUtils.getInstance().returnRes(jedis);
					count++;
				}
			}
		}catch(Exception e) {
			logger.error("ConstructComETL saveData error",e);
		}
	}
	
	/**
	 * ETL相关数据至总集成表（TBBZ）
	 */
	public void ETLToTable() {
		for(String unitName:unitNameSet) {
			Set<String> id_set = getRawDataId(unitName);
			List<RawHtml> raw_htm_list = getRawHtmlList(id_set);
			saveData(raw_htm_list,unitName);
		}
		logger.info(unitType+"-"+credit_tag+" ETL finish! new add data size:"+count);
	}
	
	public static void ConstructComETLMain() {
		ExecutorService Service = Executors.newCachedThreadPool();
		ConstructComETL roadTBBZetl = new ConstructComETL("公路建设企业","表彰",new TBBZDAO());
		Service.execute(roadTBBZetl);
		
		ConstructComETL roadHJQKetl = new ConstructComETL("公路建设企业","获奖",new HJQKDAO());
		Service.execute(roadHJQKetl);
		
		ConstructComETL roadTBPPJLetl = new ConstructComETL("公路建设企业","批评",new TBPPJLDAO());
		Service.execute(roadTBPPJLetl);
		
		ConstructComETL shipTBBZetl = new ConstructComETL("水运建设企业","表彰",new TBBZDAO());
		Service.execute(shipTBBZetl);
		
		ConstructComETL shipHJQKetl = new ConstructComETL("水运建设企业","获奖",new HJQKDAO());
		Service.execute(shipHJQKetl);
		
		ConstructComETL shipTBPPJLetl = new ConstructComETL("水运建设企业","批评",new TBPPJLDAO());
		Service.execute(shipTBPPJLetl);
		Service.shutdown();
		
	}
	
	public static void main(String[] args) throws Exception {
		ConstructComETLMain();
	}

	@Override
	public void run() {
		try{
			ETLToTable();
		}catch(Exception e) {
			logger.error("ConstructComETL run() error",e);
		}
		
	}

}
