package pw.flyshit.ClassOnline.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

/* 课堂问题实体类 */
@Entity
@Table(name = "Question")
public class Question
{
	@Id
	@Column(length = 32)
	private String questionId; //问题ID编号
	@Column(length = 16)
	private long createTime; //问题创建时间
	@Column(length = 64)
	private String questionTitle; //问题
	@Column(length = 1)
	private int questionType; //问题类型,0为单选，1为多选，2为短问答
	@Column(length = 4)
	private int questionScore; //问题分值
	@Column(length = 64)
	private String rightAnswer; //问题正确答案
	@Column(length = 64)
	private String option1; //答案选项1
	@Column(length = 64)
	private String option2; //答案选项2
	@Column(length = 64)
	private String option3; //答案选项3
	@Column(length = 64)
	private String option4; //答案选项4
	@Column(length = 64)
	private String option5; //答案选项5
	@Column(length = 64)
	private String option6; //答案选项6
	
	public Question()
	{
		
	}
	/* setter和getter */

	public String getQuestionId() 
	{
		return questionId;
	}

	public void setQuestionId(String questionId) 
	{
		this.questionId = questionId;
	}

	public long getCreateTime() 
	{
		return createTime;
	}

	public void setCreateTime(long createTime) 
	{
		this.createTime = createTime;
	}

	public String getQuestionTitle() 
	{
		return questionTitle;
	}

	public void setQuestionTitle(String questionTitle) 
	{
		this.questionTitle = questionTitle;
	}

	public int getQuestionType() 
	{
		return questionType;
	}

	public void setQuestionType(int questionType) 
	{
		this.questionType = questionType;
	}

	public int getQuestionScore() 
	{
		return questionScore;
	}

	public void setQuestionScore(int questionScore) 
	{
		this.questionScore = questionScore;
	}

	public String getRightAnswer() 
	{
		return rightAnswer;
	}

	public void setRightAnswer(String rightAnswer) 
	{
		this.rightAnswer = rightAnswer;
	}

	public String getOption1() 
	{
		return option1;
	}

	public void setOption1(String option1) 
	{
		this.option1 = option1;
	}

	public String getOption2() 
	{
		return option2;
	}

	public void setOption2(String option2) 
	{
		this.option2 = option2;
	}

	public String getOption3() 
	{
		return option3;
	}

	public void setOption3(String option3) 
	{
		this.option3 = option3;
	}

	public String getOption4() 
	{
		return option4;
	}

	public void setOption4(String option4) 
	{
		this.option4 = option4;
	}

	public String getOption5() 
	{
		return option5;
	}

	public void setOption5(String option5) 
	{
		this.option5 = option5;
	}

	public String getOption6() 
	{
		return option6;
	}

	public void setOption6(String option6) 
	{
		this.option6 = option6;
	}
	
}
