package com.example.cutview2.mainAppComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch

@Composable
fun <T> SelectorDialog(
    title : String,
    currentItem: T,
    listItems: List<T>,
    updateFunc: (T) -> Unit,
    onDismissRequest: () -> Unit,
    textStyle: TextStyle
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        text = title, modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(8.dp))
                    Divider()
                    SelectorDialogColumn(listItems = listItems, currentItem = currentItem,
                        updateFunc = updateFunc, onDismissRequest = onDismissRequest,
                        textStyle = textStyle, modifier = Modifier
                    )
                }

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(
                        modifier = Modifier,
                        onClick = { onDismissRequest() }){
                        Text("Dismiss", style = textStyle)
                    }
                }

            }
        }
    }
}

@Composable
fun <T> SelectorDialogColumn(listItems: List<T>, currentItem: T, updateFunc: (T) -> Unit, onDismissRequest: () -> Unit, textStyle: TextStyle, modifier: Modifier) {
    var currentIndex = 0
    val coroutineScope = rememberCoroutineScope()
    val state = rememberLazyListState()
    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        state = state
    ) {
        listItems.forEachIndexed() {index, selectorItem ->
            if (currentItem == selectorItem) {
                currentIndex = index
            }
            item {
                ListItem(
                    headlineContent = { Text(selectorItem.toString(), style = textStyle) },
                    leadingContent = { RadioButton(selected = (currentItem == selectorItem), onClick = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (currentItem == selectorItem),
                            onClick = {
                                updateFunc(selectorItem)
                                onDismissRequest()
                            },
                            role = Role.RadioButton
                        ),
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
            }
        }
        coroutineScope.launch{
            state.scrollToItem(index = currentIndex)
        }
    }
}

// currentIndex is to be determined by whatever is calling Selector function
@Composable
fun <T> Selector(
    listItems: List<T>,
    currentItem: T,
    dialogTitle: String,
    updateFunc: (T) -> Unit,
    textStyle: TextStyle
) {
    // make currentItem.toString the button label
    Selector(
        listItems = listItems,
        currentItem = currentItem,
        dialogTitle = dialogTitle,
        buttonLabel = currentItem.toString(),
        updateFunc = updateFunc,
        textStyle = textStyle
    )
}
@Composable
fun <T> Selector(
    listItems: List<T>,
    currentItem: T,
    buttonLabel: String,
    dialogTitle: String,
    updateFunc: (T) -> Unit,
    textStyle: TextStyle
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        // Button to toggle the menu
        ElevatedButton(onClick = { expanded = true }) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ){
                Text(buttonLabel, style = textStyle)
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "$buttonLabel Dropdown icon",
                )
            }
        }
        // Dialog launch here
        if (expanded) {
            SelectorDialog(
                title = dialogTitle,
                currentItem = currentItem,
                listItems = listItems,
                updateFunc = updateFunc,
                onDismissRequest = {expanded = false},
                textStyle = textStyle
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectorPreview() {
    val listItems = listOf("A", "B", "C")
    Selector(
        listItems = listItems,
        currentItem = listItems[1],
        dialogTitle = "Title",
        updateFunc = {},
        textStyle = TextStyle()
    )
}
@Preview(showBackground = true)
@Composable
fun SelectorDialogPreviewLotsOfItems() {
    SelectorDialog(
        title = "Title",
        currentItem = 2,
        listItems = (1..100).toList(),
        updateFunc = {},
        onDismissRequest = {},
        textStyle = TextStyle()
    )
}

@Preview(showBackground = true)
@Composable
fun SelectorDialogPreview() {
    SelectorDialog(
        title = "Title",
        currentItem = "A",
        listItems = listOf("A", "B", "C"),
        updateFunc = {},
        onDismissRequest = {},
        textStyle = TextStyle()
    )
}