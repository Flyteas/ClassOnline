package pw.flyshit.ClassOnline.Dao.Impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import pw.flyshit.ClassOnline.Dao.StudentDao;
import pw.flyshit.ClassOnline.Domain.Student;
@Repository
public class StudentDaoImpl implements StudentDao
{
	private HibernateTemplate ht;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
	{
		ht = new HibernateTemplate(sessionFactory);
	}
	
	@Transactional
	@Override
	public int studentReg(String stuId,String openId) //注册学生OpenID，成功返回0,已注册返回1,无此学生ID返回2
	{
		Student regStu;
		regStu = this.findStudentById(stuId);
		if(regStu == null) //无此学生
		{
			return 2;
		}
		if(!regStu.getStuOpenId().isEmpty()) //OpenId非空，即已注册
		{
			return 1;
		}
		regStu.setStuOpenId(openId);
		ht.update(regStu);
		return 0;
	}
	
	@Transactional
	@Override
	public int studentDeleteRegInfo(String stuId) //删除指定学生注册信息，即根据学生ID清空OpenId信息，成功返回0，OpenId为空返回1，无此学生ID返回2
	{
		Student modifyStu;
		modifyStu = this.findStudentById(stuId);
		if(modifyStu == null) //不存在此学生
		{
			return 2;
		}
		if(modifyStu.getStuOpenId().isEmpty()) //OpenId为空，即未注册，不需要清空注册信息
		{
			return 1;
		}
		modifyStu.setStuOpenId(""); //设置OpenId为空
		ht.update(modifyStu);
		return 0;
	}
	
	@Override
	public Student findStudentById(String stuId) //通过学生ID查询学生，不存在返回null
	{
		return ht.get(Student.class, stuId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Student> findStudentByName(String stuName) //通过学生名字查询学生
	{
		String hqlStr = "from Student wehre stuName=?";
		return (List<Student>)ht.find(hqlStr, stuName);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Student> findStudentByOpenId(String stuOpenId) //通过OpenId查询学生，不存在返回null
	{
		String hqlStr = "from Student wehre stuWechatOpenId=?";
		return (List<Student>)ht.find(hqlStr, stuOpenId);
	}
	
	@Transactional
	@Override
	public boolean deleteStudentById(String stuId) //通过学生ID删除学生
	{
		Student delStu;
		delStu = this.findStudentById(stuId);
		if(delStu == null) //不存在此学生
		{
			return false;
		}
		ht.delete(delStu);
		return true;
	}
	
	@Transactional
	@Override
	public boolean addStudent(Student newStudent) //添加学生
	{
		ht.save(newStudent);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Student> getAllRegStu() //获取所有已注册学生
	{
		String hqlStr = "from Student wehre stuWechatOpenId is not null";
		return (List<Student>)ht.find(hqlStr);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Student> getAllUnregStu() //获取所有未注册学生
	{
		String hqlStr = "from Student wehre stuWechatOpenId is null";
		return (List<Student>)ht.find(hqlStr);
	}
}
