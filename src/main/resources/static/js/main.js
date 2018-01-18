var eppenBeszel = new Array("", "");
var jelenlegiMsg = "";

$(document).ready(function() {

    updateFriendList();

    $(document.body).on('click', '.friendItem', function() {
        eppenBeszel[0] = $(this).find('.status').text();
        eppenBeszel[1] = $(this).find('.user').text();
        updateBeszelgetes();
        $(".top .info .name").html(eppenBeszel[1]);
    });

    setInterval(function() {
        updateFriendList();
    }, 500);
    setInterval(function() {
        updateBeszelgetes();
    }, 500);

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

function sendMessage() {
    if (eppenBeszel[0] != "") {
        var uzenet = $.trim($("#texxt").val());
        if (uzenet != "") {
            $.ajax({
                type: "GET",
                url: window.location + "/uzenet?kinek=" + eppenBeszel[0] +
                    "&szoveg=" + uzenet,
                success: updateBeszelgetes()
            });
        }
        $("#texxt").val("");
        updateBeszelgetes();
    }
}

function updateBeszelgetes()
{
    if (eppenBeszel[0] != "")
    {
        $.ajax({type: "GET",
                url: window.location + "/beszelgetes?vele=" + eppenBeszel[0],
                success: function(result)
                {
                    if (result.status == "Done")
                    {
                        res = JSON.stringify(result.data);

                        if (jelenlegiMsg != res)
                        {
                            jelenlegiMsg = res;

                            $('.messages').empty();
                            $.each(result.data,
                                   function(id, obj)
                                   {
                            	    	var msgs = $(".messages").append("<li class='i'><div class='head'><span class='time'>" + formatDateToMessages(obj.date) + "</span><span class='name'>" + obj.name +
                                    									"</span></div><div class='message'>" + obj.message + "</div></li>");
                                    	$('.messages').append(msgs);
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
            url: window.location + "/baratok",
            success: function(result) {
                if (result.status == "Done") {
                    $('.list-friends').empty();
                    $.each(result.data,
                            function(id, user) {
			                        	var app_friend = "<li class='friendItem'><div class='info'><div class='user'>" + user.name  + "</div><div class='status'>" + user.email + "</div</div></li>";
			                            $('.list-friends').append(app_friend);
                            	
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