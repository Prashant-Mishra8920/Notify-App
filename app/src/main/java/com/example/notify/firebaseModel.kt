package com.example.notify

class firebaseModel {
    var title: String? = null
    var content: String? = null
    var date: String? = null
    var color: String? = null

    constructor() {}
    constructor(title: String?, content: String?, date: String?, color: String?) {
        this.title = title
        this.content = content
        this.date = date
        this.color = color
    }
}