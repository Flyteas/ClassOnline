package pw.flyshit.ClassOnline.Dao;
import java.util.List;

import pw.flyshit.ClassOnline.Domain.Student;
public interface StudentDao 
{
	public int studentReg(String stuId,String openId); //注册学生OpenID，成功返回0,已注册返回1,无此学生ID返回2
	public int studentDeleteRegInfo(String stuId); //删除指定学生注册信息，即根据学生ID清空OpenId信息，成功返回0，OpenId为空返回1，无此学生ID返回2
	public Student findStudentById(String stuId); //通过学生ID查询学生，不存在返回null
	public List<Student> findStudentByName(String stuName); //通过学生名字查询学生
	public List<Student> findStudentByOpenId(String stuOpenId); //通过OpenId查询学生
	public boolean deleteStudentById(String stuId); //通过学生ID删除学生
	public boolean addStudent(Student newStudent); //添加学生
}
