/*
 * 常用JS方法
 */
function manageSessionSubmit()
{
	if(!(document.getElementById("sessionId").value == "" || document.getElementById("sessionId").value == null))
	{
		document.getElementById("sessionForm").submit();		
	}
}

function classSelectSubmit()
{
	if(!(document.getElementById("courseClassId").value == "" || document.getElementById("courseClassId").value == null))
	{
		document.getElementById("courseClassForm").submit();		
	}
}

function manageCurrentSessionSubmit(url)
{
	location.href(url);
}

function timestampToDateStr(timestamp)
{
	var date = new Date(timestamp);
	var dateStr = date.toLocaleString();
	document.write(dateStr);
	return dateStr;
}

function delSessionSubmit(delUrl)
{
	if(confirm('确定删除会话?'))
		{
			location.href(delUrl);
		}
}

function delStuRegInfoSubmit(delUrl,stuName)
{
	if(confirm('确定重置学生 '+stuName+' 的注册信息?\n重置注册信息后学生可使用微信重新注册'))
	{
		location.href(delUrl);
	}
}

function delClassStuSubmit(delUrl,stuName)
{
	if(confirm('确定从班级删除学生 '+stuName+' ?'))
	{
		location.href(delUrl);
	}
}

function delClsSubmit(delUrl,clsId)
{
	if(confirm('确定删除教学班 '+clsId+' ?'))
	{
		location.href(delUrl);
	}
}

function stopSessionSubmit(stopUrl)
{
	if(confirm('确定停止会话?'))
		{
			location.href(stopUrl);
		}
}

function addSessionSubmit(addUrl)
{
	location.href(addUrl);
}

function formCollapseCtrl(quesCollapseId,optionsCollapseId,ansMutilCollapseId,ansSingleCollapseId,ansTextCollapseId,sessionTypeSelectedId,quesTypeSelectedId) //控制题目表单的显示与隐藏
{
	var quesCollapse = document.getElementById(quesCollapseId);
	var optionsCollapse = document.getElementById(optionsCollapseId);
	var ansMutilCollapse = document.getElementById(ansMutilCollapseId);
	var ansSingleCollapse = document.getElementById(ansSingleCollapseId);
	var ansTextCollapse = document.getElementById(ansTextCollapseId);
	var sessionTypeSelVal = document.getElementById(sessionTypeSelectedId).value;
	var quesTypeSelVal = document.getElementById(quesTypeSelectedId).value;
	if(sessionTypeSelVal == '2') //答题模式
	{
		quesCollapse.setAttribute("class", "collapse in");  //显示题目表单
		if(quesTypeSelVal == '0') //单选模式
		{
			optionsCollapse.setAttribute("class", "collapse in");  //显示选项表单
			ansMutilCollapse.setAttribute("class", "collapse");  //隐藏多选答案表单
			ansSingleCollapse.setAttribute("class", "collapse in");  //显示单选答案表单
			ansTextCollapse.setAttribute("class", "collapse"); //隐藏短回答答案表单
		}
		else if(quesTypeSelVal == '1') //多选模式
		{
			optionsCollapse.setAttribute("class", "collapse in");  //显示选项表单
			ansMutilCollapse.setAttribute("class", "collapse in");  //显示多选答案表单
			ansSingleCollapse.setAttribute("class", "collapse");  //隐藏单选答案表单
			ansTextCollapse.setAttribute("class", "collapse"); //隐藏短回答答案表单
		}
		else
		{
			optionsCollapse.setAttribute("class", "collapse");  //隐藏选项表单
			ansMutilCollapse.setAttribute("class", "collapse");  //显示多选答案表单
			ansSingleCollapse.setAttribute("class", "collapse");  //隐藏单选答案表单
			ansTextCollapse.setAttribute("class", "collapse in"); //隐藏短回答答案表单
		}
	}
	else //非答题模式
	{
		quesCollapse.setAttribute("class", "collapse");  //隐藏题目表单
		optionsCollapse.setAttribute("class", "collapse");  //隐藏选项表单
		ansMutilCollapse.setAttribute("class", "collapse");  //隐藏多选答案表单
		ansSingleCollapse.setAttribute("class", "collapse");  //隐藏单选答案表单
	}
}

function checkSessionForm() //检查AddSession表单
{
	var sessionTypeVal = document.getElementById("sessionType").value
	var sessionNameVal = document.getElementById("sessionName").value
	var startTimePickerVal = document.getElementById("startTimePicker").value
	var endTimePickerVal = document.getElementById("endTimePicker").value
	var questionTitleVal = document.getElementById("questionTitle").value
	
	if(sessionNameVal == "" || sessionNameVal == null)
	{
		alert("会话名称不能为空!");
		return false;
	}
	if(startTimePickerVal == "" || startTimePickerVal == null)
	{
		alert("开始时间不能为空!");
		return false;
	}
	if(endTimePickerVal == "" || endTimePickerVal == null)
	{
		alert("结束时间不能为空!");
		return false;
	}
	if(sessionTypeVal == 2) //答题模式
	{
		if(questionTitleVal == "" || questionTitleVal == null)
		{
			alert("题目详情不能为空!");
			return false;
		}
	}
	return true;
}

function checkPwdModify(form)
{
	if(form.oldPwd.value == "" || form.oldPwd.value == null)
	{
		alert("原密码不能为空");
		return false;
	}
	if(form.newPwd.value == "" || form.newPwd.value == null)
	{
		alert("新密码不能为空");
		return false;
	}
	if(form.newPwdConfirm.value == "" || form.newPwdConfirm.value == null)
	{
		alert("确认密码不能为空");
		return false;
	}
	if(form.oldPwd.value == form.newPwd.value)
	{
		alert("新密码不能与原密码相同");
		return false;
	}
	if(form.newPwd.value != form.newPwdConfirm.value)
	{
		alert("两次密码输入不一致");
		return false;
	}
	return true;
}

function checkClassStuAdd(form)
{
	if(form.stuId.value == null || form.stuId.value == "")
	{
		alert("学生学号不能为空");
		return false;
	}
	return true;
}

function checkTeacherAdd(form)
{
	if(form.techId.value == null || form.techId.value == "")
	{
		alert("教师ID不能为空");
		return false;
	}
	if(form.techRealName.value == null || form.techRealName.value == "")
	{
		alert("真实姓名不能为空");
		return false;
	}
	if(form.techPassword.value == null || form.techPassword.value == "")
	{
		alert("登陆密码不能为空");
		return false;
	}
	return true;
}

function checkTeacherModify(form)
{
	if(form.techRealName.value == null || form.techRealName.value == "")
	{
		alert("真实姓名不能为空");
		return false;
	}
	return true;
}

function modifyTeacherSubmit(modifyUrl)
{
	window.open(modifyUrl);
}

function delTeacherSubmit(delUrl,techName)
{
	if(confirm('确定删除教师 '+techName+' ?'))
	{
		location.href(delUrl);
	}
}

function delStuSubmit(delUrl,stuName)
{
	if(confirm('确定删除学生 '+stuName+' ?'))
	{
		location.href(delUrl);
	}
}

function checkStudentModify(form)
{
	if(form.stuName.value == null || form.stuName.value == "")
	{
		alert("学生姓名不能为空");
		return false;
	}
	return true;
}

function checkClassAdd(form)
{
	if(form.clsId.value == null || form.clsId.value == "")
	{
		alert("教学班ID不能为空");
		return false;
	}
	if(form.courseName.value == null || form.courseName.value == "")
	{
		alert("课程名称不能为空");
		return false;
	}
	if(form.teacherId.value == null || form.teacherId.value == "")
	{
		alert("任课教师ID不能为空");
		return false;
	}
	return true;
}

function checkClassModify(form)
{
	if(form.courseName.value == null || form.courseName.value == "")
	{
		alert("课程名称不能为空");
		return false;
	}
	if(form.teacherId.value == null || form.teacherId.value == "")
	{
		alert("任课教师ID不能为空");
		return false;
	}
	return true;
}