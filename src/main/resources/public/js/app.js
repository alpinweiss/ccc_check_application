"use strict";

$(function(){
	var jForm = $("#phoneNumberForm");
	var jPhoneNumber = $('#phoneNumber');
	var jButton = $('#phoneSubmitButton');
	var jInfoMessage = $('#infoMessage');

	jForm.submit(function(event) {
		event.preventDefault();
		return false;
	});

	jButton.click(function() {
		var phoneNumber = jPhoneNumber.val();
		jInfoMessage.hide();
		if (phoneNumber.length > 0) {
			$.ajax({
				url: '/detect/country/by/number/' + phoneNumber,
				type: 'GET',
				success: function (data) {
					if (jInfoMessage.hasClass("alert-danger")) {
						jInfoMessage.removeClass("alert-danger");
						jInfoMessage.addClass("alert-success");
					}
					jInfoMessage.html(data.country);
					jInfoMessage.show();
				},
				error: function (error) {
					if (jInfoMessage.hasClass("alert-success")) {
						jInfoMessage.removeClass("alert-success");
						jInfoMessage.addClass("alert-danger");
					}
					if (error.responseJSON) {
						jInfoMessage.html(error.responseJSON.message);
					} else {
						switch (error.status) {
							case 400:
								jInfoMessage.html("Incorrect input, please check it.");
							break;

							case 500:
								jInfoMessage.html("Internal error, please try again later!");
							break;

							default:
								jInfoMessage.html("Unexpected error, please try again later!");
						}
					}
					console.log(error);
					jInfoMessage.show();
				}
			});
		}
	});

	jInfoMessage.hide();
});