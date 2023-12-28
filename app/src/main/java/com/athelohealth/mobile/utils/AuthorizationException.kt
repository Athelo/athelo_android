package com.athelohealth.mobile.utils

class AuthorizationException(message: String= "Looks like your user has been logout. Please Log in to use this app") : Throwable(message)