var host = window.location.protocol + "//" + window.location.host;
var userName=parent.eXo.env.portal.userName;
var rest=parent.eXo.env.portal.rest;

init=function(){
 
  getXhr();
    xhr.onreadystatechange = function()
    {
    if(xhr.readyState == 4 && xhr.status == 200)
     {
		getList(xhr);
     }
	};
	
  serviceUrl =host+  "/" + rest + "/BonitaService/sendList?ServiceUrl=/bonita-server-rest/API/queryRuntimeAPI/getTaskListByUserIdAndActivityState/"+userName+"/READY";
  xhr.open("GET",serviceUrl, true);  
  xhr.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
  xhr.send();
  return false;
};
 
getXhr=function (){
	
	if(window.XMLHttpRequest){
		xhr = new XMLHttpRequest();
	} else if(window.ActiveXObject){
		try {
			xhr = new ActiveXObject("Msxml2.XMLHTTP");
		}catch (e){
			xhr = new ActiveXObject("Microsoft.XMLHTTP");
		}
	}else{
		alert("Votre navigateur ne supporte pas les objets XMLHTTPRequest, veuillez le mettre à jour");
		xhr = false;
	}
};

getList=function(xhr){
 
    var xml= xhr.responseXML;
    var baseURL = host + "/portal/intranet/workflow?url=";
    var urlXp = encodeURIComponent(host+"/bonita-todo/console/BonitaConsole.html?#CaseList/lab:Inbox");
	//document.getElementById('more').innerHTML ="<a id='More' href='/portal/private/intranet/bonitaTODO' target='_parent' class='IconDropDown'>"+eXo.social.Locale.getMsg('more_link_label')+"</a><div class='ContTit'>"+eXo.social.Locale.getMsg('my_todos')+"</div>";
	var activityinstance=xml.getElementsByTagName('ActivityInstance');
    var str2='';
	var str0='';
	var str1='';
	if(activityinstance.length!=0){
		for (i=0 ; i<activityinstance.length ; i++)	{

		  var activityDefinitionUUID=activityinstance[i].getElementsByTagName('rootInstanceUUID');
		  var instanceUUID=activityinstance[i].getElementsByTagName('instanceUUID')[0].getElementsByTagName('value')[0].textContent;                            
		  var label=activityinstance[i].getElementsByTagName('label')[0].textContent;
		  var priority=activityinstance[i].getElementsByTagName('priority')[0].textContent;
		  var activityDefinitionUUIDvalue=activityDefinitionUUID[0].getElementsByTagName('value')[0].textContent;
		  var uuid=  activityinstance[i].getElementsByTagName('uuid')[0].getElementsByTagName('value')[0].textContent;
		  var tab=activityDefinitionUUIDvalue.split("--");
		  var process='';
		  var doclink;
		  var path='';
		  if(tab[0]=="PublicationProcess"){
			  doclink =activityinstance[i].getElementsByTagName('instanceUUID')[0].getElementsByTagName('doclink')[0].textContent;   
			  var titledoc=doclink .split("/");
			  var title=titledoc[titledoc.length-1];   
			  //process=tab[2]+" | "+label+" : \""+title+"\"";
			  process=label+" : \""+title+"\"";
			  doclink=doclink.substring(11);
			  path="&path="+doclink;
		  }else{
			 //process=tab[0]+"-"+tab[2]+" : "+label;
			 process=label;
		  }   
		  var url=encodeURIComponent(host+"/bonita/console/BonitaConsole.html?task="+uuid+"&"+"bonitaLocale="+ "en&mode=form");
		  if(priority==0){
		  str0+="<div><a id='Task' class='link_"+priority+"' href='"+ baseURL +url+"'"+ "target='_parent'>"+process+"</a></div>";
		 }else{
		 if(priority==1){
		  str1+="<div><a id='Task' class='link_"+priority+"' href='"+ baseURL +url+"'"+ "target='_parent'>"+process+"</a></div>";
		 }else{
		  str2+="<div><a id='Task' class='link_"+priority+"' href='"+ baseURL +url+"'"+ "target='_parent'>"+process+"</a></div>";
		 }
		 }
		}
       if(str0!="" && str1=="" && str2==""){
		document.getElementById('TodosList').innerHTML=str0;
		}else{
		if(str0!="" ){
		document.getElementById('TodosList').innerHTML += "<div class='menu' id='menu1' onclick='afficheMenu(this)' ><div class='ArrowIcon'><img alt='' src='/exo-gadget-resources/skin/exo-gadget/images/ArrowRight.gif' class='listIcon off CustomImage'></div><a href='#'>"+eXo.social.Locale.getMsg('taskminor')+"</a></div><div id='sousmenu1' style='display:none'>"+str0+"</div>";
		}
		}
		if(str1!="" && str0=="" && str2==""){
		document.getElementById('TodosList').innerHTML=str1;
		}else{
		if(str1!="" ){
		document.getElementById('TodosList').innerHTML += "<div class='menu' id='menu2' onclick='afficheMenu(this)' ><div class='ArrowIcon'><img alt='' src='/exo-gadget-resources/skin/exo-gadget/images/ArrowRight.gif' class='listIcon off CustomImage'></div><a href='#'>"+eXo.social.Locale.getMsg('taskmajor')+"</a></div><div id='sousmenu2' style='display:none'>"+str1+"</div>";
		}}
		if(str2!=""&& str0=="" && str1==""){
		document.getElementById('TodosList').innerHTML=str2;
		}else{
		if(str2!="" ){
		document.getElementById('TodosList').innerHTML +=  "<div class='menu' id='menu3' onclick='afficheMenu(this)' ><div class='ArrowIcon'><img alt='' src='/exo-gadget-resources/skin/exo-gadget/images/ArrowRight.gif' class='listIcon off CustomImage'></div><a href='#'>"+eXo.social.Locale.getMsg('taskblocker')+"</a></div><div id='sousmenu3' style='display:none'>"+str2+"</div>";
		}}
	}else{
		document.getElementById('TodosList').innerHTML="<div class='light_message'>"+eXo.social.Locale.getMsg('notask')+"</div>";
	}
	gadgets.window.adjustHeight();
	return;		
};
afficheMenu=function(obj){
	
	var idMenu     = obj.id;
	var idSousMenu = 'sous' + idMenu;
	var sousMenu   = document.getElementById(idSousMenu);
	
	/*****************************************************/
	/**	on cache tous les sous-menus pour n'afficher    **/
	/** que celui dont le menu correspondant est cliqué **/
	/** où 4 correspond au nombre de sous-menus         **/
	/*****************************************************/
	for(var i = 1; i <= 3; i++){
		if(document.getElementById('sousmenu' + i) && document.getElementById('sousmenu' + i) != sousMenu){
			document.getElementById('sousmenu' + i).style.display = "none";
		}
	}
	
	if(sousMenu){
	
		//alert(sousMenu.style.display);
		if(sousMenu.style.display == "block"){
			sousMenu.style.display = "none";
		}
		else{
			sousMenu.style.display = "block";
		}
	}
	
};
