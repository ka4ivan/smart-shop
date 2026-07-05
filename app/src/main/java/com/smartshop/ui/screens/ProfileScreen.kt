package com.smartshop.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.smartshop.BuildConfig
import com.smartshop.R
import com.smartshop.ui.theme.LocalCustomColors
import com.smartshop.ui.theme.Red
import com.smartshop.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    currentTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    currentLanguage: String,
    onLanguageChange: (String) -> Unit,
    authViewModel: AuthViewModel
) {
    var isThemeSheetVisible by remember { mutableStateOf(false) }
    var isLanguageSheetVisible by remember { mutableStateOf(false) }
    var isAuthSheetVisible by remember { mutableStateOf(false) }

    // ModalBottomSheet renders its content in a separate Popup that does not
    // inherit the localized LocalContext override from MainActivity, so it
    // must be re-provided explicitly here to keep the sheets translated.
    val localizedContext = LocalContext.current

    val currentUser by authViewModel.currentUser.collectAsState()

    // rememberLauncherForActivityResult must be called from the main composition,
    // not from inside the ModalBottomSheet's Popup content — the popup window isn't
    // attached to the Activity's view tree, so LocalActivityResultRegistryOwner
    // can't be resolved there and the app crashes.
    var googleAuthError by remember { mutableStateOf<String?>(null) }

    val googleSignInClient = remember(localizedContext) {
        val webClientResId = localizedContext.resources.getIdentifier(
            "default_web_client_id", "string", localizedContext.packageName
        )
        val optionsBuilder = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
        if (webClientResId != 0) {
            optionsBuilder.requestIdToken(localizedContext.getString(webClientResId))
        }
        GoogleSignIn.getClient(localizedContext, optionsBuilder.build())
    }

    val googleLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        try {
            val account = GoogleSignIn.getSignedInAccountFromIntent(result.data).getResult(ApiException::class.java)
            val idToken = account?.idToken
            if (idToken != null) {
                googleAuthError = null
                authViewModel.signInWithGoogle(localizedContext, idToken) { isAuthSheetVisible = false }
            } else {
                googleAuthError = localizedContext.getString(R.string.google_sign_in_unavailable)
            }
        } catch (e: ApiException) {
            if (e.statusCode != GoogleSignInStatusCodes.SIGN_IN_CANCELLED) {
                googleAuthError = localizedContext.getString(R.string.google_sign_in_unavailable)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ProfileHeader()

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(LocalCustomColors.current.listBackground)
        ) {
            val user = currentUser
            if (user != null) {
                AccountRow(
                    user = user,
                    onSignOut = { authViewModel.signOut() }
                )
            } else {
                SettingsRow(
                    icon = R.drawable.user,
                    title = stringResource(R.string.account),
                    value = stringResource(R.string.not_signed_in),
                    onClick = { isAuthSheetVisible = true }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(LocalCustomColors.current.listBackground)
        ) {
            SettingsRow(
                icon = R.drawable.pallet,
                title = stringResource(R.string.change_theme),
                value = stringResource(if (currentTheme) R.string.dark_theme else R.string.light_theme),
                onClick = { isThemeSheetVisible = true }
            )
            HorizontalDivider(
                modifier = Modifier.padding(start = 70.dp),
                thickness = 1.dp,
                color = LocalCustomColors.current.lightGray
            )
            SettingsRow(
                icon = R.drawable.language,
                title = stringResource(R.string.language),
                value = if (currentLanguage == "en") "English" else "Українська",
                onClick = { isLanguageSheetVisible = true }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(R.string.app_version, BuildConfig.VERSION_NAME),
            fontSize = 12.sp,
            color = LocalCustomColors.current.textSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )
    }

    if (isThemeSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { isThemeSheetVisible = false },
            windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, (-3).dp)
        ) {
            CompositionLocalProvider(LocalContext provides localizedContext) {
                ThemeBottomSheetContent(
                    currentTheme = currentTheme,
                    onThemeChange = {
                        onThemeChange(it)
                        isThemeSheetVisible = false
                    }
                )
            }
        }
    }

    if (isLanguageSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { isLanguageSheetVisible = false },
            windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, (-3).dp)
        ) {
            CompositionLocalProvider(LocalContext provides localizedContext) {
                LanguageBottomSheetContent(
                    currentLanguage = currentLanguage,
                    onLanguageChange = {
                        onLanguageChange(it)
                        isLanguageSheetVisible = false
                    }
                )
            }
        }
    }

    if (isAuthSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                isAuthSheetVisible = false
                authViewModel.clearError()
                googleAuthError = null
            },
            windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, (-3).dp)
        ) {
            CompositionLocalProvider(LocalContext provides localizedContext) {
                AuthBottomSheetContent(
                    authViewModel = authViewModel,
                    googleAuthError = googleAuthError,
                    onGoogleSignInClick = {
                        googleAuthError = null
                        authViewModel.clearError()
                        googleLauncher.launch(googleSignInClient.signInIntent)
                    },
                    onSuccess = { isAuthSheetVisible = false }
                )
            }
        }
    }
}

@Composable
private fun AccountRow(user: FirebaseUser, onSignOut: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(LocalCustomColors.current.lightBlue),
            contentAlignment = Alignment.Center
        ) {
            val photoUrl = user.photoUrl
            if (photoUrl != null) {
                Image(
                    painter = rememberAsyncImagePainter(photoUrl.toString()),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.user),
                    contentDescription = null,
                    tint = LocalCustomColors.current.blue,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.email ?: user.displayName ?: stringResource(R.string.account),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = LocalCustomColors.current.text
            )
            Text(
                text = stringResource(R.string.account),
                fontSize = 13.sp,
                color = LocalCustomColors.current.textSecondary
            )
        }

        TextButton(onClick = onSignOut) {
            Text(
                text = stringResource(R.string.sign_out),
                color = Red,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthBottomSheetContent(
    authViewModel: AuthViewModel,
    googleAuthError: String?,
    onGoogleSignInClick: () -> Unit,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    var isRegisterMode by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoading by authViewModel.isLoading.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(if (isRegisterMode) R.string.sign_up else R.string.sign_in).uppercase(),
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = LocalCustomColors.current.text,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text(text = stringResource(R.string.email), color = LocalCustomColors.current.textSecondary) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = LocalCustomColors.current.inputBackground,
                focusedBorderColor = LocalCustomColors.current.lightGray,
                unfocusedBorderColor = LocalCustomColors.current.lightGray,
                focusedTextColor = LocalCustomColors.current.text,
                unfocusedTextColor = LocalCustomColors.current.text,
            ),
            shape = MaterialTheme.shapes.medium.copy(CornerSize(16.dp))
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text(text = stringResource(R.string.password), color = LocalCustomColors.current.textSecondary) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = LocalCustomColors.current.inputBackground,
                focusedBorderColor = LocalCustomColors.current.lightGray,
                unfocusedBorderColor = LocalCustomColors.current.lightGray,
                focusedTextColor = LocalCustomColors.current.text,
                unfocusedTextColor = LocalCustomColors.current.text,
            ),
            shape = MaterialTheme.shapes.medium.copy(CornerSize(16.dp))
        )

        val displayedError = googleAuthError ?: errorMessage
        if (displayedError != null) {
            Text(
                text = displayedError,
                color = Red,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = {
                authViewModel.clearError()
                if (isRegisterMode) {
                    authViewModel.signUpWithEmail(context, email.trim(), password, onSuccess)
                } else {
                    authViewModel.signInWithEmail(context, email.trim(), password, onSuccess)
                }
            },
            enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LocalCustomColors.current.btnAddBackground,
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = LocalCustomColors.current.btnAddText,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = stringResource(if (isRegisterMode) R.string.sign_up else R.string.sign_in),
                    fontWeight = FontWeight.SemiBold,
                    color = LocalCustomColors.current.btnAddText
                )
            }
        }

        TextButton(
            onClick = {
                isRegisterMode = !isRegisterMode
                authViewModel.clearError()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(if (isRegisterMode) R.string.have_account_login else R.string.no_account_register),
                color = LocalCustomColors.current.blue,
                fontSize = 13.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = LocalCustomColors.current.lightGray)
            Text(
                text = stringResource(R.string.or),
                color = LocalCustomColors.current.textSecondary,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = LocalCustomColors.current.lightGray)
        }

        OutlinedButton(
            onClick = onGoogleSignInClick,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, LocalCustomColors.current.lightGray)
        ) {
            Icon(
                painter = painterResource(R.drawable.google_logo),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.continue_with_google),
                color = LocalCustomColors.current.text,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ProfileHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp)
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(LocalCustomColors.current.blue),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.user),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(46.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.app_name),
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = LocalCustomColors.current.text
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.hello),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = LocalCustomColors.current.textSecondary
        )
    }
}

@Composable
private fun SettingsRow(
    icon: Int,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(LocalCustomColors.current.lightBlue),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = null,
                tint = LocalCustomColors.current.blue,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = LocalCustomColors.current.text
            )
            Text(
                text = value,
                fontSize = 13.sp,
                color = LocalCustomColors.current.textSecondary
            )
        }

        Icon(
            painter = painterResource(R.drawable.arrow_left),
            contentDescription = null,
            tint = LocalCustomColors.current.textSecondary,
            modifier = Modifier
                .size(18.dp)
                .graphicsLayer(rotationZ = 180f)
        )
    }
}

@Composable
private fun SelectableOptionsSheet(
    title: String,
    options: List<Pair<String, Boolean>>,
    onSelect: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = title.uppercase(),
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = LocalCustomColors.current.text,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        options.forEachIndexed { index, (label, selected) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onSelect(index) }
                    .padding(vertical = 8.dp, horizontal = 4.dp)
            ) {
                Text(
                    text = label,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.SemiBold,
                    color = LocalCustomColors.current.text
                )
                RadioButton(
                    selected = selected,
                    onClick = { onSelect(index) },
                    colors = RadioButtonDefaults.colors(selectedColor = LocalCustomColors.current.blue)
                )
            }
        }
    }
}

@Composable
fun ThemeBottomSheetContent(
    currentTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val themeOptions = listOf(
        stringResource(R.string.light_theme) to false,
        stringResource(R.string.dark_theme) to true,
    )

    SelectableOptionsSheet(
        title = stringResource(R.string.change_theme),
        options = themeOptions.map { (label, isDark) -> label to (isDark == currentTheme) },
        onSelect = { index -> onThemeChange(themeOptions[index].second) }
    )
}

@Composable
fun LanguageBottomSheetContent(
    currentLanguage: String,
    onLanguageChange: (String) -> Unit
) {
    val languageOptions = listOf(
        "Українська" to "uk",
        "English" to "en",
    )

    SelectableOptionsSheet(
        title = stringResource(R.string.language),
        options = languageOptions.map { (label, code) -> label to (code == currentLanguage) },
        onSelect = { index -> onLanguageChange(languageOptions[index].second) }
    )
}
