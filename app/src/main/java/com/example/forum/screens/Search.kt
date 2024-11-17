package com.example.forum.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.forum.data.models.UserModel
import com.example.forum.viewmodels.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(navHostController: NavHostController) {
    val searchViewModel: SearchViewModel = viewModel()
    val userList by searchViewModel.userList.observeAsState()

    var search by remember{
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Search",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            placeholder = { Text(text = "Search...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                containerColor = MaterialTheme.colorScheme.surface,
                focusedLabelColor = MaterialTheme.colorScheme.onSurface
            ),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant // Ensure correct color
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            if(userList!= null){

                val filterItems = userList!!.filter { it.name.contains(search,ignoreCase = true) || it.userName.contains(search,ignoreCase = false)}
                items(filterItems ?: emptyList()) { user ->
                    SearchItemCard(user, navHostController)
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
                    )
                }
            }

        }
    }
}
@Composable
fun SearchItemCard(user: UserModel, navHostController: NavHostController) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Profile Picture, Username, and Name
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Profile Image
            Image(
                painter = rememberAsyncImagePainter(model = user.imageUri),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // User Details (Username and Name)
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = user.userName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    // Verified Icon (Optional)
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Verified",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = user.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Follow Button
        Button(
            onClick = { /* Handle follow action */ },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Text(text = "Follow", style = MaterialTheme.typography.bodyMedium)
        }
    }
}