var eppenBeszel = new Array("", "");
var jelenlegiMsg = "";
var numberOfMessages = -1;

$(document).ready(function() {

    updateFriendList();

    $(document.body).on('mousedown', '.friendItem', function() {
        eppenBeszel[0] = $(this).find('.email').text();
        eppenBeszel[1] = $(this).find('.name').text();
        numberOfMessages = -1;
        checkForNewMessages();
        setTextBox();        
        $(".top .info .name").html(eppenBeszel[1] + "<br /><span class='email'>" + eppenBeszel[0] + "</span>");
    });

    setInterval(function() {
        updateFriendList();
    }, 1000);
    setInterval(function() {
    	checkForNewMessages();
    }, 200);

    $(".list-friends").niceScroll(conf);
    $(".messages").niceScroll(lol);
    $(".ui .write-form textarea").keypress(function(e) {
        if (e.keyCode === 13 && !e.shiftKey) {

            sendMessage();

            return false;
        }
    });
    return $(".send").click(function() {
        sendMessage();

    });
});

function sendMessage() {
    if (eppenBeszel[0] != "") {
        var msg = $.trim($(".ui .write-form textarea").val());
        var msg2 = msg.replace(/\n|\r/g, "<br />");
        if (msg2 != "") {
            $.ajax({
                type: "GET",
                url: window.location + "/api/newMessage?for=" + eppenBeszel[0] + "&message=" + msg2
            });
        }
        $(".ui .write-form textarea").val("");
    }
}

function checkForNewMessages()
{
	if (eppenBeszel[0] != "")
    {
        $.ajax({type: "GET",
                url: window.location + "/api/getNumberOfMessages?with=" + eppenBeszel[0],
                success: function(result)
                {
                    if (result.status == "Ok")
                    {
                       if(result.data > numberOfMessages)
                       {
                    	   numberOfMessages = result.data;
                    	   updateMessages();
                       }
                    }
                }
            });
    }
}

function updateMessages()
{
    if (eppenBeszel[0] != "")
    {
        $.ajax({type: "GET",
                url: window.location + "/api/getMessages?with=" + eppenBeszel[0],
                success: function(result)
                {
                    if (result.status == "Ok")
                    {
                        var res = JSON.stringify(result.data);

                        if(jelenlegiMsg != res)
                        {
                            jelenlegiMsg = res;

                            $('.messages').empty();
                            $.each(result.data,
                                   function(id, obj)
                                   {               
                            	    	$(".messages").append("<li class='" + ((eppenBeszel[0] == obj.senderEmail) ? 'mess' : 'i') + "'><div class='head'><span class='time'>" +
                            	    				 		  formatDateToMessages(obj.date) + "</span><span class='name'>" + obj.senderName +
                            	    				          "</span></div><div class='message'>" + obj.message + "</div></li>");
                                    	clearResizeScroll();
                                   });
                        }
                    }
                }
            });
    }
}

function formatDateToMessages(date)
{
	var date2 = new Date(date);
	var today = new Date();
	
	var year = date2.getFullYear();
	var month = date2.getMonth()+1;
	var day = date2.getDate();
	var hour = date2.getHours();
	var minute = date2.getMinutes();
	
	month = putZeroToLeft(month);
	day = putZeroToLeft(day);
	hour = putZeroToLeft(hour);
	minute = putZeroToLeft(minute);
	
	if(year < today.getFullYear())
		return year+"."+month+"."+day+". "+hour+":"+minute;
	else if(month != (today.getMonth()+1) || day != today.getDate())
		return month+"."+day+". "+hour+":"+minute;
	else
		return "Ma, "+hour+":"+minute;	
}

function putZeroToLeft(number)
{
	if(number < 10)
		return '0'+number;
	return number;
}

function updateFriendList() {
    $
        .ajax({
            type: "GET",
            url: window.location + "/api/getFriends",
            success: function(result) {
                if (result.status == "Ok") {
                    $('#leftpanel ul').empty();
                    $.each(result.data,
                            function(id, user)
                            {	
			                 	$('#leftpanel ul').append("<li class='friendItem'><a href='' onclick='return false;'><span class='name'>" + user.name  + "</span><div class='email'>" + user.email + "</div></a></li>");                            	
                            });
                }
            }
        });
}

var clearResizeScroll, conf, lol;

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

clearResizeScroll = function() {

    $(".messages").getNiceScroll(0).resize();
    return $(".messages").getNiceScroll(0).doScrollTop(999999, 999);
};

function setTextBox()
{
	$(".write-form").css("visibility", "visible");
	$(".write-form textarea").val('');
}