package pw.flyshit.ClassOnline.Service;
import java.util.List;

import pw.flyshit.ClassOnline.Domain.LessonSession;
import pw.flyshit.ClassOnline.Domain.Question;
import pw.flyshit.ClassOnline.Domain.StuAnswer;
import pw.flyshit.ClassOnline.Domain.StuSignIn;
import pw.flyshit.ClassOnline.Domain.Student;
/* 教师端业务接口类  */

public interface TeacherService 
{
	public LessonSession startNewSession(int sessionType, String courseClassId, long beginTime, long endTime); //开启一个新的会话
	public LessonSession startNewSession(int sessionType, String courseClassId, long beginTime, long endTime, Question question); //开启一个新的会话
	public boolean stopSession(String lessonSessionId); //停止一个会话
	public List<Student> getRegisteredStudent(); //获取所有已注册学生
	public List<Student> getUnregisterStudent(); //获取所有未注册学生
	public boolean deleteStuRegInfo(String studentId); //清除指定学生注册信息，也就是变为未注册状态
	public List<StuSignIn> getSignInStudent(String lessonSessionId); //获取指定会话所有已签到学生
	public List<Student> getUnsignInStudent(String lessonSessionId); //获取指定会话所有未签到学生
	public List<StuAnswer> getStuAnswerBySessionId(String lessonSessionId); //根据会话ID获取答题记录
	public List<StuAnswer> getStuAnswerByQuestionId(String questionId); //根据问题ID获取答题记录
	public List<StuAnswer> getCorrectAnsBySessionId(String lessonSessionId); //根据会话ID获取正确答题记录
	public List<StuAnswer> getCorrectAnsByQuestionId(String questionId); //根据问题ID获取正确答题记录
}
