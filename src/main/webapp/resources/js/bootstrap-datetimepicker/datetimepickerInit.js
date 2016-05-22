/**
 * datetimepicker初始化设置
 */
$(function ()
{
	$('#startTimePicker').datetimepicker({
	    format: 'yyyy-mm-dd hh:ii:ss',
	    autoclose: true,
	    todayBtn: true,
	    minuteStep: 1,
	    startView: 'day',
	    language: 'zh-CN'
	});
	$('#endTimePicker').datetimepicker({
	    format: 'yyyy-mm-dd hh:ii:ss',
	    autoclose: true,
	    todayBtn: true,
	    minuteStep: 1,
	    startView: 'day',
	    language: 'zh-CN'
	});
})