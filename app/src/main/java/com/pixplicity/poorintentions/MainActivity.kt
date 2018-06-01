package com.pixplicity.poorintentions

import android.content.ContextWrapper
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_main.*
import android.app.ActivityManager
import android.graphics.Color


class MainActivity : AppCompatActivity() {

    companion object {
        private const val KEY_ENABLE_RESOLUTION = "enable_resolution"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName("prefs")
                .setUseDefaultSharedPreference(true)
                .build()

        var enableResolution = Prefs.getBoolean(KEY_ENABLE_RESOLUTION, false)

        if (enableResolution) {
            // Check that the activity was launched correctly
            if (!isTaskRoot
                    && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                    && intent.action == Intent.ACTION_MAIN) {
                // This activity doesn't belong; just the launcher or whatever misbehaving
                showErrorToast()
                finish()
                return
            }
        }

        setContentView(R.layout.activity_main)

        supportActionBar?.setIcon(R.drawable.ic_toolbar_icon)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val taskCount = countTasks()
        tv_task_stack.text = getString(R.string.task_stack, taskCount, resources.getQuantityString(R.plurals.activity, taskCount))
        if (taskCount > 1) tv_task_stack.setTextColor(getColor(R.color.colorPrimaryDark))

        sw_resolution.isChecked = enableResolution
        sw_resolution.setOnCheckedChangeListener { _, isChecked ->
            enableResolution = isChecked
            Prefs.putBoolean(KEY_ENABLE_RESOLUTION, enableResolution)
        }

        bt_continue.setOnClickListener {
            startActivity(Intent(this, DetailActivity::class.java))
        }
    }

    private fun countTasks(): Int {
        val activityManager = getSystemService(ContextWrapper.ACTIVITY_SERVICE) as ActivityManager
        var numOfActivities = 0
        activityManager.appTasks.forEach {
            numOfActivities += it.taskInfo.numActivities
        }
        return numOfActivities
    }

    private fun showErrorToast() {
        Toast(this).apply {
            view = layoutInflater.inflate(R.layout.toast, null)
            duration = Toast.LENGTH_LONG
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }

}
