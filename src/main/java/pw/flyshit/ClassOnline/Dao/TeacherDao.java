package pw.flyshit.ClassOnline.Dao;
import java.util.List;
import pw.flyshit.ClassOnline.Domain.Teacher;

public interface TeacherDao 
{
	public Teacher teacherLogin(String techId,String techPassword,String loginIp); //登陆教师帐户，失败返回NULL
	public Teacher findTeacherById(String techId); //通过ID查找
	public List<Teacher> findTeacherByName(String techRealName); //通过姓名查找
	public List<Teacher> findTeacherByType(int techRole); //查找某角色的所有教师，0为管理员，1为普通教师
	public List<Teacher> findTeacherByLastIp(String techLastLoginIP); //通过最后登陆IP查找教师帐户
	public boolean deleteTeacherById(String techId); //通过ID删除
	public boolean addTeacher(Teacher teacher); //添加教师
}
