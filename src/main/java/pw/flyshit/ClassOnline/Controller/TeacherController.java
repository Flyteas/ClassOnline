package pw.flyshit.ClassOnline.Controller;

import pw.flyshit.ClassOnline.Domain.Course;
import pw.flyshit.ClassOnline.Domain.CourseClass;
import pw.flyshit.ClassOnline.Domain.LessonSession;
import pw.flyshit.ClassOnline.Domain.Question;
import pw.flyshit.ClassOnline.Domain.StuAnswer;
import pw.flyshit.ClassOnline.Domain.StuSignIn;
import pw.flyshit.ClassOnline.Domain.Student;
import pw.flyshit.ClassOnline.Domain.Teacher;
import pw.flyshit.ClassOnline.Service.TeacherService;
import pw.flyshit.ClassOnline.Util.DateTimeConverter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
@Controller
public class TeacherController
{

	@Autowired
	private TeacherService teacherService;
	
	private DateTimeConverter dateTimeConverter = new DateTimeConverter();
	
	boolean loginCheck(HttpSession session) //检查是否处于登陆状态
	{
		Object teacher;
		teacher = session.getAttribute("user");
		if(teacher == null)
		{
			return false;
		}
		return true;
	}
	
	@RequestMapping(value = { "/Index.do" }, method = RequestMethod.GET)
	public ModelAndView index(HttpSession session) throws Exception //首页转跳
	{
		Teacher user;
		String lastLoginDateStr;
		ModelAndView mv;
		mv = new ModelAndView();
		user = (Teacher)session.getAttribute("user");
		if(user != null) //已登陆
		{
			if(user.getTechLastLoginTime() >0 ) //等于0则是第一次登陆
			{
				lastLoginDateStr = dateTimeConverter.dateTimeLongToStr(user.getTechLastLoginTime(), "yyyy-MM-dd HH:mm:ss");
			}
			else
			{
				lastLoginDateStr = "";
			}
			mv.addObject("lastLoginDateStr",lastLoginDateStr);
			mv.setViewName("Home");
		}
		else
		{
			mv.setViewName("Login");
		}
		return mv;
	}
	
	@RequestMapping(value = { "/404" }, method = RequestMethod.GET) //404
	public ModelAndView redirect404(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("404");
		return mv;
	}
	
	@RequestMapping(value = { "/Login.do" }, method = RequestMethod.POST) //用户登陆
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
	{
		request.setCharacterEncoding("UTF-8");
		Teacher user;
		String techId;
		String techPassword;
		int loginError;
		ModelAndView mv;
		mv = new ModelAndView();
		if(loginCheck(session)) //已登陆
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		techId = request.getParameter("username");
		techPassword = request.getParameter("password");
		user = teacherService.login(techId, techPassword, request.getRemoteAddr());
		if(user == null) //登陆失败
		{
			loginError = 1;
			mv.addObject("loginError",loginError);
			mv.setViewName("Login");
		}
		else
		{
			session.setAttribute("user",user);
			mv.setViewName("redirect:index.jsp");
		}
		return mv;
	}
	
	@RequestMapping(value = { "/Logout.do" }, method = RequestMethod.GET) //用户注销
	public void logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
	{
		request.setCharacterEncoding("UTF-8");
		session.removeAttribute("user");
		response.sendRedirect("index.jsp");
	}
	
	@RequestMapping(value = { "/SessionAdd.do" }, method = RequestMethod.GET) //添加一个会话
	public ModelAndView addSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
	{
		request.setCharacterEncoding("UTF-8");
		String courseClassId;
		ModelAndView mv = new ModelAndView();
		courseClassId = request.getParameter("courseClassId");
		mv.addObject("courseClassId",courseClassId);
		mv.setViewName("AddSession");
		return mv;
	}
	
	@RequestMapping(value = { "/SessionStart.do" }, method = RequestMethod.POST) //开启一个会话
	public ModelAndView startSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
	{
		request.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		String courseClassId;
		String sessionName;
		int sessionType; //会话模式,0为注册，1为签到，2为答题
		long beginTime;
		long endTime;
		
		Question question = null;
		List<String> errMsgs;
		LessonSession currentSession = null;
		LessonSession newSession = null;
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session)) //没有登陆
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		errMsgs = new ArrayList<String>();
		courseClassId = request.getParameter("courseClassId");
		sessionName = request.getParameter("sessionName");
		sessionType = Integer.valueOf(request.getParameter("sessionType"));
		beginTime = dateTimeConverter.dateTimeStrToLong(request.getParameter("beginTime"),"yyyy-MM-dd HH:mm:ss");
		endTime = dateTimeConverter.dateTimeStrToLong(request.getParameter("endTime"),"yyyy-MM-dd HH:mm:ss");
		
		currentSession = teacherService.getCurrentSession(courseClassId); //获取当前该教学班是正在进行的会话
		if(currentSession != null) //存在正在进行的会话
		{
			errMsgs.add("该教学班存在正在进行的会话!");
			mv.setViewName("redirect:SessionList.do?courseClassId="+courseClassId);
			return mv;
		}
		if(sessionType == 2) //设置问题并验证问题正确性
		{
			question = new Question();
			question.setQuestionTitle(request.getParameter("questionTitle"));
			question.setQuestionType(Integer.valueOf(request.getParameter("questionType")));
			question.setCreateTime(System.currentTimeMillis());
			question.setQuestionId(String.valueOf(System.currentTimeMillis()));
			if(question.getQuestionType() == 0) //单选题
			{
				int rightAns = 0;
				question.setOption1(request.getParameter("optionA"));
				question.setOption2(request.getParameter("optionB"));
				question.setOption3(request.getParameter("optionC"));
				question.setOption4(request.getParameter("optionD"));
				question.setOption5(request.getParameter("optionE"));
				question.setOption6(request.getParameter("optionF"));
				if(request.getParameter("rightSingleAns") != null)
				{
					rightAns = Integer.valueOf(request.getParameter("rightSingleAns"));
				}
				switch(rightAns)
				{
				case 1:
					question.setRightAnswer("A");
					break;
				case 2:
					question.setRightAnswer("B");
					break;
				case 3:
					question.setRightAnswer("C");
					break;
				case 4:
					question.setRightAnswer("D");
					break;
				case 5:
					question.setRightAnswer("E");
					break;
				case 6:
					question.setRightAnswer("F");
					break;
				default:
					question.setRightAnswer("");	
				}
			}
			else if(question.getQuestionType() == 1) //多选题
			{
				String rightAns = "";
				question.setOption1(request.getParameter("optionA"));
				question.setOption2(request.getParameter("optionB"));
				question.setOption3(request.getParameter("optionC"));
				question.setOption4(request.getParameter("optionD"));
				question.setOption5(request.getParameter("optionE"));
				question.setOption6(request.getParameter("optionF"));
				if(request.getParameter("optionARightFlag") != null)
				{
					rightAns += "A";
				}
				if(request.getParameter("optionBRightFlag") != null)
				{
					rightAns += "B";
				}
				if(request.getParameter("optionCRightFlag") != null)
				{
					rightAns += "C";
				}
				if(request.getParameter("optionDRightFlag") != null)
				{
					rightAns += "D";
				}
				if(request.getParameter("optionERightFlag") != null)
				{
					rightAns += "E";
				}
				if(request.getParameter("optionFRightFlag") != null)
				{
					rightAns += "F";
				}
				question.setRightAnswer(rightAns);
			}
			else if(question.getQuestionType() == 2) //问答题
			{
				question.setRightAnswer(request.getParameter("rightTextAns"));
			}
			else
			{
				errMsgs.add("问题类型错误!");
			}
		}
		if(errMsgs.isEmpty()) //无错误
		{
			if(sessionType == 2) //问题会话
			{
				newSession = teacherService.startNewSession(sessionName,sessionType, courseClassId, beginTime, endTime, question);
			}
			else
			{
				newSession = teacherService.startNewSession(sessionName,sessionType, courseClassId, beginTime, endTime);
			}
			if(newSession == null)
			{
				errMsgs.add("启动会话失败! 请检查教学班是否正确");
			}
		}
		mv.setViewName("redirect:SessionState.do?sessionId="+newSession.getLessonSessionId());
		return mv;
	}
	
	@RequestMapping(value = { "/SessionStop.do" }, method = RequestMethod.GET)
	public ModelAndView stopSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //停止一个会话
	{
		request.setCharacterEncoding("UTF-8");
		String sessionId;
		LessonSession lessonSession = null;
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session)) //登陆状态检查
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		sessionId = request.getParameter("sessionId");
		if(teacherService.stopSession(sessionId))
		{
			lessonSession = teacherService.getSession(sessionId);
		}
		if(lessonSession != null)
		{
			mv.setViewName("redirect:SessionList.do?courseClassId="+lessonSession.getCourseClass().getCourseClassId());
		}
		else
		{
			mv.setViewName("redirect:SessionManage.do");
		}
		return mv;
	}
	@RequestMapping(value = { "/SessionDel.do" }, method = RequestMethod.GET)
	public ModelAndView delSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //删除一个会话
	{
		request.setCharacterEncoding("UTF-8");
		String sessionId;
		String courseClassId;
		LessonSession lessonSession;
		ModelAndView mv = new ModelAndView();
		sessionId = request.getParameter("sessionId");
		lessonSession = teacherService.getSession(sessionId);
		if(lessonSession == null)
		{
			mv.setViewName("redirect:SessionManage.do");
		}
		else
		{
			courseClassId = lessonSession.getCourseClass().getCourseClassId();
			teacherService.delSession(sessionId);
			mv.setViewName("redirect:SessionList.do?courseClassId="+courseClassId);
		}
		return mv;
	}
	
	@RequestMapping(value = { "/RegInfoDelete.do" }, method = RequestMethod.GET)
	public ModelAndView deleteRegInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //删除学生注册信息
	{
		request.setCharacterEncoding("UTF-8");
		String studentId;
		String sessionId;
		String courseClassId;
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session)) //登录状态检查
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		studentId = request.getParameter("studentId");
		sessionId = request.getParameter("sessionId");
		courseClassId = request.getParameter("courseClassId");
		teacherService.deleteStuRegInfo(studentId); //学生不存在
		if(sessionId != null && !sessionId.isEmpty())
		{
			mv.setViewName("redirect:SessionState.do?sessionId="+sessionId);
		}
		else if(courseClassId != null && !courseClassId.isEmpty())
		{
			mv.setViewName("redirect:ClassStuList.do?courseClassId="+courseClassId);
		}
		else
		{
			mv.setViewName("redirect:404");
		}
		return mv;
	}
	
	@RequestMapping(value = { "/ClassStuDelete.do" }, method = RequestMethod.GET)
	public ModelAndView delClassStu(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //从教学班删除学生
	{
		request.setCharacterEncoding("UTF-8");
		String studentId;
		String courseClassId;
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session)) //登录状态检查
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		studentId = request.getParameter("studentId");
		courseClassId = request.getParameter("courseClassId");
		if(studentId == null || studentId.isEmpty() || courseClassId == null || courseClassId.isEmpty())
		{
			mv.setViewName("404");
			return mv;
		}
		teacherService.delClassStu(studentId, courseClassId);
		mv.setViewName("redirect:ClassStuList.do?courseClassId="+courseClassId);
		return mv;
	}
	
	@RequestMapping(value = { "/SessionManage.do" }, method = RequestMethod.GET)
	public ModelAndView listClass(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //列出当前教师的教学班
	{
		request.setCharacterEncoding("UTF-8");
		Teacher teacher;
		List<CourseClass> courseClasses;
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		teacher = (Teacher)session.getAttribute("user");
		courseClasses = teacherService.getCourseClassByTech(teacher.getTechId());
		if(courseClasses == null) //教师不存在
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		mv.addObject("courseClasses", courseClasses);
		mv.setViewName("ManageSessionSelect");
		return mv;
	}
	
	@RequestMapping(value = { "/SessionList.do" }, method = RequestMethod.GET)
	public ModelAndView listSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //获取当前教学班正在进行的会话
	{
		request.setCharacterEncoding("UTF-8");
		String courseClassId;
		List<LessonSession> lessonSessions;
		LessonSession currentSession;
		CourseClass courseClass;
		String errMsg;
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		courseClassId = request.getParameter("courseClassId");
		courseClass = teacherService.getCourseClassById(courseClassId);
		if(courseClass == null) //教学班不存在
		{
			errMsg = "教学班不存在!";
			mv.addObject("errMsg",errMsg);
			mv.setViewName("redirect:SessionManage.do");
			return mv;
		}
		lessonSessions = teacherService.getClassSession(courseClassId);
		currentSession = teacherService.getCurrentSession(courseClassId);
		mv.addObject("courseClass",courseClass);
		mv.addObject("lessonSessions",lessonSessions);
		if(currentSession != null)
		{
			mv.addObject("currentSession",currentSession);
		}
		mv.setViewName("ManageSession");
		return mv;
	}
	@RequestMapping(value = { "/SessionState.do" }, method = RequestMethod.GET)
	public ModelAndView getSessionState(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //获取会话状态
	{
		request.setCharacterEncoding("UTF-8");
		String sessionId;
		String courseClassId;
		LessonSession lessonSession;
		String BeginTimeStr;
		String EndTimeStr;
		int sessionState; //会话状态，0为正在进行，1为已结束，2为未开始
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		sessionId = request.getParameter("sessionId");
		lessonSession = teacherService.getSession(sessionId);
		if(lessonSession == null)
		{
			mv.setViewName("redirect:SessionManage.do");
			return mv;
		}
		if(lessonSession.getBeginTime() <= System.currentTimeMillis() && lessonSession.getEndTime() >= System.currentTimeMillis()) //正在进行
		{
			sessionState = 0;
		}
		else if(lessonSession.getEndTime() < System.currentTimeMillis()) //已结束
		{
			sessionState = 1;
		}
		else if(lessonSession.getBeginTime() > System.currentTimeMillis()) //未开始
		{
			sessionState = 2;
		}
		else
		{
			sessionState = -1;
		}
		BeginTimeStr = dateTimeConverter.dateTimeLongToStr(lessonSession.getBeginTime(),"yyyy-MM-dd HH:mm:ss");
		EndTimeStr = dateTimeConverter.dateTimeLongToStr(lessonSession.getEndTime(),"yyyy-MM-dd HH:mm:ss");
		mv.addObject("BeginTimeStr",BeginTimeStr);
		mv.addObject("EndTimeStr",EndTimeStr);
		mv.addObject("sessionState",sessionState);
		mv.addObject("lessonSession",lessonSession);
		courseClassId = lessonSession.getCourseClass().getCourseClassId();
		mv.addObject("courseClassId",courseClassId);
		if(lessonSession.getSessionType() == 0) //注册模式
		{
			List<Student> regStus;
			List<Student> unregStus;
			float regRates; //注册率
			regStus = teacherService.getRegStusByCourseClass(courseClassId);
			unregStus = teacherService.getUnregStusByCourseClass(courseClassId);
			if(regStus.size() + unregStus.size() == 0)
			{
				regRates = 0;
			}
			else
			{
				regRates = (float)regStus.size()/(regStus.size() + unregStus.size()) * 100;
			}
			mv.addObject("regStus",regStus);
			mv.addObject("unregStus",unregStus);
			mv.addObject("regRates",(int)regRates);
			mv.setViewName("RegState");
		}
		else if(lessonSession.getSessionType() == 1) //签到模式
		{
			List<StuSignIn> stuSignInRecord; //签到记录
			List<Student> unsignInStus; //未签到学生
			float signInRates; //签到率
			stuSignInRecord = teacherService.getSignInStudent(sessionId);
			unsignInStus = teacherService.getUnsignInStudent(sessionId);
			if(stuSignInRecord.size() + unsignInStus.size() == 0)
			{
				signInRates = 0;
			}
			else
			{
				signInRates = (float)stuSignInRecord.size() / (stuSignInRecord.size() + unsignInStus.size()) * 100;
			}
			mv.addObject("stuSignInRecord",stuSignInRecord);
			mv.addObject("unsignInStus",unsignInStus);
			mv.addObject("signInRates",(int)signInRates);
			mv.setViewName("SignInState");
		}
		else if(lessonSession.getSessionType() == 2) //答题模式
		{
			Question question;
			List<StuAnswer> stuAnswers;
			List<Student> noAnsStus;
			float involveRates; //答题率
			Map<String,String> ansCountMap = new LinkedHashMap<String,String>();
			question = lessonSession.getQuestion();
			stuAnswers = teacherService.getStuAnswerByQuestionId(question.getQuestionId());
			noAnsStus = teacherService.getNoAnsStusBySessionId(sessionId);
			if(question.getQuestionType() == 0 || question.getQuestionType() == 1) //选择题
			{
				int correctCount = 0;
				float correctRates;
				for(int i=0;i<stuAnswers.size();i++)
				{
					if(stuAnswers.get(i).isAnswerCorrect())
					{
						correctCount++;
					}
				}
				correctRates = (float)correctCount/stuAnswers.size() * 100;
				mv.addObject("correctRates",(int)correctRates);
				if(question.getQuestionType() == 0) //单选
				{
					int optionACount = 0; //A选项计数
					int optionBCount = 0; //B选项计数
					int optionCCount = 0; //C选项计数
					int optionDCount = 0; //D选项计数
					int optionECount = 0; //E选项计数
					int optionFCount = 0; //F选项计数
					for(int i=0;i<stuAnswers.size();i++)
					{
						String stuAns = stuAnswers.get(i).getAnswer();
						stuAns = stuAns.replace('1', 'A');
						stuAns = stuAns.replace('2', 'B');
						stuAns = stuAns.replace('3', 'C');
						stuAns = stuAns.replace('4', 'D');
						stuAns = stuAns.replace('5', 'E');
						stuAns = stuAns.replace('6', 'F');
						stuAns = stuAns.toUpperCase();
						if(stuAns.equals("A"))
						{
							optionACount++;
						}
						else if(stuAns.equals("B"))
						{
							optionBCount++;
						}
						else if(stuAns.equals("C"))
						{
							optionCCount++;
						}
						else if(stuAns.equals("D"))
						{
							optionDCount++;
						}
						else if(stuAns.equals("E"))
						{
							optionECount++;
						}
						else if(stuAns.equals("F"))
						{
							optionFCount++;
						}
					}
					ansCountMap.put("A", String.valueOf(optionACount));
					ansCountMap.put("B", String.valueOf(optionBCount));
					ansCountMap.put("C", String.valueOf(optionCCount));
					ansCountMap.put("D", String.valueOf(optionDCount));
					ansCountMap.put("E", String.valueOf(optionECount));
					ansCountMap.put("F", String.valueOf(optionFCount));
				}
			}
			involveRates = (float)stuAnswers.size()/(stuAnswers.size()+noAnsStus.size()) * 100;
			mv.addObject("involveRates",(int)involveRates);
			mv.addObject("noAnsStus",noAnsStus);
			mv.addObject("stuAnswers",stuAnswers);
			mv.addObject("ansCountMap",ansCountMap);
			mv.setViewName("AnswerState");
		}
		else
		{
			mv.setViewName("redirect:SessionManage.do");
			return mv;
		}
		return mv;
	}
	
	@RequestMapping(value = { "/UserInfo.do" }, method = RequestMethod.POST)
	public ModelAndView ModifyUserInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //修改用户信息
	{
		request.setCharacterEncoding("UTF-8");
		Teacher user;
		String msg;
		String techPhoneNum;
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(request.getParameter("techPhoneNum") != null)
		{
			techPhoneNum = (String)request.getParameter("techPhoneNum");
			user.setTechPhoneNum(techPhoneNum);
			if(teacherService.modifyInfo(user)) 
			{
				msg = "0"; //修改成功
				session.setAttribute("user", user);
			}
			else
			{
				msg = "1"; //修改失败
			}
			mv.addObject("msg",msg);
		}
		mv.setViewName("UserInfo");
		return mv;
	}
	
	@RequestMapping(value = { "/UserInfo.do" }, method = RequestMethod.GET)
	public ModelAndView getUserInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //显示用户信息
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		mv.setViewName("UserInfo");
		return mv;
	}
	
	@RequestMapping(value = { "/PwdModify.do" }, method = RequestMethod.GET)
	public ModelAndView redirectUserPwd(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //修改密码页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv;
		mv = new ModelAndView();
		mv.setViewName("PwdModify");
		return mv;
	}
	
	@RequestMapping(value = { "/PwdModify.do" }, method = RequestMethod.POST)
	public ModelAndView modifyUserPwd(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //修改密码页面
	{
		request.setCharacterEncoding("UTF-8");
		Teacher user;
		String oldPwd;
		String newPwd;
		String msg;
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(request.getParameter("oldPwd") != null)
		{
			oldPwd = (String)request.getParameter("oldPwd");
		}
		else
		{
			msg = "1"; //原密码为空，也就是密码错误
			mv.addObject("msg",msg);
			mv.setViewName("PwdModify");
			return mv;
		}
		if(request.getParameter("newPwd") != null)
		{
			newPwd = (String)request.getParameter("newPwd"); //新密码为空
		}
		else
		{
			msg = "2";
			mv.addObject("msg",msg);
			mv.setViewName("PwdModify");
			return mv;
		}
		if(user.getTechPassword().equals(oldPwd)) //原密码正确
		{
			user.setTechPassword(newPwd);
			if(teacherService.modifyInfo(user)) //修改成功
			{
				msg = "0"; //修改成功
				session.setAttribute("user", user);
			}
			else //教师不存在
			{
				msg = "3"; //账号不存在
			}
		}
		else //原密码错误
		{
			msg = "1";
		}
		mv.addObject("msg",msg);
		mv.setViewName("PwdModify");
		return mv;
	}
	
	@RequestMapping(value = { "/ClassManage.do" }, method = RequestMethod.GET)
	public ModelAndView manageClass(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理教学班
	{
		request.setCharacterEncoding("UTF-8");
		Teacher teacher;
		List<CourseClass> courseClasses;
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		teacher = (Teacher)session.getAttribute("user");
		if(teacher.getTechRole() == 0) //管理员权限
		{
			mv.setViewName("redirect:404");
		}
		else if(teacher.getTechRole() == 1) //教师权限
		{
			courseClasses = teacherService.getCourseClassByTech(teacher.getTechId());
			if(courseClasses == null) //教师不存在
			{
				mv.setViewName("redirect:index.jsp");
				return mv;
			}
			mv.addObject("courseClasses", courseClasses);
			mv.setViewName("ManageClassSelect");
		}
		else
		{
			mv.setViewName("redirect:index.jsp");
		}
		return mv;
	}
	
	@RequestMapping(value = { "/ClassStuList.do" }, method = RequestMethod.GET)
	public ModelAndView listClassStu(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //列出教学班学生
	{
		request.setCharacterEncoding("UTF-8");
		String courseClassId;
		CourseClass courseClass;
		List<Student> stus;
		String errMsg;
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		errMsg = request.getParameter("errMsg");
		courseClassId = request.getParameter("courseClassId");
		courseClass = teacherService.getCourseClassById(courseClassId);
		if(courseClass == null) //教学班不存在
		{
			mv.setViewName("redirect:ClassManage.do");
			return mv;
		}
		stus = teacherService.getCourseClassStus(courseClassId);
		if(stus == null) //教学班不存在
		{
			mv.setViewName("redirect:ClassManage.do");
			return mv;
		}
		mv.addObject("errMsg",errMsg);
		mv.addObject("stus",stus);
		mv.addObject("courseClass",courseClass);
		mv.setViewName("ManageClass");
		return mv;
	}
	@RequestMapping(value = { "/ClassStuAdd.do" }, method = RequestMethod.POST)
	public ModelAndView addClassStu(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //添加教学班学生
	{
		request.setCharacterEncoding("UTF-8");
		String stuId;
		String courClassId;
		CourseClass courseClass;
		Student stu;
		String errMsg;
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		courClassId = request.getParameter("courseClassId");
		stuId= request.getParameter("stuId");
		courseClass = teacherService.getCourseClassById(courClassId);
		stu = teacherService.getStuById(stuId);
		if(courseClass == null)
		{
			errMsg = "3"; //无此课程
			mv.addObject("errMsg",errMsg);
			mv.setViewName("redirect:ClassStuList.do?courseClassId="+courClassId);
			return mv;
		}
		if(stu == null)
		{
			errMsg = "2"; //无此学生
			mv.addObject("errMsg",errMsg);
			mv.setViewName("redirect:ClassStuList.do?courseClassId="+courClassId);
			return mv;
		}
		if(teacherService.addClassStu(stuId, courClassId))
		{
			errMsg = "0"; //添加成功
		}
		else
		{
			errMsg = "1"; //学生已存在
		}
		mv.addObject("errMsg",errMsg);
		mv.setViewName("redirect:ClassStuList.do?courseClassId="+courClassId);
		return mv;
	}
	
	@RequestMapping(value = { "/TeacherManage.do" }, method = RequestMethod.GET)
	public ModelAndView manageTeacher(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理员管理教师 页面
	{
		request.setCharacterEncoding("UTF-8");
		Teacher user;
		String msg;
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(user.getTechRole() == 1) //非管理员权限
		{
			mv.setViewName("redirect:404");
			return mv;
		}
		msg = request.getParameter("msg");
		if(msg != null && !msg.isEmpty())
		{
			mv.addObject("msg",msg);
		}
		mv.setViewName("ManageTeacher");
		return mv;
	}
	
	@RequestMapping(value = { "/TeacherManage.do" }, method = RequestMethod.POST)
	public ModelAndView searchTeacher(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理员管理教师
	{
		request.setCharacterEncoding("UTF-8");
		Teacher user;
		List<Teacher> teachers;
		String keyword;
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(user.getTechRole() == 1) //非管理员权限
		{
			mv.setViewName("redirect:404");
			return mv;
		}
		keyword = (String) request.getParameter("techIdOrName");
		if(keyword != null)
		{
			teachers = teacherService.searchTeacher(keyword);
			mv.addObject("teachers",teachers);
		}
		mv.setViewName("ManageTeacher");
		return mv;
	}
	
	@RequestMapping(value = { "/TeacherModify.do" }, method = RequestMethod.GET)
	public ModelAndView redirectModifyTeacher(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理员修改教师资料 页面
	{
		request.setCharacterEncoding("UTF-8");
		Teacher user;
		Teacher modifyUser;
		String techId;
		String techLastLoginTime;
		String msg;
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(user.getTechRole() == 1) //非管理员权限
		{
			mv.setViewName("redirect:404");
			return mv;
		}
		techId = request.getParameter("techId");
		if(techId == null || techId.isEmpty())
		{
			mv.setViewName("redirect:TeacherManage.do");
			return mv;
		}
		modifyUser = teacherService.findTeacher(techId);
		if(modifyUser == null)
		{
			mv.setViewName("redirect:TeacherManage.do");
			return mv;
		}
		msg = request.getParameter("msg");
		if(msg != null && !msg.isEmpty())
		{
			mv.addObject("msg",msg);
		}
		if(modifyUser.getTechLastLoginTime() >0 )
		{
			techLastLoginTime = dateTimeConverter.dateTimeLongToStr(modifyUser.getTechLastLoginTime(), "yyyy-MM-dd HH:mm:ss");
		}
		else
		{
			techLastLoginTime = "";
		}
		mv.addObject("modifyUser",modifyUser);
		mv.addObject("techLastLoginTime",techLastLoginTime);
		mv.setViewName("TeacherModify");
		return mv;
	}
	
	@RequestMapping(value = { "/TeacherModify.do" }, method = RequestMethod.POST)
	public ModelAndView modifyTeacher(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理员修改教师资料 页面
	{
		request.setCharacterEncoding("UTF-8");
		Teacher user;
		Teacher modifyUser;
		String msg;
		String techId = request.getParameter("techId");
		String techPassword = request.getParameter("techPassword");
		String techRole = request.getParameter("techRole");
		String techRealName = request.getParameter("techRealName");
		String techSex = request.getParameter("techSex");
		String techPhoneNum = request.getParameter("techPhoneNum");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(user.getTechRole() == 1) //非管理员权限
		{
			mv.setViewName("redirect:404.do");
			return mv;
		}
		if(techId == null || techId.isEmpty())
		{
			mv.setViewName("redirect:TeacherManage.do");
			return mv;
		}
		modifyUser = teacherService.findTeacher(techId);
		if(modifyUser == null)
		{
			mv.setViewName("redirect:TeacherManage.do");
			return mv;
		}
		if(techPassword != null && !techPassword.isEmpty())
		{
			modifyUser.setTechPassword(techPassword);
		}
		if(techRole != null && (techRole.equals("0") || techRole.equals("1")))
		{
			modifyUser.setTechRole(Integer.valueOf(techRole));
		}
		if(techRealName != null)
		{
			modifyUser.setTechRealName(techRealName);
		}
		if(techSex != null && (techSex.equals("0")||techSex.equals("1")))
		{
			modifyUser.setTechSex(techSex);
		}
		if(techPhoneNum != null)
		{
			modifyUser.setTechPhoneNum(techPhoneNum);
		}
		if(teacherService.modifyInfo(modifyUser))
		{
			msg = "0";
		}
		else
		{
			msg = "1";
		}
		String techLastLoginTime;
		if(modifyUser.getTechLastLoginTime() >0 )
		{
			techLastLoginTime = dateTimeConverter.dateTimeLongToStr(modifyUser.getTechLastLoginTime(), "yyyy-MM-dd HH:mm:ss");
		}
		else
		{
			techLastLoginTime = "";
		}
		mv.addObject("modifyUser",modifyUser);
		mv.addObject("techLastLoginTime",techLastLoginTime);
		mv.addObject("msg",msg);
		mv.setViewName("TeacherModify");
		return mv;
	}
	
	@RequestMapping(value = { "/TeacherAdd.do" }, method = RequestMethod.GET)
	public ModelAndView redirectAddTeacher(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理员添加教师 页面
	{
		request.setCharacterEncoding("UTF-8");
		Teacher user;
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(user.getTechRole() == 1) //非管理员权限
		{
			mv.setViewName("redirect:404.do");
			return mv;
		}
		mv.setViewName("TeacherAdd");
		return mv;
	}
	
	@RequestMapping(value = { "/TeacherAdd.do" }, method = RequestMethod.POST)
	public ModelAndView addTeacher(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理员添加教师
	{
		request.setCharacterEncoding("UTF-8");
		Teacher user;
		Teacher newUser;
		String msg;
		String techId = request.getParameter("techId");
		String techPassword = request.getParameter("techPassword");
		String techRole = request.getParameter("techRole");
		String techRealName = request.getParameter("techRealName");
		String techSex = request.getParameter("techSex");
		String techPhoneNum = request.getParameter("techPhoneNum");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(user.getTechRole() == 1) //非管理员权限
		{
			mv.setViewName("redirect:404");
			return mv;
		}
		if(techId == null || techId.isEmpty())
		{
			msg = "1"; //ID不能为空
			mv.addObject("msg",msg);
			mv.setViewName("TeacherAdd");
			return mv;
		}
		if(teacherService.findTeacher(techId) != null) //ID已存在
		{
			msg = "2"; //ID已存在
			mv.addObject("msg",msg);
			mv.setViewName("TeacherAdd");
			return mv;
		}
		if(techPassword == null || techPassword.isEmpty())
		{
			msg = "3"; //资料不全，添加失败
			mv.addObject("msg",msg);
			mv.setViewName("TeacherAdd");
			return mv;
		}
		if(techRole == null || !(techRole.equals("0") || techRole.equals("1")))
		{
			msg = "3"; //资料不全，添加失败
			mv.addObject("msg",msg);
			mv.setViewName("TeacherAdd");
			return mv;
		}
		if(techSex == null || !(techSex.equals("0") || techSex.equals("1")))
		{
			msg = "3"; //资料不全，添加失败
			mv.addObject("msg",msg);
			mv.setViewName("TeacherAdd");
			return mv;
		}
		newUser = new Teacher();
		newUser.setTechId(techId);
		newUser.setTechLastLoginTime(0);
		newUser.setTechLastLoginIP("");
		newUser.setTechPassword(techPassword);
		newUser.setTechRole(Integer.valueOf(techRole));
		newUser.setTechSex(techSex);
		if(techRealName == null)
		{
			newUser.setTechRealName("");
		}
		else
		{
			newUser.setTechRealName(techRealName);
		}
		if(techPhoneNum == null)
		{
			newUser.setTechPhoneNum("");
		}
		else
		{
			newUser.setTechPhoneNum(techPhoneNum);
		}
		if(teacherService.addTeacher(newUser))
		{
			msg = "4"; //添加成功
			mv.addObject("techId",newUser.getTechId());
			mv.addObject("msg",msg);
			mv.setViewName("redirect:TeacherModify.do");
		}
		else
		{
			msg = "3";
			mv.addObject("newUser",newUser);
			mv.addObject("msg",msg);
			mv.setViewName("TeacherAdd");
		}
		return mv;
	}
	
	@RequestMapping(value = { "/TeacherDel.do" }, method = RequestMethod.GET)
	public ModelAndView delTeacher(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理员删除教师
	{
		request.setCharacterEncoding("UTF-8");
		Teacher user;
		Teacher delUser;
		String techId;
		String msg;
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(user.getTechRole() == 1) //非管理员权限
		{
			mv.setViewName("redirect:404");
			return mv;
		}
		techId = request.getParameter("techId");
		if(techId == null || techId.isEmpty() || teacherService.findTeacher(techId) == null)
		{
			msg = "1";
			mv.addObject("msg");
			mv.setViewName("redirect:TeacherManage.do");
			return mv;
		}
		delUser = teacherService.findTeacher(techId);
		if(user.getTechId().equals(delUser.getTechId())) //自己删除自己
		{
			if(teacherService.delTeacher(delUser.getTechId()))
			{
				mv.setViewName("redirect:Logout.do");
			}
			else
			{
				msg = "1";
				mv.addObject("msg",msg);
				mv.setViewName("redirect:TeacherManage.do");
			}
		}
		else
		{
			if(teacherService.delTeacher(delUser.getTechId()))
			{
				msg = "0";
			}
			else
			{
				msg = "1";
			}
			mv.addObject("msg",msg);
			mv.setViewName("redirect:TeacherManage.do");
		}
		return mv;
	}
	
	@RequestMapping(value = { "/StuBatchAdd.do" }, method = RequestMethod.GET)
	public ModelAndView redirectAddBatchStu(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("StuBatchAdd");
		return mv;
	}
	
	@RequestMapping(value = { "/StuBatchAdd.do" }, method = RequestMethod.POST)
	public ModelAndView addBatchStu(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
	{
		request.setCharacterEncoding("UTF-8");
		String errMsg;
		ModelAndView mv = new ModelAndView();
		errMsg = "0";
		mv.addObject("errMsg",errMsg);
		mv.setViewName("StuBatchAdd");
		return mv;
	}
	
	@RequestMapping(value = { "/StudentManage.do" }, method = RequestMethod.GET)
	public ModelAndView redirectStuManage(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理员学生管理 页面
	{
		request.setCharacterEncoding("UTF-8");
		Teacher user;
		String msg;
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(user.getTechRole() == 1) //非管理员权限
		{
			mv.setViewName("redirect:404");
			return mv;
		}
		msg = request.getParameter("msg");
		if(msg != null && !msg.isEmpty())
		{
			mv.addObject("msg",msg);
		}
		mv.setViewName("ManageStu");
		return mv;
	}
	
	@RequestMapping(value = { "/StudentManage.do" }, method = RequestMethod.POST)
	public ModelAndView stuManage(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理员学生管理
	{
		request.setCharacterEncoding("UTF-8");
		Teacher user;
		String idOrName;
		List<Student> stus;
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(user.getTechRole() == 1) //非管理员权限
		{
			mv.setViewName("redirect:404");
			return mv;
		}
		idOrName = request.getParameter("idOrName");
		if(idOrName == null)
		{
			mv.setViewName("redirect:StudentManage.do");
			return mv;
		}
		stus = teacherService.searchStu(idOrName);
		mv.addObject("stus",stus);
		mv.setViewName("ManageStu");
		return mv;
	}
	
	@RequestMapping(value = { "/StudentDel.do" }, method = RequestMethod.GET)
	public ModelAndView delStudent(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
	{
		request.setCharacterEncoding("UTF-8");
		Teacher user;
		String stuId;
		String msg;
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(user.getTechRole() == 1) //非管理员权限
		{
			mv.setViewName("redirect:404");
			return mv;
		}
		stuId = request.getParameter("stuId");
		if(stuId == null || stuId.isEmpty())
		{
			msg = "1";
		}
		if(teacherService.delStudent(stuId))
		{
			msg = "0";
		}
		else
		{
			msg = "1";
		}
		mv.addObject("msg",msg);
		mv.setViewName("redirect:StudentManage.do");
		return mv;
	}
	
	@RequestMapping(value = { "/RegInfoDeleteAdmin.do" }, method = RequestMethod.GET)
	public ModelAndView delRegInfoAdmin(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //删除学生注册信息
	{
		request.setCharacterEncoding("UTF-8");
		Teacher user;
		String stuId;
		String msg;
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(user.getTechRole() == 1) //非管理员权限
		{
			mv.setViewName("redirect:404");
			return mv;
		}
		stuId = request.getParameter("stuId");
		if(stuId == null || stuId.isEmpty())
		{
			msg = "3"; //删除失败
			mv.addObject("msg",msg);
			mv.setViewName("redirect:StudentManage.do");
			return mv;
		}
		if(teacherService.deleteStuRegInfo(stuId))
		{
			msg = "2"; //删除成功
		}
		else
		{
			msg = "1"; //删除失败
		}
		mv.addObject("msg",msg);
		mv.setViewName("redirect:StudentManage.do");
		return mv;
	}
	
	@RequestMapping(value = { "/StudentAdd.do" }, method = RequestMethod.GET)
	public ModelAndView redirectStuAdd(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理 添加学生页面
	{
		request.setCharacterEncoding("UTF-8");
		Teacher user;
		String msg;
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(user.getTechRole() == 1) //非管理员权限
		{
			mv.setViewName("redirect:404");
			return mv;
		}
		msg = request.getParameter("msg");
		if(msg != null && !msg.isEmpty())
		{
			mv.addObject("msg",msg);
		}
		mv.setViewName("StudentAdd");
		return mv;
	}
	
	@RequestMapping(value = { "/StudentAdd.do" }, method = RequestMethod.POST)
	public ModelAndView studentAdd(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理 添加学生
	{
		request.setCharacterEncoding("UTF-8");
		Teacher user;
		String stuId;
		String stuName;
		String stuSex;
		String stuClass;
		String msg;
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(user.getTechRole() == 1) //非管理员权限
		{
			mv.setViewName("redirect:404");
			return mv;
		}
		stuId = request.getParameter("stuId");
		stuName = request.getParameter("stuName");
		stuSex = request.getParameter("stuSex");
		stuClass = request.getParameter("stuClass");
		Student newStu = new Student();
		if(stuId == null || stuId.isEmpty())
		{
			msg = "1"; //学号为空
			mv.addObject("msg",msg);
			mv.setViewName("StudentAdd");
			return mv;
		}
		if(teacherService.getStuById(stuId) != null)
		{
			msg = "2"; //学号已存在
			mv.addObject("msg",msg);
			mv.setViewName("StudentAdd");
			return mv;
		}
		if(stuName == null || stuName.isEmpty())
		{
			msg = "3";
			mv.addObject("msg",msg);
			mv.setViewName("StudentAdd");
			return mv;
		}
		if(stuSex == null || stuSex.isEmpty() || !(stuSex.equals("0") || stuSex.equals("1")))
		{
			msg = "3";
			mv.addObject("msg",msg);
			mv.setViewName("StudentAdd");
			return mv;
		}
		newStu.setStuId(stuId);
		newStu.setStuName(stuName);
		newStu.setStuSex(stuSex);
		if(stuClass == null)
		{
			newStu.setStuClass("");
		}
		else
		{
			newStu.setStuClass(stuClass);
		}
		newStu.setStuWechatOpenId("");
		if(teacherService.addStudent(newStu))
		{
			msg = "4";
			mv.addObject("stuId",stuId);
			mv.setViewName("redirect:StudentModify.do");
		}
		else
		{
			msg = "3";
			mv.setViewName("StudentAdd");
		}
		mv.addObject("msg",msg);
		return mv;
	}
	
	@RequestMapping(value = { "/StudentModify.do" }, method = RequestMethod.GET)
	public ModelAndView redirectStuModify(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理 修改学生 页面
	{
		request.setCharacterEncoding("UTF-8");
		String stuId;
		String msg;
		Teacher user;
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(user.getTechRole() == 1) //非管理员权限
		{
			mv.setViewName("redirect:404");
			return mv;
		}
		stuId = request.getParameter("stuId");
		if(stuId == null || stuId.isEmpty())
		{
			mv.setViewName("redirect:StudentManage.do");
			return mv;
		}
		Student modifyStu = teacherService.getStuById(stuId);
		if(modifyStu == null)
		{
			mv.setViewName("redirect:StudentManage.do");
			return mv;
		}
		msg = request.getParameter("msg");
		if(msg != null && !msg.isEmpty())
		{
			mv.addObject("msg",msg);
		}
		mv.addObject("modifyStu",modifyStu);
		mv.setViewName("StudentModify");
		return mv;
	}
	
	@RequestMapping(value = { "/StudentModify.do" }, method = RequestMethod.POST)
	public ModelAndView studentModify(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理 修改学生
	{
		request.setCharacterEncoding("UTF-8");
		String stuId;
		String stuName;
		String stuSex;
		String stuClass;
		String stuWechatOpenId;
		String msg;
		Student modifyStu;
		Teacher user;
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(user.getTechRole() == 1) //非管理员权限
		{
			mv.setViewName("redirect:404");
			return mv;
		}
		stuId = request.getParameter("stuId");
		stuName = request.getParameter("stuName");
		stuSex = request.getParameter("stuSex");
		stuClass = request.getParameter("stuClass");
		stuWechatOpenId = request.getParameter("stuWechatOpenId");
		if(stuId == null || stuId.isEmpty())
		{
			mv.setViewName("redirect:StudentManage.do");
			return mv;
		}
		modifyStu = teacherService.getStuById(stuId);
		if(modifyStu == null)
		{
			mv.setViewName("redirect:StudentManage.do");
			return mv;
		}
		if(stuName != null && !stuName.isEmpty())
		{
			modifyStu.setStuName(stuName);
		}
		if(stuSex != null && !stuSex.isEmpty() && (stuSex.equals("0")||stuSex.equals("1")))
		{
			modifyStu.setStuSex(stuSex);
		}
		if(stuClass != null)
		{
			modifyStu.setStuClass(stuClass);
		}
		if(stuWechatOpenId != null)
		{
			modifyStu.setStuWechatOpenId(stuWechatOpenId);
		}
		if(teacherService.modifyStu(modifyStu))
		{
			msg = "0";
		}
		else
		{
			msg = "1";
		}
		mv.addObject("msg",msg);
		mv.addObject("modifyStu",modifyStu);
		mv.setViewName("StudentModify");
		return mv;
	}
	
	@RequestMapping(value = { "/ClassManageAdmin.do" }, method = RequestMethod.GET)
	public ModelAndView redirectClsManageAdm(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理员管理教学班 页面
	{
		request.setCharacterEncoding("UTF-8");
		Teacher user;
		String msg;
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(user.getTechRole() == 1) //非管理员权限
		{
			mv.setViewName("redirect:404");
			return mv;
		}
		msg = request.getParameter("msg");
		if(msg != null && !msg.isEmpty())
		{
			mv.addObject("msg",msg);
		}
		mv.setViewName("ManageClassAdmin");
		return mv;
	}
	
	@RequestMapping(value = { "/ClassManageAdmin.do" }, method = RequestMethod.POST)
	public ModelAndView clsManageAdm(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理员管理教学班
	{
		request.setCharacterEncoding("UTF-8");
		Teacher user;
		String clsId;
		List<CourseClass> clses;
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(user.getTechRole() == 1) //非管理员权限
		{
			mv.setViewName("redirect:404");
			return mv;
		}
		clsId = request.getParameter("clsId");
		if(clsId == null)
		{
			mv.setViewName("redirect:ClassManageAdmin.do");
			return mv;
		}
		clses = teacherService.searchClass(clsId);
		mv.addObject("clses",clses);
		mv.setViewName("ManageClassAdmin");
		return mv;
	}
	
	@RequestMapping(value = { "/ClassDel.do" }, method = RequestMethod.GET)
	public ModelAndView delClass(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理员删除教学班
	{
		request.setCharacterEncoding("UTF-8");
		String clsId;
		String msg;
		Teacher user;
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(user.getTechRole() == 1) //非管理员权限
		{
			mv.setViewName("redirect:404");
			return mv;
		}
		clsId = request.getParameter("clsId");
		if(clsId == null || clsId.isEmpty())
		{
			mv.setViewName("redirect:ClassManageAdmin.do");
			return mv;
		}
		CourseClass cls = teacherService.getCourseClassById(clsId);
		if(cls == null)
		{
			msg = "1";
			mv.addObject("msg",msg);
			mv.setViewName("ClassManageAdmin.do");
			return mv;
		}
		if(teacherService.delClass(clsId))
		{
			msg = "0";
		}
		else
		{
			msg = "1";
		}
		mv.addObject("msg",msg);
		mv.setViewName("ClassManageAdmin.do");
		return mv;
	}
	
	@RequestMapping(value = { "/ClassAdd.do" }, method = RequestMethod.GET)
	public ModelAndView redirectAddClass(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理员添加教学班 页面
	{
		request.setCharacterEncoding("UTF-8");
		Teacher user;
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(user.getTechRole() == 1) //非管理员权限
		{
			mv.setViewName("redirect:404");
			return mv;
		}
		mv.setViewName("ClassAdd");
		return mv;
	}
	
	@RequestMapping(value = { "/ClassAdd.do" }, method = RequestMethod.POST)
	public ModelAndView addClass(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理员添加教学班
	{
		request.setCharacterEncoding("UTF-8");
		String clsId;
		String teacherId;
		String courseName;
		String msg;
		Teacher user;
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(user.getTechRole() == 1) //非管理员权限
		{
			mv.setViewName("redirect:404");
			return mv;
		}
		clsId = request.getParameter("clsId");
		if(clsId == null || clsId.isEmpty())
		{
			msg = "1"; //添加失败
			mv.addObject("msg",msg);
			mv.setViewName("ClassAdd");
			return mv;
		}
		if(teacherService.getCourseClassById(clsId) != null)
		{
			msg = "2"; //教学班ID已存在
			mv.addObject("msg",msg);
			mv.setViewName("ClassAdd");
			return mv;
		}
		teacherId = request.getParameter("teacherId");
		if(teacherId == null || teacherId.isEmpty() || teacherService.findTeacher(teacherId) == null)
		{
			msg = "3"; //教师不存在
			mv.addObject("msg",msg);
			mv.setViewName("ClassAdd");
			return mv;
		}
		courseName = request.getParameter("courseName");
		if(courseName == null)
		{
			courseName = "";
		}
		Course newCourse = new Course();
		newCourse.setCourseId(String.valueOf(System.currentTimeMillis()));
		newCourse.setCourseName(courseName);
		if(!teacherService.addCourse(newCourse))
		{
			msg = "1";
			mv.addObject("msg",msg);
			mv.setViewName("ClassAdd");
			return mv;
		}
		CourseClass newCls = new CourseClass();
		newCls.setCourseClassId(clsId);
		newCls.setCourse(newCourse);
		newCls.setTeacher(teacherService.findTeacher(teacherId));
		if(teacherService.addCourseClass(newCls))
		{
			msg = "4"; //添加成功 转向修改页面
			mv.addObject("clsId",newCls.getCourseClassId());
			mv.setViewName("redirect:ClassModify.do");
		}
		else
		{
			msg = "1";
			mv.setViewName("ClassAdd");
		}
		mv.addObject("msg",msg);
		return mv;	
	}
	
	@RequestMapping(value = { "/ClassModify.do" }, method = RequestMethod.GET)
	public ModelAndView redirectClsModify(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理员修改教学班 页面
	{
		request.setCharacterEncoding("UTF-8");
		String clsId;
		String msg;
		Teacher user;
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(user.getTechRole() == 1) //非管理员权限
		{
			mv.setViewName("redirect:404");
			return mv;
		}
		clsId = request.getParameter("clsId");
		if(clsId == null || clsId.isEmpty())
		{
			mv.setViewName("redirect:ClassManageAdmin.do");
			return mv;
		}
		CourseClass modifyCls = teacherService.getCourseClassById(clsId);
		if(modifyCls == null)
		{
			mv.setViewName("redirect:ClassManageAdmin.do");
			return mv;
		}
		msg = request.getParameter("msg");
		if(msg != null && !msg.isEmpty())
		{
			mv.addObject("msg",msg);
		}
		mv.addObject("modifyCls",modifyCls);
		return mv;
	}
	
	@RequestMapping(value = { "/ClassModify.do" }, method = RequestMethod.POST)
	public ModelAndView clsModify(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理员修改教学班
	{
		request.setCharacterEncoding("UTF-8");
		String clsId;
		String courseName;
		String teacherId;
		String msg;
		Teacher user;
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(user.getTechRole() == 1) //非管理员权限
		{
			mv.setViewName("redirect:404");
			return mv;
		}
		clsId = request.getParameter("clsId");
		if(clsId == null || clsId.isEmpty())
		{
			mv.setViewName("recirect:ClassManageAdmin.do");
			return mv;
		}
		CourseClass modifyCls = teacherService.getCourseClassById(clsId);
		if(modifyCls == null)
		{
			mv.setViewName("redirecr:ClassManage.do");
			return mv;
		}
		courseName = request.getParameter("courseName");
		teacherId = request.getParameter("teacherId");
		if(teacherId != null && !teacherId.isEmpty())
		{
			Teacher newTeacher = teacherService.findTeacher(teacherId);
			if(newTeacher == null)
			{
				msg = "2"; //无此教师
				mv.addObject("modifyCls",modifyCls);
				mv.addObject("msg",msg);
				mv.setViewName("ClassModify");
				return mv;
			}
			else
			{
				modifyCls.setTeacher(newTeacher);
			}
		}
		Course oldCour = null;
		if(courseName != null && !courseName.isEmpty())
		{
			if(!modifyCls.getCourse().getCourseName().equals(courseName))
			{//修改课程名称
				oldCour = modifyCls.getCourse();
				Course newCour = new Course();
				newCour.setCourseId(String.valueOf(System.currentTimeMillis()));
				newCour.setCourseName(courseName);
				if(teacherService.addCourse(newCour))
				{
					modifyCls.setCourse(newCour);
				}
			}
		}
		if(teacherService.modifyCourseClass(modifyCls))
		{
			msg = "0";
			if(oldCour != null)
			{
				teacherService.delCourse(oldCour.getCourseId());
			}
		}
		else
		{
			msg = "1";
		}
		mv.addObject("msg",msg);
		mv.addObject("modifyCls",modifyCls);
		mv.setViewName("ClassModify");
		return mv;
	}
	
	@RequestMapping(value = { "/SessionManageAdmin.do" }, method = RequestMethod.GET)
	public ModelAndView listClassAdmin(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理员管理会话
	{
		request.setCharacterEncoding("UTF-8");
		Teacher user;
		List<CourseClass> courseClasses;
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Teacher)session.getAttribute("user");
		if(user.getTechRole() == 1) //非管理员权限
		{
			mv.setViewName("redirect:404");
			return mv;
		}
		courseClasses = teacherService.searchClass("");
		mv.addObject("courseClasses", courseClasses);
		mv.setViewName("ManageSessionSelect");
		return mv;
	}
}
