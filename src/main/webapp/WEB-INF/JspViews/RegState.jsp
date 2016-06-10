<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
   <title>课堂应答系统</title>
   <link href="resources/css/bootstrap.min.css" rel="stylesheet">
   <script src="resources/js/jquery-1.12.2.min.js"></script>
   <script src="resources/js/bootstrap.min.js"></script>
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
						<c:if test="${user.techRole == '1'}" >
						<li class="active">
							 <a href="SessionManage.do">会话管理</a>
						</li>
						<li>
							 <a href="ClassManage.do">班级管理</a>
						</li>
						</c:if>
						<c:if test="${user.techRole == '0'}" >
						<li class="active">
							 <a href="SessionManageAdmin.do">会话管理</a>
						</li>
						<li>
							 <a href="ClassManageAdmin.do">班级管理</a>
						</li>
						<li>
							 <a href="StudentManage.do">学生管理</a>
						</li>
						<li>
							 <a href="TeacherManage.do">教师管理</a>
						</li>
						</c:if>
					</ul>
					<ul class="nav navbar-nav navbar-right">
						<li class="dropdown">
							 <a class="dropdown-toggle" href="#" data-toggle="dropdown">${user.techRealName}<strong class="caret"></strong></a>
							<ul class="dropdown-menu">
								<li>
									 <a href="UserInfo.do">个人资料</a>
								</li>
								<li>
									 <a href="PwdModify.do">修改密码</a>
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
		<div class="col-md-1 column">
		</div>
		<div class="col-md-10 column">
			<label for="TableName">会话信息</label><br>
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>
							会话名称
						</th>
						<th>
							${lessonSession.sessionName}
						</th>
					</tr>
				</thead>
				<tbody>
					<tr class="success">
						<td>
							会话编号
						</td>
						<td>
							${lessonSession.lessonSessionId}
						</td>
					</tr>
					<tr class="info">
						<td>
							会话模式
						</td>
						<td>
							<c:if test="${lessonSession.sessionType == '0'}">注册模式</c:if>
							<c:if test="${lessonSession.sessionType == '1'}">签到模式</c:if>
							<c:if test="${lessonSession.sessionType == '2'}">答题模式</c:if>
						</td>
					</tr>	
					<tr class="success">
						<td>
							会话状态
						</td>
						<td>
							<c:if test="${sessionState == '0'}">正在进行</c:if>
							<c:if test="${sessionState == '1'}">已结束</c:if>
							<c:if test="${sessionState == '2'}">未开始</c:if>
						</td>
					</tr>
					<tr class="info">
						<td>
							开始时间
						</td>
						<td>
							${BeginTimeStr}
						</td>
					</tr>
					<tr class="success">
						<td>
							结束时间
						</td>
						<td>
							${EndTimeStr}
						</td>
					</tr>	
					<tr class="info">
						<td>
							会话操作
						</td>
						<td>
							<c:if test="${sessionState == '0'}">
							<button type="button" class="btn btn-success btn" onclick="stopSessionSubmit('SessionStop.do?sessionId=${lessonSession.lessonSessionId}')">停止会话</button>
							</c:if>
							<button type="button" class="btn btn-success btn" onclick="delSessionSubmit('SessionDel.do?sessionId=${lessonSession.lessonSessionId}')">删除会话</button>
						</td>
					</tr>
				</tbody>
			</table>
			<br><br><br>
			<label for="TableName">已注册学生&nbsp;&nbsp;&nbsp;注册率: ${regRates}%</label><br>
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>
							学生学号
						</th>
						<th>
							学生姓名
						</th>
						<th>
							学生性别
						</th>
						<th>
							学生班级
						</th>
						<th>
							OpenId
						</th>
						<th>
							操作
						</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach var="regStu" items="${regStus}">
					<tr class="success">
						<td>
							<c:out value="${regStu.stuId}"/>
						</td>
						<td>
							<c:out value="${regStu.stuName}"/>
						</td>
						<td>
							<c:if test="${regStu.stuSex == 0}">男</c:if>
							<c:if test="${regStu.stuSex == 1}">女</c:if>
						</td>
						<td>
							<c:out value="${regStu.stuClass}"/>
						</td>
						<td>
							<c:out value="${regStu.stuWechatOpenId}"/>
						</td>
						<td>
							<button type="button" class="btn btn-success btn-xs" onclick="delStuRegInfoSubmit('RegInfoDelete.do?studentId=${regStu.stuId}&sessionId=${lessonSession.lessonSessionId}','${regStu.stuName}')">重置注册</button>
						</td>
					</tr>
					</c:forEach>	
				</tbody>
			</table>
			<br><br><br>
			<label for="TableName">未注册学生</label><br>
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>
							学生学号
						</th>
						<th>
							学生姓名
						</th>
						<th>
							学生性别
						</th>
						<th>
							学生班级
						</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach var="unregStu" items="${unregStus}">
					<tr class="success">
						<td>
							<c:out value="${unregStu.stuId}"/>
						</td>
						<td>
							<c:out value="${unregStu.stuName}"/>
						</td>
						<td>
							<c:if test="${unregStu.stuSex == 0}">男</c:if>
							<c:if test="${unregStu.stuSex == 1}">女</c:if>
						</td>
						<td>
							<c:out value="${unregStu.stuClass}"/>
						</td>
					</tr>
					</c:forEach>	
				</tbody>
			</table>
		</div>
		<div class="col-md-1 column">
		</div>
	</div>
</div>
</body>
</html>