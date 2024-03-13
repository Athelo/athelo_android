package com.athelohealth.mobile.utils.contentful

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.athelohealth.mobile.presentation.model.news.Category
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
import com.athelohealth.mobile.presentation.model.news.NewsData
import com.contentful.java.cda.CDATag
import timber.log.Timber


class ContentfulClient(
    spaceId: String,
    spaceToken: String
) {

    companion object {
        private val categorySet: MutableSet<Category> = mutableSetOf()
        private val tags: MutableSet<String> = mutableSetOf()
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
                    TextView(context).apply {
                        this.text = AndroidProcessor.creatingCharSequences().process(AndroidContext(context), node)
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

    fun getAllNews(): List<NewsData> {
        val content = contentClient.fetch(CDAEntry::class.java)
            .withContentType("articleContentModel").all()
        tags.clear()
        categorySet.clear()
        val newsData = mutableListOf<NewsData>().apply {
            for (entry in content.entries()) {
                val tagList = entry.value.metadata().tags.mapNotNull { it.id() }
                tags.addAll(tagList)
                add(NewsData.getNewsData(entry.key, entry.value))
            }
        }
        val categories = fetchCategories(tagSet = tags)
        categorySet.addAll(categories)
        return newsData
    }

    fun fetchCategories() = categorySet.toList()

    private fun fetchCategories(tagSet: Set<String>): List<Category> {
        val tagData = contentClient.fetch(CDATag::class.java).all().items().mapNotNull {
            try {
                val tag = it as CDATag
                Category(tag.id(), tag.name())
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }
        }
        return tagData.filter { tagSet.contains(it.id) }.sortedBy { it.name }
    }

    fun getNewsById(id: String): NewsData {
        val entry: CDAEntry = contentClient.fetch(CDAEntry::class.java)
            .one(id)
        return NewsData.getNewsData(id, entry)
    }
}
