var url = window.location.href;
var arr = url.split("/");
url = arr[0] + "//" + arr[2];

var SentButNotYetAccepted = "";
var WaitsForAuthUserAccept = "";

$(document).ready(function()
{
	findWhoAuthUserSentButNotYetAccepted();
	findWhoWaitsForAuthUserAccept();
		
	setInterval(function()
	{
		findWhoAuthUserSentButNotYetAccepted();
		findWhoWaitsForAuthUserAccept();
			
	}, 3000);
});

function findWhoAuthUserSentButNotYetAccepted()
{
	$.ajax({type: "GET",
        url: url + "/api/findWhoAuthUserSentButNotYetAccepted",
        success: function(result)
        {
            if (result.status == "Ok")
            {
            	if(SentButNotYetAccepted == "")
            	{
            		SentButNotYetAccepted = JSON.stringify(result.data);
            	}
        		if(SentButNotYetAccepted != JSON.stringify(result.data))
        		{
        			window.location.replace("/kapcsolatok");
        		}
            }
        }
    });
}

function findWhoWaitsForAuthUserAccept()
{
	$.ajax({type: "GET",
        url: url + "/api/findWhoWaitsForAuthUserAccept",
        success: function(result)
        {
            if (result.status == "Ok")
            {
                if(WaitsForAuthUserAccept == "")
            	{
            		WaitsForAuthUserAccept = JSON.stringify(result.data);
            	}
        		if(WaitsForAuthUserAccept != JSON.stringify(result.data))
        		{
        			window.location.replace("/kapcsolatok");
        		}
            }
        }
    });
}
