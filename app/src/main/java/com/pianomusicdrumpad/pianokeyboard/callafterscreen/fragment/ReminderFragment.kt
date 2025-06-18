package com.pianomusicdrumpad.pianokeyboard.callafterscreen.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.adapter.RemindersAdapter
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderViews.WPDatePickerAdapter
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderViews.WPHoursPickerAdapter2
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderViews.WPMinutesPickerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderDb.ReminderData
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderDb.ReminderDatabase
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderDb.ReminderRepo
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderDb.ReminderViewModel
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.reminderDb.ReminderViewModelFactory
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.service.ReminderReceiver
import com.pianomusicdrumpad.pianokeyboard.databinding.CfsFragmentReminderBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReminderFragment : Fragment() {

    private lateinit var binding: CfsFragmentReminderBinding
    private lateinit var reminderModel: ReminderViewModel
    private var remindersList = listOf<ReminderData>()
    private lateinit var remindersAdapter: RemindersAdapter
    private lateinit var hourAdapter: WPHoursPickerAdapter2
    private lateinit var minuteAdapter: WPMinutesPickerAdapter
    private lateinit var dateAdapter: WPDatePickerAdapter
    private var reminderContent = ""
    private var dateNumber = ""
    private var minuteNumber = ""
    private var hourNumber = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CfsFragmentReminderBinding.inflate(layoutInflater, container, false)
        val database = ReminderDatabase.getDatabase(requireContext())
        val reminderDao = database.reminderDao()
        val repo = ReminderRepo(reminderDao)
        // Initialize ViewModel with ViewModelFactory
        reminderModel = ViewModelProvider(
            this,
            ReminderViewModelFactory(requireActivity().application, repo)
        )[ReminderViewModel::class.java]
        init()
        clickListeners()

        return binding.root
    }

    private fun init() {
        hourAdapter = WPHoursPickerAdapter2()
        minuteAdapter = WPMinutesPickerAdapter()
        dateAdapter = WPDatePickerAdapter()
        binding.datePicker.setAdapter(WPDatePickerAdapter())
        binding.hourPicker.setAdapter(WPHoursPickerAdapter2())
        binding.minutesPicker.setAdapter(WPMinutesPickerAdapter())
        setAdapter()
    }

    private fun clickListeners() {
        binding.apply {
            etCustomMessage.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    reminderContent = p0?.toString()?.trim() ?: ""
                }
            })

            ivSetReminderFAB.setOnClickListener {
                clListAndFabLayout.visibility = View.GONE
                clSetAndSaveReminderLayout.visibility = View.VISIBLE
                binding.datePicker.setAdapter(WPDatePickerAdapter())
                var currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                var currentMinute = Calendar.getInstance().get(Calendar.MINUTE)
                binding.minutesPicker.post {
                    val minutePosition = (currentMinute)
                    Log.e("CurrentTime", "mInitDataOnCreateMethod:1 current minute  $minutePosition")
                    binding.minutesPicker.setPositionValue(minutePosition)
                }

                binding.hourPicker.post {
                    val hourPosition = currentHour
                    Log.e("CurrentTime", "mInitDataOnCreateMethod:1 current hour $hourPosition")
                    binding.hourPicker.setPositionValue(hourPosition)
                }

                etCustomMessage.clearFocus()
                etCustomMessage.text.clear()
            }

            imgCloseReminder.setOnClickListener {
                clListAndFabLayout.visibility = View.VISIBLE
                clSetAndSaveReminderLayout.visibility = View.GONE
                etCustomMessage.clearFocus()
                hideKeyboard(it)
                etCustomMessage.text.clear()
            }

            imgDoneReminder.setOnClickListener {
                val hourPos = binding.hourPicker.getPositionValue()
                val minnutePos = binding.minutesPicker.getPositionValue()
                val datePos = binding.datePicker.getPositionValue()
                hourNumber = hourAdapter.getValue(requireContext(), hourPos)
                minuteNumber = minuteAdapter.getValue(requireContext(), minnutePos)
                dateNumber = dateAdapter.getValue(requireContext(), datePos)
                Log.e("hour", "mOnClickListenerMethod: Date $dateNumber Hour $hourNumber and $minuteNumber minutes")

                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val calendar = Calendar.getInstance()

                try {
                    val selectedDate = dateFormat.parse(dateNumber)
                    calendar.time = selectedDate
                    calendar.set(Calendar.HOUR_OF_DAY, hourNumber.toInt())
                    calendar.set(Calendar.MINUTE, minuteNumber.toInt())
                    calendar.set(Calendar.SECOND, 0)
                } catch (e: Exception) {
                    Log.e("ReminderError", "Invalid date format", e)
                    return@setOnClickListener
                }

                if (calendar.timeInMillis < System.currentTimeMillis()) {
                    Log.e("ReminderError", "Selected time is in the past")
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.please_select_a_future_date_and_time),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                if (!reminderContent.isNullOrEmpty()) {
                    hideKeyboard(it)
                    scheduleReminder(requireContext(), calendar.timeInMillis, reminderContent)
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.fill_the_content_of_the_reminder),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setAdapter() {
        remindersAdapter = RemindersAdapter(this)
        binding.rcvReminders.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rcvReminders.adapter = remindersAdapter
        lifecycleScope.launch {
            reminderModel.getReminders().collectLatest {
                remindersList = it
                remindersAdapter.updateList(it)
                showRecyclerView(it)
                Log.e("remindersList", "setAdapter: Reminders size in ${remindersList.size}")
            }
        }

        Log.e("remindersList", "setAdapter: Reminders size ${remindersList.size}")
    }

    private fun showRecyclerView(it: List<ReminderData>) {
        if (it.isEmpty()) {
            binding.tvNoReminderText.visibility = View.VISIBLE
            binding.rcvReminders.visibility = View.GONE
        } else {
            binding.tvNoReminderText.visibility = View.GONE
            binding.rcvReminders.visibility = View.VISIBLE
        }
    }

    private fun scheduleReminder(context: Context, timeInMillis: Long, reminderContent: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("reminder_content", reminderContent)
            putExtra("reminder_time", timeInMillis)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            timeInMillis.toInt(), // Unique ID for each reminder
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)

        val reminder = ReminderData(
            content = reminderContent,
            reminderData = dateNumber,
            reminderTime = "$hourNumber : $minuteNumber",
            timeMillies = timeInMillis
        )
        CoroutineScope(Dispatchers.IO).launch {
            reminderModel.insertReminder(reminder)
        }
        binding.clListAndFabLayout.visibility = View.VISIBLE
        binding.clSetAndSaveReminderLayout.visibility = View.GONE
        Toast.makeText(context, getString(R.string.reminder_set_successfully), Toast.LENGTH_SHORT).show()
    }

    fun deleteReminder(item: ReminderData) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), ReminderReceiver::class.java)
        intent.putExtra("reminder_content", item.content)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            item.timeMillies.toInt(), // Unique code based on time and date
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }

        lifecycleScope.launch(Dispatchers.IO) {
            reminderModel.deleteReminderById(item.id)
        }

        Toast.makeText(requireContext(), getString(R.string.reminder_deleted), Toast.LENGTH_SHORT).show()
    }
}