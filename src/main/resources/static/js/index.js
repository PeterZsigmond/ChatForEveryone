$(".form").find("input, textarea").on("keyup blur focus", function(e) {
	var a = $(this),
		i = a.prev("label");
	"keyup" === e.type ? "" === a.val() ? i.removeClass("active highlight") : i.addClass("active highlight") : "blur" === e.type ? "" === a.val() ? i.removeClass("active highlight") : i.removeClass("highlight") : "focus" === e.type && ("" === a.val() ? i.removeClass("highlight") : "" !== a.val() && i.addClass("highlight"))
});
