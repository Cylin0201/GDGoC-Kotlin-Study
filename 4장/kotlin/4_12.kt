internal open class TalkativeButton{
    private fun yell() = println("Hey!")
    protected fun whisper() = println("Let's talk!")
}

fun TalkativeButton.giveSpeech(){   //오류 발생
    yell()
    whisper()
}
