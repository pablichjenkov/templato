package com.macaosoftware.component.demo.viewmodel.noviewmodel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.macaosoftware.component.core.Component
import com.macaosoftware.component.core.BackPressHandler

class SimpleComponent(
    val screenName: String,
    val bgColor: Color,
    val onMessage: (Msg) -> Unit
) : Component() {

    override fun onAttach() {
        println("${instanceId()}::onAttach()")
    }

    override fun onActive() {
        println("${instanceId()}::onStart()")
    }

    override fun onInactive() {
        println("${instanceId()}::onStop()")
    }

    override fun onDetach() {
        println("${instanceId()}::onDetach()")
    }

    sealed interface Msg {
        object Next : Msg
    }

    @Composable
    override fun Content(modifier: Modifier) {
        println("${instanceId()}::Composing()")
        BackPressHandler()
        Column (
            modifier = modifier.fillMaxSize()
                .background(bgColor)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier.padding(vertical = 40.dp),
                onClick = { onMessage(Msg.Next) }
            ) {
                Text(text = "Next")
            }
            SignUpForm({onMessage(Msg.Next)},{onMessage(Msg.Next)})
        }
    }

}

@Composable
fun SignUpForm(
    onSignIn: () -> Unit,
    onSignUp: () -> Unit
) {
    var fullname by remember { mutableStateOf("Matias Duarte") }
    var username by remember { mutableStateOf("duarte@gmail.com") }
    var password by remember { mutableStateOf("duartesStrongPassword") }
    var errorMessage by remember { mutableStateOf("") }
    var acceptedTerms by remember { mutableStateOf(true) }

    val focus = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    /*Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface
    ) { padding ->*/
    Column(
        Modifier
            //.padding(padding)
            .padding(horizontal = 16.dp)
            .padding(top = 32.dp)
    ) {
        Text(
            text = "Create Free Account",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Gain access to the full Composables catalog",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(24.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = fullname,
            label = { Text("Full name") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focus.moveFocus(FocusDirection.Next)
                }
            ),
            onValueChange = { fullname = it },
            singleLine = true
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = username,
            label = { Text("E-mail") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focus.moveFocus(FocusDirection.Next)
                }
            ),
            onValueChange = { username = it },
            singleLine = true
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Full name") },
            isError = errorMessage.isNotBlank(),
            value = password,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focus.clearFocus()
                    keyboardController?.hide()
                    //onSubmit()
                }
            ),
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = {
                password = it
            },
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))
        val interactionSource = remember { MutableInteractionSource() }
        Row(
            Modifier
                .fillMaxWidth()
                .clickable(interactionSource,
                    indication = ripple(),
                    onClick = {
                        acceptedTerms = acceptedTerms.not()
                    })
                .padding(horizontal = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = acceptedTerms,
                onCheckedChange = {
                    acceptedTerms = it
                },
                interactionSource = interactionSource
            )
        }

        TermsAndConditions()

        Spacer(Modifier.height(16.dp))
        Column(Modifier.padding(horizontal = 16.dp)) {
            Button(
                enabled = acceptedTerms && fullname.isNotBlank()
                        && username.isNotBlank()
                        && password.isNotBlank(),
                onClick = onSignUp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up")
            }
            Spacer(Modifier.height(8.dp))
            TextButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = onSignIn,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.66f
                    )
                )
            ) {
                Text("Already have an account? Sign in")
            }
        }
    }
    //}
}

@Composable
fun TermsAndConditions() {
    val fullText = terms
    val clickableText = "Terms & Conditions"
    val tag = "terms-and-conditions"

    val annotatedString = buildAnnotatedString {
        append(fullText)
        val start = fullText.indexOf(clickableText)
        // val end = start + clickableText.length

        addStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            ),
            start = 17,
            end = 45
        )

        addStringAnnotation(
            tag = tag,
            annotation = "https://www.composables.com",
            start = 17,
            end = 45
        )
    }
    val uriHandler = LocalUriHandler.current
    ClickableText(
        style = MaterialTheme.typography.bodyMedium.copy(
            color = LocalContentColor.current
        ),
        text = annotatedString,
        onClick = { offset ->
            annotatedString
                .getStringAnnotations(tag, offset, offset)
                .firstOrNull()
                ?.let { string ->
                    uriHandler.openUri(string.item)
                }
        }
    )
}

val terms =
    "Lorem ipsum dolor https://www.composables.com sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Rhoncus dolor purus non enim praesent elementum facilisis. Viverra suspendisse potenti nullam ac tortor vitae purus faucibus. Urna condimentum mattis pellentesque id nibh tortor. Id velit ut tortor pretium viverra suspendisse potenti nullam ac. Egestas maecenas pharetra convallis posuere morbi leo urna molestie at. Vitae aliquet nec ullamcorper sit amet risus. Phasellus vestibulum lorem sed risus ultricies. Justo donec enim diam vulputate ut. Et leo duis ut diam. Purus sit amet volutpat consequat mauris nunc congue. Amet consectetur adipiscing elit duis tristique sollicitudin nibh sit. Cras fermentum odio eu feugiat pretium nibh. Velit dignissim sodales ut eu sem integer vitae. Felis imperdiet proin fermentum leo vel orci. Ultrices eros in cursus turpis massa. Faucibus scelerisque eleifend donec pretium vulputate sapien. At volutpat diam ut venenatis tellus. At augue eget arcu dictum.\n" +
            "Ut faucibus pulvinar elementum integer enim neque volutpat ac tincidunt. Vestibulum lorem sed risus ultricies tristique nulla aliquet enim. Morbi quis commodo odio aenean sed adipiscing diam donec. Gravida rutrum quisque non tellus orci. Consequat ac felis donec et odio pellentesque diam. Augue eget arcu dictum varius duis at consectetur lorem. Enim diam vulputate ut pharetra sit amet. Sollicitudin nibh sit amet commodo nulla facilisi nullam vehicula ipsum. Velit scelerisque in dictum non consectetur. Nulla facilisi etiam dignissim diam quis. Molestie ac feugiat sed lectus vestibulum. Feugiat pretium nibh ipsum consequat nisl vel pretium lectus. Sem integer vitae justo eget magna fermentum iaculis eu. Ullamcorper velit sed ullamcorper morbi. Posuere morbi leo urna molestie at elementum. Vivamus arcu felis bibendum ut tristique et egestas quis ipsum. Arcu dictum varius duis at consectetur lorem donec massa sapien. Ultrices dui sapien eget mi proin. Metus dictum at tempor commodo ullamcorper a lacus vestibulum sed."

