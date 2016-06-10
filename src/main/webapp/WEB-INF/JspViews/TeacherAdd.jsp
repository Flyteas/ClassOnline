<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
			<form class="form-horizontal" role="form" action="TeacherAdd.do" onsubmit="return checkTeacherAdd(this)" method="post">
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputTechId">教师ID</label>
					<div class="col-sm-7">
						<input class="form-control" id="techId" name="techId" type="text" />
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputRealName">真实姓名</label>
					<div class="col-sm-7">
						<input class="form-control" id="techRealName" name="techRealName" type="text" />
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputSex">教师性别</label>
					<div class="col-sm-7">
						<select class="form-control" id="techSex" name="techSex" >
							<option value="0">男</option>
							<option value="1">女</option>
						</select>	
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputRole">教师角色</label>
					<div class="col-sm-7">
						<select class="form-control" id="techRole" name="techRole" >
							<option value="0">管理员</option>
							<option value="1">教师</option>
						</select>	
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputPhone">联系电话</label>
					<div class="col-sm-7">
						<input class="form-control" id="techPhoneNum" name="techPhoneNum" type="text" />
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputPwd">登陆密码</label>
					<div class="col-sm-7">
						<input class="form-control" id="techPassword" name="techPassword" type="password" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-4 col-sm-10">
						 <button class="btn btn-success btn-lg" type="submit">添加教师</button>
					</div>
				</div>
			</form>
			<c:if test="${msg == '1'}">
			<div class="alert alert-danger" role="alert" id="addResultAlert">
        		<strong>教师ID不能为空   添加失败</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '2'}">
			<div class="alert alert-danger" role="alert" id="addResultAlert">
        		<strong>教师ID已存在   添加失败</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '3'}">
			<div class="alert alert-danger" role="alert" id="addResultAlert">
        		<strong>添加失败</strong>
    		</div>
    		</c:if>
		</div>
	</div>
</div>

</body>
</html>