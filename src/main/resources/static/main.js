$(document).ready(function() {
	$("#generate").click(function() {
		$.ajax({
			type : 'POST',
			url : 'http://localhost:8080/v1/api/url',
			data : JSON.stringify({
				"originalUrl" : $("#urlinput").val()
			}),
			contentType : "application/json; charset=utf-8",
			success : function(data) {
				$("#shorturltext").val(data.shortnedUrl);
			}
		});
	});

	$("#copy").click(function() {
	  var copyText = document.getElementById("shorturltext");

       /* Copy the text inside the text field */
      navigator.clipboard.writeText(copyText.value);
    });



});