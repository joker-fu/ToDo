package com.joker.core.utils

import java.lang.reflect.ParameterizedType

/**
 * GenericUtils
 *
 * @author joker
 * @date 2019/2/12
 */
@Suppress("UNCHECKED_CAST")
object GenericUtils {

    fun <T> getSuperclassType(any: Any, i: Int): Class<T>? {
        try {
            //先得到类的字节码
            val aClass = any.javaClass
            val genericSuperclass = aClass.genericSuperclass
            return if (genericSuperclass is ParameterizedType) {
                val actualTypeArguments = genericSuperclass.actualTypeArguments
                return actualTypeArguments[i] as? Class<T>
            } else {
                null
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        return null
    }

    fun <T> getInterfaceType(any: Any, i: Int): Class<T>? {
        try {
            val aClass = any.javaClass//先得到类的字节码
            /*
                返回表示由此对象表示的类或接口直接实现的接口的类型。

                如果超级接口是一个参数化类型，返回的Type对象必须准确地反映源代码中使用的实际类型参数。
                代表每个超级界面的参数化类型是在之前没有创建的情况下创建的。
                有关参数化类型的创建过程的语义，请参阅ParameterizedType的声明。

                如果此对象表示一个类，则返回值是一个包含表示由类实现的所有接口的对象的数组。
                数组中接口对象的顺序对应于该对象表示的类的声明的implements子句中的接口名称的顺序。
                在数组类中，接口Cloneable和Serializable按照这个顺序返回。

                如果此对象表示一个接口，则该数组包含表示由该接口直接扩展的所有接口的对象。
                数组中接口对象的顺序对应于该对象表示的接口声明的extends子句中接口名称的顺序。

                如果此对象表示不实现接口的类或接口，则该方法返回长度为0的数组。
                如果这个对象表示一个原始类型或void，则该方法返回一个长度为0的数组。
                * */
            val types = aClass.genericInterfaces
            val parameterizedType = types[0] as ParameterizedType
            val actualTypeArguments = parameterizedType.actualTypeArguments
            return actualTypeArguments[i] as? Class<T>
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        return null
    }

    fun forName(className: String): Class<*>? {
        try {
            return Class.forName(className)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        return null
    }
}
