package com.example.uijp.view.gulaDarah.VM//package com.example.uijp
//
//import androidx.lifecycle.ViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//
//data class GulaDarahData(
//    val tanggal: String,
//    val jam: String,
//    val nilai: Int
//)
//
//class GulaDarahViewModel : ViewModel() {
//
//    // Simpan list data gula darah di memori
//    private val _gulaDarahList = MutableStateFlow<List<GulaDarahData>>(emptyList())
//    val gulaDarahList: StateFlow<List<GulaDarahData>> get() = _gulaDarahList
//
//    // Fungsi insert data ke list lokal
//    fun insertGulaDarah(tanggal: String, jam: String, nilai: Int) {
//        val currentList = _gulaDarahList.value.toMutableList()
//        currentList.add(GulaDarahData(tanggal, jam, nilai))
//        _gulaDarahList.value = currentList
//        }
//}