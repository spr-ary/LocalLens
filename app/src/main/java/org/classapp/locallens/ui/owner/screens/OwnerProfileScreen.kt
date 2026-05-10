package org.classapp.locallens.ui.owner.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.classapp.locallens.ui.owner.components.InfoLine
import org.classapp.locallens.ui.owner.components.ManagementButton
import org.classapp.locallens.ui.owner.components.OwnerTextField
import org.classapp.locallens.ui.owner.components.SectionTitle

@Composable
fun OwnerProfileScreen(
    ownerUsername: String,
    onOwnerUsernameChange: (String) -> Unit,
    ownerName: String,
    onOwnerNameChange: (String) -> Unit,
    ownerPhone: String,
    onOwnerPhoneChange: (String) -> Unit,
    onSaveProfile: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        SectionTitle("Profile")
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(82.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(ownerName.take(1), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                }
                Text(ownerName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("stall_owner", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                InfoLine("Username", ownerUsername)
                InfoLine("Phone", ownerPhone)
            }
        }

        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Edit Profile", fontWeight = FontWeight.Bold)
                OwnerTextField("Username", ownerUsername, onOwnerUsernameChange)
                OwnerTextField("Owner name", ownerName, onOwnerNameChange)
                OwnerTextField("Phone number", ownerPhone, onOwnerPhoneChange, KeyboardType.Phone)
                Button(onClick = onSaveProfile, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                    Text("Save Profile")
                }
            }
        }

        ManagementButton("Change Password", "Update account security", {})
        ManagementButton("Help / Contact Support", "Get help with your stall owner account", {})
        OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
            Text("Logout")
        }
    }
}
