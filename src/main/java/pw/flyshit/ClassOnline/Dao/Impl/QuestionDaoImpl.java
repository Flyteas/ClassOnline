package pw.flyshit.ClassOnline.Dao.Impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import pw.flyshit.ClassOnline.Dao.QuestionDao;
import pw.flyshit.ClassOnline.Domain.Question;
@Repository
public class QuestionDaoImpl implements QuestionDao
{
	private HibernateTemplate ht;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
	{
		ht = new HibernateTemplate(sessionFactory);
	}
	
	@Override
	public Question findQuestionById(String questionId) //通过ID查找
	{
		return ht.get(Question.class, questionId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Question> findQuestionByCreateTime(long createTime) //通过创建时间查找
	{
		String hqlStr = "from Question where createTime=?";
		return (List<Question>)ht.find(hqlStr, createTime);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Question> findQuestionByTimeRange(long startFindTime,long endFindTime) //查找某时间段内所有创建的问题
	{
		String hqlStr = "from Question where createTime>=? and createTime<=?";
		return (List<Question>)ht.find(hqlStr, startFindTime,endFindTime);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Question> findQuestionByTitle(String questionTitle) //通过问题查找(精确查找)
	{
		String hqlStr = "from Question where questionTitle=?";
		return (List<Question>)ht.find(hqlStr, questionTitle);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Question> searchQuestionByTitle(String questionTitle) //通过问题查找(模糊查找)
	{
		String hqlStr = "from Question where questionTitle like ?";
		return (List<Question>)ht.find(hqlStr, "%"+questionTitle+"%");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Question> findQuestionByType(int questionType) //返回某种类型的所有问题，0为单选，1为多选，2为短问答
	{
		String hqlStr = "from Question where questionType=?";
		return (List<Question>)ht.find(hqlStr, questionType);
	}
	
	@Transactional
	@Override
	public boolean deleteQuestionById(String questionId) //通过ID删除
	{
		Question delQues;
		delQues = this.findQuestionById(questionId);
		if(delQues == null) //不存在
		{
			return false;
		}
		try
		{
			ht.delete(delQues);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}
	
	@Transactional
	@Override
	public boolean addQuestion(Question question) //添加问题
	{
		try
		{
			ht.save(question);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}
}
