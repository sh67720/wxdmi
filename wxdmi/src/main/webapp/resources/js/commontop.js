


 
$(document).ready(function(){  
    var p=0,t=0;  
  
    $(window).scroll(function(e){  
            p = $(this).scrollTop();  
              
            if(t<=p){//下滚  
               $('#navFixed').addClass("actives");	
            }  
              
            else{//上滚  
             $('#navFixed').removeClass("actives");
            }  
            setTimeout(function(){t = p;},0);         
    });  
});  
  



// 联系我们表单提交///////////////////////////////////////////////////////////////////////////////////////////////////
$(document).ready(function(){     
   $("#mbtn").click(function(){           //当按钮button被点击时的处理函数   
		 postdata();                                       //button被点击时执行postdata函数
   });   
});   
function postdata(){                              //提交数据函数   
   $.ajax({                                                 //调用jquery的ajax方法   
     type: "POST",                                      //设置ajax方法提交数据的形式   
     url: "/inc/save.php",                                       //把数据提交到ok.php   
     data: "mname="+$("#mname").val()+"&mcity="+$("#mcity").val()+"&mcompany="+$("#mcompany").val()+"&mcons="+$("#mcons").val()+"&mtel="+$("#mtel").val(),     //输入框writer中的值作为提交的数据   
     success: function(msg){                  //提交成功后的回调，msg变量是ok.php输出的请输入您的邮箱。   
     alert("恭喜您提交成功！");                      //如果有必要，可以把msg变量的值显示到某个DIV元素中
	 document.getElementById("mname").value=""; 
	 document.getElementById("mtel").value="";
	 document.getElementById("mcity").value="";
	 document.getElementById("mcompany").value="";
	 document.getElementById("mcons").value="";
     } 
   });   
}