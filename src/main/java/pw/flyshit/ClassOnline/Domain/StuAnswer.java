package pw.flyshit.ClassOnline.Domain;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Id;
/* 学生答案实体类 */
@Entity
@Table(name = "StuAnswer")
public class StuAnswer 
{
	@Id
	@Column(length = 32)
	private String stuAnswerId; //ID
	@ManyToOne(optional = false)
	private Question question; //对应题目
	@ManyToOne(optional = false)
	private Student student; //回答的学生
	@Column(length = 256)
	private String answer; //回答的答案
	@Column(length = 1)
	private boolean answerCorrect; //答案是否正确
	@Column(length = 16)
	private long answerTime; //回答时间
	@Column(length = 16)
	private int answerOrder; //回答顺序
	public StuAnswer()
	{
		
	}

	public String getStuAnswerId() 
	{
		return stuAnswerId;
	}

	public void setStuAnswerId(String stuAnswerId) 
	{
		this.stuAnswerId = stuAnswerId;
	}

	public Question getQuestion() 
	{
		return question;
	}

	public void setQuestion(Question question) 
	{
		this.question = question;
	}

	public Student getStudent() 
	{
		return student;
	}

	public void setStudent(Student student) 
	{
		this.student = student;
	}

	public String getAnswer() 
	{
		return answer;
	}

	public void setAnswer(String answer) 
	{
		this.answer = answer;
	}

	public boolean isAnswerCorrect() 
	{
		return answerCorrect;
	}

	public void setAnswerCorrect(boolean answerCorrect) 
	{
		this.answerCorrect = answerCorrect;
	}

	public long getAnswerTime() 
	{
		return answerTime;
	}

	public void setAnswerTime(long answerTime) 
	{
		this.answerTime = answerTime;
	}
	
	public int getAnswerOrder()
	{
		return this.answerOrder;
	}
	
	public void setAnswerOrder(int answerOrder)
	{
		this.answerOrder = answerOrder;
	}
	
}
