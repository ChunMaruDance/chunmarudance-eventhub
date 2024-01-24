package com.chunmaru.eventhub.presentation.screens.admin_screen.elements



//@Composable
//fun AdminSearchBar() {
//
//    SearchBar(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(60.dp)
//            .padding(start = 10.dp, end = 10.dp, top = 5.dp),
//        query = searchTextState.value,
//        onQueryChange = {
//            viewModel.search(it)
//        },
//        onSearch = {
//            active.value = false
//        },
//        active = active.value,
//        onActiveChange = {
//            active.value = it
//        },
//        leadingIcon = {
//            Icon(
//                painter = painterResource(id = R.drawable.search_icon),
//                contentDescription = "Search Icon",
//                tint = Color(android.graphics.Color.parseColor("#bdec3a")),
//                modifier = Modifier.size(25.dp),
//            )
//        },
//        colors = SearchBarDefaults.colors(
//            containerColor = Color(android.graphics.Color.parseColor("#1A1A1A")),
//            dividerColor = androidx.compose.ui.graphics.Color.Gray
//        ),
//        placeholder = {
//            Text(text = "Search", color = androidx.compose.ui.graphics.Color.Gray)
//        },
//        trailingIcon = {
//            if (active.value) {
//                Icon(
//                    imageVector = Icons.Default.Close,
//                    contentDescription = "Close",
//                    modifier = Modifier
//                        .clickable {
//                            if (searchTextState.value.isNotEmpty())
//                                viewModel.search("")
//                            else
//                                active.value = false
//                        }
//                        .size(25.dp),
//                    tint = Color(android.graphics.Color.parseColor("#bdec3a")),
//                )
//            }
//        }
//    ) {
//    }
//}