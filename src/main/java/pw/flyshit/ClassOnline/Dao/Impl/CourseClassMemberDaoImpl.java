package pw.flyshit.ClassOnline.Dao.Impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import pw.flyshit.ClassOnline.Dao.CourseClassMemberDao;
import pw.flyshit.ClassOnline.Domain.CourseClass;
import pw.flyshit.ClassOnline.Domain.CourseClassMember;
import pw.flyshit.ClassOnline.Domain.Student;
@Repository
public class CourseClassMemberDaoImpl implements CourseClassMemberDao
{
	private HibernateTemplate ht;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
	{
		ht = new HibernateTemplate(sessionFactory);
	}
	
	@Override
	public CourseClassMember findCourseClassMemberById(String memberId) //根据ID查询条目
	{
		return ht.get(CourseClassMember.class, memberId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CourseClassMember> findCourseClassMemberByStu(Student stu) //根据学生查询条目，即查询某学生所有上课班
	{
		String hqlStr = "from CourseClassMember where stu=?";
		return (List<CourseClassMember>)ht.find(hqlStr, stu);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CourseClassMember> findCourseClassMemberByCourseClass(CourseClass courseClass) //根据上课班查询条目，即查询某上课班的所有学生
	{
		String hqlStr = "from CourseClassMember where courClass=?";
		return (List<CourseClassMember>)ht.find(hqlStr,courseClass);
	}
	
	@Transactional
	@Override
	public boolean deleteCourseClassMemberById(String memberId) //根据ID删除条目
	{
		CourseClassMember delCourClassMem;
		delCourClassMem = this.findCourseClassMemberById(memberId);
		if(delCourClassMem == null) //不存在
		{
			return false;
		}
		try
		{
			ht.delete(delCourClassMem);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}
	
	@Transactional
	@Override
	public int deleteCourseClassMemberByStu(Student stu) //根据学生删除条目，即删除某学生所有上课班信息，返回删除条目数量
	{
		List<CourseClassMember> delCourClassMemSet;
		delCourClassMemSet = this.findCourseClassMemberByStu(stu);
		if(delCourClassMemSet.size()>0)
		{
			ht.deleteAll(delCourClassMemSet);
		}
		return delCourClassMemSet.size();
	}
	
	@Transactional
	@Override
	public int deleteCourseClassMemberByCourseClass(CourseClass courseClass) //根据上课班删除条目，即删除某上课班所有成员信息，返回删除条目数量
	{
		List<CourseClassMember> delCourClassMemSet;
		delCourClassMemSet = this.findCourseClassMemberByCourseClass(courseClass);
		if(delCourClassMemSet.size()>0)
		{
			ht.deleteAll(delCourClassMemSet);
		}
		return delCourClassMemSet.size();
	}
	
	@Transactional
	@Override
	public int addCourseClassMember(CourseClassMember courseClassMember) //添加条目，成功返回0,学生不存在返回1，上课班不存在返回2
	{
		if(ht.get(Student.class, courseClassMember.getStu().getStuId()) == null) //学生不存在
		{
			return 1;
		}
		if(ht.get(CourseClass.class, courseClassMember.getCourseClass().getCourseClassId()) == null) //上课班不存在
		{
			return 2;
		}
		ht.save(courseClassMember);
		return 0;
	}

	@Transactional
	@Override
	@SuppressWarnings("unchecked")
	public boolean delMembByStuAndCour(Student stu, CourseClass courClass) //删除教学班某学生
	{
		String hqlStr = "from CourseClassMember where stu=? and courClass=?";
		List<CourseClassMember> membs = (List<CourseClassMember>)ht.find(hqlStr, stu,courClass);
		if(membs.size() == 0)
		{
			return false;
		}
		try
		{
			ht.deleteAll(membs);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}
}
