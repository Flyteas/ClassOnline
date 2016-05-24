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
import pw.flyshit.ClassOnline.Service.StudentService;

/* 学生微信端业务实现类 */
@Transactional
@Service
public class StudentServiceImpl implements StudentService
{
	@Autowired
	private CourseClassDao courseClassDao;
	@Autowired
	private CourseClassMemberDao courseClassMemberDao;
	@Autowired
	private CourseDao courseDao;
	@Autowired
	private LessonDao lessonDao;
	@Autowired
	private LessonSessionDao lessonSessionDao;
	@Autowired
	private QuestionDao questionDao;
	@Autowired
	private StuAnswerDao stuAnswerDao;
	@Autowired
	private StudentDao studentDao;
	@Autowired
	private StuSignInDao stuSignInDao;
	@Autowired
	private TeacherDao teacherDao;
	
	@Override
	public String handleStuMsg(String openId, String msg) 
	{
		String responseMsg;
		List<Student> stus;
		Student stu;
		boolean isRegType = false;
		stus = studentDao.findStudentByOpenId(openId);
		if(msg.length()>4&&msg.substring(0, 4).equals("reg "))
		{
			isRegType = true;
			String stuId = msg.substring(4);
			stu = studentDao.findStudentById(stuId);
			if(stu == null)
			{
				responseMsg = "学号错误!";
				return responseMsg;
			}
		}
		else
		{
			if(stus.isEmpty()) //未注册
			{
				responseMsg = "您未注册!";
				return responseMsg;
			}
			stu = stus.get(0);
		}
		List<CourseClassMember> courseClassMembs;
		courseClassMembs = courseClassMemberDao.findCourseClassMemberByStu(stu);
		if(courseClassMembs.isEmpty()) //无班级
		{
			responseMsg = "您无课程!";
			return responseMsg;
		}
		List<CourseClass> stuClasses = new ArrayList<CourseClass>();
		for(int i=0;i<courseClassMembs.size();i++)
		{
			stuClasses.add(courseClassMembs.get(i).getCourseClass());
		}
		LessonSession currentSession = null;
		for(int i=0;i<stuClasses.size();i++)
		{
			List<LessonSession> currentSessions;
			currentSessions = lessonSessionDao.getCurrentSessionByCourClass(stuClasses.get(i));
			if(!currentSessions.isEmpty())
			{
				currentSession = currentSessions.get(0);
			}
		}
		if(currentSession == null)
		{
			responseMsg = "您无正在进行的会话!";
			return responseMsg;
		}
		if(currentSession.getSessionType() !=0 && isRegType) //目前会话是不是注册模式，但学生发的是请求注册
		{
			responseMsg = "当前会话非注册模式!";
			return responseMsg;
		}
		if(currentSession.getSessionType() == 0) //正在进行的会话为注册会话
		{
			responseMsg = this.reg(openId,msg,currentSession);
		}
		else if(currentSession.getSessionType() == 1) //签到会话
		{
			responseMsg = this.signIn(openId, msg, currentSession);
		}
		else if(currentSession.getSessionType() == 2) //答题会话
		{
			responseMsg = this.answerQuest(openId, msg, currentSession);
		}
		else //会话类型错误
		{
			responseMsg = "会话类型错误!";
		}
		return responseMsg;
	}

	@Override
	public String reg(String openId, String msg, LessonSession currentSession) //学生注册
	{
		String responseMsg;
		String stuId;
		int regResult;
		if(msg.length()>3 && !msg.substring(0, 3).equals("reg"))
		{
			responseMsg = "注册格式错误!\n注册格式应为:\nreg 学号";
			return responseMsg;
		}
		if(!studentDao.findStudentByOpenId(openId).isEmpty()) //微信号已注册过
		{
			Student stu;
			stu = studentDao.findStudentByOpenId(openId).get(0);
			responseMsg = "该微信号已注册!\n当前注册信息:\n姓名  " + stu.getStuName() + "\n学号  " + stu.getStuId();
			return responseMsg;
		}
		stuId = msg.substring(4);
		regResult = studentDao.studentReg(stuId, openId);
		if(regResult == 3) //无此学生
		{
			responseMsg = "学号错误!";
		}
		else if(regResult == 2) //已注册过但不是当前微信号注册的
		{
			responseMsg = "已被其他微信号注册!";
		}
		else if(regResult == 1) //已注册过
		{
			responseMsg = "您已注册过!";
		}
		else
		{
			responseMsg = "注册成功!";
		}
		return responseMsg;
	}

	@Override
	public String signIn(String openId, String msg, LessonSession currentSession)
	{
		String responseMsg;
		Student stu;
		Student stuById;
		List<StuSignIn> signInRecords;
		if(studentDao.findStudentByOpenId(openId).isEmpty()) //未注册
		{
			responseMsg = "您未注册!";
			return responseMsg;
		}
		stu = studentDao.findStudentByOpenId(openId).get(0);
		signInRecords = stuSignInDao.findStuSignInBySession(currentSession);
		stuById = studentDao.findStudentById(msg);
		if(stuById == null || !stuById.equals(stu)) //签到学号与微信号绑定的学号不符
		{
			responseMsg = "签到失败!\n签到学号和微信注册学号不一致!签到格式:\n学号";
			return responseMsg;
		}
		for(int i=0;i<signInRecords.size();i++)
		{
			if(signInRecords.get(i).getStu().getStuId().equals(stu.getStuId())) //已签到
			{
				responseMsg = "您已签到!\n签到顺序  " + String.valueOf(signInRecords.get(i).getSignInOrder());
				return responseMsg;
			}
		}
		StuSignIn stuSignIn = new StuSignIn();
		stuSignIn.setSignInId(String.valueOf(System.currentTimeMillis()));
		stuSignIn.setLessonSession(currentSession);
		stuSignIn.setSignInOrder(signInRecords.size()+1);
		stuSignIn.setStu(stu);
		stuSignIn.setSignInTime(System.currentTimeMillis());
		if(stuSignInDao.addStuSignIn(stuSignIn))
		{
			responseMsg = "签到成功!\n签到顺序  " + String.valueOf(stuSignIn.getSignInOrder());
		}
		else
		{
			responseMsg = "签到失败!";
		}
		return responseMsg;
	}

	@Override
	public String answerQuest(String openId, String msg, LessonSession currentSession) //学生回答问题
	{
		String responseMsg;
		Student stu;
		if(studentDao.findStudentByOpenId(openId).isEmpty()) //未注册
		{
			responseMsg = "您未注册!";
			return responseMsg;
		}
		stu = studentDao.findStudentByOpenId(openId).get(0);
		if(currentSession.getQuestion() == null)
		{
			responseMsg = "问题不存在!";
			return responseMsg;
		}
		Question question = currentSession.getQuestion();
		List<StuAnswer> stuAnses = stuAnswerDao.findStuAnswerByQuestion(question);
		StuAnswer stuAns = new StuAnswer();
		stuAns.setStuAnswerId(String.valueOf(System.currentTimeMillis()));
		stuAns.setStudent(stu);
		stuAns.setQuestion(question);
		stuAns.setAnswer(msg);
		stuAns.setAnswerOrder(stuAnses.size()+1);
		stuAns.setAnswerTime(System.currentTimeMillis());
		if(question.getQuestionType() == 2) //问答题
		{
			stuAns.setAnswerCorrect(true);
			if(stuAnswerDao.addStuAnswer(stuAns))
			{
				responseMsg = "回答成功!\n回答顺序  " + String.valueOf(stuAns.getAnswerOrder());
			}
			else
			{
				responseMsg = "回答失败!";
			}
		}
		else if(question.getQuestionType() == 1) //多选题
		{
			String stuAnsStr = stuAns.getAnswer();
			String rightAnsStr = question.getRightAnswer();
			stuAnsStr = stuAnsStr.toUpperCase(); //转换成大写
			rightAnsStr = rightAnsStr.toUpperCase(); //转换成大写
			if(stuAnsStr.length() != rightAnsStr.length())
			{
				stuAns.setAnswerCorrect(false);
				responseMsg = "回答成功!\n答案错误!\n回答顺序  " + String.valueOf(stuAns.getAnswerOrder());
			}
			else
			{
				stuAns.setAnswerCorrect(true);
				for(int i=0;i<stuAnsStr.length();i++) //判断是否有重复字符
				{
					for(int j=i+1;j<stuAnsStr.length();j++)
					{
						if(stuAnsStr.charAt(i) == stuAnsStr.charAt(j)) //有重复字符，说明答案肯定不对
						{
							stuAns.setAnswerCorrect(false);
							break;
						}
					}
					if(!stuAns.isAnswerCorrect())
					{
						break;
					}
				}
				for(int i=0;i<stuAnsStr.length();i++)
				{
					if(rightAnsStr.indexOf(stuAnsStr.charAt(i)) == -1) //选了错误选项
					{
						stuAns.setAnswerCorrect(false);
						break;
					}
				}
				if(stuAns.isAnswerCorrect())
				{
					responseMsg = "回答成功!\n答案正确!\n回答顺序  " + String.valueOf(stuAns.getAnswerOrder());
				}
				else
				{
					responseMsg = "回答成功!\n答案错误!\n回答顺序  " + String.valueOf(stuAns.getAnswerOrder());
				}
			}
		}
		else if(question.getQuestionType() == 0) //单选题
		{
			String stuAnsStr = stuAns.getAnswer();
			String rightAnsStr = question.getRightAnswer();
			stuAnsStr = stuAnsStr.toUpperCase(); //转换成大写
			rightAnsStr = rightAnsStr.toUpperCase(); //转换成大写
			if(stuAnsStr.equals(rightAnsStr)) //答案正确
			{
				responseMsg = "回答成功!\n答案正确!\n回答顺序  " + String.valueOf(stuAns.getAnswerOrder());
			}
			else //答案错误
			{
				responseMsg = "回答成功!\n答案错误!\n回答顺序  " + String.valueOf(stuAns.getAnswerOrder());
			}
		}
		else
		{
			responseMsg = "回答失败!\n问题类型错误!";
		}
		return responseMsg;
	}

}
