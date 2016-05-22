package pw.flyshit.ClassOnline.Service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pw.flyshit.ClassOnline.Dao.CourseClassDao;
import pw.flyshit.ClassOnline.Dao.CourseClassMemberDao;
import pw.flyshit.ClassOnline.Dao.CourseDao;
import pw.flyshit.ClassOnline.Dao.LessonDao;
import pw.flyshit.ClassOnline.Dao.LessonSessionDao;
import pw.flyshit.ClassOnline.Dao.QuestionDao;
import pw.flyshit.ClassOnline.Dao.StuAnswerDao;
import pw.flyshit.ClassOnline.Dao.StuSignInDao;
import pw.flyshit.ClassOnline.Dao.StudentDao;
import pw.flyshit.ClassOnline.Dao.TeacherDao;
import pw.flyshit.ClassOnline.Domain.CourseClass;
import pw.flyshit.ClassOnline.Domain.CourseClassMember;
import pw.flyshit.ClassOnline.Domain.LessonSession;
import pw.flyshit.ClassOnline.Domain.Question;
import pw.flyshit.ClassOnline.Domain.StuAnswer;
import pw.flyshit.ClassOnline.Domain.StuSignIn;
import pw.flyshit.ClassOnline.Domain.Student;
import pw.flyshit.ClassOnline.Domain.Teacher;
import pw.flyshit.ClassOnline.Service.TeacherService;

@Transactional
@Service
public class TeacherServiceImpl implements TeacherService
{
	@Autowired
	CourseClassDao courseClassDao;
	@Autowired
	CourseClassMemberDao courseClassMemberDao;
	@Autowired
	CourseDao courseDao;
	@Autowired
	LessonDao lessonDao;
	@Autowired
	LessonSessionDao lessonSessionDao;
	@Autowired
	QuestionDao questionDao;
	@Autowired
	StuAnswerDao stuAnswerDao;
	@Autowired
	StudentDao studentDao;
	@Autowired
	StuSignInDao stuSignInDao;
	@Autowired
	TeacherDao teacherDao;

	@Override
	public LessonSession startNewSession(String sessionName,int sessionType, String courseClassId, long beginTime, long endTime)  //开启一个新的会话
	{
		CourseClass courseClass;
		courseClass = courseClassDao.findCourseClassById(courseClassId);
		if(courseClass == null) //无此教学班
		{
			return null;
		}
		LessonSession lessonSession = new LessonSession();
		lessonSession.setSessionName(sessionName);
		lessonSession.setLessonSessionId(String.valueOf(System.currentTimeMillis())); //会话ID设置为当前时间戳
		lessonSession.setBeginTime(beginTime);
		lessonSession.setEndTime(endTime);
		lessonSession.setSessionType(sessionType);
		lessonSession.setCourseClass(courseClass);
		if(!lessonSessionDao.addSession(lessonSession)) //启动会话失败
		{
			return null;
		}
		return lessonSession;
	}

	@Override
	public LessonSession startNewSession(String sessionName,int sessionType, String courseClassId,  long beginTime, long endTime, Question question) 
	{
		CourseClass courseClass;
		courseClass = courseClassDao.findCourseClassById(courseClassId);
		if(courseClass == null) //无此教学班
		{
			return null;
		}
		questionDao.addQuestion(question);
		LessonSession lessonSession = new LessonSession();
		lessonSession.setSessionName(sessionName);
		lessonSession.setLessonSessionId(String.valueOf(System.currentTimeMillis())); //会话ID设置为当前时间戳
		lessonSession.setBeginTime(beginTime);
		lessonSession.setEndTime(endTime);
		lessonSession.setSessionType(sessionType);
		lessonSession.setCourseClass(courseClass);
		lessonSession.setQuestion(question);
		if(!lessonSessionDao.addSession(lessonSession)) //启动会话失败
		{
			return null;
		}
		return lessonSession;
	}

	@Override
	public boolean stopSession(String lessonSessionId) //停止一个会话
	{
		LessonSession lessonSession;
		lessonSession = lessonSessionDao.findSessionById(lessonSessionId);
		if(lessonSession == null) //会话不存在
		{
			return false;
		}
		lessonSession.setEndTime(System.currentTimeMillis());
		return true;
	}

	@Override
	public List<Student> getRegisteredStudent() //获取所有已注册学生
	{
		return studentDao.getAllRegStu();
	}

	@Override
	public List<Student> getUnregisterStudent() //获取所有未注册学生
	{
		return studentDao.getAllUnregStu();
	}

	@Override
	public boolean deleteStuRegInfo(String studentId) //清除指定学生注册信息
	{
		if(studentDao.studentDeleteRegInfo(studentId) == 2) //不存在此学生
		{
			return false;
		}
		return true;
	}

	@Override
	public List<StuSignIn> getSignInStudent(String lessonSessionId) //获取某会话所有签到记录，按签到顺序升序排列
	{
		LessonSession lessonSession;
		lessonSession = lessonSessionDao.findSessionById(lessonSessionId);
		if(lessonSession == null) //不存在此会话
		{
			return null;
		}
		if(lessonSession.getSessionType() != 1) //非签到模式的会话
		{
			return null;
		}
		return stuSignInDao.findStuSignInBySession(lessonSession);
	}

	@Override
	public List<Student> getUnsignInStudent(String lessonSessionId) //获取某会话所有未签到学生
	{
		LessonSession lessonSession; //当前会话
		CourseClass courseClass; //上课班
		List<CourseClassMember> courseClassMembers;
		List<Student> courseClassStudents; //上课班所有学生
		List<StuSignIn> signInStus; //签到记录
		List<Student> unsignInStus; //未签到学生
		lessonSession = lessonSessionDao.findSessionById(lessonSessionId);
		if(lessonSession == null) //不存在此会话
		{
			return null;
		}
		if(lessonSession.getSessionType() != 1) //非签到模式的会话
		{
			return null;
		}
		courseClass = lessonSession.getCourseClass(); //获取该会话的上课班
		courseClassMembers = courseClassMemberDao.findCourseClassMemberByCourseClass(courseClass);
		courseClassStudents = new ArrayList<Student>();
		for(int i=0;i<courseClassMembers.size();i++) //获取上课班所有学生
		{
			courseClassStudents.add(courseClassMembers.get(i).getStu());
		}
		signInStus = this.getSignInStudent(lessonSessionId); //获取该会话签到记录
		unsignInStus = new ArrayList<Student>();
		for(int i=0;i<courseClassStudents.size();i++) //遍历获取所有未签到学生
		{
			boolean isSignIn = false;
			for(int j=0;j<signInStus.size();j++)
			{
				if(courseClassStudents.get(i).getStuId().equals(signInStus.get(j).getStu().getStuId()))
				{
					isSignIn = true;
					break;
				}
			}
			if(!isSignIn) //此学生没有签到
			{
				unsignInStus.add(courseClassStudents.get(i));
			}
		}
		return unsignInStus;
	}

	@Override
	public List<StuAnswer> getStuAnswerBySessionId(String lessonSessionId) //根据会话ID获取学生答题情况
	{
		LessonSession lessonSession;
		Question question;
		lessonSession = lessonSessionDao.findSessionById(lessonSessionId);
		if(lessonSession == null) //不存在此会话
		{
			return null;
		}
		if(lessonSession.getSessionType() != 2) //非答题模式的会话
		{
			return null;
		}
		question = lessonSession.getQuestion();
		if(question == null)
		{
			return null;
		}
		return stuAnswerDao.findStuAnswerByQuestion(question);
	}

	@Override
	public List<StuAnswer> getStuAnswerByQuestionId(String questionId) //根据问题ID获取学生答题情况
	{
		Question question;
		question = questionDao.findQuestionById(questionId);
		if(question == null) //此问题不存在
		{
			return null;
		}
		return stuAnswerDao.findStuAnswerByQuestion(question);
	}

	@Override
	public List<StuAnswer> getCorrectAnsBySessionId(String lessonSessionId) //根据会话ID获取正确答题记录
	{
		LessonSession lessonSession;
		Question question;
		lessonSession = lessonSessionDao.findSessionById(lessonSessionId);
		if(lessonSession == null) //不存在此会话
		{
			return null;
		}
		if(lessonSession.getSessionType() != 2) //非答题模式的会话
		{
			return null;
		}
		question = lessonSession.getQuestion();
		if(question == null)
		{
			return null;
		}
		return stuAnswerDao.findCorrectStuAnswerByQuestion(question);
	}

	@Override
	public List<StuAnswer> getCorrectAnsByQuestionId(String questionId) //问题会话ID获取正确答题记录
	{
		Question question;
		question = questionDao.findQuestionById(questionId);
		if(question == null) //此问题不存在
		{
			return null;
		}
		return stuAnswerDao.findCorrectStuAnswerByQuestion(question);
	}

	@Override
	public LessonSession getCurrentSession(String courseClassId) //获取某教学班的当前会话
	{
		List<LessonSession> currentSession = null;
		CourseClass courseClass;
		courseClass = courseClassDao.findCourseClassById(courseClassId);
		if(courseClass == null)
		{
			return null;
		}
		currentSession = lessonSessionDao.getCurrentSessionByCourClass(courseClass);
		if(currentSession.isEmpty())
		{
			return null;
		}
		return currentSession.get(0);
	}

	@Override
	public List<Student> getRegStusByCourseClass(String courseClassId) //查询某教学班所有已注册学生
	{
		CourseClass courseClass;
		List<CourseClassMember> classMembs;
		List<Student> regStus;
		regStus = new ArrayList<Student>();
		courseClass = courseClassDao.findCourseClassById(courseClassId);
		if(courseClass == null)
		{
			return null;
		}
		classMembs = courseClassMemberDao.findCourseClassMemberByCourseClass(courseClass);
		for(int i=0;i<classMembs.size();i++)
		{
			if(classMembs.get(i).getStu().getStuWechatOpenId() != null && !classMembs.get(i).getStu().getStuWechatOpenId().isEmpty()) //已经注册
			{
				regStus.add(classMembs.get(i).getStu()); //添加到结果集
			}
		}
		return regStus;
	}

	@Override
	public List<Student> getUnregStusByCourseClass(String courseClassId) //查询某教学班所有已注册学生
	{
		CourseClass courseClass;
		List<CourseClassMember> classMembs;
		List<Student> unregStus;
		unregStus = new ArrayList<Student>();
		courseClass = courseClassDao.findCourseClassById(courseClassId);
		if(courseClass == null)
		{
			return null;
		}
		classMembs = courseClassMemberDao.findCourseClassMemberByCourseClass(courseClass);
		for(int i=0;i<classMembs.size();i++)
		{
			if(classMembs.get(i).getStu().getStuWechatOpenId() == null || classMembs.get(i).getStu().getStuWechatOpenId().isEmpty()) //未注册
			{
				unregStus.add(classMembs.get(i).getStu()); //添加到结果集
			}
		}
		return unregStus;
	}

	@Override
	public Question getQuestionBySessionId(String sessionId) //根据会话ID获取问题
	{
		LessonSession session;
		session = lessonSessionDao.findSessionById(sessionId);
		if(session == null || session.getSessionType() != 2)
		{
			return null;
		}
		return session.getQuestion();
	}

	@Override
	public Teacher login(String techId, String techPassword, String loginIp) //登陆
	{
		return teacherDao.teacherLogin(techId, techPassword, loginIp);
	}

	@Override
	public List<CourseClass> getCourseClassByTech(String teacherId)  //获取某教师所有教学班
	{
		Teacher teacher;
		teacher = teacherDao.findTeacherById(teacherId);
		if(teacher == null) //教师不存在
		{
			return null;
		}
		return courseClassDao.findCourseClassByTeacher(teacher);
	}

	@Override
	public List<LessonSession> getClassSession(String courseClassId) //获取教学班所有会话
	{
		List<LessonSession> Sessions = null;
		CourseClass courseClass;
		courseClass = courseClassDao.findCourseClassById(courseClassId);
		if(courseClass == null) //教学班不存在
		{
			return null;
		}
		Sessions = lessonSessionDao.findSessionByClass(courseClass);
		return Sessions;
	}
	
	@Override
	public LessonSession getSession(String sessionId) //获取会话
	{
		LessonSession session;
		session = lessonSessionDao.findSessionById(sessionId);
		return session;
	}

	@Override
	public List<Student> getNoAnsStusBySessionId(String sessionId) //查询某问题未回答学生
	{
		LessonSession session;
		List<CourseClassMember> classStus;
		List<StuAnswer> stuAns;
		List<Student> noAnsStus = new ArrayList<Student>();
		session = lessonSessionDao.findSessionById(sessionId);
		classStus = courseClassMemberDao.findCourseClassMemberByCourseClass(session.getCourseClass());
		stuAns = stuAnswerDao.findStuAnswerByQuestion(session.getQuestion());
		for(int i=0;i<classStus.size();i++)
		{
			boolean isAns = false;
			for(int j=0;j<stuAns.size();j++)
			{
				if(classStus.get(i).getStu().getStuId().equals(stuAns.get(j).getStudent().getStuId()))
				{
					isAns = true;
					break;
				}
			}
			if(!isAns)
			{
				noAnsStus.add(classStus.get(i).getStu());
			}
		}
		return noAnsStus;
	}

	@Override
	public CourseClass getCourseClassById(String courseClassId) //通过ID取教学班
	{
		return courseClassDao.findCourseClassById(courseClassId);
	}

	@Override
	public boolean delSession(String sessionId) //删除一个会话
	{
		return lessonSessionDao.deleteSessionById(sessionId);
	}


}
