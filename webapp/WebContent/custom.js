var Solr = {
	getData: function () {
		$.ajax({
			src: "solr.json",
			success: function (data) {
				console.log(data);
			}
		});
	},	
		
}

$(function () {
	Solr.getData();
	
})