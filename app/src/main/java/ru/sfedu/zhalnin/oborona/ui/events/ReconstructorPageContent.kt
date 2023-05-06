package ru.sfedu.zhalnin.oborona.ui.events

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.data.model.dto.CostumeForm
import ru.sfedu.zhalnin.oborona.ui.common.InputField
import ru.sfedu.zhalnin.oborona.ui.common.PrimaryButton
import ru.sfedu.zhalnin.oborona.ui.dialog.ConfirmationDialog
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

private const val Ru = "ru"

private const val En = "en"

private const val True = "True"

private const val False = "False"

@Composable
fun ReconstructorPageContent(
    modifier: Modifier = Modifier,
    sendData: (CostumeForm) -> Unit,
    backClicked: () -> Unit
) {
    var confirmationDialogVisible by remember { mutableStateOf(false) }

    BackHandler(true) {
        if (confirmationDialogVisible) {
            confirmationDialogVisible = false
        } else {
            backClicked()
        }
    }

    var rusSelected by remember { mutableStateOf(true) }

    var gunSelected by remember { mutableStateOf(false) }
    var costumeSelected by remember { mutableStateOf(false) }
    var cazarmaChecked by remember { mutableStateOf(false) }

    var chest by remember { mutableStateOf("") }
    var waist by remember { mutableStateOf("") }
    var hips by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var shoes by remember { mutableStateOf("") }

    val chestValid =
        chest.isNotBlank() &&
                chest.toInt() <= CostumeConstants.CHEST_MAX &&
                chest.toInt() >= CostumeConstants.CHEST_MIN

    val waistValid =
        waist.isNotBlank() &&
                waist.toInt() <= CostumeConstants.TAL_MAX &&
                waist.toInt() >= CostumeConstants.TAL_MIN

    val hipsValid =
        hips.isNotBlank() &&
                hips.toInt() <= CostumeConstants.BEDR_MAX &&
                hips.toInt() >= CostumeConstants.BEDR_MIN

    val heightValid =
        height.isNotBlank() &&
                height.toInt() <= CostumeConstants.HEIGHT_MAX &&
                height.toInt() >= CostumeConstants.HEIGHT_MIN

    val shoesValid =
        shoes.isNotBlank() &&
                shoes.toInt() <= CostumeConstants.SHOES_MAX &&
                shoes.toInt() >= CostumeConstants.SHOES_MIN

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .background(AppTheme.colors.primary)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            val (backBtnRef, tittleRef) = createRefs()

            IconButton(
                modifier = Modifier.constrainAs(backBtnRef) {
                    start.linkTo(parent.start)
                    top.linkTo(tittleRef.top)
                    bottom.linkTo(tittleRef.bottom)
                },
                onClick = {
                    backClicked()
                }
            ) {
                Icon(
                    imageVector = AppTheme.icons.Back,
                    tint = AppTheme.colors.onPrimary,
                    contentDescription = null
                )
            }

            Text(
                modifier = Modifier
                    .padding(all = 0.dp)
                    .constrainAs(tittleRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                style = AppTheme.typography.header2,
                color = AppTheme.colors.onPrimary,
                text = stringResource(R.string.rpReconstructor)
            )
        }

        Column(
            modifier = Modifier
                .height(screenHeight)
                .background(
                    color = AppTheme.colors.background,
                    shape = AppTheme.shapes.backgroundShape
                )
        ) {
            Text(
                text = stringResource(R.string.rpSide),
                modifier = Modifier.padding(16.dp),
                style = AppTheme.typography.text2,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        modifier = Modifier.padding(),
                        selected = rusSelected,
                        onClick = { rusSelected = true },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = AppTheme.colors.primary,
                            unselectedColor = AppTheme.colors.onBackgroundVariant
                        )
                    )
                    Text(
                        text = stringResource(R.string.rpRussia),
                        modifier = Modifier.clickable {
                            rusSelected = true
                        },
                        style = AppTheme.typography.text2,
                        color = AppTheme.colors.onBackgroundVariant
                    )
                }
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        modifier = Modifier.padding(),
                        selected = !rusSelected,
                        onClick = { rusSelected = false },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = AppTheme.colors.primary,
                            unselectedColor = AppTheme.colors.onBackgroundVariant
                        )
                    )
                    Text(
                        text = stringResource(R.string.rpEng),
                        modifier = Modifier.clickable {
                            rusSelected = false
                        },
                        style = AppTheme.typography.text2,
                        color = AppTheme.colors.onBackgroundVariant
                    )
                }
            }
            Text(
                text = stringResource(R.string.rpReq),
                modifier = Modifier.padding(16.dp),
                style = AppTheme.typography.text2
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        modifier = Modifier.padding(),
                        checked = gunSelected,
                        onCheckedChange = {
                            gunSelected = it
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = AppTheme.colors.primary,
                            uncheckedColor = AppTheme.colors.onBackgroundVariant
                        )
                    )
                    Text(
                        text = stringResource(R.string.rpGun),
                        modifier = Modifier.clickable {
                            gunSelected = !gunSelected
                        },
                        color = AppTheme.colors.onBackgroundVariant,
                        style = AppTheme.typography.text2
                    )
                }
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        modifier = Modifier.padding(),
                        checked = costumeSelected,
                        onCheckedChange = {
                            costumeSelected = it
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = AppTheme.colors.primary,
                            uncheckedColor = AppTheme.colors.onBackgroundVariant
                        )
                    )
                    Text(
                        text = stringResource(R.string.rpCostume),
                        modifier = Modifier.clickable {
                            costumeSelected = !costumeSelected
                        },
                        color = AppTheme.colors.onBackgroundVariant,
                        style = AppTheme.typography.text2
                    )
                }
            }
            if (!costumeSelected) {
                Text(
                    text = stringResource(R.string.rpSize),
                    modifier = Modifier.padding(16.dp),
                    style = AppTheme.typography.text2
                )
                Row {
                    InputField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp, end = 12.dp),
                        value = chest,
                        onChange = { chest = it },
                        label = stringResource(R.string.rpSize1),
                        keyboardType = KeyboardType.Decimal,
                        error = if (!chestValid) stringResource(R.string.rpBadFormat) else null
                    )
                    InputField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp, end = 16.dp),
                        value = waist,
                        onChange = { waist = it },
                        label = stringResource(R.string.rpSize2),
                        keyboardType = KeyboardType.Decimal,
                        error = if (!waistValid) stringResource(R.string.rpBadFormat) else null
                    )
                }
                Row {
                    InputField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp, end = 12.dp),
                        value = hips,
                        onChange = { hips = it },
                        label = stringResource(R.string.rpSize3),
                        keyboardType = KeyboardType.Decimal,
                        error = if (!hipsValid) stringResource(R.string.rpBadFormat) else null
                    )
                    InputField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp, end = 16.dp),
                        value = height,
                        onChange = { height = it },
                        label = stringResource(R.string.rpSize4),
                        keyboardType = KeyboardType.Decimal,
                        error = if (!heightValid) stringResource(R.string.rpBadFormat) else null
                    )
                }
                Row {
                    InputField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp, end = 12.dp),
                        value = shoes,
                        onChange = { shoes = it },
                        label = stringResource(R.string.rpSize5),
                        keyboardType = KeyboardType.Decimal,
                        error = if (!shoesValid) stringResource(R.string.rpBadFormat) else null
                    )
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp, end = 16.dp),
                    )
                }
            }
            Text(
                text = stringResource(R.string.rpLiving),
                modifier = Modifier.padding(16.dp),
                style = AppTheme.typography.text2
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    modifier = Modifier.padding(),
                    checked = cazarmaChecked,
                    onCheckedChange = {
                        cazarmaChecked = it
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = AppTheme.colors.primary,
                        uncheckedColor = AppTheme.colors.onBackgroundVariant
                    )
                )
                Text(
                    text = stringResource(R.string.rpBaracs),
                    modifier = Modifier.clickable {
                        cazarmaChecked = !cazarmaChecked
                    },
                    color = AppTheme.colors.onBackgroundVariant,
                    style = AppTheme.typography.text2
                )
            }

            PrimaryButton(
                modifier = Modifier.padding(16.dp),
                text = stringResource(R.string.rpSend),
                enabled = costumeSelected || (chestValid && waistValid && hipsValid && heightValid && shoesValid),
                onClick = {
                    sendData(
                        CostumeForm(
                            side = if (rusSelected) Ru else En,
                            weapon = if (gunSelected) True else False,
                            costume = if (costumeSelected) True else False,
                            barrack = if (cazarmaChecked) True else False,
                            chest = if (costumeSelected) null else chest,
                            waist = if (costumeSelected) null else waist,
                            hips = if (costumeSelected) null else hips,
                            height = if (costumeSelected) null else height,
                            shoes = if (costumeSelected) null else shoes,
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        AnimatedVisibility(
            visible = confirmationDialogVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ConfirmationDialog(
                question = stringResource(R.string.rpDialogQuestion),
                onDismiss = {
                    confirmationDialogVisible = false
                },
                onConfirm = {
                    confirmationDialogVisible = false
                }
            )
        }
    }
}

@Preview
@Composable
fun ReconstructorPageContentPreview() {
    AppTheme {
        ReconstructorPageContent(
            sendData = {},
            backClicked = {}
        )
    }
}

private object CostumeConstants {
    const val BEDR_MIN = 0
    const val BEDR_MAX = 200

    const val TAL_MIN = 0
    const val TAL_MAX = 200

    const val HEIGHT_MIN = 55
    const val HEIGHT_MAX = 251

    const val SHOES_MIN = 20
    const val SHOES_MAX = 60

    const val CHEST_MIN = 0
    const val CHEST_MAX = 200
}