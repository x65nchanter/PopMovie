package com.example.popmovie.di


import dagger.MapKey
import javax.inject.Scope
import kotlin.reflect.KClass
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import dagger.hilt.DefineComponent
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * @source https://github.com/airbnb/mavericks/tree/master/hellohilt
 */

/**
 * This factory allows Mavericks to supply the initial or restored [MavericksState] to Hilt.
 *
 * Add this interface inside of your [MavericksViewModel] class then create the following Hilt module:
 *
 * @Module
 * @InstallIn(MavericksViewModelComponent::class)
 * interface ViewModelsModule {
 *   @Binds
 *   @IntoMap
 *   @ViewModelKey(MyViewModel::class)
 *   fun myViewModelFactory(factory: MyViewModel.Factory): AssistedViewModelFactory<*, *>
 * }
 *
 * If you already have a ViewModelsModule then all you have to do is add the multibinding entry for your new [MavericksViewModel].
 */
interface AssistedViewModelFactory<VM : MavericksViewModel<S>, S : MavericksState> {
    fun create(state: S): VM
}

/**
 * To connect Mavericks ViewModel creation with Hilt's dependency injection, add the following Factory and companion object to your MavericksViewModel.
 *
 * Example:
 *
 * class MyViewModel @AssistedInject constructor(...): MavericksViewModel<MyState>(...) {
 *
 *   @AssistedFactory
 *   interface Factory : AssistedViewModelFactory<MyViewModel, MyState> {
 *     ...
 *   }
 *
 *   companion object : MavericksViewModelFactory<MyViewModel, MyState> by hiltMavericksViewModelFactory()
 * }
 */

inline fun <reified VM : MavericksViewModel<S>, S : MavericksState> hiltMavericksViewModelFactory() = HiltMavericksViewModelFactory<VM, S>(VM::class.java)

class HiltMavericksViewModelFactory<VM : MavericksViewModel<S>, S : MavericksState>(
    private val viewModelClass: Class<out MavericksViewModel<S>>
) : MavericksViewModelFactory<VM, S> {

    override fun create(viewModelContext: ViewModelContext, state: S): VM {
        // We want to create the ViewModelComponent. In order to do that, we need to get its parent: ActivityComponent.
        val componentBuilder = EntryPoints.get(viewModelContext.app(), CreateMavericksViewModelComponent::class.java).mavericksViewModelComponentBuilder()
        val viewModelComponent = componentBuilder.build()
        val viewModelFactoryMap = EntryPoints.get(viewModelComponent, HiltMavericksEntryPoint::class.java).viewModelFactories
        val viewModelFactory = viewModelFactoryMap[viewModelClass]

        @Suppress("UNCHECKED_CAST")
        val castedViewModelFactory = viewModelFactory as? AssistedViewModelFactory<VM, S>
        return castedViewModelFactory?.create(state) as VM
    }
}

/**
 * Hilt's ViewModelComponent's parent is ActivityRetainedComponent but there is no easy way to access it. SingletonComponent should be sufficient
 * because the ViewModel that gets created is the only object with a reference to the created component so the lifecycle of it will
 * still be correct.
 */
@MavericksViewModelScoped
@DefineComponent(parent = SingletonComponent::class)
interface MavericksViewModelComponent

@DefineComponent.Builder
interface MavericksViewModelComponentBuilder {
    fun build(): MavericksViewModelComponent
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface CreateMavericksViewModelComponent {
    fun mavericksViewModelComponentBuilder(): MavericksViewModelComponentBuilder
}

@EntryPoint
@InstallIn(MavericksViewModelComponent::class)
interface HiltMavericksEntryPoint {
    val viewModelFactories: Map<Class<out MavericksViewModel<*>>, AssistedViewModelFactory<*, *>>
}

/**
 * Scope annotation for bindings that should exist for the life of an MavericksViewModel.
 */
@Scope
annotation class MavericksViewModelScoped

/**
 * A [MapKey] for populating a map of ViewModels and their factories.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@MapKey
annotation class ViewModelKey(val value: KClass<out MavericksViewModel<*>>)
