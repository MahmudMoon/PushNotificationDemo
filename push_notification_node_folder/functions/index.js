let functions = require('firebase-functions');
let admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.pushNotification = functions.database.ref('/DeviceTokens/{pushId}/').onCreate((snapshot, context)=>{
	var pushId = context.params.pushId;
	console.log('push id is',pushId);
	var token = snapshot.child('token').val().toString();
	var deviceName = snapshot.child('deviceName').val().toString();
	var deviceId = snapshot.child('deviceID').val().toString();
	console.log('token is',token);
	
	var payload = {
		
		"notification":{
		   "click_action" : ".MainActivity",
          "title" : "You have a new Notification",
          "body" : "Say Hello to " + deviceName	
		},
		"data" : {
          "deviceName" : deviceName,
          "deviceId"   : deviceId,
		  "token"      : token
		}
	};
	
	return admin.database().ref('/DeviceTokens/').once("value").then(function(snapshot){
			snapshot.forEach(function(data){
				var tokenchild = data.child('token').val();
				console.log('tokens are ', tokenchild);
				
				admin.messaging().sendToDevice(token,payload)
				.then(function(response){
					console.log('Notification sent successfully:',response);
				}) 
				.catch(function(error){
				console.log('Notification sent failed:',error);
				});
		});
	});
});