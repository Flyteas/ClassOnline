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
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>
							课程名称
						</th>
						<th>
							${courseClass.course.courseName}
						</th>
					</tr>
				</thead>
				<tbody>
					<tr class="success">
						<td>
							教学班
						</td>
						<td>
							${courseClass.courseClassId}
						</td>
					</tr>	
					<tr class="info">
						<td>
							当前会话
						</td>
						<td>
							<c:if test="${currentSession == null}">无</c:if>
							<c:if test="${currentSession != null}">
							${currentSession.sessionName}:${currentSession.lessonSessionId}
							</c:if>
						</td>
					</tr>	
				</tbody>
			</table>
			
			<form role="form" id="sessionForm" action="SessionState.do" method="get">
				<div class="form-group">
      				<label for="name">会话列表</label>
      				<select class="form-control" id="sessionId" name="sessionId">
         				<c:forEach var="lessonSession" items="${lessonSessions}">
							<option value="<c:out value="${lessonSession.lessonSessionId}"/>"><c:out value="${lessonSession.sessionName}:${lessonSession.lessonSessionId}"/></option>
						</c:forEach>
      				</select>
      			</div>
      		</form>
      		<c:if test="${fn:length(lessonSessions) == 0}">
      		<div class="alert alert-danger" role="alert" id="loginErrorAlert">
        		<strong>无会话!</strong>
    		</div>
    		</c:if>
		</div>
		<div class="col-md-4 column">
		</div>
	</div>
	<div class="row clearfix">
		<div class="col-md-4 column">
		</div>
		<div class="col-md-2 column">
    		<button type="button" class="btn btn-success btn-lg" onclick="manageSessionSubmit()"><strong>&nbsp;&nbsp;管理会话&nbsp;&nbsp;</strong></button>
		</div>
		<c:if test="${currentSession == null}">
		<div class="col-md-2 column">
			<button type="button" class="btn btn-success btn-lg" onclick="addSessionSubmit('SessionAdd.do?courseClassId=${courseClass.courseClassId}')"><strong>&nbsp;&nbsp;添加会话&nbsp;&nbsp;</strong></button>
		</div>
		</c:if>
		<c:if test="${currentSession != null}">
		<div class="col-md-2 column">
			<button type="button" class="btn btn-success btn-lg" onclick="manageCurrentSessionSubmit('SessionState.do?sessionId=${currentSession.lessonSessionId}')"><strong>&nbsp;&nbsp;当前会话&nbsp;&nbsp;</strong></button>
		</div>
		</c:if>
		<div class="col-md-4 column">
		</div>
	</div>
</div>
</body>
</html>