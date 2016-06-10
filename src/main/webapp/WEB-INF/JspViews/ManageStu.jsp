<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
						<li>
							 <a href="SessionManage.do">会话管理</a>
						</li>
						<li class="active">
							 <a href="ClassManage.do">班级管理</a>
						</li>
						</c:if>
						<c:if test="${user.techRole == '0'}" >
						<li>
							 <a href="SessionManageAdmin.do">会话管理</a>
						</li>
						<li>
							 <a href="ClassManageAdmin.do">班级管理</a>
						</li>
						<li class="active">
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
		<div class="col-md-3 column">
		</div>
		<div class="col-md-6 column">
			<form class="form-horizontal" role="form" action="StudentManage.do" method="post">
				<div class="form-group">
					 <label class="col-sm-3 control-label" for="inputIdOrName">学号或姓名</label>
					<div class="col-sm-6">
						<input class="form-control" id="idOrName" name="idOrName" type="text" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-3 col-sm-10">
						 <button class="btn btn-success btn-lg" type="submit">搜索学生</button>
						 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						 <button class="btn btn-success btn-lg" type="button" onclick="window.open('StudentAdd.do')">添加学生</button>
					</div>
				</div>
			</form>
			<c:if test="${msg == '0'}">
      		<div class="alert alert-success col-sm-10" role="alert" id="delSuccessAlert">
        		<strong>删除成功</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '1'}">
      		<div class="alert alert-danger col-sm-10" role="alert" id="delFailAlert">
        		<strong>删除失败</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '2'}">
      		<div class="alert alert-success col-sm-10" role="alert" id="resetSuccessAlert">
        		<strong>重置注册成功</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '3'}">
      		<div class="alert alert-danger col-sm-10" role="alert" id="resetFailAlert">
        		<strong>重置注册失败</strong>
    		</div>
    		</c:if>
		</div>
	</div>
	<br><br><br>
	<div class="row clearfix">
		<div class="col-md-1 column">
		</div>
		<div class="col-md-10 column">
			<label for="TableName">学生列表</label><br>
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
				<c:forEach var="stu" items="${stus}">
					<tr class="success">
						<td>
							<c:out value="${stu.stuId}"/>
						</td>
						<td>
							<c:out value="${stu.stuName}"/>
						</td>
						<td>
							<c:if test="${stu.stuSex == '0'}">男</c:if>
							<c:if test="${stu.stuSex == '1'}">女</c:if>
						</td>
						<td>
							<c:out value="${stu.stuClass}" />
						</td>
						<td>
							<c:out value="${stu.stuWechatOpenId}" />
						</td>
						<td>
							<button type="button" class="btn btn-success btn-xs" onclick="window.open('StudentModify.do?stuId=${stu.stuId}')">修改资料</button>
							<button type="button" class="btn btn-success btn-xs" onclick="delStuRegInfoSubmit('RegInfoDeleteAdmin.do?stuId=${stu.stuId}','${stu.stuName}')">重置注册</button>
							<button type="button" class="btn btn-success btn-xs" onclick="delStuSubmit('StudentDel.do?stuId=${stu.stuId}','${stu.stuName}')">删除学生</button>
						</td>
					</tr>
					</c:forEach>	
				</tbody>
			</table>
      		<c:if test="${stus != null && fn:length(stus) == 0}">
      		<div class="alert alert-danger" role="alert" id="noStuAlert">
        		<strong>搜索结果为空</strong>
    		</div>
    		</c:if>
		</div>
		<div class="col-md-1 column">
		</div>
	</div>
</div>
</body>
</html>