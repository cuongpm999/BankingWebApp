var Banking = {
	delete: function(link){
		var flag = confirm("Bạn có chắc chắn muốn xóa?");
		if (flag == true) {
			location.href = link;
		}
	}

}