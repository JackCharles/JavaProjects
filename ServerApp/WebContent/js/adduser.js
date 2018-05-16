/**
 * 
 */


function AddUser()
{
	var node = document.getElementById("adduser");
	if(node.style.display=="none"||node.style.display=="")
		node.style.display="block";
	else
		node.style.display="none";
}

function check(f) 
{
	if(f.userid.value=='' || f.username.value==''||f.position.value=='')
	{
		alert("基本信息不完整，请检查！");
		return false;
	}
	else
		return confirm("请确认信息完整性？");
}

function AreYouSure()
{
	return confirm("你确定删除该用户？");
}