
//upload check
function CheckAndSetInfo()
{
	var f = document.getElementById("form1");
	if (f.party.value == '')
	{
		alert("错误：当事人未填写！");
		return false;
	}
	if (f.Type.value == '#')
	{
		alert("错误：未选择案件类型！");
		return false;
	}
	if(f.trno.value!="")
	{
		if(!/(\d{4})/.test(f.tyear.value))
		{
			alert("案卷号年份不合法！");
			return false;
		}
		if(!/^\d+[-]{0,1}\d+$/.test(f.trno.value))
		{
			alert("案卷号号码部分不合法！");
			return false;
		}
		f.RNo.value="（"+f.tyear.value+"）"+"吉长国安证 "+f.ttype.value+" 字第"+f.trno.value+"号";
	}
	
	if (confirm("请再次确认：\n1、表单填写是否正确。\n2、文件选择是否正确。\n3、上传后无法修改，补传！"))
	{
		swfu.setPostParams(
		{
			"handler1": encodeURIComponent(f.Handler1.value),
			"date": f.Date.value,
			"party": encodeURIComponent(f.party.value),
			"type": encodeURIComponent(f.Type.value),
			"subtype": encodeURIComponent(f.SubType.value)
		}
		);
		swfu.startUpload();
	}
}

//AddParty
function AddParty()
{
	var node = document.getElementById("PartyBox");
	var d = dialog({
	    title: '添加当事人信息',
	    content: '姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：'+
	    '<input type="text" id="partyname"/><br/>'+
	    '身份证号：<input type="text" id="partyid"/>',
	    statusbar:'无',
	    button:[
	    	{
	    		value:'添加当事人',
	    		callback: function()
	    		{
	    			var reg = /^(\d{14}|\d{17})(\d|[xX])$/;
	    			var ptname = document.getElementById("partyname").value;
	    			var ptid = document.getElementById("partyid").value;
	    			if(ptname=='')
	    			{
	    				this.statusbar("请填写姓名");
	    				return false;
	    			}
	    			if(!reg.test(ptid))
	    			{
	    				this.statusbar("身份证号不合法");
	    				return false;
	    			}
	    			else
	    			{
	    				node.value=node.value+ptname+'@'+ptid+'\r\n';
	    				return true;
	    			}
	    		},
	    		autofocus:true
	    	},
	    	{
	    		value:'取消添加'
	    	}
	    ]
	});
	d.showModal();
}

//ClearParty
function ClearParty()
{
	if (confirm("你确定要清空所有当事人信息？"))
		document.getElementById("PartyBox").value = "";
}


function ShowSubType(f)
{
	var parent = document.getElementById("subtypearea");
	var children = parent.children;
	for(var i=0;i<children.length;++i)
		children[i].style.display = "none";
	
	if(f.value=="民事")
	{
		var nodes = document.getElementsByClassName("ms");
		for(var i=0;i<nodes.length;++i)
			nodes[i].style.display="block";
		nodes[0].selected = "true";
	}
	else if(f.value=="经济")
	{
		var nodes = document.getElementsByClassName("jj");
		for(var i=0;i<nodes.length;++i)
			nodes[i].style.display="block";
		nodes[0].selected = "true";
	}
	else if(f.value=="涉外")
	{
		var nodes = document.getElementsByClassName("sw");
		for(var i=0;i<nodes.length;++i)
			nodes[i].style.display="block";
		nodes[0].selected = "true";
	}
	else if(f.value=="其它")
	{
		var nodes = document.getElementsByClassName("other");
		for(var i=0;i<nodes.length;++i)
			nodes[i].style.display="block";
		nodes[0].selected = "true";
	}
	else
	{
		var t = document.getElementById("idle");
		t.style.display = "block";
		t.selected = "true";
	}
}