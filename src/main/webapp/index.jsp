	<!DOCTYPE html>
	<html>
	<head>
		<title>XL2DB alpha 1.0</title>
		<link rel="stylesheet" type="text/css" href="css/style.css" />
		<script type="text/javascript" src="js/jquery-2.1.4.min.js" ></script>
		<script type="text/javascript" src="js/jquery.form.min.js" ></script>
		<script type="text/javascript" src="js/main.js"></script>
	</head>
	<body>
		<header><h1>Excel to DB</h1></header>
	<!-- 	<h2>Hello World!</h2>
		This is helloworld from the jsp page.. CHeck
		<a href="TestServlet">here</a> -->
		<div class="fileBox">
			<h3>Choose File to Upload in Server</h3>
			<form  enctype="multipart/form-data" id="fileUploadForm" name="fileUploadForm" method="post" action="FileUploadHandler" >
				
				<input type="file" id="fileUploadBox" name="fileUploadBox"/>
				
				<div id="fileSelectDiv">
					<button id="fileSelect" type="button" class="file-btn">Select File</button>
				</div>
				<div id="fileDetailDiv"></div>
				

				<div id="fileUploadButDiv">
					<input type="submit" value="Upload" id="fileUploadButton" name="fileUploadButton" class="file-btn" />  
				</div>
				
				<div id = "progress-div">
					<div id = "progress-bar"></div></div>
				</div> 
			</form>
		</div>
		<div id="message"></div>
	</body>
	</html>
