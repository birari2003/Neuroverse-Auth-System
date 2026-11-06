package com.neuroverse.auth.ui.features.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neuroverse.auth.domain.model.AuthSession
import com.neuroverse.auth.ui.components.feedback.LoadingIndicator
import com.neuroverse.auth.ui.theme.AuthAppTheme
import com.neuroverse.auth.ui.theme.BrandGradient
import com.neuroverse.auth.ui.theme.Dimens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    session: AuthSession?,
    onNavigateToHome: () -> Unit,
    onNavigateToWelcome: () -> Unit
) {
    LaunchedEffect(Unit) {
        // Minimum branded display time
        delay(800)
        if (session != null) {
            onNavigateToHome()
        } else {
            onNavigateToWelcome()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(BrandGradient),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Shield,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(42.dp)
                )
            }
            Spacer(modifier = Modifier.height(Dimens.xl))
            Box(modifier = Modifier.size(48.dp)) {
                LoadingIndicator()
            }
        }
    }
}

@Preview(name = "Splash Light", showBackground = true)
@Composable
private fun SplashScreenPreview() {
    AuthAppTheme {
        SplashScreen(
            session = null,
            onNavigateToHome = {},
            onNavigateToWelcome = {}
        )
    }
}
