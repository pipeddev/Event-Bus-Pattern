package com.pipe.d.dev.eventbuspatt

sealed class SportEvent {
    data class ResultSuccess(val sportKey: Int,
                            val sportName: String,
                            val results: List<String>?,
                            val isWarning: Boolean = false): SportEvent() {
                                fun getImageRes(): Int = when(sportKey) {
                                    1 -> R.drawable.ic_soccer
                                    2 -> R.drawable.ic_weight_lifter
                                    3 -> R.drawable.ic_gymnastics
                                    4 -> R.drawable.ic_water_polo
                                    5 -> R.drawable.ic_baseball_bat
                                    6 -> R.drawable.ic_rugby
                                    7 -> R.drawable.ic_tennis_ball
                                    else -> R.drawable.ic_timer
                                }
                            }
    data class ResultError(val code: Int,
                            val msg: String): SportEvent()
    data object AdEvent: SportEvent()
    data object  SaveEvent: SportEvent()
    data object  CloseAdEvent: SportEvent()
}
