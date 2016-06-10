package pw.flyshit.ClassOnline.Service;
import java.util.List;

import pw.flyshit.ClassOnline.Domain.Course;
import pw.flyshit.ClassOnline.Domain.CourseClass;
import pw.flyshit.ClassOnline.Domain.LessonSession;
import pw.flyshit.ClassOnline.Domain.Question;
import pw.flyshit.ClassOnline.Domain.StuAnswer;
import pw.flyshit.ClassOnline.Domain.StuSignIn;
import pw.flyshit.ClassOnline.Domain.Student;
/* 教师端业务接口类  */
import pw.flyshit.ClassOnline.Domain.Teacher;

public interface TeacherService 
{
	public Teacher login(String techId,String techPassword, String loginIp); //教师账号登陆
	public boolean modifyInfo(Teacher teacher); //修改教师信息
	public LessonSession getCurrentSession(String courseClassId); //获取某教学班的当前会话
	public List<LessonSession> getClassSession(String courseClassId); //获取教学班所有会话
	public LessonSession getSession(String sessionId); //获取一个会话
	public LessonSession startNewSession(String sessionName,int sessionType, String courseClassId, long beginTime, long endTime); //开启一个新的会话
	public LessonSession startNewSession(String sessionName,int sessionType, String courseClassId, long beginTime, long endTime, Question question); //开启一个新的会话
	public boolean stopSession(String lessonSessionId); //停止一个会话
	public boolean delSession(String sessionId); //删除一个会话，级联删除
	public List<Student> getRegisteredStudent(); //获取所有已注册学生
	public List<Student> getUnregisterStudent(); //获取所有未注册学生
	public List<Student> getRegStusByCourseClass(String courseClassId); //查询某教学班所有已注册学生
	public List<Student> getUnregStusByCourseClass(String courseClassId); //查询某教学班所有未注册学生
	public boolean deleteStuRegInfo(String studentId); //清除指定学生注册信息，也就是变为未注册状态
	public boolean delClassStu(String studentId,String courseClassId); //删除教学班学生
	public List<StuSignIn> getSignInStudent(String lessonSessionId); //获取指定会话所有签到记录，按签到顺序升序排列
	public List<Student> getUnsignInStudent(String lessonSessionId); //获取指定会话所有未签到学生
	public List<StuAnswer> getStuAnswerBySessionId(String lessonSessionId); //根据会话ID获取答题记录
	public List<StuAnswer> getStuAnswerByQuestionId(String questionId); //根据问题ID获取答题记录
	public List<StuAnswer> getCorrectAnsBySessionId(String lessonSessionId); //根据会话ID获取正确答题记录
	public List<StuAnswer> getCorrectAnsByQuestionId(String questionId); //根据问题ID获取正确答题记录
	public List<Student> getNoAnsStusBySessionId(String sessionId); //查询某问题未回答学生
	public Question getQuestionBySessionId(String sessionId); //根据SessionId获取问题
	public List<CourseClass> getCourseClassByTech(String teacherId); //获取某教师所有教学班
	public CourseClass getCourseClassById(String courseClassId); //通过ID取教学班
	public List<Student> getCourseClassStus(String courseClassId); //通过ID获取教学班学生
	public Student getStuById(String stuId); //通过ID获取学生
	public boolean addClassStu(String stuId,String courseClassId); // 添加学生到教学班
	public List<Teacher> searchTeacher(String keyword); //通过ID或姓名模糊搜索教师
	public Teacher findTeacher(String techId); //通过ID查找教师
	public boolean addTeacher(Teacher teacher); //添加教师
	public boolean delTeacher(String techId); //删除教师
	public boolean delStudent(String stuId); //删除学生
	public List<Student> searchStu(String keyword); //通过学号或名字搜索学生
	public boolean addStudent(Student stu); //添加学生
	public boolean modifyStu(Student stu); //修改学生信息
	public List<CourseClass> searchClass(String clsId); //通过ID搜索班级
	public boolean delClass(String clsId); //删除班级
	public boolean addCourse(Course course); //添加课程
	public boolean delCourse(String courseId); //删除课程
	public boolean addCourseClass(CourseClass cls); //添加班级
	public boolean modifyCourseClass(CourseClass modifyCls); //修改班级
} 
