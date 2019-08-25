const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();  



exports.pushNotification = functions.database.ref('/DeviceTokens/{deviceId}/').onWrite(event=>{
   console.log("WE Are INSIDE THE FUNCTION","YES");
   admin.database().ref('/DeviceTokens/').once("value").then(function(snapshot){
        console.log("WE Are INSIDE THE DeviceToken","YES");
		snapshot.forEach(function(data){
			var token  = data.child("token").val();
			console.log("WE Are INSIDE THE for Loop","YES");
			console.log("THE DeviceToken",);
			var payload = {
                "notification" : {
				"click_action" : ".MainActivity",
				"title" : "Notification!",
				"body" : "You have got a new notification"
             }, 
               "data": {
               "name":data.child("deviceName").val(),
		       "deviceId":data.child("deviceID").val()
             }
           };
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



exports.pushNotification_subscription = functions.database.ref("/Subscriptions/{subscriptionName}/").onWrite(event=>{
	 console.log("WE Are INSIDE subscription","YES");
	 	var payload = {
                "notification" : {
				"click_action" : ".MainActivity",
				"title" : "Notification!",
				"body" : "You have got a new notification"
             }, 
               "data": {
               "name":"Mustofa maahmud",
		       "deviceId":"123456"
             }
           };
	 
	 admin.messaging().sendToTopic("Weather",payload)
	 .then(function(response){
            console.log('Notification sent successfully:',response);
        }) 
        .catch(function(error){
            console.log('Notification sent failed:',error);
        });
	 
});

