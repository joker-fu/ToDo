package com.joker.core.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*

/**
 * ArchitectureExt
 *
 * @author joker
 * @date 2019/1/22.
 */

inline fun <reified VM : ViewModel> FragmentActivity.createViewModel(): VM {
    return ViewModelProviders.of(this).get(VM::class.java)
}

inline fun <reified VM : ViewModel> Fragment.createViewModel(): VM {
    return ViewModelProviders.of(this).get(VM::class.java)
}

inline fun <reified VM : ViewModel> Fragment.createViewModelByActivity(): VM {
    return ViewModelProviders.of(requireActivity()).get(VM::class.java)
}


inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline action: (value: T) -> Unit) {
    observe(owner, Observer {
        action(it)
    })
}