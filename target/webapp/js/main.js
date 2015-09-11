$(document).ready(function() {
		$("#fileSelect").click(function(){
			$("#fileUploadBox").trigger('click');
		});
		$("#fileUploadBox").change(function(){
			var files = $("#fileUploadBox").prop("files");
			var names = $.map(files, function(val) { return val.name; });
			$("#fileDetailDiv").text(names[0]);
		});
		var options = {
        beforeSend : function() {
                $("#progress-bar").width('0%');
                $("#message").empty();
                
        },
        uploadProgress : function(event, position, total, percentComplete) {
                $("#progress-bar").width(percentComplete + '%');
                

                // change message text to red after 50%
                if (percentComplete > 10) {
                $("#message").html("<font color='red'>File Upload is in progress</font>");
                }
        },
        success : function() {
                $("#progress-bar").width('100%');
        },
        complete : function(response) {
        $("#message").html("<font color='blue'>Your file has been uploaded!</font>");
        },
        error : function() {
        $("#message").html("<font color='red'> ERROR: unable to upload files</font>");
        }
	};
	$("#fileUploadForm").ajaxForm(options);
	$("#fileUploadForm").submit(function(e){
		e.preventDefault();
		var formElement = $("#fileUploadForm")[0];
		var fd = new FormData(formElement);
		var fileInput = $("#fileUploadBox")[0];
		console.log(fileInput);
		fd.append('file', fileInput.files[0]);
		$.ajax({
			type: "POST",
  			url: "FileUploadHandler",
  			data: fd,
  			contentType: false,
  			processData: false,

  			success:function(resp){
  				console.log(resp);
  			},
  			error:function(){
  				console.log("Error");
  			}
		});
	});
});