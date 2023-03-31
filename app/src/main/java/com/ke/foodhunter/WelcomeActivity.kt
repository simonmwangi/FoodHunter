package com.ke.foodhunter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.delay

class WelcomeActivity : ComponentActivity() {

    companion object {
        const val RC_SIGN_IN = 100
    }
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        val currentUser = mAuth.currentUser

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
                    val appContext = LocalContext.current

                    WelcomeScreen(appContext, currentUser) { signIn() }
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
                            val intent = Intent(this, UserDetailsActivity::class.java)
                            startActivity(intent)
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
fun WelcomeScreen(context: Context, currentUser: FirebaseUser?, signIn: ()-> Unit ) {
    val colors = listOf(
        Color(0xFF062220), // Purple
        Color(0xFF0B4F2D), // Light blue
        Color(0xFFE2B745)  // Purple
    )
    val gradient = Brush.linearGradient(
        0.0f to colors[0],
        500.0f to colors[1],
        start = Offset.Zero,
        end = Offset.Infinite
    )
    var user by remember {
        mutableStateOf(currentUser)
    }
    var check by remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth()
        .background(gradient)
    ) {

        var scale = remember {
            androidx.compose.animation.core.Animatable(0.0f)
        }
        LaunchedEffect(key1 = true) {
            scale.animateTo(
                targetValue = 0.7f,
                animationSpec = tween(800, easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                })
            )
            if(user != null) {
                delay(1000)
                context.startActivity(Intent(context, MainActivity::class.java))
            } else{
                check = true
            }
        }



        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "",
            alignment = Alignment.TopCenter, modifier = Modifier
                .fillMaxSize()
                .padding(40.dp)
                .scale(scale.value)
            )
        Button(
            onClick = {
                      signIn()
                      //context.startActivity(Intent(context, UserDetailsActivity::class.java))
            },
            enabled = check,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .align(Alignment.Center)
                .width(100.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colors[2]
            )
        ) {
            Text("Sign In", color = Color.White, fontWeight = FontWeight.Bold)
        }


        Text(
            text = "Version - ${BuildConfig.VERSION_NAME}",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview2() {
    FoodHunterTheme {
        WelcomeScreen(context = LocalContext.current, currentUser = null, signIn = {})
    }
}