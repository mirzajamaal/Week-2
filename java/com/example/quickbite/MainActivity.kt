package com.example.quickbite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quickbite.ui.theme.QuickBiteTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val Typography = Typography(
    h1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    ),
    h2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    h3 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    // Define other text styles as needed
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase

        setContent {
            QuickBiteTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    QuickBiteApp()
                }
            }
        }
    }
}

@Composable
fun QuickBiteApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("login") {
            LoginScreen(navController)
        }
        composable("signup") {
            SignupScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("menu/{restaurantId}") { backStackEntry ->
            val restaurantId = backStackEntry.arguments?.getString("restaurantId")?.toInt() ?: 0
            MenuScreen(restaurantId, navController)
        }
        composable("cart") {
            CartScreen(navController)
        }
        composable("profile") {
            ProfileScreen(navController)
        }
        composable("order_history") {
            OrderHistoryScreen(navController)
        }
    }
}

class SplashViewModel : ViewModel() {
    fun startSplashScreen(onTimeout: () -> Unit) {
        viewModelScope.launch {
            delay(2000L)
            onTimeout()
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    val viewModel: SplashViewModel = viewModel()

    LaunchedEffect(Unit) {
        viewModel.startSplashScreen {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "QuickBite",
            style = MaterialTheme.typography.h3,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Name
        Text(
            text = "QuickBite",
            style = MaterialTheme.typography.h3.copy(
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon")
            },
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon")
            },
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Error Message
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Login Button
        Button(
            onClick = {
                // Call onLoginSuccess() to navigate to the home screen
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Login", style = MaterialTheme.typography.button)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign Up Text
        TextButton(onClick = {
            // Navigate to sign up screen
            navController.navigate("signup") {
                popUpTo("login") { inclusive = true }
            }
        }) {
            Text("Don't have an account? Sign Up")
        }
    }
}

@Composable
fun SignupScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Name
        Text(
            text = "QuickBite",
            style = MaterialTheme.typography.h3.copy(
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon")
            },
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon")
            },
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Confirm Password Input
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Confirm Password Icon")
            },
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Error Message
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Signup Button
        Button(
            onClick = {
                // Signup logic here
                navController.navigate("home") {
                    popUpTo("signup") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Sign Up", style = MaterialTheme.typography.button)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Back to Login Text
        TextButton(onClick = {
            // Navigate back to the login screen
            navController.navigate("login") {
                popUpTo("signup") { inclusive = true }
            }
        }) {
            Text("Already have an account? Log In")
        }
    }
}

@Composable
fun HomeScreen(navController: NavController, cartViewModel: CartViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    val allRestaurants = listOf(
        Restaurant(1, "Pizza Place", "123 Main St", "Pizza"),
        Restaurant(2, "Burger Joint", "456 Elm St", "Burgers"),
        Restaurant(3, "Sushi Spot", "789 Oak St", "Sushi")
    )

    // Filter restaurants based on search query
    val filteredRestaurants = allRestaurants.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.cuisine.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search for restaurants or dishes") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
            }
        )

        // Restaurant Listings
        LazyColumn {
            items(filteredRestaurants) { restaurant ->
                RestaurantItem(restaurant) {
                    navController.navigate("menu/${restaurant.id}")
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Navigation Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = {
                navController.navigate("cart")
            }) {
                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Cart Icon")
            }
            IconButton(onClick = {
                navController.navigate("profile")
            }) {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Profile Icon")
            }
            IconButton(onClick = {
                navController.navigate("order_history")
            }) {
                Icon(imageVector = Icons.Default.Info, contentDescription = "Order History Icon")
            }
        }
    }
}

@Composable
fun RestaurantItem(restaurant: Restaurant, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = restaurant.name, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = restaurant.address, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = restaurant.cuisine, style = MaterialTheme.typography.body2)
        }
    }
}

@Composable
fun MenuScreen(restaurantId: Int, navController: NavController, cartViewModel: CartViewModel = viewModel()) {
    // Sample menu items for demonstration
    val menuItems = listOf(
        MenuItem(1, "Burger", "Delicious beef burger", 5.99),
        MenuItem(2, "Pizza", "Cheesy pizza with toppings", 8.99),
        MenuItem(3, "Pasta", "Italian pasta with sauce", 7.49)
    )

    var selectedMenuItem by remember { mutableStateOf<MenuItem?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Menu for Restaurant $restaurantId", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))

        // Menu Items
        LazyColumn {
            items(menuItems) { menuItem ->
                MenuItemCard(menuItem) {
                    selectedMenuItem = menuItem
                    showDialog = true
                }
            }
        }

        // Cart Button
        Button(
            onClick = {
                navController.navigate("cart")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(48.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("View Cart", style = MaterialTheme.typography.button)
        }
    }

    if (showDialog && selectedMenuItem != null) {
        AddToCartDialog(
            menuItem = selectedMenuItem!!,
            onDismiss = { showDialog = false },
            cartViewModel = cartViewModel
        )
    }
}

@Composable
fun MenuItemCard(menuItem: MenuItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = menuItem.name, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = menuItem.description, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "$${menuItem.price}", style = MaterialTheme.typography.body2)
        }
    }
}

@Composable
fun CartScreen(navController: NavController, cartViewModel: CartViewModel = viewModel()) {
    val cartItems by remember { mutableStateOf(cartViewModel.cartItems) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Your Cart", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(cartItems) { cartItem ->
                CartItemCard(cartItem)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                // Proceed to checkout
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Proceed to Checkout", style = MaterialTheme.typography.button)
        }
    }
}

@Composable
fun CartItemCard(cartItem: CartItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = cartItem.name, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = cartItem.description, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Quantity: ${cartItem.quantity}", style = MaterialTheme.typography.body2)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Customization: ${cartItem.customization}", style = MaterialTheme.typography.body2)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "$${cartItem.price * cartItem.quantity}", style = MaterialTheme.typography.h6)
        }
    }
}

@Composable
fun AddToCartDialog(
    menuItem: MenuItem,
    onDismiss: () -> Unit,
    cartViewModel: CartViewModel
) {
    var quantity by remember { mutableStateOf(1) }
    var customization by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add ${menuItem.name} to Cart") },
        text = {
            Column {
                Text("Price: $${menuItem.price}")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = quantity.toString(),
                    onValueChange = { quantity = it.toIntOrNull() ?: 1 },
                    label = { Text("Quantity") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = customization,
                    onValueChange = { customization = it },
                    label = { Text("Customization") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                // Add to cart logic here
                cartViewModel.addToCart(
                    menuItem = menuItem,
                    quantity = quantity,
                    customization = customization
                )
                onDismiss()  // Dismiss the dialog after adding the item to the cart
            }) {
                Text("Add to Cart")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

data class MenuItem(val id: Int, val name: String, val description: String, val price: Double)
data class CartItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    var quantity: Int,
    var customization: String
)

class CartViewModel : ViewModel() {
    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: List<CartItem> get() = _cartItems

    fun addToCart(menuItem: MenuItem, quantity: Int, customization: String) {
        val existingItem = _cartItems.find { it.id == menuItem.id }
        if (existingItem != null) {
            existingItem.quantity += quantity
            existingItem.customization = customization
        } else {
            _cartItems.add(
                CartItem(
                    id = menuItem.id,
                    name = menuItem.name,
                    description = menuItem.description,
                    price = menuItem.price,
                    quantity = quantity,
                    customization = customization
                )
            )
        }
    }
}

@Composable
fun ProfileScreen(navController: NavController) {
    var name by remember { mutableStateOf("John Doe") }
    var address by remember { mutableStateOf("123 Main St") }
    var profilePicture by remember { mutableStateOf("https://example.com/profile.jpg") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Your Profile", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))
        // Profile details and edit functionality go here
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Name Icon")
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Home, contentDescription = "Address Icon")
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Profile picture change logic goes here

        Button(
            onClick = {
                // Save profile changes
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Save Changes", style = MaterialTheme.typography.button)
        }
    }
}

@Composable
fun OrderHistoryScreen(navController: NavController) {
    // Placeholder for OrderHistoryScreen implementation
    // Fetch and display user's past orders
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Order History", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))
        // Order history items go here
    }
}

data class Restaurant(val id: Int, val name: String, val address: String, val cuisine: String)

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    QuickBiteTheme {
        QuickBiteApp()
    }
}
