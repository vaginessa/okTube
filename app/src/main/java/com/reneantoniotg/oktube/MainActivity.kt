package com.reneantoniotg.oktube

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.onesignal.OneSignal

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.net.URI
import android.content.DialogInterface
import android.R.string.cancel
import android.app.AlertDialog
import android.app.PendingIntent.getActivity
import android.content.Context
import kotlinx.android.synthetic.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import android.widget.Toast




class MainActivity : AppCompatActivity() {

    lateinit var selectedFormat : String // Declare variable for storing the selected format to download

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        // Enable Quality Options Spinner

        qualityOptions()

        // Download button

        var download_button = findViewById<Button>(R.id.download)
        download_button.setOnClickListener{
            download()
        }

    }

    fun qualityOptions() {
        var video_format = findViewById<Spinner>(R.id.video_format)
        val formatlist = arrayOf("MP4", "MP3", "M4A")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, formatlist)
        video_format.setAdapter(adapter)

        video_format.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                if (video_format.selectedItem.equals("MP4")){
                    selectedFormat = "mp4"
                } else if (video_format.selectedItem.equals("MP3")) {
                    selectedFormat = "mp3"
                } else if (video_format.selectedItem.equals("M4A")) {
                    selectedFormat = "m4a"
                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>) {
                return
            }
        }
    }

    fun download () {

        var yt_id_edittext = findViewById<EditText>(R.id.videoid)
        var yt_id: String = yt_id_edittext.text.toString()
        var api_url = "https://lolyoutube.com/download/"

        if (yt_id.isEmpty()) {
            val text = "Please, enter the YouTube video ID!"
            val duration = Toast.LENGTH_LONG
            val toast = Toast.makeText(applicationContext, text, duration).show()
        } else if (yt_id.isNotEmpty()) {

            var dialogText1 = getString(R.string.download_dialog)
            var dialogText2 = getString(R.string.download_dialog2)
            var alertText = dialogText1 + selectedFormat + dialogText2
            alert("" + alertText) {
                yesButton {
                    val epoch_time = System.currentTimeMillis() / 1000
                    var apiURL_string = api_url + " " + selectedFormat + "/" + yt_id + "/" + epoch_time
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(apiURL_string)))
                }
                }.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_credits -> {
                var aboutme = getString(R.string.reneantonio)
                alert(aboutme) {
                    title = ""
                    positiveButton("YouTube") { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/ReAnTechYT"))) }
                    negativeButton("Twitter") { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/reneantoniotg"))) }
                }.show()
                true
            }
            R.id.action_licenses -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.licensesURL))))
                true
            }
            R.id.action_help -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.helpURL))))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
