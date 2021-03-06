// index.js

var REST_DATA = 'api/vinic';

var KEY_ENTER = 13;
var defaultItems = [

];

function encodeUriAndQuotes(untrustedStr) {
	return encodeURI(String(untrustedStr)).replace(/'/g, '%27').replace(')', '%29');
}

function startProgressIndicator(row)
{
	row.innerHTML="<td class='content'>Uploading file... <img height=\"50\" width=\"50\" src=\"images/loading.gif\"></img></td>";
}

function removeProgressIndicator(row)
{
	row.innerHTML="<td class='content'>uploaded...</td>";
}

function addNewRow(table)
{
	var newRow = document.createElement('tr');
	table.appendChild(newRow);
	return table.lastChild;
}

function uploadFile(node) {

	var file = node.previousSibling.files[0];

	//if file not selected, throw error
	if (!file) {
		alert("File not selected for upload... \t\t\t\t \n\n - Choose a file to upload. \n - Then click on Upload button.");
		return;
	}

	var row = node.parentNode.parentNode.parentNode;

	var form = new FormData();
	form.append("file", file);

	var id = row.getAttribute('data-id');

	var queryParams = (id == null) ? "" : "id=" + id;
	queryParams += "&name=" + row.firstChild.firstChild.value;
	queryParams += "&value=" + row.firstChild.nextSibling.firstChild.value;
	queryParams+= "&filename="+file.name;


	var table = row.firstChild.nextSibling.firstChild;
	var newRow = addNewRow(table);

	startProgressIndicator(newRow);

	xhrAttach("attach?"+queryParams, form, function(item){
		console.log('Item id - ' + item.id);
		console.log('attached: ', item);
		row.setAttribute('data-id', item.id);
		removeProgressIndicator(row);
		setRowContent(item, row);
	}, function(err) {
		console.error(err);
	});

}

//var attachButton = "<br><div class='uploadBox'><input type=\"file\" name=\"file\" id=\"upload_file\"><input width=\"100\" type=\"submit\" value=\"Upload\" onClick='uploadFile(this)'></div>";
function sanitizeInput(str) {
	return String(str).replace(/&(?!amp;|lt;|gt;)/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
}
function setRowContent(item, row) {
	var innerHTML = "<td class='contentName'><textarea id='nameText' class = 'nameText' onkeydown='onKey(event)'>" + sanitizeInput(item.name) + "</textarea></td>" +
					"<td class='contentName'><textarea id='valueText' class = 'nameText' onkeydown='onKey(event)'>" + sanitizeInput(item.value) + "</textarea></td>" +
					"<td class='contentName'><textarea id='roleText' class = 'nameText' onkeydown='onKey(event)'>" + sanitizeInput(item.role) + "</textarea></td>" +
					"<td class='contentName'><textarea id='bandText' class = 'nameText' onkeydown='onKey(event)'>" + sanitizeInput(item.band) + "</textarea></td>";
					/*** "<td class='contentDetails'>"; ***/
	
	/***
	var valueTextArea = "<textarea id='valText' onkeydown='onKey(event)' placeholder=\"Enter a description...\"></textarea>";
	if (item.value) {
		valueTextArea = "<textarea id='valText' onkeydown='onKey(event)'>" + sanitizeInput(item.value) + "</textarea>";
	}

	innerHTML += valueTextArea;
	
	var attachments = item.attachements;
	if (attachments && attachments.length > 0) {
		innerHTML += "<div class='flexBox'>";
		for (var i = 0; i < attachments.length; ++i) {
			var attachment = attachments[i];

			if (attachment.content_type.indexOf("image/") == 0) {
				innerHTML += "<div class='contentTiles'>" + attachment.key + "<br><img height=\"150\" src=\"" + encodeUriAndQuotes(attachment.url) + "\" onclick='window.open(\"" + encodeUriAndQuotes(attachment.url) + "\")'></img></div>";

			} else if (attachment.content_type.indexOf("audio/") == 0) {
				innerHTML += "<div class='contentTiles'>" + attachment.key + "<br><AUDIO  height=\"50\" src=\"" + encodeUriAndQuotes(attachment.url) + "\" controls></AUDIO></div>";

			} else if (attachment.content_type.indexOf("video/") == 0) {
				innerHTML += "<div class='contentTiles'>" + attachment.key + "<br><VIDEO  height=\"150\" src=\"" + encodeUriAndQuotes(attachment.url) + "\" controls></VIDEO></div>";

			} else if (attachment.content_type.indexOf("text/") == 0 || attachment.content_type.indexOf("application/") == 0) {
				innerHTML += "<div class='contentTiles'><a href=\"" + encodeUriAndQuotes(attachment.url) + "\" target=\"_blank\">" + attachment.key + "</a></div>";
			}

		}
		innerHTML += "</div>";

	}
	***/

	row.innerHTML = innerHTML + /** attachButton + ***/ "</td><td class = 'contentAction'><span class='deleteBtn' onclick='deleteItem(this)' title='delete me'></span></td>";

}

function addItem(item, isNew) {

	var row = document.createElement('tr');
	row.className = "tableRows";
	var id = item && item.id;
	if (id) {
		row.setAttribute('data-id', id);
	}



	if (item) // if not a new row
	{
		setRowContent(item, row);
	} else if (item) //if new row with content
	{
		row.innerHTML = "<td class='contentName'><textarea id='nameText' onkeydown='onKey(event)' placeholder=\"Enter the name...\"></textarea></td>" +
					    "<td class='contentName'><textarea id='roleText' onkeydown='onKey(event)' placeholder=\"Enter the role...\"></textarea></td>" +
					    "<td class='contentName'><textarea id='bandText' onkeydown='onKey(event)' placeholder=\"Enter the band...\"></textarea></td>" +
				        "<td class = 'contentAction'><span class='deleteBtn' onclick='deleteItem(this)' title='delete me'></span></td>";
	}

	
	var table = document.getElementById('notes');
	
	
	
	
	//table.lastChild.appendChild(row);
	table.append(row);
	row.isNew = !item || isNew;

	if (row.isNew) {
		var textarea = row.firstChild.firstChild;
		textarea.focus();
	}

}

function deleteItem(deleteBtnNode){
	debugger;
	
	var row = deleteBtnNode.parentNode.parentNode;
	if(row.getAttribute('data-id'))
	{
		row.remove();
		xhrDelete(REST_DATA + '?id=' + row.getAttribute('data-id'), function(){
		}, function(err){
			console.log(err);
			//stop showing loading message
			stopLoadingMessage();
			document.getElementById('errorDiv').innerHTML = err;
		});
	}
}


function onKey(evt) {
	
	if (evt.keyCode == KEY_ENTER && !evt.shiftKey) {

		debugger;
		
		evt.stopPropagation();
		evt.preventDefault();
		var nameV, valueV;
		var row;
		row = evt.target.parentNode.parentNode;

		/***
		if (evt.target.id == "nameText") {
			row = evt.target.parentNode.parentNode;
			nameV = evt.target.value;
			valueV = row.firstChild.nextSibling.firstChild.value;

		} else {
			row = evt.target.parentNode.parentNode;
			nameV = row.firstChild.firstChild.value;
			valueV = evt.target.value;
		}
		***/

		var data = {
			name: row.children[0].firstChild.value,
			role: row.children[1].firstChild.value,
			band: row.children[2].firstChild.value
		};

		if (row.isNew) {
			delete row.isNew;
			xhrPost(REST_DATA, data, function(item){
				row.setAttribute('data-id', item.id);
			}, function(err){
				console.log(err);
				//stop showing loading message
				stopLoadingMessage();
				document.getElementById('errorDiv').innerHTML = err;
			});
		} else {
			var requestParam = '?id=' + row.getAttribute('data-id')+"&name="+data.name+"&role="+data.role+"&band="+data.band;
			xhrPut(REST_DATA+requestParam, data, function(){
				console.log('updated: ', data);
			}, function(err){
				console.log(err);
				//stop showing loading message
				stopLoadingMessage();
				document.getElementById('errorDiv').innerHTML = err;
			});
		}


		if (row.nextSibling) {
			row.nextSibling.firstChild.firstChild.focus();
		} else {
			addItem();
		}
	}
}

function saveChange(contentNode, callback){
	var row = contentNode.parentNode.parentNode;

	var data = {
		name: row.firstChild.firstChild.value,
		value:row.firstChild.nextSibling.firstChild.value
	};

	if(row.isNew){
		delete row.isNew;
		xhrPost(REST_DATA, data, function(item){
			row.setAttribute('data-id', item.id);
			callback && callback();
		}, function(err){
			console.log(err);
			//stop showing loading message
			stopLoadingMessage();
			document.getElementById('errorDiv').innerHTML = err;
		});
	}else{
		data.id = row.getAttribute('data-id');
		xhrPut(REST_DATA, data, function(){
			console.log('updated: ', data);
		}, function(err){
			console.log(err);
			//stop showing loading message
			stopLoadingMessage();
			document.getElementById('errorDiv').innerHTML = err;
		});
	}
}

function executeSearchByAssignee(){
	
	showLoadingMessage();
	var assigneeName = document.getElementById("assigneeName").value;
	var startDate = document.getElementById("startDate").value;
	var endDate = document.getElementById("endDate").value;
	
	// Create query to be executed
	var query = '{ "selector": { ';
	if(assigneeName){ 
		//query += ' "assignee.name": "' + assigneeName + '", ';
		query += ' "assignee.name": { "$regex": "' + assigneeName + '" }, ';
	}
	
	debugger;
	
	query += ' "status_at": { "$gte": ' + startDate + ', "$lte": ' + endDate + ' } } }';
		
	xhrGet(REST_DATA + "?query=" + query, function(data){
		var receivedItems = data.body || [];
		var items = [];
		var i;
		
		// Make sure the received items have correct format
		for(i = 0; i < receivedItems.length; ++i){
			var item = receivedItems[i];
			if(item && 'id' in item){
				items.push(item);
			}
		}
		var hasItems = (items.length && items[0].name != null && items[0].name != "");
		
		// If has items, update table		
		if(hasItems){
			for(i = 0; i < items.length; ++i){
				addItem(items[i], !hasItems);
			}
			drawChart(items);
		// If has no items, show message
		} else {
			alert("Not found");
		}
			
	}, function(err){
		console.log(err);
		document.getElementById('errorDiv').innerHTML = err;

	});
	stopLoadingMessage();

}

function toggleAppInfo(){
	var node = document.getElementById('appinfo');
	node.style.display = node.style.display == 'none' ? '' : 'none';
}

function showLoadingMessage()
{
	document.getElementById('loadingImage').innerHTML = "Loading data "+"<img height=\"100\" width=\"100\" src=\"images/loading.gif\"></img>";
}

function stopLoadingMessage()
{
	document.getElementById('loadingImage').innerHTML = "";
}

	
//Draw Chart - Uses Google Chart JS
function drawChart(items) {
		
	// Create the data table.
	var data = new google.visualization.DataTable();
	data.addColumn('string', 'Assignees');
	data.addColumn('number', 'Hours Spent');
	
	
	var flgFound = false;
	
	// Calculate total time spent by assignee
	// Loop Issues
	for(var i = 0; i < items.length; ++i){
		
		flgFound = false;
		
		// Loop Data Rows
		for(var j = 0; j < data.getNumberOfRows(); j++){
			
			// If assignee already in data row, sum total time spent
			if(data.wg[j].c[0].v == sanitizeInput(items[i].name)){
				flgFound = true;
				data.wg[j].c[1].v += (items[i].timeStats.total_time_spent/3600); //seconds per hour
			}
		}
		
		// If new assignee, create new data row
		if(!flgFound){
			data.addRow([sanitizeInput(items[i].name) , items[i].timeStats.total_time_spent/3600]); //seconds per hour
		}
	}	

	// Set chart options
	var options = {'title':'Issues By Assignee',
			'width':500,
			'height':300};

	// Instantiate and draw our chart, passing in some options.
	var barchart = new google.visualization.BarChart(document.getElementById('bar_chart_div'));
	var piechart = new google.visualization.PieChart(document.getElementById('pie_chart_div'));
	barchart.draw(data, options);
	piechart.draw(data, options);
}


