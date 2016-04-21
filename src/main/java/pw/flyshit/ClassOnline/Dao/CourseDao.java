package pw.flyshit.ClassOnline.Dao;
import java.util.List;

import pw.flyshit.ClassOnline.Domain.Course;
public interface CourseDao 
{
	Course findCourseById(String courseId); //通过课程ID查找课程，不存在返回NULL
	List<Course> findCourseByName(String courseName); //通过课程名称查找课程
	boolean deleteCourseById(String courseId); //通过课程ID删除课程
	boolean addCourse(Course course); //添加课程
}
