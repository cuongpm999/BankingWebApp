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

	$('#search-key-customer').keyup(function(e) {
		if (e.key == 'Enter') {
			$('.search-customer button').click();
		}
	})
	
	$('#search-key-employee').keyup(function(e) {
		if (e.key == 'Enter') {
			$('.search-employee button').click();
		}
	})

});

var Banking = {
	delete: function(link) {
		var flag = confirm("Bạn có chắc chắn muốn xóa?");
		if (flag == true) {
			location.href = link;
		}
	},

	checkAccountId: function() {
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

	goNext() {
		var tech = Banking.getUrlParameter('page') || 1;
		Banking.addUrlParameter('page', (parseInt(tech) + 1));
	},

	getUrlParameter: function(sParam) {
		var sPageURL = window.location.search.substring(1),
			sURLVariables = sPageURL.split('&'),
			sParameterName,
			i;

		for (i = 0; i < sURLVariables.length; i++) {
			sParameterName = sURLVariables[i].split('=');

			if (sParameterName[0] === sParam) {
				return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
			}
		}
	},

	goPrev() {
		var tech = Banking.getUrlParameter('page') || 1;
		if (parseInt(tech) > 1)
			Banking.addUrlParameter('page', (parseInt(tech) - 1));

	},

	searchForCustomer() {
		var keyCustomer = $('#search-key-customer').val();
		var fromDate = $('#from-date').val();
		var toDate = $('#to-date').val();

		var searchParams = new URLSearchParams(window.location.search);
		if (keyCustomer !== '') {
			searchParams.set('keyCustomer', keyCustomer);
		}
		else searchParams.delete('keyCustomer');
		if (fromDate !== '') {
			searchParams.set('fromDate', fromDate);
		}
		else searchParams.delete('fromDate');
		if (toDate !== '') {
			searchParams.set('toDate', toDate);
		}
		else searchParams.delete('toDate');
		window.location.search = searchParams.toString();
	},

	addUrlParameter(name, value) {
		var searchParams = new URLSearchParams(window.location.search);
		searchParams.set(name, value);
		window.location.search = searchParams.toString();
	},
	
	searchForEmployee(){
		var keyEmployee = $('#search-key-employee').val();
		var fromDate = $('#from-date').val();
		var toDate = $('#to-date').val();
		
		var searchParams = new URLSearchParams(window.location.search);	
		if(keyEmployee !== ''){
			searchParams.set('keyEmployee',keyEmployee);		
		}
		else searchParams.delete('keyEmployee');
		if(fromDate !== ''){
			searchParams.set('fromDate',fromDate);
		}
		else searchParams.delete('fromDate');
		if(toDate !== ''){
			searchParams.set('toDate',toDate);
		}
		else searchParams.delete('toDate');
		window.location.search = searchParams.toString();
	},
	
	searchForTransaction(){
		var fromDate = $('#from-date').val();
		var toDate = $('#to-date').val();
		
		var searchParams = new URLSearchParams(window.location.search);	
		if(fromDate !== ''){
			searchParams.set('fromDate',fromDate);
		}
		else searchParams.delete('fromDate');
		if(toDate !== ''){
			searchParams.set('toDate',toDate);
		}
		else searchParams.delete('toDate');
		window.location.search = searchParams.toString();
	},

}
