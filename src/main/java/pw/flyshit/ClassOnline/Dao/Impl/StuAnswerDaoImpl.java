package pw.flyshit.ClassOnline.Dao.Impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import pw.flyshit.ClassOnline.Dao.StuAnswerDao;
import pw.flyshit.ClassOnline.Domain.Question;
import pw.flyshit.ClassOnline.Domain.StuAnswer;
import pw.flyshit.ClassOnline.Domain.Student;
@Repository
public class StuAnswerDaoImpl implements StuAnswerDao
{
	private HibernateTemplate ht;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
	{
		ht = new HibernateTemplate(sessionFactory);
	}
	
	@Override
	public StuAnswer findStuAnswerById(String stuAnsId) //通过ID查找
	{
		return ht.get(StuAnswer.class, stuAnsId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StuAnswer> findStuAnswerByQuestion(Question question) //查找某问题的所有回答
	{
		String hqlStr = "from StuAnswer where question=?";
		return (List<StuAnswer>)ht.find(hqlStr, question);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StuAnswer> findCorrectStuAnswerByQuestion(Question question) //查找某问题的所有正确回答
	{
		String hqlStr = "from StuAnswer where answerCorrect=? and question=?";
		return (List<StuAnswer>)ht.find(hqlStr,true,question);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StuAnswer> findStuAnswerByStudent(Student student) //查找某学生的所有回答
	{
		String hqlStr = "from StuAnswer where student=?";
		return (List<StuAnswer>)ht.find(hqlStr,student);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StuAnswer> findCorrectStuAnswerByStudent(Student student) //查找某学生的所有正确回答
	{
		String hqlStr = "from StuAnswer where answerCorrect=? and student=?";
		return (List<StuAnswer>)ht.find(hqlStr,true,student);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StuAnswer> findStuAnswerByStuAndQues(Student student,Question question) //查找某学生对某问题的所有回答
	{
		String hqlStr = "from StuAnswer where student=? and question=?";
		return (List<StuAnswer>)ht.find(hqlStr,student,question);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StuAnswer> findCorrectStuAnswerByStuAndQues(Student student,Question question) //查找某学生对某问题的所有正确回答
	{
		String hqlStr = "from StuAnswer where answerCorrect=? and student=? and question=?";
		return (List<StuAnswer>)ht.find(hqlStr,true,student,question);
	}
	
	@Transactional
	@Override
	public boolean deleteStuAnswerById(String stuAnsId) //通过ID删除
	{
		StuAnswer deleteAns;
		deleteAns = this.findStuAnswerById(stuAnsId);
		if(deleteAns == null) //不存在该回答
		{
			return false;
		}
		ht.delete(deleteAns);
		return false;
	}
	
	@Transactional
	@Override
	public int deleteStuAnswerByQuestion(Question question) //删除某问题的所有回答，返回删除数
	{
		List<StuAnswer> deleteAnsSet;
		deleteAnsSet = this.findStuAnswerByQuestion(question);
		if(deleteAnsSet.size()>0)
		{
			ht.deleteAll(deleteAnsSet);
		}
		return deleteAnsSet.size();
	}
	
	@Transactional
	@Override
	public int deleteStuAnswerByStudent(Student student) //删除某学生的所有回答，返回删除数
	{
		List<StuAnswer> deleteAnsSet;
		deleteAnsSet = this.findStuAnswerByStudent(student);
		if(deleteAnsSet.size()>0)
		{
			ht.deleteAll(deleteAnsSet);
		}
		return deleteAnsSet.size();
	}
	
	@Transactional
	@Override
	public int deleteStuAnswerByStuAndQuest(Student student,Question question) //删除某学生对某问题的所有回答，返回删除数
	{
		List<StuAnswer> deleteAnsSet;
		deleteAnsSet = this.findCorrectStuAnswerByStuAndQues(student, question);
		if(deleteAnsSet.size()>0)
		{
			ht.deleteAll(deleteAnsSet);
		}
		return deleteAnsSet.size();
	}
	
	@Transactional
	@Override
	public boolean addStuAnswer(StuAnswer stuAnswer) //添加回答
	{
		ht.save(stuAnswer);
		return true;
	}
}
