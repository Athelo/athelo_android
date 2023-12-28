package com.athelohealth.mobile.utils


import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.model.activity.ActivityScreen
import com.athelohealth.mobile.presentation.model.base.Image
import com.athelohealth.mobile.presentation.model.chart.BarChartDataSet
import com.athelohealth.mobile.presentation.model.chart.CircleChartDataSet
import com.athelohealth.mobile.presentation.model.chat.Conversation
import com.athelohealth.mobile.presentation.model.chat.ConversationInfo
import com.athelohealth.mobile.presentation.model.chat.SimpleUser
import com.athelohealth.mobile.presentation.model.health.Symptom
import com.athelohealth.mobile.presentation.model.health.SymptomSummary
import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.presentation.model.sleep.SleepChartEntry
import com.athelohealth.mobile.presentation.model.sleep.SleepDetailScreen
import com.athelohealth.mobile.presentation.model.sleep.SleepSummaryScreen
import com.athelohealth.mobile.presentation.ui.theme.lightOlivaceous
import com.athelohealth.mobile.websocket.constant.ROUTABLE
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt

internal fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start + ((stop - start) * fraction)
}

internal fun getShiftedFraction(fraction: Float, shift: Float): Float {
    val newFraction = (fraction + shift) % 1
    return (if (newFraction > .5) 1 - newFraction else newFraction) * 2
}

internal fun mirrorIndex(index: Int, size: Int): Int {
    return if (index > (size / 2f).roundToInt() - 1) size - index - 1 else index
}

internal fun size(size: Dp): Modifier {
    return Modifier.size(size)
}

internal fun size(width: Dp, height: Dp): Modifier {
    return Modifier.size(width = width, height = height)
}

internal fun size(size: DpSize): Modifier {
    return Modifier.size(size)
}

internal fun createMockImage(size: Int): Image =
    Image(
        image5050 = "https://placekitten.com/$size/$size",
        image = "https://placekitten.com/$size/$size",
        image100100 = "https://placekitten.com/$size/$size",
        image250250 = "https://placekitten.com/$size/$size",
        image125125 = "https://placekitten.com/$size/$size",
        image500500 = "https://placekitten.com/$size/$size"
    )

internal fun createMockSimpleUser(index: Int) =
    SimpleUser(createMockImage(index * 10), "Test $index", id = index)

internal fun createMockUser(index: Int) =
    User(displayName = "Test $index", id = index, photo = createMockImage(index * 10))

internal fun createMockSimpleUserList(count: Int) = buildList {
    for (i in 1..count + 1) {
        add(createMockSimpleUser(i))
    }
}

internal fun createMockConversation(index: Int, myConversation: Boolean) = Conversation(
    index, "$index",
    createMockSimpleUserList(abs(5 - index)), "Test$index", myConversation, abs(5 - index)
)

internal fun createMockConversations(count: Int, myConversation: Boolean) = buildList {
    for (i in 1..count + 1) {
        add(createMockConversation(i, myConversation))
    }
}

internal fun createMockMessage(index: Int) = ConversationInfo.ConversationMessage(
    ROUTABLE,
    index.toString(),
    index % 3 == 0,
    "$index",
    "$index",
    "Test$index",
    createMockUser(index),
    Date()
)

internal fun createMockMessages(count: Int) = buildList {
    for (i in 1..count + 1) {
        add(createMockMessage(i))
    }
}

internal fun createMockSymptoms(count: Int) = buildList<Symptom> {
    for (i in 1..count + 1) {
        add(
            Symptom(
                i,
                "Test Name with very long name for testing symptom cell $i",
                description = LoremIpsum(i * 10).toString(),
                symptomId = i
            )
        )
    }
}

internal fun createMockSymptomsSummary(count: Int) = buildList<SymptomSummary> {
    for (i in 1..count + 1) {
        add(
            SymptomSummary(
                symptom = Symptom(
                    i,
                    name = "Test Name $i",
                    description = LoremIpsum(i * 10).toString(),
                    symptomId = i,
                    icon = createMockImage(i * 10)
                ), i
            )
        )
    }
}

internal fun createMockIdealSleep() = SleepSummaryScreen.IdealSleep(
    "8h 30m",
    1,
    "Ideal Hours for Sleep",
    R.drawable.ic_ideal_sleep
)

internal fun createMockSleepResult() = SleepSummaryScreen.SleepResult(
    "You almost reach a perfect week of sleep",
    CircleChartDataSet(
        (7 * 60 + 10) / (12 * 60f),
        bgColor = lightOlivaceous.copy(alpha = 0.2f),
        textColor = lightOlivaceous,
        valueColor = lightOlivaceous,
        formattedValue = "7h 10m"
    )
)

internal fun createMockSleepInformation() = SleepSummaryScreen.SleepInformation(
    "4h 33m",
    "4h 00m",
    "2h 00m",
    "15h 27m",
)

internal fun createMockDailySleepDetails() = SleepDetailScreen.DailyInformation(
    "4h 33m",
    "4h 00m",
    "2h 00m",
    "15h 27m",
    "8h 33m"
)

internal fun createMockPeriodInfo() = SleepDetailScreen.PeriodInfo(
    "May 17, 2022",
    "Today",
    true,
    false,
)

internal fun createMockWeeklySleepDetails() = SleepDetailScreen.WeeklyInformation(
    BarChartDataSet(
        values = listOf(
            SleepChartEntry(Date(1658139756000), 4 * 60 * 60, 4 * 60 * 60, 4 * 60 * 60),
            SleepChartEntry(Date(1658226156000), 4 * 60 * 60, 3 * 60 * 60, 4 * 60 * 60),
            SleepChartEntry(Date(1658312556000), 3 * 60 * 60, 3 * 60 * 60, 4 * 60 * 60),
            SleepChartEntry(
                Date(1658398956000),
                (2.3 * 60 * 60).toInt(),
                (2.1 * 60 * 60).toInt(),
                (2.1 * 60 * 60).toInt()
            ),
            SleepChartEntry(
                Date(1658485356000),
                (2.3 * 60 * 60).toInt(),
                (2.1 * 60 * 60).toInt(),
                (2.1 * 60 * 60).toInt()
            ),
            SleepChartEntry(Date(1658571756000), 3 * 60 * 60, 3 * 60 * 60, 4 * 60 * 60),
            SleepChartEntry(Date(1658658156000), 2 * 60 * 60, 2 * 60 * 60, 4 * 60 * 60),
        ),
        yAxisFormatter = { it.toInt().toString() },
        xAxisFormatter = {
            SimpleDateFormat("EE", Locale.US)
                .format((it as? SleepChartEntry)?.date ?: Date())
        },
        valueFormatter = { "%.1f".format(it.value) },
        cloudFormatter = {
            "1h 10m" to SimpleDateFormat(
                "hh'h' mm'm'",
                Locale.getDefault()
            ).format(Date(it.total.toInt() * 1000L))
        }
    ),
    "7h 10m"
)

internal fun createMockMonthlySleepDetails() = SleepDetailScreen.MonthlyInformation(
    BarChartDataSet(
        values = listOf(
            SleepChartEntry(Date(1658139756000), 4 * 60 * 60, 4 * 60 * 60, 4 * 60 * 60),
        ),
        yAxisFormatter = { it.toInt().toString() },
        xAxisFormatter = {
            SimpleDateFormat("EE", Locale.US)
                .format((it as? SleepChartEntry)?.date ?: Date())
        },
        valueFormatter = { "%.1f".format(it.value) },
        cloudFormatter = {
            "1h 10m" to SimpleDateFormat(
                "hh'h' mm'm'",
                Locale.getDefault()
            ).format(Date(it.total.toInt() * 1000L))
        }
    ),
    "7h 10m",
    "58%",
    "32%",
    "10%"
)

internal fun createMockStepsInformation() =
    ActivityScreen.Steps(value = "6 098", data = listOf(0.6f, 0.7f, 0.8f, 0.5f, 0.4f, 0.5f, 0.7f))

internal fun createMockActivityInformation() =
    ActivityScreen.Activity(
        value = "68 min",
        data = listOf(0.6f, 0.7f, 0.8f, 0.5f, 0.4f, 0.5f, 0.7f)
    )

internal fun createMockHRInformation() =
    ActivityScreen.HeartRate(
        value = "95 bps",
        data = listOf(0.6f, 0.7f, 0.8f, 0.5f, 0.4f, 0.5f, 0.7f)
    )

internal fun createMockHRVInformation() =
    ActivityScreen.HeartRateVariability(
        value = "",
        data = listOf(0.6f, 0.7f, 0.8f, 0.5f, 0.4f, 0.5f, 0.7f)
    )