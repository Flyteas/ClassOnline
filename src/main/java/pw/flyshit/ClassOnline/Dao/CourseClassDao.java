package pw.flyshit.ClassOnline.Dao;
import java.util.List;

import pw.flyshit.ClassOnline.Domain.CourseClass;
import pw.flyshit.ClassOnline.Domain.Course;
import pw.flyshit.ClassOnline.Domain.Teacher;
public interface CourseClassDao 
{
	public CourseClass findCourseClassById(String courseClassId); //通过课程上课班ID查询
	public List<CourseClass> findCourseClassByCourse(Course course); //通过课程查找课程上课班，即返回某课程所有上课班
	public List<CourseClass> findCourseClassByTeacher(Teacher teacher); //通过教师查找课程上课班，即返回某教师所有上课班
	public List<CourseClass> findCourseClassByCourseAndTeacher(Course course,Teacher teacher); //通过课程和教师查询上课班，即返回某教师某课程的所有上课班
	public boolean deleteCourseClassById(String courseClassId); //通过ID删除
	public int deleteCourseClassByCourse(Course course); //通过课程删除，即删除某课程所有上课班，返回删除的数目
	public int deleteCourseClassByTeacher(Teacher teacher); //通过教师删除，即删除某教师所有上课班，返回删除的数目
	public int deleteCourseClassByCourseAndTeacher(Course course,Teacher teacher); //通过课程和教师删除，即删除某教师某课程的所有上课班，返回删除的数目
	public boolean addCourseClass(CourseClass courseClass); //添加上课班
}
