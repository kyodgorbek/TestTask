package com.example.testtask.root.mapperBase

interface Mapper<T, R> {

    operator fun invoke(input: T): R

}