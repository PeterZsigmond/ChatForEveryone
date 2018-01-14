(function() {
	
	var eppenBeszel = "";
	var jelenlegiMsg = "";
	
	$(document).ready(function() {
		  
		  updateFriendList();
		  
		  $(document.body).on('click', '.friendItem' ,function(){
			  eppenBeszel=$(this).text();
			  updateBeszelgetes();
			  $(".top .info .name").html(eppenBeszel);
		  });
		  
		
		setInterval(function(){ updateFriendList(); }, 1000);
		setInterval(function(){ updateBeszelgetes(); }, 1000);
		  
	    $(".list-friends").niceScroll(conf);
	    $(".messages").niceScroll(lol);
	    $("#texxt").keypress(function(e) {
	      if (e.keyCode === 13) {
	    	  
	    	  sendMessage();
	        
	        
	        return false;
	      }
	    });
	    return $(".send").click(function() {
	    	sendMessage();
	    	
	    	
	    });
	  });
	
	function sendMessage()
	{
		if(eppenBeszel != "")
		{
			var uzenet = $.trim($("#texxt").val());
			if(uzenet != "")
			{
				$.ajax({
					type : "GET",
					url : window.location + "/uzenet?kinek="+eppenBeszel + "&szoveg=" + uzenet,
					success: updateBeszelgetes()
				});
			}
			$("#texxt").val("");
			updateBeszelgetes();
		}
	}
	
//	function updateMessages(var newMsg)
//	{
//		$('.messages').append(newMsg);
//	}
	
	
	function updateBeszelgetes()
	{
		if(eppenBeszel != "")
		{
			$.ajax({
				type : "GET",
				url : window.location + "/beszelgetes?vele=" + eppenBeszel,
				success: function(result){
					if(result.status == "Done")
					{
						res = JSON.stringify(result.data);
						
						if(jelenlegiMsg != res)
						{	
							jelenlegiMsg = res;
							
//							console.log("frissitve a msg-k.");
//							console.log(jelenlegiMsg);
//							console.log(res);
							
							$('.messages').empty();
							$.each(result.data, function(id, obj)
							{
								
									if(obj.kuldo != eppenBeszel)
										var app = $(".messages").append("<li class='i'><div class='head'><span class='name'>" + obj.kuldo + "</span></div><div class='message'>" + obj.uzenet + "</div></li>");	
									else
										var app = $(".messages").append("<li class='mess'><div class='head'><span class='name'>" + obj.kuldo + "</span></div><div class='message'>" + obj.uzenet + "</div></li>");	
									
									$('.messages').append(app);
									
									claerResizeScroll();
									
									
									
									//updateMessages();
								
					        });
							
	//						$(".messages").append("<li class='i'><div class='head'><span class='name'>Barát 1</span></div><div class='message'>" + innerText + "</div></li>");
	//					    $(".messages").append("<li class='mess'><div class='head'><span class='name'>Barát 1</span></div><div class='message'>" + msg + "</div></li>");
	//						claerResizeScroll();
							        
							//console.log(result.data);
						}
					}				
				}
			});
		}
	}
	
	
	
	
	function updateFriendList(){
		$.ajax({
			type : "GET",
			url : window.location + "/baratok",
			success: function(result){
				if(result.status == "Done")
				{
					$('.list-friends ul').empty();
					$.each(result.data, function(id, barat)
					{
						var app_friend = "<li class='friendItem'><div class='info'><div class='user'>" + barat + "</div></div></li>";						
						$('.list-friends ul').append(app_friend);
			        });					
				}				
			}
		});
	}
	
  var NYLM, claerResizeScroll, conf, getRandomInt, insertI, lol;

  conf = {
    cursorcolor: "#696c75",
    cursorwidth: "4px",
    cursorborder: "none"
  };

  lol = {
    cursorcolor: "#cdd2d6",
    cursorwidth: "4px",
    cursorborder: "none"
  };

  NYLM = ["a", "b", "c", "d", "e", "f", ")", "g", "h", "i", "j", "k"];

  getRandomInt = function(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
  };

  claerResizeScroll = function() {
    
    $(".messages").getNiceScroll(0).resize();
    return $(".messages").getNiceScroll(0).doScrollTop(999999, 999);
  };

  /*insertI = function() {
    var innerText, otvet;
    innerText = $.trim($("#texxt").val());
    if (innerText !== "") {
      $(".messages").append("<li class='i'><div class='head'><span class='time'>" + (new Date().getHours()) + ":" + (new Date().getMinutes()) + " Ma</span><span class='name'> Barát 1</span></div><div class='message'>" + innerText + "</div></li>");
      claerResizeScroll();
      return otvet = setInterval(function() {
        $(".messages").append("<li class='mess'><div class='head'><span class='name'>Barát 1  </span><span class='time'>" + (new Date().getHours()) + ":" + (new Date().getMinutes()) + " Ma</span></div><div class='message'>" + NYLM[getRandomInt(0, NYLM.length - 1)] + "</div></li>");
        claerResizeScroll();
        return clearInterval(otvet);
      }, getRandomInt(2500, 500));
    }
  };*/
  
  /*
   innerText = $.trim($("#texxt").val());
   $(".messages").append("<li class='i'><div class='head'><span class='name'>Barát 1</span></div><div class='message'>" + innerText + "</div></li>");
      
   $(".messages").append("<li class='mess'><div class='head'><span class='name'>Barát 1</span></div><div class='message'>" + msg + "</div></li>");
        claerResizeScroll();
   */

  

}).call(this);