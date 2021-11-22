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
	
	checkAccountId: function(){
		var data = {};
		data["number"] = $('#number').val();

		$.ajax({
			url: "/rest/api/check/account",
			type: "post",
			contentType: "application/json",
			data: JSON.stringify(data),

			dataType: "json",
			success: function(jsonResult) {
				if (jsonResult.status == "333") {
					location.href = "/admin/transaction/create-payment/create";
				}
				else if (jsonResult.status == "111") {
					alert("Tài khoản không tồn tại");
				}
				else if (jsonResult.status == "222") {
					alert("Vui lòng nhập số tài khoản");
				}
			}
		});
	},


}
