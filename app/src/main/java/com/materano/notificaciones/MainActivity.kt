package com.materano.notificaciones

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.materano.notificaciones.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private val alarmID = 1
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val settings =  getSharedPreferences(
            "Preferencias",
            Context.MODE_PRIVATE
        )
        //peuebas
        var hour: String?
        var minute: String?

        hour = settings.getString("hour", "")
        minute = settings.getString("minute", "")

        if (hour!!.length > 0) {
            binding.notificationsTime.setText("$hour:$minute")
        }
        binding.changeNotification.setOnClickListener {
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(this@MainActivity,
                { timePicker, selectedHour, selectedMinute ->
                    var finalHour: String
                    var finalMinute: String
                    finalHour = "" + selectedHour
                    finalMinute = "" + selectedMinute
                    if (selectedHour < 10) finalHour = "0$selectedHour"
                    if (selectedMinute < 10) finalMinute = "0$selectedMinute"
                    binding.notificationsTime.setText("$finalHour:$finalMinute")
                    val today = Calendar.getInstance()
                    today[Calendar.HOUR_OF_DAY] = selectedHour
                    today[Calendar.MINUTE] = selectedMinute
                    today[Calendar.SECOND] = 0
                    val edit = settings.edit()
                    edit.putString("hour", finalHour)
                    edit.putString("minute", finalMinute)

                    //SAVE ALARM TIME TO USE IT IN CASE OF REBOOT
                    edit.putInt("alarmID", alarmID)
                    edit.putLong("alarmTime", today.timeInMillis)
                    edit.commit()
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.changed_to, "$finalHour:$finalMinute"),
                        Toast.LENGTH_LONG
                    ).show()
                    Utils.setAlarm(alarmID, today.timeInMillis, this@MainActivity)
                }, hour, minute, true
            ) //Yes 24 hour time

            mTimePicker.setTitle(getString(R.string.select_time))
            mTimePicker.show()
        }
    }
}