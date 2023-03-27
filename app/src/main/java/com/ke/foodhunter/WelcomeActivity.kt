package com.ke.foodhunter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.ke.foodhunter.data.User
import com.ke.foodhunter.ui.theme.FoodHunterTheme
import com.ke.foodhunter.user.details.UserDetailsActivity

class WelcomeActivity : ComponentActivity() {

    companion object {
        const val RC_SIGN_IN = 100
    }
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)

        setContent {
            FoodHunterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Logo(LocalContext.current)
                }
            }
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    @Deprecated(message = "Will be replaced later")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception

            if(task.isSuccessful){
                try {
                    //Google SignIn was successful
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken!!)
                }catch (e: Exception){
                    //Failed
                    Log.d("SIGN-IN","Google Sign In Failed")
                }
            }else{
                Log.d("SIGN-IN",exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Sign In Successful", Toast.LENGTH_SHORT).show()

                    val user : FirebaseUser = mAuth.currentUser!!
                    //val user = task.user
                    val currentUser = User(
                        email = user.email?: "",
                        firstName = user.displayName?.split(" ")?.getOrNull(0),
                        lastName = user.displayName?.split(" ")?.getOrNull(1),
                        profileImageUrl = user.photoUrl.toString()
                    )
                    FirebaseFirestore.getInstance().collection("users")
                        .document(user.uid)
                        .set(currentUser)
                        .addOnSuccessListener {
                            //onSignInSuccess()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Error creating user: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }

                }else{
                    Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
                }

            }
    }
}


@Composable
fun Logo(context: Context) {

    Column (horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ){
        Text(text = "Food-Hunter",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(all=5.dp)
                )
        Image(painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "logo-space",
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = Color.Red,
                    shape = CircleShape
                ),
            contentScale = ContentScale.Crop
        )
        Row {
            Text(text = "Prepare yourself...",
                fontSize = 20.sp
            )
        }
        /*
        Might Prove Useful Later
        var visible by remember {
            mutableStateOf(true)
        }
        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .size(56.dp)
                .clip(CircleShape)
                .background(color = Color.Green)
                .clickable(
                    onClick = {
                        Toast
                            .makeText(context, "OnClick", Toast.LENGTH_LONG)
                            .show()
                        Log.v("OnClick", "OnClick")
                        visible = !visible
                        GlobalScope.launch {
                            //delay(2000)
                            //context.startActivity(Intent(context, ScanActivity::class.java))
                            visible = true
                        }

                    }
                ),
            contentAlignment = Alignment.Center
        )
        {

            if (visible) {
                Image(painterResource(id = R.drawable.arrow_forward),
                    alignment = Alignment.Center,
                    contentDescription = "next activity")
                Text(text = "Prepare yourself...",
                    fontSize = 20.sp
                )

            }else {
                visible=false
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier
                        .size(50.dp),
                    strokeWidth = 5.dp

                )
            }



        }
        */
        Button(
            onClick = {
                //startForResult.launch(googleSignInClient?.signInIntent)
                context.startActivity(Intent(context, UserDetailsActivity::class.java))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Green
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.google_color_icon),
                contentDescription = ""
            )
            Text(text = "Sign in with Google", modifier = Modifier.padding(6.dp))
        }
    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview2() {
    FoodHunterTheme {
        Logo(context = LocalContext.current)
    }
}