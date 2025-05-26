package com.example.talenta

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.talenta.data.model.Bio
import com.example.talenta.data.model.Certificate
import com.example.talenta.data.model.Ethnicity
import com.example.talenta.data.model.Media
import com.example.talenta.data.model.MediaType
import com.example.talenta.data.model.PhysicalAttributes
import com.example.talenta.data.model.ProfessionalData
import com.example.talenta.data.model.SocialMediaLinks
import com.example.talenta.data.model.SponsorDetails
import com.example.talenta.data.model.SponsorType
import com.example.talenta.data.model.User


object Test {
    val mockUser = User(
        id = "user_001",
        firstName = "John",
        lastName = "Doe",
        email = "john.doe@example.com",
        phoneNumber = "+1234567890",
        physicalAttributes = PhysicalAttributes(
            height = "180 cm",
            weight = "75 kg",
            gender = "Male",
            age = 30,
            ethnicity = Ethnicity.WHITE,
            color = "Fair"
        ),
        profilePicture = "https://example.com/images/john_doe.jpg",
        bio = Bio(
            city = "New York",
            country = "USA",
            bioData = "Professional guitarist and music teacher with over 10 years of experience.",
            language = "English",
            socialMediaLinks = SocialMediaLinks(
                facebook = "john.doe.fb",
                instagram = "john_doe_ig",
                linkedin = "john-doe-linkedin",
                twitter = "john_doe_tw"
            )
        ),
        role = null,
        isVerified = true,
        isBlocked = false,
        professionalData = ProfessionalData(
            profession = "Musician",
            subProfession = "Guitarist",
            media = listOf(
                Media(
                    url = "https://example.com/media/guitar1.jpg",
                    type = MediaType.IMAGE,
                    description = "Playing live concert",
                    timestamp = System.currentTimeMillis()
                )
            ),
            skills = listOf("GUITAR", "THEORY_TEACHING"),
            certifications = listOf("Guitar Level 1", "Music Theory Certificate"),
            certificatesList = listOf(
                Certificate(
                    id = "cert_01",
                    imageUrl = "https://example.com/certificates/cert1.jpg",
                    description = "Guitar Level 1 Certificate",
                    timestamp = System.currentTimeMillis()
                )
            )
        ),
        sponsorDetails = SponsorDetails(
            sponsorType = SponsorType.INDIVIDUAL,
            profileInterests = listOf("Music", "Teaching"),
            companyName = "",
            address = ""
        )
    )

    val mockUsersList = listOf(
        mockUser,
        mockUser.copy(
            id = "user_002",
            firstName = "Alice",
            lastName = "Smith",
            bio = mockUser.bio.copy(city = "Los Angeles"),
            professionalData = mockUser.professionalData.copy(profession = "Singer")
        ),
        mockUser.copy(
            id = "user_003",
            firstName = "Bob",
            lastName = "Johnson",
            bio = mockUser.bio.copy(city = "Chicago"),
            professionalData = mockUser.professionalData.copy(profession = "Pianist")
        ),
        mockUser.copy(
            id = "user_004",
            firstName = "Carol",
            lastName = "Williams",
            bio = mockUser.bio.copy(city = "Miami"),
            professionalData = mockUser.professionalData.copy(profession = "Music Teacher")
        ),
        mockUser.copy(
            id = "user_005",
            firstName = "David",
            lastName = "Brown",
            bio = mockUser.bio.copy(city = "Seattle"),
            professionalData = mockUser.professionalData.copy(profession = "Music Therapist")
        ),
        mockUser.copy(
            id = "user_006",
            firstName = "Eva",
            lastName = "Davis",
            bio = mockUser.bio.copy(city = "Austin"),
            professionalData = mockUser.professionalData.copy(profession = "Singer")
        ),
        mockUser.copy(
            id = "user_007",
            firstName = "Frank",
            lastName = "Miller",
            bio = mockUser.bio.copy(city = "Denver"),
            professionalData = mockUser.professionalData.copy(profession = "Guitarist")
        ),
        mockUser.copy(
            id = "user_008",
            firstName = "Grace",
            lastName = "Wilson",
            bio = mockUser.bio.copy(city = "Boston"),
            professionalData = mockUser.professionalData.copy(profession = "Pianist")
        ),
        mockUser.copy(
            id = "user_009",
            firstName = "Henry",
            lastName = "Moore",
            bio = mockUser.bio.copy(city = "San Francisco"),
            professionalData = mockUser.professionalData.copy(profession = "Composer")
        ),
        mockUser.copy(
            id = "user_010",
            firstName = "Isabel",
            lastName = "Taylor",
            bio = mockUser.bio.copy(city = "Portland"),
            professionalData = mockUser.professionalData.copy(profession = "Instrumental Teacher")
        )
    )
}

@Composable
fun SponsorTemplateScreen(
    experts: List<User>,
    onSearchQueryChange: (String) -> Unit,
    searchQuery: String,
    onExpertClick: (User) -> Unit,
    modifier: Modifier = Modifier,
    showSearch: Boolean = true,
) {
    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Optional Search Bar
            if (showSearch) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Search") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Gray,
                    ),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items = experts) { expert ->
                    ExpertCard(expert = expert, onClick = { onExpertClick(expert) })
                }
            }

        }
    }

}

@Composable
fun ExpertCard(
    expert: User,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Image
            AsyncImage(
                model = expert.profilePicture,
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                placeholder = painterResource(R.drawable.placeholder),
                error = painterResource(R.drawable.placeholder)
            )

            Spacer(Modifier.height(5.dp))

            // Rating Row (Dummy)
            Row {
                repeat(5) { index ->
                    Icon(
                        painter = painterResource(id = if (index < 2) R.drawable.star_filled else R.drawable.star_outline),
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.run { size(16.dp) }
                    )
                }
            }

            Text(
                text = "Todo reviews",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(start = 4.dp)
            )

            Spacer(Modifier.height(5.dp))

            // Name & Location
            Text(
                text = "${expert.firstName} ${expert.lastName}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "${expert.professionalData.profession} | ${expert.bio.country}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Description
            Text(
                text = expert.bio.bioData,
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

