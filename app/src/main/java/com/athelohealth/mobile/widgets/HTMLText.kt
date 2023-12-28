package com.athelohealth.mobile.widgets

import android.graphics.Typeface
import android.text.style.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import com.athelohealth.mobile.presentation.ui.theme.link
import java.io.File

private const val URL_TAG = "url_tag"
private const val bullet = "\u2022"

@Composable
fun HtmlText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    softWrap: Boolean = true,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    linkClicked: ((String) -> Unit)? = null,
    fontSize: TextUnit = 14.sp,
    flags: Int = HtmlCompat.FROM_HTML_MODE_LEGACY,
    URLSpanStyle: SpanStyle = SpanStyle(
        color = MaterialTheme.typography.link.color,
        textDecoration = TextDecoration.Underline
    )
) {
    val content = text.cleanHtml().asHTML(fontSize, flags, URLSpanStyle)
    if (linkClicked != null) {
        ClickableText(
            modifier = modifier,
            text = content,
            style = style,
            softWrap = softWrap,
            overflow = overflow,
            maxLines = maxLines,
            onTextLayout = onTextLayout,
            onClick = {
                content
                    .getStringAnnotations(URL_TAG, it, it)
                    .firstOrNull()
                    ?.let { stringAnnotation -> linkClicked(stringAnnotation.item) }
            }
        )
    } else {
        Text(
            modifier = modifier,
            text = content,
            style = style,
            softWrap = softWrap,
            overflow = overflow,
            maxLines = maxLines,
            onTextLayout = onTextLayout
        )
    }

}

fun String.cleanHtml(): String {
    return this.replace("<li>", "<li_>")
        .replace("</li>", "</li_>")
        .replace("<ul>", "<ul_>")
        .replace("</ul>", "</ul_>")
}

@Composable
private fun String.asHTML(
    fontSize: TextUnit,
    flags: Int,
    URLSpanStyle: SpanStyle
) = buildAnnotatedString {
    var parent = ""
    var number = 1
    val spanned = HtmlCompat.fromHtml(
        this@asHTML, flags, null
    ) { opening, tag, output, _ ->
        if (tag == "ul_") {
            parent = "ul"
            if (!opening)
                output.append("\n")
        } else if (tag == "ol") {
            parent = "ol"
            if (!opening)
                number = 1
        }
        if (tag == "li_") {
            when (parent) {
                "ol" -> if (opening) output.append("\t$number.\t")
                    .also { number++ } else output.append("\n")
                "ul" -> if (opening) output.append("\t$bullet\t") else output.append("\n")
            }
        }
    }
    val spans = spanned.getSpans(0, spanned.length, Any::class.java)

    append(spanned.toString().trim())

    spans
        .filter { it !is BulletSpan }
        .forEach { span ->
            val start = spanned.getSpanStart(span)
            val end = spanned.getSpanEnd(span)
            when (span) {
                is RelativeSizeSpan -> span.spanStyle(fontSize)
                is StyleSpan -> span.spanStyle()
                is UnderlineSpan -> span.spanStyle()
                is ForegroundColorSpan -> span.spanStyle()
                is TypefaceSpan -> span.spanStyle()
                is StrikethroughSpan -> span.spanStyle()
                is SuperscriptSpan -> span.spanStyle()
                is SubscriptSpan -> span.spanStyle()
                is URLSpan -> {
                    addStringAnnotation(
                        tag = URL_TAG,
                        annotation = span.url,
                        start = start,
                        end = end
                    )
                    URLSpanStyle
                }
                else -> {
                    null
                }
            }?.let { spanStyle ->
                addStyle(spanStyle, start, end)
            }
        }
}


private const val PATH_SYSTEM_FONTS_FILE = "/system/etc/fonts.xml"
private const val PATH_SYSTEM_FONTS_DIR = "/system/fonts/"

internal fun RelativeSizeSpan.spanStyle(fontSize: TextUnit): SpanStyle =
    SpanStyle(fontSize = (fontSize.value * sizeChange).sp)

internal fun StyleSpan.spanStyle(): SpanStyle? = when (style) {
    Typeface.BOLD -> SpanStyle(fontWeight = FontWeight.Bold)
    Typeface.ITALIC -> SpanStyle(fontStyle = FontStyle.Italic)
    Typeface.BOLD_ITALIC -> SpanStyle(
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic,
    )
    else -> null
}

internal fun SubscriptSpan.spanStyle(): SpanStyle =
    SpanStyle(baselineShift = BaselineShift.Subscript)

internal fun SuperscriptSpan.spanStyle(): SpanStyle =
    SpanStyle(baselineShift = BaselineShift.Superscript)

internal fun TypefaceSpan.spanStyle(): SpanStyle? {
    val xmlContent = File(PATH_SYSTEM_FONTS_FILE).readText()
    return if (xmlContent.contains("""<family name="$family""")) {
        val familyChunkXml = xmlContent.substringAfter("""<family name="$family""")
            .substringBefore("""</family>""")
        val fontName = familyChunkXml.substringAfter("""<font weight="400" style="normal">""")
            .substringBefore("</font>")
        SpanStyle(fontFamily = FontFamily(Typeface.createFromFile("$PATH_SYSTEM_FONTS_DIR$fontName")))
    } else {
        null
    }
}

internal fun UnderlineSpan.spanStyle(): SpanStyle =
    SpanStyle(textDecoration = TextDecoration.Underline)

internal fun ForegroundColorSpan.spanStyle(): SpanStyle =
    SpanStyle(color = Color(foregroundColor))

internal fun StrikethroughSpan.spanStyle(): SpanStyle =
    SpanStyle(textDecoration = TextDecoration.LineThrough)