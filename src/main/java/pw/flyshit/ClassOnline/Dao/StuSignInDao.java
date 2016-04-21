package pw.flyshit.ClassOnline.Dao;
import java.util.List;

import pw.flyshit.ClassOnline.Domain.Student;
import pw.flyshit.ClassOnline.Domain.StuSignIn;
import pw.flyshit.ClassOnline.Domain.LessonSession;
public interface StuSignInDao 
{
	public StuSignIn findStuSignInById(String signInId); //通过ID查找
	public List<StuSignIn> findStuSignInByStudent(Student student); //查询某学生所有签到记录
	public List<StuSignIn> findStuSignInBySession(LessonSession lessonSession); //查询某应答会话的所有签到记录
	public boolean deleteStuSignInById(String signInId); //通过ID删除
	public int deleteStuSignInByStudent(Student student); //删除某学生所有签到记录，返回删除条目数
	public int deleteStuSignInBySession(LessonSession lessonSession); //删除某应答会话所有签到记录,返回删除条目数
	public boolean addStuSignIn(StuSignIn stuSignIn); //添加签到记录
}
