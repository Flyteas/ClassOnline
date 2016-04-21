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
			<form class="form-horizontal" role="form">
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputEmail3">用户名</label>
					<div class="col-sm-7">
						<input class="form-control" id="inputEmail3" type="email" />
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputPassword3">密码</label>
					<div class="col-sm-7">
						<input class="form-control" id="inputPassword3" type="password" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<div class="checkbox">
							 <label><input type="checkbox" />记住账号</label>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						 <button class="btn btn-default" type="submit">登陆</button>
					</div>
				</div>
			</form>
			<div class="alert alert-success" role="alert">
        		<strong>登录成功</strong>
    		</div>
		</div>

	</div>
</div>

</body>
</html>