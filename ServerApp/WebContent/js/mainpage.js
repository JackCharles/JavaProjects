function CheckPasswd(form)
{
	var len = form.OldPasswd.value.length;
	var len1 = form.NewPasswd1.value.length;
	if (len == 0 || form.NewPasswd1.value != form.NewPasswd2.value || len1 < 6)
	{
		alert("输入不能为空，新密码两次输入要一致，且新密码不少于6位");
		return false;
	}
	return true;
}

//获取所有节点然后隐藏
var searchnode, uploadnode, selfinfonode, viewallnode, statinode, sysadminode;
function GetAllNodeAndHidden()
{
	document.getElementById("welcom-page").style.display="none";
	searchnode = document.getElementById("search-event");
	uploadnode = document.getElementById("upload-event");
	selfinfonode = document.getElementById("selfinfo-modify");
	viewallnode = document.getElementById("viewall-event");
	statinode = document.getElementById("statistic");
	sysadminode = document.getElementById("system-admin");

	uploadnode.style.display = "none";
	selfinfonode.style.display = "none";
	searchnode.style.display = "none";
	viewallnode.style.display = "none";
	statinode.style.display = "none";
	sysadminode.style.display = "none";
}

function SearchEvent()
{
	GetAllNodeAndHidden();
	searchnode.style.display = "block";
	searchnode.innerHTML = "<iframe src=SearchEvents.jsp width=100% height=800px frameborder=0>你的浏览器不支持iframe</iframe>";
}

function SelfInfoModify()
{
	GetAllNodeAndHidden();
	selfinfonode.style.display = "block";
	selfinfonode.innerHTML = "<iframe src=SelfModify.jsp width=100% height=800px frameborder=0>你的浏览器不支持iframe</iframe>";
}

function UploadEvent()
{
	GetAllNodeAndHidden();
	uploadnode.style.display = "block";
	uploadnode.innerHTML = "<iframe src=UploadEvent.jsp width=100% height=800px frameborder=0>你的浏览器不支持iframe</iframe>";
}

function ViewAllEvent()
{
	GetAllNodeAndHidden();
	viewallnode.style.display = "block";
	viewallnode.innerHTML = "<iframe src=ViewAll.jsp?type="+encodeURIComponent("全部")+"&page=1 width=100% height=800px frameborder=0>你的浏览器不支持iframe</iframe>";
}

function Statistic()
{
	GetAllNodeAndHidden();
	statinode.style.display = "block";
	statinode.innerHTML = "<iframe src=Statistic.jsp width=100% height=800px frameborder=0>你的浏览器不支持iframe</iframe>";
}

function UserManager()
{
	GetAllNodeAndHidden();
	sysadminode.style.display = "block";
	sysadminode.innerHTML = "<iframe src=UserManager.jsp width=100% height=800px frameborder=0>你的浏览器不支持iframe</iframe>";
}

function TypeManager()
{
	GetAllNodeAndHidden();
	sysadminode.style.display = "block";
	sysadminode.innerHTML = "<iframe src=TypeManager.jsp width=100% height=800px frameborder=0>你的浏览器不支持iframe</iframe>";
}

function ViewMS()
{
	GetAllNodeAndHidden();
	sysadminode.style.display = "block";
	sysadminode.innerHTML = "<iframe src=ViewAll.jsp?type="+encodeURIComponent("民事")+"&page=1 width=100% height=800px frameborder=0>你的浏览器不支持iframe</iframe>";
}

function ViewJJ()
{
	GetAllNodeAndHidden();
	sysadminode.style.display = "block";
	sysadminode.innerHTML = "<iframe src=ViewAll.jsp?type="+encodeURIComponent("经济")+"&page=1 width=100% height=800px frameborder=0>你的浏览器不支持iframe</iframe>";
}

function ViewSW()
{
	GetAllNodeAndHidden();
	sysadminode.style.display = "block";
	sysadminode.innerHTML = "<iframe src=ViewAll.jsp?type="+encodeURIComponent("涉外")+"&page=1 width=100% height=800px frameborder=0>你的浏览器不支持iframe</iframe>";
}

function ViewOther()
{
	GetAllNodeAndHidden();
	sysadminode.style.display = "block";
	sysadminode.innerHTML = "<iframe src=ViewAll.jsp?type="+encodeURIComponent("其它")+"&page=1 width=100% height=800px frameborder=0>你的浏览器不支持iframe</iframe>";
}

function ViewConfirm()
{
	GetAllNodeAndHidden();
	sysadminode.style.display = "block";
	sysadminode.innerHTML = "<iframe src=ViewAll.jsp?type=Y&page=1 width=100% height=800px frameborder=0>你的浏览器不支持iframe</iframe>";
}

function ViewNotConf()
{
	GetAllNodeAndHidden();
	sysadminode.style.display = "block";
	sysadminode.innerHTML = "<iframe src=ViewAll.jsp?type=N&page=1 width=100% height=800px frameborder=0>你的浏览器不支持iframe</iframe>";
}

function ShowOptLogs()
{
	GetAllNodeAndHidden();
	sysadminode.style.display = "block";
	sysadminode.innerHTML = "<iframe src=ViewAllLog.jsp?type="+encodeURIComponent("全部事件")+"&page=1 width=100% height=800px frameborder=0>你的浏览器不支持iframe</iframe>";
}

function BackupDatabase()
{
	if(!confirm("数据库应定期备份，但过于频繁的备份会造成磁盘空间浪费，\n你确定要继续备份？"))
		return false;
	GetAllNodeAndHidden();
	sysadminode.style.display = "block";
	sysadminode.innerHTML = "<iframe src=BackupDatabase width=100% height=800px frameborder=0>你的浏览器不支持iframe</iframe>";
}

function AboutSystem()
{
	GetAllNodeAndHidden();
	sysadminode.style.display = "block";
	sysadminode.innerHTML = "<iframe src=AboutSystem.jsp width=100% height=800px frameborder=0>你的浏览器不支持iframe</iframe>";
}