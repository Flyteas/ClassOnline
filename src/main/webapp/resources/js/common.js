/*
 * 常用JS方法
 */

function getSessionSubmit()
{
	if(!(document.getElementById("courseClassId").value == "" || document.getElementById("courseClassId").value == null))
	{
		document.getElementById("courseClassForm").submit();		
	}
}

function timestampToDateStr(timestamp)
{
	var date = new Date(timestamp);
	var dateStr = date.toLocaleString();
	document.write(dateStr);
	return dateStr;
}

function delSessionSubmit(delUrl)
{
	if(confirm('确定删除会话?'))
		{
			location.href(delUrl);
		}
}

function stopSessionSubmit(stopUrl)
{
	if(confirm('确定停止会话?'))
		{
			location.href(stopUrl);
		}
}