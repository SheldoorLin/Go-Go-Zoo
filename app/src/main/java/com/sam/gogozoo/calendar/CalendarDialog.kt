package com.sam.gogozoo.calendar

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.kd.dynamic.calendar.generator.ImageGenerator
import com.sam.gogozoo.MainActivity
import com.sam.gogozoo.R
import com.sam.gogozoo.ZooApplication
import com.sam.gogozoo.data.Control
import com.sam.gogozoo.data.MockData
import com.sam.gogozoo.data.NavInfo
import com.sam.gogozoo.data.area.LocalArea
import com.sam.gogozoo.databinding.DialogCalendarBinding
import com.sam.gogozoo.ext.getVmFactory
import com.sam.gogozoo.util.Logger
import java.util.*

class CalendarDialog : AppCompatDialogFragment(), DatePickerDialog.OnDateSetListener {

    private val viewModel by viewModels<CalendarViewModel> { getVmFactory() }
    lateinit var binding: DialogCalendarBinding
    lateinit var mCurrentDate: Calendar
    lateinit var mGeneratedDateIcon: Bitmap
    lateinit var mImageGenerator: ImageGenerator
    lateinit var mDisplayGeneratedImage: ImageView
    lateinit var textDate: TextView
    lateinit var adapter: CalendarAdapter

    companion object {
        fun newInstance() = CalendarDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            DialogFragment.STYLE_NO_FRAME,
            R.style.LoginDialog
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate the layout for this fragment
        binding = DialogCalendarBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.calendarDialog.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_enter))

        mImageGenerator = ImageGenerator(context)
        mDisplayGeneratedImage = binding.imageGen
        textDate = binding.textDate

        viewModel.leave.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                Log.d("sam","leave=$it")
                dismiss()
                viewModel.onLeaveCompleted()
            }
        })

        mImageGenerator.setIconSize(50,50)
        mImageGenerator.setDateSize(30F)
        mImageGenerator.setMonthSize(10F)
        mImageGenerator.setDatePosition(42)
        mImageGenerator.setMonthPosition(14)
        mImageGenerator.setDateColor(Color.parseColor("#3c6eaf"))
        mImageGenerator.setMonthColor(Color.WHITE)
        mImageGenerator.setStorageToSDCard(true)

        val today = Calendar.getInstance()
        viewModel.currentCarlendar.value = today


        textDate.setOnClickListener {
            mCurrentDate = Calendar.getInstance()
            val year = mCurrentDate.get(Calendar.YEAR)
            val month = mCurrentDate.get(Calendar.MONTH)
            val day = mCurrentDate.get(Calendar.DAY_OF_MONTH)
            val mDatePicker = DatePickerDialog((activity as MainActivity), this, year, month, day)
            mDatePicker.show()
        }

        mDisplayGeneratedImage.setOnClickListener {
            Logger.d("mDisplayGeneratedImageClicked")
            val now = Calendar.getInstance()
            viewModel.currentCarlendar.value = now
        }

        adapter = CalendarAdapter(viewModel)
        binding.rcyCalendar.adapter = adapter

        viewModel.currentCarlendar.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            mGeneratedDateIcon = mImageGenerator.generateDateImage(it, R.drawable.empty_calendar)
            mDisplayGeneratedImage.setImageBitmap(mGeneratedDateIcon)
            val dayOfMonth = it.get(Calendar.DAY_OF_MONTH)
            val month = it.get(Calendar.MONTH)
            val year = it.get(Calendar.YEAR)
            viewModel.getListCalendar(it)
            textDate.text = "$year-${month+1}-$dayOfMonth"
            (binding.rcyCalendar.adapter as CalendarAdapter).notifyDataSetChanged()
        })

        viewModel.selectCalendars.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Logger.d("selectCalendars=$it")
            (binding.rcyCalendar.adapter as CalendarAdapter).submitList(it)
        })

        viewModel.selectLocalCalendar.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            var navInfo = NavInfo(it.title, it.geo[0])
            val areaList = MockData.localAreas.filter { area ->
                area.name == it.location
            }
            if (areaList != listOf<LocalArea>()){
                navInfo.imageUrl = areaList[0].picture
            }else{
                navInfo.image = R.drawable.icon_house
            }

            (activity as MainActivity).info.value = navInfo
            (activity as MainActivity).markInfo.value = navInfo
            Control.hasPolyline = false
            dismiss()
        })


        binding.buttonRight.setOnClickListener {
            val current = viewModel.currentCarlendar.value
            current?.let {
                val day = it.get(Calendar.DAY_OF_MONTH)
                it.set(Calendar.DATE, day+1)
                viewModel.currentCarlendar.value = current
            }
        }

        binding.buttonLeft.setOnClickListener {
            val current = viewModel.currentCarlendar.value
            current?.let {
                val day = it.get(Calendar.DAY_OF_MONTH)
                it.set(Calendar.DATE, day-1)
                viewModel.currentCarlendar.value = it
            }
        }


        return binding.root
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        mCurrentDate.set(year, month, dayOfMonth)
        viewModel.currentCarlendar.value = mCurrentDate
    }

    override fun dismiss() {
        binding.calendarDialog.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_out))
        Handler().postDelayed({ super.dismiss() }, 200)
    }
}
