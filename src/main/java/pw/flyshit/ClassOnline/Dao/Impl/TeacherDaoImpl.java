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
	public Teacher teacherLogin(String techId,String techPassword) //登陆教师帐户，失败返回NULL
	{
		String hqlStr = "from Teacher where techId=? and techPassword=?";
		List<Teacher> teacherLoginResult = (List<Teacher>)ht.find(hqlStr,techId,techPassword);
		if(teacherLoginResult.size()>0)
		{
			return teacherLoginResult.get(0);
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
		String hqlStr = "from Teacher wehre techRealName=?";
		return (List<Teacher>)ht.find(hqlStr,techRealName);	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Teacher> findTeacherByType(int techRole) //查找某角色的所有教师，0为管理员，1为普通教师
	{
		String hqlStr = "from Teacher wehre techRole=?";
		return (List<Teacher>)ht.find(hqlStr,techRole);	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Teacher> findTeacherByLastIp(String techLastLoginIP) //通过最后登陆IP查找教师帐户
	{
		String hqlStr = "from Teacher wehre techLastLoginIP=?";
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
