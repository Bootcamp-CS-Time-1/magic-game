package br.com.bootcamp.magicgamecs.models.pojo

data class PageResult<T>(val data: T, val total: Int, val nextPage: Int?)