$(document).ready(function() {
	$("#myInput").on("keyup", function() {
		var value = $(this).val().toLowerCase();
		$("#myTable tr").filter(function() {
			$(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
		});
	});

	$(document).scroll(function() {
		if ($(document).scrollTop() != 0) {
			$("#onTop").fadeIn();
		} else {
			$("#onTop").fadeOut();
		}
	});
	$("#onTop").click(function() {
		$("html, body").animate({ scrollTop: 0 }, 700);
	});


});

var Banking = {
	delete: function(link) {
		var flag = confirm("Bạn có chắc chắn muốn xóa?");
		if (flag == true) {
			location.href = link;
		}
	},

}
