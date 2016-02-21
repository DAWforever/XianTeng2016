package cn.edu.nju.iip.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.edu.nju.iip.dao.JudgeDocDAO;
import cn.edu.nju.iip.dao.NewsDAO;
import cn.edu.nju.iip.model.JWNews;
import cn.edu.nju.iip.model.JudgeDoc;

@Controller
public class SearchController {
	
	private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
	
	@Autowired
	private NewsDAO newsDAO;
	
	@Autowired
	private JudgeDocDAO judgeDocDAO;
	
	/**
	 * 根据type 选择特定搜索
	 * @param query
	 * @param page
	 * @param model
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/CoreQuery")
	public String CoreQuery(String query,Model model,HttpServletRequest request) {
		logger.info("query="+query);
		List<JWNews> pos_list = newsDAO.getPosNews(query);
		List<JWNews> neg_list = newsDAO.getNegNews(query);
		List<JWNews> list = newsDAO.getAllNews(query);
		List<JudgeDoc> judge_list = judgeDocDAO.getJudgeDoc(query);
		request.getSession().setAttribute("judge_list", judge_list);
		request.getSession().setAttribute("news", list);
		request.getSession().setAttribute("pos_news", pos_list);
		request.getSession().setAttribute("neg_news", neg_list);
		request.getSession().setAttribute("unitName", query);
		model.addAttribute("count", list.size());
		model.addAttribute("pos_count", pos_list.size());
		model.addAttribute("neg_count", neg_list.size());
		model.addAttribute("judge_doc_count", judge_list.size());
		model.addAttribute("unitName", query);
		return "index.jsp";
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/news_list")
	public String getNewsList(String type,Model model,HttpServletRequest request) {
		logger.info("getNewsList called");
		logger.info("type="+type);
		if(type.equals("all")) {
			List<JWNews> list = (List<JWNews>) request.getSession().getAttribute("news");
			model.addAttribute("news_list",list);
		}
		else if(type.equals("positive")) {
			List<JWNews> pos_list = (List<JWNews>) request.getSession().getAttribute("pos_news");
			model.addAttribute("news_list",pos_list);
		}
		else if(type.equals("negative")) {
			List<JWNews> neg_list = (List<JWNews>) request.getSession().getAttribute("neg_news");
			model.addAttribute("news_list",neg_list);
		}
		String unitName = (String) request.getSession().getAttribute("unitName");
		model.addAttribute("unitName",unitName);
		return "news_list.jsp";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/judge_list")
	public String getJudgeList(Model model,HttpServletRequest request) {
		logger.info("getJudgeList called");
		List<JudgeDoc> list = (List<JudgeDoc>) request.getSession().getAttribute("judge_list");
		logger.info("list isze="+list.size());
		String unitName = (String) request.getSession().getAttribute("unitName");
		model.addAttribute("unitName",unitName);
		model.addAttribute("list",list);
		return "judge_list.jsp";
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/get_doc")
	public String getDoc(String id,Model model,HttpServletRequest request) {
		logger.info("getDoc called");
		logger.info("id="+id);
		List<JudgeDoc> list = (List<JudgeDoc>) request.getSession().getAttribute("judge_list");
		String unitName = (String) request.getSession().getAttribute("unitName");
		JudgeDoc temp = new JudgeDoc();
		for(JudgeDoc doc:list) {
			if(id.equals(doc.getId()+"")) {
				temp = doc;
			}
		}
		model.addAttribute("doc",temp);
		model.addAttribute("unitName",unitName);
		return "judge.jsp";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/get_News")
	public String getNews(String id,Model model,HttpServletRequest request) {
		logger.info("getNews called");
		logger.info("id="+id);
		List<JWNews> list = (List<JWNews>) request.getSession().getAttribute("news");
		String unitName = (String) request.getSession().getAttribute("unitName");
		JWNews temp = new JWNews();
		for(JWNews news:list) {
			if(id.equals(news.getId()+"")) {
				temp = news;
			}
		}
		model.addAttribute("news",temp);
		model.addAttribute("unitName",unitName);
		return "news.jsp";
	}

}
