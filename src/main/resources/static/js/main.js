$('header .services').hover(
	function () {
		$('header .drop-down-menu').css('display', 'block');
	}, function(){
		$('header .drop-down-menu').css('display', 'none');
	}
)

$('header .drop-down-menu').hover(
	function () {
		$('header .drop-down-menu').css('display', 'block');
	}, function(){
		$('header .drop-down-menu').css('display', 'none');
	}
)

$('#main .tien-gui').hover(
	function () {
		$('#main .tien-gui .bg-img').css('background', 'rgba(0,0,0,0.5)');
		$('#main .ngan-hang-so .bg-img').css('opacity', '0');
		$('#main .dich-vu-the .bg-img').css('opacity', '0');
		$('#main .thanh-toan_chuyen-khoan .bg-img').css('opacity', '0');
		$('#main .vay-ca-nhan .bg-img').css('opacity', '0');
		$('#main .ngoai-hoi_thi-truong-von .bg-img').css('opacity', '0');
		$('.change-bg').css('background', 'linear-gradient(90deg,rgba(0,0,0,0.3), rgba(0,0,0,0.3)),url("/img/change-bg.jpg") no-repeat');
		$('.change-bg').css('background-size', 'auto 350px');
		$('.tien-gui .main-item-content').css('display', 'block');
	}, function(){
		$('#main .ngan-hang-so .bg-img').css('opacity', '1');
		$('#main .dich-vu-the .bg-img').css('opacity', '1');
		$('#main .thanh-toan_chuyen-khoan .bg-img').css('opacity', '1');
		$('#main .vay-ca-nhan .bg-img').css('opacity', '1');
		$('#main .ngoai-hoi_thi-truong-von .bg-img').css('opacity', '1');
		$('#main .tien-gui .bg-img').css('background', 'linear-gradient(90deg,rgba(0,0,0,0.3), rgba(0,0,0,0.3)), url("/img/gui-tien.jpeg") no-repeat 95% 50%');
		$('.tien-gui .main-item-content').css('display', 'none');
	}
)

$('#main .ngan-hang-so').hover(
	function () {
		$('#main .ngan-hang-so .bg-img').css('background', 'rgba(0,0,0,0.5)');
		$('#main .tien-gui .bg-img').css('opacity', '0');
		$('#main .dich-vu-the .bg-img').css('opacity', '0');
		$('#main .thanh-toan_chuyen-khoan .bg-img').css('opacity', '0');
		$('#main .vay-ca-nhan .bg-img').css('opacity', '0');
		$('#main .ngoai-hoi_thi-truong-von .bg-img').css('opacity', '0');
		$('.change-bg').css('background', 'linear-gradient(90deg,rgba(0,0,0,0.3), rgba(0,0,0,0.3)),url("/img/change-bg.jpg") no-repeat');
		$('.change-bg').css('background-size', 'auto 350px');
		$('.ngan-hang-so .main-item-content').css('display', 'block');
	}, function(){
		$('#main .tien-gui .bg-img').css('opacity', '1');
		$('#main .dich-vu-the .bg-img').css('opacity', '1');
		$('#main .thanh-toan_chuyen-khoan .bg-img').css('opacity', '1');
		$('#main .vay-ca-nhan .bg-img').css('opacity', '1');
		$('#main .ngoai-hoi_thi-truong-von .bg-img').css('opacity', '1');
		$('#main .ngan-hang-so .bg-img').css('background', 'linear-gradient(90deg,rgba(0,0,0,0.3), rgba(0,0,0,0.3)),url("/img/ngan-hang-so.png") no-repeat 67% 12%');
		$('.ngan-hang-so .main-item-content').css('display', 'none');
	}
)

$('#main .dich-vu-the').hover(
	function () {
		$('#main .dich-vu-the .bg-img').css('background', 'rgba(0,0,0,0.5)');
		$('#main .ngan-hang-so .bg-img').css('opacity', '0');
		$('#main .tien-gui .bg-img').css('opacity', '0');
		$('#main .thanh-toan_chuyen-khoan .bg-img').css('opacity', '0');
		$('#main .vay-ca-nhan .bg-img').css('opacity', '0');
		$('#main .ngoai-hoi_thi-truong-von .bg-img').css('opacity', '0');
		$('.change-bg').css('background', 'linear-gradient(90deg,rgba(0,0,0,0.3), rgba(0,0,0,0.3)),url("/img/change-bg.jpg") no-repeat');
		$('.change-bg').css('background-size', 'auto 350px');
		$('.dich-vu-the .main-item-content').css('display', 'block');
	}, function(){
		$('#main .ngan-hang-so .bg-img').css('opacity', '1');
		$('#main .tien-gui .bg-img').css('opacity', '1');
		$('#main .thanh-toan_chuyen-khoan .bg-img').css('opacity', '1');
		$('#main .vay-ca-nhan .bg-img').css('opacity', '1');
		$('#main .ngoai-hoi_thi-truong-von .bg-img').css('opacity', '1');
		$('#main .dich-vu-the .bg-img').css('background', 'linear-gradient(90deg,rgba(0,0,0,0.3), rgba(0,0,0,0.3)),url("/img/dich-vu-the.jpg") no-repeat 67% 12%');
		$('.dich-vu-the .main-item-content').css('display', 'none');
	}
)

$('#main .thanh-toan_chuyen-khoan').hover(
	function () {
		$('#main .thanh-toan_chuyen-khoan .bg-img').css('background', 'rgba(0,0,0,0.5)');
		$('#main .ngan-hang-so .bg-img').css('opacity', '0');
		$('#main .dich-vu-the .bg-img').css('opacity', '0');
		$('#main .tien-gui .bg-img').css('opacity', '0');
		$('#main .vay-ca-nhan .bg-img').css('opacity', '0');
		$('#main .ngoai-hoi_thi-truong-von .bg-img').css('opacity', '0');
		$('.change-bg').css('background', 'linear-gradient(90deg,rgba(0,0,0,0.3), rgba(0,0,0,0.3)),url("/img/change-bg.jpg") no-repeat');
		$('.change-bg').css('background-size', 'auto 350px');
		$('.thanh-toan_chuyen-khoan .main-item-content').css('display', 'block');
	}, function(){
		$('#main .ngan-hang-so .bg-img').css('opacity', '1');
		$('#main .dich-vu-the .bg-img').css('opacity', '1');
		$('#main .tien-gui .bg-img').css('opacity', '1');
		$('#main .vay-ca-nhan .bg-img').css('opacity', '1');
		$('#main .ngoai-hoi_thi-truong-von .bg-img').css('opacity', '1');
		$('#main .thanh-toan_chuyen-khoan .bg-img').css('background', 'linear-gradient(90deg,rgba(0,0,0,0.3), rgba(0,0,0,0.3)),url("/img/thanh-toan-chuyen-tien.jpg") no-repeat 30% 80%');
		$('.thanh-toan_chuyen-khoan .main-item-content').css('display', 'none');
	}
)

$('#main .vay-ca-nhan').hover(
	function () {
		$('#main .vay-ca-nhan .bg-img').css('background', 'rgba(0,0,0,0.5)');
		$('#main .ngan-hang-so .bg-img').css('opacity', '0');
		$('#main .dich-vu-the .bg-img').css('opacity', '0');
		$('#main .thanh-toan_chuyen-khoan .bg-img').css('opacity', '0');
		$('#main .tien-gui .bg-img').css('opacity', '0');
		$('#main .ngoai-hoi_thi-truong-von .bg-img').css('opacity', '0');
		$('.change-bg').css('background', 'linear-gradient(90deg,rgba(0,0,0,0.3), rgba(0,0,0,0.3)),url("/img/change-bg.jpg") no-repeat');
		$('.change-bg').css('background-size', 'auto 350px');
		$('.vay-ca-nhan .main-item-content').css('display', 'block');
	}, function(){
		$('#main .ngan-hang-so .bg-img').css('opacity', '1');
		$('#main .dich-vu-the .bg-img').css('opacity', '1');
		$('#main .thanh-toan_chuyen-khoan .bg-img').css('opacity', '1');
		$('#main .tien-gui .bg-img').css('opacity', '1');
		$('#main .ngoai-hoi_thi-truong-von .bg-img').css('opacity', '1');
		$('#main .vay-ca-nhan .bg-img').css('background', 'linear-gradient(90deg,rgba(0,0,0,0.3), rgba(0,0,0,0.3)),url("/img/vay-ca-nhan.jpg") no-repeat 82% 32%');
		$('.vay-ca-nhan .main-item-content').css('display', 'none');
	}
)

$('#main .ngoai-hoi_thi-truong-von').hover(
	function () {
		$('#main .ngoai-hoi_thi-truong-von .bg-img').css('background', 'rgba(0,0,0,0.5)');
		$('#main .ngan-hang-so .bg-img').css('opacity', '0');
		$('#main .dich-vu-the .bg-img').css('opacity', '0');
		$('#main .thanh-toan_chuyen-khoan .bg-img').css('opacity', '0');
		$('#main .vay-ca-nhan .bg-img').css('opacity', '0');
		$('#main .tien-gui .bg-img').css('opacity', '0');
		$('.change-bg').css('background', 'linear-gradient(90deg,rgba(0,0,0,0.3), rgba(0,0,0,0.3)),url("/img/change-bg.jpg") no-repeat');
		$('.change-bg').css('background-size', 'auto 350px');
		$('.ngoai-hoi_thi-truong-von .main-item-content').css('display', 'block');
	}, function(){
		$('#main .ngan-hang-so .bg-img').css('opacity', '1');
		$('#main .dich-vu-the .bg-img').css('opacity', '1');
		$('#main .thanh-toan_chuyen-khoan .bg-img').css('opacity', '1');
		$('#main .vay-ca-nhan .bg-img').css('opacity', '1');
		$('#main .tien-gui .bg-img').css('opacity', '1');
		$('#main .ngoai-hoi_thi-truong-von .bg-img').css('background', 'linear-gradient(90deg,rgba(0,0,0,0.3), rgba(0,0,0,0.3)),url("/img/thi-truong-von.jpg") no-repeat 82% 32%');
		$('.ngoai-hoi_thi-truong-von .main-item-content').css('display', 'none');
	}
)