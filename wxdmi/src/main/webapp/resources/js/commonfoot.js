//手机菜单
jQuery(document).ready(function($) {
	$(".mobopen").click(function(event){		
		$(".navmenu ul").show();
		$(".mobclose").show();
	});
	$(".mobclose").click(function(event){		
		$('.navmenu ul').hide();
		$(this).hide();
	});
});
//返回顶部
$(function()
{
		$(this).find(".backtotop").click(function()
		{
			$("html, body").animate({
				"scroll-top":0
			},"fast");
		});
});