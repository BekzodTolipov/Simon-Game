package edu.umsl.simonsaysapp

import java.lang.ref.WeakReference
import kotlin.reflect.KClass


class ModelHolder private constructor(){

    private val modelPlayers = HashMap<String, WeakReference<Any?>>()

    companion object{
        @JvmStatic
        val instance = ModelHolder()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> get(classType: KClass<T>): T? {
        val modelPlayer = modelPlayers[classType.java.toString()]
        return modelPlayer?.get() as? T
    }

    fun <T: Any> set(classInstance: T?) {
        modelPlayers[classInstance?.javaClass.toString()] = WeakReference(classInstance as? Any)
    }
}