package nju.iip.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import nju.iip.dao.NewsDAO;
import nju.iip.dto.JWNews;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SearchController {
	
	private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
	
	@Autowired
	private NewsDAO newsDAO;
	
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
		List<JWNews> list = newsDAO.getNews(query);
		request.getSession().setAttribute("news", list);
		request.getSession().setAttribute("unitName", query);
		model.addAttribute("count", list.size());
		model.addAttribute("unitName", query);
		return "index.jsp";
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/news_list")
	public String getNewsList(Model model,HttpServletRequest request) {
		logger.info("getNewsList called");
		List<JWNews> list = (List<JWNews>) request.getSession().getAttribute("news");
		String unitName = (String) request.getSession().getAttribute("unitName");
		model.addAttribute("news_list",list);
		model.addAttribute("unitName",unitName);
		return "news_list.jsp";
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
