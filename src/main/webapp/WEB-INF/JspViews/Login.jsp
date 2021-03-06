﻿<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
   <title>登录</title>
   <link href="resources/css/bootstrap.min.css" rel="stylesheet">
   <script src="resources/js/jquery-1.12.2.min.js"></script>
   <script src="resources/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
	<div class="row clearfix">
		<div class="col-md-12 column">
			<ul class="nav nav-tabs">
				<li class="active">
					 <a href="#">登录</a>
				</li>
			</ul>
			<div class="page-header">
				<h1>
					<small>登录您的账号</small>
				</h1>
			</div>
		</div>
	</div>
	<div class="row clearfix">
		<div class="col-md-2 column">
		</div>
		<div class="col-md-5 column">
			<form class="form-horizontal" role="form" action="Login.do" method="post">
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputUsername">账号</label>
					<div class="col-sm-7">
						<input class="form-control" id="username" name="username" type="text" />
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputPassword">密码</label>
					<div class="col-sm-7">
						<input class="form-control" id="password" name="password" type="password" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						 <button class="btn btn-default" type="submit">登录</button>
					</div>
				</div>
			</form>
			<c:if test="${loginError != null}">
			<div class="alert alert-danger" role="alert" id="loginErrorAlert">
        		<strong>账号或密码错误</strong>
    		</div>
    		</c:if>
		</div>
	</div>
</div>

</body>
</html>