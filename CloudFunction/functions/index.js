
const functions = require("firebase-functions");

// The Firebase Admin SDK to access Cloud Firestore.
const admin = require("firebase-admin");
admin.initializeApp();

// Create and Deploy Your First Cloud Functions
// https://firebase.google.com/docs/functions/write-firebase-functions

exports.addNewUser = functions.https.onCall((data, context) => {
  functions.logger.info("New user created!");
  const usersRef = admin.firestore().collection("users");
  return usersRef.doc(data.uid).set({
    username: data.username,
    email: data.email,
    profile_pic_url: data.profile_pic_url,
    name: data.name,
  });
});

// exports.addUserToFirestore = functions.auth.user().onCreate((user) => {
//   functions.logger.info("New user created!");
//   const usersRef = admin.firestore().collection("users");
//   return usersRef.doc(user.uid).set({
//     displayName: user.displayName,
//     email: user.email,
//   });
// });
