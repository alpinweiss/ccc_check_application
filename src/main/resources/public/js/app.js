"use strict";

$(function(){
	var jForm = $("#phoneNumberForm");
	var jPhoneNumber = $('#phoneNumber');
	var jButton = $('#phoneSubmitButton');

	jForm.submit(function(event) {
		event.preventDefault();
		return false;
	});

	jButton.click(function() {
		var phoneNumber = jPhoneNumber.val();
		$.ajax({
			url:        '/detect/country/by/number/' + phoneNumber,
			type:       'GET',
			success:    function (data) {
				console.log(data);
			},
			error:      function(error) {
				console.log(error);
			}
		});
	});
});