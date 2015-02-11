var Solr = {
	getData : function() {
		var that = this;

		$.ajax({
			url : "solr.json",
			success : function(data) {
				var jsonData = $.parseJSON(data);

				that.setPlaces(jsonData.response.docs);
			}
		});
	},
	setPlaces : function(places) {
		for ( var element in places) {

			var meta = places[element];

			console.log(meta);

			var template = $($('.templates .place').html());
			$(template).find('input[type="text"]').val(meta.title);

			$(template).find('input[type="text"]').attr('data-title',
					meta.title);
			$(template).find('input[type="text"]').attr('data-description',
					meta.description);
			$(template).find('input[type="text"]').attr('data-picture',
					meta.picture);
			$(template).find('input[type="text"]').attr('data-location',
					meta.location);

			$('.sidebar .nav').append(template);

		}
	}

}

$(function() {
	Solr.getData();

	$(".sidebar").delegate(".point-checkbox", "click", function() {


		
		calcRoute();
	});

})