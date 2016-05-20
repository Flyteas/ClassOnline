<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
   <title>课堂应答系统</title>
   <link href="resources/css/bootstrap.min.css" rel="stylesheet">
   <script src="resources/js/jquery-1.12.2.min.js"></script>
   <script src="resources/js/bootstrap.min.js"></script>
   <script src="resources/js/common.js"></script>
</head>
<body>
<!--<script type="text/javascript">
function getSession()
{
	if(!(document.getElementById("courseClassId").value == "" || document.getElementById("courseClassId").value == null))
	{
		document.getElementById("courseClassForm").submit();		
	}
}
</script>-->

<div class="container">
	<div class="row clearfix">
		<div class="col-md-12 column">
			<nav class="navbar navbar-default navbar-inverse navbar-fixed-top" role="navigation">
				<div class="navbar-header">
					 <button class="navbar-toggle" type="button" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">管理<span class="sr-only"></span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></button> <a class="navbar-brand" href="index.jsp">主页</a>
				</div>
				
				<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
					<ul class="nav navbar-nav">
						<li class="active">
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
		<div class="col-md-4 column">
		</div>
		<div class="col-md-4 column">
			<form role="form" id="courseClassForm" action="SessionList.do" method="get">
				<div class="form-group">
      				<label for="name">教学班</label>
      				<select class="form-control" id="courseClassId" name="courseClassId">
         				<c:forEach var="courseClass" items="${courseClasses}">
							<option value="<c:out value="${courseClass.courseClassId}"/>"><c:out value="${courseClass.course.courseName}:${courseClass.courseClassId}"/></option>
						</c:forEach>
      				</select>
      			</div>
      		</form>
      		<div class="alert alert-danger <c:if test="${fn:length(courseClasses) != 0}">hidden</c:if>" role="alert" id="loginErrorAlert">
        		<strong>无教学班!</strong>
    		</div>
		</div>
		<div class="col-md-4 column">
		</div>
	</div>
	<div class="row clearfix">
		<div class="col-md-5 column">
		</div>
		<div class="col-md-2 column">
    		 <button type="button" class="btn btn-success btn-lg" onclick="getSessionSubmit()"><strong>&nbsp;&nbsp;选择教学班&nbsp;&nbsp;</strong></button>
		</div>
		<div class="col-md-5 column">
		</div>
	</div>
</div>
</body>
</html>