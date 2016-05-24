package pw.flyshit.ClassOnline.Controller;

/* 教师端控制器 */
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
import java.util.List;

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
	
	@RequestMapping(value = { "/Login.do" }, method = RequestMethod.POST) //用户登陆
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
	{
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
		session.removeAttribute("user");
		response.sendRedirect("index.jsp");
	}
	
	@RequestMapping(value = { "/SessionAdd.do" }, method = RequestMethod.GET) //添加一个会话
	public ModelAndView addSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
	{
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
			response.sendRedirect("index.jsp");
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
		String sessionId;
		LessonSession lessonSession = null;
		//String msg;
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session)) //登陆状态检查
		{
			response.sendRedirect("index.jsp");
			return mv;
		}
		sessionId = request.getParameter("sessionId");
		if(teacherService.stopSession(sessionId))
		{
			//msg = "停止会话成功!";
			lessonSession = teacherService.getSession(sessionId);
		}
		else
		{
			//msg = "会话不存在!";
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
	/*
	@RequestMapping(value = { "/RegStateList.do" }, method = RequestMethod.GET)
	public ModelAndView listRegState(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //查看学生注册状态
	{
		String courseClassId;
		List<Student> regStus; //已注册学生
		List<Student> unregStus; //未注册学生
		String errMsg;
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session)) //登陆状态检查
		{
			response.sendRedirect("index.jsp");
			return mv;
		}
		courseClassId = request.getParameter("courseClassId");
		regStus = teacherService.getRegStusByCourseClass(courseClassId);
		unregStus = teacherService.getUnregStusByCourseClass(courseClassId);
		if(regStus == null || unregStus == null)
		{
			errMsg = "教学班不存在!";
			mv.addObject("errMsg",errMsg);
			mv.setViewName("SessionManage");
		}
		else
		{
			request.setAttribute("regStus", regStus);
			request.setAttribute("unregStus", unregStus);
			mv.addObject("regStus",regStus);
			mv.addObject("unregStus",unregStus);
			mv.setViewName("RegStateList");
		}
		return mv;
	}*/
	
	@RequestMapping(value = { "/RegInfoDelete.do" }, method = RequestMethod.GET)
	public ModelAndView deleteRegInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //删除学生注册信息
	{
		String studentId;
		String sessionId;
		//String msg;
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session)) //登录状态检查
		{
			response.sendRedirect("index.jsp");
			return mv;
		}
		studentId = request.getParameter("studentId");
		sessionId = request.getParameter("sessionId");
		if(!teacherService.deleteStuRegInfo(studentId)) //学生不存在
		{
			//msg = "此学生不存在!";
		}
		else
		{
			//msg = "清空学生" + studentId + "注册信息成功!";
		}
		//mv.addObject("msg",msg);
		mv.setViewName("redirect:SessionState.do?sessionId="+sessionId);
		return mv;
	}
	/*
	@RequestMapping(value = { "/SignInStateList.do" }, method = RequestMethod.GET)
	public ModelAndView listSignInState(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //查看学生签到状态
	{
		String sessionId;
		List<StuSignIn> signInRecord; //已签到记录
		List<Student> unsignInStus; //未签到学生
		String errMsg;
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			response.sendRedirect("index.jsp");
			return mv;
		}
		sessionId = request.getParameter("sessionId");
		signInRecord = teacherService.getSignInStudent(sessionId);
		unsignInStus = teacherService.getUnsignInStudent(sessionId);
		if(signInRecord == null || unsignInStus == null)
		{
			errMsg = "会话不存在或非签到会话!";
			mv.addObject("errMsg", errMsg);
			mv.setViewName("SessionManage");
		}
		else
		{
			mv.addObject("signInRecord", signInRecord);
			mv.addObject("unsignInStus", unsignInStus);
			mv.setViewName("SignInStateList");
		}
		return mv;
	}
	
	@RequestMapping(value = { "/AnswerStateList.do" }, method = RequestMethod.GET)
	public ModelAndView listAnswerState(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //查看学生答题状态
	{
		String sessionId;
		List<StuAnswer> stuAnswers;
		Question question;
		String errMsg;
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			response.sendRedirect("index.jsp");
			return mv;
		}
		sessionId = request.getParameter("sessionId");
		stuAnswers = teacherService.getStuAnswerBySessionId(sessionId);
		if(stuAnswers == null)
		{
			errMsg = "会话不存在或非答题会话!";
			mv.addObject("errMsg", errMsg);
			mv.setViewName("SessionManage");
			return mv;
		}
		question = teacherService.getQuestionBySessionId(sessionId);
		if(question == null)
		{
			errMsg = "问题不存在!";
			mv.addObject("errMsg", errMsg);
			mv.setViewName("SessionManage");
			return mv;
		}
		mv.addObject("stuAnswers", stuAnswers);
		mv.addObject("question", question);
		mv.setViewName("AnwserStateList");
		return mv;
	}*/
	
	@RequestMapping(value = { "/SessionManage.do" }, method = RequestMethod.GET)
	public ModelAndView listClass(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //列出当前教师的教学班
	{
		Teacher teacher;
		List<CourseClass> courseClasses;
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			response.sendRedirect("index.jsp");
			return mv;
		}
		teacher = (Teacher)session.getAttribute("user");
		courseClasses = teacherService.getCourseClassByTech(teacher.getTechId());
		if(courseClasses == null) //教师不存在
		{
			response.sendRedirect("index.jsp");
			return mv;
		}
		mv.addObject("courseClasses", courseClasses);
		mv.setViewName("ListClass");
		return mv;
	}
	
	@RequestMapping(value = { "/SessionList.do" }, method = RequestMethod.GET)
	public ModelAndView listSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //获取当前教学班正在进行的会话
	{
		String courseClassId;
		List<LessonSession> lessonSessions;
		LessonSession currentSession;
		CourseClass courseClass;
		String errMsg;
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			response.sendRedirect("index.jsp");
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
		String sessionId;
		String courseClassId;
		LessonSession lessonSession;
		String BeginTimeStr;
		String EndTimeStr;
		int sessionState; //会话状态，0为正在进行，1为已结束，2为未开始
		ModelAndView mv;
		mv = new ModelAndView();
		sessionId = request.getParameter("sessionId");
		lessonSession = teacherService.getSession(sessionId);
		if(lessonSession == null)
		{
			response.sendRedirect("SessionManage.do");
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
			}
			involveRates = (float)stuAnswers.size()/(stuAnswers.size()+noAnsStus.size()) * 100;
			mv.addObject("involveRates",(int)involveRates);
			mv.addObject("noAnsStus",noAnsStus);
			mv.addObject("stuAnswers",stuAnswers);
			mv.setViewName("AnswerState");
		}
		else
		{
			response.sendRedirect("SessionManage.do");
			return mv;
		}
		return mv;
	}
}
