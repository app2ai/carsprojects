package com.sevenpeakssoftware.vishalr.repo

// Generic data convertor interface
// K is API MODEL and T is UI DATA MODEL
interface DataConvertor<K, T> {
    fun intoUiModel(dataClass: K?): List<T>
}
