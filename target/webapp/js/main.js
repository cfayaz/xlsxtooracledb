$(document)
		.ready(
				function() {
					$("#file-upload-response").hide();
					$("#fileSelect").click(function() {
						$("#fileUploadBox").trigger('click');
					});
					$("#fileUploadBox").change(function() {
						var files = $("#fileUploadBox").prop("files");
						var names = $.map(files, function(val) {
							return val.name;
						});
						$("#fileDetailDiv").text(names[0]);
					});
					var options = {

						beforeSend : function(event) {
							$("#progress-bar").width('0%');
							$("#message").empty();
							var extension = $("#fileDetailDiv").text().replace(
									/^.*\./, '').trim();
							// console.log(extension.length);
							if (extension != "xlsx" && extension != "csv") {
								if (extension.length == 0)
									$("#message")
											.html(
													"<font color='red'> ERROR: Please select a file</font>");
								else
									$("#message")
											.html(
													"<font color='red'> ERROR: Invalid Extension</font>");
								event.abort();
							}
						},

						uploadProgress : function(event, position, total,
								percentComplete) {
							$("#progress-bar").width(percentComplete + '%');

							// change message text to red after 50%
							if (percentComplete > 10) {
								$("#message")
										.html(
												"<font color='red'>File Upload is in progress</font>");
							}
						},
						success : function() {
							$("#progress-bar").width('100%');
						},

						complete : function(response) {
							$("#message")
									.html(
											"<font color='blue'>Your file has been uploaded!</font>");
							var sheets = jQuery
									.parseJSON(response.responseText);
							console.log(sheets);
							console.log(sheets.length);
							this.fillSheetPanel(sheets);
							$("#file-upload-response").show();
						},

						error : function() {
							$("#message")
									.html(
											"<font color='red'> ERROR: unable to upload files</font>");
						},

						fillSheetPanel : function(sheets) {
							console.log("Filling");
							/*
							 * jQuery('<div/>', { id: 'sheet-item-header'
							 * }).appendTo('#sheet-panel');
							 */
							$('#sheet-panel')
									.append(
											"<table><thead><tr class=\"cell-item\"><th>Sheet Name</th><th>Table Name</th></tr></thead><tbody>");
							$('#sheet-panel').append("<hr>");
							for (var i = 0; i < sheets.length; i++) {
								$('#sheet-panel')
										.append(
												"<tr class=\"cell-item\"><td><input class=\"css-checkbox\" type=\"checkbox\" id=\"checkbox"
														+ i
														+ "\"/><label for=\"checkbox"
														+ i
														+ "\" name=\"checkbox"
														+ i
														+ "_lbl\" class=\"css-label lite-orange-check\">"
														+ sheets[i]
														+ "</label></td> <td><input type=\"text\" class=\"table-input\" name=\"fname\"></td></tr>");
							}
							$('#sheet-panel').append("</tbody></table>");
						}
					};
					$("#fileUploadForm").ajaxForm(options);

					$('.tab-panels .tabs li').on(
							'click',
							function() {

								var $panel = $(this).closest('.tab-panels');

								$panel.find('.tabs li.active').removeClass(
										'active');
								$(this).addClass('active');

								// figure out which panel to show
								var panelToShow = $(this).attr('rel');

								// hide current panel
								$panel.find('.panel.active').slideUp(300,
										showNextPanel);

								// show next panel
								function showNextPanel() {
									$(this).removeClass('active');

									$('#' + panelToShow).slideDown(300,
											function() {
												$(this).addClass('active');
											});
								}
							});
					$('#used').on('click',function(){
						if($('#toggle-col').attr('data-toggle')==0){
							
							$.ajax({
								url:'DBDetailHelper',
								data:{
									mode : '0'
								},
								contentType: "application/json; charset=utf-8",
								dataType:"json",
								success:function(data){									
									$('#conn-name').remove();
									var cont = '<div id="conn-cont"></div>';								
									//data = $.parseJSON(data);
									var select = '<div id="navi"><select id="conn-names" type="text" class="conn-names-select"></select></div>';
									var img = '<div id="infoi"><img src="css/dd1.png" height="20px" width="20px" /></div>';
									$('#toggle-col').append(cont);
									$('#conn-cont').append(select+img);
									var options='<option disabled selected> -- select an option -- </option>';
									$.each(data, function(i, item) {
										//'<option value="1">1</option>
										options+='<option value="'+item+'">'+item+'</option>';
	    								
									});
									$('#conn-names').append(options);
									$('#toggle-col').attr({'data-toggle':1});
									$('#used').val('New');
									
								},
								error:function(msg){
									console.log("error!!!");
								}
							});							
						}
						else{
							
							var connInput = '<input type="text" class="table-input" id="conn-name" name="conn-name">';
							$('#conn-cont').remove();
							$('#toggle-col').append(connInput);
							$('#toggle-col').attr({'data-toggle':0});
							$('#used').val('Use Saved');
							$('#host').val('');
							$('#port').val('');
							$('#sid').val('');
							$('#user').val('');
							$('#pwd').val('');

						}


					});
					$('#toggle-col').on('change','#conn-names',function(e) {
						// $.each(data, function(key, element) {
						//     alert('key: ' + key + '\n' + 'value: ' + element);
						// });						
						var selection = e.target.options[e.target.selectedIndex].text;
						console.log(selection);
						$.ajax({
							url:'DBDetailHelper',
							data:{
								mode:'1',
								id:selection
							},
							contentType: "application/json; charset=utf-8",
							dataType:"json",
							success:function(data){
								console.log(data);
								$('#host').val(data.host);
								$('#port').val(data.port);
								$('#sid').val(data.sid);
								$('#user').val(data.user);
								$('#pwd').val(data.pwd);
							},
							error:function(msg){
								console.log('Error');
							}
						});
					});
					$('#save').click(function(){
						$.ajax({
							url:'DBDetailHelper',
							method:'POST',
							beforeSend:function(){
								if($('#host').val==''||$('#port').val==''||
									$('#sid').val==''||$('#user').val==''||$('#pwd').val==''||$('#conn-name').val==''){
										event.abort();
										alert('Please fill in all DB details');
								}
							},
							data:{
								host:$('#host').val(),
								port:$('#port').val(),
								sid:$('#sid').val(),
								user:$('#user').val(),
								pwd:$('#pwd').val(),
								name:$('#conn-name').val()
							},
							success:function(data){
								console.log(data);
							},
							error:function(msg){
								console.log('ERROR '+msg);
							}


						});

					});

				});