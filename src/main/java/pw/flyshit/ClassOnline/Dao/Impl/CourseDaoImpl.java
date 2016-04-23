package pw.flyshit.ClassOnline.Dao.Impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import pw.flyshit.ClassOnline.Dao.CourseDao;
import pw.flyshit.ClassOnline.Domain.Course;
@Repository
public class CourseDaoImpl implements CourseDao
{
	private HibernateTemplate ht;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
	{
		ht = new HibernateTemplate(sessionFactory);
	}
	
	@Override
	public Course findCourseById(String courseId) //通过课程ID查找课程，不存在返回NULL
	{
		return ht.get(Course.class, courseId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Course> findCourseByName(String courseName) //通过课程名称查找课程
	{
		String hqlStr = "from Course where courseName=?";
		return (List<Course>)ht.find(hqlStr, courseName);
	}
	
	@Transactional
	@Override
	public boolean deleteCourseById(String courseId) //通过课程ID删除课程
	{
		Course delCourse;
		delCourse = this.findCourseById(courseId);
		if(delCourse == null) //不存在
		{
			return false;
		}
		ht.delete(delCourse);
		return true;
	}
	
	@Transactional
	@Override
	public boolean addCourse(Course course) //添加课程
	{
		ht.save(course);
		return true;
	}
}
