const userCase = [{
	name: "Create user \"NekoTest\"",
	url: "/api/user",
	method: "POST",
	header: "admin-auth",
	body: `{
			"id": 3,
			"age": 26,
			"userName": "NekoTest",
			"password": "nekos password",
			"gender": "Male",
			"image": "profile-img-1.png",
			"ability": "1|3|4|2|6",
			"highestAbility": "",
			"createdTime": "2018-01-01-23:44:32",
			"lastLoginTime": "",
			"reserved": "",
			"extraInfo": null
			}`,
	verify: {
		expectedCode: 200,
	}
},
{
	name: "Create user \"NekoTest\" with wrong request body",
	url: "/api/user",
	method: "POST",
	header: "admin-auth",
	body: `{-
			"id": 3,
			"age": 26,
			"userName": "NekoTest",
			"password": "nekos password",
			"gender": "Male",
			"image": "profile-img-1.png",
			"ability": "1|3|4|2|6",
			"highestAbility": "",
			"createdTime": "2018-01-01-23:44:32",
			"lastLoginTime": "",
			"reserved": "",
			"extraInfo": null
			}`,
	verify: {
		expectedCode: 500,
	}
},
{
	name: "Check user \"NekoTest\" exists or not",
	url: "/api/user",
	method: "GET",
	header: "admin-auth",
	body: "",
	verify: {
		expectedCode: 200,
		expectedFunction: function(result) {
			caseVars.globalVar = null;
			for(let i = 0; i < result.length; i++) {
				if(result[i].userName == "NekoTest") {
					caseVars.globalVar = result[i].id;
				}
			}
			if(caseVars.globalVar == null) {
				throw "User \"NekoTest\" not found"
			}
		}
	}
},
{
	name: "Delete user \"NekoTest\"",
	url: "/api/user/",
	method: "DELETE",
	header: "admin-auth",
	body: "",
	before: function() {
		this.url = "/api/user/" + caseVars.globalVar;
	},
	verify: {
		expectedCode: 200,
	}
},
{
	name: "Delete user with wrong ID",
	url: "/api/user/-1",
	method: "DELETE",
	header: "admin-auth",
	body: "",
	verify: {
		expectedCode: 404,
	}
},
{
	name: "Get user with wrong ID",
	url: "/api/user/-1",
	method: "DELETE",
	header: "admin-auth",
	body: "",
	verify: {
		expectedCode: 404,
	}
},
{
	name: "Unauthorized request",
	url: "/api/user",
	method: "GET",
	header: "stupid-authorization",
	body: "",
	verify: {
		expectedCode: 401,
	}
},
]