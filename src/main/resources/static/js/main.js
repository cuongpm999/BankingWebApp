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

const items = [
	{
		name: '.tien-gui',
		urlImg: 'linear-gradient(90deg,rgba(0,0,0,0.3), rgba(0,0,0,0.3)), url("/img/gui-tien.jpeg") no-repeat 95% 50%'
	},
	{
		name: '.ngan-hang-so',
		urlImg: 'linear-gradient(90deg,rgba(0,0,0,0.3), rgba(0,0,0,0.3)), url("/img/ngan-hang-so.png") no-repeat 67% 12%'
	},
	{
		name: '.dich-vu-the',
		urlImg: 'linear-gradient(90deg,rgba(0,0,0,0.3), rgba(0,0,0,0.3)), url("/img/dich-vu-the.jpg") no-repeat 67% 12%'
	},
	{
		name: '.thanh-toan_chuyen-khoan',
		urlImg: 'linear-gradient(90deg,rgba(0,0,0,0.3), rgba(0,0,0,0.3)), url("/img/thanh-toan-chuyen-tien.jpg") no-repeat 30% 80%'
	},
	{
		name: '.vay-ca-nhan',
		urlImg: 'linear-gradient(90deg,rgba(0,0,0,0.3), rgba(0,0,0,0.3)), url("/img/vay-ca-nhan.jpg") no-repeat 82% 32%'
	},
	{
		name: '.ngoai-hoi_thi-truong-von',
		urlImg: 'linear-gradient(90deg,rgba(0,0,0,0.3), rgba(0,0,0,0.3)), url("/img/thi-truong-von.jpg") no-repeat 82% 32%'
	}
]

items.forEach((item, i) => {
	$(`#main ${item.name}`).hover(
		function () {
			$(`#main ${item.name} .bg-img`).css('background', 'rgba(0,0,0,0.5)');
			items.forEach((subItem, j) => {
				if (i !== j) {
					$(`#main ${subItem.name} .bg-img`).css('opacity', '0');
				}
			})
			$('.change-bg').css('background', 'linear-gradient(90deg,rgba(0,0,0,0.3), rgba(0,0,0,0.3)),url("/img/change-bg.jpg") no-repeat');
			$('.change-bg').css('background-size', 'auto 350px');
			$(`${item.name} .main-item-content`).css('display', 'block');
		}, function () {
			items.forEach((subItem, j) => {
				if (i !== j) {
					$(`#main ${subItem.name} .bg-img`).css('opacity', '1');
				}
			})
			$(`#main ${item.name} .bg-img`).css('background', `${item.urlImg}`);
			$(`${item.name} .main-item-content`).css('display', 'none');
		})
})
