const roomCase = [{
	name: "Create user \"SaberTest\"",
	url: "/api/user",
	method: "POST",
	header: "admin-auth",
	body: `{
			"id": 3,
			"age": 26,
			"userName": "SaberTest",
			"password": "sabers password",
			"gender": "Female",
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
	name: "Create user \"ArcherTest\"",
	url: "/api/user",
	method: "POST",
	header: "admin-auth",
	body: `{
			"id": 3,
			"age": 26,
			"userName": "ArcherTest",
			"password": "archers password",
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
	name: "Create user \"LancerTest\"",
	url: "/api/user",
	method: "POST",
	header: "admin-auth",
	body: `{
			"id": 3,
			"age": 26,
			"userName": "LancerTest",
			"password": "lancers password",
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
	name: "Check created three users",
	url: "/api/user",
	method: "GET",
	header: "admin-auth",
	body: "",
	verify: {
		expectedCode: 200,
		expectedFunction: function(result) {
			let saberId, archerId, lancerId;
			saberId = archerId = lancerId = null;
			for(let i = 0; i < result.length; i++) {
				if(result[i].userName == "SaberTest") {
					saberId = result[i].id;
				}
				if(result[i].userName == "ArcherTest") {
					archerId = result[i].id;
				}
				if(result[i].userName == "LancerTest") {
					lancerId = result[i].id;
				}
			}
			if(saberId == null) {
				throw "User \"SaberTest\" not found"
			}
			if(archerId == null) {
				throw "User \"ArcherTest\" not found"
			}
			if(lancerId == null) {
				throw "User \"LancerTest\" not found"
			}
			caseVars.globalArray = [saberId, archerId, lancerId];
		}
	}
},
{
	name: "Create room \"Fate Room Test\" for three users",
	url: "/api/room",
	method: "POST",
	header: "admin-auth",
	before: function() {
		this.body = `{
		"id": 1,
		"type": 0,
		"creator": ${caseVars.globalArray[0]},
		"createdTime": "2018-06-07-23:59:45",
		"name": "Fate Room Test",
		"appointmentTime": "{from:{hour:9,minute:0},to:{hour:18,minute:0}}",
		"members": "${caseVars.globalArray[0]}|${caseVars.globalArray[1]}|${caseVars.globalArray[2]}",
		"reserved": ""}`
	},
	body: "",
	verify: {
		expectedCode: 200,
	}
},
{
	name: "Check room exists",
	url: "/api/room",
	method: "GET",
	header: "admin-auth",
	body: "",
	verify: {
		expectedCode: 200,
		expectedFunction: function(result) {
			let roomId = null;
			for(let i = 0; i < result.length; i++) {
				if(result[i].name == "Fate Room Test") {
					roomId = result[i].id;
					break;
				}
			}
			if(roomId == null) {
				throw "Room does not exist";
			}
			caseVars.globalVar = roomId;
		}
	}
},
{
	name: "Check room properties",
	url: "/api/room",
	method: "GET",
	header: "admin-auth",
	before: function() {
		this.url = "/api/room/" + caseVars.globalVar
	},
	body: "",
	verify: {
		expectedCode: 200,
		expectedFunction: function(result) {
			const memberString = caseVars.globalArray[0] + "|" + caseVars.globalArray[1] + "|" + caseVars.globalArray[2];
			if(result.creator != caseVars.globalArray[0]) {
				throw "Property of creator error";
			}
			if(result.members.toString() != memberString) {
				throw "Property of members error";
			}
			// check extraInfo
			const extra = result.extraInfo;
			const creator = extra.creator;
			const members = extra.members;
			if(creator.id != caseVars.globalArray[0]) {
				throw "Creator is not saber";
			}
			const idArr = [members[0].id, members[1].id, members[2].id];
			caseVars.globalArray.sort();
			idArr.sort();
			if(caseVars.globalArray.toString() != idArr.toString()) {
				throw "Members are wrong";
			}
		}
	}
},
{
	name: "Delete room \"Fate Room Test\"",
	url: "/api/room/",
	method: "DELETE",
	header: "admin-auth",
	body: "",
	before: function() {
		this.url = "/api/room/" + caseVars.globalVar;
	},
	verify: {
		expectedCode: 200,
	}
},
{
	name: "Delete user \"SaberTest\"",
	url: "/api/user/",
	method: "DELETE",
	header: "admin-auth",
	body: "",
	before: function() {
		this.url = "/api/user/" + caseVars.globalArray[0];
	},
	verify: {
		expectedCode: 200,
	}
},
{
	name: "Delete user \"ArcherTest\"",
	url: "/api/user/",
	method: "DELETE",
	header: "admin-auth",
	body: "",
	before: function() {
		this.url = "/api/user/" + caseVars.globalArray[1];
	},
	verify: {
		expectedCode: 200,
	}
},
{
	name: "Delete user \"LancerTest\"",
	url: "/api/user/",
	method: "DELETE",
	header: "admin-auth",
	body: "",
	before: function() {
		this.url = "/api/user/" + caseVars.globalArray[2];
	},
	verify: {
		expectedCode: 200,
	}
},
{
	name: "Delete room with wrong ID",
	url: "/api/user/-1",
	method: "DELETE",
	header: "admin-auth",
	body: "",
	verify: {
		expectedCode: 404,
	}
},
{
	name: "Get room with wrong ID",
	url: "/api/user/-1",
	method: "DELETE",
	header: "admin-auth",
	body: "",
	verify: {
		expectedCode: 404,
	}
},
{
	name: "Create room with wrong request body",
	url: "/api/user",
	method: "POST",
	header: "admin-auth",
	body: `{-
			"id": 1,
			"type": 0,
			"creator": 1,
			"createdTime": "2018-06-07-23:59:45",
			"name": "Fate Room Test",
			"appointmentTime": "{from:{hour:9,minute:0},to:{hour:18,minute:0}}",
			"members": "1|2|3",
			"reserved": ""
			}`,
	verify: {
		expectedCode: 500,
	}
},
];