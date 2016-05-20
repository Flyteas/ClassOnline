package pw.flyshit.ClassOnline.Dao.Impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;
import pw.flyshit.ClassOnline.Dao.TeacherDao;
import pw.flyshit.ClassOnline.Domain.Teacher;
@Repository
public class TeacherDaoImpl implements TeacherDao
{
	private HibernateTemplate ht;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
	{
		ht = new HibernateTemplate(sessionFactory);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Teacher teacherLogin(String techId,String techPassword,String loginIp) //登陆教师帐户，失败返回NULL
	{
		String hqlStr = "from Teacher where techId=? and techPassword=?";
		Teacher loginTeacher = new Teacher();
		List<Teacher> teacherLoginResult = (List<Teacher>)ht.find(hqlStr,techId,techPassword);
		if(teacherLoginResult.size()>0)
		{
			loginTeacher.setTechId(teacherLoginResult.get(0).getTechId()); //对象拷贝
			loginTeacher.setTechLastLoginIP(teacherLoginResult.get(0).getTechLastLoginIP());
			loginTeacher.setTechLastLoginTime(teacherLoginResult.get(0).getTechLastLoginTime());
			loginTeacher.setTechPassword(teacherLoginResult.get(0).getTechPassword());
			loginTeacher.setTechPhoneNum(teacherLoginResult.get(0).getTechPhoneNum());
			loginTeacher.setTechRealName(teacherLoginResult.get(0).getTechRealName());
			loginTeacher.setTechRole(teacherLoginResult.get(0).getTechRole());
			loginTeacher.setTechSex(teacherLoginResult.get(0).getTechSex());
			
			teacherLoginResult.get(0).setTechLastLoginTime(System.currentTimeMillis()); //更新登陆时间
			teacherLoginResult.get(0).setTechLastLoginIP(loginIp); //更新IP
			ht.update(teacherLoginResult.get(0));
			return loginTeacher;
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public Teacher findTeacherById(String techId) //通过ID查找
	{
		return ht.get(Teacher.class, techId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Teacher> findTeacherByName(String techRealName) //通过姓名查找
	{
		String hqlStr = "from Teacher where techRealName=?";
		return (List<Teacher>)ht.find(hqlStr,techRealName);	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Teacher> findTeacherByType(int techRole) //查找某角色的所有教师，0为管理员，1为普通教师
	{
		String hqlStr = "from Teacher where techRole=?";
		return (List<Teacher>)ht.find(hqlStr,techRole);	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Teacher> findTeacherByLastIp(String techLastLoginIP) //通过最后登陆IP查找教师帐户
	{
		String hqlStr = "from Teacher where techLastLoginIP=?";
		return (List<Teacher>)ht.find(hqlStr,techLastLoginIP);	
	}
	
	@Transactional
	@Override
	public boolean deleteTeacherById(String techId) //通过ID删除
	{
		Teacher deleteTeacher;
		deleteTeacher = this.findTeacherById(techId);
		if(deleteTeacher == null)
		{
			return false;
		}
		ht.delete(deleteTeacher);
		return true;
	}
	
	@Transactional
	@Override
	public boolean addTeacher(Teacher teacher) //添加教师
	{
		ht.save(teacher);
		return true;
	}
}
