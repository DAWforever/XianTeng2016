package cn.edu.nju.iip.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.edu.nju.iip.dao.AdminUserDao;
import cn.edu.nju.iip.model.AdminUser;

@Controller
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private AdminUserDao userDao;

	/**
	 * 管理员登录
	 * 
	 * @return
	 */
	@RequestMapping(value = "/login")
	public String login(AdminUser user, HttpServletRequest request) {
		if (userDao.validate(user)) {
			logger.info("login ok");
			return "index.jsp";
		} else {
			logger.info("login error");
			return "login-1.jsp";
		}
	}

}
