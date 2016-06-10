package pw.flyshit.ClassOnline.Dao;
import pw.flyshit.ClassOnline.Domain.CourseClassMember;
import pw.flyshit.ClassOnline.Domain.Student;
import pw.flyshit.ClassOnline.Domain.CourseClass;
import java.util.List;

public interface CourseClassMemberDao 
{
	public CourseClassMember findCourseClassMemberById(String memberId); //根据ID查询条目
	public List<CourseClassMember> findCourseClassMemberByStu(Student stu); //根据学生查询条目，即查询某学生所有上课班
	public List<CourseClassMember> findCourseClassMemberByCourseClass(CourseClass courseClass); //根据上课班查询条目，即查询某上课班的所有学生
	public boolean deleteCourseClassMemberById(String memberId); //根据ID删除条目
	public int deleteCourseClassMemberByStu(Student stu); //根据学生删除条目，即删除某学生所有上课班信息，返回删除条目数量
	public int deleteCourseClassMemberByCourseClass(CourseClass courseClass); //根据上课班删除条目，即删除某上课班所有成员信息，返回删除条目数量
	public int addCourseClassMember(CourseClassMember courseClassMember); //添加条目，成功返回0,学生不存在返回1，上课班不存在返回2
	public boolean delMembByStuAndCour(Student stu,CourseClass courClass); //删除教学班某个学生
}
