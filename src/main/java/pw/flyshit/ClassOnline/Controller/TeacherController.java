package pw.flyshit.ClassOnline.Controller;

/* 教师端控制器 */
import pw.flyshit.ClassOnline.Domain.LessonSession;
import pw.flyshit.ClassOnline.Domain.Question;
import pw.flyshit.ClassOnline.Domain.StuAnswer;
import pw.flyshit.ClassOnline.Domain.StuSignIn;
import pw.flyshit.ClassOnline.Domain.Student;
import pw.flyshit.ClassOnline.Domain.Teacher;
import pw.flyshit.ClassOnline.Service.TeacherService;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
@Controller
public class TeacherController
{

	@Autowired
	TeacherService teacherService;
	
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
	
	@RequestMapping(value = { "/Login.do" }, method = RequestMethod.POST) //用户登陆
	public void Login(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
	{
		Teacher user;
		String techId;
		String techPassword;
		int loginError;
		
		if(loginCheck(session)) //已登陆
		{
			response.sendRedirect("Home.jsp");
			return;
		}
		techId = request.getParameter("username");
		techPassword = request.getParameter("password");
		user = teacherService.login(techId, techPassword);
		if(user == null) //登陆失败
		{
			loginError = 1;
			session.setAttribute("loginError", loginError);
			response.sendRedirect("Login.jsp");
		}
		else
		{
			session.removeAttribute("loginError");
			session.setAttribute("user",user);
			response.sendRedirect("Home.jsp");
		}
	}
	
	@RequestMapping(value = { "/Logout.do" }, method = RequestMethod.GET) //用户注销
	public void logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
	{
		session.removeAttribute("user");
		response.sendRedirect("Login.jsp");
	}
	
	@RequestMapping(value = { "/SessionStart.do" }, method = RequestMethod.POST) //开启一个会话
	public void startSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
	{
		int sessionType; //会话模式,0为注册，1为签到，2为答题
		String courseClassId;
		long beginTime;
		long endTime;
		Question question = null;
		List<String> errMsgs;
		LessonSession currentSession = null;
		LessonSession newSession = null;
		
		if(!loginCheck(session)) //没有登陆
		{
			response.sendRedirect("Login.jsp");
			return;
		}
		errMsgs = new ArrayList<String>();
		sessionType = Integer.valueOf(request.getParameter("sessionType"));
		courseClassId = request.getParameter("courseClassId");
		beginTime = Long.valueOf(request.getParameter("beginTime"));
		endTime = Long.valueOf(request.getParameter("endTime"));
		currentSession = teacherService.getCurrentSession(courseClassId); //获取当前该教学班是正在进行的会话
		if(currentSession != null) //存在正在进行的会话
		{
			errMsgs.add("该教学班存在正在进行的会话!");
			request.setAttribute("errMsgs", errMsgs);
			request.setAttribute("currentSession", currentSession);
			request.getRequestDispatcher("/SessionManage.jsp").forward(request, response);
			return;
		}
		if(sessionType == 2) //设置问题并验证问题正确性
		{
			question = new Question();
			question.setQuestionTitle(request.getParameter("qtitle"));
			question.setOption1(request.getParameter("qoption1"));
			question.setOption2(request.getParameter("qoption2"));
			question.setOption3(request.getParameter("qoption3"));
			question.setOption4(request.getParameter("qoption4"));
			question.setOption5(request.getParameter("qoption5"));
			question.setOption6(request.getParameter("qoption6"));
			question.setRightAnswer(request.getParameter("qrightans"));
			question.setCreateTime(System.currentTimeMillis());
			question.setQuestionId(String.valueOf(System.currentTimeMillis()));
			question.setQuestionScore(Integer.valueOf(request.getParameter("qscore")));
			if(question.getQuestionScore() < 0)
			{
				errMsgs.add("问题分数不能小于0!");
			}
			question.setQuestionType(Integer.valueOf(request.getParameter("qtype")));
			if(question.getQuestionType() != 0 || question.getQuestionType() != 1 || question.getQuestionType() != 2)
			{
				errMsgs.add("问题类型错误!");
			}
		}
		if(errMsgs.isEmpty()) //无错误
		{
			if(sessionType == 2) //问题会话
			{
				newSession = teacherService.startNewSession(sessionType, courseClassId, beginTime, endTime, question);
			}
			else
			{
				newSession = teacherService.startNewSession(sessionType, courseClassId, beginTime, endTime);
			}
			if(newSession == null)
			{
				errMsgs.add("启动会话失败! 请检查教学班是否正确");
			}
		}
		request.setAttribute("errMsgs", errMsgs);
		request.setAttribute("newSession", newSession);
		request.setAttribute("question", question);
		request.getRequestDispatcher("/SessionManage.jsp").forward(request, response);
	}
	
	@RequestMapping(value = { "/SessionStop.do" }, method = RequestMethod.GET)
	public void stopSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //停止一个会话
	{
		String sessionId;
		String msg;
		if(!loginCheck(session)) //登陆状态检查
		{
			response.sendRedirect("Login.jsp");
			return;
		}
		sessionId = request.getParameter("sessionId");
		if(teacherService.stopSession(sessionId))
		{
			msg = "停止会话成功!";
		}
		else
		{
			msg = "会话不存在!";
		}
		request.setAttribute("msg", msg);
		request.getRequestDispatcher("/SessionManage.jsp").forward(request, response);
	}
	
	@RequestMapping(value = { "/RegStateList.do" }, method = RequestMethod.GET)
	public void ListRegState(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //查看学生注册状态
	{
		String courseClassId;
		List<Student> regStus; //已注册学生
		List<Student> unregStus; //未注册学生
		String errMsg;
		if(!loginCheck(session)) //登陆状态检查
		{
			response.sendRedirect("Login.jsp");
			return;
		}
		courseClassId = request.getParameter("courseClassId");
		regStus = teacherService.getRegStusByCourseClass(courseClassId);
		unregStus = teacherService.getUnregStusByCourseClass(courseClassId);
		if(regStus == null || unregStus == null)
		{
			errMsg = "教学班不存在!";
			request.setAttribute("errMsg", errMsg);
			request.getRequestDispatcher("/SessionManage.jsp").forward(request, response);
		}
		else
		{
			request.setAttribute("regStus", regStus);
			request.setAttribute("unregStus", unregStus);
			request.getRequestDispatcher("/RegStateList.jsp").forward(request, response);
		}
	}
	
	@RequestMapping(value = { "/RegInfoDelete.do" }, method = RequestMethod.GET)
	public void deleteRegInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //删除学生注册信息
	{
		String studentId;
		String msg;
		if(!loginCheck(session)) //登录状态检查
		{
			response.sendRedirect("Login.jsp");
			return;
		}
		studentId = request.getParameter("studentId");
		if(!teacherService.deleteStuRegInfo(studentId)) //学生不存在
		{
			msg = "此学生不存在!";
		}
		else
		{
			msg = "清空学生" + studentId + "注册信息成功!";
		}
		request.setAttribute("msg", msg);
		request.getRequestDispatcher("/RegStateList.jsp").forward(request, response);
	}
	
	@RequestMapping(value = { "/SignInStateList.do" }, method = RequestMethod.GET)
	public void listSignInState(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //查看学生签到状态
	{
		String sessionId;
		List<StuSignIn> signInRecord; //已签到记录
		List<Student> unsignInStus; //未签到学生
		String errMsg;
		
		if(!loginCheck(session))
		{
			response.sendRedirect("Login.jsp");
			return;
		}
		sessionId = request.getParameter("sessionId");
		signInRecord = teacherService.getSignInStudent(sessionId);
		unsignInStus = teacherService.getUnsignInStudent(sessionId);
		if(signInRecord == null || unsignInStus == null)
		{
			errMsg = "此会话不存在或非签到会话!";
			request.setAttribute("errMsg", errMsg);
			request.getRequestDispatcher("/SessionManage.jsp").forward(request, response);
		}
		else
		{
			request.setAttribute("signInRecord", signInRecord);
			request.setAttribute("unsignInStus", unsignInStus);
			request.getRequestDispatcher("/SignInStateList.jsp").forward(request, response);
		}
	}
	
	@RequestMapping(value = { "/AnswerStateList.do" }, method = RequestMethod.GET)
	public void listAnswerState(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //查看学生答题状态
	{
		String sessionId;
		List<StuAnswer> stuAnswers;
		Question question;
		String errMsg;
		
		if(!loginCheck(session))
		{
			response.sendRedirect("Login.jsp");
			return;
		}
		sessionId = request.getParameter("sessionId");
		stuAnswers = teacherService.getStuAnswerBySessionId(sessionId);
		if(stuAnswers == null)
		{
			errMsg = "此会话不存在或非答题会话!";
			request.setAttribute("errMsg", errMsg);
			request.getRequestDispatcher("/SessionManage.jsp").forward(request, response);
		}
		question = teacherService.getQuestionBySessionId(sessionId);
		if(question == null)
		{
			errMsg = "此问题不存在!";
			request.setAttribute("errMsg", errMsg);
			request.getRequestDispatcher("/SessionManage.jsp").forward(request, response);
		}
		request.setAttribute("stuAnswers", stuAnswers);
		request.setAttribute("question", question);
		request.getRequestDispatcher("/AnwserStateList.jsp").forward(request, response);
	}
}
