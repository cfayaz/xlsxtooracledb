
<!DOCTYPE html>
<html>
<head>
<title>XL2DB alpha 1.0</title>
<link rel="stylesheet" type="text/css" href="css/style.css" />
<script type="text/javascript" src="js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="js/jquery.form.min.js"></script>
<script type="text/javascript" src="js/main.js"></script>
</head>
<body>
	<header>
		<h1>Excel to DB</h1>
	</header>
	<!-- 	<h2>Hello World!</h2>
		This is helloworld from the jsp page.. CHeck
		<a href="TestServlet">here</a> -->
	<div class="fileBox">
		<h4>Choose File to Upload in Server</h4>
		<hr>
		<form enctype="multipart/form-data" id="fileUploadForm"
			name="fileUploadForm" method="post" action="FileUploadHandler">

			<input type="file" id="fileUploadBox" name="fileUploadBox" />

			<div id="fileSelectDiv">
				<button id="fileSelect" type="button" class="file-btn">Select
					File</button>
			</div>
			<div id="fileDetailDiv"></div>


			<div id="fileUploadButDiv">
				<input type="submit" value="Upload" id="fileUploadButton"
					name="fileUploadButton" class="file-btn" />
			</div>

			<div id="progress-div">
				<div id="progress-bar"></div>
			</div>
	</div>
	</form>
	</div>
	<div id="message"></div>
	<div id="file-upload-response">
		<div class="tab-panels">
			<ul class="tabs">
				<li rel="sheet-panel" class="active">Select Sheets</li>
				<li rel="db-panel">DB Details</li>
			</ul>
			<div id="sheet-panel" class="panel active"></div>
			<div id="db-panel" class="panel">
				<table>
					<tr>
						<td>Conn Name</td>
						<td colspan="3" id="toggle-col" data-toggle="0"><input type="text" class="table-input" id="conn-name"
							name="conn-name"></td>
					</tr>					
					<tr>
						<td>Host Name</td>
						<td colspan="3"><input type="text" class="table-input"
							id="host"></td>
					</tr>
					<tr>
						<td>SID/Service</td>
						<td><input type="text" class="db-input" id="sid"></td>
						<td>Port</td>
						<td><input type="text" class="db-input" id="port"></td>
					</tr>
					<tr>
						<td>User Name</td>
						<td><input type="text" class="db-input" id="user"></td>
						<td>Password</td>
						<td><input type="password" class="db-input" id="pwd"></td>
					</tr>
					<tr>
						<td></td>
						<td><input type="button" id ="used" class="file-btn" value="Use Saved"></td>						
						<td><input type="button" class="file-btn" id="save"  value="Save"></td>
						<td></td>
					</tr>					
				</table>


			</div>
		</div>
	</div>
</body>
</html>
