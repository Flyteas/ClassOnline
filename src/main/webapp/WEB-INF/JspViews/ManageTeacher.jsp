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
						<li>
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
						<li>
							 <a href="StudentManage.do">学生管理</a>
						</li>
						<li class="active">
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
			<form class="form-horizontal" role="form" action="TeacherManage.do" method="post">
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputTechIdOrName">ID或姓名</label>
					<div class="col-sm-7">
						<input class="form-control" id="techIdOrName" name="techIdOrName" type="text" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						&nbsp;&nbsp;&nbsp;
						 <button class="btn btn-success btn-lg" type="submit">搜索教师</button>
						 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						 <button class="btn btn-success btn-lg" type="button" onclick="window.open('TeacherAdd.do')">添加教师</button>
					</div>
				</div>
			</form>
			<c:if test="${msg == '0'}">
      		<div class="alert alert-success col-sm-10" role="alert" id="addSuccessAlert">
        		<strong>删除成功</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '1'}">
      		<div class="alert alert-danger col-sm-10" role="alert" id="delFailAlert">
        		<strong>删除失败</strong>
    		</div>
    		</c:if>
		</div>
	</div>
	<br><br><br>
	<div class="row clearfix">
		<div class="col-md-1 column">
		</div>
		<div class="col-md-10 column">
			<label for="TableName">教师列表</label><br>
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>
							教师ID
						</th>
						<th>
							教师姓名
						</th>
						<th>
							教师性别
						</th>
						<th>
							教师角色
						</th>
						<th>
							操作
						</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach var="teacher" items="${teachers}">
					<tr class="success">
						<td>
							<c:out value="${teacher.techId}"/>
						</td>
						<td>
							<c:out value="${teacher.techRealName}"/>
						</td>
						<td>
							<c:if test="${teacher.techSex == '0'}">男</c:if>
							<c:if test="${teacher.techSex == '1'}">女</c:if>
						</td>
						<td>
							<c:if test="${teacher.techRole == '0'}">管理员</c:if>
							<c:if test="${teacher.techRole == '1'}">教师</c:if>
						</td>
						<td>
							<button type="button" class="btn btn-success btn-xs" onclick="modifyTeacherSubmit('TeacherModify.do?techId=${teacher.techId}')">修改资料</button>
							<button type="button" class="btn btn-success btn-xs" onclick="delTeacherSubmit('TeacherDel.do?techId=${teacher.techId}','${teacher.techRealName}')">删除教师</button>
						</td>
					</tr>
					</c:forEach>	
				</tbody>
			</table>
      		<c:if test="${teachers != null && fn:length(teachers) == 0}">
      		<div class="alert alert-danger" role="alert" id="noTeacherAlert">
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