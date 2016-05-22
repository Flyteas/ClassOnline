<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
   <title>课堂应答系统</title>
   <link href="resources/css/bootstrap.min.css" rel="stylesheet">
   <link href="resources/css/bootstrap-datetimepicker.min.css" rel="stylesheet">
   <script src="resources/js/jquery-1.12.2.min.js"></script>
   <script src="resources/js/bootstrap.min.js"></script>
   <script src="resources/js/bootstrap-datetimepicker/bootstrap-datetimepicker.min.js"></script>
   <script src="resources/js/bootstrap-datetimepicker/locales/bootstrap-datetimepicker.zh-CN.js"></script>
   <script src="resources/js/bootstrap-datetimepicker/datetimepickerInit.js"></script>
   <script src="resources/js/common.js"></script>
</head>
<body>
<div class="container">
	<div class="row clearfix">
		<div class="col-md-12 column">
			<nav class="navbar navbar-default navbar-inverse navbar-fixed-top" role="navigation">
				<div class="navbar-header">
					 <button class="navbar-toggle" type="button" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">管理<span class="sr-only"></span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></button> <a class="navbar-brand" href="index.jsp">主页</a>
				</div>
				
				<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
					<ul class="nav navbar-nav">
						<li>
							 <a href="SessionManage.do">会话管理</a>
						</li>
						<li>
							 <a href="#">学生管理</a>
						</li>
						<li>
							 <a href="#">班级管理</a>
						</li>
						<li>
							 <a href="#">教师管理</a>
						</li>
					</ul>
					<ul class="nav navbar-nav navbar-right">
						<li class="dropdown">
							 <a class="dropdown-toggle" href="#" data-toggle="dropdown">${user.techRealName}<strong class="caret"></strong></a>
							<ul class="dropdown-menu">
								<li>
									 <a href="#">个人资料</a>
								</li>
								<li>
									 <a href="#">修改密码</a>
								</li>
								<li class="divider">
								</li>
								<li>
									 <a href="Logout.do">注销</a>
								</li>
							</ul>
						</li>
					</ul>
				</div>
			</nav>
		</div>
	</div>
	<br><br><br><br>
	<div class="row clearfix">
		<div class="col-md-3 column">
		</div>
		<div class="col-md-6 column">
			<form class="form-horizontal" role="form" action="SessionStart.do" method="post" onsubmit="return checkSessionForm()">
				<div class="form-group">
					<div class="col-sm-9">
						<input class="hidden"  id="courseClassId" name="courseClassId" value="${courseClassId}"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label" for="inputSessionname">会话名称</label>
					<div class="col-sm-9">
						<textarea class="form-control" rows="2" id="sessionName" name="sessionName"></textarea>
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputSessionType">会话模式</label>
					<div class="col-sm-9">
						<select class="form-control" id="sessionType" name="sessionType" onchange="formCollapseCtrl('questionCollapse','optionsCollapse','rightMutilAnsCollapse','rightSingleAnsCollapse','rightTextAnsCollapse','sessionType','questionType')">
							<option value="0">注册模式</option>
							<option value="1">签到模式</option>
							<option value="2">答题模式</option>
						</select>
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputBeginTime">开始时间</label>
					<div class="col-sm-9">
						<input class="form-control" id="startTimePicker" name="beginTime" readonly/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label" for="inputEndTime">结束时间</label>
					<div class="col-sm-9">
						<input class="form-control" id="endTimePicker" name="endTime" readonly/>
					</div>
				</div>
				<div id="questionCollapse" class="collapse">
					<br><br>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="inputQuestionTitle">题目详情</label>
						<div class="col-sm-9">
							<textarea class="form-control" rows="3" id="questionTitle" name="questionTitle"></textarea>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="inputQuestionTitle">题目类型</label>
						<div class="col-sm-9">
							<select class="form-control" id="questionType" name="questionType" onchange="formCollapseCtrl('questionCollapse','optionsCollapse','rightMutilAnsCollapse','rightSingleAnsCollapse','rightTextAnsCollapse','sessionType','questionType')">
								<option value="0">单选</option>
								<option value="1">多选</option>
								<option value="2">问答</option>
							</select>
						</div>
					</div>
				</div>
				<div id="optionsCollapse" class="collapse">
					<div class="form-group">
						<label class="col-sm-2 control-label" for="inputOptionA">选项A</label>
						<div class="col-sm-9">
							<input class="form-control" id="optionA" name="optionA"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="inputOptionB">选项B</label>
						<div class="col-sm-9">
							<input class="form-control" id="optionB" name="optionB"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="inputOptionC">选项C</label>
						<div class="col-sm-9">
							<input class="form-control" id="optionC" name="optionC"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="inputOptionD">选项D</label>
						<div class="col-sm-9">
							<input class="form-control" id="optionD" name="optionD"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="inputOptionE">选项E</label>
						<div class="col-sm-9">
							<input class="form-control" id="optionE" name="optionE"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="inputOptionF">选项F</label>
						<div class="col-sm-9">
							<input class="form-control" id="optionF" name="optionF"/>
						</div>
					</div>
				</div>
				<div id="rightMutilAnsCollapse" class="collapse">
					<div class="form-group">
					<label class="col-sm-2 control-label" for="inputRightAnswer">正确答案</label>
						&nbsp;&nbsp;&nbsp;
						<label class="checkbox-inline">
							<input type="checkbox" id="optionACheckbox" name="optionARightFlag" value="1"> 选项A
						</label>
						<label class="checkbox-inline">
							<input type="checkbox" id="optionBCheckbox" name="optionBRightFlag" value="1"> 选项B
						</label>
						<label class="checkbox-inline">
							<input type="checkbox" id="optionCCheckbox" name="optionCRightFlag" value="1"> 选项C
						</label>
						<label class="checkbox-inline">
							<input type="checkbox" id="optionDCheckbox" name="optionDRightFlag" value="1"> 选项D
						</label>
						<label class="checkbox-inline">
							<input type="checkbox" id="optionECheckbox" name="optionERightFlag" value="1"> 选项E
						</label>
						<label class="checkbox-inline">
							<input type="checkbox" id="optionFCheckbox" name="optionFRightFlag" value="1"> 选项F
						</label>
					</div>	
				</div>
				<div id="rightSingleAnsCollapse" class="collapse">
					<div class="form-group">
					<label class="col-sm-2 control-label" for="inputRightAnswer">正确答案</label>
						&nbsp;&nbsp;&nbsp;
						<label class="radio-inline">
							<input type="radio" id="optionARadio" name="rightSingleAns" value="1"> 选项A
						</label>
						<label class="radio-inline">
							<input type="radio" id="optionBRadio" name="rightSingleAns" value="2"> 选项B
						</label>
						<label class="radio-inline">
							<input type="radio" id="optionVRadio" name="rightSingleAns" value="3"> 选项C
						</label>
						<label class="radio-inline">
							<input type="radio" id="optionDRadio" name="rightSingleAns" value="4"> 选项D
						</label>
						<label class="radio-inline">
							<input type="radio" id="optionERadio" name="rightSingleAns" value="5"> 选项E
						</label>
						<label class="radio-inline">
							<input type="radio" id="optionFRadio" name="rightSingleAns" value="6"> 选项F
						</label>
					</div>	
				</div>
				<div id="rightTextAnsCollapse" class="collapse">
					<div class="form-group">
					<label class="col-sm-2 control-label" for="inputRightAnswer">正确答案</label>
					<div class="col-sm-9">
						<textarea class="form-control" rows="3" id="rightTextAns" name="rightTextAns"></textarea>
					</div>
					</div>
				</div>
				<br>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						 <button class="btn btn-success btn-lg" type="submit">创建会话</button>
					</div>
				</div>
			</form>
		</div>
		<div class="col-md-3 column">
		</div>
	</div>
</div>
</body>
</html>