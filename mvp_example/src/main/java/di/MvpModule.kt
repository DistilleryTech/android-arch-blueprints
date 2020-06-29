package di

import com.distillery.android.domain.FakeToDoRepository
import org.koin.dsl.module

val mvpModule = module{
    single { FakeToDoRepository(get()) }
}
