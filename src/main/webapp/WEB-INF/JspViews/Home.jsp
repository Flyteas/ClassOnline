<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
   <title>课堂应答系统</title>
   <link href="resources/css/bootstrap.min.css" rel="stylesheet">
   <script src="resources/js/jquery-1.12.2.min.js"></script>
   <script src="resources/js/bootstrap.min.js"></script>
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
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>
							当前用户
						</th>
						<th>
							${user.techRealName}
						</th>
					</tr>
				</thead>
				<tbody>
					<tr class="success">
						<td>
							上次登陆时间
						</td>
						<td>
							${lastLoginDateStr}
						</td>
					</tr>	
					<tr class="info">
						<td>
							上次登陆IP
						</td>
						<td>
							${user.techLastLoginIP}
						</td>
					</tr>	
				</tbody>
			</table>
		</div>
		<div class="col-md-3 column">
		</div>
	</div>
</div>
</body>
</html>