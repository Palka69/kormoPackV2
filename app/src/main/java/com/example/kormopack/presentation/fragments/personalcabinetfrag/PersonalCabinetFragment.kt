package com.example.kormopack.presentation.fragments.personalcabinetfrag

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.kormopack.R
import com.example.kormopack.data.SheetsDataRepositoryImp
import com.example.kormopack.databinding.FragmentPersonalCabinetBinding
import com.example.kormopack.domain.personalfrag.CalculateWorkDayUseCase
import com.example.kormopack.domain.personalfrag.InitDayChartUseCase
import com.example.kormopack.domain.personalfrag.SheetsDataUseCase
import com.example.kormopack.presentation.mainactivity.DrawerViewModel
import com.example.kormopack.presentation.mainactivity.KEY_USER_STRING
import com.example.kormopack.presentation.mainactivity.MainAuthViewModel
import com.example.kormopack.presentation.mainactivity.PREF_NAME
import com.example.kormopack.presentation.mainactivity.ToolbarViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.SheetsScopes
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import kotlinx.coroutines.Job
import java.util.Calendar
import kotlin.math.round

class PersonalCabinetFragment : Fragment() {

    private var _binding: FragmentPersonalCabinetBinding? = null
    private val binding get() = _binding!!

    private var cachedValues: List<List<Any>>? = null

    private lateinit var userName: String

    private var hourMap = mutableMapOf<Int, String>()
    private var bonusMap = mutableMapOf<Int, Float>()
    private var workDays = mutableListOf<MutableList<Any>>()

    private val toolbarViewModel:  ToolbarViewModel by activityViewModels()
    private val drawerViewModel:  DrawerViewModel by activityViewModels()
    private val mainAuthViewModel:  MainAuthViewModel by activityViewModels()
    private val personalViewModel: PersonalViewModel by viewModels {
        PersonalViewModelFactory(
            SheetsDataUseCase(SheetsDataRepositoryImp(requireContext())),
            CalculateWorkDayUseCase(),
            InitDayChartUseCase()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalCabinetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drawerViewModel.unlockDrawer()
        toolbarViewModel.changeToolbarColor(0)
        toolbarViewModel.showToolbar()

        val sharPref = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharPref.getString(KEY_USER_STRING, "User")?.let { userName = it }

        mainAuthViewModel.networkStatus.observe(viewLifecycleOwner) {
            if (mainAuthViewModel.networkStatus.value!!) {
                if (mainAuthViewModel.isUserAuthenticated.value!!.isUserAuthenticated) {
                    val account = mainAuthViewModel.isUserAuthenticated.value!!.googleUser
                    if (account != null) {
                        personalViewModel.fetchSheetsData(account)
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Немає інтернет-з'єднання", Toast.LENGTH_SHORT).show()
            }
        }

        personalViewModel.sheetsData.observe(viewLifecycleOwner) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            val startDate = CalendarDay.from(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(
                Calendar.DAY_OF_MONTH))
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            val endDate = CalendarDay.from(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(
                Calendar.DAY_OF_MONTH))
            binding.calendar.addDecorator(DayColorDecorator(startDate, endDate))
            binding.calendar.addDecorator(NightColorDecorator(startDate, endDate))

            if (it != null) {
                personalViewModel.getWorkDays(it, userName)
                workDays = personalViewModel.workDays.value!!
                cachedValues = personalViewModel.sheetsData.value

                binding.calendar.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        binding.calendar.invalidateDecorators()
                        personalViewModel.initDayChart(workDays, bonusMap)

                        binding.calendar.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
            }
        }

        personalViewModel.dayChartData.observe(viewLifecycleOwner) { data ->
            binding.dayChart.isRotationEnabled = false

            var dayHour = data["day"] as Float
            var nightHour = data["night"] as Float
            Log.d("BonusLog2", "2 = $bonusMap")
            var bonus = data["bonus"] as Float

            val entry = ArrayList<PieEntry>()
            entry.add(PieEntry(nightHour, "Нічні"))
            entry.add(PieEntry(dayHour, "Денні"))
            val dataSet = PieDataSet(entry, null)
            dataSet.colors = listOf(
                ContextCompat.getColor(requireContext(), R.color.kormotech_light_blue),
                ContextCompat.getColor(requireContext(), R.color.kormotech_green),
            )
            binding.dayChart.centerText = "${(dayHour + nightHour).toInt()}г"
            val pieData = PieData(dataSet)
            pieData.setValueFormatter(IntegerValueFormatter())
            binding.dayChart.data = pieData
            binding.dayChart.description.textSize = 19f
            binding.dayChart.holeRadius = 34f
            binding.dayChart.transparentCircleRadius = 38f
            binding.dayChart.description.textColor = ContextCompat.getColor(requireContext(), R.color.kormoTech_blue)
            binding.dayChart.description.text = "Бонус: $bonus%"
            binding.dayChart.setCenterTextSize(25f)
            dataSet.valueTextSize = 18f
            dataSet.sliceSpace = 1f
            binding.dayChart.notifyDataSetChanged()
            binding.dayChart.invalidate()

            if (binding.dayCard.findViewById<PieChart>(R.id.day_chart).centerText == "0г") {
                Toast.makeText(requireContext(), "Графік для даного ПІБ не знайдено.", Toast.LENGTH_LONG).show()
            }

            binding.calendar.invalidateDecorators()
        }
    }

    inner class IntegerValueFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return value.toInt().toString()
        }
    }

    inner class DayColorDecorator(
        private val startDate: CalendarDay,
        private val endDate: CalendarDay,
    ) : DayViewDecorator {

        override fun shouldDecorate(day: CalendarDay): Boolean {

            if (cachedValues != null && (day.isAfter(startDate)
                        && day.isBefore(endDate) || day == startDate || day == endDate)) {

                for ((index, list) in workDays.withIndex()) {
                    if (list.size > 0 && list[0] == day.day && list[1] == "День") {
                        hourMap.put(list[0].toString().toInt(), list[3].toString())
                        bonusMap.put(day.day, list[2].toString().toFloat())
                        return true
                    }
                }
            }

            binding.calendarCard.visibility = View.VISIBLE
            binding.dayCard.visibility = View.VISIBLE
            binding.progress.visibility = View.GONE
            return false
        }

        override fun decorate(view: DayViewFacade) {
            ContextCompat.getDrawable(requireContext(), R.drawable.green_background)?.let {
                view.setBackgroundDrawable(it)
            }
        }
    }

    inner class NightColorDecorator(
        private val startDate: CalendarDay,
        private val endDate: CalendarDay,
    ) : DayViewDecorator {

        override fun shouldDecorate(day: CalendarDay): Boolean {

            if (cachedValues != null && (day.isAfter(startDate)
                        && day.isBefore(endDate) || day == startDate || day == endDate)) {
                for ((index, list) in workDays.withIndex()) {
                    if (list.size > 0 && list[0] == day.day && list[1] == "Ніч") {
                        hourMap.put(list[0].toString().toInt(), list[3].toString())
                        bonusMap.put(day.day, list[2].toString().toFloat())
                        return true
                    }
                }
            }

            binding.calendarCard.visibility = View.VISIBLE
            binding.dayCard.visibility = View.VISIBLE
            binding.progress.visibility = View.GONE
            return false
        }

        override fun decorate(view: DayViewFacade) {
            ContextCompat.getDrawable(requireContext(), R.drawable.blue_background)?.let {
                view.setBackgroundDrawable(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}