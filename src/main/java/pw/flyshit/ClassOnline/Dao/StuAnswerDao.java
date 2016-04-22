package pw.flyshit.ClassOnline.Dao;
import java.util.List;

import pw.flyshit.ClassOnline.Domain.StuAnswer;
import pw.flyshit.ClassOnline.Domain.Question;
import pw.flyshit.ClassOnline.Domain.Student;
public interface StuAnswerDao 
{
	public StuAnswer findStuAnswerById(String stuAnsId); //通过ID查找
	public List<StuAnswer> findStuAnswerByQuestion(Question question); //查找某问题的所有回答
	public List<StuAnswer> findCorrectStuAnswerByQuestion(Question question); //查找某问题的所有正确回答
	public List<StuAnswer> findStuAnswerByStudent(Student student); //查找某学生的所有回答
	public List<StuAnswer> findCorrectStuAnswerByStudent(Student student); //查找某学生的所有正确回答
	public List<StuAnswer> findStuAnswerByStuAndQues(Student student,Question question); //查找某学生对某问题的所有回答
	public List<StuAnswer> findCorrectStuAnswerByStuAndQues(Student student,Question question); //查找某学生对某问题的所有正确回答
	public boolean deleteStuAnswerById(String stuAnsId); //通过ID删除
	public int deleteStuAnswerByQuestion(Question question); //删除某问题的所有回答，返回删除数
	public int deleteStuAnswerByStudent(Student student); //删除某学生的所有回答，返回删除数
	public int deleteStuAnswerByStuAndQuest(Student student,Question question); //删除某学生对某问题的所有回答，返回删除数
	public boolean addStuAnswer(StuAnswer stuAnswer); //添加回答
}
