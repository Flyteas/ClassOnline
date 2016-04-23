package pw.flyshit.ClassOnline.Dao.Impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import pw.flyshit.ClassOnline.Dao.StuSignInDao;
import pw.flyshit.ClassOnline.Domain.LessonSession;
import pw.flyshit.ClassOnline.Domain.StuSignIn;
import pw.flyshit.ClassOnline.Domain.Student;
@Repository
public class StuSignInDaoImpl implements StuSignInDao
{
	private HibernateTemplate ht;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
	{
		ht = new HibernateTemplate(sessionFactory);
	}

	@Override
	public StuSignIn findStuSignInById(String signInId) //通过ID查找
	{
		return ht.get(StuSignIn.class, signInId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StuSignIn> findStuSignInByStudent(Student student) //查询某学生所有签到记录
	{
		String hqlStr = "from StuSignIn wehre stu=?";
		return (List<StuSignIn>)ht.find(hqlStr, student);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StuSignIn> findStuSignInBySession(LessonSession lessonSession) //查询某应答会话的所有签到记录
	{
		String hqlStr = "from StuSignIn wehre lessonSession=?";
		return (List<StuSignIn>)ht.find(hqlStr,lessonSession);
	}
	
	@Transactional
	@Override
	public boolean deleteStuSignInById(String signInId) //通过ID删除
	{
		StuSignIn deleteStuSignIn;
		deleteStuSignIn = this.findStuSignInById(signInId);
		if(deleteStuSignIn == null) //无此记录
		{
			return false;
		}
		ht.delete(deleteStuSignIn);
		return true;
	}
	
	@Transactional
	@Override
	public int deleteStuSignInByStudent(Student student) //删除某学生所有签到记录，返回删除条目数
	{
		List<StuSignIn> deleteStuSignInSet;
		deleteStuSignInSet = this.findStuSignInByStudent(student);
		if(deleteStuSignInSet.size()>0)
		{
			ht.deleteAll(deleteStuSignInSet);
		}
		return deleteStuSignInSet.size();
	}
	
	@Transactional
	@Override
	public int deleteStuSignInBySession(LessonSession lessonSession) //删除某应答会话所有签到记录,返回删除条目数
	{
		List<StuSignIn> deleteStuSignInSet;
		deleteStuSignInSet = this.findStuSignInBySession(lessonSession);
		if(deleteStuSignInSet.size()>0)
		{
			ht.deleteAll(deleteStuSignInSet);
		}
		return deleteStuSignInSet.size();
	}
	
	@Transactional
	@Override
	public boolean addStuSignIn(StuSignIn stuSignIn) //添加签到记录
	{
		ht.save(stuSignIn);
		return true;
	}
}
