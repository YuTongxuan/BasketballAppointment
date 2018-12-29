const handleStep = function(steps, index, callback) {
	const testCase = steps[index];
	const item = {
		id: index,
		name: testCase.name,
		status: 'Querying',
		result: 'Unknown',
		style: 'info',
		expectedCode: testCase.verify.expectedCode
	}
	vueObject.cases.push(item);
	if(testCase.before) {
		testCase.before();
	}
	$.ajax({
		url: testCase.url,
		data: testCase.body,
		processData: false,
		contentType: 'application/json',
		type: testCase.method,
		headers: {
			authorization: testCase.header
		},
		complete: function(jqXHR, textStatus) {
			const status = jqXHR.status;
			const data = JSON.parse(jqXHR.responseText);
			let success = true;
			item.comment = '';
			item.actualCode = status;
			if(status != testCase.verify.expectedCode) {
				item.comment = 'Wrong response code: ' + status;
				success = false;
			}
			else {
				try {
					if(testCase.verify.expectedFunction) {
						testCase.verify.expectedFunction(data);
					}
				}
				catch(err) {
					item.comment = 'Error: ' + err;
					success = false;
				}
			}
			item.status = 'Complete';
			item.result = success ? 'Success' : 'Failure';
			item.style = success ? 'success' : 'danger';
			
			if(index < steps.length - 1) {
				handleStep(steps, index + 1, callback);
			}
			else {
				const emptyLine = {
					id: '',
					name: '',
					status: '',
					result: '',
					style: 'info',
					expectedCode: ''
				}
				vueObject.cases.push(emptyLine);
				if(callback) {
					callback();
				}
			}
		}
	});
}

const handleAllSteps = function(steps, callback) {
	handleStep(steps, 0, callback);
}

const handleCases = function(cases, index) {
	handleAllSteps(cases[index], function() {
		if(index < cases.length - 1) {
			handleCases(cases, index + 1);
		}
	});
}

const registerCasesAndRun = function() {
	handleCases(arguments, 0);
}
