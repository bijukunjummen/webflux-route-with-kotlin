package org.bk.routesample.domain

data class Message(var id: String, var payload: String) {
    constructor() : this("", "")
}