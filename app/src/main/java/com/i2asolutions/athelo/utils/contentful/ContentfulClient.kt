package com.i2asolutions.athelo.utils.contentful

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.contentful.java.cda.CDAAsset
import com.contentful.java.cda.CDAClient
import com.contentful.java.cda.CDAEntry
import com.contentful.java.cda.Logger
import com.contentful.java.cda.rich.CDARichDocument
import com.contentful.java.cda.rich.CDARichEmbeddedBlock
import com.contentful.java.cda.rich.CDARichHyperLink
import com.contentful.java.cda.rich.CDARichText
import com.contentful.rich.android.AndroidContext
import com.contentful.rich.android.AndroidProcessor
import com.i2asolutions.athelo.extensions.displayAsString
import com.i2asolutions.athelo.extensions.toDate
import com.i2asolutions.athelo.presentation.model.community.CommunityData
import com.i2asolutions.athelo.presentation.model.news.NewsData
import com.i2asolutions.athelo.utils.consts.DATE_FORMAT_FULL
import com.i2asolutions.athelo.utils.consts.DATE_FORMAT_SHORT
import com.i2asolutions.athelo.utils.navigateToInAppBrowser
import timber.log.Timber
import java.util.Date


class ContentfulClient(
    spaceId: String,
    spaceToken: String
) {

    companion object {
        private lateinit var viewProcessor: AndroidProcessor<View>

        private fun updateCallBack(context: Context) {
            if (!this::viewProcessor.isInitialized) return
            viewProcessor.overrideRenderer(
                // Checker
                { _, nodeValue ->
                    nodeValue is CDARichEmbeddedBlock && nodeValue.data is CDAAsset
                },

                // Renderer
                { _, nodeValue ->
                    val data = (nodeValue as CDARichEmbeddedBlock).data as CDAAsset
                    val imageview = ImageView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    }
                    Glide.with(context).load("https:" + data.url()).into(imageview)
                    imageview
                }
            )
            viewProcessor.overrideRenderer(
                // Checker
                { _, nodeValue ->
                    nodeValue is CDARichHyperLink &&
                            nodeValue.content.isNotEmpty() &&
                            nodeValue.content.first() is CDARichText
                },

                // Renderer
                { _, nodeValue ->
                    val node = nodeValue as CDARichHyperLink

                    LinearLayout(context).apply {
                        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        orientation = LinearLayout.HORIZONTAL
                        addView(
                            TextView(context).apply {
                                this.text = AndroidProcessor.creatingCharSequences().process(AndroidContext(context), node)
                            }
                        )
                        setOnClickListener {
                            navigateToInAppBrowser(context, node.data.toString())
                        }
                    }
                }
            )
        }

        fun getDataView(context: Context, node: CDARichDocument): View {
            viewProcessor = AndroidProcessor.creatingNativeViews()
            updateCallBack(context)
            val view = viewProcessor.process(AndroidContext(context), node)
            view?.setPadding(30,30,30,30)
            return view ?: View(context)
        }

        fun getText(context: Context, data: CDARichDocument): String {
            return AndroidProcessor.creatingCharSequences().process(AndroidContext(context), data).toString()
        }
    }


    private val logger = Logger { message ->
        Timber.tag("CONTENTFUL")
            .v(message)
    }
    private var contentClient: CDAClient = CDAClient.builder()
        .setSpace(spaceId)
        .setToken(spaceToken)
        .setLogger(logger)
        .setLogLevel(Logger.Level.BASIC)
        .build()

    fun getAllData(): CDARichDocument {
        val entry: CDAEntry = contentClient.fetch(CDAEntry::class.java)
            .one("5hOYnlXbV9FFDfe1qwtNio")
        return entry.getField("bodyText")
    }

    fun getAllNews(): List<NewsData> {
        val content = contentClient.fetch(CDAEntry::class.java)
            .withContentType("articleContentModel").all()
        return mutableListOf<NewsData>().apply {
            for (entry in content.entries()) {
                add(NewsData.getNewsData(entry.key, entry.value))
            }
        }
    }

    fun getNewsById(id: String): NewsData {
        val entry: CDAEntry = contentClient.fetch(CDAEntry::class.java)
            .one(id)
        return NewsData.getNewsData(id, entry)
    }

    fun getDemoData(): CommunityData {
        val entry: CDAEntry = contentClient.fetch(CDAEntry::class.java)
            .one("5hOYnlXbV9FFDfe1qwtNio")
        return CommunityData.getCommunityData(entry)
    }

}
