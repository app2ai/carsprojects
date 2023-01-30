package com.sevenpeakssoftware.vishalr.repo

interface DataConvertor<K, T> {
    fun intoUiModel(dataClass: K?): List<T>
}
