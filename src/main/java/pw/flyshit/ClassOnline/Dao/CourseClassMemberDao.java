package pw.flyshit.ClassOnline.Dao;
import pw.flyshit.ClassOnline.Domain.CourseClassMember;
import pw.flyshit.ClassOnline.Domain.Student;
import pw.flyshit.ClassOnline.Domain.CourseClass;
import java.util.List;

public interface CourseClassMemberDao 
{
	CourseClassMember findCourseClassMemberById(String memberId); //根据ID查询条目
	List<CourseClassMember> findCourseClassMemberByStu(Student stu); //根据学生查询条目，即查询某学生所有上课班
	List<CourseClassMember> findCourseClassMemberByCourseClass(CourseClass courseClass); //根据上课班查询条目，即查询某上课班的所有学生
	boolean deleteCourseClassMemberById(String memberId); //根据ID删除条目
	int deleteCourseClassMemberByStu(Student stu); //根据学生删除条目，即删除某学生所有上课班信息，返回删除条目数量
	int deleteCourseClassMemberByCourseClass(CourseClass courseClass); //根据上课班删除条目，即删除某上课班所有成员信息，返回删除条目数量
	int addCourseClassMember(CourseClassMember courseClassMember); //添加条目，成功返回0,学生不存在返回1，上课班不存在返回2
}
