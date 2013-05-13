package com.globant.controllers;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.autodesk.clientlib.AnalyticsFormatter;
import com.autodesk.clientlib.KeyPair.Key;
import com.globant.entities.Language;
import com.globant.entities.Role;
import com.globant.entities.User;
import com.globant.persistence.LanguageDAO;
import com.globant.persistence.RoleDAO;
import com.globant.persistence.UserDAO;

@Controller
public class AppController{
	private UserDAO userDAO;
	private RoleDAO roleDAO;
	private LanguageDAO languageDAO;
	static final Logger as_looger = Logger.getLogger(AppController.class);

	public UserDAO getUserDAO() {
		return userDAO;
	}
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	public RoleDAO getRoleDAO() {
		return roleDAO;
	}
	public void setRoleDAO(RoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}
	
	public LanguageDAO getLanguageDAO() {
		return languageDAO;
	}
	public void setLanguageDAO(LanguageDAO languageDAO) {
		this.languageDAO = languageDAO;
	}

	@Autowired
	public AppController(LanguageDAO langDAO, UserDAO userDAO,RoleDAO roleDAO){
		setUserDAO(userDAO);
		setRoleDAO(roleDAO);
		setLanguageDAO(langDAO);
	}
	
	@RequestMapping("/home")
	public String homeControl(Model model,HttpServletRequest request) {
////-------------THL---------------------------------------------
//		Date date=new Date();
//		SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");
//		as_looger.info(format.format(date));
//	   	
//    	AnalyticsFormatter.getInstance().put("PAGE2","HOME");
//		AnalyticsFormatter.getInstance().put(Key.API_METHOD,"POST");
//    	String output=AnalyticsFormatter.getInstance().outputEvent();
//
//    	as_looger.info(output);	
//    	as_looger.info(Thread.currentThread());
//    	
////-------------OPTION 2---------------------------------------------
		Principal userPrincipal=request.getUserPrincipal();
		Integer userId=null;
		if(userPrincipal!=null){
			userId=getUserDAO().getUserByName(userPrincipal.getName()).getIdUser();
			request.getSession().setAttribute("userId",userId);
		}
		Boolean isSupervisor = request.isUserInRole("supervisor");
		request.getSession().setAttribute("isSupervisor",isSupervisor);
		model.addAttribute("isSupervisor", isSupervisor);
		model.addAttribute("userId",userId);
		model.addAttribute("userName",userPrincipal.getName());
		return "home";
	}
	
	@RequestMapping("/profile")
	public String profileControl(Model model,HttpServletRequest request) throws Exception {
		Boolean isSupervisor = (Boolean)request.getSession().getAttribute("isSupervisor");
////-------------THL---------------------------------------------
//				Date date=new Date();
//				SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
//				as_looger.info(format.format(date));
//			   	
//		    	AnalyticsFormatter.getInstance().put("PAGE-PROF","HOME");
//				AnalyticsFormatter.getInstance().put(Key.API_METHOD,"POST");
//		    	String output=AnalyticsFormatter.getInstance().outputEvent();
//
//		    	as_looger.info(output);	
//		    	//as_looger.info(Thread.currentThread());
//		    	
////----------------------------------------------------------

		Integer userId=(Integer)request.getSession().getAttribute("userId");
		List<Role> roles=getRoleDAO().getAllRoles();
		User user=new User();
		Boolean create = false;
		if(request.getParameter("create")!=null) {
			create = true;
		}
		String pag=null;
		try{
			Integer id=Integer.parseInt(request.getParameter("id"));
			if(isSupervisor||(id.equals(userId))){
				user=getUserDAO().load(id);
				if(request.getParameter("email")!=null){
					user.setEmail(request.getParameter("email"));
					user.setExperience(request.getParameter("experience"));
					user.setEnabled(true);
					Set<Language> lang = new LinkedHashSet<Language>();
					if (request.getParameter("english") != null) {
						Language eng = getLanguageDAO().getLanguageByName("English");
						lang.add(eng);
					}
					if (request.getParameter("french") != null) {
						Language fr = getLanguageDAO().getLanguageByName("French");
						lang.add(fr);
					}
					if (request.getParameter("german") != null) {
						Language ger = getLanguageDAO().getLanguageByName("German");
						lang.add(ger);
					}
					if (request.getParameter("italian") != null) {
						Language it = getLanguageDAO().getLanguageByName("Italian");
						lang.add(it);
					}
					if (request.getParameter("portuguese") != null) {
						Language por = getLanguageDAO().getLanguageByName("Portuguese");
						lang.add(por);
					}
					if (request.getParameter("spanish") != null) {
						Language sp = getLanguageDAO().getLanguageByName("Spanish");
						lang.add(sp);
					}
					user.setLanguages(lang);
					if(isSupervisor){
						user.setName(request.getParameter("name"));
						user.setCompany(request.getParameter("company"));
						user.setJobTitle(request.getParameter("jobTitle"));
						user.setUsername(request.getParameter("username"));
						user.setPassword(request.getParameter("password"));
						user.setRole(getRoleDAO().getRoleByName(request.getParameter("role")));
						pag="redirect:/dashboard";
					}else{
						pag="redirect:/home";
					}
					getUserDAO().save(user);
				}else{
					pag="profile";
				}
			}else{
				pag="redirect:/denied";
			}
		}catch(Exception ex){
			if(isSupervisor){
				if(request.getParameter("email")!=null){
					Set<Language> lang = new LinkedHashSet<Language>();
					if (request.getParameter("english") != null) {
						Language eng = getLanguageDAO().getLanguageByName("English");
						lang.add(eng);
					}
					if (request.getParameter("french") != null) {
						Language fr = getLanguageDAO().getLanguageByName("French");
						lang.add(fr);
					}
					if (request.getParameter("german") != null) {
						Language ger = getLanguageDAO().getLanguageByName("German");
						lang.add(ger);
					}
					if (request.getParameter("italian") != null) {
						Language it = getLanguageDAO().getLanguageByName("Italian");
						lang.add(it);
					}
					if (request.getParameter("portuguese") != null) {
						Language por = getLanguageDAO().getLanguageByName("Portuguese");
						lang.add(por);
					}
					if (request.getParameter("spanish") != null) {
						Language sp = getLanguageDAO().getLanguageByName("Spanish");
						lang.add(sp);
					}
					user.setLanguages(lang);
					user.setEmail(request.getParameter("email"));
					user.setExperience(request.getParameter("experience"));
					user.setName(request.getParameter("name"));
					user.setCompany(request.getParameter("company"));
					user.setJobTitle(request.getParameter("jobTitle"));
					user.setUsername(request.getParameter("username"));
					user.setPassword(request.getParameter("password"));
					user.setRole(getRoleDAO().getRoleByName(request.getParameter("role")));
					user.setEnabled(true);
					getUserDAO().save(user);	
					pag="redirect:/dashboard";
				}else{
					pag="profile";
				}
				
			}else{
				pag="redirect:/home";
			}
		}
		model.addAttribute("create",create);
		model.addAttribute("isSupervisor", isSupervisor);
		model.addAttribute("userId",userId);
		model.addAttribute("user",user);
		model.addAttribute("roles",roles);
		return pag;
	}
	
	@RequestMapping("/")
	public String welcomePage(Model model,HttpServletRequest request) throws Exception {
	        return "login";
	}
	
	@RequestMapping("/login")
	public String loginControl(Model model,HttpServletRequest request) throws Exception {

////Leo -----THL----------------------------------------------------PUT
//		Date date=new Date();
//		SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
//		as_looger.info(format.format(date));
//    	
//		AnalyticsFormatter.getInstance().put("PAGE-LOG","login");
//		AnalyticsFormatter.getInstance().put(Key.API_METHOD,"POST");    	
//    	String output=AnalyticsFormatter.getInstance().outputEvent();
//    	
//    	as_looger.info(output);	
//    	as_looger.info(Thread.currentThread());
////--------------------------------------------------------------
		String pageForward=null;
		Boolean isSupervisor = (Boolean)request.getSession().getAttribute("isSupervisor");
		if(isSupervisor==null){
			pageForward="login";
		}else{
			pageForward="redirect:/home";
		}
		return pageForward;
	}
	
    @RequestMapping("/dashboard")
	public String dashboardControl(Model model,HttpServletRequest request) throws Exception {
    	//Leo -----THL----------------------------------------------------PUT
    			Date date=new Date();
    			SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
    			as_looger.info(format.format(date));
    	    	
    			AnalyticsFormatter.getInstance().put("PAGE-DASH","login");
    			AnalyticsFormatter.getInstance().put(Key.CONTEXT_CALL,"Searching into the dashboard");    	
    	    	String output=AnalyticsFormatter.getInstance().outputEvent();
    	    	
    	    	as_looger.info(output);	
    	    	as_looger.info(Thread.currentThread());
    	//--------------------------------------------------------------

		for (int i = 1; i <= 20; i++) {
			try {
				Integer id = Integer.parseInt(request.getParameter(String.valueOf(i)));
				getUserDAO().delete(getUserDAO().load(id));
			} catch (Exception ex) {}
		}
		String search = request.getParameter("searchValue");
		String orderIndex=request.getParameter("orderIndex");
		String order=request.getParameter("order");
		Integer pag;
		if (search == null) {
			search=(String)request.getSession().getAttribute("searchValue");
			if(search==null){
				search = "";
			}
		}
		if (orderIndex == null) {
			orderIndex=(String)request.getSession().getAttribute("orderIndex");
			if(orderIndex==null){
				orderIndex = "name";
			}
		}
		if (order == null) {
			order=(String)request.getSession().getAttribute("order");
			if(order==null){
				order = "ASC";
			}
		}
		try{
			pag=Integer.parseInt(request.getParameter("pag"));
		}catch(Exception ex){
			pag=(Integer)request.getSession().getAttribute("pag");
			if(pag==null){
				pag=0;
			}
		}
		Boolean isSupervisor = (Boolean)request.getSession().getAttribute("isSupervisor");
		Integer userId=(Integer)request.getSession().getAttribute("userId");
		request.getSession().setAttribute("searchValue",search);
		request.getSession().setAttribute("orderIndex",orderIndex);
		request.getSession().setAttribute("order",order);
		request.getSession().setAttribute("pag",pag);
		List<User> users = getUserDAO().find(search,orderIndex,order,pag);
		Long userCount = getUserDAO().count(search);
		model.addAttribute("isSupervisor", isSupervisor);
		model.addAttribute("users", users);
		model.addAttribute("userCount", userCount);
		model.addAttribute("search", search);
		model.addAttribute("previous",(pag>0)?(pag-1):0);
		model.addAttribute("next",(pag<((userCount-1)/UserDAO.RESULTS))?(pag+1):((userCount-1)/UserDAO.RESULTS));
		model.addAttribute("from",pag*UserDAO.RESULTS+1);
		model.addAttribute("to",pag*UserDAO.RESULTS+users.size());
		model.addAttribute("userId",userId);
		return"dashboard";
	}
    
	@RequestMapping("/denied")
	public String deniedController(Model model,HttpServletRequest request) throws Exception {
		Boolean isSupervisor = (Boolean)request.getSession().getAttribute("isSupervisor");
		Integer userId=(Integer)request.getSession().getAttribute("userId");
		model.addAttribute("isSupervisor", isSupervisor);
		model.addAttribute("userId",userId);
		return "denied";
	}

	

    
}
