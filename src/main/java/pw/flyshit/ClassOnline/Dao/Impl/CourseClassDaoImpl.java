package pw.flyshit.ClassOnline.Dao.Impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import pw.flyshit.ClassOnline.Dao.CourseClassDao;
import pw.flyshit.ClassOnline.Domain.Course;
import pw.flyshit.ClassOnline.Domain.CourseClass;
import pw.flyshit.ClassOnline.Domain.Teacher;
@Repository
public class CourseClassDaoImpl implements CourseClassDao
{
	private HibernateTemplate ht;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
	{
		ht = new HibernateTemplate(sessionFactory);
	}
	
	@Override
	public CourseClass findCourseClassById(String courseClassId) //通过课程上课班ID查询
	{
		return ht.get(CourseClass.class, courseClassId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CourseClass> findCourseClassByCourse(Course course) //通过课程查找课程上课班，即返回某课程所有上课班
	{
		String hqlStr = "from CourseClass where course=?";
		return (List<CourseClass>)ht.find(hqlStr, course);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CourseClass> findCourseClassByTeacher(Teacher teacher) //通过教师查找课程上课班，即返回某教师所有上课班
	{
		String hqlStr = "from CourseClass where teacher=?";
		return (List<CourseClass>)ht.find(hqlStr, teacher);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CourseClass> findCourseClassByCourseAndTeacher(Course course,Teacher teacher) //通过课程和教师查询上课班，即返回某教师某课程的所有上课班
	{
		String hqlStr = "from CourseClass where course=? and teacher=?";
		return (List<CourseClass>)ht.find(hqlStr,course, teacher);
	}
	
	@Transactional
	@Override
	public boolean deleteCourseClassById(String courseClassId) //通过ID删除
	{
		CourseClass delCourClass;
		delCourClass = this.findCourseClassById(courseClassId);
		if(delCourClass == null) //不存在
		{
			return false;
		}
		try
		{
			ht.delete(delCourClass);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}
	
	@Transactional
	@Override
	public int deleteCourseClassByCourse(Course course) //通过课程删除，即删除某课程所有上课班，返回删除的数目
	{
		List<CourseClass> delCourClassSet;
		delCourClassSet = this.findCourseClassByCourse(course);
		if(delCourClassSet.size()>0)
		{
			ht.deleteAll(delCourClassSet);
		}
		return delCourClassSet.size();
	}
	
	@Transactional
	@Override
	public int deleteCourseClassByTeacher(Teacher teacher) //通过教师删除，即删除某教师所有上课班，返回删除的数目
	{
		List<CourseClass> delCourClassSet;
		delCourClassSet = this.findCourseClassByTeacher(teacher);
		if(delCourClassSet.size()>0)
		{
			ht.deleteAll(delCourClassSet);
		}
		return delCourClassSet.size();
	}
	
	@Transactional
	@Override
	public int deleteCourseClassByCourseAndTeacher(Course course,Teacher teacher) //通过课程和教师删除，即删除某教师某课程的所有上课班，返回删除的数目
	{
		List<CourseClass> delCourClassSet;
		delCourClassSet = this.findCourseClassByCourseAndTeacher(course, teacher);
		if(delCourClassSet.size()>0)
		{
			ht.deleteAll(delCourClassSet);
		}
		return delCourClassSet.size();
	}
	
	@Transactional
	@Override
	public boolean addCourseClass(CourseClass courseClass) //添加上课班
	{
		try
		{
			ht.save(courseClass);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CourseClass> searchClass(String clsId) 
	{
		String hql;
		if(clsId == null || clsId.isEmpty())
		{
			hql = "from CourseClass";
			return (List<CourseClass>) ht.find(hql);
		}
		else
		{
			hql = "from CourseClass where courseClassId like ?";
			return (List<CourseClass>) ht.find(hql, "%"+clsId+"%");
		}
	}

	@Override
	public boolean modifyCls(CourseClass modifyCls) 
	{
		try
		{
			ht.update(modifyCls);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}
}
