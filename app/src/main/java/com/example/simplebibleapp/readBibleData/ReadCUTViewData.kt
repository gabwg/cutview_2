package com.example.simplebibleapp.readBibleData

import android.content.Context
import com.example.simplebibleapp.R
import com.example.simplebibleapp.dataClasses.BookDetails
import org.xmlpull.v1.XmlPullParser


class ReadCUTViewData (private var context: Context) : ReadBibleData {
    val cutview_xml_ids : List<Int> = listOf(
        R.xml.cutview_genesis,
        R.xml.cutview_exodus,
        R.xml.cutview_leviticus,
        R.xml.cutview_numbers,
        R.xml.cutview_deuteronomy,
        R.xml.cutview_joshua,
        R.xml.cutview_judges,
        R.xml.cutview_ruth,
        R.xml.cutview_samuel1,
        R.xml.cutview_samuel2,
        R.xml.cutview_kings1,
        R.xml.cutview_kings2,
        R.xml.cutview_chronicles1,
        R.xml.cutview_chronicles2,
        R.xml.cutview_ezra,
        R.xml.cutview_nehemiah,
        R.xml.cutview_esther,
        R.xml.cutview_job,
        R.xml.cutview_psalms,
        R.xml.cutview_proverbs,
        R.xml.cutview_ecclesiastes,
        R.xml.cutview_songofsolomon,
        R.xml.cutview_isaiah,
        R.xml.cutview_jeremiah,
        R.xml.cutview_lamentations,
        R.xml.cutview_ezekiel,
        R.xml.cutview_daniel,
        R.xml.cutview_hosea,
        R.xml.cutview_joel,
        R.xml.cutview_amos,
        R.xml.cutview_obadiah,
        R.xml.cutview_jonah,
        R.xml.cutview_micah,
        R.xml.cutview_nahum,
        R.xml.cutview_habakkuk,
        R.xml.cutview_zephaniah,
        R.xml.cutview_haggai,
        R.xml.cutview_zechariah,
        R.xml.cutview_malachi,
        R.xml.cutview_matthew,
        R.xml.cutview_mark,
        R.xml.cutview_luke,
        R.xml.cutview_john,
        R.xml.cutview_acts,
        R.xml.cutview_romans,
        R.xml.cutview_corinthians1,
        R.xml.cutview_corinthians2,
        R.xml.cutview_galatians,
        R.xml.cutview_ephesians,
        R.xml.cutview_philippians,
        R.xml.cutview_colossians,
        R.xml.cutview_thessalonians1,
        R.xml.cutview_thessalonians2,
        R.xml.cutview_timothy1,
        R.xml.cutview_timothy2,
        R.xml.cutview_titus,
        R.xml.cutview_philemon,
        R.xml.cutview_hebrews,
        R.xml.cutview_james,
        R.xml.cutview_peter1,
        R.xml.cutview_peter2,
        R.xml.cutview_john1,
        R.xml.cutview_john2,
        R.xml.cutview_john3,
        R.xml.cutview_jude,
        R.xml.cutview_revelation
    )
    val dummy_xml_id = R.xml.dummy_book
    val resources = context.resources
    override fun getBooknamesList() : List<String> {
        return resources.getStringArray(R.array.booknames_tcn).asList()
    }

    override fun getChapterCount(bookname: String): Int {
        // both book names and chapter count are in bible order
        val index = getBooknamesList().indexOf(bookname)
        if (index == -1) {
            return index
        }
        return getChapterCount(index)
    }
    override fun getChapterCount(bookIndex: Int): Int {
        return resources.getIntArray(R.array.chapternumbers)[bookIndex]
    }

    override fun getLanguage(): String {
        return "tcn"
    }

    override fun getChapterFromBook(bookDetails: BookDetails, chapter: Int) : List<String> {
        val index = bookDetails.bookIndex
        if (index == -1) {
            return emptyList()
        }
        if (chapter > getChapterCount(index)) {
            return listOf("ERROR: Chapter $chapter is out of range", bookDetails.bookName, bookDetails.bookIndex.toString(), bookDetails.chapterCount.toString())
        }
        var text = ""
        // parse xml
        val parser : XmlPullParser = resources.getXml(cutview_xml_ids[index])
        var indexChapter = 0
        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            var debugname = ""
            if (eventType == XmlPullParser.START_TAG) {
                debugname = parser.name
            }
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.name == "Chapter") {
                    val index = parser.getAttributeValue(0)
                    indexChapter = index.toInt()
                }
            } else if (eventType == XmlPullParser.TEXT && indexChapter == chapter - 1) {
                text = parser.getText()

            }
            eventType = parser.next()
        }



        var templist = text.replace("\\10", "\n").split("\n")
        val regex = "^\\d+\\s".toRegex()
        templist = templist.map { regex.replace(it, "") }
        return templist
    }
}
// original list copied from cutview app:
// R.xml.genesis, R.xml.exodus, R.xml.leviticus, R.xml.numbers, R.xml.deuteronomy, R.xml.joshua, R.xml.judges, R.xml.ruth, R.xml.samuel1, R.xml.samuel2, R.xml.kings1, R.xml.kings2, R.xml.chronicles1, R.xml.chronicles2, R.xml.ezra, R.xml.nehemiah, R.xml.esther, R.xml.job, R.xml.psalms, R.xml.proverbs, R.xml.ecclesiastes, R.xml.songofsolomon, R.xml.isaiah, R.xml.jeremiah, R.xml.lamentations, R.xml.ezekiel, R.xml.daniel, R.xml.hosea, R.xml.joel, R.xml.amos, R.xml.obadiah, R.xml.jonah, R.xml.micah, R.xml.nahum, R.xml.habakkuk, R.xml.zephaniah, R.xml.haggai, R.xml.zechariah, R.xml.malachi, R.xml.matthew, R.xml.mark, R.xml.luke, R.xml.john, R.xml.acts, R.xml.romans, R.xml.corinthians1, R.xml.corinthians2, R.xml.galatians, R.xml.ephesians, R.xml.philippians, R.xml.colossians, R.xml.thessalonians1, R.xml.thessalonians2, R.xml.timothy1, R.xml.timothy2, R.xml.titus, R.xml.philemon, R.xml.hebrews, R.xml.james, R.xml.peter1, R.xml.peter2, R.xml.john1, R.xml.john2, R.xml.john3, R.xml.jude, R.xml.revelation
