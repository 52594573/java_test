/*PublicFun*/
function Rgb2Hex(rgb) {
	rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
	function hex(x) {
		return ("0" + parseInt(x).toString(16)).slice(-2);
	}
	return "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]);
}
/*PublicFunEnd*/
$.fn.extend({
	/*ImgResize*/
	ImgResize:function(type){
		var wrap = $(this).find(".imgWrap");
		var img = wrap.find("img");
		var resize = function(){
			var ml,mt;
			img.css({"width":"100%","height":"auto"});
			if(type == "full"){
				if(img.height() < wrap.height()){
					img.height(wrap.height());
					img.css("width","auto");
				}
			}else if(type == "showAll"){
				if(img.height() > wrap.height()){
					img.height(wrap.height());
					img.css("width","auto");
				}
			}
			ml = (wrap.width() - img.width() ) * 0.5;
			mt = (wrap.height() - img.height()) * 0.5;
			img.css("left",ml + "px");
			img.css("top",mt + "px");
			img.attr("imgWidth",img.width());
			img.attr("imgHeight",img.height());
		}
		if(img.height() > 0 & img.width() > 0){
			resize();
		}else{
			img.on("load",function(){
				resize();
			});
		}
	},
	ImgHoverEffect:function(){
		var img = $(this).find("img");
		var wrap = $(this).find(".imgWrap");
		$(this).on("mouseenter",function(){
			var imgWidth = img.width() * 1.1;
			var imgHeight = img.height() * 1.1;
			var left = (wrap.width() - imgWidth) * 0.5;
			var top = (wrap.height() - imgHeight) * 0.5;
			img.stop().animate({width:imgWidth + "px",height: imgHeight + "px",left: left+"px", top: top + "px"},200);
		});
		$(this).on("mouseleave",function(){
			var imgWidth = Number(img.attr("imgWidth"));
			var imgHeight = Number(img.attr("imgHeight"));
			var left = (wrap.width() - imgWidth) * 0.5;
			var top = (wrap.height() - imgHeight) * 0.5;
			
			img.stop().animate({width:imgWidth + "px",height: imgHeight + "px",left: left+"px", top: top + "px"},200,function(){
				if(Math.abs(imgWidth - wrap.width()) > Math.abs(imgHeight - wrap.height())){
					img.css("width","auto");
				}else{
					img.css("height","auto");
				}
			});
		});
	},
	HoverGrayBGEffect:function(){
		var defaultColor = $(this).css("background-color");
		console.log(defaultColor);
		$(this).on("mouseenter",function(){
			$(this).stop().animate({"backgroundColor":"#eeeeee"},200);
		});
		$(this).on("mouseleave",function(){
			$(this).stop().animate({"backgroundColor":defaultColor},200);
		});
	},
	/*ImgResize*/
	/*ElementVerVerticalAlignMiddle*/
	ElementVerVerticalAlignMiddle:function(target){
		var pt = ($(this).height() - target.height() )* 0.5;
		target.css("padding-top",pt + "px");
	},
	/*ElementVerVerticalAlignMiddle*/
	initMenu:function(){
		var bg = $(this).find(".bg");
		var btnGroup = $(this).find("a")
		var hoverBtn = $(this).find("a.hover")
		var nowHover = btnGroup.index(hoverBtn);
		var cnDefaultColor = Rgb2Hex(btnGroup.find(".cn").css("color"));
		var enDefaultColor = Rgb2Hex(btnGroup.find(".en").css("color"));
		var txtHoverColor = "#ffffff";
		var animateTime = 200;
		var txtAnimateTime = 300;
		var mouseoutDelay;
		var mouseoutDelayTime =100;
		var getBgPos = function(btn){
			var _w = btn.outerWidth();
			var _left = btn.position().left + Number(btn.css("margin-left").replace("px",""));
			return [_w,_left];
		}
		var setBgPos = function(btn){
			var pos = getBgPos(btn);
			bg.width(pos[0]);
			bg.css("left",pos[1]+"px");
		}
		var bgMove = function(btn){
			if(hoverBtn.length != 0){
				var pos = getBgPos(btn);
				bg.stop().animate({width:pos[0]+"px",left:pos[1] + "px",opacity:"1"},animateTime);
			}else{
				bg.stop().animate({opacity:"0"},animateTime);
			}
		}
		var txtToWhite = function(btn){
			btn.find(".cn").stop().animate({color:txtHoverColor},txtAnimateTime);
			btn.find(".en").stop().animate({color:txtHoverColor},txtAnimateTime);
		};
		var txtToDefault = function(btn){
			btn.find(".cn").stop().animate({color:cnDefaultColor},txtAnimateTime);
			btn.find(".en").stop().animate({color:enDefaultColor},txtAnimateTime);
		};
		btnGroup.each(function(index,element){
			$(element).on("mouseover",function(){
				clearTimeout(mouseoutDelay);
				bgMove($(element));
				txtToDefault($(hoverBtn));
				txtToWhite($(element));
			})
			$(element).on("mouseleave ",function(){
				clearTimeout(mouseoutDelay);
				mouseoutDelay = setTimeout(function(){
					bgMove($(hoverBtn));
				},mouseoutDelayTime);
				txtToDefault($(element));
				txtToWhite($(hoverBtn));
			})
		})
		if(hoverBtn.length != 0){
			setBgPos(hoverBtn);
			txtToWhite($(hoverBtn));
		}
	},
	InitMainTitle:function(){
		var title = $(this);
		$(window).on("scroll",function(){
			var nowTop =title.offset().top - $(window).scrollTop() - $(window).height() * 0.4 + Number(title.css("padding-top").replace("px",""));
			var process = nowTop / $(window).height() * 0.6;
			process = process > 1? 1:process;
			process = process < 0? 0:process;
			enLeft = -100 * process + "%";
			enOpacity = 0.3 * (1 - process);
			bgLeft = 50 + 50 * process + "%";
			title.find(".en").css({"left": enLeft,"opacity":enOpacity});
			title.find(".bg").css({"left":bgLeft,"opacity":(1 - process)});
		})
	},
	InitHoverBtn:function(_bgColor,_txtColor,_borderColor){
		_bgColor = _bgColor || "#1c56a6";
		_txtColor = _txtColor || "#ffffff";
		_borderColor = _borderColor || "#1c56a6";
		var defaultBG = $(this).css("backgroundColor");
		var defaultTxt = $(this).css("color");
		var defaultBorder = $(this).css("borderColor");
		$(this).on("mouseenter",function(){
			$(this).stop().animate({backgroundColor:_bgColor,color:_txtColor,borderColor:_borderColor},200);
		})
		$(this).on("mouseleave",function(){
			$(this).stop().animate({backgroundColor:defaultBG,color:defaultTxt,borderColor:defaultBorder},300);
		})
	}
})

/*PublicInit*/
$(function(){
	$(".Header .nav").initMenu();
	$(".MainTitle").each(function(){
		$(this).InitMainTitle();
	})
	$(".HoverBtn").each(function(){
		var bg = $(this).attr("bgHoverColor");
		var txt = $(this).attr("txtHoverColor");
		var border = $(this).attr("borderHoverColor");
		$(this).InitHoverBtn(bg,txt,border);
	})
	$(".PageBanner").ImgResize("full");
	$(".PageBanner .pageMenu .text").each(function(){
		$(this).on("mouseenter",function(){
			$(this).stop().animate({opacity:"0.9",backgroundColor:"#1c56a6"},200)
		})
		$(this).on("mouseleave",function(){
			if(!$(this).hasClass("hover")){
				$(this).stop().animate({opacity:"0.5",backgroundColor:"#000000"},200)
			}
		})
	})
	if($(".PageBanner").length > 0){
		$(window).on("resize",function(){
			$(".PageBanner").ImgResize("full");
		})
	}
})
/*PublicInitEnd*/