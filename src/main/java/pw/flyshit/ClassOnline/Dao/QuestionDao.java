package pw.flyshit.ClassOnline.Dao;
import java.util.List;

import pw.flyshit.ClassOnline.Domain.Question;

public interface QuestionDao 
{
	public Question findQuestionById(String questionId); //通过ID查找
	public Question findQuestionByCreateTime(long createTime); //通过创建时间查找
	public List<Question> findQuestionByTimeRange(long startFindTime,long endFindTime); //查找某时间段内所有创建的问题
	public List<Question> findQuestionByTitle(String questionTitle); //通过问题查找(精确查找)
	public List<Question> searchQuestionByTitle(String questionTitle); //通过问题查找(模糊查找)
	public List<Question> findQuestionByType(int questionType); //返回某种类型的所有问题，0为单选，1为多选，2为短问答
	public boolean deleteQuestionById(String questionId); //通过ID删除
	public boolean addQuestion(Question question); //添加问题
}
