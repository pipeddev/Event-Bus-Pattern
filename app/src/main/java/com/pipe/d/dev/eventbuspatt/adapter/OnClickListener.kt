package com.pipe.d.dev.eventbuspatt.adapter

import com.pipe.d.dev.eventbuspatt.SportEvent


/****
 * Project: Event Bus Pattern
 * From: com.cursosant.eventbuspattern
 * Created by Alain Nicolás Tello on 23/01/24 at 16:38
 * All rights reserved 2024.
 *
 * All my Udemy Courses:
 * https://www.udemy.com/user/alain-nicolas-tello/
 * And Frogames formación:
 * https://cursos.frogamesformacion.com/pages/instructor-alain-nicolas
 *
 * Coupons on my Website:
 * www.alainnicolastello.com
 ***/
interface OnClickListener {
    fun onClick(result: SportEvent.ResultSuccess)
}